use anchor_lang::prelude::*;

/// Subscription plan state
#[account]
pub struct SubscriptionPlan {
    pub authority: Pubkey,
    pub plan_id: [u8; 64],
    pub name: [u8; 128],
    pub description: [u8; 256],
    pub price_per_period: u64,
    pub period_duration: i64,
    pub max_subscribers: Option<u32>,
    pub current_subscribers: u32,
    pub is_active: bool,
    pub created_at: i64,
    pub updated_at: i64,
    pub bump: u8,
}

impl SubscriptionPlan {
    pub const LEN: usize = 8 + 32 + 64 + 128 + 256 + 8 + 8 + (1 + 4) + 4 + 1 + 8 + 8 + 1 + 32;

    pub fn set_plan_id(&mut self, plan_id: &str) {
        self.plan_id = string_to_fixed_bytes::<64>(plan_id);
    }

    pub fn get_plan_id(&self) -> String {
        bytes_to_string(&self.plan_id)
    }

    pub fn set_name(&mut self, name: &str) {
        self.name = string_to_fixed_bytes::<128>(name);
    }

    pub fn get_name(&self) -> String {
        bytes_to_string(&self.name)
    }

    pub fn set_description(&mut self, description: &str) {
        self.description = string_to_fixed_bytes::<256>(description);
    }

    pub fn get_description(&self) -> String {
        bytes_to_string(&self.description)
    }

    pub fn from_fields(
        authority: Pubkey,
        plan_id: &str,
        name: &str,
        description: &str,
        price_per_period: u64,
        period_duration: i64,
        max_subscribers: Option<u32>,
        is_active: bool,
        bump: u8,
    ) -> Self {
        Self {
            authority,
            plan_id: string_to_fixed_bytes::<64>(plan_id),
            name: string_to_fixed_bytes::<128>(name),
            description: string_to_fixed_bytes::<256>(description),
            price_per_period,
            period_duration,
            max_subscribers,
            current_subscribers: 0,
            is_active,
            created_at: Clock::get().unwrap().unix_timestamp,
            updated_at: Clock::get().unwrap().unix_timestamp,
            bump,
        }
    }

    pub fn update(
        &mut self,
        name: Option<&str>,
        description: Option<&str>,
        price_per_period: Option<u64>,
        period_duration: Option<i64>,
        max_subscribers: Option<u32>,
        is_active: Option<bool>,
    ) {
        if let Some(name) = name {
            self.name = string_to_fixed_bytes::<128>(name);
        }
        if let Some(description) = description {
            self.description = string_to_fixed_bytes::<256>(description);
        }
        if let Some(price) = price_per_period {
            self.price_per_period = price;
        }
        if let Some(duration) = period_duration {
            self.period_duration = duration;
        }
        if let Some(max) = max_subscribers {
            self.max_subscribers = Some(max);
        }
        if let Some(active) = is_active {
            self.is_active = active;
        }
        self.updated_at = Clock::get().unwrap().unix_timestamp;
    }

    pub fn activate(&mut self) {
        self.is_active = true;
        self.updated_at = Clock::get().unwrap().unix_timestamp;
    }

    pub fn deactivate(&mut self) {
        self.is_active = false;
        self.updated_at = Clock::get().unwrap().unix_timestamp;
    }
}

/// User subscription state
#[account]
pub struct UserSubscription {
    pub user: Pubkey,
    pub subscription_plan: Pubkey,
    pub subscription_id: [u8; 64],
    pub is_active: bool,
    pub next_payment_due: i64,
    pub last_payment_date: Option<i64>,
    pub auto_pay_enabled: bool,
    pub payment_thread: Option<Pubkey>,
    pub total_payments_made: u64,
    pub created_at: i64,
    pub updated_at: i64,
    pub bump: u8,
}

impl UserSubscription {
    pub const LEN: usize = 8 + 32 + 32 + 64 + 1 + 8 + (1 + 8) + 1 + (1 + 32) + 8 + 8 + 8 + 1 + 16;

    pub fn set_subscription_id(&mut self, id: &str) {
        self.subscription_id = string_to_fixed_bytes::<64>(id);
    }

