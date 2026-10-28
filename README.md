# 🛍️ ALL IN ONE — Android Application

[![Build & Release Android APK](https://github.com/nisikantapapu-bot/ALL-IN-ONE-diy-apk/actions/workflows/build-apk.yml/badge.svg)](https://github.com/nisikantapapu-bot/ALL-IN-ONE-diy-apk/actions/workflows/build-apk.yml)
[![Platform](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-purple.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack_Compose-M3-blue.svg)](https://developer.android.com/jetpack/compose)

Local online shopping and express doorstep delivery application for **Ragadi, Jajpur Road, Odisha (within 15 KM radius)**.

---

## 📱 Features

- **Local Express Delivery (15 KM Radius):**
  - Live distance calculation from Ragadi anchor coordinates (`20.9507° N, 86.1344° E`).
  - Automated delivery fee formula: Free delivery above ₹499, base ₹20 up to 3 km, ₹5/km thereafter.
  - Boundary check prevents orders outside the 15 km serviceable area.

- **Shopping & Categories:**
  - Groceries, Atta, Rice, Spices, Dairy, Electronics, Clothing, and Daily Household Essentials.
  - Search by keyword, filter by categories, live cart updates, and wishlist items.

- **Flexible Payments:**
  - **Cash on Delivery (COD)** with doorstep verification.
  - **UPI Instant Pay** integration for Google Pay, PhonePe, Paytm, and BHIM (`allinone.ragadi@okaxis`).

- **Shop Owner Admin Panel (PIN: 1234):**
  - Add, edit, or delete items and live inventory.
  - Update order status: *Pending ➔ Confirmed ➔ Preparing ➔ Out for Delivery ➔ Delivered*.
  - One-tap WhatsApp chat & phone dialer pre-populated with customer and order details.

- **Bilingual Interface:**
  - Complete English and Odia (ଓଡ଼ିଆ) delivery instructions and download guidance.

---

## 🚀 Automated Builds via GitHub Actions

This repository includes a native GitHub Actions CI/CD pipeline:
[`.github/workflows/build-apk.yml`](.github/workflows/build-apk.yml)

Every `push` to `main`/`master` or trigger via `workflow_dispatch` automatically:
1. Sets up JDK 21 and Gradle build environment.
2. Generates the required debug keystore and signs the APK using **APK Signature Scheme v2**.
3. Compiles the native Android application into `ALL-IN-ONE-debug.apk`.
4. Uploads the build artifact and publishes releases with download links.

---

## 📥 How to Download & Install

### Option 1: Download from GitHub Releases
1. Navigate to the **[Releases](https://github.com/nisikantapapu-bot/ALL-IN-ONE-diy-apk/releases)** tab in this repository.
2. Download the latest `ALL-IN-ONE-debug.apk`.
3. Open the file on your Android device (ensure *"Install unknown apps"* is allowed in Settings).

### Option 2: Build Locally using Gradle
```bash
# Clone the repository
git clone https://github.com/nisikantapapu-bot/ALL-IN-ONE-diy-apk.git
cd ALL-IN-ONE-diy-apk

# Make gradlew executable
chmod +x gradlew

# Build Debug APK
./gradlew assembleDebug

# Output APK location:
# app/build/outputs/apk/debug/app-debug.apk
```

---

## 🛠️ Tech Stack & Architecture

- **Language:** Kotlin 2.2
- **UI Toolkit:** Jetpack Compose with Material 3 (M3)
- **Architecture:** Clean MVVM (Model - View - ViewModel) + StateFlow
- **Local Persistence:** Room Database 2.7 (with KSP)
- **Image Loading:** Coil Compose
- **Target SDK:** 36 | **Min SDK:** 24 (Android 7.0+)

---

## 📍 Store Location & Contact
- **Store:** ALL IN ONE
- **Location:** Main Market, Ragadi, Jajpur Road, Odisha - 755019
- **Helpline:** +91 94380 12345 | **WhatsApp:** +91 94380 12345
