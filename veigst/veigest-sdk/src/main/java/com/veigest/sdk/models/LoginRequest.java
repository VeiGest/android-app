package com.veigest.sdk.models;

import com.google.gson.annotations.SerializedName;

/**
 * Requisição de login.
 */
public class LoginRequest {
    
    @SerializedName("email")
    private String email;
    
    @SerializedName("username")
    private String username;
    
    @SerializedName("password")
    private String password;
    
    public LoginRequest(String emailOrUsername, String password) {
        // Detecta se é email ou username
        if (emailOrUsername != null && emailOrUsername.contains("@")) {
            this.email = emailOrUsername;
        } else {
            this.username = emailOrUsername;
        }
        this.password = password;
    }
    
    public static LoginRequest withEmail(String email, String password) {
        LoginRequest request = new LoginRequest(null, password);
        request.email = email;
        return request;
    }
    
    public static LoginRequest withUsername(String username, String password) {
        LoginRequest request = new LoginRequest(null, password);
        request.username = username;
        return request;
    }
}
