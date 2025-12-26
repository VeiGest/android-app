package com.veigest.sdk.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Modelo de rota do sistema VeiGest.
 */
public class Route {
    
    @SerializedName("id")
    private int id;
    
    @SerializedName("company_id")
    private int companyId;
    
    @SerializedName("vehicle_id")
    private int vehicleId;
    
    @SerializedName("driver_id")
    private int driverId;
    
    @SerializedName("inicio")
    private String inicio;
    
    @SerializedName("start_time")
    private String startTime;
    
    @SerializedName("fim")
    private String fim;
    
    @SerializedName("end_time")
    private String endTime;
    
    @SerializedName("km_inicial")
    private int kmInicial;
    
    @SerializedName("start_km")
    private int startKm;
    
    @SerializedName("km_final")
    private int kmFinal;
    
    @SerializedName("end_km")
    private int endKm;
    
    @SerializedName("origem")
    private String origem;
    
    @SerializedName("origin")
    private String origin;
    
    @SerializedName("destino")
    private String destino;
    
    @SerializedName("destination")
    private String destination;
    
    @SerializedName("distancia_km")
    private double distanciaKm;
    
    @SerializedName("distance_km")
    private double distanceKm;
    
    @SerializedName("status")
    private String status;
    
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
    
    public int getDriverId() {
        return driverId;
    }
    
    @Nullable
    public String getInicio() {
        return inicio != null ? inicio : startTime;
    }
    
    @Nullable
    public String getFim() {
        return fim != null ? fim : endTime;
    }
    
    public int getKmInicial() {
        return kmInicial > 0 ? kmInicial : startKm;
    }
    
    public int getKmFinal() {
        return kmFinal > 0 ? kmFinal : endKm;
    }
    
    @Nullable
    public String getOrigem() {
        return origem != null ? origem : origin;
    }
    
    @Nullable
    public String getDestino() {
        return destino != null ? destino : destination;
    }
    
    public double getDistanciaKm() {
        if (distanciaKm > 0) return distanciaKm;
        if (distanceKm > 0) return distanceKm;
        // Calcular se não vier da API
        int inicial = getKmInicial();
        int finalKm = getKmFinal();
        return finalKm > inicial ? finalKm - inicial : 0;
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
    public String getCreatedAt() {
        return createdAt;
    }
    
    @Nullable
    public String getUpdatedAt() {
        return updatedAt;
    }
    
    /**
     * Verifica se a rota está em andamento.
     */
    public boolean isInProgress() {
        return "em_andamento".equalsIgnoreCase(status) || "in_progress".equalsIgnoreCase(status);
    }
    
    /**
     * Verifica se a rota foi concluída.
     */
    public boolean isCompleted() {
        return "concluida".equalsIgnoreCase(status) || "completed".equalsIgnoreCase(status);
    }
    
    /**
     * Verifica se a rota foi cancelada.
     */
    public boolean isCancelled() {
        return "cancelada".equalsIgnoreCase(status) || "cancelled".equalsIgnoreCase(status);
    }
    
    @Override
    public String toString() {
        return "Route{" +
                "id=" + id +
                ", origem='" + getOrigem() + '\'' +
                ", destino='" + getDestino() + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
