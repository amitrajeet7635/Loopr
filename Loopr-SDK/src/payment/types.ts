export type SubscriptionFrequency = 'monthly' | 'quarterly' | 'yearly';

export interface PaymentIntentConfig {
  recipient: string; // merchant wallet address (base58)
  amount: number; // in SOL
  frequency: SubscriptionFrequency;
  label?: string;
  message?: string;
  metadata?: {
    planId?: string;
    userId?: string;
    [key: string]: any;
  };
}
