package com.veigest.sdk.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.veigest.sdk.models.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Classe utilitária para parsing de JSON.
 * Converte respostas da API em objetos do modelo.
 */
public class VeiGestJsonParser {
    
    // ==================== LOGIN ====================
    
    /**
     * Parse da resposta de login.
     * @param response JSON da resposta
     * @return Array com [token, User] ou null em caso de erro
     */
    public static Object[] parserJsonLogin(JSONObject response) {
        try {
            String token = response.optString("token", null);
            
            JSONObject userJson = response.optJSONObject("user");
            User user = null;
            if (userJson != null) {
                user = parserJsonUser(userJson);
            }
            
            return new Object[]{token, user};
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // ==================== USER ====================
    
    public static User parserJsonUser(JSONObject json) {
        try {
            User user = new User();
            user.setId(json.optInt("id", 0));
            user.setUsername(json.optString("username", ""));
            user.setEmail(json.optString("email", ""));
            user.setRole(json.optString("role", ""));
            user.setStatus(json.optString("status", "active"));
            user.setCompanyId(json.optInt("company_id", 0));
            user.setCreatedAt(json.optString("created_at", null));
            user.setUpdatedAt(json.optString("updated_at", null));
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static ArrayList<User> parserJsonUsers(JSONArray jsonArray) {
        ArrayList<User> users = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                User user = parserJsonUser(jsonArray.getJSONObject(i));
                if (user != null) {
                    users.add(user);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return users;
    }
    
    // ==================== VEHICLE ====================
    
    public static Vehicle parserJsonVehicle(JSONObject json) {
        try {
            Vehicle vehicle = new Vehicle();
            vehicle.setId(json.optInt("id", 0));
            vehicle.setCompanyId(json.optInt("company_id", 0));
            vehicle.setLicensePlate(json.optString("license_plate", ""));
            vehicle.setBrand(json.optString("brand", ""));
            vehicle.setModel(json.optString("model", ""));
            vehicle.setYear(json.optInt("year", 0));
            vehicle.setFuelType(json.optString("fuel_type", ""));
            vehicle.setMileage(json.optInt("mileage", 0));
            vehicle.setStatus(json.optString("status", "active"));
            vehicle.setDriverId(json.optInt("driver_id", 0));
            vehicle.setDriverName(json.optString("driver_name", null));
            vehicle.setPhoto(json.optString("photo", null));
            vehicle.setCreatedAt(json.optString("created_at", null));
            vehicle.setUpdatedAt(json.optString("updated_at", null));
            return vehicle;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static ArrayList<Vehicle> parserJsonVehicles(JSONArray jsonArray) {
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                Vehicle vehicle = parserJsonVehicle(jsonArray.getJSONObject(i));
                if (vehicle != null) {
                    vehicles.add(vehicle);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return vehicles;
    }
    
    // ==================== MAINTENANCE ====================
    
    public static Maintenance parserJsonMaintenance(JSONObject json) {
        try {
            Maintenance maintenance = new Maintenance();
            maintenance.setId(json.optInt("id", 0));
            maintenance.setCompanyId(json.optInt("company_id", 0));
            maintenance.setVehicleId(json.optInt("vehicle_id", 0));
            maintenance.setType(json.optString("type", ""));
            maintenance.setTypeLabel(json.optString("type_label", ""));
            maintenance.setDescription(json.optString("description", ""));
            maintenance.setCost(json.optDouble("cost", 0.0));
            maintenance.setDate(json.optString("date", ""));
            maintenance.setMileageRecord(json.optInt("mileage_record", 0));
            maintenance.setNextDate(json.optString("next_date", null));
            maintenance.setWorkshop(json.optString("workshop", ""));
            maintenance.setStatus(json.optString("status", ""));
            maintenance.setStatusLabel(json.optString("status_label", ""));
            maintenance.setCreatedAt(json.optString("created_at", null));
            maintenance.setUpdatedAt(json.optString("updated_at", null));
            return maintenance;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static ArrayList<Maintenance> parserJsonMaintenances(JSONArray jsonArray) {
        ArrayList<Maintenance> maintenances = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                Maintenance maintenance = parserJsonMaintenance(jsonArray.getJSONObject(i));
                if (maintenance != null) {
                    maintenances.add(maintenance);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return maintenances;
    }
    
    // ==================== FUEL LOG ====================
    
    public static FuelLog parserJsonFuelLog(JSONObject json) {
        try {
            FuelLog fuelLog = new FuelLog();
            fuelLog.setId(json.optInt("id", 0));
            fuelLog.setVehicleId(json.optInt("vehicle_id", 0));
            fuelLog.setLiters(json.optDouble("liters", 0.0));
            fuelLog.setValue(json.optDouble("value", 0.0));
            fuelLog.setCurrentMileage(json.optInt("current_mileage", 0));
            fuelLog.setDate(json.optString("date", ""));
            fuelLog.setPricePerLiter(json.optDouble("price_per_liter", 0.0));
            fuelLog.setNotes(json.optString("notes", ""));
            fuelLog.setCreatedAt(json.optString("created_at", null));
            fuelLog.setUpdatedAt(json.optString("updated_at", null));
            return fuelLog;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static ArrayList<FuelLog> parserJsonFuelLogs(JSONArray jsonArray) {
        ArrayList<FuelLog> fuelLogs = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                FuelLog fuelLog = parserJsonFuelLog(jsonArray.getJSONObject(i));
                if (fuelLog != null) {
                    fuelLogs.add(fuelLog);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fuelLogs;
    }
    
    // ==================== ALERT ====================
    
    public static Alert parserJsonAlert(JSONObject json) {
        try {
            Alert alert = new Alert();
            alert.setId(json.optInt("id", 0));
            alert.setCompanyId(json.optInt("company_id", 0));
            alert.setType(json.optString("type", ""));
            alert.setTypeLabel(json.optString("type_label", ""));
            alert.setTitle(json.optString("title", ""));
            alert.setDescription(json.optString("description", ""));
            alert.setPriority(json.optString("priority", ""));
            alert.setPriorityLabel(json.optString("priority_label", ""));
            alert.setStatus(json.optString("status", ""));
            alert.setStatusLabel(json.optString("status_label", ""));
            alert.setDetails(json.optString("details", null));
            alert.setAge(json.optString("age", null));
            alert.setCreatedAt(json.optString("created_at", null));
            alert.setResolvedAt(json.optString("resolved_at", null));
            return alert;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static ArrayList<Alert> parserJsonAlerts(JSONArray jsonArray) {
        ArrayList<Alert> alerts = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                Alert alert = parserJsonAlert(jsonArray.getJSONObject(i));
                if (alert != null) {
                    alerts.add(alert);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return alerts;
    }
    
    // ==================== DOCUMENT ====================
    
    public static Document parserJsonDocument(JSONObject json) {
        try {
            Document document = new Document();
            document.setId(json.optInt("id", 0));
            document.setCompanyId(json.optInt("company_id", 0));
            document.setFileId(json.optInt("file_id", 0));
            document.setVehicleId(json.optInt("vehicle_id", 0));
            document.setDriverId(json.optInt("driver_id", 0));
            document.setType(json.optString("type", ""));
            document.setTypeLabel(json.optString("type_label", ""));
            document.setExpiryDate(json.optString("expiry_date", null));
            document.setDaysToExpiry(json.optInt("days_to_expiry", 0));
            document.setStatus(json.optString("status", ""));
            document.setNotes(json.optString("notes", ""));
            document.setCreatedAt(json.optString("created_at", null));
            document.setUpdatedAt(json.optString("updated_at", null));
            return document;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static ArrayList<Document> parserJsonDocuments(JSONArray jsonArray) {
        ArrayList<Document> documents = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                Document document = parserJsonDocument(jsonArray.getJSONObject(i));
                if (document != null) {
                    documents.add(document);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return documents;
    }
    
    // ==================== ROUTE ====================
    
    public static Route parserJsonRoute(JSONObject json) {
        try {
            Route route = new Route();
            route.setId(json.optInt("id", 0));
            route.setCompanyId(json.optInt("company_id", 0));
            route.setVehicleId(json.optInt("vehicle_id", 0));
            route.setDriverId(json.optInt("driver_id", 0));
            route.setStartLocation(json.optString("start_location", ""));
            route.setEndLocation(json.optString("end_location", ""));
            route.setStartTime(json.optString("start_time", null));
            route.setEndTime(json.optString("end_time", null));
            route.setStatus(json.optString("status", ""));
            route.setStatusLabel(json.optString("status_label", ""));
            route.setDuration(json.optInt("duration", 0));
            route.setDurationFormatted(json.optString("duration_formatted", ""));
            route.setCreatedAt(json.optString("created_at", null));
            route.setUpdatedAt(json.optString("updated_at", null));
            return route;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static ArrayList<Route> parserJsonRoutes(JSONArray jsonArray) {
        ArrayList<Route> routes = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                Route route = parserJsonRoute(jsonArray.getJSONObject(i));
                if (route != null) {
                    routes.add(route);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return routes;
    }
    
    // ==================== UTILITÁRIOS ====================
    
    /**
     * Verifica se há conexão com a internet.
     */
    public static boolean isConnectionInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return false;
    }
    
    /**
     * Extrai mensagem de erro do JSON de resposta.
     */
    public static String parserJsonError(JSONObject json) {
        try {
            if (json.has("message")) {
                return json.getString("message");
            }
            if (json.has("error")) {
                return json.getString("error");
            }
            if (json.has("errors")) {
                JSONArray errors = json.getJSONArray("errors");
                if (errors.length() > 0) {
                    JSONObject firstError = errors.getJSONObject(0);
                    return firstError.optString("message", "Erro desconhecido");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "Erro ao processar resposta";
    }
    
    /**
     * Extrai array de items da resposta paginada.
     */
    public static JSONArray getItemsFromResponse(JSONObject response) {
        try {
            if (response.has("items")) {
                return response.getJSONArray("items");
            }
            // Se não tiver "items", pode ser um array direto
            if (response.has("data")) {
                Object data = response.get("data");
                if (data instanceof JSONArray) {
                    return (JSONArray) data;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }
}
