# VeiGest Android SDK

SDK Android para integração com a API VeiGest - Sistema de Gestão de Frotas.

## 📦 Instalação

### Adicionar o módulo ao projeto

1. Adicione o módulo no `settings.gradle.kts`:

```kotlin
include(":veigest-sdk")
```

2. Adicione a dependência no `app/build.gradle.kts`:

```kotlin
dependencies {
    implementation(project(":veigest-sdk"))
}
```

### Permissões

O SDK já declara as permissões necessárias no seu manifest:
- `INTERNET` - Para comunicação com a API
- `ACCESS_NETWORK_STATE` - Para verificar conectividade

## 🚀 Início Rápido

### 1. Inicializar o SDK

Inicialize o SDK na sua `Application` ou `Activity` principal:

```java
import com.veigest.sdk.VeiGestSDK;
import com.veigest.sdk.VeiGestConfig;

// Na sua Application.onCreate() ou Activity.onCreate()
VeiGestConfig config = new VeiGestConfig.Builder()
    .setBaseUrl("https://veigestback.dryadlang.org")
    .setDebugMode(BuildConfig.DEBUG) // Ativa logs em debug
    .setConnectTimeout(30)
    .setReadTimeout(30)
    .build();

VeiGestSDK sdk = VeiGestSDK.init(getApplicationContext(), config);
```

### 2. Fazer Login

```java
sdk.auth().login("user@email.com", "password", new VeiGestCallback<LoginResponse>() {
    @Override
    public void onSuccess(@NonNull LoginResponse response) {
        // Login bem-sucedido!
        User user = response.getUser();
        Log.d("VeiGest", "Bem-vindo, " + user.getNome());
    }
    
    @Override
    public void onError(@NonNull VeiGestException error) {
        // Erro no login
        Log.e("VeiGest", "Erro: " + error.getMessage());
        
        if (error.isAuthenticationError()) {
            // Credenciais inválidas
        } else if (error.isNetworkError()) {
            // Sem conexão
        }
    }
});
```

### 3. Listar Veículos

```java
sdk.vehicles().list(new VeiGestCallback<List<Vehicle>>() {
    @Override
    public void onSuccess(@NonNull List<Vehicle> vehicles) {
        for (Vehicle vehicle : vehicles) {
            Log.d("VeiGest", vehicle.getMatricula() + " - " + vehicle.getFullName());
        }
    }
    
    @Override
    public void onError(@NonNull VeiGestException error) {
        Log.e("VeiGest", "Erro: " + error.getMessage());
    }
});
```

### 4. Fazer Logout

```java
sdk.auth().logout();
```

## 📚 Serviços Disponíveis

### AuthService - Autenticação
```java
VeiGestSDK.getInstance().auth()
    .login(email, password, callback)      // Login
    .logout()                               // Logout
    .refreshToken(callback)                 // Renovar token
    .getCurrentUser(callback)               // Dados do utilizador
    .isAuthenticated()                      // Verifica se está logado
```

### VehicleService - Veículos
```java
VeiGestSDK.getInstance().vehicles()
    .list(callback)                                    // Listar todos
    .list(companyId, estado, page, limit, sort, cb)   // Listar com filtros
    .listActive(callback)                              // Listar ativos
    .listInMaintenance(callback)                       // Em manutenção
    .listByStatus(status, callback)                    // Por status (NEW)
    .get(id, callback)                                 // Obter por ID
    .create(matricula, marca, modelo, ano, tipo, cb)  // Criar
    .update(id, data, callback)                        // Atualizar
    .updateMileage(id, km, callback)                   // Atualizar KM
    .updateStatus(id, estado, callback)                // Atualizar estado
    .delete(id, callback)                              // Eliminar
    .assignDriver(vehicleId, driverId, callback)       // Atribuir condutor
    .unassignDriver(vehicleId, callback)               // Remover condutor
    // Novos endpoints
    .getMaintenances(vehicleId, callback)              // Manutenções do veículo (NEW)
    .getFuelLogs(vehicleId, callback)                  // Abastecimentos do veículo (NEW)
    .getStats(vehicleId, callback)                     // Estatísticas do veículo (NEW)
```

### UserService - Utilizadores
```java
VeiGestSDK.getInstance().users()
    .list(callback)                         // Listar todos
    .get(id, callback)                      // Obter por ID
    .create(nome, email, password, cb)      // Criar
    .update(id, data, callback)             // Atualizar
    .delete(id, callback)                   // Eliminar
    .resetPassword(id, newPassword, cb)     // Reset password
    // Novos endpoints
    .listDrivers(callback)                  // Listar condutores (NEW)
    .getProfile(callback)                   // Perfil atual (NEW)
    .listByCompany(companyId, callback)     // Por empresa (NEW)
```

