package com.veigest.sdk.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Estatísticas de uma empresa.
 */
public class CompanyStats {
    
    @SerializedName("company")
    private Company company;
    
    @SerializedName("vehicles_count")
    private int vehiclesCount;
    
    @SerializedName("active_vehicles")
    private int activeVehicles;
    
    @SerializedName("users_count")
    private int usersCount;
    
    @SerializedName("drivers_count")
    private int driversCount;
    
    @SerializedName("maintenance_stats")
    private MaintenanceStatsInfo maintenanceStats;
    
    @SerializedName("fuel_stats")
    private FuelStatsInfo fuelStats;
    
    // Getters
    @Nullable
    public Company getCompany() {
        return company;
    }
    
    public int getVehiclesCount() {
        return vehiclesCount;
    }
    
    public int getActiveVehicles() {
        return activeVehicles;
    }
    
    public int getUsersCount() {
        return usersCount;
    }
    
    public int getDriversCount() {
        return driversCount;
    }
    
    @Nullable
    public MaintenanceStatsInfo getMaintenanceStats() {
        return maintenanceStats;
    }
    
    @Nullable
    public FuelStatsInfo getFuelStats() {
        return fuelStats;
    }
    
    /**
     * Informações de estatísticas de manutenção.
     */
    public static class MaintenanceStatsInfo {
        
        @SerializedName("total_maintenances")
        private int totalMaintenances;
        
        @SerializedName("pending_maintenances")
        private int pendingMaintenances;
        
        @SerializedName("completed_maintenances")
        private int completedMaintenances;
        
        @SerializedName("total_cost")
        private double totalCost;
        
        public int getTotalMaintenances() {
            return totalMaintenances;
        }
        
        public int getPendingMaintenances() {
            return pendingMaintenances;
        }
        
        public int getCompletedMaintenances() {
            return completedMaintenances;
        }
        
        public double getTotalCost() {
            return totalCost;
        }
    }
    
    /**
     * Informações de estatísticas de combustível.
     */
    public static class FuelStatsInfo {
        
        @SerializedName("total_fuel_logs")
        private int totalFuelLogs;
        
        @SerializedName("total_fuel_cost")
        private double totalFuelCost;
        
        @SerializedName("total_liters")
        private double totalLiters;
        
        @SerializedName("average_cost_per_liter")
        private double averageCostPerLiter;
        
        public int getTotalFuelLogs() {
            return totalFuelLogs;
        }
        
        public double getTotalFuelCost() {
            return totalFuelCost;
        }
        
        public double getTotalLiters() {
            return totalLiters;
        }
        
        public double getAverageCostPerLiter() {
            return averageCostPerLiter;
        }
    }
    
    @Override
    public String toString() {
        return "CompanyStats{" +
                "vehiclesCount=" + vehiclesCount +
                ", activeVehicles=" + activeVehicles +
                ", usersCount=" + usersCount +
                ", driversCount=" + driversCount +
                '}';
    }
}
