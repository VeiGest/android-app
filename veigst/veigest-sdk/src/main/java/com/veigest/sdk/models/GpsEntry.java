package com.veigest.sdk.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Modelo de ponto GPS do sistema VeiGest.
 */
public class GpsEntry {
    
    @SerializedName("id")
    private int id;
    
    @SerializedName("route_id")
    private int routeId;
    
    @SerializedName("latitude")
    private double latitude;
    
    @SerializedName("longitude")
    private double longitude;
    
    @SerializedName("timestamp")
    private String timestamp;
    
    @SerializedName("velocidade")
    private double velocidade;
    
    @SerializedName("speed")
    private double speed;
    
    @SerializedName("altitude")
    private double altitude;
    
    @SerializedName("precisao")
    private double precisao;
    
    @SerializedName("accuracy")
    private double accuracy;
    
    // Getters
    public int getId() {
        return id;
    }
    
    public int getRouteId() {
        return routeId;
    }
    
    public double getLatitude() {
        return latitude;
    }
    
    public double getLongitude() {
        return longitude;
    }
    
    @Nullable
    public String getTimestamp() {
        return timestamp;
    }
    
    public double getVelocidade() {
        return velocidade > 0 ? velocidade : speed;
    }
    
    public double getAltitude() {
        return altitude;
    }
    
    public double getPrecisao() {
        return precisao > 0 ? precisao : accuracy;
    }
    
    @Override
    public String toString() {
        return "GpsEntry{" +
                "id=" + id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", velocidade=" + getVelocidade() +
                '}';
    }
    
    /**
     * Builder para criar GpsEntry facilmente.
     */
    public static class Builder {
        private final GpsEntry entry = new GpsEntry();
        
        public Builder routeId(int routeId) {
            entry.routeId = routeId;
            return this;
        }
        
        public Builder latitude(double latitude) {
            entry.latitude = latitude;
            return this;
        }
        
        public Builder longitude(double longitude) {
            entry.longitude = longitude;
            return this;
        }
        
        public Builder velocidade(double velocidade) {
            entry.velocidade = velocidade;
            return this;
        }
        
        public Builder altitude(double altitude) {
            entry.altitude = altitude;
            return this;
        }
        
        public Builder precisao(double precisao) {
            entry.precisao = precisao;
            return this;
        }
        
        public GpsEntry build() {
            return entry;
        }
    }
}
