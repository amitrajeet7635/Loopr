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

      console.log('Creating payment intent for plan:', planId, 'user:', userId);
      const result = await createPlanPaymentIntent(planId, userId);
      console.log('Payment intent result:', result);
      console.log('QR Code type:', typeof result.qrCode);
      console.log('QR Code instanceof HTMLCanvasElement:', result.qrCode instanceof HTMLCanvasElement);
      
      // Additional validation
      if (!result.qrCode) {
        throw new Error('QR Code generation failed: No QR code returned');
      }
      
      if (typeof result.qrCode === 'string' && result.qrCode.length < 10) {
        throw new Error('QR Code generation failed: Invalid QR code data');
      }
      
      if (result.qrCode instanceof HTMLCanvasElement) {
        console.log('Canvas dimensions:', result.qrCode.width, 'x', result.qrCode.height);
        if (result.qrCode.width === 0 || result.qrCode.height === 0) {
          throw new Error('QR Code generation failed: Invalid canvas dimensions');
        }
      }
      
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
      console.error('Payment error:', error);
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
                Loopr QR Code
              </h3>
              <p className="text-gray-300 text-sm">
                Open the Loopr mobile app to scan the QR code
              </p>
            </div>
            
            {/* QR Code container with animated frame */}
            <div className="flex justify-center mb-8">
              <div className="relative">
                {/* Animated corners */}
                <div className="absolute -top-2 -left-2 w-6 h-6 border-t-3 border-l-3 border-purple-400 rounded-tl-lg animate-pulse"></div>
                <div className="absolute -top-2 -right-2 w-6 h-6 border-t-3 border-r-3 border-purple-400 rounded-tr-lg animate-pulse"></div>
                <div className="absolute -bottom-2 -left-2 w-6 h-6 border-b-3 border-l-3 border-purple-400 rounded-bl-lg animate-pulse"></div>
                <div className="absolute -bottom-2 -right-2 w-6 h-6 border-b-3 border-r-3 border-purple-400 rounded-br-lg animate-pulse"></div>
                
                {/* QR Code display */}
                <div className="bg-white p-6 rounded-xl shadow-2xl">
                  {paymentState.qrCode ? (
                    <div className="w-64 h-64 mx-auto flex items-center justify-center">
                      {typeof paymentState.qrCode === 'string' ? (
                        paymentState.qrCode.startsWith('data:') ? (
                          <img 
                            src={paymentState.qrCode} 
                            alt="Payment QR Code" 
                            className="w-full h-full object-contain rounded-lg"
                            style={{ maxWidth: '100%', maxHeight: '100%' }}
                          />
                        ) : (
                          <pre className="text-xs bg-black text-green-400 p-4 rounded-lg whitespace-pre-wrap max-w-full font-mono overflow-auto">
                            {paymentState.qrCode}
                          </pre>
                        )
                      ) : paymentState.qrCode instanceof HTMLCanvasElement ? (
                        (() => {
                          // Convert canvas to data URL and render as <img>
                          try {
                            const dataURL = paymentState.qrCode.toDataURL('image/png');
                            return (
                              <img
                                src={dataURL}
                                alt="Payment QR Code"
                                className="w-full h-full object-contain rounded-lg"
                                style={{ maxWidth: '100%', maxHeight: '100%' }}
                                onLoad={() => console.log('QR Code displayed successfully')}
                                onError={(e) => console.error('QR Code display error:', e)}
                              />
                            );
                          } catch (error) {
                            console.error('Error converting canvas to image:', error);
                            return (
                              <div className="w-full h-full rounded-lg flex items-center justify-center bg-gray-100">
                                <span className="text-gray-500 text-center">QR Code conversion error</span>
                              </div>
                            );
                          }
                        })()
                      ) : (
                        <div className="w-full h-full rounded-lg flex items-center justify-center bg-gray-100">
                          <span className="text-gray-500 text-center">QR Code format not supported</span>
                        </div>
                      )}
                    </div>
                  ) : (
                    <div className="w-64 h-64 rounded-lg flex items-center justify-center bg-gray-100">
                      <span className="text-gray-500">QR Code not available</span>
                    </div>
                  )}
                </div>
              </div>
            </div>

            {/* Status indicator */}
            <div className="text-center mb-8">
              <div className="flex items-center justify-center space-x-3 mb-4">
                <div className="flex space-x-1">
                  <div className="w-2 h-2 bg-purple-500 rounded-full animate-bounce"></div>
                  <div className="w-2 h-2 bg-blue-500 rounded-full animate-bounce" style={{animationDelay: '0.1s'}}></div>
                  <div className="w-2 h-2 bg-purple-500 rounded-full animate-bounce" style={{animationDelay: '0.2s'}}></div>
                </div>
                <span className="text-white text-base font-medium">Waiting for payment confirmation</span>
              </div>
              <p className="text-gray-400 text-sm leading-relaxed max-w-sm mx-auto">
                Payment will be confirmed automatically once processed on the blockchain
              </p>
            </div>

            {/* Action buttons */}
            <div className="flex flex-col space-y-3">
              <button
                onClick={() => navigator.clipboard.writeText(paymentState.paymentIntent || '')}
                className="w-full px-6 py-3 bg-gradient-to-r from-purple-600 to-blue-600 text-white font-semibold rounded-lg hover:from-purple-700 hover:to-blue-700 transition-all duration-300 transform hover:scale-105 shadow-lg hover:shadow-purple-500/25 flex items-center justify-center space-x-2"
              >
                <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 16H6a2 2 0 01-2-2V6a2 2 0 012-2h8a2 2 0 012 2v2m-6 12h8a2 2 0 002-2v-8a2 2 0 00-2-2h-8a2 2 0 00-2 2v8a2 2 0 002 2z" />
                </svg>
                <span>Copy Payment Link</span>
              </button>
              <button
                onClick={handleReset}
                className="w-full px-6 py-3 bg-gray-800 text-gray-300 font-medium rounded-lg border border-gray-600 hover:bg-gray-700 hover:text-white hover:border-gray-500 transition-all duration-300 transform hover:scale-105 flex items-center justify-center space-x-2"
              >
                <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                </svg>
                <span>Cancel Payment</span>
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
      
      {/* Enhanced Pay with Loopr Button */}
      <div className="relative mb-4">
        {/* Multiple glowing background effects for enhanced visibility */}
        <div className="absolute -inset-2 bg-gradient-to-r from-purple-500 via-blue-500 to-purple-500 rounded-xl blur-lg opacity-90 animate-pulse"></div>
        <div className="absolute -inset-1 bg-gradient-to-r from-purple-400 via-blue-400 to-purple-400 rounded-xl blur-md opacity-75"></div>
        
        <button
          onClick={handlePayment}
          disabled={paymentState.isLoading}
          className={`
            group relative w-full px-8 py-5 rounded-xl font-bold text-xl transition-all duration-300 overflow-hidden border-3 shadow-2xl
            ${paymentState.isLoading 
              ? 'bg-gray-700 cursor-not-allowed text-gray-400 border-gray-600' 
              : 'bg-gradient-to-r from-purple-600 via-blue-600 to-purple-600 text-white hover:from-purple-500 hover:via-blue-500 hover:to-purple-500 transform hover:scale-110 shadow-purple-500/50 border-purple-300 hover:border-white hover:shadow-purple-400/60'
            }
          `}
        >
          {/* Enhanced animated background for non-loading state */}
          {!paymentState.isLoading && (
            <>
              <div className="absolute inset-0 bg-gradient-to-r from-transparent via-white/30 to-transparent transform -skew-x-12 -translate-x-full group-hover:translate-x-full transition-transform duration-1000"></div>
              {/* Additional bright glow effect */}
              <div className="absolute inset-0 bg-gradient-to-r from-purple-400/60 via-blue-400/60 to-purple-400/60 opacity-0 group-hover:opacity-100 transition-opacity duration-300"></div>
              {/* Pulsing border effect */}
              <div className="absolute inset-0 rounded-xl border-2 border-white/50 opacity-0 group-hover:opacity-100 animate-pulse"></div>
            </>
          )}
          
          {/* Button content with enhanced styling */}
          <div className="relative flex items-center justify-center space-x-3">
            {paymentState.isLoading ? (
              <>
                <div className="animate-spin rounded-full h-6 w-6 border-2 border-gray-400 border-t-transparent"></div>
                <span className="text-lg">Processing Payment...</span>
              </>
            ) : (
              <>
                <div className="w-7 h-7 flex items-center justify-center bg-white/30 rounded-full border border-white/20">
                  <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={3} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1" />
                  </svg>
                </div>
                <span className="tracking-wide text-xl font-extrabold">Pay with Loopr</span>
                <div className="w-3 h-3 bg-green-400 rounded-full animate-pulse shadow-lg shadow-green-400/50"></div>
              </>
            )}
          </div>
        </button>
      </div>
      
      {/* Trust indicators */}
      {!paymentState.isLoading && (
        <div className="mt-4 p-4 bg-gray-800/30 backdrop-blur-sm rounded-lg border border-purple-500/20">
          <div className="flex items-center justify-center space-x-6 text-sm text-gray-300">
            <div className="flex items-center space-x-2">
              <svg className="w-4 h-4 text-green-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
              </svg>
              <span className="font-medium">Secure Payment</span>
            </div>
            <div className="flex items-center space-x-2">
              <svg className="w-4 h-4 text-blue-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 10V3L4 14h7v7l9-11h-7z" />
              </svg>
              <span className="font-medium">Lightning Fast</span>
            </div>
            <div className="flex items-center space-x-2">
              <svg className="w-4 h-4 text-purple-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
              <span className="font-medium">Blockchain Verified</span>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
