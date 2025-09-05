import { createPlanPaymentIntent } from './src/payment/createPlanPaymentIntent';

(async () => {
  try {
    const planId = 'pro-monthly';
    const userId = 'user_abc_123';

    const { plan, intent, qrCode } = await createPlanPaymentIntent(planId, userId);

    console.log('✅ Plan details:', plan);
    console.log('✅ Generated Solana Pay URI:', intent);
    console.log('✅ QR code generated:', typeof qrCode === 'string' ? 'Data URL' : 'Canvas element');
  } catch (err) {
    console.error('❌ Error creating payment intent:', err);
  }
})();
