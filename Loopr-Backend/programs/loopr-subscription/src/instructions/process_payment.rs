use anchor_lang::prelude::*;
use anchor_spl::token::{self, Token, TokenAccount, Transfer};
use crate::{state::*, errors::*};

#[derive(Accounts)]
pub struct ProcessPayment<'info> {
    #[account(
        mut,
        seeds = [b"user_subscription", user.key().as_ref(), user_subscription.subscription_plan.as_ref()],
        bump = user_subscription.bump,
        constraint = user_subscription.user == user.key()
    )]
    pub user_subscription: Account<'info, UserSubscription>,
    
    #[account(
        seeds = [b"subscription_plan", subscription_plan.get_plan_id().as_bytes()],
        bump = subscription_plan.bump,
        constraint = subscription_plan.is_active @ LooprError::PlanNotActive
    )]
    pub subscription_plan: Account<'info, SubscriptionPlan>,
    
    #[account(
        init,
        payer = user,
        space = PaymentRecord::LEN,
        seeds = [b"payment_record", user_subscription.key().as_ref(), &Clock::get()?.unix_timestamp.to_le_bytes()],
        bump
    )]
    pub payment_record: Account<'info, PaymentRecord>,
    
    #[account(mut)]
    pub user: Signer<'info>,
    
    #[account(
        mut,
        constraint = user_token_account.owner == user.key(),
        constraint = user_token_account.mint == native_mint.key()
    )]
    pub user_token_account: Account<'info, TokenAccount>,
    
    #[account(
        mut,
        constraint = plan_token_account.owner == subscription_plan.authority,
        constraint = plan_token_account.mint == native_mint.key()
    )]
    pub plan_token_account: Account<'info, TokenAccount>,
    
    /// CHECK: This is the native mint for SOL
    pub native_mint: AccountInfo<'info>,
    
    #[account(
        mut,
        seeds = [b"global_state"],
        bump = global_state.bump
    )]
    pub global_state: Account<'info, GlobalState>,
    
    pub token_program: Program<'info, Token>,
    pub system_program: Program<'info, System>,
}

pub fn handler(ctx: Context<ProcessPayment>, amount: u64) -> Result<()> {
    require!(!ctx.accounts.global_state.is_paused, LooprError::ProgramPaused);
    require!(
        amount == ctx.accounts.subscription_plan.price_per_period,
        LooprError::InvalidPaymentAmount
    );

    let user_subscription = &mut ctx.accounts.user_subscription;
    let subscription_plan = &ctx.accounts.subscription_plan;
    let clock = Clock::get()?;

    // Transfer tokens from user to plan authority
    let transfer_ctx = CpiContext::new(
        ctx.accounts.token_program.to_account_info(),
        Transfer {
            from: ctx.accounts.user_token_account.to_account_info(),
            to: ctx.accounts.plan_token_account.to_account_info(),
            authority: ctx.accounts.user.to_account_info(),
        },
    );
    token::transfer(transfer_ctx, amount)?;

    // Update subscription
    user_subscription.is_active = true;
    user_subscription.last_payment_date = Some(clock.unix_timestamp);
    user_subscription.next_payment_due = clock.unix_timestamp + subscription_plan.period_duration;
    user_subscription.total_payments_made = user_subscription.total_payments_made.checked_add(1).unwrap();
    user_subscription.updated_at = clock.unix_timestamp;

    // Create payment record
    let payment_record = &mut ctx.accounts.payment_record;
    payment_record.user = ctx.accounts.user.key();
    payment_record.subscription = user_subscription.key();
    payment_record.amount = amount;
    payment_record.payment_date = clock.unix_timestamp;
    payment_record.set_transaction_signature(""); // Will be filled by client
    payment_record.payment_method = PaymentMethod::Manual;
    payment_record.status = PaymentStatus::Completed;
    payment_record.bump = ctx.bumps.payment_record;

    // Update global state
    let global_state = &mut ctx.accounts.global_state;
    global_state.total_payments_processed = global_state.total_payments_processed.checked_add(1).unwrap();
    global_state.total_volume = global_state.total_volume.checked_add(amount).unwrap();

    msg!("Payment processed: {} SOL for subscription {}", amount, user_subscription.get_subscription_id());
    
    Ok(())
}