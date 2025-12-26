package com.veigest.sdk.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Gerenciador de autenticação do SDK.
 * Armazena tokens de forma segura usando EncryptedSharedPreferences.
 */
public class AuthManager {
    
    private static final String TAG = "VeiGestAuth";
    private static final String PREFS_NAME = "veigest_auth_prefs";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    private static final String KEY_TOKEN_EXPIRES_AT = "token_expires_at";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_COMPANY_ID = "company_id";
    
    private final SharedPreferences prefs;
    private String cachedAccessToken;
    
    public AuthManager(@NonNull Context context) {
        this.prefs = createSecurePrefs(context);
        this.cachedAccessToken = prefs.getString(KEY_ACCESS_TOKEN, null);
    }
    
    /**
     * Cria SharedPreferences criptografadas.
     */
    private SharedPreferences createSecurePrefs(Context context) {
        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();
            
            return EncryptedSharedPreferences.create(
                    context,
                    PREFS_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            Log.w(TAG, "Não foi possível criar prefs criptografadas, usando normais", e);
            return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }
    }
    
    /**
     * Salva os tokens de autenticação.
     * 
     * @param accessToken Token de acesso
     * @param refreshToken Token de refresh (pode ser null)
     * @param expiresIn Tempo de expiração em segundos
     */
    public void saveTokens(@NonNull String accessToken, @Nullable String refreshToken, int expiresIn) {
        long expiresAt = System.currentTimeMillis() + (expiresIn * 1000L);
        
        prefs.edit()
                .putString(KEY_ACCESS_TOKEN, accessToken)
                .putString(KEY_REFRESH_TOKEN, refreshToken)
                .putLong(KEY_TOKEN_EXPIRES_AT, expiresAt)
                .apply();
        
        this.cachedAccessToken = accessToken;
    }
    
    /**
     * Salva informações adicionais do utilizador.
     */
    public void saveUserInfo(int userId, int companyId) {
        prefs.edit()
                .putInt(KEY_USER_ID, userId)
                .putInt(KEY_COMPANY_ID, companyId)
                .apply();
    }
    
    /**
     * Obtém o token de acesso atual.
     */
    @Nullable
    public String getAccessToken() {
        if (cachedAccessToken == null) {
            cachedAccessToken = prefs.getString(KEY_ACCESS_TOKEN, null);
        }
        return cachedAccessToken;
    }
    
    /**
     * Obtém o refresh token.
     */
    @Nullable
    public String getRefreshToken() {
        return prefs.getString(KEY_REFRESH_TOKEN, null);
    }
    
    /**
     * Obtém o timestamp de expiração do token.
     */
    public long getTokenExpiresAt() {
        return prefs.getLong(KEY_TOKEN_EXPIRES_AT, 0);
    }
    
    /**
     * Obtém o ID do utilizador.
     */
    public int getUserId() {
        return prefs.getInt(KEY_USER_ID, 0);
    }
    
    /**
     * Obtém o ID da empresa.
     */
    public int getCompanyId() {
        return prefs.getInt(KEY_COMPANY_ID, 0);
    }
    
    /**
     * Verifica se o utilizador está autenticado.
     */
    public boolean isAuthenticated() {
        return getAccessToken() != null;
    }
    
    /**
     * Verifica se o token está expirado.
     */
    public boolean isTokenExpired() {
        long expiresAt = getTokenExpiresAt();
        if (expiresAt == 0) return true;
        
        // Considera expirado se faltarem menos de 60 segundos
        return System.currentTimeMillis() >= (expiresAt - 60000);
    }
    
    /**
     * Verifica se o token precisa ser renovado (próximo de expirar).
     */
    public boolean shouldRefreshToken() {
        long expiresAt = getTokenExpiresAt();
        if (expiresAt == 0) return false;
        
        // Renova se faltarem menos de 5 minutos
        return System.currentTimeMillis() >= (expiresAt - 300000);
    }
    
    /**
     * Limpa todos os tokens (logout).
     */
    public void clearTokens() {
        prefs.edit()
                .remove(KEY_ACCESS_TOKEN)
                .remove(KEY_REFRESH_TOKEN)
                .remove(KEY_TOKEN_EXPIRES_AT)
                .remove(KEY_USER_ID)
                .remove(KEY_COMPANY_ID)
                .apply();
        
        this.cachedAccessToken = null;
    }
    
    /**
     * Atualiza apenas o token de acesso (após refresh).
     */
    public void updateAccessToken(@NonNull String accessToken, int expiresIn) {
        long expiresAt = System.currentTimeMillis() + (expiresIn * 1000L);
        
        prefs.edit()
                .putString(KEY_ACCESS_TOKEN, accessToken)
                .putLong(KEY_TOKEN_EXPIRES_AT, expiresAt)
                .apply();
        
        this.cachedAccessToken = accessToken;
    }
}
