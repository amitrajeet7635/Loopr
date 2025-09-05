use anchor_lang::prelude::*;
use anchor_spl::token::{self, Token, TokenAccount, Transfer};

pub mod state;
pub mod errors;
pub mod instructions;

use instructions::*;
use state::*;
use errors::*;

declare_id!("LooprSub11111111111111111111111111111111");

#[program]
pub mod loopr_subscription {
    use super::*;

    /// Initialize a new subscription plan
    pub fn initialize_subscription_plan(
        ctx: Context<InitializeSubscriptionPlan>,
        plan_id: String,
        name: String,
        description: String,
        price_per_period: u64,
        period_duration: i64,
        max_subscribers: Option<u32>,
    ) -> Result<()> {
        instructions::initialize_subscription_plan::handler(
            ctx,
            plan_id,
            name,
            description,
            price_per_period,
            period_duration,
            max_subscribers,
        )
    }

    /// Create a new user subscription
    pub fn create_subscription(
        ctx: Context<CreateSubscription>,
        subscription_id: String,
    ) -> Result<()> {
        instructions::create_subscription::handler(ctx, subscription_id)
    }

    /// Process a payment for a subscription
    pub fn process_payment(ctx: Context<ProcessPayment>, amount: u64) -> Result<()> {
        instructions::process_payment::handler(ctx, amount)
    }

    /// Cancel a subscription
    pub fn cancel_subscription(ctx: Context<CancelSubscription>) -> Result<()> {
        instructions::cancel_subscription::handler(ctx)
    }

    /// Update subscription plan details
    pub fn update_subscription_plan(
        ctx: Context<UpdateSubscriptionPlan>,
        name: Option<String>,
        description: Option<String>,
        price_per_period: Option<u64>,
        period_duration: Option<i64>,
        max_subscribers: Option<u32>,
        is_active: Option<bool>,
    ) -> Result<()> {
        instructions::update_subscription_plan::handler(
            ctx,
            name,
            description,
            price_per_period,
            period_duration,
            max_subscribers,
            is_active,
        )
    }

    /// Automated payment processing (called by Clockwork thread)
    pub fn automated_payment(ctx: Context<AutomatedPayment>) -> Result<()> {
        instructions::automated_payment::handler(ctx)
    }

    /// Initialize payment thread for autopay
    pub fn initialize_payment_thread(
        ctx: Context<InitializePaymentThread>,
        thread_id: String,
    ) -> Result<()> {
        instructions::initialize_payment_thread::handler(ctx, thread_id)
    }

    /// Initialize global state
    pub fn initialize_global_state(ctx: Context<InitializeGlobalState>) -> Result<()> {
        instructions::initialize_global_state::handler(ctx)
    }

    /// Create payment intent for QR code flow
    pub fn create_payment_intent(
        ctx: Context<CreatePaymentIntent>,
        intent_id: String,
        plan_id: String,
        amount: u64,
        expires_at: i64,
    ) -> Result<()> {
        instructions::create_payment_intent::handler(ctx, intent_id, plan_id, amount, expires_at)
    }

    /// Subscribe and pay (complete QR flow)
    pub fn subscribe_and_pay(
        ctx: Context<SubscribeAndPay>,
        subscription_id: String,
    ) -> Result<()> {
        instructions::subscribe_and_pay::handler(ctx, subscription_id)
    }

    /// Confirm payment and complete subscription setup
    pub fn confirm_payment(ctx: Context<ConfirmPayment>) -> Result<()> {
        instructions::confirm_payment::handler(ctx)
    }
}
