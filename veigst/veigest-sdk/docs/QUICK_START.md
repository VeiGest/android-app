# Guia de Início Rápido

Este guia vai ajudá-lo a integrar o VeiGest SDK na sua aplicação Android em menos de 5 minutos.

---

## Pré-requisitos

- Android Studio Arctic Fox (2020.3.1) ou superior
- SDK Android 24 ou superior
- Credenciais de acesso à API VeiGest

---

## Passo 1: Adicionar o SDK ao Projeto

### Opção A: Como módulo local

1. Clone ou copie a pasta `veigest-sdk` para o diretório raiz do seu projeto

2. Adicione ao `settings.gradle.kts`:
```kotlin
include(":veigest-sdk")
```

3. Adicione a dependência em `app/build.gradle.kts`:
```kotlin
dependencies {
    implementation(project(":veigest-sdk"))
}
```

4. Sincronize o projeto (Sync Now)

### Opção B: Via Maven/JitPack (futuro)
```kotlin
// Em breve disponível
implementation("com.veigest:sdk:1.0.0")
```

---

## Passo 2: Configurar Permissões

O SDK já declara as permissões necessárias. Se precisar usar localização GPS para rotas:

```xml
<!-- AndroidManifest.xml -->
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

---

## Passo 3: Inicializar o SDK

### Na classe Application (Recomendado)

```java
public class MyApplication extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        // Configurar o SDK
        VeiGestConfig config = new VeiGestConfig.Builder()
            .setBaseUrl("https://veigestback.dryadlang.org")
            .setDebugMode(BuildConfig.DEBUG)
            .setConnectTimeout(30)
            .setReadTimeout(30)
            .build();
        
        // Inicializar
        VeiGestSDK.init(this, config);
    }
}
```

Não esqueça de registrar a Application no `AndroidManifest.xml`:
```xml
<application
    android:name=".MyApplication"
    ... >
```

### Ou na Activity principal

```java
public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Inicializar se ainda não foi
        if (!VeiGestSDK.isInitialized()) {
            VeiGestConfig config = new VeiGestConfig.Builder()
                .setDebugMode(true)
                .build();
            
            VeiGestSDK.init(this, config);
        }
    }
}
```

---

## Passo 4: Fazer Login

```java
VeiGestSDK sdk = VeiGestSDK.getInstance();

// Login com email
sdk.auth().login("utilizador@empresa.com", "password123", 
    new VeiGestCallback<LoginResponse>() {
        @Override
        public void onSuccess(@NonNull LoginResponse response) {
            // ✅ Login bem sucedido!
            String token = response.getAccessToken();
            User user = response.getUser();
            
            Log.d("VeiGest", "Bem-vindo, " + user.getNome());
            Log.d("VeiGest", "Empresa: " + user.getCompanyId());
            
            // Redirecionar para a tela principal
            startActivity(new Intent(this, DashboardActivity.class));
        }
        
        @Override
        public void onError(@NonNull VeiGestException error) {
            // ❌ Erro no login
            if (error.isAuthenticationError()) {
                showError("Email ou password incorretos");
            } else if (error.isNetworkError()) {
                showError("Sem conexão à internet");
            } else {
                showError(error.getMessage());
            }
        }
    }
);
```

---

## Passo 5: Usar os Serviços

### Listar Veículos
```java
sdk.vehicles().list(new VeiGestCallback<List<Vehicle>>() {
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
```

### Registar Abastecimento
```java
sdk.fuelLogs().create(
    vehicleId,          // ID do veículo
    "2024-12-23",       // Data
    45.5,               // Litros
    68.25,              // Valor total (€)
    125000,             // Quilometragem atual
    new VeiGestCallback<FuelLog>() {
        @Override
        public void onSuccess(@NonNull FuelLog fuelLog) {
            showMessage("Abastecimento registado com sucesso!");
        }
        
        @Override
        public void onError(@NonNull VeiGestException error) {
            showError("Erro: " + error.getMessage());
        }
    }
);
```

### Criar Ticket de Suporte
```java
sdk.tickets().create(
    "Problema no GPS",                          // Título
    "O GPS do veículo não está funcionando",    // Descrição
    "tecnico",                                  // Tipo
    "alta",                                     // Prioridade
    new VeiGestCallback<Ticket>() {
        @Override
        public void onSuccess(@NonNull Ticket ticket) {
            showMessage("Ticket #" + ticket.getId() + " criado!");
        }
        
        @Override
        public void onError(@NonNull VeiGestException error) {
            showError(error.getMessage());
        }
    }
);
```

---

## Passo 6: Fazer Logout

```java
sdk.auth().logout(new VeiGestCallback<Void>() {
    @Override
    public void onSuccess(@NonNull Void result) {
        // Limpar dados locais e voltar ao login
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
    
    @Override
    public void onError(@NonNull VeiGestException error) {
        // Mesmo com erro, fazer logout local
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
});

// Ou simplesmente:
sdk.auth().logout(); // Sem callback
```

---

## Verificar Estado de Autenticação

```java
// Em qualquer parte da app
VeiGestSDK sdk = VeiGestSDK.getInstance();

if (sdk.isAuthenticated()) {
    // Utilizador está logado
    int userId = sdk.auth().getUserId();
    int companyId = sdk.auth().getCompanyId();
} else {
    // Redirecionar para login
    startActivity(new Intent(this, LoginActivity.class));
}
```

---

## Exemplo Completo: Tela de Veículos

```java
public class VehiclesActivity extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    private VehicleAdapter adapter;
    private VeiGestSDK sdk;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles);
        
        sdk = VeiGestSDK.getInstance();
        setupRecyclerView();
        loadVehicles();
    }
    
    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VehicleAdapter();
        recyclerView.setAdapter(adapter);
    }
    
    private void loadVehicles() {
        showLoading(true);
        
        sdk.vehicles().listActive(new VeiGestCallback<List<Vehicle>>() {
            @Override
            public void onSuccess(@NonNull List<Vehicle> vehicles) {
                showLoading(false);
                adapter.setVehicles(vehicles);
                
                if (vehicles.isEmpty()) {
                    showEmptyState();
                }
            }
            
            @Override
            public void onError(@NonNull VeiGestException error) {
                showLoading(false);
                showError(error.getMessage());
            }
        });
    }
    
    private void showLoading(boolean show) {
        findViewById(R.id.progressBar).setVisibility(show ? View.VISIBLE : View.GONE);
    }
    
    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
    
    private void showEmptyState() {
        findViewById(R.id.emptyState).setVisibility(View.VISIBLE);
    }
}
```

---

## Próximos Passos

- 📖 [Documentação completa dos Serviços](SERVICES.md)
- 📊 [Referência de Modelos](MODELS.md)
- 🔒 [Guia de Autenticação](AUTHENTICATION.md)
- 📁 [Upload de Ficheiros](FILE_UPLOAD.md)
- ⚠️ [Tratamento de Erros](EXCEPTIONS.md)

---

## Precisa de Ajuda?

- Consulte o [Troubleshooting](TROUBLESHOOTING.md) para problemas comuns
- Abra uma [issue](https://github.com/VeiGest/android-sdk/issues) no GitHub
- Entre em contacto: suporte@veigest.com
