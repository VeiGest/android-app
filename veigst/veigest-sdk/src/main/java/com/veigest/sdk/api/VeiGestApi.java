package com.veigest.sdk.api;

import com.veigest.sdk.models.*;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;


// Interface para definir os endpoints da API VeiGest
// De forma a facilitar a implementação com 

public interface VeiGestApi {
    
    @POST("auth/login")
    Call<ApiResponse<LoginResponse>> login(@Body LoginRequest request);
    
    @POST("auth/refresh")
    Call<ApiResponse<LoginResponse>> refreshToken(@Body Map<String, String> body);
    
    @POST("auth/logout")
    Call<ApiResponse<Void>> logout();
    
    @GET("auth/me")
    Call<ApiResponse<User>> getCurrentUser();
    
    // ==================== EMPRESAS ====================
    
    @GET("companies")
    Call<ApiResponse<List<Company>>> getCompanies(
            @Query("page") Integer page,
            @Query("limit") Integer limit,
            @Query("sort") String sort,
            @Query("filter") String filter
    );
    
    @GET("companies/{id}")
    Call<ApiResponse<Company>> getCompany(@Path("id") int id);
    
    @POST("companies")
    Call<ApiResponse<Company>> createCompany(@Body Map<String, Object> body);
    
    @PUT("companies/{id}")
    Call<ApiResponse<Company>> updateCompany(@Path("id") int id, @Body Map<String, Object> body);
    
    @PATCH("companies/{id}")
    Call<ApiResponse<Company>> patchCompany(@Path("id") int id, @Body Map<String, Object> body);
    
    @DELETE("companies/{id}")
    Call<ApiResponse<Void>> deleteCompany(@Path("id") int id);
    
    // Endpoints personalizados de empresas
    @GET("companies/{id}/vehicles")
    Call<ApiResponse<List<Vehicle>>> getCompanyVehicles(@Path("id") int companyId);
    
    @GET("companies/{id}/users")
    Call<ApiResponse<List<User>>> getCompanyUsers(@Path("id") int companyId);
    
    @GET("companies/{id}/stats")
    Call<ApiResponse<CompanyStats>> getCompanyStats(@Path("id") int companyId);
    
    // ==================== UTILIZADORES ====================
    
    @GET("users")
    Call<ApiResponse<List<User>>> getUsers(
            @Query("company_id") Integer companyId,
            @Query("page") Integer page,
            @Query("limit") Integer limit,
            @Query("sort") String sort,
            @Query("filter") String filter
    );
    
    @GET("users/{id}")
    Call<ApiResponse<User>> getUser(@Path("id") int id);
    
    @POST("users")
    Call<ApiResponse<User>> createUser(@Body Map<String, Object> body);
    
    @PUT("users/{id}")
    Call<ApiResponse<User>> updateUser(@Path("id") int id, @Body Map<String, Object> body);
    
    @PATCH("users/{id}")
    Call<ApiResponse<User>> patchUser(@Path("id") int id, @Body Map<String, Object> body);
    
    @DELETE("users/{id}")
    Call<ApiResponse<Void>> deleteUser(@Path("id") int id);
    
    @POST("users/{id}/reset-password")
    Call<ApiResponse<Void>> resetPassword(@Path("id") int id, @Body Map<String, String> body);
    
    // Endpoints personalizados de utilizadores
    @GET("users/drivers")
    Call<ApiResponse<List<User>>> getDrivers();
    
    @GET("users/profile")
    Call<ApiResponse<User>> getProfile();
    
    @GET("users/by-company/{company_id}")
    Call<ApiResponse<List<User>>> getUsersByCompany(@Path("company_id") int companyId);
    
    @Multipart
    @POST("users/{id}/update-photo")
    Call<ApiResponse<User>> updateUserPhoto(
            @Path("id") int id, 
            @Part MultipartBody.Part photo
    );
    
    // ==================== VEÍCULOS ====================
    
    @GET("vehicles")
    Call<ApiResponse<List<Vehicle>>> getVehicles(
            @Query("company_id") Integer companyId,
            @Query("estado") String estado,
            @Query("page") Integer page,
            @Query("limit") Integer limit,
            @Query("sort") String sort
    );
    
    @GET("vehicles/{id}")
    Call<ApiResponse<Vehicle>> getVehicle(@Path("id") int id);
    
    @POST("vehicles")
    Call<ApiResponse<Vehicle>> createVehicle(@Body Map<String, Object> body);
    
