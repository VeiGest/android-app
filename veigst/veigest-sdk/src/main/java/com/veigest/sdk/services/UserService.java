package com.veigest.sdk.services;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.veigest.sdk.VeiGestCallback;
import com.veigest.sdk.api.VeiGestApiClient;
import com.veigest.sdk.models.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Serviço de utilizadores do SDK.
 * Gerencia CRUD de utilizadores.
 */
public class UserService extends BaseService {
    
    public UserService(@NonNull VeiGestApiClient apiClient) {
        super(apiClient);
    }
    
    /**
     * Lista todos os utilizadores.
     * 
     * @param callback Callback com lista de utilizadores
     */
    public void list(@NonNull VeiGestCallback<List<User>> callback) {
        list(null, null, null, null, null, callback);
    }
    
    /**
     * Lista utilizadores com filtros.
     * 
     * @param companyId ID da empresa (opcional)
     * @param page Número da página (opcional)
     * @param limit Quantidade por página (opcional)
     * @param sort Campo de ordenação (opcional)
     * @param filter Filtro de pesquisa (opcional)
     * @param callback Callback com lista de utilizadores
     */
    public void list(@Nullable Integer companyId, @Nullable Integer page, @Nullable Integer limit,
                     @Nullable String sort, @Nullable String filter,
                     @NonNull VeiGestCallback<List<User>> callback) {
        executeCall(apiClient.getApi().getUsers(companyId, page, limit, sort, filter), callback);
    }
    
    /**
     * Obtém um utilizador por ID.
     * 
     * @param id ID do utilizador
     * @param callback Callback com o utilizador
     */
    public void get(int id, @NonNull VeiGestCallback<User> callback) {
        executeCall(apiClient.getApi().getUser(id), callback);
    }
    
    /**
     * Cria um novo utilizador.
     * 
     * @param nome Nome do utilizador
     * @param email Email do utilizador
     * @param password Password do utilizador
     * @param callback Callback com o utilizador criado
     */
    public void create(@NonNull String nome, @NonNull String email, @NonNull String password,
                       @NonNull VeiGestCallback<User> callback) {
        Map<String, Object> body = createBody();
        body.put("nome", nome);
        body.put("email", email);
        body.put("senha", password);
        
        executeCall(apiClient.getApi().createUser(body), callback);
    }
    
    /**
     * Cria um novo utilizador com builder.
     * 
     * @param builder Builder com os dados do utilizador
     * @param callback Callback com o utilizador criado
     */
    public void create(@NonNull UserBuilder builder, @NonNull VeiGestCallback<User> callback) {
        executeCall(apiClient.getApi().createUser(builder.build()), callback);
    }
    
    /**
     * Atualiza um utilizador completamente.
     * 
     * @param id ID do utilizador
     * @param data Dados para atualização
     * @param callback Callback com o utilizador atualizado
     */
    public void update(int id, @NonNull Map<String, Object> data,
                       @NonNull VeiGestCallback<User> callback) {
        executeCall(apiClient.getApi().updateUser(id, data), callback);
    }
    
    /**
     * Atualiza campos específicos de um utilizador.
     * 
     * @param id ID do utilizador
     * @param data Campos para atualização
     * @param callback Callback com o utilizador atualizado
     */
    public void patch(int id, @NonNull Map<String, Object> data,
                      @NonNull VeiGestCallback<User> callback) {
        executeCall(apiClient.getApi().patchUser(id, data), callback);
    }
    
    /**
     * Atualiza o estado de um utilizador.
     * 
     * @param id ID do utilizador
     * @param estado Novo estado: "ativo", "inativo"
     * @param callback Callback com o utilizador atualizado
     */
    public void updateStatus(int id, @NonNull String estado, @NonNull VeiGestCallback<User> callback) {
        Map<String, Object> body = createBody();
        body.put("estado", estado);
        patch(id, body, callback);
    }
    
    /**
     * Elimina um utilizador.
     * 
     * @param id ID do utilizador
     * @param callback Callback com resultado
     */
    public void delete(int id, @NonNull VeiGestCallback<Void> callback) {
        executeCall(apiClient.getApi().deleteUser(id), callback);
    }
    
    /**
     * Reset de password de um utilizador.
     * 
     * @param id ID do utilizador
     * @param newPassword Nova password
     * @param callback Callback com resultado
     */
    public void resetPassword(int id, @NonNull String newPassword, @NonNull VeiGestCallback<Void> callback) {
        Map<String, String> body = new HashMap<>();
        body.put("new_password", newPassword);
        executeCall(apiClient.getApi().resetPassword(id, body), callback);
    }
    
    // ==================== NOVOS ENDPOINTS ====================
    
    /**
     * Lista todos os condutores.
     * 
     * @param callback Callback com lista de condutores
     */
    public void listDrivers(@NonNull VeiGestCallback<List<User>> callback) {
        executeCall(apiClient.getApi().getDrivers(), callback);
    }
    
    /**
     * Obtém o perfil do utilizador atual.
     * 
     * @param callback Callback com o perfil
     */
    public void getProfile(@NonNull VeiGestCallback<User> callback) {
        executeCall(apiClient.getApi().getProfile(), callback);
    }
    
    /**
     * Lista utilizadores de uma empresa específica.
     * 
     * @param companyId ID da empresa
     * @param callback Callback com lista de utilizadores
     */
    public void listByCompany(int companyId, @NonNull VeiGestCallback<List<User>> callback) {
        executeCall(apiClient.getApi().getUsersByCompany(companyId), callback);
    }
    
    /**
     * Builder para criar utilizadores.
     */
    public static class UserBuilder {
        private final Map<String, Object> data = new HashMap<>();
        
        public UserBuilder nome(@NonNull String nome) {
            data.put("nome", nome);
            return this;
        }
        
        public UserBuilder email(@NonNull String email) {
            data.put("email", email);
            return this;
        }
        
        public UserBuilder password(@NonNull String password) {
            data.put("senha", password);
            return this;
        }
        
        public UserBuilder telefone(@NonNull String telefone) {
            data.put("telefone", telefone);
            return this;
        }
        
        public UserBuilder numeroCarta(@NonNull String numeroCarta) {
            data.put("numero_carta", numeroCarta);
            return this;
        }
        
        public UserBuilder validadeCarta(@NonNull String validadeCarta) {
            data.put("validade_carta", validadeCarta);
            return this;
        }
        
        public UserBuilder tipo(@NonNull String tipo) {
            data.put("tipo", tipo);
            return this;
        }
        
        public UserBuilder companyId(int companyId) {
            data.put("company_id", companyId);
            return this;
        }
        
        Map<String, Object> build() {
            return data;
        }
    }
}
