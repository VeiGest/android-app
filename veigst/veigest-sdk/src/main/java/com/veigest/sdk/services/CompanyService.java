package com.veigest.sdk.services;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.veigest.sdk.VeiGestCallback;
import com.veigest.sdk.api.VeiGestApiClient;
import com.veigest.sdk.models.Company;
import com.veigest.sdk.models.CompanyStats;
import com.veigest.sdk.models.User;
import com.veigest.sdk.models.Vehicle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Serviço de empresas do SDK.
 */
public class CompanyService extends BaseService {
    
    public CompanyService(@NonNull VeiGestApiClient apiClient) {
        super(apiClient);
    }
    
    /**
     * Lista todas as empresas.
     */
    public void list(@NonNull VeiGestCallback<List<Company>> callback) {
        list(null, null, null, null, callback);
    }
    
    /**
     * Lista empresas com filtros.
     */
    public void list(@Nullable Integer page, @Nullable Integer limit,
                     @Nullable String sort, @Nullable String filter,
                     @NonNull VeiGestCallback<List<Company>> callback) {
        executeCall(apiClient.getApi().getCompanies(page, limit, sort, filter), callback);
    }
    
    /**
     * Obtém uma empresa por ID.
     */
    public void get(int id, @NonNull VeiGestCallback<Company> callback) {
        executeCall(apiClient.getApi().getCompany(id), callback);
    }
    
    /**
     * Cria uma nova empresa.
     */
    public void create(@NonNull String nome, @NonNull String nif, @NonNull String email,
                       @Nullable String plano, @NonNull VeiGestCallback<Company> callback) {
        Map<String, Object> body = createBody();
        body.put("nome", nome);
        body.put("nif", nif);
        body.put("email", email);
        addIfNotNull(body, "plano", plano);
        
        executeCall(apiClient.getApi().createCompany(body), callback);
    }
    
    /**
     * Cria empresa com builder.
     */
    public void create(@NonNull CompanyBuilder builder, @NonNull VeiGestCallback<Company> callback) {
        executeCall(apiClient.getApi().createCompany(builder.build()), callback);
    }
    
    /**
     * Atualiza uma empresa.
     */
    public void update(int id, @NonNull Map<String, Object> data,
                       @NonNull VeiGestCallback<Company> callback) {
        executeCall(apiClient.getApi().updateCompany(id, data), callback);
    }
    
    /**
     * Atualiza campos específicos de uma empresa.
     */
    public void patch(int id, @NonNull Map<String, Object> data,
                      @NonNull VeiGestCallback<Company> callback) {
        executeCall(apiClient.getApi().patchCompany(id, data), callback);
    }
    
    /**
     * Atualiza o estado de uma empresa.
     */
    public void updateStatus(int id, @NonNull String estado, @NonNull VeiGestCallback<Company> callback) {
        Map<String, Object> body = createBody();
        body.put("estado", estado);
        patch(id, body, callback);
    }
    
    /**
     * Elimina uma empresa.
     */
    public void delete(int id, @NonNull VeiGestCallback<Void> callback) {
        executeCall(apiClient.getApi().deleteCompany(id), callback);
    }
    
    // ==================== NOVOS ENDPOINTS ====================
    
    /**
     * Lista os veículos de uma empresa.
     * 
     * @param companyId ID da empresa
     * @param callback Callback com lista de veículos
     */
    public void getVehicles(int companyId, @NonNull VeiGestCallback<List<Vehicle>> callback) {
        executeCall(apiClient.getApi().getCompanyVehicles(companyId), callback);
    }
    
    /**
     * Lista os utilizadores de uma empresa.
     * 
     * @param companyId ID da empresa
     * @param callback Callback com lista de utilizadores
     */
    public void getUsers(int companyId, @NonNull VeiGestCallback<List<User>> callback) {
        executeCall(apiClient.getApi().getCompanyUsers(companyId), callback);
    }
    
    /**
     * Obtém estatísticas de uma empresa.
     * 
     * @param companyId ID da empresa
     * @param callback Callback com estatísticas
     */
    public void getStats(int companyId, @NonNull VeiGestCallback<CompanyStats> callback) {
        executeCall(apiClient.getApi().getCompanyStats(companyId), callback);
    }
    
    /**
     * Builder para criar empresas.
     */
    public static class CompanyBuilder {
        private final Map<String, Object> data = new HashMap<>();
        
        public CompanyBuilder nome(@NonNull String nome) {
            data.put("nome", nome);
            return this;
        }
        
        public CompanyBuilder nif(@NonNull String nif) {
            data.put("nif", nif);
            return this;
        }
        
        public CompanyBuilder email(@NonNull String email) {
            data.put("email", email);
            return this;
        }
        
        public CompanyBuilder telefone(@NonNull String telefone) {
            data.put("telefone", telefone);
            return this;
        }
        
        public CompanyBuilder morada(@NonNull String morada) {
            data.put("morada", morada);
            return this;
        }
        
        public CompanyBuilder plano(@NonNull String plano) {
            data.put("plano", plano);
            return this;
        }
        
        Map<String, Object> build() {
            return data;
        }
    }
}
