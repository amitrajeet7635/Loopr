
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

  return url.toString(); // Returns: solana:<address>?amount=...&memo=...
}
