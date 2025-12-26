package com.veigest.sdk.services;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.veigest.sdk.VeiGestCallback;
import com.veigest.sdk.api.VeiGestApiClient;
import com.veigest.sdk.models.Ticket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Serviço de tickets/bilhetes do SDK.
 */
public class TicketService extends BaseService {
    
    public TicketService(@NonNull VeiGestApiClient apiClient) {
        super(apiClient);
    }
    
    /**
     * Lista todos os tickets.
     * 
     * @param callback Callback com lista de tickets
     */
    public void list(@NonNull VeiGestCallback<List<Ticket>> callback) {
        list(null, null, null, null, null, null, callback);
    }
    
    /**
     * Lista tickets com filtros.
     * 
     * @param status Estado: "aberto", "pendente", "concluido", "cancelado"
     * @param tipo Tipo de ticket
     * @param prioridade Prioridade: "alta", "media", "baixa"
     * @param vehicleId ID do veículo
     * @param page Página
     * @param limit Limite por página
     * @param callback Callback com lista de tickets
     */
    public void list(@Nullable String status, @Nullable String tipo, @Nullable String prioridade,
                     @Nullable Integer vehicleId, @Nullable Integer page, @Nullable Integer limit,
                     @NonNull VeiGestCallback<List<Ticket>> callback) {
        executeCall(apiClient.getApi().getTickets(status, tipo, prioridade, vehicleId, page, limit), callback);
    }
    
    /**
     * Lista tickets pendentes.
     */
    public void listPending(@NonNull VeiGestCallback<List<Ticket>> callback) {
        list("pendente", null, null, null, null, null, callback);
    }
    
    /**
     * Lista tickets abertos.
     */
    public void listOpen(@NonNull VeiGestCallback<List<Ticket>> callback) {
        list("aberto", null, null, null, null, null, callback);
    }
    
    /**
     * Lista tickets de alta prioridade.
     */
    public void listHighPriority(@NonNull VeiGestCallback<List<Ticket>> callback) {
        list(null, null, "alta", null, null, null, callback);
    }
    
    /**
     * Lista tickets de um veículo.
     * 
     * @param vehicleId ID do veículo
     * @param callback Callback com lista de tickets
     */
    public void listByVehicle(int vehicleId, @NonNull VeiGestCallback<List<Ticket>> callback) {
        list(null, null, null, vehicleId, null, null, callback);
    }
    
    /**
     * Obtém um ticket por ID.
     * 
     * @param id ID do ticket
     * @param callback Callback com o ticket
     */
    public void get(int id, @NonNull VeiGestCallback<Ticket> callback) {
        executeCall(apiClient.getApi().getTicket(id), callback);
    }
    
    /**
     * Cria um novo ticket.
     * 
     * @param titulo Título do ticket
     * @param descricao Descrição
     * @param tipo Tipo
     * @param prioridade Prioridade: "alta", "media", "baixa"
     * @param callback Callback com o ticket criado
     */
    public void create(@NonNull String titulo, @NonNull String descricao,
                       @NonNull String tipo, @Nullable String prioridade,
                       @NonNull VeiGestCallback<Ticket> callback) {
        Map<String, Object> body = createBody();
        body.put("titulo", titulo);
        body.put("descricao", descricao);
        body.put("tipo", tipo);
        addIfNotNull(body, "prioridade", prioridade);
        
        executeCall(apiClient.getApi().createTicket(body), callback);
    }
    
    /**
     * Cria ticket com builder.
     */
    public void create(@NonNull TicketBuilder builder, @NonNull VeiGestCallback<Ticket> callback) {
        executeCall(apiClient.getApi().createTicket(builder.build()), callback);
    }
    
    /**
     * Atualiza um ticket.
     * 
     * @param id ID do ticket
     * @param data Dados para atualização
     * @param callback Callback com o ticket atualizado
     */
    public void update(int id, @NonNull Map<String, Object> data,
                       @NonNull VeiGestCallback<Ticket> callback) {
        executeCall(apiClient.getApi().updateTicket(id, data), callback);
    }
    
    /**
     * Cancela um ticket.
     * 
     * @param id ID do ticket
     * @param motivo Motivo do cancelamento
     * @param callback Callback com o ticket cancelado
     */
    public void cancel(int id, @Nullable String motivo, @NonNull VeiGestCallback<Ticket> callback) {
        Map<String, String> body = new HashMap<>();
        if (motivo != null) {
            body.put("motivo", motivo);
        }
        executeCall(apiClient.getApi().cancelTicket(id, body), callback);
    }
    
    /**
     * Cancela um ticket sem motivo.
     */
    public void cancel(int id, @NonNull VeiGestCallback<Ticket> callback) {
        cancel(id, null, callback);
    }
    
    /**
     * Marca um ticket como completo.
     * 
     * @param id ID do ticket
     * @param observacoes Observações de conclusão
     * @param callback Callback com o ticket completo
     */
    public void complete(int id, @Nullable String observacoes, @NonNull VeiGestCallback<Ticket> callback) {
        Map<String, String> body = new HashMap<>();
        if (observacoes != null) {
            body.put("observacoes", observacoes);
        }
        executeCall(apiClient.getApi().completeTicket(id, body), callback);
    }
    
    /**
     * Marca um ticket como completo sem observações.
     */
    public void complete(int id, @NonNull VeiGestCallback<Ticket> callback) {
        complete(id, null, callback);
    }
    
    /**
     * Elimina um ticket.
     * 
     * @param id ID do ticket
     * @param callback Callback com resultado
     */
    public void delete(int id, @NonNull VeiGestCallback<Void> callback) {
        executeCall(apiClient.getApi().deleteTicket(id), callback);
    }
    
    /**
     * Builder para criar tickets.
     */
    public static class TicketBuilder {
        private final Map<String, Object> data = new HashMap<>();
        
        public TicketBuilder titulo(@NonNull String titulo) {
            data.put("titulo", titulo);
            return this;
        }
        
        public TicketBuilder descricao(@NonNull String descricao) {
            data.put("descricao", descricao);
            return this;
        }
        
        public TicketBuilder tipo(@NonNull String tipo) {
            data.put("tipo", tipo);
            return this;
        }
        
        public TicketBuilder prioridade(@NonNull String prioridade) {
            data.put("prioridade", prioridade);
            return this;
        }
        
        public TicketBuilder vehicleId(int vehicleId) {
            data.put("vehicle_id", vehicleId);
            return this;
        }
        
        public TicketBuilder driverId(int driverId) {
            data.put("driver_id", driverId);
            return this;
        }
        
        public TicketBuilder routeId(int routeId) {
            data.put("route_id", routeId);
            return this;
        }
        
        public TicketBuilder observacoes(@NonNull String observacoes) {
            data.put("observacoes", observacoes);
            return this;
        }
        
        Map<String, Object> build() {
            return data;
        }
    }
}
