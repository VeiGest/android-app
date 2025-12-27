package com.veigest.sdk.services;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.veigest.sdk.VeiGestCallback;
import com.veigest.sdk.api.VeiGestApiClient;
import com.veigest.sdk.models.Maintenance;
import com.veigest.sdk.models.MaintenanceReport;
import com.veigest.sdk.models.ReportStats;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Serviço de manutenções do SDK.
 */
public class MaintenanceService extends BaseService {
    
    public MaintenanceService(@NonNull VeiGestApiClient apiClient) {
        super(apiClient);
    }
    
    /**
     * Lista todas as manutenções.
     */
    public void list(@NonNull VeiGestCallback<List<Maintenance>> callback) {
        list(null, null, null, null, null, callback);
    }
    
    /**
     * Lista manutenções com filtros.
     */
    public void list(@Nullable Integer vehicleId, @Nullable String dataInicio, @Nullable String dataFim,
                     @Nullable Integer page, @Nullable Integer limit,
                     @NonNull VeiGestCallback<List<Maintenance>> callback) {
        executeCall(apiClient.getApi().getMaintenances(vehicleId, dataInicio, dataFim, page, limit), callback);
    }
    
    /**
     * Lista manutenções de um veículo específico.
     */
    public void listByVehicle(int vehicleId, @NonNull VeiGestCallback<List<Maintenance>> callback) {
        list(vehicleId, null, null, null, null, callback);
    }
    
    /**
     * Obtém uma manutenção por ID.
     */
    public void get(int id, @NonNull VeiGestCallback<Maintenance> callback) {
        executeCall(apiClient.getApi().getMaintenance(id), callback);
    }
    
    /**
     * Cria uma nova manutenção.
     */
    public void create(int vehicleId, @NonNull String tipo, @NonNull String data, double custo,
                       @Nullable String descricao, @Nullable String oficina,
                       @NonNull VeiGestCallback<Maintenance> callback) {
        Map<String, Object> body = createBody();
        body.put("vehicle_id", vehicleId);
        body.put("tipo", tipo);
        body.put("data", data);
        body.put("custo", custo);
        addIfNotNull(body, "descricao", descricao);
        addIfNotNull(body, "oficina", oficina);
        
        executeCall(apiClient.getApi().createMaintenance(body), callback);
    }
    
    /**
     * Cria manutenção com builder.
     */
    public void create(@NonNull MaintenanceBuilder builder, @NonNull VeiGestCallback<Maintenance> callback) {
        executeCall(apiClient.getApi().createMaintenance(builder.build()), callback);
    }
    
    /**
     * Atualiza uma manutenção.
     */
    public void update(int id, @NonNull Map<String, Object> data,
                       @NonNull VeiGestCallback<Maintenance> callback) {
        executeCall(apiClient.getApi().updateMaintenance(id, data), callback);
    }
    
    /**
     * Atualiza campos específicos de uma manutenção.
     */
    public void patch(int id, @NonNull Map<String, Object> data,
                      @NonNull VeiGestCallback<Maintenance> callback) {
        executeCall(apiClient.getApi().patchMaintenance(id, data), callback);
    }
    
    /**
     * Elimina uma manutenção.
     */
    public void delete(int id, @NonNull VeiGestCallback<Void> callback) {
        executeCall(apiClient.getApi().deleteMaintenance(id), callback);
    }
    
    // ==================== NOVOS ENDPOINTS ====================
    
    /**
     * Lista manutenções de um veículo específico (via endpoint dedicado).
     * 
     * @param vehicleId ID do veículo
     * @param callback Callback com lista de manutenções
     */
    public void getByVehicle(int vehicleId, @NonNull VeiGestCallback<List<Maintenance>> callback) {
        executeCall(apiClient.getApi().getMaintenancesByVehicle(vehicleId), callback);
    }
    
    /**
     * Lista manutenções por estado.
     * 
     * @param estado Estado: "agendada", "em_progresso", "concluida", "cancelada"
     * @param callback Callback com lista de manutenções
     */
    public void getByStatus(@NonNull String estado, @NonNull VeiGestCallback<List<Maintenance>> callback) {
        executeCall(apiClient.getApi().getMaintenancesByStatus(estado), callback);
    }
    
    /**
     * Lista manutenções agendadas.
     */
    public void listScheduled(@NonNull VeiGestCallback<List<Maintenance>> callback) {
        getByStatus("agendada", callback);
    }
    
    /**
     * Lista manutenções concluídas.
     */
    public void listCompleted(@NonNull VeiGestCallback<List<Maintenance>> callback) {
        getByStatus("concluida", callback);
    }
    
    /**
     * Lista manutenções em progresso.
     */
    public void listInProgress(@NonNull VeiGestCallback<List<Maintenance>> callback) {
        getByStatus("em_progresso", callback);
    }
    
