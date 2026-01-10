package com.veigest.sdk.models;

import java.io.Serializable;

/**
 * Modelo de documento do sistema VeiGest.
 * Simplificado para corresponder à API atualizada.
 */
public class Document implements Serializable {
    
    private int id;
    private int companyId;
    private int fileId;
    private int vehicleId;
    private int driverId;
    private String type;
    private String typeLabel;
    private String expiryDate;
    private int daysToExpiry;
    private String status;
    private String notes;
    private String createdAt;
    private String updatedAt;
    
    // Construtor vazio
    public Document() {}
    
    // Construtor básico
    public Document(int fileId, int vehicleId, String type, String expiryDate) {
        this.fileId = fileId;
        this.vehicleId = vehicleId;
        this.type = type;
        this.expiryDate = expiryDate;
    }
    
    // Getters
    public int getId() { return id; }
    public int getCompanyId() { return companyId; }
    public int getFileId() { return fileId; }
    public int getVehicleId() { return vehicleId; }
    public int getDriverId() { return driverId; }
    public String getType() { return type; }
    public String getTypeLabel() { return typeLabel; }
    public String getExpiryDate() { return expiryDate; }
    public int getDaysToExpiry() { return daysToExpiry; }
    public String getStatus() { return status; }
    public String getNotes() { return notes; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    
    // Setters
    public void setId(int id) { this.id = id; }
    public void setCompanyId(int companyId) { this.companyId = companyId; }
    public void setFileId(int fileId) { this.fileId = fileId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }
    public void setDriverId(int driverId) { this.driverId = driverId; }
    public void setType(String type) { this.type = type; }
    public void setTypeLabel(String typeLabel) { this.typeLabel = typeLabel; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
    public void setDaysToExpiry(int daysToExpiry) { this.daysToExpiry = daysToExpiry; }
    public void setStatus(String status) { this.status = status; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    
    // Métodos auxiliares
    public boolean isExpired() {
        return "expired".equals(status) || daysToExpiry < 0;
    }
    
    public boolean isExpiringSoon() {
        return daysToExpiry >= 0 && daysToExpiry <= 30;
    }
    
    @Override
    public String toString() {
        return typeLabel != null ? typeLabel : type;
    }
}
