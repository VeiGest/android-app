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
    
    @GET("company")
    Call<ApiResponse<List<Company>>> getCompanies(
            @Query("page") Integer page,
            @Query("limit") Integer limit,
            @Query("sort") String sort,
            @Query("filter") String filter
    );
    
    @GET("company/{id}")
    Call<ApiResponse<Company>> getCompany(@Path("id") int id);
    
    @POST("company")
    Call<ApiResponse<Company>> createCompany(@Body Map<String, Object> body);
    
    @PUT("company/{id}")
    Call<ApiResponse<Company>> updateCompany(@Path("id") int id, @Body Map<String, Object> body);
    
    @PATCH("company/{id}")
    Call<ApiResponse<Company>> patchCompany(@Path("id") int id, @Body Map<String, Object> body);
    
    @DELETE("company/{id}")
    Call<ApiResponse<Void>> deleteCompany(@Path("id") int id);
    
    // Endpoints personalizados de empresas
    @GET("company/{id}/vehicles")
    Call<ApiResponse<List<Vehicle>>> getCompanyVehicles(@Path("id") int companyId);
    
    @GET("company/{id}/users")
    Call<ApiResponse<List<User>>> getCompanyUsers(@Path("id") int companyId);
    
    @GET("company/{id}/stats")
    Call<ApiResponse<CompanyStats>> getCompanyStats(@Path("id") int companyId);
    
    // ==================== UTILIZADORES ====================
    
    @GET("user")
    Call<ApiResponse<List<User>>> getUsers(
            @Query("company_id") Integer companyId,
            @Query("page") Integer page,
            @Query("limit") Integer limit,
            @Query("sort") String sort,
            @Query("filter") String filter
    );
    
    @GET("user/{id}")
    Call<ApiResponse<User>> getUser(@Path("id") int id);
    
    @POST("user")
    Call<ApiResponse<User>> createUser(@Body Map<String, Object> body);
    
    @PUT("user/{id}")
    Call<ApiResponse<User>> updateUser(@Path("id") int id, @Body Map<String, Object> body);
    
    @PATCH("user/{id}")
    Call<ApiResponse<User>> patchUser(@Path("id") int id, @Body Map<String, Object> body);
    
    @DELETE("user/{id}")
    Call<ApiResponse<Void>> deleteUser(@Path("id") int id);
    
    @POST("user/{id}/reset-password")
    Call<ApiResponse<Void>> resetPassword(@Path("id") int id, @Body Map<String, String> body);
    
    // Endpoints personalizados de utilizadores
    @GET("user/drivers")
    Call<ApiResponse<List<User>>> getDrivers();
    
    @GET("user/profile")
    Call<ApiResponse<User>> getProfile();
    
    @GET("user/by-company/{company_id}")
    Call<ApiResponse<List<User>>> getUsersByCompany(@Path("company_id") int companyId);
    
    @Multipart
    @POST("user/{id}/update-photo")
    Call<ApiResponse<User>> updateUserPhoto(
            @Path("id") int id, 
            @Part MultipartBody.Part photo
    );
    
    // ==================== VEÍCULOS ====================
    
    @GET("vehicle")
    Call<ApiResponse<List<Vehicle>>> getVehicles(
            @Query("company_id") Integer companyId,
            @Query("estado") String estado,
            @Query("page") Integer page,
            @Query("limit") Integer limit,
            @Query("sort") String sort
    );
    
    @GET("vehicle/{id}")
    Call<ApiResponse<Vehicle>> getVehicle(@Path("id") int id);
    
    @POST("vehicle")
    Call<ApiResponse<Vehicle>> createVehicle(@Body Map<String, Object> body);
    
    @PUT("vehicle/{id}")
    Call<ApiResponse<Vehicle>> updateVehicle(@Path("id") int id, @Body Map<String, Object> body);
    
    @PATCH("vehicle/{id}")
    Call<ApiResponse<Vehicle>> patchVehicle(@Path("id") int id, @Body Map<String, Object> body);
    
    @DELETE("vehicle/{id}")
    Call<ApiResponse<Void>> deleteVehicle(@Path("id") int id);
    
    @POST("vehicle/{id}/assign-driver")
    Call<ApiResponse<Void>> assignDriver(@Path("id") int vehicleId, @Body Map<String, Integer> body);
    
    @POST("vehicle/{id}/unassign-driver")
    Call<ApiResponse<Void>> unassignDriver(@Path("id") int vehicleId);
    
    // Endpoints personalizados de veículos
    @GET("vehicle/{id}/maintenances")
    Call<ApiResponse<List<Maintenance>>> getVehicleMaintenances(@Path("id") int vehicleId);
    
    @GET("vehicle/{id}/fuel-logs")
    Call<ApiResponse<List<FuelLog>>> getVehicleFuelLogs(@Path("id") int vehicleId);
    
    @GET("vehicle/{id}/stats")
    Call<ApiResponse<VehicleStats>> getVehicleStats(@Path("id") int vehicleId);
    
    @GET("vehicle/by-status/{status}")
    Call<ApiResponse<List<Vehicle>>> getVehiclesByStatus(@Path("status") String status);
    
    // ==================== MANUTENÇÕES ====================
    
    @GET("maintenance")
    Call<ApiResponse<List<Maintenance>>> getMaintenances(
            @Query("vehicle_id") Integer vehicleId,
            @Query("data_inicio") String dataInicio,
            @Query("data_fim") String dataFim,
            @Query("page") Integer page,
            @Query("limit") Integer limit
    );
    
    @GET("maintenance/{id}")
    Call<ApiResponse<Maintenance>> getMaintenance(@Path("id") int id);
    
    @POST("maintenance")
    Call<ApiResponse<Maintenance>> createMaintenance(@Body Map<String, Object> body);
    
    @PUT("maintenance/{id}")
    Call<ApiResponse<Maintenance>> updateMaintenance(@Path("id") int id, @Body Map<String, Object> body);
    
    @PATCH("maintenance/{id}")
    Call<ApiResponse<Maintenance>> patchMaintenance(@Path("id") int id, @Body Map<String, Object> body);
    
    @DELETE("maintenance/{id}")
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
    
    @GET("fuel-log")
    Call<ApiResponse<List<FuelLog>>> getFuelLogs(
            @Query("vehicle_id") Integer vehicleId,
            @Query("driver_id") Integer driverId,
            @Query("data_inicio") String dataInicio,
            @Query("data_fim") String dataFim,
            @Query("page") Integer page,
            @Query("limit") Integer limit
    );
    
    @GET("fuel-log/{id}")
    Call<ApiResponse<FuelLog>> getFuelLog(@Path("id") int id);
    
    @POST("fuel-log")
    Call<ApiResponse<FuelLog>> createFuelLog(@Body Map<String, Object> body);
    
    @PUT("fuel-log/{id}")
    Call<ApiResponse<FuelLog>> updateFuelLog(@Path("id") int id, @Body Map<String, Object> body);
    
    @PATCH("fuel-log/{id}")
    Call<ApiResponse<FuelLog>> patchFuelLog(@Path("id") int id, @Body Map<String, Object> body);
    
    @DELETE("fuel-log/{id}")
    Call<ApiResponse<Void>> deleteFuelLog(@Path("id") int id);
    
    // Endpoints personalizados de combustível
    @GET("fuel-log/by-vehicle/{vehicle_id}")
    Call<ApiResponse<List<FuelLog>>> getFuelLogsByVehicle(@Path("vehicle_id") int vehicleId);
    
    @GET("fuel-log/stats")
    Call<ApiResponse<ReportStats>> getFuelLogStats(
            @Query("vehicle_id") Integer vehicleId,
            @Query("start_date") String startDate,
            @Query("end_date") String endDate
    );
    
    @GET("fuel-log/alerts")
    Call<ApiResponse<List<FuelAlert>>> getFuelAlerts();
    
    @GET("fuel-log/efficiency-report")
    Call<ApiResponse<FuelEfficiencyReport>> getFuelEfficiencyReport(
            @Query("start_date") String startDate,
            @Query("end_date") String endDate
    );
    
    // ==================== ROTAS ====================
    
    @GET("route")
    Call<ApiResponse<List<Route>>> getRoutes(
            @Query("vehicle_id") Integer vehicleId,
            @Query("driver_id") Integer driverId,
            @Query("status") String status,
            @Query("data_inicio") String dataInicio,
            @Query("data_fim") String dataFim,
            @Query("page") Integer page,
            @Query("limit") Integer limit
    );
    
    @GET("route/{id}")
    Call<ApiResponse<Route>> getRoute(@Path("id") int id);
    
    @POST("route")
    Call<ApiResponse<Route>> createRoute(@Body Map<String, Object> body);
    
    @PUT("route/{id}")
    Call<ApiResponse<Route>> updateRoute(@Path("id") int id, @Body Map<String, Object> body);
    
    @PATCH("route/{id}/finish")
    Call<ApiResponse<Route>> finishRoute(@Path("id") int id, @Body Map<String, Object> body);
    
    @PATCH("route/{id}/cancel")
    Call<ApiResponse<Route>> cancelRoute(@Path("id") int id, @Body Map<String, Object> body);
    
    @DELETE("route/{id}")
    Call<ApiResponse<Void>> deleteRoute(@Path("id") int id);
    
    // ==================== TICKETS ====================
    
    @GET("ticket")
    Call<ApiResponse<List<Ticket>>> getTickets(
            @Query("status") String status,
            @Query("tipo") String tipo,
            @Query("prioridade") String prioridade,
            @Query("vehicle_id") Integer vehicleId,
            @Query("page") Integer page,
            @Query("limit") Integer limit
    );
    
    @GET("ticket/{id}")
    Call<ApiResponse<Ticket>> getTicket(@Path("id") int id);
    
    @POST("ticket")
    Call<ApiResponse<Ticket>> createTicket(@Body Map<String, Object> body);
    
    @PUT("ticket/{id}")
    Call<ApiResponse<Ticket>> updateTicket(@Path("id") int id, @Body Map<String, Object> body);
    
    @POST("ticket/{id}/cancel")
    Call<ApiResponse<Ticket>> cancelTicket(@Path("id") int id, @Body Map<String, String> body);
    
    @POST("ticket/{id}/complete")
    Call<ApiResponse<Ticket>> completeTicket(@Path("id") int id, @Body Map<String, String> body);
    
    @DELETE("ticket/{id}")
    Call<ApiResponse<Void>> deleteTicket(@Path("id") int id);
    
    // ==================== GPS ====================
    
    @GET("gps-entry")
    Call<ApiResponse<List<GpsEntry>>> getGpsEntries(
            @Query("route_id") Integer routeId,
            @Query("timestamp_inicio") String timestampInicio,
            @Query("timestamp_fim") String timestampFim,
            @Query("page") Integer page,
            @Query("limit") Integer limit
    );
    
    @GET("route/{routeId}/gps-entries")
    Call<ApiResponse<List<GpsEntry>>> getRouteGpsEntries(
            @Path("routeId") int routeId,
            @Query("page") Integer page,
            @Query("limit") Integer limit
    );
    
    @POST("gps-entry")
    Call<ApiResponse<GpsEntry>> createGpsEntry(@Body Map<String, Object> body);
    
    @POST("gps-entry/batch")
    Call<ApiResponse<Void>> createGpsEntriesBatch(@Body Map<String, Object> body);
    
    @DELETE("gps-entry/{id}")
    Call<ApiResponse<Void>> deleteGpsEntry(@Path("id") int id);
    
    // ==================== DOCUMENTOS ====================
    
    @GET("document")
    Call<ApiResponse<List<Document>>> getDocuments(
            @Query("vehicle_id") Integer vehicleId,
            @Query("driver_id") Integer driverId,
            @Query("tipo") String tipo,
            @Query("status") String status,
            @Query("page") Integer page,
            @Query("limit") Integer limit
    );
    
    @GET("document/{id}")
    Call<ApiResponse<Document>> getDocument(@Path("id") int id);
    
    @POST("document")
    Call<ApiResponse<Document>> createDocument(@Body Map<String, Object> body);
    
    @PUT("document/{id}")
    Call<ApiResponse<Document>> updateDocument(@Path("id") int id, @Body Map<String, Object> body);
    
    @PATCH("document/{id}")
    Call<ApiResponse<Document>> patchDocument(@Path("id") int id, @Body Map<String, Object> body);
    
    @DELETE("document/{id}")
    Call<ApiResponse<Void>> deleteDocument(@Path("id") int id);
    
    @GET("document/expiring")
    Call<ApiResponse<List<Document>>> getExpiringDocuments(@Query("dias") Integer dias);
    
    // ==================== ALERTAS ====================
    
    @GET("alert")
    Call<ApiResponse<List<Alert>>> getAlerts(
            @Query("tipo") String tipo,
            @Query("status") String status,
            @Query("prioridade") String prioridade,
            @Query("page") Integer page,
            @Query("limit") Integer limit
    );
    
    @GET("alert/{id}")
    Call<ApiResponse<Alert>> getAlert(@Path("id") int id);
    
    @POST("alert")
    Call<ApiResponse<Alert>> createAlert(@Body Map<String, Object> body);
    
    @PATCH("alert/{id}/resolve")
    Call<ApiResponse<Alert>> resolveAlert(@Path("id") int id, @Body Map<String, String> body);
    
    @PATCH("alert/{id}/ignore")
    Call<ApiResponse<Alert>> ignoreAlert(@Path("id") int id, @Body Map<String, String> body);
    
    @DELETE("alert/{id}")
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
    
    @GET("file")
    Call<ApiResponse<List<FileInfo>>> getFiles(
            @Query("tipo") String tipo,
            @Query("page") Integer page,
            @Query("limit") Integer limit
    );
    
    @Multipart
    @POST("file")
    Call<ApiResponse<FileInfo>> uploadFile(
            @Part MultipartBody.Part file,
            @Part("nome") RequestBody nome,
            @Part("tipo") RequestBody tipo
    );
    
    @GET("file/{id}")
    Call<ApiResponse<FileInfo>> getFile(@Path("id") int id);
    
    @GET("file/{id}/download")
    Call<okhttp3.ResponseBody> downloadFile(@Path("id") int id);
    
    @DELETE("file/{id}")
    Call<ApiResponse<Void>> deleteFile(@Path("id") int id);
}
