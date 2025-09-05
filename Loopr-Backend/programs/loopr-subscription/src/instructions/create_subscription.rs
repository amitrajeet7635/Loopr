use anchor_lang::prelude::*;
use crate::{state::*, errors::*};

#[derive(Accounts)]
#[instruction(subscription_id: String)]
pub struct CreateSubscription<'info> {
    #[account(
        init,
        payer = user,
        space = UserSubscription::LEN,
        seeds = [b"user_subscription", user.key().as_ref(), subscription_plan.key().as_ref()],
        bump
    )]
    pub user_subscription: Account<'info, UserSubscription>,
    
    #[account(
        mut,
        seeds = [b"subscription_plan", subscription_plan.get_plan_id().as_bytes()],
        bump = subscription_plan.bump,
        constraint = subscription_plan.is_active @ LooprError::PlanNotActive
    )]
    pub subscription_plan: Account<'info, SubscriptionPlan>,
    
    #[account(mut)]
    pub user: Signer<'info>,
    
    #[account(
        mut,
        seeds = [b"global_state"],
        bump = global_state.bump
    )]
    pub global_state: Account<'info, GlobalState>,
    
    pub system_program: Program<'info, System>,
}

pub fn handler(
    ctx: Context<CreateSubscription>,
    subscription_id: String,
) -> Result<()> {
    require!(!ctx.accounts.global_state.is_paused, LooprError::ProgramPaused);
    require!(subscription_id.len() <= 64, LooprError::SubscriptionIdTooLong);

    let subscription_plan = &mut ctx.accounts.subscription_plan;
    
    // Check if max subscribers limit is reached
    if let Some(max_subscribers) = subscription_plan.max_subscribers {
        require!(
            subscription_plan.current_subscribers < max_subscribers,
            LooprError::MaxSubscribersReached
        );
    }

    let user_subscription = &mut ctx.accounts.user_subscription;
    let clock = Clock::get()?;

    user_subscription.user = ctx.accounts.user.key();
    user_subscription.subscription_plan = subscription_plan.key();
    user_subscription.set_subscription_id(&subscription_id);
    user_subscription.is_active = false; // Will be activated after first payment
    user_subscription.next_payment_due = clock.unix_timestamp + subscription_plan.period_duration;
    user_subscription.last_payment_date = None;
    user_subscription.auto_pay_enabled = false;
    user_subscription.payment_thread = None;
    user_subscription.total_payments_made = 0;
    user_subscription.created_at = clock.unix_timestamp;
    user_subscription.updated_at = clock.unix_timestamp;
    user_subscription.bump = ctx.bumps.user_subscription;

    // Update subscription plan count
    subscription_plan.current_subscribers = subscription_plan.current_subscribers.checked_add(1).unwrap();

    // Update global state
    let global_state = &mut ctx.accounts.global_state;
    global_state.total_subscriptions = global_state.total_subscriptions.checked_add(1).unwrap();

    msg!("User subscription created: {}", user_subscription.get_subscription_id());
    
    Ok(())
}