    @PUT("vehicles/{id}")
    Call<ApiResponse<Vehicle>> updateVehicle(@Path("id") int id, @Body Map<String, Object> body);
    
    @PATCH("vehicles/{id}")
    Call<ApiResponse<Vehicle>> patchVehicle(@Path("id") int id, @Body Map<String, Object> body);
    
    @DELETE("vehicles/{id}")
    Call<ApiResponse<Void>> deleteVehicle(@Path("id") int id);
    
    @POST("vehicles/{id}/assign-driver")
    Call<ApiResponse<Void>> assignDriver(@Path("id") int vehicleId, @Body Map<String, Integer> body);
    
    @POST("vehicles/{id}/unassign-driver")
    Call<ApiResponse<Void>> unassignDriver(@Path("id") int vehicleId);
    
    // Endpoints personalizados de veículos
    @GET("vehicles/{id}/maintenances")
    Call<ApiResponse<List<Maintenance>>> getVehicleMaintenances(@Path("id") int vehicleId);
    
    @GET("vehicles/{id}/fuel-logs")
    Call<ApiResponse<List<FuelLog>>> getVehicleFuelLogs(@Path("id") int vehicleId);
    
    @GET("vehicles/{id}/stats")
    Call<ApiResponse<VehicleStats>> getVehicleStats(@Path("id") int vehicleId);
    
    @GET("vehicles/by-status/{status}")
    Call<ApiResponse<List<Vehicle>>> getVehiclesByStatus(@Path("status") String status);
    
    // ==================== MANUTENÇÕES ====================
    
    @GET("maintenances")
    Call<ApiResponse<List<Maintenance>>> getMaintenances(
            @Query("vehicle_id") Integer vehicleId,
            @Query("data_inicio") String dataInicio,
            @Query("data_fim") String dataFim,
            @Query("page") Integer page,
            @Query("limit") Integer limit
    );
    
    @GET("maintenances/{id}")
    Call<ApiResponse<Maintenance>> getMaintenance(@Path("id") int id);
    
    @POST("maintenances")
    Call<ApiResponse<Maintenance>> createMaintenance(@Body Map<String, Object> body);
    
    @PUT("maintenances/{id}")
    Call<ApiResponse<Maintenance>> updateMaintenance(@Path("id") int id, @Body Map<String, Object> body);
    
    @PATCH("maintenances/{id}")
    Call<ApiResponse<Maintenance>> patchMaintenance(@Path("id") int id, @Body Map<String, Object> body);
    
    @DELETE("maintenances/{id}")
    Call<ApiResponse<Void>> deleteMaintenance(@Path("id") int id);
    
    // Endpoints personalizados de manutenções
    @GET("maintenance/by-vehicle/{vehicle_id}")
    Call<ApiResponse<List<Maintenance>>> getMaintenancesByVehicle(@Path("vehicle_id") int vehicleId);
    
    @GET("maintenance/by-status/{estado}")
    Call<ApiResponse<List<Maintenance>>> getMaintenancesByStatus(@Path("estado") String estado);
    
    @POST("maintenance/{id}/schedule")
    Call<ApiResponse<Maintenance>> scheduleMaintenance(
            @Path("id") int id, 
            @Body Map<String, Object> body
    );
    
    @GET("maintenance/reports/monthly")
    Call<ApiResponse<MaintenanceReport>> getMaintenanceMonthlyReport(
            @Query("month") Integer month,
            @Query("year") Integer year
    );
    
    @GET("maintenance/reports/costs")
    Call<ApiResponse<MaintenanceReport>> getMaintenanceCostsReport(
            @Query("vehicle_id") Integer vehicleId,
            @Query("start_date") String startDate,
            @Query("end_date") String endDate
    );
    
    @GET("maintenance/stats")
    Call<ApiResponse<ReportStats>> getMaintenanceStats();
    
    // ==================== COMBUSTÍVEL ====================
    
    @GET("fuel-logs")
    Call<ApiResponse<List<FuelLog>>> getFuelLogs(
            @Query("vehicle_id") Integer vehicleId,
            @Query("driver_id") Integer driverId,
            @Query("data_inicio") String dataInicio,
            @Query("data_fim") String dataFim,
            @Query("page") Integer page,
            @Query("limit") Integer limit
    );
    
    @GET("fuel-logs/{id}")
    Call<ApiResponse<FuelLog>> getFuelLog(@Path("id") int id);
    
    @POST("fuel-logs")
    Call<ApiResponse<FuelLog>> createFuelLog(@Body Map<String, Object> body);
    
