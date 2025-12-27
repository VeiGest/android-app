package com.veigest.sdk;

import androidx.annotation.NonNull;

/**
 * Configurações do SDK VeiGest.
 * 
 * <p>Use o Builder para criar uma instância:</p>
 * <pre>
 * VeiGestConfig config = new VeiGestConfig.Builder()
 *     .setBaseUrl("https://veigestback.dryadlang.org")
 *     .setDebugMode(BuildConfig.DEBUG)
 *     .setConnectTimeout(30)
 *     .setReadTimeout(30)
 *     .build();
 * </pre>
 */
public final class VeiGestConfig {
    
    /** URL base padrão da API */
    public static final String DEFAULT_BASE_URL = "https://veigestback.dryadlang.org";
    
    /** Timeout padrão de conexão em segundos */
    public static final int DEFAULT_CONNECT_TIMEOUT = 30;
    
    /** Timeout padrão de leitura em segundos */
    public static final int DEFAULT_READ_TIMEOUT = 30;
    
    /** Timeout padrão de escrita em segundos */
    public static final int DEFAULT_WRITE_TIMEOUT = 30;
    
    private final String baseUrl;
    private final boolean debugMode;
    private final int connectTimeout;
    private final int readTimeout;
    private final int writeTimeout;
    private final boolean retryOnConnectionFailure;
    
    private VeiGestConfig(Builder builder) {
        this.baseUrl = builder.baseUrl;
        this.debugMode = builder.debugMode;
        this.connectTimeout = builder.connectTimeout;
        this.readTimeout = builder.readTimeout;
        this.writeTimeout = builder.writeTimeout;
        this.retryOnConnectionFailure = builder.retryOnConnectionFailure;
    }
    
    /**
     * URL base da API.
     */
    @NonNull
    public String getBaseUrl() {
        return baseUrl;
    }
    
    /**
     * Indica se o modo debug está ativado.
     * Quando ativado, os logs HTTP são exibidos.
     */
    public boolean isDebugMode() {
        return debugMode;
    }
    
    /**
     * Timeout de conexão em segundos.
     */
    public int getConnectTimeout() {
        return connectTimeout;
    }
    
    /**
     * Timeout de leitura em segundos.
     */
    public int getReadTimeout() {
        return readTimeout;
    }
    
    /**
     * Timeout de escrita em segundos.
     */
    public int getWriteTimeout() {
        return writeTimeout;
    }
    
    /**
     * Indica se deve tentar reconectar automaticamente em caso de falha.
     */
    public boolean isRetryOnConnectionFailure() {
        return retryOnConnectionFailure;
    }
    
    /**
     * Builder para criar configurações do SDK.
     */
    public static class Builder {
        private String baseUrl = DEFAULT_BASE_URL;
        private boolean debugMode = false;
        private int connectTimeout = DEFAULT_CONNECT_TIMEOUT;
        private int readTimeout = DEFAULT_READ_TIMEOUT;
        private int writeTimeout = DEFAULT_WRITE_TIMEOUT;
        private boolean retryOnConnectionFailure = true;
        
        /**
         * Define a URL base da API.
         * 
         * @param baseUrl URL base (ex: "https://veigestback.dryadlang.org")
         * @return Builder
         */
        @NonNull
        public Builder setBaseUrl(@NonNull String baseUrl) {
            // Remove trailing slash se existir
            this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
            return this;
        }
        
        /**
         * Ativa ou desativa o modo debug.
         * Quando ativado, os logs HTTP são exibidos no Logcat.
         * 
         * @param debugMode true para ativar, false para desativar
         * @return Builder
         */
        @NonNull
        public Builder setDebugMode(boolean debugMode) {
            this.debugMode = debugMode;
            return this;
        }
        
        /**
         * Define o timeout de conexão.
         * 
         * @param seconds Timeout em segundos
         * @return Builder
         */
        @NonNull
        public Builder setConnectTimeout(int seconds) {
            this.connectTimeout = seconds;
            return this;
        }
        
        /**
         * Define o timeout de leitura.
         * 
         * @param seconds Timeout em segundos
         * @return Builder
         */
        @NonNull
        public Builder setReadTimeout(int seconds) {
            this.readTimeout = seconds;
            return this;
        }
        
        /**
         * Define o timeout de escrita.
         * 
         * @param seconds Timeout em segundos
         * @return Builder
         */
        @NonNull
        public Builder setWriteTimeout(int seconds) {
            this.writeTimeout = seconds;
            return this;
        }
        
        /**
         * Define se deve tentar reconectar automaticamente em caso de falha.
         * 
         * @param retry true para ativar, false para desativar
         * @return Builder
         */
        @NonNull
        public Builder setRetryOnConnectionFailure(boolean retry) {
            this.retryOnConnectionFailure = retry;
            return this;
        }
        
        /**
         * Constrói a configuração do SDK.
         * 
         * @return Instância de VeiGestConfig
         */
        @NonNull
        public VeiGestConfig build() {
            return new VeiGestConfig(this);
        }
    }
}
