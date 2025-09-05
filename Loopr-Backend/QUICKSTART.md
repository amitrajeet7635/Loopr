# Loopr Backend Quick Start Guide

This guide will help you set up, build, and deploy the Loopr backend smart contracts on Solana.

## 📋 Prerequisites

Before you begin, ensure you have the following installed:

### Required Tools

1. **Rust** (latest stable version)
   ```bash
   curl --proto '=https' --tlsv1.2 -sSf https://sh.rustup.rs | sh
   source ~/.cargo/env
   ```

2. **Solana CLI** (v1.17.0 or later)
   ```bash
   sh -c "$(curl -sSfL https://release.solana.com/v1.17.0/install)"
   export PATH="~/.local/share/solana/install/active_release/bin:$PATH"
   ```

3. **Anchor CLI** (v0.29.0)
   ```bash
   npm install -g @coral-xyz/anchor-cli
   ```

4. **Node.js** (v16 or later)
   ```bash
   # Using Node Version Manager (recommended)
   curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.0/install.sh | bash
   nvm install 16
   nvm use 16
   ```

### Verify Installation

```bash
# Check Rust
rustc --version

# Check Solana CLI
solana --version

# Check Anchor CLI
anchor --version

# Check Node.js
node --version
npm --version
```

## 🚀 Setup

### 1. Navigate to Backend Directory

```bash
cd Loopr-Backend
```

### 2. Install Dependencies

```bash
# Install Node.js dependencies
npm install

# Install Rust dependencies (automatically handled by Anchor)
```

### 3. Configure Solana

```bash
# Set Solana to use local/devnet/mainnet
solana config set --url localhost    # For local development
# OR
solana config set --url devnet       # For devnet deployment
# OR  
solana config set --url mainnet-beta # For mainnet deployment

# Generate or set your keypair
solana-keygen new --outfile ~/.config/solana/id.json
# OR if you have an existing keypair
solana config set --keypair /path/to/your/keypair.json

# Check your configuration
solana config get
```

### 4. Fund Your Account (for devnet/localhost)

```bash
# For localhost testing
solana airdrop 10 $(solana address)

# For devnet testing  
solana airdrop 2 $(solana address) --url devnet
```

## 🔨 Build

### 1. Build the Smart Contracts

```bash
# Clean previous builds
anchor clean

# Build the program
anchor build
```

This will:
- Compile the Rust smart contracts
- Generate TypeScript types in `target/types/`
- Create the program binary in `target/deploy/`

### 2. Generate Program Keypair (first time only)

```bash
# Generate a new program keypair
solana-keygen new --outfile target/deploy/loopr_subscription-keypair.json

# Get the program ID
solana address -k target/deploy/loopr_subscription-keypair.json
```

### 3. Update Program ID

Update the program ID in:

1. `Anchor.toml` - Update all program IDs
2. `programs/loopr-subscription/src/lib.rs` - Update the `declare_id!` macro

```rust
// In lib.rs
declare_id!("YOUR_GENERATED_PROGRAM_ID_HERE");
```

### 4. Rebuild After Program ID Update

```bash
anchor build
```

## 🚀 Deploy

### 1. Start Local Validator (for local testing)

```bash
# In a new terminal window
solana-test-validator
```

### 2. Deploy the Program

```bash
# Deploy to current cluster (localhost/devnet/mainnet)
anchor deploy

# Or deploy to specific cluster
anchor deploy --provider.cluster devnet
```

### 3. Verify Deployment

```bash
# Check program account
solana account $(solana address -k target/deploy/loopr_subscription-keypair.json)

# Check program deployment
anchor idl fetch $(solana address -k target/deploy/loopr_subscription-keypair.json)
```

## 🧪 Test

### 1. Run Tests

```bash
# Run all tests
anchor test

# Run tests with logs
anchor test --skip-deploy

# Run specific test file
anchor test --skip-deploy tests/qr-payment-flow.ts
```

### 2. Run Test on Devnet

