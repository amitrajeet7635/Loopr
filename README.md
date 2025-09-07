<div align="center">
  <img width = "200" alt="loop" src="https://github.com/user-attachments/assets/f35ee305-ad9c-45c9-8fb3-9bbd4025a9af" />
  <p>Subscriptions made simple. Poweered by SolanaPay</p>

<br/>
<p align="center">
  <img src="https://img.shields.io/github/last-commit/amitrajeet7635/loopr?label=Last%20Commit&logo=git" alt="Last Commit" />
  <img src="https://img.shields.io/badge/Hackathon-purple" alt="Hackathon" />
  <img src="https://img.shields.io/badge/status-Prototype-orange" alt="Status" />
  <img src="https://img.shields.io/badge/version-0.1.0-blue" alt="Version" />
  <img src="https://img.shields.io/badge/stability-Prototype-lightgrey" alt="Stability" />
  <img src="https://vbr.nathanchung.dev/badge?page_id=amitrajeet7635.Loopr" alt="Visitors" />
  <br/>
  <img src = "https://img.shields.io/badge/Android-green?logo=android&logoColor=white" />
  <img src="https://img.shields.io/badge/-Blockchain-121212?logo=blockchaindotcom&logoColor=white" alt="Blockchain" />
</p>

<p align="center">
  <img src="https://user-images.githubusercontent.com/74038190/212284100-561aa473-3905-4a80-b561-0d28506553ee.gif" alt="Animated Line" width="90%" />
</p>
</div>

### 📌 Submitted on MetaMask-Card-Dev-Cook-Off
Loopr is a decentralized subscription payment and management platform built on Solana. It enables businesses to integrate seamless subscription payments into their applications and allows users to manage all their subscriptions in one place.

