package com.veigest.sdk.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Modelo de utilizador do sistema VeiGest.
 */
public class User {
    
    @SerializedName("id")
    private int id;
    
    @SerializedName("company_id")
    private int companyId;
    
    @SerializedName("nome")
    private String nome;
    
    @SerializedName("name")
    private String name;
    
    @SerializedName("email")
    private String email;
    
    @SerializedName("telefone")
    private String telefone;
    
    @SerializedName("estado")
    private String estado;
    
    @SerializedName("numero_carta")
    private String numeroCarta;
    
    @SerializedName("validade_carta")
    private String validadeCarta;
    
    @SerializedName("roles")
    private List<String> roles;
    
    @SerializedName("permissions")
    private List<String> permissions;
    
    @SerializedName("tipo")
    private String tipo;
    
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
    
    @Nullable
    public String getNome() {
        return nome != null ? nome : name;
    }
    
    @Nullable
    public String getEmail() {
        return email;
    }
    
    @Nullable
    public String getTelefone() {
        return telefone;
    }
    
    @Nullable
    public String getEstado() {
        return estado;
    }
    
    @Nullable
    public String getNumeroCarta() {
        return numeroCarta;
    }
    
    @Nullable
    public String getValidadeCarta() {
        return validadeCarta;
    }
    
    @Nullable
    public List<String> getRoles() {
        return roles;
    }
    
    @Nullable
    public List<String> getPermissions() {
        return permissions;
    }
    
    @Nullable
    public String getTipo() {
        return tipo;
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
     * Verifica se o utilizador está ativo.
     */
    public boolean isActive() {
        return "ativo".equalsIgnoreCase(estado) || "active".equalsIgnoreCase(estado);
    }
    
    /**
     * Verifica se o utilizador tem uma determinada role.
     */
    public boolean hasRole(String role) {
        return roles != null && roles.contains(role);
    }
    
    /**
     * Verifica se o utilizador tem uma determinada permissão.
     */
    public boolean hasPermission(String permission) {
        return permissions != null && permissions.contains(permission);
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nome='" + getNome() + '\'' +
                ", email='" + email + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}
