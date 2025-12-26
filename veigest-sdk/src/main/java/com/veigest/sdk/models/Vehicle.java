package com.veigest.sdk.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Modelo de veículo do sistema VeiGest.
 */
public class Vehicle {
    
    @SerializedName("id")
    private int id;
    
    @SerializedName("company_id")
    private int companyId;
    
    @SerializedName("matricula")
    private String matricula;
    
    @SerializedName("license_plate")
    private String licensePlate;
    
    @SerializedName("marca")
    private String marca;
    
    @SerializedName("brand")
    private String brand;
    
    @SerializedName("modelo")
    private String modelo;
    
    @SerializedName("model")
    private String model;
    
    @SerializedName("ano")
    private int ano;
    
    @SerializedName("year")
    private int year;
    
    @SerializedName("tipo_combustivel")
    private String tipoCombustivel;
    
    @SerializedName("fuel_type")
    private String fuelType;
    
    @SerializedName("quilometragem")
    private int quilometragem;
    
    @SerializedName("mileage")
    private int mileage;
    
    @SerializedName("estado")
    private String estado;
    
    @SerializedName("status")
    private String status;
    
    @SerializedName("condutor_id")
    private Integer condutorId;
    
    @SerializedName("driver_id")
    private Integer driverId;
    
    @SerializedName("created_at")
    private String createdAt;
    
    @SerializedName("updated_at")
    private String updatedAt;
    
    // Getters com fallback para campos alternativos
    public int getId() {
        return id;
    }
    
    public int getCompanyId() {
        return companyId;
    }
    
    @Nullable
    public String getMatricula() {
        return matricula != null ? matricula : licensePlate;
    }
    
    @Nullable
    public String getMarca() {
        return marca != null ? marca : brand;
    }
    
    @Nullable
    public String getModelo() {
        return modelo != null ? modelo : model;
    }
    
    public int getAno() {
        return ano > 0 ? ano : year;
    }
    
    @Nullable
    public String getTipoCombustivel() {
        return tipoCombustivel != null ? tipoCombustivel : fuelType;
    }
    
    public int getQuilometragem() {
        return quilometragem > 0 ? quilometragem : mileage;
    }
    
    @Nullable
    public String getEstado() {
        return estado != null ? estado : status;
    }
    
    @Nullable
    public Integer getCondutorId() {
        return condutorId != null ? condutorId : driverId;
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
     * Verifica se o veículo está ativo.
     */
    public boolean isActive() {
        String currentStatus = getEstado();
        return "ativo".equalsIgnoreCase(currentStatus) || "active".equalsIgnoreCase(currentStatus);
    }
    
    /**
     * Verifica se o veículo está em manutenção.
     */
    public boolean isInMaintenance() {
        String currentStatus = getEstado();
        return "manutencao".equalsIgnoreCase(currentStatus) || "maintenance".equalsIgnoreCase(currentStatus);
    }
    
    /**
     * Verifica se o veículo tem condutor atribuído.
     */
    public boolean hasDriver() {
        Integer driver = getCondutorId();
        return driver != null && driver > 0;
    }
    
    /**
     * Retorna nome completo do veículo (Marca Modelo).
     */
    public String getFullName() {
        String m = getMarca();
        String mod = getModelo();
        if (m != null && mod != null) {
            return m + " " + mod;
        } else if (m != null) {
            return m;
        } else if (mod != null) {
            return mod;
        }
        return "";
    }
    
    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", matricula='" + getMatricula() + '\'' +
                ", marca='" + getMarca() + '\'' +
                ", modelo='" + getModelo() + '\'' +
                ", estado='" + getEstado() + '\'' +
                '}';
    }
}
