# Loopr

> **Subscriptions Made Simple. Powered by Solana.**

[![License: ISC](https://img.shields.io/badge/License-ISC-blue.svg)](https://opensource.org/licenses/ISC)
[![TypeScript](https://img.shields.io/badge/TypeScript-007ACC?logo=typescript&logoColor=white)](https://www.typescriptlang.org/)
[![Solana](https://img.shields.io/badge/Solana-9945FF?logo=solana&logoColor=white)](https://solana.com/)
[![React](https://img.shields.io/badge/React-20232A?logo=react&logoColor=61DAFB)](https://reactjs.org/)

Loopr is a modern, decentralized subscription management platform built on the Solana blockchain. It provides a seamless way for businesses to create and manage recurring payment subscriptions while offering users a secure, transparent, and cost-effective payment experience.

## ✨ Features

- **🔗 Blockchain-Powered**: Built on Solana for fast, low-cost transactions
- **🔄 Flexible Subscriptions**: Support for monthly, quarterly, and yearly billing cycles
- **📱 Multi-Platform**: Web application, SDK, and Android app
- **🎯 QR Code Payments**: Generate QR codes for easy mobile payments
- **🛡️ Secure**: Leverages Solana Pay protocol for secure transactions
- **⚡ Fast**: Near-instant transaction confirmations
- **💰 Low Fees**: Minimal transaction costs compared to traditional payment processors

## 🏗️ Architecture

Loopr consists of three main components:

```
loopr/
├── Loopr-SDK/          # TypeScript SDK for payment integration
├── Loopr-Web/          # React web application
└── Loopr-android/      # Android mobile application
```

### 📦 Components

| Component | Description | Technology Stack |
|-----------|-------------|------------------|
| **Loopr-SDK** | Core TypeScript SDK for payment processing and QR code generation | TypeScript, Solana Pay, QRCode.js |
| **Loopr-Web** | Web interface for managing subscriptions | React, TypeScript, Vite, TailwindCSS |
| **Loopr-Android** | Native Android application | Kotlin/Java, Android SDK |

## 🚀 Quick Start

### Prerequisites

- Node.js 18+ and npm
- Solana CLI tools (for development)
- A Solana wallet (Phantom, Solflare, etc.)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/amitrajeet7635/Loopr.git
   cd Loopr
   ```

2. **Install SDK dependencies**
   ```bash
   cd Loopr-SDK
   npm install
   npm run build
   ```

3. **Install Web app dependencies**
   ```bash
   cd ../Loopr-Web
   npm install
   ```

4. **Start the development server**
   ```bash
   npm run dev
   ```

The web application will be available at `http://localhost:5173`

## 📖 Usage

### Using the SDK

```typescript
import { createPaymentIntent } from 'loopr-sdk';

// Create a subscription payment intent
const paymentConfig = {
  recipient: 'merchant_wallet_address_here',
  amount: 9.99, // Amount in SOL
  frequency: 'monthly',
  label: 'Premium Subscription',
  message: 'Monthly premium plan access',
  metadata: {
    planId: 'premium-monthly',
    userId: 'user123'
  }
};

const paymentURI = createPaymentIntent(paymentConfig);
console.log('Payment URI:', paymentURI);
```

### Generating QR Codes

```typescript
import { generateQRCode } from 'loopr-sdk';

const canvas = await generateQRCode(paymentURI);
document.body.appendChild(canvas);
```

### Supported Subscription Frequencies

- `monthly` - Recurring monthly payments
- `quarterly` - Recurring quarterly payments  
- `yearly` - Recurring annual payments

## 🛠️ Development

### Setting up the Development Environment

1. **Install dependencies for all components:**
   ```bash
   # SDK
   cd Loopr-SDK && npm install
   
   # Web App
   cd ../Loopr-Web && npm install
   
   # Return to root
   cd ..
   ```

2. **Build the SDK (required for other components):**
   ```bash
   cd Loopr-SDK
   npm run build
   ```

3. **Start development servers:**
   ```bash
   # Web App (from Loopr-Web directory)
   npm run dev
   ```

### Code Quality

The project uses ESLint and TypeScript for code quality:

```bash
# Lint the web application
cd Loopr-Web
npm run lint

# Build with type checking
npm run build
```

### Project Structure

```
Loopr-SDK/
├── src/
│   ├── payment/          # Payment intent creation
│   ├── qr/              # QR code generation
│   └── index.ts         # Main exports
├── dist/                # Built SDK
└── package.json

Loopr-Web/
├── src/
│   ├── components/      # React components
│   ├── pages/          # Application pages
│   └── App.tsx         # Main application
├── public/             # Static assets
└── package.json

Loopr-android/
├── app/                # Android app source
├── gradle/             # Gradle configuration
└── build.gradle.kts    # Build configuration
```

## 🤝 Contributing

We welcome contributions to Loopr! Please follow these steps:

1. **Fork the repository**
2. **Create a feature branch**: `git checkout -b feature/amazing-feature`
3. **Commit your changes**: `git commit -m 'Add amazing feature'`
4. **Push to the branch**: `git push origin feature/amazing-feature`
5. **Open a Pull Request**

### Development Guidelines

- Follow TypeScript best practices
- Write clear, descriptive commit messages
- Add tests for new functionality
- Update documentation as needed
- Ensure code passes linting and type checking

## 📄 License

This project is licensed under the ISC License. See the [LICENSE](LICENSE) file for details.

## 🔗 Links

- **Documentation**: [Coming Soon]
- **API Reference**: [Coming Soon]
- **Solana Pay**: https://solanapay.com/
- **Solana Developer Docs**: https://docs.solana.com/

## 🆘 Support

For support and questions:

- **Issues**: [GitHub Issues](https://github.com/amitrajeet7635/Loopr/issues)
- **Discussions**: [GitHub Discussions](https://github.com/amitrajeet7635/Loopr/discussions)

## 🚧 Roadmap

- [ ] Enhanced subscription management features
- [ ] Multi-token support (USDC, USDT)
- [ ] Advanced analytics dashboard
- [ ] Mobile SDK for iOS
- [ ] Plugin system for e-commerce platforms
- [ ] Automated subscription renewals

---

**Built with ❤️ for the Solana ecosystem**
