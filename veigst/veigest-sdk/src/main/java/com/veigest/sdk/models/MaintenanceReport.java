package com.veigest.sdk.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Relatório de manutenções.
 */
public class MaintenanceReport {
    
    @SerializedName("period")
    private String period;
    
    @SerializedName("month")
    private String month;
    
    @SerializedName("year")
    private int year;
    
    @SerializedName("total_maintenances")
    private int totalMaintenances;
    
    @SerializedName("total_cost")
    private double totalCost;
    
    @SerializedName("by_type")
    private List<TypeBreakdown> byType;
    
    @SerializedName("by_vehicle")
    private List<VehicleBreakdown> byVehicle;
    
    @SerializedName("by_status")
    private List<StatusBreakdown> byStatus;
    
    // Getters
    @Nullable
    public String getPeriod() {
        return period;
    }
    
    @Nullable
    public String getMonth() {
        return month;
    }
    
    public int getYear() {
        return year;
    }
    
    public int getTotalMaintenances() {
        return totalMaintenances;
    }
    
    public double getTotalCost() {
        return totalCost;
    }
    
    @Nullable
    public List<TypeBreakdown> getByType() {
        return byType;
    }
    
    @Nullable
    public List<VehicleBreakdown> getByVehicle() {
        return byVehicle;
    }
    
    @Nullable
    public List<StatusBreakdown> getByStatus() {
        return byStatus;
    }
    
    /**
     * Breakdown por tipo de manutenção.
     */
    public static class TypeBreakdown {
        
        @SerializedName("tipo")
        private String tipo;
        
        @SerializedName("type")
        private String type;
        
        @SerializedName("count")
        private int count;
        
        @SerializedName("total_cost")
        private double totalCost;
        
        @SerializedName("percentage")
        private double percentage;
        
        public String getTipo() {
            return tipo != null ? tipo : type;
        }
        
        public int getCount() {
            return count;
        }
        
        public double getTotalCost() {
            return totalCost;
        }
        
        public double getPercentage() {
            return percentage;
        }
    }
    
    /**
     * Breakdown por veículo.
     */
    public static class VehicleBreakdown {
        
        @SerializedName("vehicle_id")
        private int vehicleId;
        
        @SerializedName("matricula")
        private String matricula;
        
        @SerializedName("license_plate")
        private String licensePlate;
        
        @SerializedName("count")
        private int count;
        
        @SerializedName("total_cost")
        private double totalCost;
        
        public int getVehicleId() {
            return vehicleId;
        }
        
        public String getMatricula() {
            return matricula != null ? matricula : licensePlate;
        }
        
        public int getCount() {
            return count;
        }
        
        public double getTotalCost() {
            return totalCost;
        }
    }
    
    /**
     * Breakdown por status.
     */
    public static class StatusBreakdown {
        
        @SerializedName("estado")
        private String estado;
        
        @SerializedName("status")
        private String status;
        
        @SerializedName("count")
        private int count;
        
        public String getEstado() {
            return estado != null ? estado : status;
        }
        
        public int getCount() {
            return count;
        }
    }
    
    @Override
    public String toString() {
        return "MaintenanceReport{" +
                "period='" + period + '\'' +
                ", totalMaintenances=" + totalMaintenances +
                ", totalCost=" + totalCost +
                '}';
    }
}
