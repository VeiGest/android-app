# VeiGest - Persistência de Dados com SQLite
## Base de Dados Local

**ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO - 2025 / 2026**  
**TESP PROGRAMAÇÃO DE SISTEMAS DE INFORMAÇÃO**

---

## 📖 Índice

1. [Introdução ao SQLite](#introdução-ao-sqlite)
2. [SQLiteOpenHelper](#sqliteopenhelper)
3. [VeiGestBDHelper](#veigestbdhelper)
4. [Operações CRUD](#operações-crud)
5. [Tabelas e Estrutura](#tabelas-e-estrutura)
6. [Sincronização com API](#sincronização-com-api)
7. [Boas Práticas](#boas-práticas)

---

## Introdução ao SQLite

O **SQLite** é uma base de dados relacional integrada no Android. No VeiGest é usado para:

- Cache offline de dados da API
- Persistência local do utilizador autenticado
- Armazenamento de dados para funcionamento offline

### Vantagens do SQLite

| Vantagem | Descrição |
|----------|-----------|
| **Integrado** | Não requer servidor externo |
| **Leve** | Poucas centenas de KB |
| **ACID** | Transações atómicas |
| **SQL Standard** | Sintaxe SQL familiar |
| **Offline** | Funciona sem internet |

---

## SQLiteOpenHelper

O `SQLiteOpenHelper` é uma classe auxiliar que facilita a criação e gestão da base de dados.

### Métodos Principais

| Método | Descrição |
|--------|-----------|
| `onCreate(SQLiteDatabase)` | Chamado na primeira criação da BD |
| `onUpgrade(SQLiteDatabase, int, int)` | Chamado quando a versão muda |
| `getWritableDatabase()` | Obtém BD para escrita |
| `getReadableDatabase()` | Obtém BD para leitura |

### Estrutura Base

```java
public class MinhaBDHelper extends SQLiteOpenHelper {
    
    private static final String DB_NAME = "minha_bd";
    private static final int DB_VERSION = 1;
    
    public MinhaBDHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Criar tabelas
        String createTable = "CREATE TABLE tabela (" +
                "id INTEGER PRIMARY KEY, " +
                "nome TEXT NOT NULL" +
                ");";
        db.execSQL(createTable);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Migração de versão
        db.execSQL("DROP TABLE IF EXISTS tabela");
        onCreate(db);
    }
}
```

---

## VeiGestBDHelper

### Estrutura da Classe

```java
package com.veigest.sdk.database;

public class VeiGestBDHelper extends SQLiteOpenHelper {
    
    // Configuração da BD
    private static final String DB_NAME = "veigest_db";
    private static final int DB_VERSION = 1;
    
    // Nomes das Tabelas
    public static final String TABLE_USERS = "users";
    public static final String TABLE_VEHICLES = "vehicles";
    public static final String TABLE_MAINTENANCES = "maintenances";
    public static final String TABLE_FUEL_LOGS = "fuel_logs";
    public static final String TABLE_ALERTS = "alerts";
    public static final String TABLE_DOCUMENTS = "documents";
    public static final String TABLE_ROUTES = "routes";
    
    // Colunas Comuns
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
    
    // Referência à BD
    private final SQLiteDatabase database;
    
    public VeiGestBDHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.database = this.getWritableDatabase();
    }
}
```

### Criação das Tabelas

```java
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
    
    // ... mais tabelas
}

@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // Em produção, fazer migrações incrementais
    // Para desenvolvimento, recria as tabelas
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_VEHICLES);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_MAINTENANCES);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_FUEL_LOGS);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALERTS);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCUMENTS);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTES);
    onCreate(db);
}
```

---

## Operações CRUD

### CREATE (Adicionar)

```java
// Usando ContentValues
public void adicionarUserBD(User user) {
    ContentValues values = new ContentValues();
    values.put(COL_ID, user.getId());
    values.put(COL_USERNAME, user.getUsername());
    values.put(COL_EMAIL, user.getEmail());
    values.put(COL_ROLE, user.getRole());
    values.put(COL_STATUS, user.getStatus());
    values.put(COL_COMPANY_ID, user.getCompanyId());
    
    // insertWithOnConflict para atualizar se já existir
    this.database.insertWithOnConflict(
        TABLE_USERS, 
        null, 
        values, 
        SQLiteDatabase.CONFLICT_REPLACE
    );
}

// Veículos
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
    
    this.database.insertWithOnConflict(
        TABLE_VEHICLES, 
        null, 
        values, 
        SQLiteDatabase.CONFLICT_REPLACE
    );
}
```

### READ (Ler)

```java
// Ler um registo por ID
public User getUserBD(int id) {
    Cursor cursor = this.database.rawQuery(
        "SELECT * FROM " + TABLE_USERS + " WHERE " + COL_ID + " = ?",
        new String[]{String.valueOf(id)}
    );
    
    if (cursor.moveToFirst()) {
        User user = cursorToUser(cursor);
        cursor.close();
        return user;
    }
    cursor.close();
    return null;
}

// Ler todos os registos
public ArrayList<Vehicle> getAllVehiclesBD() {
    ArrayList<Vehicle> vehicles = new ArrayList<>();
    
    Cursor cursor = this.database.rawQuery(
        "SELECT * FROM " + TABLE_VEHICLES, 
        null
    );
    
    if (cursor.moveToFirst()) {
        do {
            vehicles.add(cursorToVehicle(cursor));
        } while (cursor.moveToNext());
    }
    cursor.close();
    
    return vehicles;
}

// Converter Cursor para Objeto
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
```

### UPDATE (Atualizar)

```java
public boolean atualizarVehicleBD(Vehicle vehicle) {
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
    
    int rows = this.database.update(
        TABLE_VEHICLES,
        values,
        COL_ID + " = ?",
        new String[]{String.valueOf(vehicle.getId())}
    );
    
    return rows > 0;
}
```

### DELETE (Remover)

```java
// Remover por ID
public void removerUserBD(int id) {
    this.database.delete(
        TABLE_USERS, 
        COL_ID + " = ?", 
        new String[]{String.valueOf(id)}
    );
}

// Remover todos
public void removerAllUsersBD() {
    this.database.delete(TABLE_USERS, null, null);
}

// Remover com condição
public void removerVehiclesByCompanyBD(int companyId) {
    this.database.delete(
        TABLE_VEHICLES,
        COL_COMPANY_ID + " = ?",
        new String[]{String.valueOf(companyId)}
    );
}
```

---

## Tabelas e Estrutura

### Diagrama das Tabelas

```
┌─────────────────────────────────────────────────────────────────────┐
│                        VEIGEST DATABASE                             │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  ┌─────────────┐      ┌─────────────────┐      ┌────────────────┐  │
│  │   users     │      │    vehicles     │      │  maintenances  │  │
│  ├─────────────┤      ├─────────────────┤      ├────────────────┤  │
│  │ id (PK)     │      │ id (PK)         │──────│ id (PK)        │  │
│  │ username    │      │ company_id      │      │ vehicle_id (FK)│  │
│  │ email       │──────│ driver_id (FK)  │      │ type           │  │
│  │ role        │      │ license_plate   │      │ description    │  │
│  │ status      │      │ brand           │      │ cost           │  │
│  │ company_id  │      │ model           │      │ date           │  │
│  └─────────────┘      │ year            │      │ workshop       │  │
│                       │ fuel_type       │      └────────────────┘  │
│  ┌─────────────┐      │ mileage         │                          │
│  │  fuel_logs  │      │ status          │      ┌────────────────┐  │
│  ├─────────────┤      │ photo           │      │    alerts      │  │
│  │ id (PK)     │──────└─────────────────┘      ├────────────────┤  │
│  │ vehicle_id  │                               │ id (PK)        │  │
│  │ liters      │      ┌─────────────────┐      │ company_id     │  │
│  │ value       │      │   documents     │      │ type           │  │
│  │ mileage     │      ├─────────────────┤      │ title          │  │
│  │ date        │      │ id (PK)         │      │ description    │  │
│  │ notes       │      │ vehicle_id (FK) │      │ priority       │  │
│  └─────────────┘      │ type            │      │ status         │  │
│                       │ expiry_date     │      └────────────────┘  │
│  ┌─────────────┐      │ notes           │                          │
│  │   routes    │      └─────────────────┘                          │
│  ├─────────────┤                                                   │
│  │ id (PK)     │                                                   │
│  │ vehicle_id  │                                                   │
│  │ driver_id   │                                                   │
│  │ start_loc   │                                                   │
│  │ end_loc     │                                                   │
│  │ start_time  │                                                   │
│  │ end_time    │                                                   │
│  │ status      │                                                   │
│  └─────────────┘                                                   │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

### Tipos de Dados SQLite

| Tipo | Uso | Exemplo |
|------|-----|---------|
| `INTEGER` | Números inteiros | id, year, mileage |
| `TEXT` | Strings | username, email, status |
| `REAL` | Números decimais | cost, liters, value |
| `BLOB` | Dados binários | imagens (não usado) |

---

## Sincronização com API

### Fluxo de Sincronização

```
┌─────────────────────────────────────────────────────────────┐
│                    FLUXO DE DADOS                           │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│   ┌─────────┐         ┌─────────────────┐         ┌─────┐  │
│   │ Fragment │◄────────│ SingletonVeiGest │────────►│ API │  │
│   └─────────┘         └─────────────────┘         └─────┘  │
│       │                       │                             │
│       │                       │                             │
│       │                       ▼                             │
│       │               ┌─────────────────┐                   │
│       └──────────────►│ VeiGestBDHelper │                   │
│                       │     (SQLite)    │                   │
│                       └─────────────────┘                   │
│                                                             │
│   1. Fragment pede dados ao Singleton                       │
│   2. Singleton retorna dados do cache local (BD)            │
│   3. Singleton faz requisição à API                         │
│   4. API retorna dados atualizados                          │
│   5. Singleton atualiza BD local                            │
│   6. Singleton notifica Fragment via Listener               │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### Implementação no Singleton

```java
// No SingletonVeiGest.java

public void getAllVeiculosAPI() {
    JsonObjectRequest request = new JsonObjectRequest(
        Request.Method.GET,
        mUrlAPIVehicles,
        null,
        response -> {
            try {
                // Parsear resposta
                ArrayList<Vehicle> vehicles = VeiGestJsonParser.parserJsonVehicles(response);
                
                // Atualizar lista em memória
                veiculos.clear();
                veiculos.addAll(vehicles);
                
                // Persistir na BD local
                if (veiGestBD != null) {
                    for (Vehicle v : vehicles) {
                        veiGestBD.adicionarVehicleBD(v);
                    }
                }
                
                // Notificar listener
                if (veiculosListener != null) {
                    veiculosListener.onRefreshVeiculos(vehicles);
                }
                
            } catch (Exception e) {
                e.printStackTrace();
                if (veiculosListener != null) {
                    veiculosListener.onRefreshVeiculosError("Erro ao processar dados");
                }
            }
        },
        error -> {
            // Em caso de erro, tentar carregar do cache local
            if (veiGestBD != null) {
                ArrayList<Vehicle> cachedVehicles = veiGestBD.getAllVehiclesBD();
                if (!cachedVehicles.isEmpty() && veiculosListener != null) {
                    veiculosListener.onRefreshVeiculos(cachedVehicles);
                    return;
                }
            }
            
            if (veiculosListener != null) {
                veiculosListener.onRefreshVeiculosError("Erro de conexão");
            }
        }
    ) {
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + getToken());
            return headers;
        }
    };
    
    volleyQueue.add(request);
}
```

---

## Boas Práticas

### ✅ Fazer

```java
// Sempre fechar Cursors
Cursor cursor = database.rawQuery(...);
try {
    if (cursor.moveToFirst()) {
        // processar
    }
} finally {
    cursor.close();
}

// Usar constantes para nomes de colunas
public static final String COL_ID = "id";

// Usar insertWithOnConflict para upsert
database.insertWithOnConflict(
    TABLE, null, values, 
    SQLiteDatabase.CONFLICT_REPLACE
);

// Usar transações para operações em lote
database.beginTransaction();
try {
    for (Vehicle v : vehicles) {
        adicionarVehicleBD(v);
    }
    database.setTransactionSuccessful();
} finally {
    database.endTransaction();
}

// Usar getColumnIndexOrThrow para segurança
cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
```

### ❌ Evitar

```java
// Não fechar cursors
Cursor cursor = database.rawQuery(...);
// cursor nunca é fechado - MEMORY LEAK

// Hardcoded strings
database.rawQuery("SELECT * FROM users WHERE id = ?", ...);  // ❌

// SQL injection vulnerável
String query = "SELECT * FROM users WHERE name = '" + name + "'";  // ❌

// Operações pesadas na Main Thread
// Considerar usar AsyncTask ou Coroutines para operações grandes
```

### Queries Comuns

```java
// Busca com filtro
public ArrayList<Vehicle> getVehiclesByStatusBD(String status) {
    ArrayList<Vehicle> vehicles = new ArrayList<>();
    
    Cursor cursor = this.database.rawQuery(
        "SELECT * FROM " + TABLE_VEHICLES + 
        " WHERE " + COL_STATUS + " = ?",
        new String[]{status}
    );
    
    // processar...
    
    return vehicles;
}

// Contagem
public int getVehicleCountBD() {
    Cursor cursor = this.database.rawQuery(
        "SELECT COUNT(*) FROM " + TABLE_VEHICLES,
        null
    );
    
    int count = 0;
    if (cursor.moveToFirst()) {
        count = cursor.getInt(0);
    }
    cursor.close();
    
    return count;
}

// Verificar existência
public boolean vehicleExistsBD(int id) {
    Cursor cursor = this.database.rawQuery(
        "SELECT 1 FROM " + TABLE_VEHICLES + 
        " WHERE " + COL_ID + " = ?",
        new String[]{String.valueOf(id)}
    );
    
    boolean exists = cursor.moveToFirst();
    cursor.close();
    
    return exists;
}
```

---

## 📚 Documentação Relacionada

| Documento | Descrição |
|-----------|-----------|
| [02_VeiGest_SDK.md](02_VeiGest_SDK.md) | Documentação do SDK |
| [07_Integracao_API_REST.md](07_Integracao_API_REST.md) | Integração com API |
| [10_Troubleshooting_Erros_Comuns.md](10_Troubleshooting_Erros_Comuns.md) | Problemas comuns com BD |

---

**Última atualização:** Janeiro 2026
