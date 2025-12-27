package com.veigest.sdk.services;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.veigest.sdk.VeiGestCallback;
import com.veigest.sdk.api.VeiGestApiClient;
import com.veigest.sdk.models.FuelAlert;
import com.veigest.sdk.models.FuelEfficiencyReport;
import com.veigest.sdk.models.FuelLog;
import com.veigest.sdk.models.ReportStats;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Serviço de registos de abastecimento do SDK.
 */
public class FuelLogService extends BaseService {
    
    public FuelLogService(@NonNull VeiGestApiClient apiClient) {
        super(apiClient);
    }
    
    /**
     * Lista todos os registos de abastecimento.
     */
    public void list(@NonNull VeiGestCallback<List<FuelLog>> callback) {
        list(null, null, null, null, null, null, callback);
    }
    
    /**
     * Lista registos de abastecimento com filtros.
     */
    public void list(@Nullable Integer vehicleId, @Nullable Integer driverId,
                     @Nullable String dataInicio, @Nullable String dataFim,
                     @Nullable Integer page, @Nullable Integer limit,
                     @NonNull VeiGestCallback<List<FuelLog>> callback) {
        executeCall(apiClient.getApi().getFuelLogs(vehicleId, driverId, dataInicio, dataFim, page, limit), callback);
    }
    
    /**
     * Lista registos de abastecimento de um veículo específico.
     */
    public void listByVehicle(int vehicleId, @NonNull VeiGestCallback<List<FuelLog>> callback) {
        list(vehicleId, null, null, null, null, null, callback);
    }
    
    /**
     * Lista registos de abastecimento de um condutor específico.
     */
    public void listByDriver(int driverId, @NonNull VeiGestCallback<List<FuelLog>> callback) {
        list(null, driverId, null, null, null, null, callback);
    }
    
    /**
     * Obtém um registo de abastecimento por ID.
     */
    public void get(int id, @NonNull VeiGestCallback<FuelLog> callback) {
        executeCall(apiClient.getApi().getFuelLog(id), callback);
    }
    
    /**
     * Cria um novo registo de abastecimento.
     */
    public void create(int vehicleId, @NonNull String data, double litros, double valor, int kmAtual,
                       @NonNull VeiGestCallback<FuelLog> callback) {
        create(vehicleId, null, data, litros, valor, kmAtual, null, callback);
    }
    
    /**
     * Cria um novo registo de abastecimento completo.
     */
    public void create(int vehicleId, @Nullable Integer driverId, @NonNull String data,
                       double litros, double valor, int kmAtual, @Nullable String notas,
                       @NonNull VeiGestCallback<FuelLog> callback) {
        Map<String, Object> body = createBody();
        body.put("vehicle_id", vehicleId);
        addIfNotNull(body, "driver_id", driverId);
        body.put("data", data);
        body.put("litros", litros);
        body.put("valor", valor);
        body.put("km_atual", kmAtual);
        addIfNotNull(body, "notas", notas);
        
        executeCall(apiClient.getApi().createFuelLog(body), callback);
    }
    
    /**
     * Cria registo de abastecimento com builder.
     */
    public void create(@NonNull FuelLogBuilder builder, @NonNull VeiGestCallback<FuelLog> callback) {
        executeCall(apiClient.getApi().createFuelLog(builder.build()), callback);
    }
    
    /**
     * Atualiza um registo de abastecimento.
     */
    public void update(int id, @NonNull Map<String, Object> data,
                       @NonNull VeiGestCallback<FuelLog> callback) {
        executeCall(apiClient.getApi().updateFuelLog(id, data), callback);
    }
    
    /**
     * Atualiza campos específicos.
     */
    public void patch(int id, @NonNull Map<String, Object> data,
                      @NonNull VeiGestCallback<FuelLog> callback) {
        executeCall(apiClient.getApi().patchFuelLog(id, data), callback);
    }
    
    /**
     * Elimina um registo de abastecimento.
     */
    public void delete(int id, @NonNull VeiGestCallback<Void> callback) {
        executeCall(apiClient.getApi().deleteFuelLog(id), callback);
    }
    
