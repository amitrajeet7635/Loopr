import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { PayWithLoopr } from 'loopr-sdk';

// Mock plans data (same as in Home.tsx)
const plans = [
  {
    id: 'starter-monthly',
    name: 'Starter Plan',
    priceUSD: 5,
    frequency: 'monthly' as const,
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
    frequency: 'monthly' as const,
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
    frequency: 'yearly' as const,
    features: [
      'Customized solutions',
      'Dedicated support',
      'Unlimited users'
    ],
  }
];

// Mock user ID for demo
const MOCK_USER_ID = 'user_demo_123';

export const Checkout: React.FC = () => {
  const { planId } = useParams<{ planId: string }>();
  const navigate = useNavigate();
  const [paymentSuccess, setPaymentSuccess] = useState(false);

  const plan = plans.find(p => p.id === planId);

  if (!plan) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-slate-900 via-purple-900 to-slate-900 flex items-center justify-center">
        <div className="text-center">
          <h1 className="text-2xl font-bold text-white mb-4">Plan Not Found</h1>
          <button 
            onClick={() => navigate('/')}
            className="bg-gradient-to-r from-purple-500 to-blue-500 text-white px-6 py-3 rounded-lg hover:from-purple-600 hover:to-blue-600 transition-all"
          >
            Back to Plans
          </button>
        </div>
      </div>
    );
  }

  const getFrequencyDisplay = (frequency: string) => {
    switch (frequency) {
      case 'monthly':
        return '/month';
      case 'quarterly':
        return '/quarter';
      case 'yearly':
        return '/year';
      default:
        return '';
    }
  };

  const handlePaymentSuccess = () => {
    setPaymentSuccess(true);
    // Simulate redirect to success page after 3 seconds
    setTimeout(() => {
      navigate('/', { state: { message: 'Payment successful! Welcome to ' + plan.name } });
    }, 3000);
  };

  if (paymentSuccess) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-slate-900 via-purple-900 to-slate-900 flex items-center justify-center">
        <div className="text-center max-w-md mx-auto p-8">
          <div className="w-20 h-20 bg-green-500 rounded-full flex items-center justify-center mx-auto mb-6">
            <svg className="w-10 h-10 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
            </svg>
          </div>
          <h1 className="text-3xl font-bold text-white mb-4">Payment Successful!</h1>
          <p className="text-gray-300 mb-6">
            Welcome to {plan.name}. You'll be redirected to the dashboard shortly.
          </p>
          <div className="animate-spin rounded-full h-6 w-6 border-b-2 border-white mx-auto"></div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-900 via-purple-900 to-slate-900">
      {/* Navigation */}
      <nav className="container mx-auto px-6 py-4">
        <div className="flex items-center justify-between">
          <button 
            onClick={() => navigate('/')}
            className="flex items-center space-x-2 text-gray-300 hover:text-white transition-colors"
          >
            <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
            </svg>
            <span>Back to Plans</span>
          </button>
          <div className="flex items-center space-x-2">
            <div className="w-8 h-8 bg-gradient-to-r from-purple-400 to-blue-400 rounded-lg flex items-center justify-center">
              <span className="text-white font-bold text-lg">L</span>
            </div>
            <span className="text-white text-xl font-bold">Loopr</span>
          </div>
        </div>
      </nav>

      {/* Checkout Content */}
      <div className="container mx-auto px-6 py-12">
        <div className="max-w-4xl mx-auto">
          <div className="text-center mb-12">
            <h1 className="text-4xl font-bold text-white mb-4">Complete Your Purchase</h1>
            <p className="text-xl text-gray-300">
              Secure payment powered by Solana blockchain
            </p>
          </div>

          <div className="grid md:grid-cols-2 gap-12">
            {/* Plan Summary */}
            <div className="bg-gray-800/50 backdrop-blur-sm border border-gray-700 rounded-xl p-8">
              <h2 className="text-2xl font-bold text-white mb-6">Order Summary</h2>
              
              <div className="space-y-4 mb-6">
                <div className="flex items-center justify-between">
                  <span className="text-gray-300">Plan</span>
                  <span className="text-white font-semibold">{plan.name}</span>
                </div>
                <div className="flex items-center justify-between">
                  <span className="text-gray-300">Billing</span>
                  <span className="text-white">{plan.frequency}</span>
                </div>
                <div className="border-t border-gray-700 pt-4">
                  <div className="flex items-center justify-between text-xl">
                    <span className="text-white font-semibold">Total</span>
                    <span className="text-white font-bold">
                      ${plan.priceUSD}{getFrequencyDisplay(plan.frequency)}
                    </span>
                  </div>
                </div>
              </div>

              <div className="space-y-3">
                <h3 className="text-lg font-semibold text-white">Included Features:</h3>
                {plan.features.map((feature, index) => (
                  <div key={index} className="flex items-center space-x-3">
                    <div className="flex-shrink-0">
                      <svg className="w-5 h-5 text-green-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
                      </svg>
                    </div>
                    <span className="text-gray-300">{feature}</span>
                  </div>
                ))}
              </div>
            </div>

            {/* Payment Section */}
            <div className="bg-gray-800/50 backdrop-blur-sm border border-gray-700 rounded-xl p-8">
              <h2 className="text-2xl font-bold text-white mb-6">Payment Method</h2>
              
              <div className="mb-6">
                <div className="flex items-center space-x-3 mb-4">
                  <div className="w-10 h-10 bg-gradient-to-r from-purple-500 to-blue-500 rounded-lg flex items-center justify-center">
                    <svg className="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1" />
                    </svg>
                  </div>
                  <div>
                    <h3 className="text-lg font-semibold text-white">Solana Pay</h3>
                    <p className="text-sm text-gray-400">Fast, secure, and decentralized</p>
                  </div>
                </div>
                
                <div className="bg-blue-900/20 border border-blue-700/50 rounded-lg p-4 mb-6">
                  <div className="flex items-start space-x-3">
                    <svg className="w-5 h-5 text-blue-400 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                    </svg>
                    <div>
                      <p className="text-sm text-blue-300 font-medium">Secure Payment</p>
                      <p className="text-xs text-blue-200 mt-1">
                        Your payment is processed on the Solana blockchain. No credit card information is stored.
                      </p>
                    </div>
                  </div>
                </div>
              </div>

              <PayWithLoopr
                planId={planId!}
                userId={MOCK_USER_ID}
                onPaymentSuccess={handlePaymentSuccess}
                onError={(error: string) => console.error('Payment error:', error)}
              />

              <div className="mt-6 text-center">
                <p className="text-xs text-gray-400">
                  By completing this purchase, you agree to our Terms of Service and Privacy Policy.
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
