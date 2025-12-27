package com.veigest.sdk.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Modelo de Ticket/Bilhete.
 */
public class Ticket {
    
    @SerializedName("id")
    private int id;
    
    @SerializedName("company_id")
    private int companyId;
    
    @SerializedName("vehicle_id")
    private Integer vehicleId;
    
    @SerializedName("driver_id")
    private Integer driverId;
    
    @SerializedName("route_id")
    private Integer routeId;
    
    @SerializedName("titulo")
    private String titulo;
    
    @SerializedName("title")
    private String title;
    
    @SerializedName("descricao")
    private String descricao;
    
    @SerializedName("description")
    private String description;
    
    @SerializedName("tipo")
    private String tipo;
    
    @SerializedName("type")
    private String type;
    
    @SerializedName("prioridade")
    private String prioridade;
    
    @SerializedName("priority")
    private String priority;
    
    @SerializedName("estado")
    private String estado;
    
    @SerializedName("status")
    private String status;
    
    @SerializedName("data_abertura")
    private String dataAbertura;
    
    @SerializedName("created_at")
    private String createdAt;
    
    @SerializedName("data_conclusao")
    private String dataConclusao;
    
    @SerializedName("completed_at")
    private String completedAt;
    
    @SerializedName("data_cancelamento")
    private String dataCancelamento;
    
    @SerializedName("cancelled_at")
    private String cancelledAt;
    
    @SerializedName("observacoes")
    private String observacoes;
    
    @SerializedName("notes")
    private String notes;
    
    // Relacionamentos
    @SerializedName("vehicle")
    private Vehicle vehicle;
    
    @SerializedName("driver")
    private User driver;
    
    // Getters
    public int getId() {
        return id;
    }
    
    public int getCompanyId() {
        return companyId;
    }
    
    @Nullable
    public Integer getVehicleId() {
        return vehicleId;
    }
    
    @Nullable
    public Integer getDriverId() {
        return driverId;
    }
    
    @Nullable
    public Integer getRouteId() {
        return routeId;
    }
    
    public String getTitulo() {
        return titulo != null ? titulo : title;
    }
    
    public String getDescricao() {
        return descricao != null ? descricao : description;
    }
    
    public String getTipo() {
        return tipo != null ? tipo : type;
    }
    
    public String getPrioridade() {
        return prioridade != null ? prioridade : priority;
    }
    
    public String getEstado() {
        return estado != null ? estado : status;
    }
    
    public String getDataAbertura() {
        return dataAbertura != null ? dataAbertura : createdAt;
    }
    
    @Nullable
    public String getDataConclusao() {
        return dataConclusao != null ? dataConclusao : completedAt;
    }
    
    @Nullable
    public String getDataCancelamento() {
        return dataCancelamento != null ? dataCancelamento : cancelledAt;
    }
    
    @Nullable
    public String getObservacoes() {
        return observacoes != null ? observacoes : notes;
    }
    
    @Nullable
    public Vehicle getVehicle() {
        return vehicle;
    }
    
    @Nullable
    public User getDriver() {
        return driver;
    }
    
    // Estado helpers
    public boolean isPending() {
        String s = getEstado();
        return s != null && (s.equalsIgnoreCase("pendente") || s.equalsIgnoreCase("pending") || s.equalsIgnoreCase("aberto") || s.equalsIgnoreCase("open"));
    }
    
    public boolean isCompleted() {
        String s = getEstado();
        return s != null && (s.equalsIgnoreCase("concluido") || s.equalsIgnoreCase("completed") || s.equalsIgnoreCase("fechado") || s.equalsIgnoreCase("closed"));
    }
    
    public boolean isCancelled() {
        String s = getEstado();
        return s != null && (s.equalsIgnoreCase("cancelado") || s.equalsIgnoreCase("cancelled"));
    }
    
    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", titulo='" + getTitulo() + '\'' +
                ", tipo='" + getTipo() + '\'' +
                ", estado='" + getEstado() + '\'' +
                ", prioridade='" + getPrioridade() + '\'' +
                '}';
    }
}
