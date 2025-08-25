import React from 'react';

interface Plan {
  id: string;
  name: string;
  priceUSD: number;
  frequency: 'monthly' | 'quarterly' | 'yearly';
  features: string[];
}

interface PlanCardProps {
  plan: Plan;
  onSelect: () => void;
  isPopular?: boolean;
}

export const PlanCard: React.FC<PlanCardProps> = ({ plan, onSelect, isPopular = false }) => {
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

  return (
    <div className={`
      relative group bg-gray-800/50 backdrop-blur-sm border rounded-xl p-8 transition-all duration-500 hover:scale-105 hover:-translate-y-2
      ${isPopular 
        ? 'border-purple-500 shadow-lg shadow-purple-500/20 animate-glow' 
        : 'border-gray-700 hover:border-purple-500/50 hover:shadow-lg hover:shadow-purple-500/10'
      }
    `}>
      {/* Animated background gradient */}
      <div className="absolute inset-0 bg-gradient-to-br from-purple-600/5 via-blue-600/5 to-purple-600/5 rounded-xl opacity-0 group-hover:opacity-100 transition-opacity duration-500"></div>
      
      {isPopular && (
        <div className="absolute -top-4 left-1/2 transform -translate-x-1/2">
          <span className="bg-gradient-to-r from-purple-500 to-blue-500 text-white px-4 py-1 rounded-full text-sm font-semibold animate-pulse">
            Most Popular
          </span>
        </div>
      )}
      
      <div className="relative z-10">
        <div className="text-center mb-6">
          {/* Plan icon */}
          <div className="w-16 h-16 mx-auto mb-4 bg-gradient-to-r from-purple-500 to-blue-500 rounded-full flex items-center justify-center group-hover:animate-float">
            <svg className="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              {plan.id.includes('starter') && (
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 10V3L4 14h7v7l9-11h-7z" />
              )}
              {plan.id.includes('pro') && (
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
              )}
              {plan.id.includes('enterprise') && (
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
              )}
            </svg>
          </div>
          
          <h3 className="text-2xl font-bold text-white mb-2 group-hover:text-transparent group-hover:bg-gradient-to-r group-hover:from-purple-400 group-hover:to-blue-400 group-hover:bg-clip-text transition-all duration-300">
            {plan.name}
          </h3>
          <div className="flex items-baseline justify-center space-x-1">
            <span className="text-4xl font-bold text-white">${plan.priceUSD}</span>
            <span className="text-gray-400">{getFrequencyDisplay(plan.frequency)}</span>
          </div>
        </div>

        <div className="space-y-4 mb-8">
          {plan.features.map((feature, index) => (
            <div key={index} className="flex items-center space-x-3 group-hover:scale-105 transition-transform duration-300" style={{transitionDelay: `${index * 50}ms`}}>
              <div className="flex-shrink-0">
                <svg className="w-5 h-5 text-green-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
                </svg>
              </div>
              <span className="text-gray-300">{feature}</span>
            </div>
          ))}
        </div>

        <button
          onClick={onSelect}
          className={`
            group/btn relative w-full py-3 px-6 rounded-lg font-semibold transition-all duration-300 transform hover:scale-105 overflow-hidden
            ${isPopular
              ? 'bg-gradient-to-r from-purple-500 to-blue-500 text-white hover:from-purple-600 hover:to-blue-600 shadow-lg hover:shadow-xl hover:shadow-purple-500/25'
              : 'border border-gray-600 text-gray-300 hover:border-purple-500 hover:text-white hover:bg-gradient-to-r hover:from-purple-500/10 hover:to-blue-500/10'
            }
          `}
        >
          {/* Button shimmer effect */}
          <div className="absolute inset-0 bg-gradient-to-r from-transparent via-white/10 to-transparent transform -skew-x-12 -translate-x-full group-hover/btn:translate-x-full transition-transform duration-1000"></div>
          
          <span className="relative flex items-center justify-center space-x-2">
            <span>{isPopular ? 'Get Started' : 'Choose Plan'}</span>
            <svg className="w-4 h-4 group-hover/btn:translate-x-1 transition-transform duration-200" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 7l5 5m0 0l-5 5m5-5H6" />
            </svg>
          </span>
        </button>
      </div>
    </div>
  );
};
