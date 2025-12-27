package com.veigest.sdk.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Modelo de registo de abastecimento do sistema VeiGest.
 */
public class FuelLog {
    
    @SerializedName("id")
    private int id;
    
    @SerializedName("company_id")
    private int companyId;
    
    @SerializedName("vehicle_id")
    private int vehicleId;
    
    @SerializedName("driver_id")
    private Integer driverId;
    
    @SerializedName("data")
    private String data;
    
    @SerializedName("date")
    private String date;
    
    @SerializedName("litros")
    private double litros;
    
    @SerializedName("liters")
    private double liters;
    
    @SerializedName("valor")
    private double valor;
    
    @SerializedName("value")
    private double value;
    
    @SerializedName("preco_litro")
    private double precoLitro;
    
    @SerializedName("price_per_liter")
    private double pricePerLiter;
    
    @SerializedName("km_atual")
    private int kmAtual;
    
    @SerializedName("current_km")
    private int currentKm;
    
    @SerializedName("notas")
    private String notas;
    
    @SerializedName("notes")
    private String notes;
    
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
    public Integer getDriverId() {
        return driverId;
    }
    
    @Nullable
    public String getData() {
        return data != null ? data : date;
    }
    
    public double getLitros() {
        return litros > 0 ? litros : liters;
    }
    
    public double getValor() {
        return valor > 0 ? valor : value;
    }
    
    public double getPrecoLitro() {
        if (precoLitro > 0) return precoLitro;
        if (pricePerLiter > 0) return pricePerLiter;
        // Calcular se não vier da API
        double l = getLitros();
        double v = getValor();
        return l > 0 ? v / l : 0;
    }
    
    public int getKmAtual() {
        return kmAtual > 0 ? kmAtual : currentKm;
    }
    
    @Nullable
    public String getNotas() {
        return notas != null ? notas : notes;
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
        return "FuelLog{" +
                "id=" + id +
                ", vehicleId=" + vehicleId +
                ", data='" + getData() + '\'' +
                ", litros=" + getLitros() +
                ", valor=" + getValor() +
                '}';
    }
}
