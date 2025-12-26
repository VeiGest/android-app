package com.veigest.sdk.services;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.veigest.sdk.VeiGestCallback;
import com.veigest.sdk.api.VeiGestApiClient;
import com.veigest.sdk.models.Alert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Serviço de alertas do SDK.
 */
public class AlertService extends BaseService {
    
    public AlertService(@NonNull VeiGestApiClient apiClient) {
        super(apiClient);
    }
    
    /**
     * Lista todos os alertas.
     */
    public void list(@NonNull VeiGestCallback<List<Alert>> callback) {
        list(null, null, null, null, null, callback);
    }
    
    /**
     * Lista alertas com filtros.
     */
    public void list(@Nullable String tipo, @Nullable String status, @Nullable String prioridade,
                     @Nullable Integer page, @Nullable Integer limit,
                     @NonNull VeiGestCallback<List<Alert>> callback) {
        executeCall(apiClient.getApi().getAlerts(tipo, status, prioridade, page, limit), callback);
    }
    
    /**
     * Lista alertas ativos.
     */
    public void listActive(@NonNull VeiGestCallback<List<Alert>> callback) {
        list(null, "ativo", null, null, null, callback);
    }
    
    /**
     * Lista alertas de alta prioridade.
     */
    public void listHighPriority(@NonNull VeiGestCallback<List<Alert>> callback) {
        list(null, "ativo", "alta", null, null, callback);
    }
    
    /**
     * Lista alertas por tipo.
     */
    public void listByType(@NonNull String tipo, @NonNull VeiGestCallback<List<Alert>> callback) {
        list(tipo, null, null, null, null, callback);
    }
    
    /**
     * Obtém um alerta por ID.
     */
    public void get(int id, @NonNull VeiGestCallback<Alert> callback) {
        executeCall(apiClient.getApi().getAlert(id), callback);
    }
    
    /**
     * Cria um novo alerta.
     */
    public void create(@NonNull String tipo, @NonNull String titulo,
                       @Nullable String descricao, @Nullable String prioridade,
                       @NonNull VeiGestCallback<Alert> callback) {
        Map<String, Object> body = createBody();
        body.put("tipo", tipo);
        body.put("titulo", titulo);
        addIfNotNull(body, "descricao", descricao);
        addIfNotNull(body, "prioridade", prioridade);
        
        executeCall(apiClient.getApi().createAlert(body), callback);
    }
    
    /**
     * Cria alerta com builder.
     */
    public void create(@NonNull AlertBuilder builder, @NonNull VeiGestCallback<Alert> callback) {
        executeCall(apiClient.getApi().createAlert(builder.build()), callback);
    }
    
    /**
     * Resolve um alerta.
     */
    public void resolve(int id, @Nullable String notas, @NonNull VeiGestCallback<Alert> callback) {
        Map<String, String> body = new HashMap<>();
        if (notas != null) {
            body.put("notas", notas);
        }
        executeCall(apiClient.getApi().resolveAlert(id, body), callback);
    }
    
    /**
     * Resolve um alerta sem notas.
     */
    public void resolve(int id, @NonNull VeiGestCallback<Alert> callback) {
        resolve(id, null, callback);
    }
    
    /**
     * Ignora um alerta.
     */
    public void ignore(int id, @Nullable String notas, @NonNull VeiGestCallback<Alert> callback) {
        Map<String, String> body = new HashMap<>();
        if (notas != null) {
            body.put("notas", notas);
        }
        executeCall(apiClient.getApi().ignoreAlert(id, body), callback);
    }
    
    /**
     * Ignora um alerta sem notas.
     */
    public void ignore(int id, @NonNull VeiGestCallback<Alert> callback) {
        ignore(id, null, callback);
    }
    
    /**
     * Elimina um alerta.
     */
    public void delete(int id, @NonNull VeiGestCallback<Void> callback) {
        executeCall(apiClient.getApi().deleteAlert(id), callback);
    }
    
    /**
     * Builder para criar alertas.
     */
    public static class AlertBuilder {
        private final Map<String, Object> data = new HashMap<>();
        
        public AlertBuilder tipo(@NonNull String tipo) {
            data.put("tipo", tipo);
            return this;
        }
        
        public AlertBuilder titulo(@NonNull String titulo) {
            data.put("titulo", titulo);
            return this;
        }
        
        public AlertBuilder descricao(@NonNull String descricao) {
            data.put("descricao", descricao);
            return this;
        }
        
        public AlertBuilder prioridade(@NonNull String prioridade) {
            data.put("prioridade", prioridade);
            return this;
        }
        
        public AlertBuilder companyId(int companyId) {
            data.put("company_id", companyId);
            return this;
        }
        
        public AlertBuilder detalhes(@NonNull Map<String, Object> detalhes) {
            data.put("detalhes", detalhes);
            return this;
        }
        
        Map<String, Object> build() {
            return data;
        }
    }
}
