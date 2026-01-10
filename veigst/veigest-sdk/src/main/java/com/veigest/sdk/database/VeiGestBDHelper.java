package com.veigest.sdk.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.veigest.sdk.models.Alert;
import com.veigest.sdk.models.Document;
import com.veigest.sdk.models.FuelLog;
import com.veigest.sdk.models.Maintenance;
import com.veigest.sdk.models.Route;
import com.veigest.sdk.models.User;
import com.veigest.sdk.models.Vehicle;

import java.util.ArrayList;

/**
 * Helper para gestão da base de dados SQLite.
 * Implementado seguindo o padrão SQLiteOpenHelper do Android.
 * 
 * Responsável por:
 * - Criar e atualizar a estrutura da BD
 * - Operações CRUD para todas as entidades
 */
public class VeiGestBDHelper extends SQLiteOpenHelper {
    
    // Configuração da BD
    private static final String DB_NAME = "veigest_db";
    private static final int DB_VERSION = 1;
    
    // Tabelas
    public static final String TABLE_USERS = "users";
    public static final String TABLE_VEHICLES = "vehicles";
    public static final String TABLE_MAINTENANCES = "maintenances";
    public static final String TABLE_FUEL_LOGS = "fuel_logs";
    public static final String TABLE_ALERTS = "alerts";
    public static final String TABLE_DOCUMENTS = "documents";
    public static final String TABLE_ROUTES = "routes";
    
    // Colunas comuns
    public static final String COL_ID = "id";
    public static final String COL_COMPANY_ID = "company_id";
    public static final String COL_CREATED_AT = "created_at";
    public static final String COL_UPDATED_AT = "updated_at";
    
    // Colunas Users
    public static final String COL_USERNAME = "username";
    public static final String COL_EMAIL = "email";
    public static final String COL_ROLE = "role";
    public static final String COL_STATUS = "status";
    
    // Colunas Vehicles
    public static final String COL_LICENSE_PLATE = "license_plate";
    public static final String COL_BRAND = "brand";
    public static final String COL_MODEL = "model";
    public static final String COL_YEAR = "year";
    public static final String COL_FUEL_TYPE = "fuel_type";
    public static final String COL_MILEAGE = "mileage";
    public static final String COL_DRIVER_ID = "driver_id";
    public static final String COL_PHOTO = "photo";
    
    // Colunas Maintenances
    public static final String COL_VEHICLE_ID = "vehicle_id";
    public static final String COL_TYPE = "type";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_COST = "cost";
    public static final String COL_DATE = "date";
    public static final String COL_MILEAGE_RECORD = "mileage_record";
    public static final String COL_WORKSHOP = "workshop";
    
    // Colunas Fuel Logs
    public static final String COL_LITERS = "liters";
    public static final String COL_VALUE = "value";
    public static final String COL_CURRENT_MILEAGE = "current_mileage";
    public static final String COL_NOTES = "notes";
    
    // Colunas Alerts
    public static final String COL_TITLE = "title";
    public static final String COL_PRIORITY = "priority";
    public static final String COL_DETAILS = "details";
    
    // Colunas Routes
    public static final String COL_START_LOCATION = "start_location";
    public static final String COL_END_LOCATION = "end_location";
    public static final String COL_START_TIME = "start_time";
    public static final String COL_END_TIME = "end_time";
    
    // Colunas Documents
    public static final String COL_FILE_ID = "file_id";
    public static final String COL_EXPIRY_DATE = "expiry_date";
    
    private final SQLiteDatabase database;
    
    public VeiGestBDHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.database = this.getWritableDatabase();
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tabela Users
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_ID + " INTEGER PRIMARY KEY, " +
                COL_USERNAME + " TEXT NOT NULL, " +
                COL_EMAIL + " TEXT, " +
                COL_ROLE + " TEXT, " +
                COL_STATUS + " TEXT, " +
                COL_COMPANY_ID + " INTEGER, " +
                COL_CREATED_AT + " TEXT, " +
                COL_UPDATED_AT + " TEXT" +
                ");";
        db.execSQL(createUsersTable);
        