### MaintenanceService - Manutenções
```java
VeiGestSDK.getInstance().maintenances()
    .list(callback)                         // Listar todas
    .listByVehicle(vehicleId, callback)     // Por veículo
    .get(id, callback)                      // Obter por ID
    .create(vehicleId, tipo, data, custo, desc, oficina, cb)  // Criar
    .update(id, data, callback)             // Atualizar
    .delete(id, callback)                   // Eliminar
    // Novos endpoints
    .getByVehicle(vehicleId, callback)      // Por veículo via endpoint dedicado (NEW)
    .getByStatus(estado, callback)          // Por estado (NEW)
    .listScheduled(callback)                // Agendadas (NEW)
    .listCompleted(callback)                // Concluídas (NEW)
    .listInProgress(callback)               // Em progresso (NEW)
    .schedule(id, date, priority, tech, cb) // Agendar manutenção (NEW)
    .getMonthlyReport(month, year, cb)      // Relatório mensal (NEW)
    .getCostsReport(vehicleId, start, end, cb)  // Relatório de custos (NEW)
    .getStats(callback)                     // Estatísticas gerais (NEW)
```

### FuelLogService - Abastecimentos
```java
VeiGestSDK.getInstance().fuelLogs()
    .list(callback)                         // Listar todos
    .listByVehicle(vehicleId, callback)     // Por veículo
    .listByDriver(driverId, callback)       // Por condutor
    .get(id, callback)                      // Obter por ID
    .create(vehicleId, data, litros, valor, km, cb)  // Criar
    .update(id, data, callback)             // Atualizar
    .delete(id, callback)                   // Eliminar
    // Novos endpoints
    .getByVehicle(vehicleId, callback)      // Por veículo via endpoint dedicado (NEW)
    .getStats(callback)                     // Estatísticas de consumo (NEW)
    .getStats(vehicleId, start, end, cb)    // Estatísticas com filtros (NEW)
    .getAlerts(callback)                    // Alertas de combustível (NEW)
    .getEfficiencyReport(callback)          // Relatório de eficiência (NEW)
    .getEfficiencyReport(start, end, cb)    // Relatório por período (NEW)
```

### RouteService - Rotas e GPS
```java
VeiGestSDK.getInstance().routes()
    .list(callback)                         // Listar todas
    .listByVehicle(vehicleId, callback)     // Por veículo
    .listByDriver(driverId, callback)       // Por condutor
    .listInProgress(callback)               // Em andamento
    .listCompleted(callback)                // Concluídas
    .get(id, callback)                      // Obter por ID
    .start(vehicleId, driverId, origem, destino, kmInicial, cb)  // Iniciar
    .finish(id, kmFinal, notas, callback)   // Finalizar
    .cancel(id, notas, callback)            // Cancelar
    .delete(id, callback)                   // Eliminar
    
    // GPS
    .getGpsEntries(routeId, callback)       // Pontos GPS da rota
    .addGpsEntry(routeId, lat, lng, vel, alt, cb)  // Adicionar ponto
    .addGpsEntriesBatch(routeId, entries, cb)      // Adicionar vários
```

### DocumentService - Documentos
```java
VeiGestSDK.getInstance().documents()
    .list(callback)                         // Listar todos
    .listByVehicle(vehicleId, callback)     // Por veículo
    .listByDriver(driverId, callback)       // Por condutor
    .listByType(tipo, callback)             // Por tipo
    .listValid(callback)                    // Válidos
    .listExpired(callback)                  // Expirados
    .listExpiring(dias, callback)           // A expirar
    .get(id, callback)                      // Obter por ID
    .create(tipo, dataValidade, vehicleId, driverId, notas, cb)  // Criar
    .update(id, data, callback)             // Atualizar
    .delete(id, callback)                   // Eliminar
```

### AlertService - Alertas
```java
VeiGestSDK.getInstance().alerts()
    .list(callback)                         // Listar todos
    .listActive(callback)                   // Ativos
    .listHighPriority(callback)             // Alta prioridade
    .listByType(tipo, callback)             // Por tipo
    .get(id, callback)                      // Obter por ID
    .create(tipo, titulo, descricao, prioridade, cb)  // Criar
    .resolve(id, notas, callback)           // Resolver
    .ignore(id, notas, callback)            // Ignorar
    .delete(id, callback)                   // Eliminar
```

### ReportService - Relatórios
```java
VeiGestSDK.getInstance().reports()
    .getCompanyStats(callback)              // Estatísticas da empresa
    .getVehicleCosts(vehicleId, callback)   // Custos por veículo
    .getFuelConsumption(vehicleId, periodo, cb)  // Consumo combustível
    .getMaintenanceSchedule(callback)       // Cronograma manutenções
    .getDriverPerformance(driverId, periodo, cb)  // Performance condutor
```