    @PUT("fuel-logs/{id}")
    Call<ApiResponse<FuelLog>> updateFuelLog(@Path("id") int id, @Body Map<String, Object> body);
    
    @PATCH("fuel-logs/{id}")
    Call<ApiResponse<FuelLog>> patchFuelLog(@Path("id") int id, @Body Map<String, Object> body);
    
    @DELETE("fuel-logs/{id}")
    Call<ApiResponse<Void>> deleteFuelLog(@Path("id") int id);
    
    // Endpoints personalizados de combustível
    @GET("fuel-logs/by-vehicle/{vehicle_id}")
    Call<ApiResponse<List<FuelLog>>> getFuelLogsByVehicle(@Path("vehicle_id") int vehicleId);
    
    @GET("fuel-logs/stats")
    Call<ApiResponse<ReportStats>> getFuelLogStats(
            @Query("vehicle_id") Integer vehicleId,
            @Query("start_date") String startDate,
            @Query("end_date") String endDate
    );
    
    @GET("fuel-logs/alerts")
    Call<ApiResponse<List<FuelAlert>>> getFuelAlerts();
    
    @GET("fuel-logs/efficiency-report")
    Call<ApiResponse<FuelEfficiencyReport>> getFuelEfficiencyReport(
            @Query("start_date") String startDate,
            @Query("end_date") String endDate
    );
    
    // ==================== ROTAS ====================
    
    @GET("routes")
    Call<ApiResponse<List<Route>>> getRoutes(
            @Query("vehicle_id") Integer vehicleId,
            @Query("driver_id") Integer driverId,
            @Query("status") String status,
            @Query("data_inicio") String dataInicio,
            @Query("data_fim") String dataFim,
            @Query("page") Integer page,
            @Query("limit") Integer limit
    );
    
    @GET("routes/{id}")
    Call<ApiResponse<Route>> getRoute(@Path("id") int id);
    
    @POST("routes")
    Call<ApiResponse<Route>> createRoute(@Body Map<String, Object> body);
    
    @PUT("routes/{id}")
    Call<ApiResponse<Route>> updateRoute(@Path("id") int id, @Body Map<String, Object> body);
    
    @PATCH("routes/{id}/finish")
    Call<ApiResponse<Route>> finishRoute(@Path("id") int id, @Body Map<String, Object> body);
    
    @PATCH("routes/{id}/cancel")
    Call<ApiResponse<Route>> cancelRoute(@Path("id") int id, @Body Map<String, Object> body);
    
    @DELETE("routes/{id}")
    Call<ApiResponse<Void>> deleteRoute(@Path("id") int id);
    
    // ==================== TICKETS ====================
    
    @GET("tickets")
    Call<ApiResponse<List<Ticket>>> getTickets(
            @Query("status") String status,
            @Query("tipo") String tipo,
            @Query("prioridade") String prioridade,
            @Query("vehicle_id") Integer vehicleId,
            @Query("page") Integer page,
            @Query("limit") Integer limit
    );
    
    @GET("tickets/{id}")
    Call<ApiResponse<Ticket>> getTicket(@Path("id") int id);
    
    @POST("tickets")
    Call<ApiResponse<Ticket>> createTicket(@Body Map<String, Object> body);
    
    @PUT("tickets/{id}")
    Call<ApiResponse<Ticket>> updateTicket(@Path("id") int id, @Body Map<String, Object> body);
    
    @POST("tickets/{id}/cancel")
    Call<ApiResponse<Ticket>> cancelTicket(@Path("id") int id, @Body Map<String, String> body);
    
    @POST("tickets/{id}/complete")
    Call<ApiResponse<Ticket>> completeTicket(@Path("id") int id, @Body Map<String, String> body);
    
    @DELETE("tickets/{id}")
    Call<ApiResponse<Void>> deleteTicket(@Path("id") int id);
    
    // ==================== GPS ====================
    
    @GET("gps-entries")
    Call<ApiResponse<List<GpsEntry>>> getGpsEntries(
            @Query("route_id") Integer routeId,
            @Query("timestamp_inicio") String timestampInicio,
            @Query("timestamp_fim") String timestampFim,
            @Query("page") Integer page,
            @Query("limit") Integer limit
    );
    
    @GET("routes/{routeId}/gps-entries")
    Call<ApiResponse<List<GpsEntry>>> getRouteGpsEntries(
            @Path("routeId") int routeId,
            @Query("page") Integer page,
            @Query("limit") Integer limit
    );
    
    @POST("gps-entries")
    Call<ApiResponse<GpsEntry>> createGpsEntry(@Body Map<String, Object> body);
    
