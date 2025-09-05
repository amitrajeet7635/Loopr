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
      <div className="w-full max-w-2xl mx-auto">
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
                
                {/* QR Code display - larger size */}
                <div className="bg-white p-4 rounded-xl shadow-2xl">
                  {paymentState.qrCode ? (
                    <div className="w-80 h-80 mx-auto flex items-center justify-center">
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
                    <div className="w-80 h-80 rounded-lg flex items-center justify-center bg-gray-100">
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
      
      {/* Premium Glass Morphism Pay with Loopr Button */}
      <div className="relative mb-4">
        {/* Outer glow effects */}
        <div className="absolute -inset-6 bg-gradient-to-r from-purple-500/40 via-blue-500/40 to-purple-500/40 rounded-3xl blur-3xl opacity-75 animate-pulse"></div>
        <div className="absolute -inset-4 bg-gradient-to-r from-purple-400/30 via-blue-400/30 to-purple-400/30 rounded-2xl blur-2xl opacity-60"></div>
        
        {/* Main button container with glass morphism */}
        <div className="relative">
          {/* Glass background */}
          <div className="absolute inset-0 bg-gradient-to-br from-white/20 via-white/10 to-white/5 backdrop-blur-xl rounded-2xl border border-white/30 shadow-2xl"></div>
          
          {/* Inner glass reflection */}
          <div className="absolute inset-[1px] bg-gradient-to-br from-white/10 to-transparent rounded-2xl"></div>
          
          {/* Top highlight reflection */}
          <div className="absolute top-[1px] left-[1px] right-[1px] h-8 bg-gradient-to-r from-transparent via-white/40 to-transparent rounded-t-2xl opacity-60"></div>
          
          <button
            onClick={handlePayment}
            disabled={paymentState.isLoading}
            className={`
              group relative w-full px-10 py-6 rounded-2xl font-black text-2xl transition-all duration-500 overflow-hidden transform
              ${paymentState.isLoading 
                ? 'cursor-not-allowed scale-100 bg-gradient-to-br from-gray-700/80 via-gray-600/80 to-gray-700/80 text-gray-300 backdrop-blur-xl' 
                : 'cursor-pointer hover:scale-[1.02] active:scale-[0.98] bg-gradient-to-br from-purple-600/90 via-blue-600/90 to-purple-600/90 text-white hover:from-purple-500/95 hover:via-blue-500/95 hover:to-purple-500/95 shadow-2xl hover:shadow-purple-500/50 backdrop-blur-xl'
              }
            `}
          >
            {/* Enhanced glass effects for non-loading state */}
            {!paymentState.isLoading && (
              <>
                {/* Animated shimmer reflection */}
                <div className="absolute inset-0 bg-gradient-to-r from-transparent via-white/50 to-transparent transform -skew-x-12 -translate-x-full group-hover:translate-x-full transition-transform duration-1200 ease-out"></div>
                
                {/* Hover glow overlay */}
                <div className="absolute inset-0 bg-gradient-to-br from-white/20 via-transparent to-white/10 opacity-0 group-hover:opacity-100 transition-opacity duration-500 rounded-2xl"></div>
                
                {/* Inner shadow for depth */}
                <div className="absolute inset-0 rounded-2xl shadow-inner shadow-black/30"></div>
                
                {/* Active press effect */}
                <div className="absolute inset-0 rounded-2xl bg-gradient-to-br from-black/20 to-black/10 opacity-0 group-active:opacity-100 transition-opacity duration-100"></div>
                
                {/* Pulsing border highlight */}
                <div className="absolute inset-0 rounded-2xl border-2 border-white/50 opacity-0 group-hover:opacity-100 transition-opacity duration-300 animate-pulse"></div>
              </>
            )}
            
            {/* Button content with glass effect */}
            <div className="relative flex items-center justify-center space-x-4 z-10">
              {paymentState.isLoading ? (
                <>
                  <div className="animate-spin rounded-full h-8 w-8 border-3 border-gray-300 border-t-transparent shadow-lg"></div>
                  <span className="text-xl font-bold tracking-wide drop-shadow-lg">Processing Payment...</span>
                </>
              ) : (
                <>
                  {/* Icon with glass effect */}
                  <div className="w-10 h-10 flex items-center justify-center bg-gradient-to-br from-white/30 via-white/20 to-white/10 rounded-full border border-white/40 shadow-xl backdrop-blur-sm">
                    <div className="absolute inset-0 bg-gradient-to-br from-white/20 to-transparent rounded-full"></div>
                    <svg className="w-6 h-6 relative z-10 drop-shadow-lg" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={3} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1" />
                    </svg>
                  </div>
                  
                  {/* Text with enhanced styling */}
                  <span className="tracking-wide text-2xl font-black drop-shadow-2xl bg-gradient-to-r from-white via-blue-100 to-white bg-clip-text text-transparent filter brightness-110">
                    Pay with Loopr
                  </span>
                  
                  {/* Status indicator with glass effect */}
                  <div className="relative w-5 h-5">
                    <div className="absolute inset-0 bg-gradient-to-br from-green-400 via-green-300 to-green-500 rounded-full shadow-xl shadow-green-400/60 border border-green-200/50 backdrop-blur-sm">
                      <div className="absolute inset-[1px] bg-gradient-to-br from-white/30 to-transparent rounded-full"></div>
                      <div className="absolute inset-0 bg-green-300/80 rounded-full animate-ping opacity-75"></div>
                    </div>
                  </div>
                </>
              )}
            </div>
            
            {/* Bottom reflection line */}
            <div className="absolute bottom-0 left-1/4 right-1/4 h-[1px] bg-gradient-to-r from-transparent via-white/60 to-transparent"></div>
          </button>
        </div>
      </div>
      
      {/* Premium Trust Indicators with Glass Morphism */}
      {!paymentState.isLoading && (
        <div className="mt-6 relative">
          {/* Background glow */}
          <div className="absolute -inset-2 bg-gradient-to-r from-purple-500/20 via-blue-500/20 to-purple-500/20 rounded-2xl blur-xl"></div>
          
          {/* Glass container */}
          <div className="relative p-6 bg-gradient-to-br from-white/10 via-white/5 to-white/10 backdrop-blur-xl rounded-2xl border border-white/20 shadow-2xl">
            {/* Top reflection */}
            <div className="absolute top-[1px] left-[1px] right-[1px] h-6 bg-gradient-to-r from-transparent via-white/30 to-transparent rounded-t-2xl"></div>
            
            <div className="relative z-10">
              <div className="flex items-center justify-center space-x-6 text-sm text-gray-200 mb-4">
                <div className="flex items-center space-x-2 bg-gradient-to-br from-green-900/40 via-green-800/30 to-green-900/40 px-4 py-3 rounded-xl border border-green-400/30 backdrop-blur-sm shadow-xl">
                  <div className="relative">
                    <svg className="w-5 h-5 text-green-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
                    </svg>
                    <div className="absolute inset-0 bg-green-300/30 rounded-full blur-sm animate-pulse"></div>
                  </div>
                  <span className="font-bold text-green-200">Secure Payment</span>
                </div>
                
                <div className="flex items-center space-x-2 bg-gradient-to-br from-blue-900/40 via-blue-800/30 to-blue-900/40 px-4 py-3 rounded-xl border border-blue-400/30 backdrop-blur-sm shadow-xl">
                  <div className="relative">
                    <svg className="w-5 h-5 text-blue-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 10V3L4 14h7v7l9-11h-7z" />
                    </svg>
                    <div className="absolute inset-0 bg-blue-300/30 rounded-full blur-sm animate-pulse"></div>
                  </div>
                  <span className="font-bold text-blue-200">Lightning Fast</span>
                </div>
                
                <div className="flex items-center space-x-2 bg-gradient-to-br from-purple-900/40 via-purple-800/30 to-purple-900/40 px-4 py-3 rounded-xl border border-purple-400/30 backdrop-blur-sm shadow-xl">
                  <div className="relative">
                    <svg className="w-5 h-5 text-purple-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                    </svg>
                    <div className="absolute inset-0 bg-purple-300/30 rounded-full blur-sm animate-pulse"></div>
                  </div>
                  <span className="font-bold text-purple-200">Blockchain Verified</span>
                </div>
              </div>
              
              <div className="text-center">
                <div className="inline-flex items-center bg-gradient-to-r from-gray-800/60 via-gray-700/60 to-gray-800/60 px-6 py-3 rounded-full border border-white/20 backdrop-blur-sm shadow-lg">
                  <div className="flex items-center space-x-2">
                    <div className="w-2 h-2 bg-gradient-to-r from-yellow-400 to-yellow-300 rounded-full animate-pulse shadow-lg shadow-yellow-400/50"></div>
                    <p className="text-sm text-gray-200 font-medium">
                      Click the button above to start your secure payment
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
