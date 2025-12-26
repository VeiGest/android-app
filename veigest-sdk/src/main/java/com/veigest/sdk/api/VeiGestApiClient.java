package com.veigest.sdk.api;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.veigest.sdk.VeiGestConfig;
import com.veigest.sdk.auth.AuthManager;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Cliente HTTP para comunicação com a API VeiGest.
 */
public class VeiGestApiClient {
    
    private final Retrofit retrofit;
    private final AuthManager authManager;
    private final VeiGestConfig config;
    
    // APIs disponíveis
    private VeiGestApi veiGestApi;
    
    public VeiGestApiClient(@NonNull VeiGestConfig config, @NonNull AuthManager authManager) {
        this.config = config;
        this.authManager = authManager;
        this.retrofit = createRetrofit();
    }
    
    /**
     * Cria a instância do Retrofit configurada.
     */
    private Retrofit createRetrofit() {
        OkHttpClient client = createOkHttpClient();
        Gson gson = createGson();
        
        String baseUrl = config.getBaseUrl();
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }
        // Adiciona /api/ se não estiver presente
        if (!baseUrl.contains("/api")) {
            baseUrl += "api/";
        }
        
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
    
    /**
     * Cria o cliente OkHttp com interceptors.
     */
    private OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(config.getConnectTimeout(), TimeUnit.SECONDS)
                .readTimeout(config.getReadTimeout(), TimeUnit.SECONDS)
                .writeTimeout(config.getWriteTimeout(), TimeUnit.SECONDS)
                .retryOnConnectionFailure(config.isRetryOnConnectionFailure());
        
        // Interceptor de autenticação
        builder.addInterceptor(createAuthInterceptor());
        
        // Interceptor de headers padrão
        builder.addInterceptor(createHeadersInterceptor());
        
        // Logging (apenas em modo debug)
        if (config.isDebugMode()) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }
        
        return builder.build();
    }
    
    /**
     * Interceptor que adiciona o token de autenticação.
     */
    private Interceptor createAuthInterceptor() {
        return chain -> {
            Request originalRequest = chain.request();
            
            // Não adiciona auth em endpoints públicos
            String path = originalRequest.url().encodedPath();
            if (path.contains("/auth/login") || path.contains("/auth/register")) {
                return chain.proceed(originalRequest);
            }
            
            // Adiciona o token se disponível
            String token = authManager.getAccessToken();
            if (token != null) {
                Request.Builder builder = originalRequest.newBuilder()
                        .header("Authorization", "Bearer " + token);
                return chain.proceed(builder.build());
            }
            
            return chain.proceed(originalRequest);
        };
    }
    
    /**
     * Interceptor que adiciona headers padrão.
     */
    private Interceptor createHeadersInterceptor() {
        return chain -> {
            Request originalRequest = chain.request();
            Request.Builder builder = originalRequest.newBuilder()
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("X-SDK-Version", "1.0.0")
                    .header("X-Platform", "Android");
            
            return chain.proceed(builder.build());
        };
    }
    
    /**
     * Cria instância do Gson configurada.
     */
    private Gson createGson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .setLenient()
                .create();
    }
    
    /**
     * Obtém a API principal do VeiGest.
     */
    @NonNull
    public VeiGestApi getApi() {
        if (veiGestApi == null) {
            veiGestApi = retrofit.create(VeiGestApi.class);
        }
        return veiGestApi;
    }
    
    /**
     * Obtém a instância do Retrofit (para APIs customizadas).
     */
    @NonNull
    public Retrofit getRetrofit() {
        return retrofit;
    }
    
    /**
     * Obtém o gerenciador de autenticação.
     */
    @NonNull
    public AuthManager getAuthManager() {
        return authManager;
    }
}
