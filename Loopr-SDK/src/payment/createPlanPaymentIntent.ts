import { getPlanById } from '../plans/manager';
import { convertUsdToSol } from '../utils/convertCurrency';
import { createPaymentIntent } from './createIntent';
import { generateQRCode } from '../qr/generateQR';

export async function createPlanPaymentIntent(planId: string, userId: string) {
  const plan = getPlanById(planId);
  if (!plan) {
    throw new Error(`Invalid planId: ${planId}`);
  }

  const amountInSOL = await convertUsdToSol(plan.priceUSD);

  const intent = createPaymentIntent({
    recipient: plan.merchantWallet, 
    amount: amountInSOL,
    frequency: plan.frequency,
    label: plan.name,
    message: `Subscription for ${plan.name}`,
    metadata: {
      planId: plan.id,
      userId,
    },
  });

  const qrCode = await generateQRCode(intent);

  return {
    plan,
    intent,
    qrCode,
  };
}