```bash
# Set cluster to devnet
anchor test --provider.cluster devnet
```

## 🎯 Initialize the Program

### 1. Initialize Global State

```bash
npm run initialize
```

This script will:
- Initialize the global program state
- Set up the authority
- Create initial configuration

### 2. Create Sample Plans

```bash
npm run create-sample-plan
```

This script will:
- Create sample subscription plans (Netflix, Spotify, Disney+)
- Generate a sample payment intent for testing
- Display all created accounts and their details

## 🔧 Development Workflow

### 1. Make Changes to Smart Contracts

Edit files in `programs/loopr-subscription/src/`

### 2. Rebuild and Redeploy

```bash
anchor build
anchor deploy
```

### 3. Update Frontend Types (if applicable)

```bash
# Copy the generated types to your frontend
cp target/types/loopr_subscription.ts ../Loopr-Web/src/types/
```

### 4. Run Tests

```bash
anchor test --skip-deploy
```

## 🌐 Integration with Frontend

### 1. Install Dependencies in Frontend

```bash
cd ../Loopr-Web  # or your frontend directory
npm install @coral-xyz/anchor @solana/web3.js @solana/wallet-adapter-base
```

### 2. Copy Types

```bash
cp ../Loopr-Backend/target/types/loopr_subscription.ts src/types/
```

### 3. Use in Frontend

```typescript
import { Program } from "@coral-xyz/anchor";
import { LooprSubscription } from "./types/loopr_subscription";

// Initialize program
const program = new Program(IDL, programId, provider) as Program<LooprSubscription>;

// Call instructions
await program.methods
  .createPaymentIntent(intentId, planId, amount, expiresAt)
  .accounts({
    // ... accounts
  })
  .rpc();
```

## 🔍 Monitoring and Debugging

### 1. View Logs

```bash
# View program logs
solana logs $(solana address -k target/deploy/loopr_subscription-keypair.json)
```

### 2. Check Account Data

```bash
# Check global state
anchor account GlobalState <GLOBAL_STATE_PDA>

# Check subscription plan
anchor account SubscriptionPlan <PLAN_PDA>

# Check user subscription
anchor account UserSubscription <SUBSCRIPTION_PDA>
```

### 3. Debug Transactions

```bash
# View transaction details
solana confirm -v <TRANSACTION_SIGNATURE>
```

## 📝 Common Commands

```bash
# Full rebuild and redeploy
anchor clean && anchor build && anchor deploy

# Test specific functionality
anchor test --skip-deploy --grep "QR Code"

# Get program account info
solana account <PROGRAM_ID>

# Get account balance
solana balance <PUBLIC_KEY>

# Transfer SOL
solana transfer <RECIPIENT> <AMOUNT>
```

## 🚨 Troubleshooting

### Program Build Fails
- Ensure Rust and Anchor versions are compatible
- Check for syntax errors in Rust code
- Verify all dependencies are installed

### Deployment Fails
- Check you have sufficient SOL for deployment
- Verify your keypair has the correct permissions
- Ensure the cluster is running (for localhost)

### Tests Fail
- Verify the program is deployed
- Check account PDAs are correctly derived
- Ensure test accounts have sufficient SOL

### Type Errors
- Regenerate types with `anchor build`
- Check program ID matches in all files
- Verify IDL is up to date

## 🎉 Next Steps

1. **Frontend Integration**: Connect your web app to the smart contracts
2. **Mobile Integration**: Implement QR code scanning in the mobile app
3. **Monitoring**: Set up transaction monitoring and analytics
4. **Production Deployment**: Deploy to mainnet with proper security measures

## 📚 Additional Resources

- [Anchor Documentation](https://book.anchor-lang.com/)
- [Solana Documentation](https://docs.solana.com/)
- [Solana Cookbook](https://solanacookbook.com/)
- [Clockwork Documentation](https://docs.clockwork.xyz/)

---

**Need help?** Check the [README.md](./README.md) for more detailed information or open an issue in the repository.
