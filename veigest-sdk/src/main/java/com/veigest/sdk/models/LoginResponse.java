package com.veigest.sdk.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Resposta de login da API.
 */
public class LoginResponse {
    
    @SerializedName("access_token")
    private String accessToken;
    
    @SerializedName("token")
    private String token;
    
    @SerializedName("token_type")
    private String tokenType;
    
    @SerializedName("expires_in")
    private int expiresIn;
    
    @SerializedName("refresh_token")
    private String refreshToken;
    
    @SerializedName("user")
    private User user;
    
    @SerializedName("data")
    private LoginData data;
    
    /**
     * Obtém o token de acesso.
     */
    @Nullable
    public String getAccessToken() {
        if (accessToken != null) return accessToken;
        if (token != null) return token;
        if (data != null && data.token != null) return data.token;
        return null;
    }
    
    /**
     * Obtém o tipo de token (geralmente "Bearer").
     */
    @Nullable
    public String getTokenType() {
        return tokenType != null ? tokenType : "Bearer";
    }
    
    /**
     * Obtém o tempo de expiração do token em segundos.
     */
    public int getExpiresIn() {
        return expiresIn > 0 ? expiresIn : 3600;
    }
    
    /**
     * Obtém o refresh token.
     */
    @Nullable
    public String getRefreshToken() {
        if (refreshToken != null) return refreshToken;
        if (data != null) return data.refreshToken;
        return null;
    }
    
    /**
     * Obtém os dados do utilizador.
     */
    @Nullable
    public User getUser() {
        if (user != null) return user;
        if (data != null) return data.user;
        return null;
    }
    
    /**
     * Estrutura alternativa de dados de login.
     */
    public static class LoginData {
        @SerializedName("token")
        String token;
        
        @SerializedName("refresh_token")
        String refreshToken;
        
        @SerializedName("user")
        User user;
        
        @SerializedName("expires_in")
        int expiresIn;
    }
    
    @Override
    public String toString() {
        return "LoginResponse{" +
                "accessToken='" + (getAccessToken() != null ? "[HIDDEN]" : "null") + '\'' +
                ", tokenType='" + getTokenType() + '\'' +
                ", expiresIn=" + getExpiresIn() +
                ", user=" + getUser() +
                '}';
    }
}
