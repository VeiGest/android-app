package com.ipleiria.veigest;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Classe para armazenamento seguro usando EncryptedSharedPreferences
 */
public class SecureStorage {
    
    private static final String PREFERENCES_FILE = "veigest_secure_prefs";
    private static SecureStorage instance;
    private SharedPreferences sharedPreferences;
    
    private SecureStorage(Context context) {
        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();
            
            sharedPreferences = EncryptedSharedPreferences.create(
                    context,
                    PREFERENCES_FILE,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            // Fallback to regular SharedPreferences if encryption fails
            sharedPreferences = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        }
    }
    
    public static synchronized SecureStorage getInstance(Context context) {
        if (instance == null) {
            instance = new SecureStorage(context.getApplicationContext());
        }
        return instance;
    }
    
    public void saveString(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }
    
    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }
    
    public void saveBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }
    
    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }
    
    public void remove(String key) {
        sharedPreferences.edit().remove(key).apply();
    }
    
    public void clear() {
        sharedPreferences.edit().clear().apply();
    }
    
    public boolean contains(String key) {
        return sharedPreferences.contains(key);
    }
}