### CompanyService - Empresas
```java
VeiGestSDK.getInstance().companies()
    .list(callback)                         // Listar todas
    .get(id, callback)                      // Obter por ID
    .create(nome, nif, email, plano, cb)    // Criar
    .update(id, data, callback)             // Atualizar
    .delete(id, callback)                   // Eliminar
    // Novos endpoints
    .getVehicles(companyId, callback)       // Veículos da empresa (NEW)
    .getUsers(companyId, callback)          // Utilizadores da empresa (NEW)
    .getStats(companyId, callback)          // Estatísticas da empresa (NEW)
```

### TicketService - Tickets/Bilhetes (NEW)
```java
VeiGestSDK.getInstance().tickets()
    .list(callback)                         // Listar todos
    .list(status, tipo, prioridade, vehicleId, page, limit, cb)  // Com filtros
    .listPending(callback)                  // Pendentes
    .listOpen(callback)                     // Abertos
    .listHighPriority(callback)             // Alta prioridade
    .listByVehicle(vehicleId, callback)     // Por veículo
    .get(id, callback)                      // Obter por ID
    .create(titulo, descricao, tipo, prioridade, cb)  // Criar
    .update(id, data, callback)             // Atualizar
    .cancel(id, motivo, callback)           // Cancelar
    .complete(id, observacoes, callback)    // Completar
    .delete(id, callback)                   // Eliminar
```

### FileService - Ficheiros (NEW)
```java
VeiGestSDK.getInstance().files()
    .list(callback)                         // Listar todos
    .list(tipo, page, limit, callback)      // Com filtros
    .listImages(callback)                   // Apenas imagens
    .listPdfs(callback)                     // Apenas PDFs
    .get(id, callback)                      // Obter informações
    .upload(file, nome, tipo, callback)     // Upload de ficheiro
    .upload(bytes, fileName, mimeType, tipo, cb)  // Upload de bytes
    .download(id, destinationFile, callback)  // Download
    .downloadTo(id, dir, fileName, callback)  // Download para pasta
    .delete(id, callback)                   // Eliminar
```

## 🔧 Builders

O SDK fornece Builders para facilitar a criação de objetos:

### VehicleBuilder
```java
sdk.vehicles().create(
    new VehicleService.VehicleBuilder()
        .matricula("AA-00-AA")
        .marca("Toyota")
        .modelo("Corolla")
        .ano(2024)
        .tipoCombustivel("gasolina")
        .quilometragem(0)
        .build(),
    callback
);
```

### MaintenanceBuilder
```java
sdk.maintenances().create(
    new MaintenanceService.MaintenanceBuilder()
        .vehicleId(1)
        .tipo("Revisão")
        .descricao("Revisão geral")
        .data("2024-01-15")
        .custo(150.00)
        .kmRegistro(50000)
        .oficina("AutoRep")
        .build(),
    callback
);
```

## ⚠️ Tratamento de Erros

```java
new VeiGestCallback<List<Vehicle>>() {
    @Override
    public void onSuccess(@NonNull List<Vehicle> result) {
        // Sucesso
    }
    
    @Override
    public void onError(@NonNull VeiGestException error) {
        // Verificar tipo de erro
        switch (error.getType()) {
            case NETWORK:
                // Sem conexão
                break;
            case AUTHENTICATION:
                // Token inválido/expirado - redirecionar para login
                break;
            case AUTHORIZATION:
                // Sem permissão
                break;
            case VALIDATION:
                // Dados inválidos
                Map<String, List<String>> errors = error.getValidationErrors();
                break;
            case NOT_FOUND:
                // Recurso não encontrado
                break;
            case SERVER:
                // Erro do servidor
                break;
        }
        
        // Código HTTP
        int httpCode = error.getHttpCode();
        
        // Mensagem de erro
        String message = error.getMessage();
    }
}
```

## 🔒 Segurança

- Tokens são armazenados de forma segura usando `EncryptedSharedPreferences`
- Autenticação automática em todas as requisições
- Refresh token automático quando necessário

## 📱 Compatibilidade

- **minSdk**: 24 (Android 7.0)
- **targetSdk**: 36
- **Java**: 11

## 📊 Novos Modelos de Dados

A versão atualizada inclui novos modelos para suportar os endpoints avançados:

- `Ticket` - Tickets/bilhetes de suporte
- `CompanyStats` - Estatísticas de empresa
- `VehicleStats` - Estatísticas de veículo
- `FuelEfficiencyReport` - Relatório de eficiência de combustível
- `FuelAlert` - Alertas de combustível/eficiência
- `MaintenanceReport` - Relatório de manutenções
- `FileInfo` - Informações de ficheiros

## 📂 Exemplos Completos

Exemplos completos estão disponíveis na pasta `app/src/main/java/com/ipleiria/veigest/examples/`:

- `VeiGestApplication.java` - Inicialização do SDK
- `LoginFragmentExample.java` - Exemplo de tela de login
- `VehiclesFragmentExample.java` - Exemplo de listagem de veículos

## 📄 Licença

MIT License - VeiGest © 2024
