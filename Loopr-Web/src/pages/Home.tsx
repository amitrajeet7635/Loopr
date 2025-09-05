import React from 'react';
import { useNavigate } from 'react-router-dom';
import { PlanCard } from '../components/PlanCard';
import looprLogo from '../assets/loopr-icon.png';
import looprIcon from '../assets/loopr-logo-icon.png';

// Mock plans data based on the SDK config
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

export const Home: React.FC = () => {
  const navigate = useNavigate();

  const handleSelectPlan = (planId: string) => {
    navigate(`/checkout/${planId}`);
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-900 via-purple-900 to-slate-900 relative overflow-hidden">
      {/* Floating background elements */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-32 h-32 bg-purple-500/10 rounded-full animate-float" style={{animationDelay: '0s'}}></div>
        <div className="absolute top-3/4 right-1/4 w-24 h-24 bg-blue-500/10 rounded-full animate-float" style={{animationDelay: '2s'}}></div>
        <div className="absolute top-1/2 left-3/4 w-20 h-20 bg-purple-400/10 rounded-full animate-float" style={{animationDelay: '4s'}}></div>
        <div className="absolute top-1/3 right-1/3 w-16 h-16 bg-blue-400/10 rounded-full animate-float" style={{animationDelay: '1s'}}></div>
      </div>

      {/* Content */}
      <div className="relative z-10">
        {/* Navigation */}
        <nav className="container mx-auto px-6 py-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center space-x-2">
              <img 
                src={looprIcon} 
                alt="Loopr Logo" 
                className="w-8 h-8 rounded-lg"
              />
              <span className="text-white text-xl font-bold">Loopr Services</span>
            </div>
            <div className="hidden md:flex items-center space-x-6">
              {/* Navigation items removed as requested */}
            </div>
          </div>
        </nav>

        {/* Hero Section */}
        <div className="container mx-auto px-6 py-20">
          <div className="text-center mb-16">
            <div className="animate-fade-in-up">
              {/* Hero Logo */}
              <div className="flex justify-center mb-8">
                <img 
                  src={looprLogo} 
                  alt="Loopr Logo" 
                  className=" h-30 rounded-2xl animate-float"
                />
              </div>
              <h1 className="text-5xl md:text-7xl font-bold text-white mb-6">
                Web3 Payment
                <span className="bg-gradient-to-r from-purple-400 to-blue-400 bg-clip-text text-transparent animate-gradient">
                  {" "}Infrastructure
                </span>
              </h1>
            </div>
            <div className="animate-fade-in-up" style={{animationDelay: '0.2s'}}>
              <p className="text-xl text-gray-300 mb-8 max-w-3xl mx-auto">
                Accept crypto payments seamlessly with Loopr Pay. 
                Built for developers, designed for scale, powered by blockchain technology.
              </p>
            </div>
            <div className="animate-fade-in-up" style={{animationDelay: '0.4s'}}>
              <div className="flex flex-col sm:flex-row items-center justify-center gap-4">
                <button 
                  onClick={() => document.getElementById('pricing')?.scrollIntoView({ behavior: 'smooth' })}
                  className="group bg-gradient-to-r from-purple-500 to-blue-500 text-white px-8 py-4 rounded-lg text-lg font-semibold hover:from-purple-600 hover:to-blue-600 transform hover:scale-105 transition-all duration-200 relative overflow-hidden"
                >
                  <div className="absolute inset-0 bg-gradient-to-r from-transparent via-white/10 to-transparent transform -skew-x-12 -translate-x-full group-hover:translate-x-full transition-transform duration-1000"></div>
                  <span className="relative">Start Building</span>
                </button>
              </div>
            </div>
          </div>

          {/* Features Grid */}
          <div id="features" className="grid md:grid-cols-3 gap-8 mb-20">
            <div className="group bg-gray-800/50 backdrop-blur-sm border border-gray-700 rounded-xl p-8 hover:border-purple-500/50 transition-all duration-300 hover:scale-105 animate-fade-in-up" style={{animationDelay: '0.6s'}}>
              <div className="w-12 h-12 bg-gradient-to-r from-purple-500 to-blue-500 rounded-lg flex items-center justify-center mb-4 group-hover:animate-float">
                <svg className="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 10V3L4 14h7v7l9-11h-7z" />
                </svg>
              </div>
              <h3 className="text-xl font-semibold text-white mb-3">Lightning Fast</h3>
              <p className="text-gray-400">Instant payments with high-speed blockchain infrastructure</p>
            </div>

            <div className="group bg-gray-800/50 backdrop-blur-sm border border-gray-700 rounded-xl p-8 hover:border-purple-500/50 transition-all duration-300 hover:scale-105 animate-fade-in-up" style={{animationDelay: '0.8s'}}>
              <div className="w-12 h-12 bg-gradient-to-r from-purple-500 to-blue-500 rounded-lg flex items-center justify-center mb-4 group-hover:animate-float">
                <svg className="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
                </svg>
              </div>
              <h3 className="text-xl font-semibold text-white mb-3">Secure by Design</h3>
              <p className="text-gray-400">Enterprise-grade security with blockchain-native protection</p>
            </div>

            <div className="group bg-gray-800/50 backdrop-blur-sm border border-gray-700 rounded-xl p-8 hover:border-purple-500/50 transition-all duration-300 hover:scale-105 animate-fade-in-up" style={{animationDelay: '1.0s'}}>
              <div className="w-12 h-12 bg-gradient-to-r from-purple-500 to-blue-500 rounded-lg flex items-center justify-center mb-4 group-hover:animate-float">
                <svg className="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z" />
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                </svg>
              </div>
              <h3 className="text-xl font-semibold text-white mb-3">Developer First</h3>
              <p className="text-gray-400">Simple SDK integration with comprehensive documentation</p>
            </div>
          </div>

          {/* Pricing Section */}
          <div id="pricing" className="text-center mb-12">
            <h2 className="text-4xl font-bold text-white mb-4">
              Choose Your Plan
            </h2>
            <p className="text-xl text-gray-300 mb-12">
              Start with our free tier or scale with enterprise features
            </p>
          </div>

          {/* Plans Grid */}
          <div className="grid md:grid-cols-3 gap-8 max-w-6xl mx-auto">
            {plans.map((plan, index) => (
              <PlanCard
                key={plan.id}
                plan={plan}
                onSelect={() => handleSelectPlan(plan.id)}
                isPopular={index === 1} // Make the middle plan popular
              />
            ))}
          </div>

          {/* Technology Stack Section */}
          <div className="mt-20 pt-20 border-t border-gray-800">
            <div className="text-center mb-12">
              <h2 className="text-3xl font-bold text-white mb-4">
                Built on Modern Web3 Technology
              </h2>
              <p className="text-xl text-gray-300 max-w-3xl mx-auto">
                Loopr leverages cutting-edge blockchain technology to provide seamless subscription management and payment processing for the decentralized web.
              </p>
            </div>

            <div className="grid md:grid-cols-3 gap-8">
              <div className="bg-gray-800/30 backdrop-blur-sm border border-gray-700 rounded-xl p-6 text-center">
                <div className="w-16 h-16 bg-gradient-to-r from-purple-500 to-blue-500 rounded-lg flex items-center justify-center mx-auto mb-4">
                  <svg className="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19.428 15.428a2 2 0 00-1.022-.547l-2.387-.477a6 6 0 00-3.86.517l-.318.158a6 6 0 01-3.86.517L6.05 15.21a2 2 0 00-1.806.547M8 4h8l-1 1v5.172a2 2 0 00.586 1.414l5 5c1.26 1.26.367 3.414-1.415 3.414H4.828c-1.782 0-2.674-2.154-1.414-3.414l5-5A2 2 0 009 7.172V5L8 4z" />
                  </svg>
                </div>
                <h3 className="text-lg font-semibold text-white mb-2">Solana Powered</h3>
                <p className="text-gray-400 text-sm">
                  Built on Solana's high-performance blockchain for fast, low-cost transactions
                </p>
              </div>

              <div className="bg-gray-800/30 backdrop-blur-sm border border-gray-700 rounded-xl p-6 text-center">
                <div className="w-16 h-16 bg-gradient-to-r from-purple-500 to-blue-500 rounded-lg flex items-center justify-center mx-auto mb-4">
                  <svg className="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
                  </svg>
                </div>
                <h3 className="text-lg font-semibold text-white mb-2">Smart Contracts</h3>
                <p className="text-gray-400 text-sm">
                  Automated subscription management through secure smart contract protocols
                </p>
              </div>

              <div className="bg-gray-800/30 backdrop-blur-sm border border-gray-700 rounded-xl p-6 text-center">
                <div className="w-16 h-16 bg-gradient-to-r from-purple-500 to-blue-500 rounded-lg flex items-center justify-center mx-auto mb-4">
                  <svg className="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M10 20l4-16m4 4l4 4-4 4M6 16l-4-4 4-4" />
                  </svg>
                </div>
                <h3 className="text-lg font-semibold text-white mb-2">Developer SDK</h3>
                <p className="text-gray-400 text-sm">
                  Simple integration with React components and TypeScript support
                </p>
              </div>
            </div>

            <div className="mt-12 bg-gradient-to-r from-purple-900/20 to-blue-900/20 border border-purple-500/20 rounded-xl p-8">
              <div className="text-center">
                <h3 className="text-2xl font-bold text-white mb-4">
                  The Future of Subscription Payments
                </h3>
                <p className="text-gray-300 max-w-2xl mx-auto mb-6">
                  Loopr eliminates the complexity of traditional payment processors by leveraging blockchain technology. 
                  No more chargebacks, reduced fees, and instant global payments - all while maintaining full decentralization.
                </p>
                <div className="flex flex-wrap justify-center gap-4 text-sm">
                  <span className="bg-purple-500/20 text-purple-300 px-3 py-1 rounded-full">Zero Chargebacks</span>
                  <span className="bg-blue-500/20 text-blue-300 px-3 py-1 rounded-full">Global Payments</span>
                  <span className="bg-green-500/20 text-green-300 px-3 py-1 rounded-full">Low Fees</span>
                  <span className="bg-yellow-500/20 text-yellow-300 px-3 py-1 rounded-full">Instant Settlement</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Footer */}
        <footer className="border-t border-gray-800 mt-20">
          <div className="container mx-auto px-6 py-8">
            <div className="flex flex-col md:flex-row items-center justify-between">
              <div className="flex items-center space-x-2 mb-4 md:mb-0">
                <img 
                  src={looprIcon} 
                  alt="Loopr Logo" 
                  className="w-6 h-6 rounded"
                />
                <span className="text-white font-semibold">Loopr Services</span>
              </div>
              <div className="text-gray-400 text-sm">
                © 2025 Loopr Services. Built with ❤️ for Web3 developers.
              </div>
            </div>
          </div>
        </footer>
      </div>
    </div>
  );
};
