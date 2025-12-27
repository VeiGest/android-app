package com.veigest.sdk.services;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.veigest.sdk.VeiGestCallback;
import com.veigest.sdk.api.VeiGestApiClient;
import com.veigest.sdk.models.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Serviço de documentos do SDK.
 */
public class DocumentService extends BaseService {
    
    public DocumentService(@NonNull VeiGestApiClient apiClient) {
        super(apiClient);
    }
    
    /**
     * Lista todos os documentos.
     */
    public void list(@NonNull VeiGestCallback<List<Document>> callback) {
        list(null, null, null, null, null, null, callback);
    }
    
    /**
     * Lista documentos com filtros.
     */
    public void list(@Nullable Integer vehicleId, @Nullable Integer driverId, @Nullable String tipo,
                     @Nullable String status, @Nullable Integer page, @Nullable Integer limit,
                     @NonNull VeiGestCallback<List<Document>> callback) {
        executeCall(apiClient.getApi().getDocuments(vehicleId, driverId, tipo, status, page, limit), callback);
    }
    
    /**
     * Lista documentos de um veículo específico.
     */
    public void listByVehicle(int vehicleId, @NonNull VeiGestCallback<List<Document>> callback) {
        list(vehicleId, null, null, null, null, null, callback);
    }
    
    /**
     * Lista documentos de um condutor específico.
     */
    public void listByDriver(int driverId, @NonNull VeiGestCallback<List<Document>> callback) {
        list(null, driverId, null, null, null, null, callback);
    }
    
    /**
     * Lista documentos por tipo.
     */
    public void listByType(@NonNull String tipo, @NonNull VeiGestCallback<List<Document>> callback) {
        list(null, null, tipo, null, null, null, callback);
    }
    
    /**
     * Lista documentos válidos.
     */
    public void listValid(@NonNull VeiGestCallback<List<Document>> callback) {
        list(null, null, null, "valido", null, null, callback);
    }
    
    /**
     * Lista documentos expirados.
     */
    public void listExpired(@NonNull VeiGestCallback<List<Document>> callback) {
        list(null, null, null, "expirado", null, null, callback);
    }
    
    /**
     * Lista documentos a expirar nos próximos dias.
     * 
     * @param dias Número de dias (default: 30)
     */
    public void listExpiring(@Nullable Integer dias, @NonNull VeiGestCallback<List<Document>> callback) {
        executeCall(apiClient.getApi().getExpiringDocuments(dias), callback);
    }
    
    /**
     * Lista documentos a expirar nos próximos 30 dias.
     */
    public void listExpiring(@NonNull VeiGestCallback<List<Document>> callback) {
        listExpiring(null, callback);
    }
    
    /**
     * Obtém um documento por ID.
     */
    public void get(int id, @NonNull VeiGestCallback<Document> callback) {
        executeCall(apiClient.getApi().getDocument(id), callback);
    }
    
    /**
     * Cria um novo documento.
     */
    public void create(@NonNull String tipo, @NonNull String dataValidade,
                       @Nullable Integer vehicleId, @Nullable Integer driverId,
                       @Nullable String notas, @NonNull VeiGestCallback<Document> callback) {
        Map<String, Object> body = createBody();
        body.put("tipo", tipo);
        body.put("data_validade", dataValidade);
        addIfNotNull(body, "vehicle_id", vehicleId);
        addIfNotNull(body, "driver_id", driverId);
        addIfNotNull(body, "notas", notas);
        
        executeCall(apiClient.getApi().createDocument(body), callback);
    }
    
    /**
     * Cria documento com builder.
     */
    public void create(@NonNull DocumentBuilder builder, @NonNull VeiGestCallback<Document> callback) {
        executeCall(apiClient.getApi().createDocument(builder.build()), callback);
    }
    
    /**
     * Atualiza um documento.
     */
    public void update(int id, @NonNull Map<String, Object> data,
                       @NonNull VeiGestCallback<Document> callback) {
        executeCall(apiClient.getApi().updateDocument(id, data), callback);
    }
    
    /**
     * Atualiza campos específicos de um documento.
     */
    public void patch(int id, @NonNull Map<String, Object> data,
                      @NonNull VeiGestCallback<Document> callback) {
        executeCall(apiClient.getApi().patchDocument(id, data), callback);
    }
    
    /**
     * Atualiza o status de um documento.
     */
    public void updateStatus(int id, @NonNull String status, @NonNull VeiGestCallback<Document> callback) {
        Map<String, Object> body = createBody();
        body.put("status", status);
        patch(id, body, callback);
    }
    
    /**
     * Elimina um documento.
     */
    public void delete(int id, @NonNull VeiGestCallback<Void> callback) {
        executeCall(apiClient.getApi().deleteDocument(id), callback);
    }
    
    /**
     * Builder para criar documentos.
     */
    public static class DocumentBuilder {
        private final Map<String, Object> data = new HashMap<>();
        
        public DocumentBuilder tipo(@NonNull String tipo) {
            data.put("tipo", tipo);
            return this;
        }
        
        public DocumentBuilder dataValidade(@NonNull String dataValidade) {
            data.put("data_validade", dataValidade);
            return this;
        }
        
        public DocumentBuilder vehicleId(int vehicleId) {
            data.put("vehicle_id", vehicleId);
            return this;
        }
        
        public DocumentBuilder driverId(int driverId) {
            data.put("driver_id", driverId);
            return this;
        }
        
        public DocumentBuilder fileId(int fileId) {
            data.put("file_id", fileId);
            return this;
        }
        
        public DocumentBuilder companyId(int companyId) {
            data.put("company_id", companyId);
            return this;
        }
        
        public DocumentBuilder notas(@NonNull String notas) {
            data.put("notas", notas);
            return this;
        }
        
        Map<String, Object> build() {
            return data;
        }
    }
}
