package com.veigest.sdk.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Modelo de manutenção do sistema VeiGest.
 */
public class Maintenance {
    
    @SerializedName("id")
    private int id;
    
    @SerializedName("company_id")
    private int companyId;
    
    @SerializedName("vehicle_id")
    private int vehicleId;
    
    @SerializedName("tipo")
    private String tipo;
    
    @SerializedName("type")
    private String type;
    
    @SerializedName("descricao")
    private String descricao;
    
    @SerializedName("description")
    private String description;
    
    @SerializedName("data")
    private String data;
    
    @SerializedName("date")
    private String date;
    
    @SerializedName("custo")
    private double custo;
    
    @SerializedName("cost")
    private double cost;
    
    @SerializedName("km_registro")
    private int kmRegistro;
    
    @SerializedName("km_record")
    private int kmRecord;
    
    @SerializedName("proxima_data")
    private String proximaData;
    
    @SerializedName("next_date")
    private String nextDate;
    
    @SerializedName("oficina")
    private String oficina;
    
    @SerializedName("workshop")
    private String workshop;
    
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
    
    public int getVehicleId() {
        return vehicleId;
    }
    
    @Nullable
    public String getTipo() {
        return tipo != null ? tipo : type;
    }
    
    @Nullable
    public String getDescricao() {
        return descricao != null ? descricao : description;
    }
    
    @Nullable
    public String getData() {
        return data != null ? data : date;
    }
    
    public double getCusto() {
        return custo > 0 ? custo : cost;
    }
    
    public int getKmRegistro() {
        return kmRegistro > 0 ? kmRegistro : kmRecord;
    }
    
    @Nullable
    public String getProximaData() {
        return proximaData != null ? proximaData : nextDate;
    }
    
    @Nullable
    public String getOficina() {
        return oficina != null ? oficina : workshop;
    }
    
    @Nullable
    public String getCreatedAt() {
        return createdAt;
    }
    
    @Nullable
    public String getUpdatedAt() {
        return updatedAt;
    }
    
    @Override
    public String toString() {
        return "Maintenance{" +
                "id=" + id +
                ", vehicleId=" + vehicleId +
                ", tipo='" + getTipo() + '\'' +
                ", data='" + getData() + '\'' +
                ", custo=" + getCusto() +
                '}';
    }
}
