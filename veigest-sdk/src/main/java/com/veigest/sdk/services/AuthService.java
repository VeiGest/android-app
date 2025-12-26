package com.veigest.sdk.services;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.veigest.sdk.VeiGestCallback;
import com.veigest.sdk.VeiGestException;
import com.veigest.sdk.api.VeiGestApiClient;
import com.veigest.sdk.auth.AuthManager;
import com.veigest.sdk.models.ApiResponse;
import com.veigest.sdk.models.LoginRequest;
import com.veigest.sdk.models.LoginResponse;
import com.veigest.sdk.models.User;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Serviço de autenticação do SDK.
 * Gerencia login, logout, refresh token e dados do utilizador.
 */
public class AuthService extends BaseService {
    
    private final AuthManager authManager;
    
    public AuthService(@NonNull VeiGestApiClient apiClient, @NonNull AuthManager authManager) {
        super(apiClient);
        this.authManager = authManager;
    }
    
    /**
     * Realiza login com email/username e password.
     * 
     * @param emailOrUsername Email ou username do utilizador
     * @param password Password do utilizador
     * @param callback Callback com resultado
     */
    public void login(@NonNull String emailOrUsername, @NonNull String password,
                      @NonNull VeiGestCallback<LoginResponse> callback) {
        LoginRequest request = new LoginRequest(emailOrUsername, password);
        
        apiClient.getApi().login(request).enqueue(new Callback<ApiResponse<LoginResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<LoginResponse>> call,
                                   @NonNull Response<ApiResponse<LoginResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body().getData();
                    
                    if (loginResponse != null && loginResponse.getAccessToken() != null) {
                        // Salva tokens
                        authManager.saveTokens(
                                loginResponse.getAccessToken(),
                                loginResponse.getRefreshToken(),
                                loginResponse.getExpiresIn()
                        );
                        
                        // Salva info do utilizador
                        User user = loginResponse.getUser();
                        if (user != null) {
                            authManager.saveUserInfo(user.getId(), user.getCompanyId());
                        }
                        
                        runOnMainThread(() -> callback.onSuccess(loginResponse));
                    } else {
                        VeiGestException error = new VeiGestException(
                                "Credenciais inválidas",
                                401,
                                "INVALID_CREDENTIALS"
                        );
                        runOnMainThread(() -> callback.onError(error));
                    }
                } else {
                    VeiGestException error = parseErrorResponse(response);
                    runOnMainThread(() -> callback.onError(error));
                }
            }
            
            @Override
            public void onFailure(@NonNull Call<ApiResponse<LoginResponse>> call, @NonNull Throwable t) {
                handleFailure(t, callback);
            }
        });
    }
    
    /**
     * Realiza login com email e password.
     */
    public void loginWithEmail(@NonNull String email, @NonNull String password,
                               @NonNull VeiGestCallback<LoginResponse> callback) {
        login(email, password, callback);
    }
    
    /**
     * Realiza login com username e password.
     */
    public void loginWithUsername(@NonNull String username, @NonNull String password,
                                  @NonNull VeiGestCallback<LoginResponse> callback) {
        login(username, password, callback);
    }
    
    /**
     * Renova o token de acesso usando o refresh token.
     * 
     * @param callback Callback com resultado
     */
    public void refreshToken(@NonNull VeiGestCallback<LoginResponse> callback) {
        String refreshToken = authManager.getRefreshToken();
        
        if (refreshToken == null) {
            runOnMainThread(() -> callback.onError(
                    VeiGestException.authenticationError("Refresh token não disponível")
            ));
            return;
        }
        
        Map<String, String> body = new HashMap<>();
        body.put("refresh_token", refreshToken);
        
        apiClient.getApi().refreshToken(body).enqueue(new Callback<ApiResponse<LoginResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<LoginResponse>> call,
                                   @NonNull Response<ApiResponse<LoginResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body().getData();
                    
                    if (loginResponse != null && loginResponse.getAccessToken() != null) {
                        authManager.updateAccessToken(
                                loginResponse.getAccessToken(),
                                loginResponse.getExpiresIn()
                        );
                        
                        runOnMainThread(() -> callback.onSuccess(loginResponse));
                    } else {
                        VeiGestException error = new VeiGestException(
                                "Falha ao renovar token",
                                401,
                                "REFRESH_FAILED"
                        );
                        runOnMainThread(() -> callback.onError(error));
                    }
                } else {
                    // Se refresh falhar, limpa tokens
                    authManager.clearTokens();
                    VeiGestException error = parseErrorResponse(response);
                    runOnMainThread(() -> callback.onError(error));
                }
            }
            
            @Override
            public void onFailure(@NonNull Call<ApiResponse<LoginResponse>> call, @NonNull Throwable t) {
                handleFailure(t, callback);
            }
        });
    }
    
    /**
     * Realiza logout do utilizador.
     * 
     * @param callback Callback com resultado (pode ser null)
     */
    public void logout(@Nullable VeiGestCallback<Void> callback) {
        // Tenta fazer logout na API
        apiClient.getApi().logout().enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Void>> call,
                                   @NonNull Response<ApiResponse<Void>> response) {
                // Limpa tokens localmente independente do resultado
                authManager.clearTokens();
                
                if (callback != null) {
                    runOnMainThread(() -> callback.onSuccess(null));
                }
            }
            
            @Override
            public void onFailure(@NonNull Call<ApiResponse<Void>> call, @NonNull Throwable t) {
                // Limpa tokens mesmo em caso de erro
                authManager.clearTokens();
                
                if (callback != null) {
                    runOnMainThread(() -> callback.onSuccess(null));
                }
            }
        });
    }
    
    /**
     * Realiza logout sem callback.
     */
    public void logout() {
        logout(null);
    }
    
    /**
     * Obtém os dados do utilizador autenticado.
     * 
     * @param callback Callback com os dados do utilizador
     */
    public void getCurrentUser(@NonNull VeiGestCallback<User> callback) {
        executeCall(apiClient.getApi().getCurrentUser(), callback);
    }
    
    /**
     * Alias para getCurrentUser.
     */
    public void me(@NonNull VeiGestCallback<User> callback) {
        getCurrentUser(callback);
    }
    
    /**
     * Verifica se o utilizador está autenticado.
     */
    public boolean isAuthenticated() {
        return authManager.isAuthenticated();
    }
    
    /**
     * Verifica se o token está expirado.
     */
    public boolean isTokenExpired() {
        return authManager.isTokenExpired();
    }
    
    /**
     * Obtém o token de acesso atual.
     */
    @Nullable
    public String getAccessToken() {
        return authManager.getAccessToken();
    }
    
    /**
     * Obtém o ID do utilizador autenticado.
     */
    public int getUserId() {
        return authManager.getUserId();
    }
    
    /**
     * Obtém o ID da empresa do utilizador autenticado.
     */
    public int getCompanyId() {
        return authManager.getCompanyId();
    }
}