    @POST("gps-entries/batch")
    Call<ApiResponse<Void>> createGpsEntriesBatch(@Body Map<String, Object> body);
    
    @DELETE("gps-entries/{id}")
    Call<ApiResponse<Void>> deleteGpsEntry(@Path("id") int id);
    
    // ==================== DOCUMENTOS ====================
    
    @GET("documents")
    Call<ApiResponse<List<Document>>> getDocuments(
            @Query("vehicle_id") Integer vehicleId,
            @Query("driver_id") Integer driverId,
            @Query("tipo") String tipo,
            @Query("status") String status,
            @Query("page") Integer page,
            @Query("limit") Integer limit
    );
    
    @GET("documents/{id}")
    Call<ApiResponse<Document>> getDocument(@Path("id") int id);
    
    @POST("documents")
    Call<ApiResponse<Document>> createDocument(@Body Map<String, Object> body);
    
    @PUT("documents/{id}")
    Call<ApiResponse<Document>> updateDocument(@Path("id") int id, @Body Map<String, Object> body);
    
    @PATCH("documents/{id}")
    Call<ApiResponse<Document>> patchDocument(@Path("id") int id, @Body Map<String, Object> body);
    
    @DELETE("documents/{id}")
    Call<ApiResponse<Void>> deleteDocument(@Path("id") int id);
    
    @GET("documents/expiring")
    Call<ApiResponse<List<Document>>> getExpiringDocuments(@Query("dias") Integer dias);
    
    // ==================== ALERTAS ====================
    
    @GET("alerts")
    Call<ApiResponse<List<Alert>>> getAlerts(
            @Query("tipo") String tipo,
            @Query("status") String status,
            @Query("prioridade") String prioridade,
            @Query("page") Integer page,
            @Query("limit") Integer limit
    );
    
    @GET("alerts/{id}")
    Call<ApiResponse<Alert>> getAlert(@Path("id") int id);
    
    @POST("alerts")
    Call<ApiResponse<Alert>> createAlert(@Body Map<String, Object> body);
    
    @PATCH("alerts/{id}/resolve")
    Call<ApiResponse<Alert>> resolveAlert(@Path("id") int id, @Body Map<String, String> body);
    
    @PATCH("alerts/{id}/ignore")
    Call<ApiResponse<Alert>> ignoreAlert(@Path("id") int id, @Body Map<String, String> body);
    
    @DELETE("alerts/{id}")
    Call<ApiResponse<Void>> deleteAlert(@Path("id") int id);
    
    // ==================== RELATÓRIOS ====================
    
    @GET("reports/company-stats")
    Call<ApiResponse<ReportStats>> getCompanyStats(@Query("company_id") Integer companyId);
    
    @GET("reports/vehicle-costs")
    Call<ApiResponse<List<ReportStats>>> getVehicleCosts(
            @Query("vehicle_id") Integer vehicleId,
            @Query("data_inicio") String dataInicio,
            @Query("data_fim") String dataFim
    );
    
    @GET("reports/fuel-consumption")
    Call<ApiResponse<ReportStats>> getFuelConsumption(
            @Query("vehicle_id") Integer vehicleId,
            @Query("periodo") String periodo
    );
    
    @GET("reports/maintenance-schedule")
    Call<ApiResponse<List<ReportStats>>> getMaintenanceSchedule(
            @Query("data_inicio") String dataInicio,
            @Query("data_fim") String dataFim
    );
    
    @GET("reports/driver-performance")
    Call<ApiResponse<ReportStats>> getDriverPerformance(
            @Query("driver_id") Integer driverId,
            @Query("periodo") String periodo
    );
    
    // ==================== FICHEIROS ====================
    
    @GET("files")
    Call<ApiResponse<List<FileInfo>>> getFiles(
            @Query("tipo") String tipo,
            @Query("page") Integer page,
            @Query("limit") Integer limit
    );
    
    @Multipart
    @POST("files")
    Call<ApiResponse<FileInfo>> uploadFile(
            @Part MultipartBody.Part file,
            @Part("nome") RequestBody nome,
            @Part("tipo") RequestBody tipo
    );
    
    @GET("files/{id}")
    Call<ApiResponse<FileInfo>> getFile(@Path("id") int id);
    
    @GET("files/{id}/download")
    Call<okhttp3.ResponseBody> downloadFile(@Path("id") int id);
    
    @DELETE("files/{id}")
    Call<ApiResponse<Void>> deleteFile(@Path("id") int id);
}
