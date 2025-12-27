package com.veigest.sdk.services;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.veigest.sdk.VeiGestCallback;
import com.veigest.sdk.api.VeiGestApiClient;
import com.veigest.sdk.models.FuelLog;
import com.veigest.sdk.models.Maintenance;
import com.veigest.sdk.models.Vehicle;
import com.veigest.sdk.models.VehicleStats;

import java.util.List;
import java.util.Map;

/**
 * Serviço de veículos do SDK.
 * Gerencia CRUD de veículos e atribuição de condutores.
 */
public class VehicleService extends BaseService {
    
    public VehicleService(@NonNull VeiGestApiClient apiClient) {
        super(apiClient);
    }
    
    /**
     * Lista todos os veículos.
     * 
     * @param callback Callback com lista de veículos
     */
    public void list(@NonNull VeiGestCallback<List<Vehicle>> callback) {
        list(null, null, null, null, null, callback);
    }
    
    /**
     * Lista veículos com filtros.
     * 
     * @param companyId ID da empresa (opcional)
     * @param estado Estado do veículo: "ativo", "inativo", "manutencao" (opcional)
     * @param page Número da página (opcional)
     * @param limit Quantidade por página (opcional)
     * @param sort Campo de ordenação (opcional)
     * @param callback Callback com lista de veículos
     */
    public void list(@Nullable Integer companyId, @Nullable String estado,
                     @Nullable Integer page, @Nullable Integer limit, @Nullable String sort,
                     @NonNull VeiGestCallback<List<Vehicle>> callback) {
        executeCall(apiClient.getApi().getVehicles(companyId, estado, page, limit, sort), callback);
    }
    
    /**
     * Lista veículos ativos.
     */
    public void listActive(@NonNull VeiGestCallback<List<Vehicle>> callback) {
        list(null, "ativo", null, null, null, callback);
    }
    
    /**
     * Lista veículos em manutenção.
     */
    public void listInMaintenance(@NonNull VeiGestCallback<List<Vehicle>> callback) {
        list(null, "manutencao", null, null, null, callback);
    }
    
    /**
     * Obtém um veículo por ID.
     * 
     * @param id ID do veículo
     * @param callback Callback com o veículo
     */
    public void get(int id, @NonNull VeiGestCallback<Vehicle> callback) {
        executeCall(apiClient.getApi().getVehicle(id), callback);
    }
    
    /**
     * Cria um novo veículo.
     * 
     * @param matricula Matrícula do veículo
     * @param marca Marca do veículo
     * @param modelo Modelo do veículo
     * @param ano Ano do veículo
     * @param tipoCombustivel Tipo de combustível
     * @param callback Callback com o veículo criado
     */
    public void create(@NonNull String matricula, @NonNull String marca, @NonNull String modelo,
                       int ano, @Nullable String tipoCombustivel,
                       @NonNull VeiGestCallback<Vehicle> callback) {
        Map<String, Object> body = createBody();
        body.put("matricula", matricula);
        body.put("marca", marca);
        body.put("modelo", modelo);
        body.put("ano", ano);
        addIfNotNull(body, "tipo_combustivel", tipoCombustivel);
        
        executeCall(apiClient.getApi().createVehicle(body), callback);
    }
    
    /**
     * Cria um novo veículo com builder.
     * 
     * @param builder Builder com os dados do veículo
     * @param callback Callback com o veículo criado
     */
    public void create(@NonNull VehicleBuilder builder, @NonNull VeiGestCallback<Vehicle> callback) {
        executeCall(apiClient.getApi().createVehicle(builder.build()), callback);
    }
    
    /**
     * Atualiza um veículo completamente.
     * 
     * @param id ID do veículo
     * @param data Dados para atualização
     * @param callback Callback com o veículo atualizado
     */
    public void update(int id, @NonNull Map<String, Object> data,
                       @NonNull VeiGestCallback<Vehicle> callback) {
        executeCall(apiClient.getApi().updateVehicle(id, data), callback);
    }
    
    /**
     * Atualiza campos específicos de um veículo.
     * 
     * @param id ID do veículo
     * @param data Campos para atualização
     * @param callback Callback com o veículo atualizado
     */
    public void patch(int id, @NonNull Map<String, Object> data,
                      @NonNull VeiGestCallback<Vehicle> callback) {
        executeCall(apiClient.getApi().patchVehicle(id, data), callback);
    }
    
    /**
     * Atualiza a quilometragem de um veículo.
     * 
     * @param id ID do veículo
     * @param quilometragem Nova quilometragem
     * @param callback Callback com o veículo atualizado
     */
    public void updateMileage(int id, int quilometragem, @NonNull VeiGestCallback<Vehicle> callback) {
        Map<String, Object> body = createBody();
        body.put("quilometragem", quilometragem);
        patch(id, body, callback);
    }
    
