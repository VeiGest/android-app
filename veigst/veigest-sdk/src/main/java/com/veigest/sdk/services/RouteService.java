package com.veigest.sdk.services;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.veigest.sdk.VeiGestCallback;
import com.veigest.sdk.api.VeiGestApiClient;
import com.veigest.sdk.models.GpsEntry;
import com.veigest.sdk.models.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Serviço de rotas e GPS do SDK.
 */
public class RouteService extends BaseService {
    
    public RouteService(@NonNull VeiGestApiClient apiClient) {
        super(apiClient);
    }
    
    // ==================== ROTAS ====================
    
    /**
     * Lista todas as rotas.
     */
    public void list(@NonNull VeiGestCallback<List<Route>> callback) {
        list(null, null, null, null, null, null, null, callback);
    }
    
    /**
     * Lista rotas com filtros.
     */
    public void list(@Nullable Integer vehicleId, @Nullable Integer driverId, @Nullable String status,
                     @Nullable String dataInicio, @Nullable String dataFim,
                     @Nullable Integer page, @Nullable Integer limit,
                     @NonNull VeiGestCallback<List<Route>> callback) {
        executeCall(apiClient.getApi().getRoutes(vehicleId, driverId, status, dataInicio, dataFim, page, limit), callback);
    }
    
    /**
     * Lista rotas de um veículo específico.
     */
    public void listByVehicle(int vehicleId, @NonNull VeiGestCallback<List<Route>> callback) {
        list(vehicleId, null, null, null, null, null, null, callback);
    }
    
    /**
     * Lista rotas de um condutor específico.
     */
    public void listByDriver(int driverId, @NonNull VeiGestCallback<List<Route>> callback) {
        list(null, driverId, null, null, null, null, null, callback);
    }
    
    /**
     * Lista rotas em andamento.
     */
    public void listInProgress(@NonNull VeiGestCallback<List<Route>> callback) {
        list(null, null, "em_andamento", null, null, null, null, callback);
    }
    
    /**
     * Lista rotas concluídas.
     */
    public void listCompleted(@NonNull VeiGestCallback<List<Route>> callback) {
        list(null, null, "concluida", null, null, null, null, callback);
    }
    
    /**
     * Obtém uma rota por ID.
     */
    public void get(int id, @NonNull VeiGestCallback<Route> callback) {
        executeCall(apiClient.getApi().getRoute(id), callback);
    }
    
    /**
     * Inicia uma nova rota.
     */
    public void start(int vehicleId, int driverId, @NonNull String origem, @NonNull String destino,
                      int kmInicial, @NonNull VeiGestCallback<Route> callback) {
        Map<String, Object> body = createBody();
        body.put("vehicle_id", vehicleId);
        body.put("driver_id", driverId);
        body.put("origem", origem);
        body.put("destino", destino);
        body.put("km_inicial", kmInicial);
        
        executeCall(apiClient.getApi().createRoute(body), callback);
    }
    
    /**
     * Cria rota com builder.
     */
    public void create(@NonNull RouteBuilder builder, @NonNull VeiGestCallback<Route> callback) {
        executeCall(apiClient.getApi().createRoute(builder.build()), callback);
    }
    
    /**
     * Atualiza uma rota.
     */
    public void update(int id, @NonNull Map<String, Object> data,
                       @NonNull VeiGestCallback<Route> callback) {
        executeCall(apiClient.getApi().updateRoute(id, data), callback);
    }
    
    /**
     * Finaliza uma rota.
     */
    public void finish(int id, int kmFinal, @Nullable String notas,
                       @NonNull VeiGestCallback<Route> callback) {
        Map<String, Object> body = createBody();
        body.put("km_final", kmFinal);
        addIfNotNull(body, "notas", notas);
        
        executeCall(apiClient.getApi().finishRoute(id, body), callback);
    }
    
    /**
     * Cancela uma rota.
     */
    public void cancel(int id, @Nullable String notas, @NonNull VeiGestCallback<Route> callback) {
        Map<String, Object> body = createBody();
        addIfNotNull(body, "notas", notas);
        
        executeCall(apiClient.getApi().cancelRoute(id, body), callback);
    }
    
    /**
     * Elimina uma rota.
     */
    public void delete(int id, @NonNull VeiGestCallback<Void> callback) {
        executeCall(apiClient.getApi().deleteRoute(id), callback);
    }
    
