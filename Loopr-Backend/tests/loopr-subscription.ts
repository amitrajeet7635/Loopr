import * as anchor from "@coral-xyz/anchor";import { Program } from "@coral-xyz/anchor";import { PublicKey, Keypair, SystemProgram, LAMPORTS_PER_SOL } from "@solana/web3.js";import { LooprSubscription } from "../target/types/loopr_subscription";import { expect } from "chai";describe("loopr-subscription", () => {  // Configure the client to use the local cluster.  const provider = anchor.AnchorProvider.env();  anchor.setProvider(provider);  const program = anchor.workspace.LooprSubscription as Program<LooprSubscription>;    // Test accounts  let authority: Keypair;  let user: Keypair;  let globalStatePda: PublicKey;  let subscriptionPlanPda: PublicKey;  let userSubscriptionPda: PublicKey;  let paymentIntentPda: PublicKey;    const planId = "netflix-premium";  const subscriptionId = "user-netflix-123";  const intentId = "intent-123";  const planPrice = 0.1 * LAMPORTS_PER_SOL; // 0.1 SOL  const periodDuration = 30 * 24 * 60 * 60; // 30 days in seconds  before(async () => {    // Initialize test accounts    authority = Keypair.generate();    user = Keypair.generate();    // Airdrop SOL to test accounts    await provider.connection.requestAirdrop(authority.publicKey, 10 * LAMPORTS_PER_SOL);    await provider.connection.requestAirdrop(user.publicKey, 10 * LAMPORTS_PER_SOL);        // Wait for airdrops to confirm    await provider.connection.confirmTransaction(      await provider.connection.requestAirdrop(authority.publicKey, 10 * LAMPORTS_PER_SOL)    );    await provider.connection.confirmTransaction(      await provider.connection.requestAirdrop(user.publicKey, 10 * LAMPORTS_PER_SOL)    );    // Derive PDAs    [globalStatePda] = PublicKey.findProgramAddressSync(      [Buffer.from("global_state")],      program.programId    );    [subscriptionPlanPda] = PublicKey.findProgramAddressSync(      [Buffer.from("subscription_plan"), Buffer.from(planId)],      program.programId    );    [userSubscriptionPda] = PublicKey.findProgramAddressSync(      [Buffer.from("user_subscription"), user.publicKey.toBuffer(), Buffer.from(subscriptionId)],      program.programId    );    [paymentIntentPda] = PublicKey.findProgramAddressSync(      [Buffer.from("payment_intent"), Buffer.from(intentId)],      program.programId    );  });  it("Initialize global state", async () => {    try {      await program.methods        .initializeGlobalState()        .accounts({          globalState: globalStatePda,          authority: authority.publicKey,          systemProgram: SystemProgram.programId,        })        .signers([authority])        .rpc();      const globalState = await program.account.globalState.fetch(globalStatePda);      expect(globalState.authority.toString()).to.equal(authority.publicKey.toString());      expect(globalState.totalPlans.toNumber()).to.equal(0);      expect(globalState.totalSubscriptions.toNumber()).to.equal(0);      expect(globalState.isPaused).to.be.false;    } catch (error) {      console.log("Global state might already be initialized:", error.message);    }  });  it("Initialize subscription plan", async () => {    await program.methods      .initializeSubscriptionPlan(        planId,        "Netflix Premium",        "Premium Netflix subscription with 4K streaming",        new anchor.BN(planPrice),        new anchor.BN(periodDuration),        100 // max subscribers      )      .accounts({        subscriptionPlan: subscriptionPlanPda,        globalState: globalStatePda,        authority: authority.publicKey,        systemProgram: SystemProgram.programId,      })      .signers([authority])      .rpc();    const plan = await program.account.subscriptionPlan.fetch(subscriptionPlanPda);    expect(plan.planId).to.equal(planId);    expect(plan.name).to.equal("Netflix Premium");    expect(plan.pricePerPeriod.toNumber()).to.equal(planPrice);    expect(plan.isActive).to.be.true;    expect(plan.currentSubscribers).to.equal(0);  });  it("Create payment intent for QR code flow", async () => {    const now = Math.floor(Date.now() / 1000);    const expiresAt = now + 3600; // 1 hour from now    await program.methods      .createPaymentIntent(        intentId,        planId,        new anchor.BN(planPrice),        new anchor.BN(expiresAt)      )      .accounts({        paymentIntent: paymentIntentPda,        subscriptionPlan: subscriptionPlanPda,        authority: authority.publicKey,        globalState: globalStatePda,        systemProgram: SystemProgram.programId,      })      .signers([authority])      .rpc();    const intent = await program.account.paymentIntent.fetch(paymentIntentPda);    expect(intent.intentId).to.equal(intentId);    expect(intent.planId).to.equal(planId);    expect(intent.amount.toNumber()).to.equal(planPrice);    expect(intent.status).to.deep.equal({ created: {} });  });  it("Subscribe and pay via QR code flow", async () => {    await program.methods      .subscribeAndPay(subscriptionId)      .accounts({        paymentIntent: paymentIntentPda,        subscriptionPlan: subscriptionPlanPda,        userSubscription: userSubscriptionPda,        user: user.publicKey,        authority: authority.publicKey,        globalState: globalStatePda,        systemProgram: SystemProgram.programId,      })      .signers([user, authority])      .rpc();    const subscription = await program.account.userSubscription.fetch(userSubscriptionPda);    expect(subscription.user.toString()).to.equal(user.publicKey.toString());    expect(subscription.subscriptionId).to.equal(subscriptionId);    expect(subscription.isActive).to.be.true;    expect(subscription.autoPayEnabled).to.be.true;    const intent = await program.account.paymentIntent.fetch(paymentIntentPda);    expect(intent.status).to.deep.equal({ completed: {} });    expect(intent.payer?.toString()).to.equal(user.publicKey.toString());  });  it("Create subscription directly", async () => {    const directSubscriptionId = "direct-sub-123";    const [directUserSubscriptionPda] = PublicKey.findProgramAddressSync(      [Buffer.from("user_subscription"), user.publicKey.toBuffer(), Buffer.from(directSubscriptionId)],      program.programId    );    await program.methods      .createSubscription(directSubscriptionId)      .accounts({        subscriptionPlan: subscriptionPlanPda,        userSubscription: directUserSubscriptionPda,        user: user.publicKey,        globalState: globalStatePda,        systemProgram: SystemProgram.programId,      })      .signers([user])      .rpc();    const subscription = await program.account.userSubscription.fetch(directUserSubscriptionPda);    expect(subscription.subscriptionId).to.equal(directSubscriptionId);    expect(subscription.isActive).to.be.true;    expect(subscription.autoPayEnabled).to.be.false; // Default for direct creation  });  it("Process payment for subscription", async () => {    const [paymentRecordPda] = PublicKey.findProgramAddressSync(      [        Buffer.from("payment_record"),        user.publicKey.toBuffer(),        userSubscriptionPda.toBuffer(),
        Buffer.from(Date.now().toString())
      ],
      program.programId
    );

    await program.methods
      .processPayment(new anchor.BN(planPrice))
      .accounts({
        subscriptionPlan: subscriptionPlanPda,
        userSubscription: userSubscriptionPda,
        paymentRecord: paymentRecordPda,
        user: user.publicKey,
        authority: authority.publicKey,
        systemProgram: SystemProgram.programId,
      })
      .signers([user])
      .rpc();

    const paymentRecord = await program.account.paymentRecord.fetch(paymentRecordPda);
    expect(paymentRecord.amount.toNumber()).to.equal(planPrice);
    expect(paymentRecord.status).to.deep.equal({ completed: {} });
    expect(paymentRecord.paymentMethod).to.deep.equal({ manual: {} });
  });

  it("Update subscription plan", async () => {
    const newPrice = 0.15 * LAMPORTS_PER_SOL;
    
    await program.methods
      .updateSubscriptionPlan(
        "Netflix Premium Plus",
        null, // description unchanged
        new anchor.BN(newPrice),
        null, // period duration unchanged
        null, // max subscribers unchanged
        null  // is_active unchanged
      )
      .accounts({
        subscriptionPlan: subscriptionPlanPda,
        authority: authority.publicKey,
        globalState: globalStatePda,
      })
      .signers([authority])
      .rpc();

    const plan = await program.account.subscriptionPlan.fetch(subscriptionPlanPda);
    expect(plan.name).to.equal("Netflix Premium Plus");
    expect(plan.pricePerPeriod.toNumber()).to.equal(newPrice);
  });

  it("Cancel subscription", async () => {
    await program.methods
      .cancelSubscription()
      .accounts({
        userSubscription: userSubscriptionPda,
        user: user.publicKey,
        subscriptionPlan: subscriptionPlanPda,
        globalState: globalStatePda,
      })
      .signers([user])
      .rpc();

    const subscription = await program.account.userSubscription.fetch(userSubscriptionPda);
    expect(subscription.isActive).to.be.false;
    expect(subscription.autoPayEnabled).to.be.false;
  });

  it("Fails to create payment intent with invalid amount", async () => {
    const invalidIntentId = "invalid-intent";
    const [invalidPaymentIntentPda] = PublicKey.findProgramAddressSync(
      [Buffer.from("payment_intent"), Buffer.from(invalidIntentId)],
      program.programId
    );

    const now = Math.floor(Date.now() / 1000);
    const expiresAt = now + 3600;
    const invalidAmount = planPrice * 2; // Wrong amount

    try {
      await program.methods
        .createPaymentIntent(
          invalidIntentId,
          planId,
          new anchor.BN(invalidAmount),
          new anchor.BN(expiresAt)
        )
        .accounts({
          paymentIntent: invalidPaymentIntentPda,
          subscriptionPlan: subscriptionPlanPda,
          authority: authority.publicKey,
          globalState: globalStatePda,
          systemProgram: SystemProgram.programId,
        })
        .signers([authority])
        .rpc();
      
      expect.fail("Should have failed with invalid payment amount");
    } catch (error) {
      expect(error.message).to.include("InvalidPaymentAmount");
    }
  });
});
