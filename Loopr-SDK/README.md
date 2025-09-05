# Loopr SDK

A TypeScript SDK for Loopr subscription payments on Solana blockchain.

## Installation

```bash
npm install loopr-sdk
```

## Usage

```typescript
import { PayWithLoopr, createPlanPaymentIntent } from 'loopr-sdk';

// Use the payment component
<PayWithLoopr 
  planId="your-plan-id"
  userId="user-id"
  onPaymentSuccess={() => console.log('Payment successful!')}
/>

// Create payment intent programmatically
const { plan, intent, qrCode } = await createPlanPaymentIntent('plan-id', 'user-id');
```

## Features

- 🔐 Secure Solana blockchain payments
- 📱 QR code generation for mobile payments
- 🔄 Subscription management
- ⚛️ React components ready to use
- 📦 TypeScript support

## License

ISC
