import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { PayWithLoopr } from 'loopr-sdk';
import looprLogo from '../assets/loopr-logo-icon.png';

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

          <h1 className="text-3xl font-bold text-white mb-4">
            Payment Successful
          </h1>
          
          <p className="text-gray-300 mb-6">
            Welcome to <span className="font-semibold text-white">{plan.name}</span>!
            <br />
            You'll be redirected to the dashboard shortly.
          </p>

          <div className="flex items-center justify-center space-x-3">
            <div className="animate-spin rounded-full h-6 w-6 border-2 border-white/30 border-t-white"></div>
            <span className="text-gray-300">Preparing your dashboard...</span>
          </div>
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
            className="flex items-center space-x-2 px-4 py-2 rounded-lg bg-white/10 text-gray-300 hover:text-white hover:bg-white/20 transition-all"
          >
            <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
            </svg>
            <span>Back to Plans</span>
          </button>
          <div className="flex items-center space-x-3">
            <img src={looprLogo} alt="Loopr Logo" className="w-8 h-8 rounded-lg" />
            <span className="text-white text-xl font-bold">Loopr Services</span>
          </div>
        </div>
      </nav>

      {/* Main Content */}
      <div className="container mx-auto px-6 py-8">
        <div className="max-w-4xl mx-auto">
          {/* Header */}
          <div className="text-center mb-12">
            <h1 className="text-4xl font-bold text-white mb-4">
              Complete Your Purchase
            </h1>
            <p className="text-gray-300 max-w-xl mx-auto">
              Secure payment powered by blockchain technology
            </p>
          </div>

          <div className="grid lg:grid-cols-5 gap-8">
            {/* Plan Summary */}
            <div className="lg:col-span-2">
              <div className="bg-white/10 backdrop-blur-sm rounded-xl border border-white/20 p-6">
                <h2 className="text-xl font-bold text-white mb-6 flex items-center space-x-2">
                  <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                  </svg>
                  <span>Order Summary</span>
                </h2>
                
                <div className="space-y-4 mb-6">
                  <div className="flex justify-between">
                    <span className="text-gray-300">Plan</span>
                    <span className="text-white font-semibold">{plan.name}</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-gray-300">Billing</span>
                    <span className="text-blue-300 capitalize">{plan.frequency}</span>
                  </div>
                  <div className="border-t border-white/20 pt-4">
                    <div className="flex justify-between items-center">
                      <span className="text-white font-bold">Total</span>
                      <div className="text-right">
                        <div className="text-2xl font-bold text-white">${plan.priceUSD}</div>
                        <div className="text-sm text-gray-300">{getFrequencyDisplay(plan.frequency)}</div>
                      </div>
                    </div>
                  </div>
                </div>

                <div className="space-y-3">
                  <h3 className="text-lg font-semibold text-white">What's Included</h3>
                  {plan.features.map((feature, index) => (
                    <div key={index} className="flex items-center space-x-3">
                      <div className="w-5 h-5 bg-green-500 rounded-full flex items-center justify-center">
                        <svg className="w-3 h-3 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
                        </svg>
                      </div>
                      <span className="text-gray-200">{feature}</span>
                    </div>
                  ))}
                </div>
              </div>
            </div>

            {/* Payment Section */}
            <div className="lg:col-span-3">
              <div className="bg-white/10 backdrop-blur-sm rounded-xl border border-white/20 p-6">
                <h2 className="text-xl font-bold text-white mb-6 flex items-center space-x-2">
                  <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z" />
                  </svg>
                  <span>Payment Method</span>
                </h2>
                
                {/* Loopr Pay Section */}
                <div className="mb-6">
                  <div className="p-4 bg-purple-900/30 rounded-lg border border-purple-400/30">
                    <div className="flex items-center space-x-3 mb-3">
                      <div className="w-10 h-10 bg-gradient-to-r from-purple-500 to-blue-500 rounded-lg flex items-center justify-center">
                        <svg className="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1" />
                        </svg>
                      </div>
                      <div>
                        <h3 className="font-semibold text-white">Loopr Pay</h3>
                        <p className="text-sm text-gray-300">Fast, secure, and decentralized</p>
                      </div>
                    </div>
                    
                    <div className="p-3 bg-blue-900/30 rounded-lg border border-blue-400/30">
                      <div className="flex items-start space-x-2">
                        <svg className="w-5 h-5 text-blue-400 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
                        </svg>
                        <div>
                          <p className="text-sm text-blue-300 font-medium">Secure Payment</p>
                          <p className="text-xs text-blue-200">
                            Your payment is processed on the blockchain. No credit card information is stored.
                          </p>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>

                {/* Payment Component */}
                <div className="mb-6">
                  <PayWithLoopr
                    planId={planId!}
                    userId={MOCK_USER_ID}
                    onPaymentSuccess={handlePaymentSuccess}
                    onError={(error: string) => console.error('Payment error:', error)}
                  />
                </div>

                {/* Footer */}
                <div className="text-center p-4 bg-gray-800/40 rounded-lg">
                  <p className="text-xs text-gray-400">
                    By completing this purchase, you agree to our{' '}
                    <span className="text-blue-400 hover:text-blue-300 cursor-pointer">Terms of Service</span>
                    {' '}and{' '}
                    <span className="text-blue-400 hover:text-blue-300 cursor-pointer">Privacy Policy</span>.
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