    // ==================== NOVOS ENDPOINTS ====================
    
    /**
     * Lista abastecimentos de um veículo específico (via endpoint dedicado).
     * 
     * @param vehicleId ID do veículo
     * @param callback Callback com lista de abastecimentos
     */
    public void getByVehicle(int vehicleId, @NonNull VeiGestCallback<List<FuelLog>> callback) {
        executeCall(apiClient.getApi().getFuelLogsByVehicle(vehicleId), callback);
    }
    
    /**
     * Obtém estatísticas de consumo de combustível.
     * 
     * @param callback Callback com estatísticas
     */
    public void getStats(@NonNull VeiGestCallback<ReportStats> callback) {
        getStats(null, null, null, callback);
    }
    
    /**
     * Obtém estatísticas de consumo de combustível com filtros.
     * 
     * @param vehicleId ID do veículo (null para todos)
     * @param startDate Data início (formato: YYYY-MM-DD)
     * @param endDate Data fim (formato: YYYY-MM-DD)
     * @param callback Callback com estatísticas
     */
    public void getStats(@Nullable Integer vehicleId, @Nullable String startDate,
                        @Nullable String endDate, @NonNull VeiGestCallback<ReportStats> callback) {
        executeCall(apiClient.getApi().getFuelLogStats(vehicleId, startDate, endDate), callback);
    }
    
    /**
     * Obtém alertas de combustível/eficiência.
     * 
     * @param callback Callback com lista de alertas
     */
    public void getAlerts(@NonNull VeiGestCallback<List<FuelAlert>> callback) {
        executeCall(apiClient.getApi().getFuelAlerts(), callback);
    }
    
    /**
     * Obtém relatório de eficiência de combustível.
     * 
     * @param callback Callback com relatório
     */
    public void getEfficiencyReport(@NonNull VeiGestCallback<FuelEfficiencyReport> callback) {
        getEfficiencyReport(null, null, callback);
    }
    
    /**
     * Obtém relatório de eficiência de combustível para um período.
     * 
     * @param startDate Data início (formato: YYYY-MM-DD)
     * @param endDate Data fim (formato: YYYY-MM-DD)
     * @param callback Callback com relatório
     */
    public void getEfficiencyReport(@Nullable String startDate, @Nullable String endDate,
                                    @NonNull VeiGestCallback<FuelEfficiencyReport> callback) {
        executeCall(apiClient.getApi().getFuelEfficiencyReport(startDate, endDate), callback);
    }
    
    /**
     * Builder para criar registos de abastecimento.
     */
    public static class FuelLogBuilder {
        private final Map<String, Object> data = new HashMap<>();
        
        public FuelLogBuilder vehicleId(int vehicleId) {
            data.put("vehicle_id", vehicleId);
            return this;
        }
        
        public FuelLogBuilder driverId(int driverId) {
            data.put("driver_id", driverId);
            return this;
        }
        
        public FuelLogBuilder companyId(int companyId) {
            data.put("company_id", companyId);
            return this;
        }
        
        public FuelLogBuilder data(@NonNull String data) {
            this.data.put("data", data);
            return this;
        }
        
        public FuelLogBuilder litros(double litros) {
            data.put("litros", litros);
            return this;
        }
        
        public FuelLogBuilder valor(double valor) {
            data.put("valor", valor);
            return this;
        }
        
        public FuelLogBuilder kmAtual(int kmAtual) {
            data.put("km_atual", kmAtual);
            return this;
        }
        
        public FuelLogBuilder notas(@NonNull String notas) {
            data.put("notas", notas);
            return this;
        }
        
        public FuelLogBuilder local(@NonNull String local) {
            data.put("local", local);
            return this;
        }
        
        public FuelLogBuilder precoPorLitro(double precoPorLitro) {
            data.put("preco_por_litro", precoPorLitro);
            return this;
        }
        
        Map<String, Object> build() {
            return data;
        }
    }
}
