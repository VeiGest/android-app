# Referência de Modelos

Este documento descreve todos os modelos de dados disponíveis no VeiGest SDK.

---

## Índice

1. [ApiResponse](#apiresponse) - Resposta base da API
2. [User](#user) - Utilizadores
3. [Vehicle](#vehicle) - Veículos
4. [Company](#company) - Empresas
5. [Maintenance](#maintenance) - Manutenções
6. [FuelLog](#fuellog) - Abastecimentos
7. [Route](#route) - Rotas
8. [GpsEntry](#gpsentry) - Pontos GPS
9. [Document](#document) - Documentos
10. [Alert](#alert) - Alertas
11. [Ticket](#ticket) - Tickets
12. [FileInfo](#fileinfo) - Ficheiros
13. [LoginResponse](#loginresponse) - Resposta de Login
14. [ReportStats](#reportstats) - Estatísticas
15. [VehicleStats](#vehiclestats) - Estatísticas de Veículo
16. [CompanyStats](#companystats) - Estatísticas de Empresa
17. [MaintenanceReport](#maintenancereport) - Relatório de Manutenções
18. [FuelEfficiencyReport](#fuelefficiencyreport) - Relatório de Eficiência
19. [FuelAlert](#fuelalert) - Alertas de Combustível

---

## ApiResponse

Wrapper genérico para todas as respostas da API.

```java
public class ApiResponse<T> {
    T getData();                    // Dados da resposta
    boolean isSuccess();            // Se foi bem sucedido
    String getMessage();            // Mensagem (se houver)
    Pagination getPagination();     // Info de paginação
    ApiError getError();            // Detalhes do erro
}
```

### Pagination

```java
public class Pagination {
    int getCurrentPage();   // Página atual
    int getLastPage();      // Última página
    int getPerPage();       // Itens por página
    int getTotal();         // Total de itens
    boolean hasMorePages(); // Se há mais páginas
}
```

### ApiError

```java
public class ApiError {
    String getMessage();                        // Mensagem de erro
    String getCode();                           // Código de erro
    Map<String, List<String>> getErrors();      // Erros de validação
}
```

---

## User

Representa um utilizador do sistema.

### Campos

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | int | ID único |
| `companyId` | int | ID da empresa |
| `nome` / `name` | String | Nome completo |
| `email` | String | Email |
| `telefone` | String | Telefone |
| `estado` | String | Estado: "ativo", "inativo" |
| `numeroCarta` | String | Número da carta de condução |
| `validadeCarta` | String | Validade da carta (data) |
| `roles` | List&lt;String&gt; | Papéis do utilizador |
| `permissions` | List&lt;String&gt; | Permissões |
| `tipo` | String | Tipo: "admin", "gestor", "condutor" |
| `createdAt` | String | Data de criação |

### Métodos

```java
int getId();
int getCompanyId();
String getNome();               // Retorna nome ou name
String getEmail();
String getTelefone();
String getEstado();
String getNumeroCarta();
String getValidadeCarta();
List<String> getRoles();
List<String> getPermissions();
String getTipo();
String getCreatedAt();

// Métodos auxiliares
boolean isActive();             // estado == "ativo"
boolean isDriver();             // tipo == "condutor" ou roles contém "driver"
boolean hasRole(String role);   // Verifica se tem um role específico
```

### Exemplo

```java
sdk.auth().getCurrentUser(new VeiGestCallback<User>() {
    @Override
    public void onSuccess(@NonNull User user) {
        Log.d("User", "Nome: " + user.getNome());
        Log.d("User", "Email: " + user.getEmail());
        Log.d("User", "Empresa: " + user.getCompanyId());
        Log.d("User", "Ativo: " + user.isActive());
        Log.d("User", "É condutor: " + user.isDriver());
        
        if (user.hasRole("admin")) {
            showAdminMenu();
        }
    }
});
```

---

## Vehicle

Representa um veículo da frota.

### Campos

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | int | ID único |
| `companyId` | int | ID da empresa |
| `matricula` / `licensePlate` | String | Matrícula |
| `marca` / `brand` | String | Marca |
| `modelo` / `model` | String | Modelo |
| `ano` / `year` | int | Ano de fabrico |
| `tipoCombustivel` / `fuelType` | String | Tipo de combustível |
| `quilometragem` / `mileage` | int | Quilometragem atual |
| `estado` / `status` | String | Estado: "ativo", "inativo", "manutencao" |
| `condutorId` / `driverId` | Integer | ID do condutor atribuído |
| `createdAt` | String | Data de criação |

### Métodos

```java
int getId();
int getCompanyId();
String getMatricula();          // Retorna matricula ou licensePlate
String getMarca();              // Retorna marca ou brand
String getModelo();             // Retorna modelo ou model
int getAno();                   // Retorna ano ou year
String getTipoCombustivel();    // Retorna tipoCombustivel ou fuelType
int getQuilometragem();         // Retorna quilometragem ou mileage
String getEstado();             // Retorna estado ou status
Integer getCondutorId();        // Retorna condutorId ou driverId
String getCreatedAt();

// Métodos auxiliares
boolean isActive();             // estado == "ativo"
boolean isInMaintenance();      // estado == "manutencao"
boolean hasDriver();            // condutorId != null
String getDisplayName();        // Retorna "marca modelo (matricula)"
```

### Exemplo

```java
sdk.vehicles().list(new VeiGestCallback<List<Vehicle>>() {
    @Override
    public void onSuccess(@NonNull List<Vehicle> vehicles) {
        for (Vehicle v : vehicles) {
            Log.d("Vehicle", v.getDisplayName());
            Log.d("Vehicle", "Km: " + v.getQuilometragem());
            Log.d("Vehicle", "Estado: " + v.getEstado());
            
            if (v.isInMaintenance()) {
                Log.w("Vehicle", "Veículo em manutenção!");
            }
        }
    }
});
```

---

## Company

Representa uma empresa no sistema.

### Campos

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | int | ID único |
| `code` | String | Código da empresa |
| `nome` / `name` | String | Nome da empresa |
| `nif` | String | NIF/NIPC |
| `email` | String | Email de contacto |
| `telefone` | String | Telefone |
| `morada` | String | Morada |
| `estado` / `status` | String | Estado: "ativo", "inativo" |
| `plano` / `plan` | String | Plano contratado |
| `configuracoes` | Object | Configurações |
| `createdAt` | String | Data de criação |

### Métodos

```java
int getId();
String getCode();
String getNome();
String getNif();
String getEmail();
String getTelefone();
String getMorada();
String getEstado();
String getPlano();
Object getConfiguracoes();
String getCreatedAt();

// Métodos auxiliares
boolean isActive();
```

---

## Maintenance

Representa uma manutenção de veículo.

### Campos

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | int | ID único |
| `companyId` | int | ID da empresa |
| `vehicleId` | int | ID do veículo |
| `tipo` / `type` | String | Tipo: "preventiva", "corretiva", "revisao" |
| `descricao` / `description` | String | Descrição |
| `data` / `date` | String | Data da manutenção |
| `custo` / `cost` | double | Custo |
| `kmRegistro` / `kmRecord` | int | Km no momento |
| `proximaData` / `nextDate` | String | Próxima manutenção |
| `status` | String | Estado: "agendada", "em_progresso", "concluida", "cancelada" |
| `createdAt` | String | Data de criação |

### Métodos

```java
int getId();
int getCompanyId();
int getVehicleId();
String getTipo();
String getDescricao();
String getData();
double getCusto();
int getKmRegistro();
String getProximaData();
String getStatus();
String getCreatedAt();

// Métodos auxiliares
boolean isScheduled();      // status == "agendada"
boolean isCompleted();      // status == "concluida"
boolean isInProgress();     // status == "em_progresso"
```

---

## FuelLog

Representa um registo de abastecimento.

### Campos

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | int | ID único |
| `companyId` | int | ID da empresa |
| `vehicleId` | int | ID do veículo |
| `driverId` | Integer | ID do condutor |
| `data` / `date` | String | Data do abastecimento |
| `litros` / `liters` | double | Quantidade em litros |
| `valor` / `value` | double | Valor total |
| `precoLitro` / `pricePerLiter` | double | Preço por litro |
| `kmAtual` / `currentKm` | int | Quilometragem |
| `notas` / `notes` | String | Observações |

### Métodos

```java
int getId();
int getCompanyId();
int getVehicleId();
Integer getDriverId();
String getData();
double getLitros();
double getValor();
double getPrecoLitro();
int getKmAtual();
String getNotas();

// Métodos auxiliares
double getCalculatedPricePerLiter();  // valor / litros
```

---

## Route

Representa uma rota/viagem.

### Campos

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | int | ID único |
| `companyId` | int | ID da empresa |
| `vehicleId` | int | ID do veículo |
| `driverId` | int | ID do condutor |
| `inicio` / `startTime` | String | Data/hora de início |
| `fim` / `endTime` | String | Data/hora de fim |
| `kmInicial` / `startKm` | int | Km inicial |
| `kmFinal` / `endKm` | int | Km final |
| `origem` / `origin` | String | Local de origem |
| `destino` / `destination` | String | Local de destino |
| `status` | String | Estado: "em_andamento", "concluida", "cancelada" |
| `notas` / `notes` | String | Observações |

### Métodos

```java
int getId();
int getCompanyId();
int getVehicleId();
int getDriverId();
String getInicio();
String getFim();
int getKmInicial();
int getKmFinal();
String getOrigem();
String getDestino();
String getStatus();
String getNotas();

// Métodos auxiliares
boolean isInProgress();     // status == "em_andamento"
boolean isCompleted();      // status == "concluida"
int getDistanceTraveled();  // kmFinal - kmInicial
```

---

## GpsEntry

Representa um ponto GPS de uma rota.

### Campos

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | int | ID único |
| `routeId` | int | ID da rota |
| `latitude` | double | Latitude |
| `longitude` | double | Longitude |
| `timestamp` | String | Data/hora |
| `velocidade` / `speed` | double | Velocidade (km/h) |
| `altitude` | double | Altitude (metros) |
| `precisao` / `accuracy` | double | Precisão (metros) |

### Métodos

```java
int getId();
int getRouteId();
double getLatitude();
double getLongitude();
String getTimestamp();
double getVelocidade();
double getAltitude();
double getPrecisao();
```

---

## Document

Representa um documento do sistema.

### Campos

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | int | ID único |
| `companyId` | int | ID da empresa |
| `fileId` | Integer | ID do ficheiro associado |
| `vehicleId` | Integer | ID do veículo |
| `driverId` | Integer | ID do condutor |
| `tipo` / `type` | String | Tipo: "seguro", "inspecao", "licenca", etc. |
| `dataValidade` / `expiryDate` | String | Data de validade |
| `status` | String | Estado: "valido", "expirado", "a_expirar" |
| `notas` / `notes` | String | Observações |
| `diasParaVencimento` | Integer | Dias até vencer |

### Métodos

```java
int getId();
int getCompanyId();
Integer getFileId();
Integer getVehicleId();
Integer getDriverId();
String getTipo();
String getDataValidade();
String getStatus();
String getNotas();
Integer getDiasParaVencimento();

// Métodos auxiliares
boolean isValid();          // status == "valido"
boolean isExpired();        // status == "expirado"
boolean isExpiring();       // status == "a_expirar"
boolean isVehicleDocument();    // vehicleId != null
boolean isDriverDocument();     // driverId != null
```

---

## Alert

Representa um alerta do sistema.

### Campos

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | int | ID único |
| `companyId` | int | ID da empresa |
| `tipo` / `type` | String | Tipo de alerta |
| `titulo` / `title` | String | Título |
| `descricao` / `description` | String | Descrição |
| `prioridade` / `priority` | String | Prioridade: "alta", "media", "baixa" |
| `status` | String | Estado: "ativo", "resolvido", "ignorado" |
| `detalhes` / `details` | Map | Detalhes adicionais |

### Métodos

```java
int getId();
int getCompanyId();
String getTipo();
String getTitulo();
String getDescricao();
String getPrioridade();
String getStatus();
Map<String, Object> getDetalhes();

// Métodos auxiliares
boolean isActive();         // status == "ativo"
boolean isHighPriority();   // prioridade == "alta"
boolean isResolved();       // status == "resolvido"
```

---

## Ticket

Representa um ticket de suporte.

### Campos

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | int | ID único |
| `companyId` | int | ID da empresa |
| `vehicleId` | Integer | ID do veículo |
| `driverId` | Integer | ID do condutor |
| `routeId` | Integer | ID da rota |
| `titulo` / `title` | String | Título |
| `descricao` / `description` | String | Descrição |
| `tipo` / `type` | String | Tipo: "tecnico", "suporte", "reclamacao" |
| `prioridade` / `priority` | String | Prioridade: "alta", "media", "baixa" |
| `estado` / `status` | String | Estado: "aberto", "pendente", "concluido", "cancelado" |
| `observacoes` / `notes` | String | Observações |
| `createdAt` | String | Data de criação |

### Métodos

```java
int getId();
int getCompanyId();
Integer getVehicleId();
Integer getDriverId();
Integer getRouteId();
String getTitulo();
String getDescricao();
String getTipo();
String getPrioridade();
String getEstado();
String getObservacoes();
String getCreatedAt();

// Métodos auxiliares
boolean isPending();        // estado == "pendente"
boolean isCompleted();      // estado == "concluido"
boolean isCancelled();      // estado == "cancelado"
boolean isHighPriority();   // prioridade == "alta"
```

---

## FileInfo

Informações sobre um ficheiro.

### Campos

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | int | ID único |
| `companyId` | int | ID da empresa |
| `nome` / `name` | String | Nome do ficheiro |
| `nomeOriginal` / `originalName` | String | Nome original |
| `tipo` / `mimeType` | String | Tipo MIME |
| `tamanho` / `size` | long | Tamanho em bytes |
| `caminho` / `path` | String | Caminho no servidor |
| `url` | String | URL de acesso |
| `downloadUrl` | String | URL de download |
| `createdAt` | String | Data de criação |

### Métodos

```java
int getId();
int getCompanyId();
String getNome();
String getNomeOriginal();
String getTipo();
long getTamanho();
String getCaminho();
String getUrl();
String getDownloadUrl();
String getCreatedAt();

// Métodos auxiliares
String getFormattedSize();  // Ex: "1.5 MB", "500 KB"
boolean isImage();          // tipo começa com "image/"
boolean isPdf();            // tipo == "application/pdf"
```

---

## LoginResponse

Resposta de login da API.

### Campos

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `accessToken` / `token` | String | Token de acesso |
| `tokenType` | String | Tipo do token ("Bearer") |
| `expiresIn` | int | Segundos até expirar |
| `refreshToken` | String | Token de renovação |
| `user` | User | Dados do utilizador |

### Métodos

```java
String getAccessToken();
String getTokenType();
int getExpiresIn();
String getRefreshToken();
User getUser();
```

---

## ReportStats

Estatísticas de relatórios.

### Campos

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `totalUsers` | int | Total de utilizadores |
| `totalVehicles` | int | Total de veículos |
| `totalDrivers` | int | Total de condutores |
| `totalStorageBytes` | long | Armazenamento usado |
| `vehicleId` | Integer | ID do veículo (se específico) |
| `matricula` | String | Matrícula |
| `totalMaintenance` | double | Custo total manutenção |
| `totalFuel` | double | Custo total combustível |
| `totalCosts` | double | Custo total |
| `periodo` | String | Período do relatório |
| `totalLitros` | double | Total de litros |
| `consumoMedio` | double | Consumo médio |

### Métodos

```java
int getTotalUsers();
int getTotalVehicles();
int getTotalDrivers();
long getTotalStorageBytes();
Integer getVehicleId();
String getMatricula();
double getTotalMaintenance();
double getTotalFuel();
double getTotalCosts();
String getPeriodo();
double getTotalLitros();
double getConsumoMedio();

// Métodos auxiliares
String getFormattedStorage();   // Ex: "1.5 GB"
```

---

## VehicleStats

Estatísticas detalhadas de um veículo.

### Estrutura

```java
public class VehicleStats {
    Vehicle vehicle;
    int vehicleId;
    String matricula;
    
    // Combustível
    FuelStats fuelStats;
    double totalFuelCost;
    double totalLiters;
    double fuelEfficiency;
    double costPerKm;
    
    // Manutenção
    MaintenanceStats maintenanceStats;
    double totalMaintenanceCost;
    int maintenanceCount;
    
    // Classes internas
    public static class FuelStats {
        double totalCost;
        double totalLiters;
        double averageConsumption;
        int logsCount;
    }
    
    public static class MaintenanceStats {
        double totalCost;
        int count;
        int pendingCount;
        int completedCount;
    }
}
```

---

## CompanyStats

Estatísticas de uma empresa.

### Estrutura

```java
public class CompanyStats {
    Company company;
    int vehiclesCount;
    int activeVehicles;
    int usersCount;
    int driversCount;
    MaintenanceStatsInfo maintenanceStats;
    FuelStatsInfo fuelStats;
    
    // Classes internas
    public static class MaintenanceStatsInfo {
        int total;
        int pending;
        int completed;
        double totalCost;
    }
    
    public static class FuelStatsInfo {
        double totalLiters;
        double totalCost;
        double averageConsumption;
    }
}
```

---

## MaintenanceReport

Relatório de manutenções.

### Estrutura

```java
public class MaintenanceReport {
    String period;
    String month;
    int year;
    int totalMaintenances;
    double totalCost;
    List<TypeBreakdown> byType;
    List<VehicleBreakdown> byVehicle;
    List<StatusBreakdown> byStatus;
    
    public static class TypeBreakdown {
        String type;
        int count;
        double cost;
    }
    
    public static class VehicleBreakdown {
        int vehicleId;
        String matricula;
        int count;
        double cost;
    }
    
    public static class StatusBreakdown {
        String status;
        int count;
    }
}
```

---

## FuelEfficiencyReport

Relatório de eficiência de combustível.

### Estrutura

```java
public class FuelEfficiencyReport {
    Period period;
    Summary summary;
    List<VehicleEfficiency> vehicleEfficiency;
    List<String> recommendations;
    
    public static class Period {
        String startDate;
        String endDate;
    }
    
    public static class Summary {
        double totalLiters;
        double totalCost;
        double averageConsumption;
        double totalDistance;
    }
    
    public static class VehicleEfficiency {
        int vehicleId;
        String matricula;
        double consumption;
        double totalCost;
        double totalLiters;
        double costPerKm;
        String efficiency;     // "bom", "normal", "mau"
    }
}
```

---

## FuelAlert

Alerta de combustível/eficiência.

### Campos

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | int | ID único |
| `vehicleId` | int | ID do veículo |
| `vehicle` | Vehicle | Veículo associado |
| `tipo` / `type` | String | Tipo de alerta |
| `mensagem` / `message` | String | Mensagem |
| `prioridade` / `priority` | String | Prioridade |
| `valorAtual` / `currentValue` | double | Valor atual |
| `valorEsperado` / `expectedValue` | double | Valor esperado |
| `createdAt` | String | Data de criação |

### Métodos

```java
int getId();
int getVehicleId();
Vehicle getVehicle();
String getTipo();
String getMensagem();
String getPrioridade();
double getValorAtual();
double getValorEsperado();
String getCreatedAt();

// Métodos auxiliares
boolean isHighPriority();
boolean isMediumPriority();
boolean isLowPriority();
double getDeviation();      // valorAtual - valorEsperado
```

---

## Próximos Passos

- [Referência de Serviços](SERVICES.md)
- [Tratamento de Erros](EXCEPTIONS.md)
- [Padrões Builder](BUILDERS.md)
