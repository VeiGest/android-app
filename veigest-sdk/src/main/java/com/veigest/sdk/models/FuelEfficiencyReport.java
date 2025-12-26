package com.veigest.sdk.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Relatório de eficiência de combustível.
 */
public class FuelEfficiencyReport {
    
    @SerializedName("period")
    private Period period;
    
    @SerializedName("summary")
    private Summary summary;
    
    @SerializedName("vehicle_efficiency")
    private List<VehicleEfficiency> vehicleEfficiency;
    
    @SerializedName("recommendations")
    private List<String> recommendations;
    
    // Getters
    @Nullable
    public Period getPeriod() {
        return period;
    }
    
    @Nullable
    public Summary getSummary() {
        return summary;
    }
    
    @Nullable
    public List<VehicleEfficiency> getVehicleEfficiency() {
        return vehicleEfficiency;
    }
    
    @Nullable
    public List<String> getRecommendations() {
        return recommendations;
    }
    
    /**
     * Período do relatório.
     */
    public static class Period {
        
        @SerializedName("start_date")
        private String startDate;
        
        @SerializedName("end_date")
        private String endDate;
        
        public String getStartDate() {
            return startDate;
        }
        
        public String getEndDate() {
            return endDate;
        }
    }
    
    /**
     * Resumo do relatório.
     */
    public static class Summary {
        
        @SerializedName("total_vehicles")
        private int totalVehicles;
        
        @SerializedName("total_fuel_cost")
        private double totalFuelCost;
        
        @SerializedName("total_liters")
        private double totalLiters;
        
        @SerializedName("fleet_average_efficiency")
        private double fleetAverageEfficiency;
        
        @SerializedName("total_km")
        private int totalKm;
        
        public int getTotalVehicles() {
            return totalVehicles;
        }
        
        public double getTotalFuelCost() {
            return totalFuelCost;
        }
        
        public double getTotalLiters() {
            return totalLiters;
        }
        
        public double getFleetAverageEfficiency() {
            return fleetAverageEfficiency;
        }
        
        public int getTotalKm() {
            return totalKm;
        }
    }
    
    /**
     * Eficiência por veículo.
     */
    public static class VehicleEfficiency {
        
        @SerializedName("vehicle")
        private Vehicle vehicle;
        
        @SerializedName("vehicle_id")
        private int vehicleId;
        
        @SerializedName("license_plate")
        private String licensePlate;
        
        @SerializedName("brand")
        private String brand;
        
        @SerializedName("model")
        private String model;
        
        @SerializedName("fuel_efficiency")
        private double fuelEfficiency;
        
        @SerializedName("cost_per_km")
        private double costPerKm;
        
        @SerializedName("total_cost")
        private double totalCost;
        
        @SerializedName("total_liters")
        private double totalLiters;
        
        @SerializedName("total_km")
        private int totalKm;
        
        @Nullable
        public Vehicle getVehicle() {
            return vehicle;
        }
        
        public int getVehicleId() {
            return vehicleId;
        }
        
        public String getLicensePlate() {
            return licensePlate;
        }
        
        public String getBrand() {
            return brand;
        }
        
        public String getModel() {
            return model;
        }
        
        public double getFuelEfficiency() {
            return fuelEfficiency;
        }
        
        public double getCostPerKm() {
            return costPerKm;
        }
        
        public double getTotalCost() {
            return totalCost;
        }
        
        public double getTotalLiters() {
            return totalLiters;
        }
        
        public int getTotalKm() {
            return totalKm;
        }
    }
    
    @Override
    public String toString() {
        return "FuelEfficiencyReport{" +
                "period=" + period +
                ", summary=" + summary +
                ", vehicleEfficiency=" + (vehicleEfficiency != null ? vehicleEfficiency.size() : 0) + " vehicles" +
                '}';
    }
}
