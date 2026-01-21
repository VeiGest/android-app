# 🔍 Troubleshooting Common Issues

Is something not working? Check here for common pitfalls in the VeiGest project.

## 1. API Returns 404 (Not Found)

- **Cause**: The URL in the SDK doesn't match the Backend configuration.
- **Check**: Look at `backend/config/main.php`. Does it use plural (`/routes`) or singular (`/route`)?
- **Fix**: Align the URL in `SingletonVeiGest.java` (e.g., change `baseUrl + "/route"` to `baseUrl + "/routes"` if needed).

## 2. Changes are "Local Only" (Not Syncing)

- **Cause**: You might be trying to update a virtual/calculated field.
- **Example**: In the `Route` model, the `status` is calculated from timestamps. Sending `"status": "completed"` via PUT won't work.
- **Fix**: Use a specialized action endpoint (like `.../complete`) or update the fields that calculate the status (like `end_time`).

## 3. Validation Error (422 Unprocessable Entity)

- **Cause**: The data types or formats sent by the app don't match the Backend rules.
- **Example**: Sending a date as `2026-01-21` when the server expects `2026-01-21 00:00:00`.
- **Fix**: Check `VeiGestJsonParser` and ensuring formatting matches the Backend model rules.

## 4. App Crashes on Launch

- **Cause**: Fragment initialization error or Null Context.
- **Fix**: Watch the **Logcat** window in Android Studio. Look for "NullPointerException". Often caused by using `getContext()` before the fragment is attached.

## 5. Screen doesn't update after Save

- **Cause**: The Listener was not notified or `notifyDataSetChanged()` was not called.
- **Fix**: Trace the flow from the API response back to the Fragment listener. Ensure `singleton.setSomeListener(this)` was called in `onResume`.
