package com.veigest.sdk.models;

import java.io.Serializable;

/**
 * Modelo de alerta do sistema VeiGest.
 * Simplificado para corresponder à API atualizada.
 */
public class Alert implements Serializable {
    
    private int id;
    private int companyId;
    private String type;
    private String typeLabel;
    private String title;
    private String description;
    private String priority;
    private String priorityLabel;
    private int priorityLevel;
    private String status;
    private String statusLabel;
    private String details;
    private String age;
    private String createdAt;
    private String resolvedAt;
    
    // Construtor vazio
    public Alert() {}
    
    // Construtor básico
    public Alert(String type, String title, String description, String priority) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.priority = priority;
    }
    
    // Getters
    public int getId() { return id; }
    public int getCompanyId() { return companyId; }
    public String getType() { return type; }
    public String getTypeLabel() { return typeLabel; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getPriority() { return priority; }
    public String getPriorityLabel() { return priorityLabel; }
    public int getPriorityLevel() { return priorityLevel; }
    public String getStatus() { return status; }
    public String getStatusLabel() { return statusLabel; }
    public String getDetails() { return details; }
    public String getAge() { return age; }
    public String getCreatedAt() { return createdAt; }
    public String getResolvedAt() { return resolvedAt; }
    
    // Setters
    public void setId(int id) { this.id = id; }
    public void setCompanyId(int companyId) { this.companyId = companyId; }
    public void setType(String type) { this.type = type; }
    public void setTypeLabel(String typeLabel) { this.typeLabel = typeLabel; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setPriority(String priority) { this.priority = priority; }
    public void setPriorityLabel(String priorityLabel) { this.priorityLabel = priorityLabel; }
    public void setPriorityLevel(int priorityLevel) { this.priorityLevel = priorityLevel; }
    public void setStatus(String status) { this.status = status; }
    public void setStatusLabel(String statusLabel) { this.statusLabel = statusLabel; }
    public void setDetails(String details) { this.details = details; }
    public void setAge(String age) { this.age = age; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setResolvedAt(String resolvedAt) { this.resolvedAt = resolvedAt; }
    
    // Métodos auxiliares
    public boolean isActive() {
        return "active".equals(status);
    }
    
    public boolean isResolved() {
        return "resolved".equals(status);
    }
    
    public boolean isCritical() {
        return "critical".equals(priority);
    }
    
    @Override
    public String toString() {
        return title + " [" + priorityLabel + "]";
    }
}
