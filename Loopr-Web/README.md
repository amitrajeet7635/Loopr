# Loopr Web Frontend

A modern React frontend for the Loopr payment platform, built with Vite, TypeScript, and Tailwind CSS.

## Features

- 🚀 Fast development with Vite
- ⚡ Built with React 19 and TypeScript
- 🎨 Styled with Tailwind CSS v4
- 🔗 Integrated with Loopr SDK for Solana payments
- 📱 Responsive design
- 🌐 Web3 payment infrastructure

## Getting Started

### Prerequisites

- Node.js 18+ 
- npm or yarn

### Installation

```bash
# Install dependencies
npm install

# Start development server
npm run dev

# Build for production
npm run build

# Preview production build
npm run preview
```

## Project Structure

```
src/
├── components/     # Reusable UI components
├── pages/         # Page components
├── types/         # TypeScript type definitions
├── assets/        # Static assets
└── index.css      # Global styles
```

## Scripts

- `npm run dev` - Start development server
- `npm run build` - Build for production
- `npm run preview` - Preview production build
- `npm run lint` - Run ESLint

## Technologies

- **React 19** - UI framework
- **TypeScript** - Type safety
- **Vite** - Build tool and dev server
- **Tailwind CSS v4** - Styling
- **React Router** - Client-side routing
- **Loopr SDK** - Solana payment integration

## Payment Integration

This frontend integrates with the Loopr SDK to provide seamless Solana-based payments. The SDK handles:

- Payment intent creation
- QR code generation for Solana Pay
- Subscription plan management
- Currency conversion

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Run tests and linting
5. Submit a pull request
