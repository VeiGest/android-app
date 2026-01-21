# 🐞 Debugging Tips

Efficiently finding bugs saves hours of development.

## 1. Logcat is your Best Friend

Use the `Log` class extensively:
```java
Log.d("TAG", "Variable value: " + value);
Log.e("TAG", "Critical error!", exception);
```
Filter by your package name and "Debug" level in Android Studio to see the SDK's internal logs.

## 2. Debugger & Breakpoints

Don't just use logs. Use the Debugger:
1. Click in the gutter to set a breakpoint (red dot).
2. Run the app in "Debug" mode (Shift + F9).
3. Inspect variable states and step through code line-by-line.

## 3. App Inspection (Database)

To see what's actually inside your SQLite database:
1. View -> Tool Windows -> App Inspection.
2. Select your device and process.
3. Click on **Database Inspector**.
4. You can run SQL queries directly on the live database!

## 4. Network Inspector

To see exactly what the JSON payload looks like:
1. Tool Windows -> **App Inspection** -> **Network Inspector**.
2. Click on a request to see:
   - URL
   - Headers (Token)
   - Request Body (Sent by App)
   - Response Body (Received from Server)

## 5. UI Layout Inspector

If your screen looks messy or elements are overlapping:
1. Tool Windows -> **Layout Inspector**.
2. It shows a 3D view of the UI tree, helping you find where margins and paddings are coming from.