    /**
     * Agenda uma manutenção.
     * 
     * @param id ID da manutenção
     * @param scheduledDate Data agendada (formato: YYYY-MM-DD)
     * @param priority Prioridade: "alta", "media", "baixa"
     * @param assignedTechnician Técnico responsável
     * @param callback Callback com a manutenção atualizada
     */
    public void schedule(int id, @NonNull String scheduledDate, @Nullable String priority,
                        @Nullable String assignedTechnician, @NonNull VeiGestCallback<Maintenance> callback) {
        Map<String, Object> body = createBody();
        body.put("scheduled_date", scheduledDate);
        addIfNotNull(body, "priority", priority);
        addIfNotNull(body, "assigned_technician", assignedTechnician);
        executeCall(apiClient.getApi().scheduleMaintenance(id, body), callback);
    }
    
    /**
     * Agenda uma manutenção com builder.
     */
    public void schedule(int id, @NonNull ScheduleBuilder builder, @NonNull VeiGestCallback<Maintenance> callback) {
        executeCall(apiClient.getApi().scheduleMaintenance(id, builder.build()), callback);
    }
    
    /**
     * Obtém relatório mensal de manutenções.
     * 
     * @param month Mês (1-12), null para mês atual
     * @param year Ano, null para ano atual
     * @param callback Callback com relatório
     */
    public void getMonthlyReport(@Nullable Integer month, @Nullable Integer year,
                                 @NonNull VeiGestCallback<MaintenanceReport> callback) {
        executeCall(apiClient.getApi().getMaintenanceMonthlyReport(month, year), callback);
    }
    
    /**
     * Obtém relatório de custos de manutenções.
     * 
     * @param vehicleId ID do veículo (null para todos)
     * @param startDate Data início (formato: YYYY-MM-DD)
     * @param endDate Data fim (formato: YYYY-MM-DD)
     * @param callback Callback com relatório
     */
    public void getCostsReport(@Nullable Integer vehicleId, @Nullable String startDate,
                               @Nullable String endDate, @NonNull VeiGestCallback<MaintenanceReport> callback) {
        executeCall(apiClient.getApi().getMaintenanceCostsReport(vehicleId, startDate, endDate), callback);
    }
    
    /**
     * Obtém estatísticas gerais de manutenções.
     * 
     * @param callback Callback com estatísticas
     */
    public void getStats(@NonNull VeiGestCallback<ReportStats> callback) {
        executeCall(apiClient.getApi().getMaintenanceStats(), callback);
    }
    
    /**
     * Builder para agendar manutenções.
    
    /**
     * Builder para criar manutenções.
     */
    public static class MaintenanceBuilder {
        private final Map<String, Object> data = new HashMap<>();
        
        public MaintenanceBuilder vehicleId(int vehicleId) {
            data.put("vehicle_id", vehicleId);
            return this;
        }
        
        public MaintenanceBuilder companyId(int companyId) {
            data.put("company_id", companyId);
            return this;
        }
        
        public MaintenanceBuilder tipo(@NonNull String tipo) {
            data.put("tipo", tipo);
            return this;
        }
        
        public MaintenanceBuilder descricao(@NonNull String descricao) {
            data.put("descricao", descricao);
            return this;
        }
        
        public MaintenanceBuilder data(@NonNull String data) {
            this.data.put("data", data);
            return this;
        }
        
        public MaintenanceBuilder custo(double custo) {
            data.put("custo", custo);
            return this;
        }
        
        public MaintenanceBuilder kmRegistro(int kmRegistro) {
            data.put("km_registro", kmRegistro);
            return this;
        }
        
        public MaintenanceBuilder proximaData(@NonNull String proximaData) {
            data.put("proxima_data", proximaData);
            return this;
        }
        
        public MaintenanceBuilder oficina(@NonNull String oficina) {
            data.put("oficina", oficina);
            return this;
        }
        
        public MaintenanceBuilder estado(@NonNull String estado) {
            data.put("estado", estado);
            return this;
        }
        
        Map<String, Object> build() {
            return data;
        }
    }
    
    /**
     * Builder para agendar manutenções.
     */
    public static class ScheduleBuilder {
        private final Map<String, Object> data = new HashMap<>();
        
        public ScheduleBuilder scheduledDate(@NonNull String scheduledDate) {
            data.put("scheduled_date", scheduledDate);
            return this;
        }
        
        public ScheduleBuilder priority(@NonNull String priority) {
            data.put("priority", priority);
            return this;
        }
        
        public ScheduleBuilder assignedTechnician(@NonNull String assignedTechnician) {
            data.put("assigned_technician", assignedTechnician);
            return this;
        }
        
        public ScheduleBuilder notes(@NonNull String notes) {
            data.put("notes", notes);
            return this;
        }
        
        Map<String, Object> build() {
            return data;
        }
    }
}
