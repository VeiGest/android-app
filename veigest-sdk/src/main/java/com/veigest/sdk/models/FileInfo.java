package com.veigest.sdk.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Informações de um ficheiro.
 */
public class FileInfo {
    
    @SerializedName("id")
    private int id;
    
    @SerializedName("company_id")
    private int companyId;
    
    @SerializedName("nome")
    private String nome;
    
    @SerializedName("name")
    private String name;
    
    @SerializedName("nome_original")
    private String nomeOriginal;
    
    @SerializedName("original_name")
    private String originalName;
    
    @SerializedName("tipo")
    private String tipo;
    
    @SerializedName("mime_type")
    private String mimeType;
    
    @SerializedName("tamanho")
    private long tamanho;
    
    @SerializedName("size")
    private long size;
    
    @SerializedName("caminho")
    private String caminho;
    
    @SerializedName("path")
    private String path;
    
    @SerializedName("url")
    private String url;
    
    @SerializedName("download_url")
    private String downloadUrl;
    
    @SerializedName("created_at")
    private String createdAt;
    
    @SerializedName("data_upload")
    private String dataUpload;
    
    @SerializedName("uploaded_by")
    private Integer uploadedBy;
    
    // Getters
    public int getId() {
        return id;
    }
    
    public int getCompanyId() {
        return companyId;
    }
    
    public String getNome() {
        return nome != null ? nome : name;
    }
    
    public String getNomeOriginal() {
        return nomeOriginal != null ? nomeOriginal : originalName;
    }
    
    public String getTipo() {
        return tipo != null ? tipo : mimeType;
    }
    
    public long getTamanho() {
        return tamanho != 0 ? tamanho : size;
    }
    
    @Nullable
    public String getCaminho() {
        return caminho != null ? caminho : path;
    }
    
    @Nullable
    public String getUrl() {
        return url;
    }
    
    @Nullable
    public String getDownloadUrl() {
        return downloadUrl;
    }
    
    public String getCreatedAt() {
        return createdAt != null ? createdAt : dataUpload;
    }
    
    @Nullable
    public Integer getUploadedBy() {
        return uploadedBy;
    }
    
    // Helpers
    public boolean isImage() {
        String t = getTipo();
        return t != null && t.startsWith("image/");
    }
    
    public boolean isPdf() {
        String t = getTipo();
        return t != null && t.equals("application/pdf");
    }
    
    /**
     * Retorna o tamanho formatado (KB, MB, etc.).
     */
    public String getFormattedSize() {
        long bytes = getTamanho();
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.1f KB", bytes / 1024.0);
        } else if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
        } else {
            return String.format("%.1f GB", bytes / (1024.0 * 1024.0 * 1024.0));
        }
    }
    
    @Override
    public String toString() {
        return "FileInfo{" +
                "id=" + id +
                ", nome='" + getNome() + '\'' +
                ", tipo='" + getTipo() + '\'' +
                ", tamanho=" + getFormattedSize() +
                '}';
    }
}
