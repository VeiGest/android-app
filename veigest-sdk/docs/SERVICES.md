# Referência de Serviços

Este documento contém a documentação completa de todos os serviços disponíveis no VeiGest SDK.

---

## Índice

1. [AuthService](#authservice) - Autenticação
2. [VehicleService](#vehicleservice) - Veículos
3. [UserService](#userservice) - Utilizadores
4. [MaintenanceService](#maintenanceservice) - Manutenções
5. [FuelLogService](#fuellogservice) - Abastecimentos
6. [RouteService](#routeservice) - Rotas e GPS
7. [DocumentService](#documentservice) - Documentos
8. [AlertService](#alertservice) - Alertas
9. [ReportService](#reportservice) - Relatórios
10. [CompanyService](#companyservice) - Empresas
11. [TicketService](#ticketservice) - Tickets
12. [FileService](#fileservice) - Ficheiros

---

## AuthService

**Acesso**: `sdk.auth()`

Gerencia autenticação, sessões e tokens.

### Métodos

| Método | Parâmetros | Retorno | Descrição |
|--------|------------|---------|-----------|
| `login` | `String emailOrUsername, String password` | `LoginResponse` | Login com email ou username |
| `loginWithEmail` | `String email, String password` | `LoginResponse` | Login explícito com email |
| `loginWithUsername` | `String username, String password` | `LoginResponse` | Login explícito com username |
| `logout` | `VeiGestCallback<Void>` (opcional) | `Void` | Logout do utilizador |
| `refreshToken` | - | `LoginResponse` | Renovar token de acesso |
| `getCurrentUser` | - | `User` | Obter dados do utilizador atual |
| `me` | - | `User` | Alias para getCurrentUser |
| `isAuthenticated` | - | `boolean` | Verificar se está autenticado |
| `isTokenExpired` | - | `boolean` | Verificar se token expirou |
| `getAccessToken` | - | `String` | Obter token atual |
| `getUserId` | - | `int` | Obter ID do utilizador |
| `getCompanyId` | - | `int` | Obter ID da empresa |

### Exemplos

```java
// Login
sdk.auth().login("user@email.com", "password", new VeiGestCallback<LoginResponse>() {
    @Override
    public void onSuccess(@NonNull LoginResponse response) {
        User user = response.getUser();
        Log.d("Auth", "Logado como: " + user.getNome());
    }
    
    @Override
    public void onError(@NonNull VeiGestException error) {
        Log.e("Auth", "Erro: " + error.getMessage());
    }
});

// Verificar autenticação
if (sdk.auth().isAuthenticated()) {
    int userId = sdk.auth().getUserId();
}

// Logout
sdk.auth().logout();
```

---

## VehicleService

**Acesso**: `sdk.vehicles()`

Gerencia veículos da frota.

### Métodos

| Método | Parâmetros | Retorno | Descrição |
|--------|------------|---------|-----------|
| `list` | - | `List<Vehicle>` | Listar todos os veículos |
| `list` | `companyId, estado, page, limit, sort` | `List<Vehicle>` | Listar com filtros |
| `listActive` | - | `List<Vehicle>` | Listar veículos ativos |
| `listInMaintenance` | - | `List<Vehicle>` | Listar veículos em manutenção |
| `listByStatus` | `String status` | `List<Vehicle>` | Listar por status |
| `get` | `int id` | `Vehicle` | Obter veículo por ID |
| `create` | `matricula, marca, modelo, ano, tipoCombustivel` | `Vehicle` | Criar veículo |
| `create` | `VehicleBuilder builder` | `Vehicle` | Criar com builder |
| `update` | `int id, Map<String, Object> data` | `Vehicle` | Atualizar veículo |
| `patch` | `int id, Map<String, Object> data` | `Vehicle` | Atualizar campos específicos |
| `updateMileage` | `int id, int quilometragem` | `Vehicle` | Atualizar quilometragem |
| `updateStatus` | `int id, String estado` | `Vehicle` | Atualizar estado |
| `delete` | `int id` | `Void` | Eliminar veículo |
| `assignDriver` | `int vehicleId, int driverId` | `Void` | Atribuir condutor |
| `unassignDriver` | `int vehicleId` | `Void` | Remover condutor |
| `getMaintenances` | `int vehicleId` | `List<Maintenance>` | Obter manutenções do veículo |
| `getFuelLogs` | `int vehicleId` | `List<FuelLog>` | Obter abastecimentos do veículo |
| `getStats` | `int vehicleId` | `VehicleStats` | Obter estatísticas do veículo |

### VehicleBuilder

```java
VehicleService.VehicleBuilder builder = new VehicleService.VehicleBuilder()
    .matricula("AA-00-BB")
    .marca("Toyota")
    .modelo("Hilux")
    .ano(2023)
    .tipoCombustivel("diesel")
    .quilometragem(15000)
    .condutorId(5)
    .estado("ativo")
    .companyId(1);

sdk.vehicles().create(builder, callback);
```

### Exemplos

```java
// Listar veículos ativos
sdk.vehicles().listActive(new VeiGestCallback<List<Vehicle>>() {
    @Override
    public void onSuccess(@NonNull List<Vehicle> vehicles) {
        for (Vehicle v : vehicles) {
            Log.d("Vehicle", v.getMatricula() + " - " + v.getMarca() + " " + v.getModelo());
        }
    }
    
    @Override
    public void onError(@NonNull VeiGestException error) {
        Log.e("Error", error.getMessage());
    }
});

// Criar veículo
sdk.vehicles().create(
    "AA-00-BB",    // matrícula
    "Toyota",      // marca
    "Hilux",       // modelo
    2023,          // ano
    "diesel",      // tipo combustível
    callback
);

// Atualizar quilometragem
sdk.vehicles().updateMileage(vehicleId, 125000, callback);

// Obter estatísticas
sdk.vehicles().getStats(vehicleId, new VeiGestCallback<VehicleStats>() {
    @Override
    public void onSuccess(@NonNull VehicleStats stats) {
        double fuelCost = stats.getTotalFuelCost();
        double efficiency = stats.getFuelEfficiency();
    }
});
```

---

## UserService

**Acesso**: `sdk.users()`

Gerencia utilizadores do sistema.

### Métodos

| Método | Parâmetros | Retorno | Descrição |
|--------|------------|---------|-----------|
| `list` | - | `List<User>` | Listar todos os utilizadores |
| `list` | `companyId, page, limit, sort, filter` | `List<User>` | Listar com filtros |
| `listDrivers` | - | `List<User>` | Listar apenas condutores |
| `listByCompany` | `int companyId` | `List<User>` | Listar por empresa |
| `get` | `int id` | `User` | Obter utilizador por ID |
| `getProfile` | - | `User` | Obter perfil do utilizador atual |
| `create` | `nome, email, password` | `User` | Criar utilizador |
| `create` | `UserBuilder builder` | `User` | Criar com builder |
| `update` | `int id, Map<String, Object> data` | `User` | Atualizar utilizador |
| `patch` | `int id, Map<String, Object> data` | `User` | Atualizar campos |
| `updateStatus` | `int id, String estado` | `User` | Atualizar estado |
| `delete` | `int id` | `Void` | Eliminar utilizador |
| `resetPassword` | `int id, String newPassword` | `Void` | Resetar password |

### UserBuilder

```java
UserService.UserBuilder builder = new UserService.UserBuilder()
    .nome("João Silva")
    .email("joao@empresa.com")
    .password("senha123")
    .telefone("+351912345678")
    .numeroCarta("AB123456")
    .validadeCarta("2028-12-31")
    .tipo("condutor")
    .companyId(1);

sdk.users().create(builder, callback);
```

### Exemplos

```java
// Listar condutores
sdk.users().listDrivers(new VeiGestCallback<List<User>>() {
    @Override
    public void onSuccess(@NonNull List<User> drivers) {
        for (User driver : drivers) {
            Log.d("Driver", driver.getNome() + " - " + driver.getEmail());
        }
    }
});

// Obter perfil atual
sdk.users().getProfile(new VeiGestCallback<User>() {
    @Override
    public void onSuccess(@NonNull User user) {
        updateProfileUI(user);
    }
});

// Reset de password
sdk.users().resetPassword(userId, "novaPassword123", callback);
```

---

## MaintenanceService

**Acesso**: `sdk.maintenances()`

Gerencia manutenções de veículos.

### Métodos

| Método | Parâmetros | Retorno | Descrição |
|--------|------------|---------|-----------|
| `list` | - | `List<Maintenance>` | Listar todas as manutenções |
| `list` | `vehicleId, dataInicio, dataFim, page, limit` | `List<Maintenance>` | Listar com filtros |
| `listByVehicle` | `int vehicleId` | `List<Maintenance>` | Listar por veículo |
| `listScheduled` | - | `List<Maintenance>` | Listar agendadas |
| `listCompleted` | - | `List<Maintenance>` | Listar concluídas |
| `listInProgress` | - | `List<Maintenance>` | Listar em progresso |
| `getByVehicle` | `int vehicleId` | `List<Maintenance>` | Obter por veículo (endpoint dedicado) |
| `getByStatus` | `String estado` | `List<Maintenance>` | Obter por estado |
| `get` | `int id` | `Maintenance` | Obter por ID |
| `create` | `vehicleId, tipo, data, custo, descricao, oficina` | `Maintenance` | Criar manutenção |
| `create` | `MaintenanceBuilder builder` | `Maintenance` | Criar com builder |
| `update` | `int id, Map<String, Object> data` | `Maintenance` | Atualizar manutenção |
| `patch` | `int id, Map<String, Object> data` | `Maintenance` | Atualizar campos |
| `delete` | `int id` | `Void` | Eliminar manutenção |
| `schedule` | `id, scheduledDate, priority, assignedTechnician` | `Maintenance` | Agendar manutenção |
| `schedule` | `int id, ScheduleBuilder builder` | `Maintenance` | Agendar com builder |
| `getMonthlyReport` | `Integer month, Integer year` | `MaintenanceReport` | Relatório mensal |
| `getCostsReport` | `vehicleId, startDate, endDate` | `MaintenanceReport` | Relatório de custos |
| `getStats` | - | `ReportStats` | Estatísticas gerais |

### MaintenanceBuilder

```java
MaintenanceService.MaintenanceBuilder builder = new MaintenanceService.MaintenanceBuilder()
    .vehicleId(10)
    .tipo("preventiva")
    .data("2024-12-23")
    .custo(250.00)
    .descricao("Troca de óleo e filtros")
    .oficina("Oficina ABC")
    .kmRegistro(125000);

sdk.maintenances().create(builder, callback);
```

### ScheduleBuilder

```java
MaintenanceService.ScheduleBuilder schedule = new MaintenanceService.ScheduleBuilder()
    .scheduledDate("2024-12-30")
    .priority("alta")
    .assignedTechnician("Carlos Mecânico");

sdk.maintenances().schedule(maintenanceId, schedule, callback);
```

### Exemplos

```java
// Listar manutenções agendadas
sdk.maintenances().listScheduled(new VeiGestCallback<List<Maintenance>>() {
    @Override
    public void onSuccess(@NonNull List<Maintenance> list) {
        for (Maintenance m : list) {
            Log.d("Maintenance", m.getTipo() + " - " + m.getData());
        }
    }
});

// Obter relatório mensal
sdk.maintenances().getMonthlyReport(12, 2024, new VeiGestCallback<MaintenanceReport>() {
    @Override
    public void onSuccess(@NonNull MaintenanceReport report) {
        int total = report.getTotalMaintenances();
        double cost = report.getTotalCost();
    }
});
```

---

## FuelLogService

**Acesso**: `sdk.fuelLogs()`

Gerencia registos de abastecimento.

### Métodos

| Método | Parâmetros | Retorno | Descrição |
|--------|------------|---------|-----------|
| `list` | - | `List<FuelLog>` | Listar todos os abastecimentos |
| `list` | `vehicleId, driverId, dataInicio, dataFim, page, limit` | `List<FuelLog>` | Listar com filtros |
| `listByVehicle` | `int vehicleId` | `List<FuelLog>` | Listar por veículo |
| `listByDriver` | `int driverId` | `List<FuelLog>` | Listar por condutor |
| `getByVehicle` | `int vehicleId` | `List<FuelLog>` | Obter por veículo (endpoint dedicado) |
| `get` | `int id` | `FuelLog` | Obter por ID |
| `create` | `vehicleId, data, litros, valor, kmAtual` | `FuelLog` | Criar abastecimento |
| `create` | `vehicleId, driverId, data, litros, valor, kmAtual, notas` | `FuelLog` | Criar completo |
| `create` | `FuelLogBuilder builder` | `FuelLog` | Criar com builder |
| `update` | `int id, Map<String, Object> data` | `FuelLog` | Atualizar abastecimento |
| `patch` | `int id, Map<String, Object> data` | `FuelLog` | Atualizar campos |
| `delete` | `int id` | `Void` | Eliminar abastecimento |
| `getStats` | - | `ReportStats` | Obter estatísticas |
| `getStats` | `vehicleId, startDate, endDate` | `ReportStats` | Estatísticas com filtros |
| `getAlerts` | - | `List<FuelAlert>` | Obter alertas de combustível |
| `getEfficiencyReport` | - | `FuelEfficiencyReport` | Relatório de eficiência |
| `getEfficiencyReport` | `startDate, endDate` | `FuelEfficiencyReport` | Relatório por período |

### FuelLogBuilder

```java
FuelLogService.FuelLogBuilder builder = new FuelLogService.FuelLogBuilder()
    .vehicleId(10)
    .driverId(5)
    .data("2024-12-23")
    .litros(45.5)
    .valor(68.25)
    .kmAtual(125500)
    .notas("Abastecimento completo")
    .local("Posto Shell Centro")
    .precoPorLitro(1.50);

sdk.fuelLogs().create(builder, callback);
```

### Exemplos

```java
// Registar abastecimento rápido
sdk.fuelLogs().create(
    vehicleId,      // ID do veículo
    "2024-12-23",   // Data
    45.5,           // Litros
    68.25,          // Valor (€)
    125500,         // Km atual
    callback
);

// Obter alertas de combustível
sdk.fuelLogs().getAlerts(new VeiGestCallback<List<FuelAlert>>() {
    @Override
    public void onSuccess(@NonNull List<FuelAlert> alerts) {
        for (FuelAlert alert : alerts) {
            if (alert.isHighPriority()) {
                showUrgentAlert(alert);
            }
        }
    }
});

// Relatório de eficiência
sdk.fuelLogs().getEfficiencyReport(new VeiGestCallback<FuelEfficiencyReport>() {
    @Override
    public void onSuccess(@NonNull FuelEfficiencyReport report) {
        FuelEfficiencyReport.Summary summary = report.getSummary();
        double avgConsumption = summary.getAverageConsumption();
        double totalCost = summary.getTotalCost();
    }
});
```

---

## RouteService

**Acesso**: `sdk.routes()`

Gerencia rotas e tracking GPS.

### Métodos

| Método | Parâmetros | Retorno | Descrição |
|--------|------------|---------|-----------|
| `list` | - | `List<Route>` | Listar todas as rotas |
| `list` | `vehicleId, driverId, status, dataInicio, dataFim, page, limit` | `List<Route>` | Listar com filtros |
| `listByVehicle` | `int vehicleId` | `List<Route>` | Listar por veículo |
| `listByDriver` | `int driverId` | `List<Route>` | Listar por condutor |
| `listInProgress` | - | `List<Route>` | Listar em andamento |
| `listCompleted` | - | `List<Route>` | Listar concluídas |
| `get` | `int id` | `Route` | Obter por ID |
| `start` | `vehicleId, driverId, origem, destino, kmInicial` | `Route` | Iniciar nova rota |
| `create` | `RouteBuilder builder` | `Route` | Criar com builder |
| `update` | `int id, Map<String, Object> data` | `Route` | Atualizar rota |
| `finish` | `int id, int kmFinal, String notas` | `Route` | Finalizar rota |
| `cancel` | `int id, String notas` | `Route` | Cancelar rota |
| `delete` | `int id` | `Void` | Eliminar rota |
| `getGpsEntries` | `int routeId` | `List<GpsEntry>` | Obter pontos GPS |
| `getGpsEntries` | `routeId, page, limit` | `List<GpsEntry>` | Pontos GPS com paginação |
| `addGpsEntry` | `routeId, latitude, longitude, velocidade, altitude` | `GpsEntry` | Adicionar ponto GPS |
| `addGpsEntry` | `GpsEntry entry` | `GpsEntry` | Adicionar ponto GPS (objeto) |
| `addGpsEntriesBatch` | `routeId, List<GpsEntry> entries` | `Void` | Adicionar múltiplos pontos |

### RouteBuilder

```java
RouteService.RouteBuilder builder = new RouteService.RouteBuilder()
    .vehicleId(10)
    .driverId(5)
    .origem("Lisboa")
    .destino("Porto")
    .kmInicial(125000)
    .notas("Entrega urgente");

sdk.routes().create(builder, callback);
```

### Exemplos

```java
// Iniciar rota
sdk.routes().start(
    vehicleId,      // ID do veículo
    driverId,       // ID do condutor
    "Lisboa",       // Origem
    "Porto",        // Destino
    125000,         // Km inicial
    new VeiGestCallback<Route>() {
        @Override
        public void onSuccess(@NonNull Route route) {
            currentRouteId = route.getId();
            startGpsTracking();
        }
    }
);

// Adicionar ponto GPS
sdk.routes().addGpsEntry(
    routeId,
    38.7223,    // Latitude
    -9.1393,    // Longitude
    60.0,       // Velocidade (km/h)
    50.0,       // Altitude (m)
    callback
);

// Finalizar rota
sdk.routes().finish(routeId, 125350, "Entrega concluída sem incidentes", callback);
```

---

## DocumentService

**Acesso**: `sdk.documents()`

Gerencia documentos e validades.

### Métodos

| Método | Parâmetros | Retorno | Descrição |
|--------|------------|---------|-----------|
| `list` | - | `List<Document>` | Listar todos os documentos |
| `list` | `vehicleId, driverId, tipo, status, page, limit` | `List<Document>` | Listar com filtros |
| `listByVehicle` | `int vehicleId` | `List<Document>` | Listar por veículo |
| `listByDriver` | `int driverId` | `List<Document>` | Listar por condutor |
| `listByType` | `String tipo` | `List<Document>` | Listar por tipo |
| `listValid` | - | `List<Document>` | Listar válidos |
| `listExpired` | - | `List<Document>` | Listar expirados |
| `listExpiring` | `Integer dias` | `List<Document>` | Listar a expirar (30 dias default) |
| `get` | `int id` | `Document` | Obter por ID |
| `create` | `tipo, dataValidade, vehicleId, driverId, notas` | `Document` | Criar documento |
| `create` | `DocumentBuilder builder` | `Document` | Criar com builder |
| `update` | `int id, Map<String, Object> data` | `Document` | Atualizar documento |
| `patch` | `int id, Map<String, Object> data` | `Document` | Atualizar campos |
| `updateStatus` | `int id, String status` | `Document` | Atualizar status |
| `delete` | `int id` | `Void` | Eliminar documento |

### DocumentBuilder

```java
DocumentService.DocumentBuilder builder = new DocumentService.DocumentBuilder()
    .tipo("seguro")
    .dataValidade("2025-12-31")
    .vehicleId(10)
    .fileId(5)
    .companyId(1)
    .notas("Seguro contra todos os riscos");

sdk.documents().create(builder, callback);
```

### Exemplos

```java
// Listar documentos a expirar em 15 dias
sdk.documents().listExpiring(15, new VeiGestCallback<List<Document>>() {
    @Override
    public void onSuccess(@NonNull List<Document> docs) {
        for (Document doc : docs) {
            Log.w("Expiring", doc.getTipo() + " expira em " + doc.getDiasParaVencimento() + " dias");
        }
    }
});

// Criar documento de seguro
sdk.documents().create(
    "seguro",           // Tipo
    "2025-12-31",       // Data validade
    vehicleId,          // ID do veículo
    null,               // ID do condutor (null se for de veículo)
    "Seguro completo",  // Notas
    callback
);
```

---

## AlertService

**Acesso**: `sdk.alerts()`

Gerencia alertas do sistema.

### Métodos

| Método | Parâmetros | Retorno | Descrição |
|--------|------------|---------|-----------|
| `list` | - | `List<Alert>` | Listar todos os alertas |
| `list` | `tipo, status, prioridade, page, limit` | `List<Alert>` | Listar com filtros |
| `listActive` | - | `List<Alert>` | Listar alertas ativos |
| `listByPriority` | `String prioridade` | `List<Alert>` | Listar por prioridade |
| `listHighPriority` | - | `List<Alert>` | Listar alta prioridade |
| `get` | `int id` | `Alert` | Obter por ID |
| `create` | `AlertBuilder builder` | `Alert` | Criar alerta |
| `update` | `int id, Map<String, Object> data` | `Alert` | Atualizar alerta |
| `resolve` | `int id, String notas` | `Alert` | Resolver alerta |
| `ignore` | `int id, String motivo` | `Alert` | Ignorar alerta |
| `delete` | `int id` | `Void` | Eliminar alerta |

### Exemplos

```java
// Listar alertas de alta prioridade
sdk.alerts().listHighPriority(new VeiGestCallback<List<Alert>>() {
    @Override
    public void onSuccess(@NonNull List<Alert> alerts) {
        for (Alert alert : alerts) {
            showNotification(alert.getTitulo(), alert.getDescricao());
        }
    }
});

// Resolver alerta
sdk.alerts().resolve(alertId, "Problema resolvido pelo técnico", callback);
```

---

## ReportService

**Acesso**: `sdk.reports()`

Gera relatórios e estatísticas.

### Métodos

| Método | Parâmetros | Retorno | Descrição |
|--------|------------|---------|-----------|
| `getCompanyStats` | - | `ReportStats` | Estatísticas da empresa |
| `getCompanyStats` | `Integer companyId` | `ReportStats` | Estatísticas de empresa específica |
| `getVehicleCosts` | - | `List<ReportStats>` | Custos de todos os veículos |
| `getVehicleCosts` | `Integer vehicleId` | `ReportStats` | Custos de veículo específico |
| `getFuelConsumption` | `String periodo` | `ReportStats` | Consumo de combustível |
| `getDriverPerformance` | - | `List<ReportStats>` | Performance dos condutores |

### Exemplos

```java
// Estatísticas da empresa
sdk.reports().getCompanyStats(new VeiGestCallback<ReportStats>() {
    @Override
    public void onSuccess(@NonNull ReportStats stats) {
        int totalVehicles = stats.getTotalVehicles();
        int totalDrivers = stats.getTotalDrivers();
        int totalUsers = stats.getTotalUsers();
    }
});

// Custos por veículo
sdk.reports().getVehicleCosts(new VeiGestCallback<List<ReportStats>>() {
    @Override
    public void onSuccess(@NonNull List<ReportStats> costs) {
        for (ReportStats cost : costs) {
            Log.d("Costs", cost.getMatricula() + ": €" + cost.getTotalCosts());
        }
    }
});
```

---

## CompanyService

**Acesso**: `sdk.companies()`

Gerencia empresas.

### Métodos

| Método | Parâmetros | Retorno | Descrição |
|--------|------------|---------|-----------|
| `list` | - | `List<Company>` | Listar todas as empresas |
| `list` | `page, limit, sort, filter` | `List<Company>` | Listar com filtros |
| `get` | `int id` | `Company` | Obter por ID |
| `create` | `CompanyBuilder builder` | `Company` | Criar empresa |
| `update` | `int id, Map<String, Object> data` | `Company` | Atualizar empresa |
| `patch` | `int id, Map<String, Object> data` | `Company` | Atualizar campos |
| `delete` | `int id` | `Void` | Eliminar empresa |
| `getVehicles` | `int companyId` | `List<Vehicle>` | Obter veículos da empresa |
| `getUsers` | `int companyId` | `List<User>` | Obter utilizadores da empresa |
| `getStats` | `int companyId` | `CompanyStats` | Obter estatísticas da empresa |

### Exemplos

```java
// Obter estatísticas da empresa
sdk.companies().getStats(companyId, new VeiGestCallback<CompanyStats>() {
    @Override
    public void onSuccess(@NonNull CompanyStats stats) {
        int vehicles = stats.getVehiclesCount();
        int users = stats.getUsersCount();
        int drivers = stats.getDriversCount();
    }
});
```

---

## TicketService

**Acesso**: `sdk.tickets()`

Gerencia tickets de suporte.

### Métodos

| Método | Parâmetros | Retorno | Descrição |
|--------|------------|---------|-----------|
| `list` | - | `List<Ticket>` | Listar todos os tickets |
| `list` | `status, tipo, prioridade, vehicleId, page, limit` | `List<Ticket>` | Listar com filtros |
| `listPending` | - | `List<Ticket>` | Listar pendentes |
| `listOpen` | - | `List<Ticket>` | Listar abertos |
| `listHighPriority` | - | `List<Ticket>` | Listar alta prioridade |
| `listByVehicle` | `int vehicleId` | `List<Ticket>` | Listar por veículo |
| `get` | `int id` | `Ticket` | Obter por ID |
| `create` | `titulo, descricao, tipo, prioridade` | `Ticket` | Criar ticket |
| `create` | `TicketBuilder builder` | `Ticket` | Criar com builder |
| `update` | `int id, Map<String, Object> data` | `Ticket` | Atualizar ticket |
| `cancel` | `int id, String motivo` | `Ticket` | Cancelar ticket |
| `complete` | `int id, String observacoes` | `Ticket` | Completar ticket |
| `delete` | `int id` | `Void` | Eliminar ticket |

### TicketBuilder

```java
TicketService.TicketBuilder builder = new TicketService.TicketBuilder()
    .titulo("GPS não funciona")
    .descricao("O GPS do veículo parou de enviar localização")
    .tipo("tecnico")
    .prioridade("alta")
    .vehicleId(10)
    .driverId(5);

sdk.tickets().create(builder, callback);
```

### Exemplos

```java
// Criar ticket
sdk.tickets().create(
    "Problema no GPS",
    "Veículo AA-00-BB sem sinal GPS",
    "tecnico",
    "alta",
    callback
);

// Completar ticket
sdk.tickets().complete(ticketId, "Antena GPS substituída", callback);
```

---

## FileService

**Acesso**: `sdk.files()`

Gerencia upload e download de ficheiros.

### Métodos

| Método | Parâmetros | Retorno | Descrição |
|--------|------------|---------|-----------|
| `list` | - | `List<FileInfo>` | Listar todos os ficheiros |
| `list` | `tipo, page, limit` | `List<FileInfo>` | Listar com filtros |
| `listImages` | - | `List<FileInfo>` | Listar apenas imagens |
| `listPdfs` | - | `List<FileInfo>` | Listar apenas PDFs |
| `get` | `int id` | `FileInfo` | Obter informações do ficheiro |
| `upload` | `File file, String nome, String tipo` | `FileInfo` | Upload de ficheiro |
| `upload` | `byte[] bytes, fileName, mimeType, tipo` | `FileInfo` | Upload de bytes |
| `download` | `int id, File destinationFile` | `File` | Download para ficheiro |
| `downloadTo` | `id, File destinationDir, String fileName` | `File` | Download para pasta |
| `delete` | `int id` | `Void` | Eliminar ficheiro |

### Exemplos

```java
// Upload de ficheiro
File file = new File(filePath);
sdk.files().upload(file, "documento.pdf", "documento", new VeiGestCallback<FileInfo>() {
    @Override
    public void onSuccess(@NonNull FileInfo info) {
        Log.d("Upload", "Ficheiro enviado: " + info.getName());
        Log.d("Upload", "Tamanho: " + info.getFormattedSize());
    }
});

// Download de ficheiro
File destFile = new File(getExternalFilesDir(null), "download.pdf");
sdk.files().download(fileId, destFile, new VeiGestCallback<File>() {
    @Override
    public void onSuccess(@NonNull File file) {
        Log.d("Download", "Ficheiro salvo em: " + file.getAbsolutePath());
    }
});
```

---

## Próximos Passos

- [Referência de Modelos](MODELS.md)
- [Padrões Builder](BUILDERS.md)
- [Tratamento de Erros](EXCEPTIONS.md)
- [Upload de Ficheiros](FILE_UPLOAD.md)
