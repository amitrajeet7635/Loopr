use anchor_lang::prelude::*;
use crate::{state::*, errors::*};

#[derive(Accounts)]
#[instruction(plan_id: String)]
pub struct InitializeSubscriptionPlan<'info> {
    #[account(
        init,
        payer = authority,
        space = SubscriptionPlan::LEN,
        seeds = [b"subscription_plan", plan_id.as_bytes()],
        bump
    )]
    pub subscription_plan: Account<'info, SubscriptionPlan>,
    
    #[account(mut)]
    pub authority: Signer<'info>,
    
    #[account(
        mut,
        seeds = [b"global_state"],
        bump = global_state.bump
    )]
    pub global_state: Account<'info, GlobalState>,
    
    pub system_program: Program<'info, System>,
}

pub fn handler(
    ctx: Context<InitializeSubscriptionPlan>,
    plan_id: String,
    name: String,
    description: String,
    price_per_period: u64,
    period_duration: i64,
    max_subscribers: Option<u32>,
) -> Result<()> {
    require!(!ctx.accounts.global_state.is_paused, LooprError::ProgramPaused);
    require!(plan_id.len() <= 64, LooprError::PlanIdTooLong);
    require!(name.len() <= 128, LooprError::PlanNameTooLong);
    require!(description.len() <= 256, LooprError::PlanDescriptionTooLong);
    require!(period_duration > 0, LooprError::InvalidPeriodDuration);

    let subscription_plan = &mut ctx.accounts.subscription_plan;
    let clock = Clock::get()?;

    subscription_plan.authority = ctx.accounts.authority.key();
    subscription_plan.set_plan_id(&plan_id);
    subscription_plan.set_name(&name);
    subscription_plan.set_description(&description);
    subscription_plan.price_per_period = price_per_period;
    subscription_plan.period_duration = period_duration;
    subscription_plan.max_subscribers = max_subscribers;
    subscription_plan.current_subscribers = 0;
    subscription_plan.is_active = true;
    subscription_plan.created_at = clock.unix_timestamp;
    subscription_plan.updated_at = clock.unix_timestamp;
    subscription_plan.bump = ctx.bumps.subscription_plan;

    // Update global state
    let global_state = &mut ctx.accounts.global_state;
    global_state.total_plans = global_state.total_plans.checked_add(1).unwrap();

    msg!("Subscription plan created: {}", subscription_plan.get_plan_id());
    
    Ok(())
}