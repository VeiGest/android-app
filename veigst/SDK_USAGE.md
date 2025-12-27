# VeiGest App - Integração com SDK

## 📁 Estrutura do Projeto

```
veigst/
├── app/                          # Aplicação principal
│   └── src/main/java/com/ipleiria/veigest/
│       ├── VeiGestApplication.java    # Inicialização do SDK
│       ├── MainActivity.java          # Activity principal
│       ├── LoginFragment.java         # Login com SDK
│       ├── DashboardFragment.java     # Dashboard com SDK
│       └── ...
│
└── veigest-sdk/                  # SDK integrado
    ├── src/main/java/com/veigest/sdk/
    │   ├── VeiGestSDK.java           # Classe principal
    │   ├── VeiGestConfig.java        # Configurações
    │   ├── VeiGestCallback.java      # Callbacks
    │   ├── VeiGestException.java     # Exceções
    │   ├── api/                      # Cliente HTTP
    │   ├── auth/                     # Autenticação
    │   ├── models/                   # Modelos de dados
    │   └── services/                 # Serviços da API
    └── docs/                         # Documentação completa
```

## 🚀 Como Usar o SDK

### 1. Inicialização (já configurado)

O SDK é inicializado automaticamente em `VeiGestApplication.java`:

```java
VeiGestConfig config = new VeiGestConfig.Builder()
    .baseUrl("https://veigestback.dryadlang.org/")
    .connectTimeout(30)
    .readTimeout(30)
    .debug(BuildConfig.DEBUG)
    .build();

VeiGestSDK.initialize(this, config);
```

### 2. Obter Instância do SDK

Em qualquer Activity ou Fragment:

```java
VeiGestSDK sdk = VeiGestApplication.getSDK();
```

### 3. Autenticação

```java
// Login
sdk.auth().login(email, password, new VeiGestCallback<User>() {
    @Override
    public void onSuccess(@NonNull User user) {
        // Login bem-sucedido
        Log.d("Auth", "Utilizador: " + user.getNome());
    }

    @Override
    public void onError(@NonNull VeiGestException error) {
        // Tratar erro
        Log.e("Auth", error.getMessage());
    }
});

// Verificar se está autenticado
if (sdk.auth().isAuthenticated()) {
    // Utilizador tem sessão ativa
}

// Logout
sdk.auth().logout(callback);
```

### 4. Veículos

```java
// Listar veículos
sdk.vehicles().list(new VeiGestCallback<List<Vehicle>>() {
    @Override
    public void onSuccess(@NonNull List<Vehicle> vehicles) {
        for (Vehicle v : vehicles) {
            Log.d("Vehicles", v.getMatricula() + " - " + v.getMarca());
        }
    }

    @Override
    public void onError(@NonNull VeiGestException error) {
        // Tratar erro
    }
});

// Obter veículo específico
sdk.vehicles().get(vehicleId, callback);

// Criar veículo
VehicleService.VehicleBuilder builder = new VehicleService.VehicleBuilder()
    .matricula("AA-00-BB")
    .marca("Toyota")
    .modelo("Hilux")
    .ano(2023);
    
sdk.vehicles().create(builder, callback);
```

### 5. Rotas

```java
// Listar rotas ativas
sdk.routes().getActive(new VeiGestCallback<List<Route>>() {
    @Override
    public void onSuccess(@NonNull List<Route> routes) {
        // Processar rotas
    }

    @Override
    public void onError(@NonNull VeiGestException error) {
        // Tratar erro
    }
});

// Criar rota
RouteService.RouteBuilder builder = new RouteService.RouteBuilder()
    .vehicleId(10)
    .driverId(5)
    .origem("Lisboa")
    .destino("Porto")
    .kmInicial(125000);
    
sdk.routes().create(builder, callback);

// Iniciar rota
sdk.routes().start(routeId, callback);

// Finalizar rota
sdk.routes().finish(routeId, kmFinal, callback);
```

### 6. Manutenções

```java
// Listar manutenções
sdk.maintenances().list(callback);

// Criar manutenção
MaintenanceService.MaintenanceBuilder builder = new MaintenanceService.MaintenanceBuilder()
    .vehicleId(10)
    .tipo("preventiva")
    .data("2024-12-26")
    .custo(350.00)
    .descricao("Troca de óleo");
    
sdk.maintenances().create(builder, callback);
```

### 7. Abastecimentos

```java
// Registar abastecimento
FuelLogService.FuelLogBuilder builder = new FuelLogService.FuelLogBuilder()
    .vehicleId(10)
    .data("2024-12-26")
    .litros(50.0)
    .valor(75.00)
    .kmAtual(130000);
    
sdk.fuelLogs().create(builder, callback);
```

### 8. Documentos

```java
// Listar documentos a expirar nos próximos 30 dias
sdk.documents().getExpiring(30, callback);

// Documentos expirados
sdk.documents().getExpired(callback);
```

### 9. Alertas

```java
// Listar alertas não lidos
sdk.alerts().getUnread(callback);

// Marcar alerta como lido
sdk.alerts().markAsRead(alertId, callback);
```

## ⚠️ Tratamento de Erros

```java
@Override
public void onError(@NonNull VeiGestException error) {
    switch (error.getErrorType()) {
        case NETWORK_ERROR:
            // Sem conexão
            break;
        case UNAUTHORIZED:
            // Token expirado ou inválido
            break;
        case FORBIDDEN:
            // Sem permissão
            break;
        case NOT_FOUND:
            // Recurso não encontrado
            break;
        case VALIDATION_ERROR:
            // Dados inválidos
            break;
        case SERVER_ERROR:
            // Erro no servidor
            break;
    }
}
```

## 📚 Documentação Completa

Consulte a documentação completa em `veigest-sdk/docs/`:

- [README.md](veigest-sdk/docs/README.md) - Índice principal
- [QUICK_START.md](veigest-sdk/docs/QUICK_START.md) - Início rápido
- [SERVICES.md](veigest-sdk/docs/SERVICES.md) - Todos os serviços
- [MODELS.md](veigest-sdk/docs/MODELS.md) - Modelos de dados
- [BUILDERS.md](veigest-sdk/docs/BUILDERS.md) - Padrões Builder
- [CALLBACKS.md](veigest-sdk/docs/CALLBACKS.md) - Callbacks
- [EXCEPTIONS.md](veigest-sdk/docs/EXCEPTIONS.md) - Tratamento de erros
- [BEST_PRACTICES.md](veigest-sdk/docs/BEST_PRACTICES.md) - Boas práticas
- [TROUBLESHOOTING.md](veigest-sdk/docs/TROUBLESHOOTING.md) - Resolução de problemas

## 🔧 Configurações

### URL da API

Altere em `VeiGestApplication.java`:

```java
private static final String API_BASE_URL = "https://sua-api.com/";
```

### Timeouts

```java
VeiGestConfig config = new VeiGestConfig.Builder()
    .baseUrl(API_BASE_URL)
    .connectTimeout(60)  // segundos
    .readTimeout(60)
    .writeTimeout(60)
    .build();
```

### Modo Debug

```java
.debug(BuildConfig.DEBUG)  // Logs apenas em debug
```
