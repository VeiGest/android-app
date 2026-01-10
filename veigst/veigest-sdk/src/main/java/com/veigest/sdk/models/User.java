package com.veigest.sdk.models;

import java.io.Serializable;

/**
 * Modelo de utilizador do sistema VeiGest.
 * Simplificado para corresponder à API atualizada.
 */
public class User implements Serializable {
    
    private int id;
    private String username;
    private String email;
    private String role;
    private String status;
    private int companyId;
    private String companyName;
    private String createdAt;
    private String updatedAt;
    
    // Construtor vazio
    public User() {}
    
    // Construtor básico
    public User(int id, String username, String email, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }
    
    // Getters
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getStatus() { return status; }
    public int getCompanyId() { return companyId; }
    public String getCompanyName() { return companyName; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    
    // Setters
    public void setId(int id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setRole(String role) { this.role = role; }
    public void setStatus(String status) { this.status = status; }
    public void setCompanyId(int companyId) { this.companyId = companyId; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    
    // Método auxiliar para nome de exibição
    public String getDisplayName() {
        return username != null ? username : email;
    }
    
    @Override
    public String toString() {
        return username + " (" + email + ")";
    }
}