    /**
     * Atualiza o estado de um veículo.
     * 
     * @param id ID do veículo
     * @param estado Novo estado: "ativo", "inativo", "manutencao"
     * @param callback Callback com o veículo atualizado
     */
    public void updateStatus(int id, @NonNull String estado, @NonNull VeiGestCallback<Vehicle> callback) {
        Map<String, Object> body = createBody();
        body.put("estado", estado);
        patch(id, body, callback);
    }
    
    /**
     * Elimina um veículo.
     * 
     * @param id ID do veículo
     * @param callback Callback com resultado
     */
    public void delete(int id, @NonNull VeiGestCallback<Void> callback) {
        executeCall(apiClient.getApi().deleteVehicle(id), callback);
    }
    
    /**
     * Atribui um condutor a um veículo.
     * 
     * @param vehicleId ID do veículo
     * @param driverId ID do condutor
     * @param callback Callback com resultado
     */
    public void assignDriver(int vehicleId, int driverId, @NonNull VeiGestCallback<Void> callback) {
        Map<String, Integer> body = new java.util.HashMap<>();
        body.put("condutor_id", driverId);
        executeCall(apiClient.getApi().assignDriver(vehicleId, body), callback);
    }
    
    /**
     * Remove o condutor de um veículo.
     * 
     * @param vehicleId ID do veículo
     * @param callback Callback com resultado
     */
    public void unassignDriver(int vehicleId, @NonNull VeiGestCallback<Void> callback) {
        executeCall(apiClient.getApi().unassignDriver(vehicleId), callback);
    }
    
    // ==================== NOVOS ENDPOINTS ====================
    
    /**
     * Lista as manutenções de um veículo.
     * 
     * @param vehicleId ID do veículo
     * @param callback Callback com lista de manutenções
     */
    public void getMaintenances(int vehicleId, @NonNull VeiGestCallback<List<Maintenance>> callback) {
        executeCall(apiClient.getApi().getVehicleMaintenances(vehicleId), callback);
    }
    
    /**
     * Lista os abastecimentos de um veículo.
     * 
     * @param vehicleId ID do veículo
     * @param callback Callback com lista de abastecimentos
     */
    public void getFuelLogs(int vehicleId, @NonNull VeiGestCallback<List<FuelLog>> callback) {
        executeCall(apiClient.getApi().getVehicleFuelLogs(vehicleId), callback);
    }
    
    /**
     * Obtém estatísticas de um veículo.
     * 
     * @param vehicleId ID do veículo
     * @param callback Callback com estatísticas
     */
    public void getStats(int vehicleId, @NonNull VeiGestCallback<VehicleStats> callback) {
        executeCall(apiClient.getApi().getVehicleStats(vehicleId), callback);
    }
    
    /**
     * Lista veículos por status.
     * 
     * @param status Status: "active", "inactive", "maintenance"
     * @param callback Callback com lista de veículos
     */
    public void listByStatus(@NonNull String status, @NonNull VeiGestCallback<List<Vehicle>> callback) {
        executeCall(apiClient.getApi().getVehiclesByStatus(status), callback);
    }
    
    /**
     * Builder para criar veículos.
     */
    public static class VehicleBuilder {
        private final Map<String, Object> data = new java.util.HashMap<>();
        
        public VehicleBuilder matricula(@NonNull String matricula) {
            data.put("matricula", matricula);
            return this;
        }
        
        public VehicleBuilder marca(@NonNull String marca) {
            data.put("marca", marca);
            return this;
        }
        
        public VehicleBuilder modelo(@NonNull String modelo) {
            data.put("modelo", modelo);
            return this;
        }
        
        public VehicleBuilder ano(int ano) {
            data.put("ano", ano);
            return this;
        }
        
        public VehicleBuilder tipoCombustivel(@NonNull String tipoCombustivel) {
            data.put("tipo_combustivel", tipoCombustivel);
            return this;
        }
        
        public VehicleBuilder quilometragem(int quilometragem) {
            data.put("quilometragem", quilometragem);
            return this;
        }
        
        public VehicleBuilder condutorId(int condutorId) {
            data.put("condutor_id", condutorId);
            return this;
        }
        
        public VehicleBuilder estado(@NonNull String estado) {
            data.put("estado", estado);
            return this;
        }
        
        public VehicleBuilder companyId(int companyId) {
            data.put("company_id", companyId);
            return this;
        }
        
        Map<String, Object> build() {
            return data;
        }
    }
}
