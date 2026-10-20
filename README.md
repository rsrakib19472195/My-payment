# Transaction Reader — Android starter

This starter app listens only after the user explicitly enables Android Notification Access. It filters notifications whose package/text contains bKash/Nagad, then stores the latest matched notification locally for inspection. It does NOT send data to a server yet.

## Build from a phone (cloud build)
1. Create a GitHub repository named `transaction-reader`.
2. Upload all files/folders in this project to the repository root (including `.github/workflows/build-apk.yml`).
3. In GitHub, open Actions → `Build Transaction Reader APK` → Run workflow.
4. When it finishes, open the run and download the `transaction-reader-debug-apk` artifact. Extract the ZIP and install `app-debug.apk`.
5. Android may ask permission to install apps from that browser/file manager; allow only for the source you trust.

## Test
1. Open app → Enable Notification Access → enable Transaction Reader.
2. Ensure bKash/Nagad app notifications are enabled.
3. Have a legitimate test notification appear, then return to this app and tap Refresh.
4. The parser is heuristic: sender number or transaction ID may not be present in notification text, and formats differ. Check the raw notification output and refine regexes to the actual notification format.

## Notes
- Notification access is powerful; enable it only on your own device and with informed consent.
- This first version stores only the latest matched notification locally and has no API integration yet.
- Never treat notification text alone as proof that a payment is genuine. Server-side payment verification is a separate step.