    pub fn get_subscription_id(&self) -> String {
        bytes_to_string(&self.subscription_id)
    }

    pub fn from_fields(
        user: Pubkey,
        subscription_plan: Pubkey,
        subscription_id: &str,
        is_active: bool,
        next_payment_due: i64,
        auto_pay_enabled: bool,
        bump: u8,
    ) -> Self {
        Self {
            user,
            subscription_plan,
            subscription_id: string_to_fixed_bytes::<64>(subscription_id),
            is_active,
            next_payment_due,
            last_payment_date: None,
            auto_pay_enabled,
            payment_thread: None,
            total_payments_made: 0,
            created_at: Clock::get().unwrap().unix_timestamp,
            updated_at: Clock::get().unwrap().unix_timestamp,
            bump,
        }
    }

    pub fn update(
        &mut self,
        is_active: Option<bool>,
        next_payment_due: Option<i64>,
        last_payment_date: Option<i64>,
        auto_pay_enabled: Option<bool>,
    ) {
        if let Some(active) = is_active {
            self.is_active = active;
        }
        if let Some(next) = next_payment_due {
            self.next_payment_due = next;
        }
        if let Some(last) = last_payment_date {
            self.last_payment_date = Some(last);
        }
        if let Some(auto_pay) = auto_pay_enabled {
            self.auto_pay_enabled = auto_pay;
        }
        self.updated_at = Clock::get().unwrap().unix_timestamp;
    }

    pub fn activate(&mut self) {
        self.is_active = true;
        self.updated_at = Clock::get().unwrap().unix_timestamp;
    }

    pub fn deactivate(&mut self) {
        self.is_active = false;
        self.updated_at = Clock::get().unwrap().unix_timestamp;
    }
}

/// Payment record state
#[account]
pub struct PaymentRecord {
    pub user: Pubkey,
    pub subscription: Pubkey,
    pub amount: u64,
    pub payment_date: i64,
    pub transaction_signature: [u8; 128],
    pub payment_method: PaymentMethod,
    pub status: PaymentStatus,
    pub bump: u8,
}

impl PaymentRecord {
    pub const LEN: usize = 8 + 32 + 32 + 8 + 8 + 128 + 1 + 1 + 1 + 8;

    pub fn set_transaction_signature(&mut self, signature: &str) {
        self.transaction_signature = string_to_fixed_bytes::<128>(signature);
    }

    pub fn get_transaction_signature(&self) -> String {
        bytes_to_string(&self.transaction_signature)
    }

    pub fn from_fields(
        user: Pubkey,
        subscription: Pubkey,
        amount: u64,
        payment_date: i64,
        transaction_signature: &str,
        payment_method: PaymentMethod,
        status: PaymentStatus,
        bump: u8,
    ) -> Self {
        Self {
            user,
            subscription,
            amount,
            payment_date,
            transaction_signature: string_to_fixed_bytes::<128>(transaction_signature),
            payment_method,
            status,
            bump,
        }
    }
}

/// Payment intent state
#[account]
pub struct PaymentIntent {
    pub intent_id: [u8; 64],
    pub plan_id: [u8; 64],
    pub payer: Option<Pubkey>,
    pub amount: u64,
    pub status: PaymentIntentStatus,
    pub created_at: i64,
    pub expires_at: i64,
    pub fulfilled_at: Option<i64>,
    pub subscription: Option<Pubkey>,
    pub bump: u8,
}

impl PaymentIntent {
    pub const LEN: usize = 8 + 64 + 64 + (1 + 32) + 8 + 1 + 8 + 8 + (1 + 8) + (1 + 32) + 1 + 16;

    pub fn set_intent_id(&mut self, id: &str) {
        self.intent_id = string_to_fixed_bytes::<64>(id);
    }

    pub fn get_intent_id(&self) -> String {
        bytes_to_string(&self.intent_id)
    }

    pub fn set_plan_id(&mut self, id: &str) {
        self.plan_id = string_to_fixed_bytes::<64>(id);
    }

    pub fn get_plan_id(&self) -> String {
        bytes_to_string(&self.plan_id)
    }

    pub
