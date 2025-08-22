import { PaymentIntentConfig } from './src';
import { createPaymentIntent } from './src/payment/createIntent';
import * as QRCode from 'qrcode';



const config: PaymentIntentConfig = {
    recipient: 'FFp8CuPYT1qNGEVcKA8MWh4iyoNekhJaJati8LvyeXDo',
    amount: 0.01,
    frequency: 'monthly',
    label: 'Loopr Pro Plan',
    message: 'Thank you!',
    metadata: {
      planId: 'pro-monthly',
      userId: 'user_abc_123',
    },
  };

(async () => {
  const uri = createPaymentIntent(config);
  console.log('Generated Solana Pay URI:', uri);

  await QRCode.toFile('loopr-payment.png', uri); 
  console.log('✅ QR code saved as loopr-payment.png');
})();

