# 🔐 Advanced: Auth & Sessions

Understanding how VeiGest keeps you logged in.

## 1. Login Flow (The Token)

1. `LoginFragment` gathers credentials.
2. `singleton.loginAPI(email, pass)` is called.
3. SDK sends a POST to `/auth/login`.
4. The server returns a JSON with a `"token"`.
5. SDK calls `saveToken(token)` which persists it in **SharedPreferences**.

## 2. Using the Token

Every time the SDK makes a request, it calls `getAuthHeaders()`:
```java
headers.put("Authorization", "Bearer " + token);
```
Without this header, the server will return a **401 Unauthorized**.

## 3. Session Expiry

If a request fails with 401, it usually means the token expired.
- **Tip**: You can implement a centralized error handler in the SDK that redirects the user to the `LoginFragment` automatically if a 401 is detected.

## 4. Persistent Login

On app launch (`MainActivity`), checking if the user is already logged in:
```java
String token = singleton.getToken();
if (token != null && !token.isEmpty()) {
    // Navigate straight to Dashboard
} else {
    // Navigate to Login screen
}
```

## 5. Security Note
The token is stored in "Private Mode" SharedPreferences, meaning other apps cannot read it. However, it is not encrypted. For highly sensitive apps, consider using **EncryptedSharedPreferences**.
