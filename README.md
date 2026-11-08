# UPI Voice Announcer (starter)

Android starter project for announcing likely incoming-payment notifications aloud.

## Build on GitHub from your phone
1. Create a new **public or private** GitHub repository.
2. Upload all files/folders from this project ZIP to the repository root.
3. Open the repository's **Actions** tab.
4. Select **Build Android APK** and tap **Run workflow** (or push to `main`).
5. Open the completed workflow run, scroll to **Artifacts**, and download `UPI-Voice-Announcer-debug`.
6. Extract the artifact ZIP; install `app-debug.apk` on your Android phone.
7. Open the app and enable **UPI Voice Announcer** under Android Notification Access settings.

## Important limitations
- This is a starter implementation, not a bank-integrated payment verifier.
- It reads notification text exposed by Android. It cannot independently confirm that money reached a bank account.
- It uses broad payment-word and amount matching; notification formats differ by app and false positives/unsupported formats are possible.
- App name is taken from the app that posted the notification. Some payment alerts may be posted by a bank or SMS app rather than the UPI app.
- Do not rely on this for business settlement, delivery release, or proof of payment. Verify in the official UPI/bank app.
- Notification access is sensitive. Review what the app can read before enabling it.
