package com.veigest.sdk.services;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.veigest.sdk.VeiGestCallback;
import com.veigest.sdk.VeiGestException;
import com.veigest.sdk.api.VeiGestApiClient;
import com.veigest.sdk.models.ApiResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Classe base para todos os serviços do SDK.
 * Fornece métodos utilitários para chamadas à API.
 */
public abstract class BaseService {
    
    protected final VeiGestApiClient apiClient;
    private final Handler mainHandler;
    private final Gson gson;
    
    protected BaseService(@NonNull VeiGestApiClient apiClient) {
        this.apiClient = apiClient;
        this.mainHandler = new Handler(Looper.getMainLooper());
        this.gson = new Gson();
    }
    
    /**
     * Executa uma chamada assíncrona e processa a resposta.
     */
    protected <T> void executeCall(@NonNull Call<ApiResponse<T>> call, 
                                   @NonNull VeiGestCallback<T> callback) {
        call.enqueue(new Callback<ApiResponse<T>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<T>> call, 
                                   @NonNull Response<ApiResponse<T>> response) {
                handleResponse(response, callback);
            }
            
            @Override
            public void onFailure(@NonNull Call<ApiResponse<T>> call, @NonNull Throwable t) {
                handleFailure(t, callback);
            }
        });
    }
    
    /**
     * Processa a resposta da API.
     */
    protected <T> void handleResponse(@NonNull Response<ApiResponse<T>> response, 
                                      @NonNull VeiGestCallback<T> callback) {
        if (response.isSuccessful() && response.body() != null) {
            ApiResponse<T> apiResponse = response.body();
            T data = apiResponse.getData();
            
            if (data != null) {
                runOnMainThread(() -> callback.onSuccess(data));
            } else if (apiResponse.isSuccess()) {
                // Para respostas sem dados (ex: delete)
                runOnMainThread(() -> callback.onSuccess(null));
            } else {
                String message = apiResponse.getMessage();
                if (message == null && apiResponse.getError() != null) {
                    message = apiResponse.getError().getMessage();
                }
                VeiGestException error = new VeiGestException(
                        message != null ? message : "Resposta inválida da API",
                        response.code(),
                        null
                );
                runOnMainThread(() -> callback.onError(error));
            }
        } else {
            VeiGestException error = parseErrorResponse(response);
            runOnMainThread(() -> callback.onError(error));
        }
    }
    
    /**
     * Processa falhas de conexão.
     */
    protected <T> void handleFailure(@NonNull Throwable t, @NonNull VeiGestCallback<T> callback) {
        VeiGestException error;
        
        if (t instanceof java.net.SocketTimeoutException) {
            error = VeiGestException.timeoutError();
        } else if (t instanceof java.net.UnknownHostException) {
            error = new VeiGestException("Sem conexão com a internet. Verifique sua conexão.", t);
        } else if (t instanceof IOException) {
            error = VeiGestException.networkError(t);
        } else {
            error = new VeiGestException("Erro inesperado: " + t.getMessage(), t);
        }
        
        runOnMainThread(() -> callback.onError(error));
    }
    
    /**
     * Analisa a resposta de erro da API.
     */
    @NonNull
    protected VeiGestException parseErrorResponse(@NonNull Response<?> response) {
        int code = response.code();
        String message = "Erro desconhecido";
        String errorCode = null;
        Map<String, List<String>> validationErrors = null;
        
        try {
            if (response.errorBody() != null) {
                String errorJson = response.errorBody().string();
                ApiResponse.ApiError apiError = gson.fromJson(errorJson, ApiResponse.ApiError.class);
                
                if (apiError != null && apiError.getMessage() != null) {
                    message = apiError.getMessage();
                }
            }
        } catch (Exception e) {
            // Ignora erros ao parsear
        }
        
        // Mensagens padrão por código HTTP
        if (message.equals("Erro desconhecido")) {
            switch (code) {
                case 400:
                    message = "Requisição inválida";
                    break;
                case 401:
                    message = "Não autenticado. Faça login novamente.";
                    break;
                case 403:
                    message = "Sem permissão para esta operação";
                    break;
                case 404:
                    message = "Recurso não encontrado";
                    break;
                case 422:
                    message = "Dados inválidos";
                    break;
                case 500:
                    message = "Erro interno do servidor";
                    break;
            }
        }
        
        return new VeiGestException(message, code, errorCode, validationErrors);
    }
    
    /**
     * Executa código na thread principal.
     */
    protected void runOnMainThread(@NonNull Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        } else {
            mainHandler.post(runnable);
        }
    }
    
    /**
     * Cria um mapa de parâmetros para body de requisições.
     */
    @NonNull
    protected Map<String, Object> createBody() {
        return new HashMap<>();
    }
    
    /**
     * Adiciona um parâmetro ao mapa se não for nulo.
     */
    protected void addIfNotNull(@NonNull Map<String, Object> map, 
                                @NonNull String key, Object value) {
        if (value != null) {
            map.put(key, value);
        }
    }
}
