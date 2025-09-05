use anchor_lang::prelude::*;
use crate::{state::*, errors::*};

#[derive(Accounts)]
#[instruction(intent_id: String, plan_id: String)]
pub struct CreatePaymentIntent<'info> {
    #[account(
        init,
        payer = authority,
        space = PaymentIntent::LEN,
        seeds = [b"payment_intent", intent_id.as_bytes()],
        bump
    )]
    pub payment_intent: Account<'info, PaymentIntent>,
    
    #[account(
        seeds = [b"subscription_plan", plan_id.as_bytes()],
        bump = subscription_plan.bump,
        constraint = subscription_plan.is_active @ LooprError::PlanNotActive
    )]
    pub subscription_plan: Account<'info, SubscriptionPlan>,
    
    #[account(mut)]
    pub authority: Signer<'info>,
    
    #[account(
        seeds = [b"global_state"],
        bump = global_state.bump
    )]
    pub global_state: Account<'info, GlobalState>,
    
    pub system_program: Program<'info, System>,
}

pub fn handler(
    ctx: Context<CreatePaymentIntent>,
    intent_id: String,
    plan_id: String,
    amount: u64,
    expires_at: i64,
) -> Result<()> {
    require!(!ctx.accounts.global_state.is_paused, LooprError::ProgramPaused);
    require!(intent_id.len() <= 64, LooprError::IntentIdTooLong);
    require!(
        amount == ctx.accounts.subscription_plan.price_per_period,
        LooprError::InvalidPaymentAmount
    );

    let payment_intent = &mut ctx.accounts.payment_intent;
    let clock = Clock::get()?;
    require!(expires_at > clock.unix_timestamp, LooprError::PaymentIntentExpired);

    payment_intent.set_intent_id(&intent_id);
    payment_intent.set_plan_id(&plan_id);
    payment_intent.payer = None;
    payment_intent.amount = amount;
    payment_intent.status = PaymentIntentStatus::Created;
    payment_intent.created_at = clock.unix_timestamp;
    payment_intent.expires_at = expires_at;
    payment_intent.fulfilled_at = None;
    payment_intent.subscription = None;
    payment_intent.bump = ctx.bumps.payment_intent;

    msg!("Payment intent created: {} for {} SOL", payment_intent.get_intent_id(), amount);
    
    Ok(())
}