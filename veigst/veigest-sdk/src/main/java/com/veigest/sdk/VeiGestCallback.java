package com.veigest.sdk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Callback para operações assíncronas do SDK.
 * 
 * @param <T> Tipo do resultado esperado
 */
public interface VeiGestCallback<T> {
    
    /**
     * Chamado quando a operação é concluída com sucesso.
     * 
     * @param result Resultado da operação
     */
    void onSuccess(@NonNull T result);
    
    /**
     * Chamado quando a operação falha.
     * 
     * @param error Exceção com detalhes do erro
     */
    void onError(@NonNull VeiGestException error);
    
    /**
     * Callback simplificado que ignora erros.
     * Use apenas para operações não críticas.
     * 
     * @param <T> Tipo do resultado
     */
    abstract class SimpleCallback<T> implements VeiGestCallback<T> {
        @Override
        public void onError(@NonNull VeiGestException error) {
            // Ignora erros por padrão
        }
    }
    
    /**
     * Callback que executa uma ação genérica em caso de sucesso ou erro.
     * 
     * @param <T> Tipo do resultado
     */
    abstract class CompletionCallback<T> implements VeiGestCallback<T> {
        
        /**
         * Chamado sempre após a conclusão, seja sucesso ou erro.
         * 
         * @param result Resultado em caso de sucesso, null em caso de erro
         * @param error Erro em caso de falha, null em caso de sucesso
         */
        public abstract void onComplete(@Nullable T result, @Nullable VeiGestException error);
        
        @Override
        public void onSuccess(@NonNull T result) {
            onComplete(result, null);
        }
        
        @Override
        public void onError(@NonNull VeiGestException error) {
            onComplete(null, error);
        }
    }
}
