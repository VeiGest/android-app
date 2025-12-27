package com.veigest.sdk.services;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.veigest.sdk.VeiGestCallback;
import com.veigest.sdk.api.VeiGestApiClient;
import com.veigest.sdk.models.ReportStats;

import java.util.List;

/**
 * Serviço de relatórios e estatísticas do SDK.
 */
public class ReportService extends BaseService {
    
    public ReportService(@NonNull VeiGestApiClient apiClient) {
        super(apiClient);
    }
    
    /**
     * Obtém estatísticas gerais da empresa.
     */
    public void getCompanyStats(@NonNull VeiGestCallback<ReportStats> callback) {
        getCompanyStats(null, callback);
    }
    
    /**
     * Obtém estatísticas de uma empresa específica.
     */
    public void getCompanyStats(@Nullable Integer companyId, @NonNull VeiGestCallback<ReportStats> callback) {
        executeCall(apiClient.getApi().getCompanyStats(companyId), callback);
    }
    
    /**
     * Obtém custos de todos os veículos.
     */
    public void getVehicleCosts(@NonNull VeiGestCallback<List<ReportStats>> callback) {
        getVehicleCosts(null, null, null, callback);
    }
    
    /**
     * Obtém custos de um veículo específico.
     */
    public void getVehicleCosts(int vehicleId, @NonNull VeiGestCallback<List<ReportStats>> callback) {
        getVehicleCosts(vehicleId, null, null, callback);
    }
    
    /**
     * Obtém custos de veículos com filtros.
     */
    public void getVehicleCosts(@Nullable Integer vehicleId, @Nullable String dataInicio,
                                @Nullable String dataFim, @NonNull VeiGestCallback<List<ReportStats>> callback) {
        executeCall(apiClient.getApi().getVehicleCosts(vehicleId, dataInicio, dataFim), callback);
    }
    
    /**
     * Obtém estatísticas de consumo de combustível.
     */
    public void getFuelConsumption(@NonNull VeiGestCallback<ReportStats> callback) {
        getFuelConsumption(null, null, callback);
    }
    
    /**
     * Obtém estatísticas de consumo de combustível de um veículo.
     */
    public void getFuelConsumption(int vehicleId, @NonNull VeiGestCallback<ReportStats> callback) {
        getFuelConsumption(vehicleId, null, callback);
    }
    
    /**
     * Obtém estatísticas de consumo de combustível com filtros.
     */
    public void getFuelConsumption(@Nullable Integer vehicleId, @Nullable String periodo,
                                   @NonNull VeiGestCallback<ReportStats> callback) {
        executeCall(apiClient.getApi().getFuelConsumption(vehicleId, periodo), callback);
    }
    
    /**
     * Obtém cronograma de manutenções.
     */
    public void getMaintenanceSchedule(@NonNull VeiGestCallback<List<ReportStats>> callback) {
        getMaintenanceSchedule(null, null, callback);
    }
    
    /**
     * Obtém cronograma de manutenções com filtros.
     */
    public void getMaintenanceSchedule(@Nullable String dataInicio, @Nullable String dataFim,
                                       @NonNull VeiGestCallback<List<ReportStats>> callback) {
        executeCall(apiClient.getApi().getMaintenanceSchedule(dataInicio, dataFim), callback);
    }
    
    /**
     * Obtém performance de todos os condutores.
     */
    public void getDriverPerformance(@NonNull VeiGestCallback<ReportStats> callback) {
        getDriverPerformance(null, null, callback);
    }
    
    /**
     * Obtém performance de um condutor específico.
     */
    public void getDriverPerformance(int driverId, @NonNull VeiGestCallback<ReportStats> callback) {
        getDriverPerformance(driverId, null, callback);
    }
    
    /**
     * Obtém performance de um condutor com filtros.
     */
    public void getDriverPerformance(@Nullable Integer driverId, @Nullable String periodo,
                                     @NonNull VeiGestCallback<ReportStats> callback) {
        executeCall(apiClient.getApi().getDriverPerformance(driverId, periodo), callback);
    }
}