        // Tabela Vehicles
        String createVehiclesTable = "CREATE TABLE " + TABLE_VEHICLES + " (" +
                COL_ID + " INTEGER PRIMARY KEY, " +
                COL_COMPANY_ID + " INTEGER, " +
                COL_LICENSE_PLATE + " TEXT NOT NULL, " +
                COL_BRAND + " TEXT, " +
                COL_MODEL + " TEXT, " +
                COL_YEAR + " INTEGER, " +
                COL_FUEL_TYPE + " TEXT, " +
                COL_MILEAGE + " INTEGER, " +
                COL_STATUS + " TEXT, " +
                COL_DRIVER_ID + " INTEGER, " +
                COL_PHOTO + " TEXT, " +
                COL_CREATED_AT + " TEXT, " +
                COL_UPDATED_AT + " TEXT" +
                ");";
        db.execSQL(createVehiclesTable);
        
        // Tabela Maintenances
        String createMaintenancesTable = "CREATE TABLE " + TABLE_MAINTENANCES + " (" +
                COL_ID + " INTEGER PRIMARY KEY, " +
                COL_COMPANY_ID + " INTEGER, " +
                COL_VEHICLE_ID + " INTEGER, " +
                COL_TYPE + " TEXT, " +
                COL_DESCRIPTION + " TEXT, " +
                COL_COST + " REAL, " +
                COL_DATE + " TEXT, " +
                COL_MILEAGE_RECORD + " INTEGER, " +
                COL_WORKSHOP + " TEXT, " +
                COL_STATUS + " TEXT, " +
                COL_CREATED_AT + " TEXT, " +
                COL_UPDATED_AT + " TEXT" +
                ");";
        db.execSQL(createMaintenancesTable);
        
        // Tabela Fuel Logs
        String createFuelLogsTable = "CREATE TABLE " + TABLE_FUEL_LOGS + " (" +
                COL_ID + " INTEGER PRIMARY KEY, " +
                COL_VEHICLE_ID + " INTEGER, " +
                COL_LITERS + " REAL, " +
                COL_VALUE + " REAL, " +
                COL_CURRENT_MILEAGE + " INTEGER, " +
                COL_DATE + " TEXT, " +
                COL_NOTES + " TEXT, " +
                COL_CREATED_AT + " TEXT, " +
                COL_UPDATED_AT + " TEXT" +
                ");";
        db.execSQL(createFuelLogsTable);
        
        // Tabela Alerts
        String createAlertsTable = "CREATE TABLE " + TABLE_ALERTS + " (" +
                COL_ID + " INTEGER PRIMARY KEY, " +
                COL_COMPANY_ID + " INTEGER, " +
                COL_TYPE + " TEXT, " +
                COL_TITLE + " TEXT, " +
                COL_DESCRIPTION + " TEXT, " +
                COL_PRIORITY + " TEXT, " +
                COL_STATUS + " TEXT, " +
                COL_DETAILS + " TEXT, " +
                COL_CREATED_AT + " TEXT" +
                ");";
        db.execSQL(createAlertsTable);
        
        // Tabela Documents
        String createDocumentsTable = "CREATE TABLE " + TABLE_DOCUMENTS + " (" +
                COL_ID + " INTEGER PRIMARY KEY, " +
                COL_COMPANY_ID + " INTEGER, " +
                COL_FILE_ID + " INTEGER, " +
                COL_VEHICLE_ID + " INTEGER, " +
                COL_DRIVER_ID + " INTEGER, " +
                COL_TYPE + " TEXT, " +
                COL_EXPIRY_DATE + " TEXT, " +
                COL_NOTES + " TEXT, " +
                COL_CREATED_AT + " TEXT, " +
                COL_UPDATED_AT + " TEXT" +
                ");";
        db.execSQL(createDocumentsTable);
        
