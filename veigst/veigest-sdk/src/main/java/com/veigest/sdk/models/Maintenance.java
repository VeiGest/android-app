package com.veigest.sdk.models;

import java.io.Serializable;

/**
 * Modelo de manutenção do sistema VeiGest.
 * Simplificado para corresponder à API atualizada.
 */
public class Maintenance implements Serializable {
    
    private int id;
    private int companyId;
    private int vehicleId;
    private String type;
    private String typeLabel;
    private String description;
    private double cost;
    private String date;
    private int mileageRecord;
    private String nextDate;
    private String workshop;
    private String status;
    private String statusLabel;
    private String createdAt;
    private String updatedAt;
    
    // Construtor vazio
    public Maintenance() {}
    
    // Construtor básico
    public Maintenance(int vehicleId, String type, String description, double cost, String date) {
        this.vehicleId = vehicleId;
        this.type = type;
        this.description = description;
        this.cost = cost;
        this.date = date;
    }
    
    // Getters
    public int getId() { return id; }
    public int getCompanyId() { return companyId; }
    public int getVehicleId() { return vehicleId; }
    public String getType() { return type; }
    public String getTypeLabel() { return typeLabel; }
    public String getDescription() { return description; }
    public double getCost() { return cost; }
    public String getDate() { return date; }
    public int getMileageRecord() { return mileageRecord; }
    public String getNextDate() { return nextDate; }
    public String getWorkshop() { return workshop; }
    public String getStatus() { return status; }
    public String getStatusLabel() { return statusLabel; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    
    // Setters
    public void setId(int id) { this.id = id; }
    public void setCompanyId(int companyId) { this.companyId = companyId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }
    public void setType(String type) { this.type = type; }
    public void setTypeLabel(String typeLabel) { this.typeLabel = typeLabel; }
    public void setDescription(String description) { this.description = description; }
    public void setCost(double cost) { this.cost = cost; }
    public void setDate(String date) { this.date = date; }
    public void setMileageRecord(int mileageRecord) { this.mileageRecord = mileageRecord; }
    public void setNextDate(String nextDate) { this.nextDate = nextDate; }
    public void setWorkshop(String workshop) { this.workshop = workshop; }
    public void setStatus(String status) { this.status = status; }
    public void setStatusLabel(String statusLabel) { this.statusLabel = statusLabel; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    
    @Override
    public String toString() {
        return typeLabel != null ? typeLabel : type + " - " + date;
    }
}
