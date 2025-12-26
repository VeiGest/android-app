package com.veigest.sdk.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Modelo de estatísticas de relatório.
 */
public class ReportStats {
    
    // Estatísticas da empresa
    @SerializedName("total_users")
    private int totalUsers;
    
    @SerializedName("total_vehicles")
    private int totalVehicles;
    
    @SerializedName("total_drivers")
    private int totalDrivers;
    
    @SerializedName("total_storage_bytes")
    private long totalStorageBytes;
    
    // Custos de veículo
    @SerializedName("vehicle_id")
    private Integer vehicleId;
    
    @SerializedName("matricula")
    private String matricula;
    
    @SerializedName("total_maintenance")
    private double totalMaintenance;
    
    @SerializedName("total_fuel")
    private double totalFuel;
    
    @SerializedName("total_costs")
    private double totalCosts;
    
    // Consumo de combustível
    @SerializedName("periodo")
    private String periodo;
    
    @SerializedName("total_litros")
    private double totalLitros;
    
    @SerializedName("total_valor")
    private double totalValor;
    
    @SerializedName("media_preco_litro")
    private double mediaPrecoLitro;
    
    @SerializedName("consumo_medio")
    private double consumoMedio;
    
    // Cronograma de manutenção
    @SerializedName("proximo_servico")
    private String proximoServico;
    
    @SerializedName("tipo")
    private String tipo;
    
    @SerializedName("dias_restantes")
    private int diasRestantes;
    
    // Performance do condutor
    @SerializedName("driver_id")
    private Integer driverId;
    
    @SerializedName("nome")
    private String nome;
    
    @SerializedName("km_percorridos")
    private int kmPercorridos;
    
    @SerializedName("rotas_concluidas")
    private int rotasConcluidas;
    
    @SerializedName("score")
    private int score;
    
    // Getters para estatísticas da empresa
    public int getTotalUsers() {
        return totalUsers;
    }
    
    public int getTotalVehicles() {
        return totalVehicles;
    }
    
    public int getTotalDrivers() {
        return totalDrivers;
    }
    
    public long getTotalStorageBytes() {
        return totalStorageBytes;
    }
    
    // Getters para custos de veículo
    @Nullable
    public Integer getVehicleId() {
        return vehicleId;
    }
    
    @Nullable
    public String getMatricula() {
        return matricula;
    }
    
    public double getTotalMaintenance() {
        return totalMaintenance;
    }
    
    public double getTotalFuel() {
        return totalFuel;
    }
    
    public double getTotalCosts() {
        return totalCosts;
    }
    
    // Getters para consumo de combustível
    @Nullable
    public String getPeriodo() {
        return periodo;
    }
    
    public double getTotalLitros() {
        return totalLitros;
    }
    
    public double getTotalValor() {
        return totalValor;
    }
    
    public double getMediaPrecoLitro() {
        return mediaPrecoLitro;
    }
    
    public double getConsumoMedio() {
        return consumoMedio;
    }
    
    // Getters para cronograma de manutenção
    @Nullable
    public String getProximoServico() {
        return proximoServico;
    }
    
    @Nullable
    public String getTipo() {
        return tipo;
    }
    
    public int getDiasRestantes() {
        return diasRestantes;
    }
    
    // Getters para performance do condutor
    @Nullable
    public Integer getDriverId() {
        return driverId;
    }
    
    @Nullable
    public String getNome() {
        return nome;
    }
    
    public int getKmPercorridos() {
        return kmPercorridos;
    }
    
    public int getRotasConcluidas() {
        return rotasConcluidas;
    }
    
    public int getScore() {
        return score;
    }
}
