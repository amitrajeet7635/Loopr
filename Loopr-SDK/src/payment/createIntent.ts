
import { PaymentIntentConfig } from './types';
import { encodeURL } from '@solana/pay';
import { PublicKey } from '@solana/web3.js';
import { BigNumber } from 'bignumber.js';


export function createPaymentIntent(config: PaymentIntentConfig): string {
  const { recipient, amount, frequency, label, message, metadata } = config;

  const recipientPK = new PublicKey(recipient);

  // Prepare memo (this is how we embed extra metadata)
  const memo = JSON.stringify({
    frequency,
    ...metadata
  });

  const url = encodeURL({
    recipient: recipientPK,
    amount: new BigNumber(amount),
    label,
    message,
    memo
  });

  // Replace 'solana:' prefix with 'loopr://setautopay?' for Loopr mobile app compatibility
  const urlString = url.toString();
  return urlString.replace(/^solana:/, 'loopr://setautopay?'); // Returns: loopr://setautopay?<address>&amount=...&memo=...
}
