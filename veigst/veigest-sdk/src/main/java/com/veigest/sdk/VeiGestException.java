package com.veigest.sdk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Map;

/**
 * Exceção personalizada para erros do SDK VeiGest.
 */
public class VeiGestException extends Exception {
    
    /** Código HTTP do erro (0 se não aplicável) */
    private final int httpCode;
    
    /** Código de erro da API */
    private final String errorCode;
    
    /** Detalhes adicionais do erro */
    private final Map<String, List<String>> validationErrors;
    
    /** Tipo de erro */
    private final ErrorType type;
    
    /**
     * Tipos de erro possíveis.
     */
    public enum ErrorType {
        /** Erro de rede (sem conexão, timeout, etc.) */
        NETWORK,
        
        /** Erro de autenticação (token inválido, expirado, etc.) */
        AUTHENTICATION,
        
        /** Erro de autorização (sem permissão) */
        AUTHORIZATION,
        
        /** Erro de validação (dados inválidos) */
        VALIDATION,
        
        /** Recurso não encontrado */
        NOT_FOUND,
        
        /** Erro interno do servidor */
        SERVER,
        
        /** Erro desconhecido */
        UNKNOWN
    }
    
    public VeiGestException(@NonNull String message) {
        super(message);
        this.httpCode = 0;
        this.errorCode = null;
        this.validationErrors = null;
        this.type = ErrorType.UNKNOWN;
    }
    
    public VeiGestException(@NonNull String message, @NonNull Throwable cause) {
        super(message, cause);
        this.httpCode = 0;
        this.errorCode = null;
        this.validationErrors = null;
        this.type = ErrorType.NETWORK;
    }
    
    public VeiGestException(@NonNull String message, int httpCode, @Nullable String errorCode) {
        super(message);
        this.httpCode = httpCode;
        this.errorCode = errorCode;
        this.validationErrors = null;
        this.type = determineType(httpCode);
    }
    
    public VeiGestException(@NonNull String message, int httpCode, @Nullable String errorCode, 
                           @Nullable Map<String, List<String>> validationErrors) {
        super(message);
        this.httpCode = httpCode;
        this.errorCode = errorCode;
        this.validationErrors = validationErrors;
        this.type = determineType(httpCode);
    }
    
    public VeiGestException(@NonNull String message, @NonNull ErrorType type) {
        super(message);
        this.httpCode = 0;
        this.errorCode = null;
        this.validationErrors = null;
        this.type = type;
    }
    
    private ErrorType determineType(int httpCode) {
        if (httpCode == 401) {
            return ErrorType.AUTHENTICATION;
        } else if (httpCode == 403) {
            return ErrorType.AUTHORIZATION;
        } else if (httpCode == 404) {
            return ErrorType.NOT_FOUND;
        } else if (httpCode == 422 || httpCode == 400) {
            return ErrorType.VALIDATION;
        } else if (httpCode >= 500) {
            return ErrorType.SERVER;
        }
        return ErrorType.UNKNOWN;
    }
    
    /**
     * Código HTTP do erro.
     * 
     * @return Código HTTP ou 0 se não aplicável
     */
    public int getHttpCode() {
        return httpCode;
    }
    
    /**
     * Código de erro da API.
     * 
     * @return Código de erro ou null
     */
    @Nullable
    public String getErrorCode() {
        return errorCode;
    }
    
    /**
     * Erros de validação por campo.
     * 
     * @return Map com campo -> lista de erros, ou null
     */
    @Nullable
    public Map<String, List<String>> getValidationErrors() {
        return validationErrors;
    }
    
    /**
     * Tipo de erro.
     * 
     * @return Tipo do erro
     */
    @NonNull
    public ErrorType getType() {
        return type;
    }
    
    /**
     * Verifica se é um erro de rede.
     */
    public boolean isNetworkError() {
        return type == ErrorType.NETWORK;
    }
    
    /**
     * Verifica se é um erro de autenticação.
     */
    public boolean isAuthenticationError() {
        return type == ErrorType.AUTHENTICATION;
    }
    
    /**
     * Verifica se é um erro de validação.
     */
    public boolean isValidationError() {
        return type == ErrorType.VALIDATION;
    }
    
    /**
     * Verifica se é um erro do servidor.
     */
    public boolean isServerError() {
        return type == ErrorType.SERVER;
    }
    
    /**
     * Cria uma exceção de erro de rede.
     */
    @NonNull
    public static VeiGestException networkError(@NonNull Throwable cause) {
        return new VeiGestException("Erro de conexão. Verifique sua internet.", cause);
    }
    
    /**
     * Cria uma exceção de erro de autenticação.
     */
    @NonNull
    public static VeiGestException authenticationError(@NonNull String message) {
        return new VeiGestException(message, 401, "UNAUTHENTICATED");
    }
    
    /**
     * Cria uma exceção de timeout.
     */
    @NonNull
    public static VeiGestException timeoutError() {
        return new VeiGestException("A requisição demorou muito. Tente novamente.", ErrorType.NETWORK);
    }
}
