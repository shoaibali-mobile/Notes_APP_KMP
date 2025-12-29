# Firebase DebugView Setup Guide

This guide will help you see Firebase Analytics events in real-time using DebugView.

## What Was Added

1. **Debug Logging**: Added comprehensive logging throughout the user setup process
2. **DebugView Support**: Enabled DebugView mode for debug builds
3. **Test Events**: Added test events to verify Firebase is working

## How to Use DebugView

### Step 1: Enable DebugView on Your Device

Run this command in your terminal (with your device connected via USB):

```bash
adb shell setprop debug.firebase.analytics.app com.shoaib.notes_app_kmp
```

**Note**: Replace `com.shoaib.notes_app_kmp` with your actual package name if different.

### Step 2: Run Your App

Run the app in debug mode on your connected device or emulator.

### Step 3: View Events in Firebase Console

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Select your project
3. Navigate to **Analytics** → **DebugView**
4. You should see events appearing in real-time!

## What You'll See

### Events to Look For:
- `app_launched` - When the app starts
- `user_setup_completed` - When user information is set
- `test_user_setup` - Test event to verify everything works
- `screen_view` - When navigating between screens
- `note_created` - When a note is created
- `note_updated` - When a note is updated
- And many more...

### User Information:
- **User ID**: `6t387`
- **User Properties**: 
  - `user_name`: "shoaib ali"
  - `device_platform`: "Android XX" or "iOS XX"
  - `user_type`: "default"

## Check Logcat

You can also check Android Logcat for debug messages:
- Filter by tag: `UserSetup` or `MainActivity`
- Look for messages starting with `✓` indicating successful operations

## Troubleshooting

### Events Not Showing?

1. **Verify DebugView is enabled**: Check logcat for "DebugView enabled" message
2. **Check ADB command**: Make sure you ran the `adb shell setprop` command
3. **Verify Firebase initialization**: Check logcat for "Firebase services initialized"
4. **Check internet connection**: Firebase needs internet to send events
5. **Wait a few seconds**: Events may take 5-10 seconds to appear

### User Properties Not Showing?

User properties need to be registered in Firebase Console first:
1. Go to Firebase Console → Analytics → User Properties
2. Click "Create user property"
3. Register these properties:
   - `user_name`
   - `device_platform`
   - `user_type`

## Standard Reports vs DebugView

- **DebugView**: Shows events in real-time (for testing)
- **Standard Reports**: Takes 24-48 hours to show data (for production)

For immediate testing, use DebugView. For production analytics, check standard reports after 24-48 hours.


