use anchor_lang::prelude::*;
use crate::{state::*, errors::*};

#[derive(Accounts)]
#[instruction(subscription_id: String)]
pub struct SubscribeAndPay<'info> {
    #[account(
        mut,
        seeds = [b"payment_intent", payment_intent.intent_id.as_bytes()],
        bump = payment_intent.bump,
        constraint = payment_intent.status == PaymentIntentStatus::Created @ LooprError::InvalidPaymentIntentStatus
    )]
    pub payment_intent: Account<'info, PaymentIntent>,
    
    #[account(
        mut,
        seeds = [b"subscription_plan", payment_intent.plan_id.as_bytes()],
        bump = subscription_plan.bump,
        constraint = subscription_plan.is_active @ LooprError::PlanNotActive
    )]
    pub subscription_plan: Account<'info, SubscriptionPlan>,
    
    #[account(
        init,
        payer = user,
        space = UserSubscription::LEN,
        seeds = [b"user_subscription", user.key().as_ref(), subscription_id.as_bytes()],
        bump
    )]
    pub user_subscription: Account<'info, UserSubscription>,
    
    #[account(mut)]
    pub user: Signer<'info>,
    
    /// CHECK: Authority that receives payment
    #[account(mut)]
    pub authority: AccountInfo<'info>,
    
    #[account(
        mut,
        seeds = [b"global_state"],
        bump = global_state.bump
    )]
    pub global_state: Account<'info, GlobalState>,
    
    pub system_program: Program<'info, System>,
}

pub fn handler(
    ctx: Context<SubscribeAndPay>,
    subscription_id: String,
) -> Result<()> {
    require!(!ctx.accounts.global_state.is_paused, LooprError::ProgramPaused);
    require!(subscription_id.len() <= 64, LooprError::SubscriptionIdTooLong);
    
    let payment_intent = &mut ctx.accounts.payment_intent;
    let subscription_plan = &mut ctx.accounts.subscription_plan;
    let clock = Clock::get()?;
    
    // Check if payment intent is still valid
    require!(
        clock.unix_timestamp <= payment_intent.expires_at,
        LooprError::PaymentIntentExpired
    );
    
    // Check max subscribers limit
    if let Some(max_subscribers) = subscription_plan.max_subscribers {
        require!(
            subscription_plan.current_subscribers < max_subscribers,
            LooprError::MaxSubscribersReached
        );
    }
    
    let amount = payment_intent.amount;
    
    // Transfer SOL from user to authority
    let transfer_instruction = anchor_lang::system_program::Transfer {
        from: ctx.accounts.user.to_account_info(),
        to: ctx.accounts.authority.to_account_info(),
    };
    
    let cpi_ctx = CpiContext::new(
        ctx.accounts.system_program.to_account_info(),
        transfer_instruction,
    );
    
    anchor_lang::system_program::transfer(cpi_ctx, amount)?;
    
    // Create user subscription
    let user_subscription = &mut ctx.accounts.user_subscription;
    user_subscription.user = ctx.accounts.user.key();
    user_subscription.subscription_plan = subscription_plan.key();
    user_subscription.subscription_id = subscription_id;
    user_subscription.is_active = true;
    user_subscription.next_payment_due = clock.unix_timestamp + subscription_plan.period_duration;
    user_subscription.last_payment_date = Some(clock.unix_timestamp);
    user_subscription.auto_pay_enabled = true; // Enable autopay for QR payments
    user_subscription.payment_thread = None;
    user_subscription.total_payments_made = 1;
    user_subscription.created_at = clock.unix_timestamp;
    user_subscription.updated_at = clock.unix_timestamp;
    user_subscription.bump = ctx.bumps.user_subscription;
    
    // Update payment intent
    payment_intent.payer = Some(ctx.accounts.user.key());
    payment_intent.status = PaymentIntentStatus::Completed;
    payment_intent.fulfilled_at = Some(clock.unix_timestamp);
    payment_intent.subscription = Some(user_subscription.key());
    
    // Update subscription plan count
    subscription_plan.current_subscribers = subscription_plan
        .current_subscribers
        .checked_add(1)
        .unwrap();
    subscription_plan.updated_at = clock.unix_timestamp;
    
    // Update global state
    let global_state = &mut ctx.accounts.global_state;
    global_state.total_subscriptions = global_state
        .total_subscriptions
        .checked_add(1)
        .unwrap();
    global_state.total_payments_processed = global_state
        .total_payments_processed
        .checked_add(1)
        .unwrap();
    global_state.total_volume = global_state
        .total_volume
        .checked_add(amount)
        .unwrap();
    
    msg!(
        "QR payment completed: {} lamports for subscription {}",
        amount,
        user_subscription.subscription_id
    );
    
    Ok(())
}