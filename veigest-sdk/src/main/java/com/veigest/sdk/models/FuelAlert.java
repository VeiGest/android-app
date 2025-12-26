package com.veigest.sdk.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Alerta de combustível/eficiência.
 */
public class FuelAlert {
    
    @SerializedName("id")
    private int id;
    
    @SerializedName("vehicle_id")
    private int vehicleId;
    
    @SerializedName("vehicle")
    private Vehicle vehicle;
    
    @SerializedName("tipo")
    private String tipo;
    
    @SerializedName("type")
    private String type;
    
    @SerializedName("mensagem")
    private String mensagem;
    
    @SerializedName("message")
    private String message;
    
    @SerializedName("prioridade")
    private String prioridade;
    
    @SerializedName("priority")
    private String priority;
    
    @SerializedName("valor_atual")
    private double valorAtual;
    
    @SerializedName("current_value")
    private double currentValue;
    
    @SerializedName("valor_esperado")
    private double valorEsperado;
    
    @SerializedName("expected_value")
    private double expectedValue;
    
    @SerializedName("variacao_percentual")
    private double variacaoPercentual;
    
    @SerializedName("percentage_variation")
    private double percentageVariation;
    
    @SerializedName("data")
    private String data;
    
    @SerializedName("created_at")
    private String createdAt;
    
    // Getters
    public int getId() {
        return id;
    }
    
    public int getVehicleId() {
        return vehicleId;
    }
    
    @Nullable
    public Vehicle getVehicle() {
        return vehicle;
    }
    
    public String getTipo() {
        return tipo != null ? tipo : type;
    }
    
    public String getMensagem() {
        return mensagem != null ? mensagem : message;
    }
    
    public String getPrioridade() {
        return prioridade != null ? prioridade : priority;
    }
    
    public double getValorAtual() {
        return valorAtual != 0 ? valorAtual : currentValue;
    }
    
    public double getValorEsperado() {
        return valorEsperado != 0 ? valorEsperado : expectedValue;
    }
    
    public double getVariacaoPercentual() {
        return variacaoPercentual != 0 ? variacaoPercentual : percentageVariation;
    }
    
    public String getData() {
        return data != null ? data : createdAt;
    }
    
    // Helpers
    public boolean isHighPriority() {
        String p = getPrioridade();
        return p != null && (p.equalsIgnoreCase("alta") || p.equalsIgnoreCase("high") || p.equalsIgnoreCase("critica") || p.equalsIgnoreCase("critical"));
    }
    
    public boolean isLowEfficiencyAlert() {
        String t = getTipo();
        return t != null && (t.contains("eficiencia") || t.contains("efficiency") || t.contains("consumo"));
    }
    
    @Override
    public String toString() {
        return "FuelAlert{" +
                "id=" + id +
                ", vehicleId=" + vehicleId +
                ", tipo='" + getTipo() + '\'' +
                ", mensagem='" + getMensagem() + '\'' +
                ", prioridade='" + getPrioridade() + '\'' +
                '}';
    }
}
