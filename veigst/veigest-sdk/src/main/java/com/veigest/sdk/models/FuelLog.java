package com.veigest.sdk.models;

import java.io.Serializable;

/**
 * Modelo de registo de abastecimento do sistema VeiGest.
 * Simplificado para corresponder à API atualizada.
 */
public class FuelLog implements Serializable {
    
    private int id;
    private int vehicleId;
    private double liters;
    private double value;
    private int currentMileage;
    private String date;
    private double pricePerLiter;
    private String notes;
    private String createdAt;
    private String updatedAt;
    
    // Construtor vazio
    public FuelLog() {}
    
    // Construtor básico
    public FuelLog(int vehicleId, double liters, double value, int currentMileage, String date) {
        this.vehicleId = vehicleId;
        this.liters = liters;
        this.value = value;
        this.currentMileage = currentMileage;
        this.date = date;
    }
    
    // Getters
    public int getId() { return id; }
    public int getVehicleId() { return vehicleId; }
    public double getLiters() { return liters; }
    public double getValue() { return value; }
    public int getCurrentMileage() { return currentMileage; }
    public String getDate() { return date; }
    public double getPricePerLiter() { return pricePerLiter; }
    public String getNotes() { return notes; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    
    // Setters
    public void setId(int id) { this.id = id; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }
    public void setLiters(double liters) { this.liters = liters; }
    public void setValue(double value) { this.value = value; }
    public void setCurrentMileage(int currentMileage) { this.currentMileage = currentMileage; }
    public void setDate(String date) { this.date = date; }
    public void setPricePerLiter(double pricePerLiter) { this.pricePerLiter = pricePerLiter; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    
    /**
     * Calcula o preço por litro se não estiver definido.
     */
    public double calcularPrecoLitro() {
        if (pricePerLiter > 0) return pricePerLiter;
        if (liters > 0) return value / liters;
        return 0;
    }
    
    @Override
    public String toString() {
        return liters + "L - €" + value + " (" + date + ")";
    }
}
