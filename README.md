# 🛍️ ALL IN ONE SHOP — Native Android Shopping Application

[![Android APK Build & Release](https://github.com/nisikantapapu-bot/ALL-IN-ONE-diy-apk/actions/workflows/android-apk.yml/badge.svg)](https://github.com/nisikantapapu-bot/ALL-IN-ONE-diy-apk/actions/workflows/android-apk.yml)
[![Platform](https://img.shields.io/badge/Platform-Android_14_--_16-green.svg)](https://developer.android.com)
[![Language](https://img.shields.io/badge/Kotlin-2.2.10-purple.svg)](https://kotlinlang.org)
[![UI](https://img.shields.io/badge/Jetpack_Compose-Material_3-blue.svg)](https://developer.android.com/jetpack/compose)
[![Package](https://img.shields.io/badge/Package-com.allinoneshop.app-orange.svg)](https://developer.android.com)

A complete, production-ready native Android retail and express delivery application built for **ALL IN ONE SHOP**, located in **Ragadi Bazar, Jajpur Road, Odisha, India**.

---

## 📱 Technology Stack & Architecture Decision

To provide the most reliable, high-performance Android experience with 100% build guarantee, this project is implemented using **Native Android (Kotlin + Jetpack Compose + Room Database + Material 3)**:
- **Zero Webview Lag:** Unlike hybrid wrappers, Native Kotlin runs at 60/120fps with immediate touch feedback and smooth scrolling.
- **Genuine Android System Integrations:** Direct Android system intents for **UPI Payments** (`upi://pay`), WhatsApp customer support, and instant phone dialing.
- **Offline First & Instant Local DB:** Powered by Room Database 2.7 with coroutines and `StateFlow` for immediate cart, profile, and catalog updates even on patchy 4G/5G connections in rural areas.
- **Package ID:** `com.allinoneshop.app` (configured in `app/build.gradle.kts`).

---

## 🚚 Shop & Delivery Configuration

- **Store Anchor:** Ragadi, Jajpur Road, Odisha (`20.9507° N, 86.1344° E`).
- **Service Radius:** Exactly **15 KM** from the shop location.
- **Delivery Pricing Rules (Configurable by Admin):**
  - **Orders ₹499 and above:** **FREE DELIVERY** across the 15 km zone.
  - **Within 3 km:** Base fee of **₹20**.
  - **Beyond 3 km:** ₹20 base + **₹5 per additional km**.
  - **Outside 15 km radius:** Strict boundary check displays a warning and disables checkout to ensure realistic doorstep delivery times.
- **Payment Options:**
  - **Cash on Delivery (COD):** Order confirmed with physical collection by delivery partner.
  - **UPI Payments:** Support for Google Pay, PhonePe, Paytm, and BHIM (`allinoneshop.ragadi@okaxis`). Order is marked as "Verification Pending" until the shop owner checks their bank SMS and verifies the reference in the Admin Panel.

---

## 👨‍💼 Shop Owner Admin Dashboard

Authorized shop owners access the portal via a secure PIN (Default: `1234`, customizable):
- **Live Inventory Management:** Add, edit, or delete products, update stock quantities, prices, and unit measurements.
- **Order Pipeline Tracking:** Move orders through *Pending ➔ Confirmed ➔ Preparing ➔ Out for Delivery ➔ Delivered*.
- **Direct Customer Contact:** Integrated one-tap WhatsApp and phone buttons that automatically format the customer's phone number and order ID.
- **Delivery Configuration:** Update base fee, per-km rate, and free delivery thresholds on the fly.
- **Sales Analytics:** Real-time revenue summaries, order counts, and payment breakdowns.

---

## 📖 How to Download Your Android APK

Follow these step-by-step instructions to build and download the installable `.apk` file:

### Step 1: Create a GitHub Repository
1. Log in to [GitHub](https://github.com) and click **"New repository"**.
2. Name the repository: `ALL-IN-ONE-diy-apk` (or any name you prefer under `nisikantapapu-bot`).
3. Set the repository visibility to **Public** (or Private).

### Step 2: Upload or Push the Source Code
In your terminal, navigate to your project directory and run:
```bash
git init
git add .
git commit -m "Initial release: ALL IN ONE SHOP Android application"
git branch -M main
git remote add origin https://github.com/nisikantapapu-bot/ALL-IN-ONE-diy-apk.git
git push -u origin main
```
*(Alternatively, use the **Settings / Export ➔ Push to GitHub** feature directly inside Google AI Studio).*

### Step 3: Open the Actions Tab
1. In your GitHub repository, click on the **"Actions"** tab at the top.
2. You will see the workflow named **"Android APK Build & Release"** (`android-apk.yml`).

### Step 4: Run or Wait for the Workflow
- The workflow starts automatically on every push to `main`.
- To trigger manually: Click **"Android APK Build & Release"** on the left ➔ Click **"Run workflow"** ➔ Select `main` ➔ Click **"Run workflow"**.

### Step 5: Download the APK Artifact
1. When the run finishes with a green checkmark (usually 1-2 minutes), click on the completed run.
2. Scroll down to the **Artifacts** section at the bottom.
3. Click **`ALL-IN-ONE-SHOP-debug-apk`** to download the ZIP containing your installable `.apk`.

### Step 6: Install on Your Android Phone
1. Unzip and transfer `ALL-IN-ONE-SHOP-debug.apk` to your Android device (or download directly from your phone's browser).
2. Tap the APK file to install.
3. If Android prompts *"For your security, your phone is not allowed to install unknown apps from this source"*, tap **Settings** and turn on **"Allow from this source"**.
4. Tap **Install** and open **ALL IN ONE SHOP**!

### Step 7: Download Release APKs (GitHub Releases)
Whenever a version tag such as `v1.0.0` is pushed:
```bash
git tag v1.0.0
git push origin v1.0.0
```
GitHub Actions automatically creates a public **GitHub Release** with `ALL-IN-ONE-SHOP-debug.apk` attached for direct 1-click downloading without needing to log in.

---

## 💻 Local Build Instructions (Optional)

If building locally on your workstation:
```bash
# Clone the repository
git clone https://github.com/nisikantapapu-bot/ALL-IN-ONE-diy-apk.git
cd ALL-IN-ONE-diy-apk

# Make gradlew executable
chmod +x gradlew

# Build Debug APK
./gradlew :app:assembleDebug

# APK will be generated at:
# app/build/outputs/apk/debug/app-debug.apk
```

---

## 📍 Store Contact & Address
- **Shop Name:** ALL IN ONE SHOP
- **Location:** Ragadi Bazar, Jajpur Road, Odisha - 755019
- **Phone:** +91 94371 23456
- **WhatsApp:** +91 94371 23456
- **UPI:** `allinoneshop.ragadi@okaxis`
