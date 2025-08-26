declare module 'loopr-sdk' {
  import { FC } from 'react';

  export interface PayWithLooprProps {
    planId: string;
    userId: string;
    onPaymentInitiated?: () => void;
    onPaymentSuccess?: () => void;
    onError?: (error: string) => void;
  }

  export const PayWithLoopr: FC<PayWithLooprProps>;

  export type SubscriptionFrequency = 'monthly' | 'quarterly' | 'yearly';

  export interface PaymentIntentConfig {
    recipient: string;
    amount: number;
    frequency: SubscriptionFrequency;
    label?: string;
    message?: string;
    metadata?: {
      planId?: string;
      userId?: string;
      [key: string]: unknown;
    };
  }

  export interface Plan {
    id: string;
    name: string;
    priceUSD: number;
    frequency: 'monthly' | 'quarterly' | 'yearly';
    features: string[];
    merchantWallet: string;
  }

  export function createPlanPaymentIntent(planId: string, userId: string): Promise<{
    plan: Plan;
    intent: string;
    qrCode: string | HTMLCanvasElement;
  }>;

  export function getPlanById(planId: string): Plan | undefined;
  export function isValidPlanId(planId: string): boolean;
}