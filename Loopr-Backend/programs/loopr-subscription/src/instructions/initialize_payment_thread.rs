use anchor_lang::prelude::*;
use clockwork_sdk::{state::Thread, ThreadProgram, cpi::thread_create};
use crate::{state::*, errors::*};

#[derive(Accounts)]
#[instruction(thread_id: String)]
pub struct InitializePaymentThread<'info> {
    #[account(
        mut,
        seeds = [b"user_subscription", user.key().as_ref(), user_subscription.subscription_plan.as_ref()],
        bump = user_subscription.bump,
        constraint = user_subscription.user == user.key(),
        constraint = user_subscription.is_active @ LooprError::SubscriptionNotActive
    )]
    pub user_subscription: Account<'info, UserSubscription>,

    #[account(
        seeds = [b"subscription_plan", subscription_plan.plan_id.as_bytes()],
        bump = subscription_plan.bump
    )]
    pub subscription_plan: Account<'info, SubscriptionPlan>,

    #[account(mut)]
    pub user: Signer<'info>,

    /// CHECK: Clockwork thread account
    #[account(mut)]
    pub thread: AccountInfo<'info>,

    #[account(
        seeds = [b"global_state"],
        bump = global_state.bump
    )]
    pub global_state: Account<'info, GlobalState>,

    pub clockwork_program: Program<'info, ThreadProgram>,
    pub system_program: Program<'info, System>,
}

pub fn handler(ctx: Context<InitializePaymentThread>, thread_id: String) -> Result<()> {
    require!(!ctx.accounts.global_state.is_paused, LooprError::ProgramPaused);
    
    let user_subscription = &mut ctx.accounts.user_subscription;
    let subscription_plan = &ctx.accounts.subscription_plan;

    // Create clockwork thread for automated payments
    let trigger = clockwork_sdk::state::Trigger::Cron {
        schedule: format!("0 0 */{} * *", subscription_plan.period_duration / 86400), // Daily check
        skippable: true,
    };

    // Create instruction for automated payment
    let automated_payment_ix = Instruction {
        program_id: crate::ID,
        accounts: vec![
            AccountMeta::new(user_subscription.key(), false),
            AccountMeta::new_readonly(subscription_plan.key(), false),
            AccountMeta::new(Pubkey::default(), false), // payment_record (will be derived)
            AccountMeta::new_readonly(ctx.accounts.thread.key(), true),
            AccountMeta::new(Pubkey::default(), false), // user_token_account
            AccountMeta::new(Pubkey::default(), false), // plan_token_account
            AccountMeta::new_readonly(Pubkey::default(), false), // native_mint
            AccountMeta::new(ctx.accounts.global_state.key(), false),
            AccountMeta::new_readonly(anchor_spl::token::ID, false),
            AccountMeta::new_readonly(system_program::ID, false),
        ],
        data: vec![], // Will be filled by client
    };

    let thread_create_ctx = CpiContext::new(
        ctx.accounts.clockwork_program.to_account_info(),
        thread_create {
            authority: ctx.accounts.user.to_account_info(),
            payer: ctx.accounts.user.to_account_info(),
            system_program: ctx.accounts.system_program.to_account_info(),
            thread: ctx.accounts.thread.to_account_info(),
        },
    );

    clockwork_sdk::cpi::thread_create(
        thread_create_ctx,
        thread_id.clone(),
        vec![automated_payment_ix],
        trigger,
    )?;

    // Update subscription with thread info
    user_subscription.auto_pay_enabled = true;
    user_subscription.payment_thread = Some(ctx.accounts.thread.key());
    user_subscription.updated_at = Clock::get()?.unix_timestamp;

    msg!("Payment thread initialized for subscription: {}", user_subscription.subscription_id);
    Ok(())
}