## Live Demos & Deliverables
- **Demo Video (Click below to watch the youtube video):** - [Zaap BuildShowcase | Project Demo](https://youtu.be/sL7EbgwUi9M) <br>
<a href="https://youtu.be/sL7EbgwUi9M" target="_blank">
  <img src="https://github.com/user-attachments/assets/f2e65157-17ee-49bb-a9ec-856a4ee6e553" alt="Build Showcase" width="400">
</a>

- **Pitch Deck / PPT Link:** [Zaap Pitch Deck](https://www.loom.com/share/c6d53a0bf8c34ac182cd7467cb829208?sid=c67c0044-8307-4b74-a7ab-6b784b6ad043)
- **Backend Hosted on Vercel:** [zaap-backend.vercel.app](https://zaap-backend.vercel.app/)
- **Android App APK:** [Download Zaap APK (Android)](https://drive.google.com/drive/folders/1G7T06gCsx93fCXGpHH7SzYnNDyLpwLYx?usp=sharing)

---

## 🎯 Objective

**What problem does our project solve ?**  
ZAAP empowers teens to spend digitally using Scan & Pay — no bank account, no KYC, just a delegated Web3 wallet.
Teens enjoy autonomy; parents stay in control with real-time monitoring, wallet linking, and spending limits via a secure dashboard.

**Who does it serves ?**
ZAAP serves both teenagers and their guardians by bridging the gap between financial independence and parental oversight. It empowers teens—especially those aged 13 to 19—to make digital payments using a secure, privacy-first solution without the need for a traditional bank account or KYC checks. At the same time, ZAAP equips parents with a powerful web-based dashboard to monitor transactions, set spending limits, and fund their child's wallet. This dual-user approach ensures that while teens gain real-world financial experience, parents maintain visibility and control—creating a safe, modern financial ecosystem built for the next generation.

**Real-World Use Case:**
Meet Aryan, a 16-year-old student in Mumbai. He frequently shops online, pays at local cafés, and subscribes to learning platforms. Traditionally, he'd need to borrow his parent's card or rely on cash — both inconvenient and lacking independence.
With ZAAP, Aryan logs into the mobile app using MetaMask Delegation Toolkit, gaining access to his smart wallet. When he visits a bookstore, he simply scans a QR code/or swipe the metamask card and pays — the transaction is authorized via the linked MetaMask or Circle wallet his parents fund.
On the other side, Aryan's mother uses the ZAAP Parental Dashboard on the web to:
  - Add funds to Aryan's wallet,
  - Set monthly limits,
  - And monitor his spending activity in real-time.

No need for a traditional bank, no KYC hurdles — just seamless, privacy-respecting teen finance.

---

## 🧠 Our Approach
 
### **Why we chose this problem:**
Managing subscriptions today is fragmented and frustrating. Subscriptions are scattered across cards, banks, and platforms. Cancelling or modifying requires contacting individual merchants. Users lack transparency, there are hidden charges or forgotten renewals pile up. Traditional payment systems (Visa, Mastercard, PayPal) are centralized and slow to innovate.

We chose this problem because subscriptions are the backbone of modern digital life — streaming, gaming, SaaS, fitness, education — yet the tools to manage them are outdated.

Loopr brings on-chain transparency, one-click control, and automated organization so users never feel locked in, overcharged, or lost in spreadsheets again.
We believe financial literacy and independence should start early, and ZAAP offers a way to do that without compromising security, privacy, or trust.

### **Key challenges we addressed:**
1. Integrating MetaMask Delegation Toolkit — Setting up secure delegated smart accounts for teens and parents.
2. Hybrid Login Flow — Combining wallet-based and social logins using Web3Auth for seamless onboarding.
3. Zaap CircleLink - Connecting Child Wallet with the Parent wallet with just a QR Scan - as simple as that (check demo for real experience)
4. QR-Based Scan & Pay — Implementing a smooth, real-time scan-and-pay experience for teens on mobile.
5. Parental Monitoring Dashboard — Creating a responsive web dashboard to track and manage child spending.
   
### **Ahhh! Some pivots, brainstorms, or breakthroughs during hacking:**
1. Switched from Traditional Wallets to Smart Accounts — We shifted to MetaMask DTK for flexible, delegated wallet access without compromising control, this also gave us added advantages.
2. Reduction of Gas Fees - As we have used Metamask DTK, we got the advantage, to sponsor gas fees enabling smooth user experience.
3. Brainstormed Dual Experience (Parent + Teen) — Decided to split experiences across mobile (for teens) and web (for parents) to optimize UX per user role.
4. Breakthrough Using Circle & MetaMask Card for Spending — Realized we could route transactions via MetaMask/Circle cards to simulate real-world teen payments.
5. Child and Parent Wallet Connect - We engineered this thing on our own, and it has been a major breakthru. Connecting wallets was never more simpler than this.
6. At first we thought to go with Linea, but then due to some problems/issues from their dev end, we shifted to ethereum.

### ✨ Key Features

Major Highlights of your project:

- ✅ Teen users can scan QR codes and pay directly using circle wallets without needing custody.
- ✅ Parents can log in via MetaMask DTK to fund, monitor, and control their child's wallet and spending.
- ✅ Supports both Web3 social login (via Web3Auth) and MetaMask DTK for secure and flexible access.
- ✅ No KYC required for children; built to ensure financial autonomy while protecting identity.
- ✅ Enables seamless USDC transfers between parent and child wallets using Circle’s developer SDKs.
- ✅ Smart Savings by Child


### 🧠 Why This Makes Sense for MetaMask Card Users — And Where It’s Going

> ⚡️ **The Problem**
>
> Right now, Web3 is still way too complicated — especially for the next-gen. You’ve got to create a wallet, manage private/public keys, handle gas, and figure out how to move funds. It’s a mess. That complexity stops normal users from adopting Web3 for daily payments.
>
> But with **ZAAP**, it’s different. You onboard instantly — just sign in with your social login. No seed phrases. No extensions. No confusion. It feels like a regular app — but under the hood, it’s Web3-powered.
>
> Imagine a teen with a **MetaMask Card** linked to their delegated wallet. They can just swipe or scan to pay at any store, kiosk, or merchant. The transaction happens in USDC, and their parent gets an instant update. They can fund the wallet, set limits, or pause access — all from their web dashboard.
>
> The MetaMask Card isn’t live yet, so we couldn’t test swipes directly — but everything else is ready. Once it's launched, ZAAP fits in directly. We’re also looking ahead: in India, we plan to integrate **UPI payments**, making it even more real-world ready.
>
> And forget about gas fees — we sponsor them. Users can pay a small annual fee and never worry about onchain transaction costs. That’s a big UX win.
>
> Looking forward, we can build loyalty and reward layers on top. We already track spending, saving, and limits — so we can partner with **teen-focused brands** to offer cashbacks, streak rewards, or custom deals. It makes ZAAP sticky, fun, and personal.
>
> This isn’t just another payment app. It’s **real-world Web3 adoption** — where MetaMask Cards help teens gain financial freedom, while parents keep control. Zero banks, zero friction, 100% ZAAP.


### Flow of our APP:
1. Parent first connect existing Metamask Wallet(Or create a new wallet )
2. Add a new child profile - set nickname, amount to be spend and Done.. 
3. Click on Show QR
4. On the mobile app, first login with your socials, and then you'd be prompted to setup Zaap CircleLink
5. Scan the QR from there, shown on the web dashboard and here you go ... ! 
6. Now you can start Zaaping
7. Scan & Pay, Transfer to wallet addresses, Deposit in wallet, Check your set limits thru your phone, Setup monthly goals and much more unlocked !! 


## 🛠️ Tech Stack

### Loopr - Web SDK:
- Language: TypeScript
- Framework: React (for UI components like PayWithLoopr)
- QR Code Generation: qrcode / qrcode.react
- Blockchain: Solana Pay integration using @solana/web3.js
- Plan Management: Custom configuration system

### Loopr - Mobile APP:
- Language: Kotlin, Java
- Authentication: Web3Auth (wallet onboarding) | Embedded Wallets 
- Payments: sol4k sdk
---
## How it looks (UI Walkthrough)! 

### Loopr Mobile App:

<div align="left">
  <img src="https://github.com/user-attachments/assets/a1c856a6-a1b5-401e-8173-4f8e3eac15d8" width="170" style="margin-right: 20px;" />
  <img src="https://github.com/user-attachments/assets/0486ac41-c128-4cf5-a383-875efb71265f" width="170" style="margin-right: 20px;" />
  <img src="https://github.com/user-attachments/assets/45a5b366-cf46-4091-ae15-a2cf83150421" width="170" style="margin-right: 20px;" />
  <img src="https://github.com/user-attachments/assets/563e9a21-5f5e-4a04-bec4-c443574aa03c" width="170" style="margin-right: 20px;" />
  <img src="https://github.com/user-attachments/assets/ee69e092-a987-4e47-9fbd-d2e50da09dfa" width="170" style="margin-right: 20px;" />
  <img src="https://github.com/user-attachments/assets/937da339-e5ae-45a9-a1c0-b62e4b45d05e" width="170" style="margin-right: 20px;" />
  <img src="https://github.com/user-attachments/assets/c7e46c6b-7693-4ce7-9c25-8304b2c6c563" width="170" style="margin-right: 20px;" />
</div>



### Web SDK:
<div align="left", gap = 30px>
<img width="600" alt="image" src="https://github.com/user-attachments/assets/8f6764b0-1ffc-4333-b9aa-663164a3d8cb" />
<img width="600" alt="image" src="https://github.com/user-attachments/assets/b6fa038b-7557-4503-a67a-9dfd588b5acf" />
<img width="600" alt="image" src="https://github.com/user-attachments/assets/86bb86ed-aee9-41d1-a7a5-77d3927c4b19" />
<img width="600" alt="image" src="https://github.com/user-attachments/assets/2695cf98-ddb5-48f3-bac5-a4d63d866821" />
</div>


## 🧪 What’s Working Now?

| Feature                      | Status                                                                                   |
|------------------------------|------------------------------------------------------------------------------------------|
| Mobile app (Kotlin/Java)      | ![Working](https://img.shields.io/badge/status-working-brightgreen?style=flat-square)    |
| Loopr Web SDK (TS)      | ![Working](https://img.shields.io/badge/status-working-brightgreen?style=flat-square)    |
| Hosting & demos               | ![Working](https://img.shields.io/badge/status-working-brightgreen?style=flat-square)    |
| Rewards NFTs | ![Ready](https://img.shields.io/badge/status-ready-brightgreen?style=flat-square)|
| Subscription Analytics     | ![Coming Soon](https://img.shields.io/badge/status-coming--soon-yellow?style=flat-square)|


> ⚠️ *Project is still buggy but fully hosted and demo-ready.*


---

## <img src = "https://user-images.githubusercontent.com/74038190/212284087-bbe7e430-757e-4901-90bf-4cd2ce3e1852.gif" width = "24"/> How to Run the Project Locally

### Requirements:
- Node.js / Python / Docker / etc.
- API Keys (if any)
- .env file setup (if needed)

### Local Setup:
```bash
# Clone the repo
git clone [https://github.com/amanna13/Lumos.git](https://github.com/amanna13/Zaap.git)


# Install dependencies
## For Web
cd Zaap-Web
cd zapp-frontend
npm install
npm run dev

cd zaap-backend
npm install
npm run dev

#Both Frontend and Backend both are deployed on vercel:
Frontend: https://zaap-eight.vercel.app/
Backend: https://zaap-backend.vercel.app/

#API Endpoint:
POST - /api/connect-child
POST - /api/set-delegator

```

### For Android App -
```bash
# Clone the repo Zaap-Android
# Build the project using Gradle
```

<details>
  <summary><strong>📎 Resources / Docs / Credits</strong></summary>

  - [Metamask Web3Auth](https://web3auth.io/) – Onboarding users using Social Logins  
  - [SolanaPay](https://docs.solanapay.com/) – Free-to-use payments framework built on Solana

</details>

---

##  <img src="https://user-images.githubusercontent.com/74038190/216122041-518ac897-8d92-4c6b-9b3f-ca01dcaf38ee.png" width="26" /> What's Next ? Are we still onto it ! 

100% — this is not just a one-weekend hack.
We’ve built a solid prototype on Web and Android, and have been heads down on this project for weeks.
ZAAP is here to stay. While this submission marks a milestone, our team is actively working to fix bugs and improve the product.
If we receive enough positive feedback and community interest, we will definitely launch ZAAP in the market.


**Subscriptions made simple by LOOPR Powered by SolanaPay.**

---

### 👋Connect with us:  

<table align="left">
  <tr>
    <td><img src="https://github.com/Anmol-Baranwal/Cool-GIFs-For-GitHub/assets/74038190/7bb1e704-6026-48f9-8435-2f4d40101348" width="50"></td>
    <td><h4>Built by the Loopr Teaam</h4></td>
  </tr>
  <tr>
    <td colspan="2" align="center">
      <table>
        <tr>
          <td align="center">
            <a href="https://github.com/amanna13">
              <img src="https://github.com/amanna13.png" width="80" height="80"><br>
              <sub><b>@amanna13</b></sub>
            </a>
          </td>
          <td align="center">
            <a href="https://github.com/amitrajeet7635">
              <img src="https://github.com/amitrajeet7635.png" width="80" height="80"><br>
              <sub><b>@amitrajeet7635</b></sub>
            </a>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>

<div align="center"> <strong>We're actively building, debugging, and prototyping —<br>drop a ⭐, share your feedback, or just vibe with us!</strong> <br/><br/> <img src="https://github.com/Anmol-Baranwal/Cool-GIFs-For-GitHub/assets/74038190/491e3e44-11a0-487a-b07b-717f677bbe4a" width="170" /> </div>

