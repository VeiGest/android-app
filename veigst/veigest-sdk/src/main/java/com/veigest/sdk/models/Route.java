package com.veigest.sdk.models;

import java.io.Serializable;

/**
 * Modelo de rota do sistema VeiGest.
 * Simplificado para corresponder à API atualizada.
 */
public class Route implements Serializable {
    
    private int id;
    private int companyId;
    private int vehicleId;
    private int driverId;
    private String startLocation;
    private String endLocation;
    private String startTime;
    private String endTime;
    private String status;
    private String statusLabel;
    private int duration;
    private String durationFormatted;
    private String createdAt;
    private String updatedAt;
    
    // Construtor vazio
    public Route() {}
    
    // Construtor básico
    public Route(int vehicleId, int driverId, String startLocation, String endLocation, 
                 String startTime, String endTime) {
        this.vehicleId = vehicleId;
        this.driverId = driverId;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    // Getters
    public int getId() { return id; }
    public int getCompanyId() { return companyId; }
    public int getVehicleId() { return vehicleId; }
    public int getDriverId() { return driverId; }
    public String getStartLocation() { return startLocation; }
    public String getEndLocation() { return endLocation; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public String getStatus() { return status; }
    public String getStatusLabel() { return statusLabel; }
    public int getDuration() { return duration; }
    public String getDurationFormatted() { return durationFormatted; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    
    // Setters
    public void setId(int id) { this.id = id; }
    public void setCompanyId(int companyId) { this.companyId = companyId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }
    public void setDriverId(int driverId) { this.driverId = driverId; }
    public void setStartLocation(String startLocation) { this.startLocation = startLocation; }
    public void setEndLocation(String endLocation) { this.endLocation = endLocation; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public void setStatus(String status) { this.status = status; }
    public void setStatusLabel(String statusLabel) { this.statusLabel = statusLabel; }
    public void setDuration(int duration) { this.duration = duration; }
    public void setDurationFormatted(String durationFormatted) { this.durationFormatted = durationFormatted; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    
    // Métodos auxiliares
    public boolean isScheduled() {
        return "scheduled".equals(status);
    }
    
    public boolean isInProgress() {
        return "in_progress".equals(status);
    }
    
    public boolean isCompleted() {
        return "completed".equals(status);
    }
    
    @Override
    public String toString() {
        return startLocation + " → " + endLocation;
    }
}
