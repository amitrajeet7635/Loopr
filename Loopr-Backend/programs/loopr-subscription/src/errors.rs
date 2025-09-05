use anchor_lang::prelude::*;

#[error_code]
pub enum LooprError {
    #[msg("Subscription plan is not active")]
    PlanNotActive,
    
    #[msg("Maximum subscribers reached")]
    MaxSubscribersReached,
    
    #[msg("Subscription is not active")]
    SubscriptionNotActive,
    
    #[msg("Payment amount does not match plan price")]
    InvalidPaymentAmount,
    
    #[msg("Insufficient funds")]
    InsufficientFunds,
    
    #[msg("Payment is overdue")]
    PaymentOverdue,
    
    #[msg("Payment is not due yet")]
    PaymentNotDue,
    
    #[msg("Auto-pay is not enabled")]
    AutoPayNotEnabled,
    
    #[msg("Thread authority mismatch")]
    ThreadAuthorityMismatch,
    
    #[msg("Plan ID too long")]
    PlanIdTooLong,
    
    #[msg("Plan name too long")]
    PlanNameTooLong,
    
    #[msg("Plan description too long")]
    PlanDescriptionTooLong,
    
    #[msg("Subscription ID too long")]
    SubscriptionIdTooLong,
    
    #[msg("Invalid period duration")]
    InvalidPeriodDuration,
    
    #[msg("Unauthorized operation")]
    Unauthorized,
    
    #[msg("Program is paused")]
    ProgramPaused,
    
    #[msg("Payment intent not found")]
    PaymentIntentNotFound,
    
    #[msg("Payment intent expired")]
    PaymentIntentExpired,
    
    #[msg("Payment intent already fulfilled")]
    PaymentIntentAlreadyFulfilled,
    
    #[msg("Invalid payment intent status")]
    InvalidPaymentIntentStatus,
    
    #[msg("QR code payment failed")]
    QRPaymentFailed,
    
    #[msg("Intent ID too long")]
    IntentIdTooLong,
}