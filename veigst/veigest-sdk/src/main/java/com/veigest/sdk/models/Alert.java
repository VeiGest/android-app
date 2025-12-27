package com.veigest.sdk.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Modelo de alerta do sistema VeiGest.
 */
public class Alert {
    
    @SerializedName("id")
    private int id;
    
    @SerializedName("company_id")
    private int companyId;
    
    @SerializedName("tipo")
    private String tipo;
    
    @SerializedName("type")
    private String type;
    
    @SerializedName("titulo")
    private String titulo;
    
    @SerializedName("title")
    private String title;
    
    @SerializedName("descricao")
    private String descricao;
    
    @SerializedName("description")
    private String description;
    
    @SerializedName("prioridade")
    private String prioridade;
    
    @SerializedName("priority")
    private String priority;
    
    @SerializedName("status")
    private String status;
    
    @SerializedName("detalhes")
    private Map<String, Object> detalhes;
    
    @SerializedName("details")
    private Map<String, Object> details;
    
    @SerializedName("resolvido_em")
    private String resolvidoEm;
    
    @SerializedName("resolved_at")
    private String resolvedAt;
    
    @SerializedName("created_at")
    private String createdAt;
    
    @SerializedName("updated_at")
    private String updatedAt;
    
    // Getters
    public int getId() {
        return id;
    }
    
    public int getCompanyId() {
        return companyId;
    }
    
    @Nullable
    public String getTipo() {
        return tipo != null ? tipo : type;
    }
    
    @Nullable
    public String getTitulo() {
        return titulo != null ? titulo : title;
    }
    
    @Nullable
    public String getDescricao() {
        return descricao != null ? descricao : description;
    }
    
    @Nullable
    public String getPrioridade() {
        return prioridade != null ? prioridade : priority;
    }
    
    @Nullable
    public String getStatus() {
        return status;
    }
    
    @Nullable
    public Map<String, Object> getDetalhes() {
        return detalhes != null ? detalhes : details;
    }
    
    @Nullable
    public String getResolvidoEm() {
        return resolvidoEm != null ? resolvidoEm : resolvedAt;
    }
    
    @Nullable
    public String getCreatedAt() {
        return createdAt;
    }
    
    @Nullable
    public String getUpdatedAt() {
        return updatedAt;
    }
    
    /**
     * Verifica se o alerta está ativo.
     */
    public boolean isActive() {
        return "ativo".equalsIgnoreCase(status) || "active".equalsIgnoreCase(status);
    }
    
    /**
     * Verifica se o alerta foi resolvido.
     */
    public boolean isResolved() {
        return "resolvido".equalsIgnoreCase(status) || "resolved".equalsIgnoreCase(status);
    }
    
    /**
     * Verifica se o alerta foi ignorado.
     */
    public boolean isIgnored() {
        return "ignorado".equalsIgnoreCase(status) || "ignored".equalsIgnoreCase(status);
    }
    
    /**
     * Verifica se é um alerta de alta prioridade.
     */
    public boolean isHighPriority() {
        String p = getPrioridade();
        return "alta".equalsIgnoreCase(p) || "high".equalsIgnoreCase(p);
    }
    
    @Override
    public String toString() {
        return "Alert{" +
                "id=" + id +
                ", titulo='" + getTitulo() + '\'' +
                ", prioridade='" + getPrioridade() + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
