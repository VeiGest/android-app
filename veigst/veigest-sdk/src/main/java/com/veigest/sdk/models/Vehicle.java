package com.veigest.sdk.models;

import java.io.Serializable;

/**
 * Modelo de veículo do sistema VeiGest.
 * Simplificado para corresponder à API atualizada.
 */
public class Vehicle implements Serializable {
    
    private int id;
    private int companyId;
    private String licensePlate;
    private String brand;
    private String model;
    private int year;
    private String fuelType;
    private String fuelTypeLabel;
    private int mileage;
    private String status;
    private String statusLabel;
    private int driverId;
    private String driverName;
    private String photo;
    private String createdAt;
    private String updatedAt;
    
    // Construtor vazio
    public Vehicle() {}
    
    // Construtor completo
    public Vehicle(int id, String licensePlate, String brand, String model, int year, 
                   String fuelType, int mileage, String status) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.fuelType = fuelType;
        this.mileage = mileage;
        this.status = status;
    }
    
    // Getters
    public int getId() { return id; }
    public int getCompanyId() { return companyId; }
    public String getLicensePlate() { return licensePlate; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public String getFuelType() { return fuelType; }
    public String getFuelTypeLabel() { return fuelTypeLabel; }
    public int getMileage() { return mileage; }
    public String getStatus() { return status; }
    public String getStatusLabel() { return statusLabel; }
    public int getDriverId() { return driverId; }
    public String getDriverName() { return driverName; }
    public String getPhoto() { return photo; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    
    // Setters
    public void setId(int id) { this.id = id; }
    public void setCompanyId(int companyId) { this.companyId = companyId; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }
    public void setBrand(String brand) { this.brand = brand; }
    public void setModel(String model) { this.model = model; }
    public void setYear(int year) { this.year = year; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }
    public void setFuelTypeLabel(String fuelTypeLabel) { this.fuelTypeLabel = fuelTypeLabel; }
    public void setMileage(int mileage) { this.mileage = mileage; }
    public void setStatus(String status) { this.status = status; }
    public void setStatusLabel(String statusLabel) { this.statusLabel = statusLabel; }
    public void setDriverId(int driverId) { this.driverId = driverId; }
    public void setDriverName(String driverName) { this.driverName = driverName; }
    public void setPhoto(String photo) { this.photo = photo; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    
    // Métodos auxiliares para compatibilidade (PT)
    public String getMatricula() { return licensePlate; }
    public String getMarca() { return brand; }
    public String getModelo() { return model; }
    public int getAno() { return year; }
    public int getQuilometragem() { return mileage; }
    
    @Override
    public String toString() {
        return brand + " " + model + " - " + licensePlate;
    }
}