    // ==================== GPS ====================
    
    /**
     * Lista pontos GPS de uma rota.
     */
    public void getGpsEntries(int routeId, @NonNull VeiGestCallback<List<GpsEntry>> callback) {
        getGpsEntries(routeId, null, null, callback);
    }
    
    /**
     * Lista pontos GPS de uma rota com paginação.
     */
    public void getGpsEntries(int routeId, @Nullable Integer page, @Nullable Integer limit,
                              @NonNull VeiGestCallback<List<GpsEntry>> callback) {
        executeCall(apiClient.getApi().getRouteGpsEntries(routeId, page, limit), callback);
    }
    
    /**
     * Adiciona um ponto GPS a uma rota.
     */
    public void addGpsEntry(int routeId, double latitude, double longitude,
                            @Nullable Double velocidade, @Nullable Double altitude,
                            @NonNull VeiGestCallback<GpsEntry> callback) {
        Map<String, Object> body = createBody();
        body.put("route_id", routeId);
        body.put("latitude", latitude);
        body.put("longitude", longitude);
        addIfNotNull(body, "velocidade", velocidade);
        addIfNotNull(body, "altitude", altitude);
        
        executeCall(apiClient.getApi().createGpsEntry(body), callback);
    }
    
    /**
     * Adiciona um ponto GPS usando o modelo GpsEntry.
     */
    public void addGpsEntry(@NonNull GpsEntry entry, @NonNull VeiGestCallback<GpsEntry> callback) {
        Map<String, Object> body = createBody();
        body.put("route_id", entry.getRouteId());
        body.put("latitude", entry.getLatitude());
        body.put("longitude", entry.getLongitude());
        if (entry.getVelocidade() > 0) body.put("velocidade", entry.getVelocidade());
        if (entry.getAltitude() > 0) body.put("altitude", entry.getAltitude());
        if (entry.getPrecisao() > 0) body.put("precisao", entry.getPrecisao());
        
        executeCall(apiClient.getApi().createGpsEntry(body), callback);
    }
    
    /**
     * Adiciona múltiplos pontos GPS de uma vez (batch).
     */
    public void addGpsEntriesBatch(int routeId, @NonNull List<GpsEntry> entries,
                                    @NonNull VeiGestCallback<Void> callback) {
        Map<String, Object> body = createBody();
        body.put("route_id", routeId);
        
        List<Map<String, Object>> entriesData = new ArrayList<>();
        for (GpsEntry entry : entries) {
            Map<String, Object> entryMap = new HashMap<>();
            entryMap.put("latitude", entry.getLatitude());
            entryMap.put("longitude", entry.getLongitude());
            if (entry.getVelocidade() > 0) entryMap.put("velocidade", entry.getVelocidade());
            if (entry.getAltitude() > 0) entryMap.put("altitude", entry.getAltitude());
            entriesData.add(entryMap);
        }
        body.put("entries", entriesData);
        
        executeCall(apiClient.getApi().createGpsEntriesBatch(body), callback);
    }
    
    /**
     * Elimina um ponto GPS.
     */
    public void deleteGpsEntry(int id, @NonNull VeiGestCallback<Void> callback) {
        executeCall(apiClient.getApi().deleteGpsEntry(id), callback);
    }
    
    /**
     * Builder para criar rotas.
     */
    public static class RouteBuilder {
        private final Map<String, Object> data = new HashMap<>();
        
        public RouteBuilder vehicleId(int vehicleId) {
            data.put("vehicle_id", vehicleId);
            return this;
        }
        
        public RouteBuilder driverId(int driverId) {
            data.put("driver_id", driverId);
            return this;
        }
        
        public RouteBuilder companyId(int companyId) {
            data.put("company_id", companyId);
            return this;
        }
        
        public RouteBuilder origem(@NonNull String origem) {
            data.put("origem", origem);
            return this;
        }
        
        public RouteBuilder destino(@NonNull String destino) {
            data.put("destino", destino);
            return this;
        }
        
        public RouteBuilder kmInicial(int kmInicial) {
            data.put("km_inicial", kmInicial);
            return this;
        }
        
        Map<String, Object> build() {
            return data;
        }
    }
}
