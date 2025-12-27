package com.veigest.sdk.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Modelo de empresa do sistema VeiGest.
 */
public class Company {
    
    @SerializedName("id")
    private int id;
    
    @SerializedName("code")
    private String code;
    
    @SerializedName("nome")
    private String nome;
    
    @SerializedName("name")
    private String name;
    
    @SerializedName("nif")
    private String nif;
    
    @SerializedName("email")
    private String email;
    
    @SerializedName("telefone")
    private String telefone;
    
    @SerializedName("morada")
    private String morada;
    
    @SerializedName("estado")
    private String estado;
    
    @SerializedName("status")
    private String status;
    
    @SerializedName("plano")
    private String plano;
    
    @SerializedName("plan")
    private String plan;
    
    @SerializedName("configuracoes")
    private Object configuracoes;
    
    @SerializedName("created_at")
    private String createdAt;
    
    @SerializedName("updated_at")
    private String updatedAt;
    
    // Getters
    public int getId() {
        return id;
    }
    
    @Nullable
    public String getCode() {
        return code;
    }
    
    @Nullable
    public String getNome() {
        return nome != null ? nome : name;
    }
    
    @Nullable
    public String getNif() {
        return nif;
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
    public String getMorada() {
        return morada;
    }
    
    @Nullable
    public String getEstado() {
        return estado != null ? estado : status;
    }
    
    @Nullable
    public String getPlano() {
        return plano != null ? plano : plan;
    }
    
    @Nullable
    public Object getConfiguracoes() {
        return configuracoes;
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
     * Verifica se a empresa está ativa.
     */
    public boolean isActive() {
        String currentStatus = getEstado();
        return "ativa".equalsIgnoreCase(currentStatus) || "active".equalsIgnoreCase(currentStatus);
    }
    
    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", nome='" + getNome() + '\'' +
                ", nif='" + nif + '\'' +
                ", estado='" + getEstado() + '\'' +
                '}';
    }
}
