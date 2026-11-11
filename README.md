# MOBILE BD & SHAWON TELECOM — Android starter

Package: `com.shawon.mobilebdshawontelecom`

This is an initial native Android project configured for the Firebase Android app with the matching package name. It currently includes email/password Firebase Authentication and a basic Firestore connection test.

## Build APK
GitHub Actions workflow: `.github/workflows/build-apk.yml`
After uploading these files to the repository's `main` branch, open **Actions** and wait for **Build Android APK** to finish. Download the artifact named `mobile-bd-shawon-telecom-debug-apk`.

## Firebase console checks
- Enable Email/Password in Authentication for this starter login.
- Keep Firestore rules locked down; deploy authenticated, user-scoped rules before using real customer data.
- Google sign-in and phone OTP are not implemented in this starter yet.
- Customer, repair, stock, staff roles, invoices and reports are planned next modules; this is not the finished shop-management app.
