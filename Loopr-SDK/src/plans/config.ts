import { Plan } from "./types";

export const plans: Plan[] = [
    {
        id: 'starter-monthly',
        name: 'Starter Plan',
        priceUSD: 5,
        frequency: 'monthly',
        merchantWallet: 'FFp8CuPYT1qNGEVcKA8MWh4iyoNekhJaJati8LvyeXDo',
        features: [
            'Access to basic features',
            'Email support',
            'Single user'
        ],
    },

    {
        id: 'pro-monthly',
        name: 'Pro Plan',
        priceUSD: 15,
        frequency: 'monthly',
        merchantWallet: 'FFp8CuPYT1qNGEVcKA8MWh4iyoNekhJaJati8LvyeXDo',
        features: [
            'Access to all features',
            'Priority email support',
            'Up to 5 users'
        ],
    },
    
    {
        id: 'enterprise-yearly',
        name: 'Enterprise Plan',
        priceUSD: 150,
        frequency: 'yearly',
        merchantWallet: 'FFp8CuPYT1qNGEVcKA8MWh4iyoNekhJaJati8LvyeXDo',
        features: [
            'Customized solutions',
            'Dedicated support',
            'Unlimited users'
        ],
    }
]
