package com.veigest.sdk.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Estatísticas de um veículo.
 */
public class VehicleStats {
    
    @SerializedName("vehicle")
    private Vehicle vehicle;
    
    @SerializedName("vehicle_id")
    private int vehicleId;
    
    @SerializedName("matricula")
    private String matricula;
    
    @SerializedName("license_plate")
    private String licensePlate;
    
    // Estatísticas de combustível
    @SerializedName("fuel_stats")
    private FuelStats fuelStats;
    
    @SerializedName("total_fuel_cost")
    private double totalFuelCost;
    
    @SerializedName("total_liters")
    private double totalLiters;
    
    @SerializedName("fuel_efficiency")
    private double fuelEfficiency;
    
    @SerializedName("cost_per_km")
    private double costPerKm;
    
    // Estatísticas de manutenção
    @SerializedName("maintenance_stats")
    private MaintenanceStats maintenanceStats;
    
    @SerializedName("total_maintenance_cost")
    private double totalMaintenanceCost;
    
    @SerializedName("maintenances_count")
    private int maintenancesCount;
    
    @SerializedName("pending_maintenances")
    private int pendingMaintenances;
    
    // Estatísticas de uso
    @SerializedName("total_km")
    private int totalKm;
    
    @SerializedName("total_routes")
    private int totalRoutes;
    
    @SerializedName("average_km_per_day")
    private double averageKmPerDay;
    
    // Custos totais
    @SerializedName("total_cost")
    private double totalCost;
    
    // Getters
    @Nullable
    public Vehicle getVehicle() {
        return vehicle;
    }
    
    public int getVehicleId() {
        return vehicleId;
    }
    
    public String getMatricula() {
        return matricula != null ? matricula : licensePlate;
    }
    
    @Nullable
    public FuelStats getFuelStats() {
        return fuelStats;
    }
    
    public double getTotalFuelCost() {
        return totalFuelCost;
    }
    
    public double getTotalLiters() {
        return totalLiters;
    }
    
    public double getFuelEfficiency() {
        return fuelEfficiency;
    }
    
    public double getCostPerKm() {
        return costPerKm;
    }
    
    @Nullable
    public MaintenanceStats getMaintenanceStats() {
        return maintenanceStats;
    }
    
    public double getTotalMaintenanceCost() {
        return totalMaintenanceCost;
    }
    
    public int getMaintenancesCount() {
        return maintenancesCount;
    }
    
    public int getPendingMaintenances() {
        return pendingMaintenances;
    }
    
    public int getTotalKm() {
        return totalKm;
    }
    
    public int getTotalRoutes() {
        return totalRoutes;
    }
    
    public double getAverageKmPerDay() {
        return averageKmPerDay;
    }
    
    public double getTotalCost() {
        return totalCost;
    }
    
    /**
     * Estatísticas de combustível detalhadas.
     */
    public static class FuelStats {
        
        @SerializedName("total_cost")
        private double totalCost;
        
        @SerializedName("total_liters")
        private double totalLiters;
        
        @SerializedName("fuel_efficiency")
        private double fuelEfficiency;
        
        @SerializedName("cost_per_km")
        private double costPerKm;
        
        @SerializedName("logs_count")
        private int logsCount;
        
        public double getTotalCost() {
            return totalCost;
        }
        
        public double getTotalLiters() {
            return totalLiters;
        }
        
        public double getFuelEfficiency() {
            return fuelEfficiency;
        }
        
        public double getCostPerKm() {
            return costPerKm;
        }
        
        public int getLogsCount() {
            return logsCount;
        }
    }
    
    /**
     * Estatísticas de manutenção detalhadas.
     */
    public static class MaintenanceStats {
        
        @SerializedName("total_cost")
        private double totalCost;
        
        @SerializedName("count")
        private int count;
        
        @SerializedName("pending")
        private int pending;
        
        @SerializedName("completed")
        private int completed;
        
        public double getTotalCost() {
            return totalCost;
        }
        
        public int getCount() {
            return count;
        }
        
        public int getPending() {
            return pending;
        }
        
        public int getCompleted() {
            return completed;
        }
    }
    
    @Override
    public String toString() {
        return "VehicleStats{" +
                "vehicleId=" + vehicleId +
                ", matricula='" + getMatricula() + '\'' +
                ", totalFuelCost=" + totalFuelCost +
                ", totalMaintenanceCost=" + totalMaintenanceCost +
                ", totalCost=" + totalCost +
                '}';
    }
}