        // Tabela Routes
        String createRoutesTable = "CREATE TABLE " + TABLE_ROUTES + " (" +
                COL_ID + " INTEGER PRIMARY KEY, " +
                COL_COMPANY_ID + " INTEGER, " +
                COL_VEHICLE_ID + " INTEGER, " +
                COL_DRIVER_ID + " INTEGER, " +
                COL_START_LOCATION + " TEXT, " +
                COL_END_LOCATION + " TEXT, " +
                COL_START_TIME + " TEXT, " +
                COL_END_TIME + " TEXT, " +
                COL_STATUS + " TEXT, " +
                COL_CREATED_AT + " TEXT, " +
                COL_UPDATED_AT + " TEXT" +
                ");";
        db.execSQL(createRoutesTable);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Apaga tabelas e recria
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VEHICLES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MAINTENANCES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FUEL_LOGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALERTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCUMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTES);
        onCreate(db);
    }
    
    // ==================== USERS ====================
    
    public void adicionarUserBD(User user) {
        ContentValues values = new ContentValues();
        values.put(COL_ID, user.getId());
        values.put(COL_USERNAME, user.getUsername());
        values.put(COL_EMAIL, user.getEmail());
        values.put(COL_ROLE, user.getRole());
        values.put(COL_STATUS, user.getStatus());
        values.put(COL_COMPANY_ID, user.getCompanyId());
        this.database.insertWithOnConflict(TABLE_USERS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }
    
    public User getUserBD(int id) {
        Cursor cursor = this.database.rawQuery(
                "SELECT * FROM " + TABLE_USERS + " WHERE " + COL_ID + " = ?",
                new String[]{String.valueOf(id)});
        
        if (cursor.moveToFirst()) {
            User user = cursorToUser(cursor);
            cursor.close();
            return user;
        }
        cursor.close();
        return null;
    }
    
    public void removerUserBD(int id) {
        this.database.delete(TABLE_USERS, COL_ID + " = ?", new String[]{String.valueOf(id)});
    }
    
    public void removerAllUsersBD() {
        this.database.delete(TABLE_USERS, null, null);
    }
    
    private User cursorToUser(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
        user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(COL_USERNAME)));
        user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL)));
        user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(COL_ROLE)));
        user.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COL_STATUS)));
        user.setCompanyId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_COMPANY_ID)));
        return user;
    }
    
    // ==================== VEHICLES ====================
    
    public void adicionarVehicleBD(Vehicle vehicle) {
        ContentValues values = new ContentValues();
        values.put(COL_ID, vehicle.getId());
        values.put(COL_COMPANY_ID, vehicle.getCompanyId());
        values.put(COL_LICENSE_PLATE, vehicle.getLicensePlate());
        values.put(COL_BRAND, vehicle.getBrand());
        values.put(COL_MODEL, vehicle.getModel());
        values.put(COL_YEAR, vehicle.getYear());
        values.put(COL_FUEL_TYPE, vehicle.getFuelType());
        values.put(COL_MILEAGE, vehicle.getMileage());
        values.put(COL_STATUS, vehicle.getStatus());
        values.put(COL_DRIVER_ID, vehicle.getDriverId());
        values.put(COL_PHOTO, vehicle.getPhoto());
        this.database.insertWithOnConflict(TABLE_VEHICLES, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }
    
    public ArrayList<Vehicle> getAllVehiclesBD() {
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("SELECT * FROM " + TABLE_VEHICLES, null);
        
        if (cursor.moveToFirst()) {
            do {
                vehicles.add(cursorToVehicle(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return vehicles;
    }
    
    public Vehicle getVehicleBD(int id) {
        Cursor cursor = this.database.rawQuery(
                "SELECT * FROM " + TABLE_VEHICLES + " WHERE " + COL_ID + " = ?",
                new String[]{String.valueOf(id)});
        
        if (cursor.moveToFirst()) {
            Vehicle vehicle = cursorToVehicle(cursor);
            cursor.close();
            return vehicle;
        }
        cursor.close();
        return null;
    }
    
    public boolean editarVehicleBD(Vehicle vehicle) {
        ContentValues values = new ContentValues();
        values.put(COL_LICENSE_PLATE, vehicle.getLicensePlate());
        values.put(COL_BRAND, vehicle.getBrand());
        values.put(COL_MODEL, vehicle.getModel());
        values.put(COL_YEAR, vehicle.getYear());
        values.put(COL_FUEL_TYPE, vehicle.getFuelType());
        values.put(COL_MILEAGE, vehicle.getMileage());
        values.put(COL_STATUS, vehicle.getStatus());
        values.put(COL_DRIVER_ID, vehicle.getDriverId());
        values.put(COL_PHOTO, vehicle.getPhoto());
        return this.database.update(TABLE_VEHICLES, values, COL_ID + " = ?", 
                new String[]{String.valueOf(vehicle.getId())}) > 0;
    }
    
    public void removerVehicleBD(int id) {
        this.database.delete(TABLE_VEHICLES, COL_ID + " = ?", new String[]{String.valueOf(id)});
    }
    
    public void removerAllVehiclesBD() {
        this.database.delete(TABLE_VEHICLES, null, null);
    }
    
    private Vehicle cursorToVehicle(Cursor cursor) {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
        vehicle.setCompanyId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_COMPANY_ID)));
        vehicle.setLicensePlate(cursor.getString(cursor.getColumnIndexOrThrow(COL_LICENSE_PLATE)));
        vehicle.setBrand(cursor.getString(cursor.getColumnIndexOrThrow(COL_BRAND)));
        vehicle.setModel(cursor.getString(cursor.getColumnIndexOrThrow(COL_MODEL)));
        vehicle.setYear(cursor.getInt(cursor.getColumnIndexOrThrow(COL_YEAR)));
        vehicle.setFuelType(cursor.getString(cursor.getColumnIndexOrThrow(COL_FUEL_TYPE)));
        vehicle.setMileage(cursor.getInt(cursor.getColumnIndexOrThrow(COL_MILEAGE)));
        vehicle.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COL_STATUS)));
        vehicle.setDriverId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_DRIVER_ID)));
        vehicle.setPhoto(cursor.getString(cursor.getColumnIndexOrThrow(COL_PHOTO)));
        return vehicle;
    }
    
    // ==================== MAINTENANCES ====================
    
    public void adicionarMaintenanceBD(Maintenance maintenance) {
        ContentValues values = new ContentValues();
        values.put(COL_ID, maintenance.getId());
        values.put(COL_COMPANY_ID, maintenance.getCompanyId());
        values.put(COL_VEHICLE_ID, maintenance.getVehicleId());
        values.put(COL_TYPE, maintenance.getType());
        values.put(COL_DESCRIPTION, maintenance.getDescription());
        values.put(COL_COST, maintenance.getCost());
        values.put(COL_DATE, maintenance.getDate());
        values.put(COL_MILEAGE_RECORD, maintenance.getMileageRecord());
        values.put(COL_WORKSHOP, maintenance.getWorkshop());
        values.put(COL_STATUS, maintenance.getStatus());
        this.database.insertWithOnConflict(TABLE_MAINTENANCES, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }
    
    public ArrayList<Maintenance> getAllMaintenancesBD() {
        ArrayList<Maintenance> maintenances = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("SELECT * FROM " + TABLE_MAINTENANCES, null);
        
        if (cursor.moveToFirst()) {
            do {
                maintenances.add(cursorToMaintenance(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return maintenances;
    }
    
    public ArrayList<Maintenance> getMaintenancesByVehicleBD(int vehicleId) {
        ArrayList<Maintenance> maintenances = new ArrayList<>();
        Cursor cursor = this.database.rawQuery(
                "SELECT * FROM " + TABLE_MAINTENANCES + " WHERE " + COL_VEHICLE_ID + " = ?",
                new String[]{String.valueOf(vehicleId)});
        
        if (cursor.moveToFirst()) {
            do {
                maintenances.add(cursorToMaintenance(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return maintenances;
    }
    
    public void removerMaintenanceBD(int id) {
        this.database.delete(TABLE_MAINTENANCES, COL_ID + " = ?", new String[]{String.valueOf(id)});
    }
    
    public void removerAllMaintenancesBD() {
        this.database.delete(TABLE_MAINTENANCES, null, null);
    }
    
    private Maintenance cursorToMaintenance(Cursor cursor) {
        Maintenance maintenance = new Maintenance();
        maintenance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
        maintenance.setCompanyId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_COMPANY_ID)));
        maintenance.setVehicleId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_VEHICLE_ID)));
        maintenance.setType(cursor.getString(cursor.getColumnIndexOrThrow(COL_TYPE)));
        maintenance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION)));
        maintenance.setCost(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_COST)));
        maintenance.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE)));
        maintenance.setMileageRecord(cursor.getInt(cursor.getColumnIndexOrThrow(COL_MILEAGE_RECORD)));
        maintenance.setWorkshop(cursor.getString(cursor.getColumnIndexOrThrow(COL_WORKSHOP)));
        maintenance.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COL_STATUS)));
        return maintenance;
    }
    
    // ==================== FUEL LOGS ====================
    
    public void adicionarFuelLogBD(FuelLog fuelLog) {
        ContentValues values = new ContentValues();
        values.put(COL_ID, fuelLog.getId());
        values.put(COL_VEHICLE_ID, fuelLog.getVehicleId());
        values.put(COL_LITERS, fuelLog.getLiters());
        values.put(COL_VALUE, fuelLog.getValue());
        values.put(COL_CURRENT_MILEAGE, fuelLog.getCurrentMileage());
        values.put(COL_DATE, fuelLog.getDate());
        values.put(COL_NOTES, fuelLog.getNotes());
        this.database.insertWithOnConflict(TABLE_FUEL_LOGS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }
    
    public ArrayList<FuelLog> getAllFuelLogsBD() {
        ArrayList<FuelLog> fuelLogs = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("SELECT * FROM " + TABLE_FUEL_LOGS, null);
        
        if (cursor.moveToFirst()) {
            do {
                fuelLogs.add(cursorToFuelLog(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return fuelLogs;
    }
    
    public ArrayList<FuelLog> getFuelLogsByVehicleBD(int vehicleId) {
        ArrayList<FuelLog> fuelLogs = new ArrayList<>();
        Cursor cursor = this.database.rawQuery(
                "SELECT * FROM " + TABLE_FUEL_LOGS + " WHERE " + COL_VEHICLE_ID + " = ?",
                new String[]{String.valueOf(vehicleId)});
        
        if (cursor.moveToFirst()) {
            do {
                fuelLogs.add(cursorToFuelLog(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return fuelLogs;
    }
    
    public void removerFuelLogBD(int id) {
        this.database.delete(TABLE_FUEL_LOGS, COL_ID + " = ?", new String[]{String.valueOf(id)});
    }
    
    public void removerAllFuelLogsBD() {
        this.database.delete(TABLE_FUEL_LOGS, null, null);
    }
    
    private FuelLog cursorToFuelLog(Cursor cursor) {
        FuelLog fuelLog = new FuelLog();
        fuelLog.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
        fuelLog.setVehicleId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_VEHICLE_ID)));
        fuelLog.setLiters(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_LITERS)));
        fuelLog.setValue(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_VALUE)));
        fuelLog.setCurrentMileage(cursor.getInt(cursor.getColumnIndexOrThrow(COL_CURRENT_MILEAGE)));
        fuelLog.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE)));
        fuelLog.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(COL_NOTES)));
        return fuelLog;
    }
    
    // ==================== ALERTS ====================
    
    public void adicionarAlertBD(Alert alert) {
        ContentValues values = new ContentValues();
        values.put(COL_ID, alert.getId());
        values.put(COL_COMPANY_ID, alert.getCompanyId());
        values.put(COL_TYPE, alert.getType());
        values.put(COL_TITLE, alert.getTitle());
        values.put(COL_DESCRIPTION, alert.getDescription());
        values.put(COL_PRIORITY, alert.getPriority());
        values.put(COL_STATUS, alert.getStatus());
        values.put(COL_DETAILS, alert.getDetails());
        this.database.insertWithOnConflict(TABLE_ALERTS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }
    
    public ArrayList<Alert> getAllAlertsBD() {
        ArrayList<Alert> alerts = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("SELECT * FROM " + TABLE_ALERTS, null);
        
        if (cursor.moveToFirst()) {
            do {
                alerts.add(cursorToAlert(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return alerts;
    }
    
    public void removerAlertBD(int id) {
        this.database.delete(TABLE_ALERTS, COL_ID + " = ?", new String[]{String.valueOf(id)});
    }
    
    public void removerAllAlertsBD() {
        this.database.delete(TABLE_ALERTS, null, null);
    }
    
    private Alert cursorToAlert(Cursor cursor) {
        Alert alert = new Alert();
        alert.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
        alert.setCompanyId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_COMPANY_ID)));
        alert.setType(cursor.getString(cursor.getColumnIndexOrThrow(COL_TYPE)));
        alert.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE)));
        alert.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION)));
        alert.setPriority(cursor.getString(cursor.getColumnIndexOrThrow(COL_PRIORITY)));
        alert.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COL_STATUS)));
        alert.setDetails(cursor.getString(cursor.getColumnIndexOrThrow(COL_DETAILS)));
        return alert;
    }
    
    // ==================== DOCUMENTS ====================
    
    public void adicionarDocumentBD(Document document) {
        ContentValues values = new ContentValues();
        values.put(COL_ID, document.getId());
        values.put(COL_COMPANY_ID, document.getCompanyId());
        values.put(COL_FILE_ID, document.getFileId());
        values.put(COL_VEHICLE_ID, document.getVehicleId());
        values.put(COL_DRIVER_ID, document.getDriverId());
        values.put(COL_TYPE, document.getType());
        values.put(COL_EXPIRY_DATE, document.getExpiryDate());
        values.put(COL_NOTES, document.getNotes());
        this.database.insertWithOnConflict(TABLE_DOCUMENTS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }
    
    public ArrayList<Document> getAllDocumentsBD() {
        ArrayList<Document> documents = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("SELECT * FROM " + TABLE_DOCUMENTS, null);
        
        if (cursor.moveToFirst()) {
            do {
                documents.add(cursorToDocument(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return documents;
    }
    
    public void removerDocumentBD(int id) {
        this.database.delete(TABLE_DOCUMENTS, COL_ID + " = ?", new String[]{String.valueOf(id)});
    }
    
    public void removerAllDocumentsBD() {
        this.database.delete(TABLE_DOCUMENTS, null, null);
    }
    
    private Document cursorToDocument(Cursor cursor) {
        Document document = new Document();
        document.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
        document.setCompanyId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_COMPANY_ID)));
        document.setFileId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_FILE_ID)));
        document.setVehicleId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_VEHICLE_ID)));
        document.setDriverId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_DRIVER_ID)));
        document.setType(cursor.getString(cursor.getColumnIndexOrThrow(COL_TYPE)));
        document.setExpiryDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_EXPIRY_DATE)));
        document.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(COL_NOTES)));
        return document;
    }
    
    // ==================== ROUTES ====================
    
    public void adicionarRouteBD(Route route) {
        ContentValues values = new ContentValues();
        values.put(COL_ID, route.getId());
        values.put(COL_COMPANY_ID, route.getCompanyId());
        values.put(COL_VEHICLE_ID, route.getVehicleId());
        values.put(COL_DRIVER_ID, route.getDriverId());
        values.put(COL_START_LOCATION, route.getStartLocation());
        values.put(COL_END_LOCATION, route.getEndLocation());
        values.put(COL_START_TIME, route.getStartTime());
        values.put(COL_END_TIME, route.getEndTime());
        values.put(COL_STATUS, route.getStatus());
        this.database.insertWithOnConflict(TABLE_ROUTES, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }
    
    public ArrayList<Route> getAllRoutesBD() {
        ArrayList<Route> routes = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("SELECT * FROM " + TABLE_ROUTES, null);
        
        if (cursor.moveToFirst()) {
            do {
                routes.add(cursorToRoute(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return routes;
    }
    
    public void removerRouteBD(int id) {
        this.database.delete(TABLE_ROUTES, COL_ID + " = ?", new String[]{String.valueOf(id)});
    }
    
    public void removerAllRoutesBD() {
        this.database.delete(TABLE_ROUTES, null, null);
    }
    
    private Route cursorToRoute(Cursor cursor) {
        Route route = new Route();
        route.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
        route.setCompanyId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_COMPANY_ID)));
        route.setVehicleId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_VEHICLE_ID)));
        route.setDriverId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_DRIVER_ID)));
        route.setStartLocation(cursor.getString(cursor.getColumnIndexOrThrow(COL_START_LOCATION)));
        route.setEndLocation(cursor.getString(cursor.getColumnIndexOrThrow(COL_END_LOCATION)));
        route.setStartTime(cursor.getString(cursor.getColumnIndexOrThrow(COL_START_TIME)));
        route.setEndTime(cursor.getString(cursor.getColumnIndexOrThrow(COL_END_TIME)));
        route.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COL_STATUS)));
        return route;
    }
    
    // ==================== UTILITÁRIOS ====================
    
    /**
     * Limpa todos os dados da base de dados.
     */
    public void limparTudo() {
        removerAllUsersBD();
        removerAllVehiclesBD();
        removerAllMaintenancesBD();
        removerAllFuelLogsBD();
        removerAllAlertsBD();
        removerAllDocumentsBD();
        removerAllRoutesBD();
    }
}
