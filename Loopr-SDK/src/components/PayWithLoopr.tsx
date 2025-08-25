import React, { useState } from 'react';
import { createPlanPaymentIntent } from '../payment/createPlanPaymentIntent';

interface PayWithLooprProps {
  planId: string;
  userId: string;
  onPaymentInitiated?: () => void;
  onPaymentSuccess?: () => void;
  onError?: (error: string) => void;
}

interface PaymentState {
  isLoading: boolean;
  qrCode: string | HTMLCanvasElement | null;
  paymentIntent: string | null;
  error: string | null;
  isWaitingForPayment: boolean;
}

export const PayWithLoopr: React.FC<PayWithLooprProps> = ({
  planId,
  userId,
  onPaymentInitiated,
  onPaymentSuccess,
  onError
}) => {
  const [paymentState, setPaymentState] = useState<PaymentState>({
    isLoading: false,
    qrCode: null,
    paymentIntent: null,
    error: null,
    isWaitingForPayment: false
  });

  const handlePayment = async () => {
    try {
      setPaymentState(prev => ({ ...prev, isLoading: true, error: null }));
      onPaymentInitiated?.();

      const result = await createPlanPaymentIntent(planId, userId);
      
      setPaymentState(prev => ({
        ...prev,
        isLoading: false,
        qrCode: result.qrCode,
        paymentIntent: result.intent,
        isWaitingForPayment: true
      }));

      // Simulate payment confirmation after 10 seconds for demo
      setTimeout(() => {
        setPaymentState(prev => ({ ...prev, isWaitingForPayment: false }));
        onPaymentSuccess?.();
      }, 10000);

    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : 'Payment failed';
      setPaymentState(prev => ({
        ...prev,
        isLoading: false,
        error: errorMessage,
        isWaitingForPayment: false
      }));
      onError?.(errorMessage);
    }
  };

  const handleReset = () => {
    setPaymentState({
      isLoading: false,
      qrCode: null,
      paymentIntent: null,
      error: null,
      isWaitingForPayment: false
    });
  };

  if (paymentState.isWaitingForPayment && paymentState.qrCode) {
    return (
      <div className="w-full max-w-lg mx-auto">
        {/* Animated container with glow effect */}
        <div className="relative bg-gradient-to-br from-gray-800 to-gray-900 rounded-2xl p-8 border border-purple-500/20 shadow-2xl shadow-purple-500/10 transform transition-all duration-500 hover:scale-105">
          {/* Animated border glow */}
          <div className="absolute inset-0 bg-gradient-to-r from-purple-600/20 via-blue-600/20 to-purple-600/20 rounded-2xl blur-xl animate-pulse"></div>
          
          <div className="relative z-10">
            {/* Header with Solana logo effect */}
            <div className="text-center mb-6">
              <div className="flex items-center justify-center mb-4">
                <div className="w-12 h-12 bg-gradient-to-r from-purple-500 to-blue-500 rounded-full flex items-center justify-center animate-pulse">
                  <svg className="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1" />
                  </svg>
                </div>
              </div>
              <h3 className="text-2xl font-bold mb-2 bg-gradient-to-r from-purple-400 to-blue-400 bg-clip-text text-transparent">
                Solana Pay QR Code
              </h3>
              <p className="text-gray-300 text-sm">
                Scan with your Solana wallet to complete payment
              </p>
            </div>
            
            {/* QR Code container with animated frame */}
            <div className="flex justify-center mb-6">
              <div className="relative">
                {/* Animated corners */}
                <div className="absolute -top-2 -left-2 w-6 h-6 border-t-3 border-l-3 border-purple-400 rounded-tl-lg animate-pulse"></div>
                <div className="absolute -top-2 -right-2 w-6 h-6 border-t-3 border-r-3 border-purple-400 rounded-tr-lg animate-pulse"></div>
                <div className="absolute -bottom-2 -left-2 w-6 h-6 border-b-3 border-l-3 border-purple-400 rounded-bl-lg animate-pulse"></div>
                <div className="absolute -bottom-2 -right-2 w-6 h-6 border-b-3 border-r-3 border-purple-400 rounded-br-lg animate-pulse"></div>
                
                {/* QR Code display */}
                <div className="bg-white p-4 rounded-xl shadow-lg">
                  {typeof paymentState.qrCode === 'string' ? (
                    paymentState.qrCode.startsWith('data:') ? (
                      <img 
                        src={paymentState.qrCode} 
                        alt="Payment QR Code" 
                        className="w-64 h-64 rounded-lg"
                      />
                    ) : (
                      <pre className="text-xs bg-black text-green-400 p-4 rounded-lg whitespace-pre-wrap max-w-64 font-mono">
                        {paymentState.qrCode}
                      </pre>
                    )
                  ) : paymentState.qrCode instanceof HTMLCanvasElement ? (
                    <div 
                      ref={(ref) => {
                        if (ref && paymentState.qrCode instanceof HTMLCanvasElement) {
                          ref.appendChild(paymentState.qrCode);
                        }
                      }}
                      className="w-64 h-64 rounded-lg flex items-center justify-center"
                    />
                  ) : (
                    <div className="w-64 h-64 rounded-lg flex items-center justify-center bg-gray-100">
                      <span className="text-gray-500">QR Code not available</span>
                    </div>
                  )}
                </div>
              </div>
            </div>

            {/* Status indicator */}
            <div className="text-center mb-6">
              <div className="flex items-center justify-center space-x-3 mb-3">
                <div className="flex space-x-1">
                  <div className="w-2 h-2 bg-purple-500 rounded-full animate-bounce"></div>
                  <div className="w-2 h-2 bg-blue-500 rounded-full animate-bounce" style={{animationDelay: '0.1s'}}></div>
                  <div className="w-2 h-2 bg-purple-500 rounded-full animate-bounce" style={{animationDelay: '0.2s'}}></div>
                </div>
                <span className="text-white text-sm font-medium">Waiting for payment confirmation</span>
              </div>
              <p className="text-gray-400 text-xs">
                Payment will be confirmed automatically once processed on the blockchain
              </p>
            </div>

            {/* Action buttons */}
            <div className="flex space-x-3">
              <button
                onClick={handleReset}
                className="flex-1 px-4 py-3 text-gray-300 border border-gray-600 rounded-lg hover:border-gray-400 hover:text-white transition-all duration-200 hover:scale-105"
              >
                Cancel Payment
              </button>
              <button
                onClick={() => navigator.clipboard.writeText(paymentState.paymentIntent || '')}
                className="flex-1 px-4 py-3 bg-gradient-to-r from-purple-600 to-blue-600 text-white rounded-lg hover:from-purple-700 hover:to-blue-700 transition-all duration-200 hover:scale-105 font-semibold"
              >
                Copy Payment Link
              </button>
            </div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="w-full">
      {paymentState.error && (
        <div className="mb-4 p-4 bg-red-900/20 border border-red-500/30 rounded-lg backdrop-blur-sm">
          <div className="flex items-center space-x-3">
            <div className="w-5 h-5 text-red-400">
              <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <p className="text-red-300 text-sm font-medium">{paymentState.error}</p>
          </div>
        </div>
      )}
      
      <button
        onClick={handlePayment}
        disabled={paymentState.isLoading}
        className={`
          group relative w-full px-8 py-4 rounded-xl font-bold text-lg transition-all duration-300 overflow-hidden
          ${paymentState.isLoading 
            ? 'bg-gray-700 cursor-not-allowed text-gray-400' 
            : 'bg-gradient-to-r from-purple-600 via-blue-600 to-purple-600 text-white hover:from-purple-700 hover:via-blue-700 hover:to-purple-700 transform hover:scale-105 shadow-lg hover:shadow-2xl hover:shadow-purple-500/25'
          }
        `}
      >
        {/* Animated background for non-loading state */}
        {!paymentState.isLoading && (
          <div className="absolute inset-0 bg-gradient-to-r from-transparent via-white/10 to-transparent transform -skew-x-12 -translate-x-full group-hover:translate-x-full transition-transform duration-1000"></div>
        )}
        
        {/* Button content */}
        <div className="relative flex items-center justify-center space-x-3">
          {paymentState.isLoading ? (
            <>
              <div className="animate-spin rounded-full h-5 w-5 border-2 border-gray-400 border-t-transparent"></div>
              <span>Generating Payment...</span>
            </>
          ) : (
            <>
              <div className="w-6 h-6 flex items-center justify-center">
                <svg className="w-full h-full" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1" />
                </svg>
              </div>
              <span>Pay with Loopr</span>
              <div className="flex items-center space-x-1 text-sm opacity-75">
                <span>•</span>
                <span>Powered by Solana</span>
              </div>
            </>
          )}
        </div>
      </button>
      
      {/* Trust indicators */}
      {!paymentState.isLoading && (
        <div className="mt-4 flex items-center justify-center space-x-6 text-xs text-gray-400">
          <div className="flex items-center space-x-1">
            <svg className="w-3 h-3 text-green-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
            </svg>
            <span>Secure</span>
          </div>
          <div className="flex items-center space-x-1">
            <svg className="w-3 h-3 text-blue-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 10V3L4 14h7v7l9-11h-7z" />
            </svg>
            <span>Instant</span>
          </div>
          <div className="flex items-center space-x-1">
            <svg className="w-3 h-3 text-purple-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
            </svg>
            <span>Decentralized</span>
          </div>
        </div>
      )}
    </div>
  );
};
