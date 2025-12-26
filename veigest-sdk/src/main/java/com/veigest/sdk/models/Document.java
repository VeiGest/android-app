package com.veigest.sdk.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Modelo de documento do sistema VeiGest.
 */
public class Document {
    
    @SerializedName("id")
    private int id;
    
    @SerializedName("company_id")
    private int companyId;
    
    @SerializedName("file_id")
    private Integer fileId;
    
    @SerializedName("vehicle_id")
    private Integer vehicleId;
    
    @SerializedName("driver_id")
    private Integer driverId;
    
    @SerializedName("tipo")
    private String tipo;
    
    @SerializedName("type")
    private String type;
    
    @SerializedName("data_validade")
    private String dataValidade;
    
    @SerializedName("expiry_date")
    private String expiryDate;
    
    @SerializedName("status")
    private String status;
    
    @SerializedName("notas")
    private String notas;
    
    @SerializedName("notes")
    private String notes;
    
    @SerializedName("dias_para_vencimento")
    private Integer diasParaVencimento;
    
    @SerializedName("days_to_expiry")
    private Integer daysToExpiry;
    
    @SerializedName("entidade")
    private String entidade;
    
    @SerializedName("vehicle")
    private Map<String, Object> vehicle;
    
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
    public Integer getFileId() {
        return fileId;
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
    public String getTipo() {
        return tipo != null ? tipo : type;
    }
    
    @Nullable
    public String getDataValidade() {
        return dataValidade != null ? dataValidade : expiryDate;
    }
    
    @Nullable
    public String getStatus() {
        return status;
    }
    
    @Nullable
    public String getNotas() {
        return notas != null ? notas : notes;
    }
    
    @Nullable
    public Integer getDiasParaVencimento() {
        return diasParaVencimento != null ? diasParaVencimento : daysToExpiry;
    }
    
    @Nullable
    public String getEntidade() {
        return entidade;
    }
    
    @Nullable
    public Map<String, Object> getVehicle() {
        return vehicle;
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
     * Verifica se o documento está válido.
     */
    public boolean isValid() {
        return "valido".equalsIgnoreCase(status) || "valid".equalsIgnoreCase(status);
    }
    
    /**
     * Verifica se o documento está expirado.
     */
    public boolean isExpired() {
        return "expirado".equalsIgnoreCase(status) || "expired".equalsIgnoreCase(status);
    }
    
    /**
     * Verifica se o documento está próximo do vencimento.
     */
    public boolean isNearExpiry() {
        Integer days = getDiasParaVencimento();
        return days != null && days <= 30 && days > 0;
    }
    
    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", tipo='" + getTipo() + '\'' +
                ", dataValidade='" + getDataValidade() + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
