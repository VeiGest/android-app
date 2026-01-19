# VeiGest SDK - Documentação Completa
## Singleton, API e Base de Dados

**ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO - 2025 / 2026**  
**TESP PROGRAMAÇÃO DE SISTEMAS DE INFORMAÇÃO**

---

## 📖 Índice

1. [Introdução ao SDK](#introdução-ao-sdk)
2. [Padrão Singleton](#padrão-singleton)
3. [Configuração e Inicialização](#configuração-e-inicialização)
4. [Autenticação](#autenticação)
5. [Gestão de Veículos](#gestão-de-veículos)
6. [Gestão de Rotas](#gestão-de-rotas)
7. [Gestão de Manutenções](#gestão-de-manutenções)
8. [Gestão de Abastecimentos](#gestão-de-abastecimentos)
9. [Gestão de Alertas](#gestão-de-alertas)
10. [Gestão de Documentos](#gestão-de-documentos)
11. [Listeners e Callbacks](#listeners-e-callbacks)

---

## Introdução ao SDK

O **VeiGest SDK** é uma biblioteca Android que encapsula toda a lógica de comunicação com a API REST e persistência local de dados. O SDK utiliza:

- **Volley**: Para requisições HTTP assíncronas
- **SQLite**: Para persistência local de dados
- **SharedPreferences**: Para guardar token e configurações
- **Padrão Singleton**: Para acesso global e gestão de estado
- **Padrão Observer (Listeners)**: Para notificação de resultados

### Benefícios do SDK

| Benefício | Descrição |
|-----------|-----------|
| **Reutilização** | Pode ser usado em múltiplas aplicações |
| **Encapsulamento** | Toda lógica de API está isolada |
| **Simplicidade** | Interface simples para operações complexas |
| **Offline** | Suporte a cache local com SQLite |
| **Assíncrono** | Operações não bloqueiam a UI |

---

## Padrão Singleton

O padrão Singleton garante que existe **apenas uma instância** da classe `SingletonVeiGest` em toda a aplicação.

### Estrutura do Singleton

```java
public class SingletonVeiGest {
    
    // Instância única (estática e privada)
    private static SingletonVeiGest INSTANCE = null;
    
    // RequestQueue do Volley (estática)
    private static RequestQueue volleyQueue = null;
    
    // Construtor privado - impede criação externa
    private SingletonVeiGest(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        veiculos = new ArrayList<>();
        manutencoes = new ArrayList<>();
        // ... inicialização de listas
        atualizarEndpoints();
    }
    
    // Método de acesso à instância
    public static synchronized SingletonVeiGest getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SingletonVeiGest(context.getApplicationContext());
            volleyQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return INSTANCE;
    }
    
    // Verificar se foi inicializado
    public static boolean isInitialized() {
        return INSTANCE != null;
    }
}
```

### Características do Singleton

| Característica | Implementação |
|----------------|---------------|
| **Instância única** | Atributo `static` privado `INSTANCE` |
| **Construtor privado** | `private SingletonVeiGest(Context context)` |
| **Acesso controlado** | Método `getInstance()` público e estático |
| **Thread-safe** | Palavra-chave `synchronized` no `getInstance()` |
| **Contexto global** | Usa `context.getApplicationContext()` |

### Como Usar o Singleton

```java
// Em qualquer parte da aplicação
SingletonVeiGest singleton = SingletonVeiGest.getInstance(context);

// Verificar se há utilizador autenticado
if (singleton.isAuthenticated()) {
    // Utilizador tem token válido
}

// Fazer login
singleton.loginAPI("email@exemplo.com", "password");

// Listar veículos
singleton.getAllVeiculosAPI();
```

---

## Configuração e Inicialização

### Inicialização na Application

A inicialização deve ser feita na classe `Application`:

```java
public class VeiGestApplication extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        // Obter instância do Singleton
        SingletonVeiGest singleton = SingletonVeiGest.getInstance(this);
        
        // Configurar URL da API
        singleton.setBaseUrl("http://192.168.1.100:8080/api/v1");
        
        // Inicializar base de dados SQLite
        singleton.iniciarBD(this);
    }
}
```

### Registar no AndroidManifest.xml

```xml
<application
    android:name=".VeiGestApplication"
    android:allowBackup="true"
    android:icon="@mipmap/ic_launcher"
    android:label="@string/app_name"
    android:theme="@style/Theme.Veigest">
    
    <activity android:name=".MainActivity">
        <intent-filter>
            <action android:name="android.intent.action.MAIN" />
            <category android:name="android.intent.category.LAUNCHER" />
        </intent-filter>
    </activity>
</application>
```

### Configurar URL da API

```java
// URL por defeito
singleton.setBaseUrl("http://localhost:8080/api/v1");

// URL de produção
singleton.setBaseUrl("https://api.veigest.com/v1");

// URL local para desenvolvimento
singleton.setBaseUrl("http://10.0.2.2:8080/api/v1"); // Emulador Android

// Obter URL atual
String baseUrl = singleton.getBaseUrl();
```

### Endpoints Configurados

O SDK configura automaticamente os seguintes endpoints:

| Endpoint | URL |
|----------|-----|
| Login | `{baseUrl}/auth/login` |
| Register | `{baseUrl}/users` |
| Vehicles | `{baseUrl}/vehicles` |
| Maintenances | `{baseUrl}/maintenances` |
| Fuel Logs | `{baseUrl}/fuel-logs` |
| Alerts | `{baseUrl}/alerts` |
| Documents | `{baseUrl}/documents` |
| Routes | `{baseUrl}/routes` |

---

## Autenticação

### Login

```java
// No Fragment ou Activity
public class LoginFragment extends Fragment implements LoginListener {
    
    private SingletonVeiGest singleton;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Obter instância e registar listener
        singleton = SingletonVeiGest.getInstance(requireContext());
        singleton.setLoginListener(this);
    }
    
    private void performLogin(String email, String password) {
        // Mostrar loading
        progressBar.setVisibility(View.VISIBLE);
        
        // Chamar API de login
        singleton.loginAPI(email, password);
    }
    
    // Callback de sucesso
    @Override
    public void onValidateLogin(String token, User user) {
        getActivity().runOnUiThread(() -> {
            progressBar.setVisibility(View.GONE);
            
            // Token já foi guardado automaticamente pelo SDK
            Toast.makeText(getContext(), "Bem-vindo, " + user.getUsername(), Toast.LENGTH_SHORT).show();
            
            // Navegar para Dashboard
            ((MainActivity) getActivity()).loadDashboard();
        });
    }
    
    // Callback de erro
    @Override
    public void onLoginError(String errorMessage) {
        getActivity().runOnUiThread(() -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
        });
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // IMPORTANTE: Remover listener para evitar memory leaks
        singleton.setLoginListener(null);
    }
}
```

### Verificar Autenticação

```java
// Verificar se há token válido
if (singleton.isAuthenticated()) {
    // Utilizador está autenticado
    loadDashboard();
} else {
    // Mostrar ecrã de login
    loadLoginFragment();
}
```

### Logout

```java
// Limpar dados de autenticação
singleton.clearAuth();

// Navegar para login
navigateToLogin();
```

### Obter Dados do Utilizador

```java
// Obter utilizador atual
User user = singleton.getUtilizadorAtual();

// Obter dados específicos
int userId = singleton.getUserId();
int companyId = singleton.getCompanyId();
String token = singleton.getToken();
```

---

## Gestão de Veículos

### Listar Todos os Veículos

```java
public class VehiclesFragment extends Fragment implements VeiculosListener {
    
    private SingletonVeiGest singleton;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        singleton = SingletonVeiGest.getInstance(requireContext());
        singleton.setVeiculosListener(this);
    }
    
    private void loadVehicles() {
        // Carregar do cache local primeiro
        ArrayList<Vehicle> localVehicles = singleton.getVeiculos();
        updateUI(localVehicles);
        
        // Atualizar da API
        singleton.getAllVeiculosAPI();
    }
    
    @Override
    public void onRefreshVeiculos(ArrayList<Vehicle> veiculos) {
        getActivity().runOnUiThread(() -> {
            updateUI(veiculos);
        });
    }
    
    @Override
    public void onRefreshVeiculosError(String error) {
        getActivity().runOnUiThread(() -> {
            Toast.makeText(getContext(), "Erro: " + error, Toast.LENGTH_SHORT).show();
        });
    }
}
```

### Obter Veículo Específico

```java
// Por ID
singleton.getVeiculoAPI(vehicleId);

// Do cache local
Vehicle vehicle = singleton.getVeiculo(vehicleId);
```

### Adicionar Veículo

```java
public class AddVehicleFragment extends Fragment implements VeiculoListener {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        singleton = SingletonVeiGest.getInstance(requireContext());
        singleton.setVeiculoListener(this);
    }
    
    private void addVehicle() {
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate("AA-00-AA");
        vehicle.setBrand("Toyota");
        vehicle.setModel("Hilux");
        vehicle.setYear(2023);
        vehicle.setFuelType("diesel");
        vehicle.setMileage(0);
        
        singleton.addVeiculoAPI(vehicle);
    }
    
    @Override
    public void onOperacaoVeiculo(int operacao, Vehicle veiculo) {
        if (operacao == VeiculoListener.OPERACAO_ADICIONAR) {
            Toast.makeText(getContext(), "Veículo adicionado!", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    public void onOperacaoVeiculoError(String error) {
        Toast.makeText(getContext(), "Erro: " + error, Toast.LENGTH_SHORT).show();
    }
}
```

### Editar Veículo

```java
Vehicle vehicle = singleton.getVeiculo(vehicleId);
vehicle.setMileage(50000);

singleton.editVeiculoAPI(vehicle);
```

### Remover Veículo

```java
singleton.removeVeiculoAPI(vehicleId);
```

---

## Gestão de Rotas

### Listar Rotas

```java
public class RoutesFragment extends Fragment implements RotasListener {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        singleton = SingletonVeiGest.getInstance(requireContext());
        singleton.setRotasListener(this);
    }
    
    private void loadRoutes() {
        singleton.getAllRotasAPI();
    }
    
    @Override
    public void onRefreshRotas(ArrayList<Route> rotas) {
        getActivity().runOnUiThread(() -> {
            // Atualizar lista de rotas
            adapter.updateData(rotas);
        });
    }
    
    @Override
    public void onRefreshRotasError(String error) {
        // Tratar erro
    }
}
```

### Modelo Route

```java
public class Route implements Serializable {
    private int id;
    private int companyId;
    private int vehicleId;
    private int driverId;
    private String startLocation;
    private String endLocation;
    private String startTime;
    private String endTime;
    private String status;
    
    // Getters e Setters...
}
```

---

## Gestão de Manutenções

### Listar Manutenções

```java
singleton.setManutencoesListener(this);
singleton.getAllManutencoesAPI();

// Filtrar por veículo
singleton.getManutencoesVeiculoAPI(vehicleId);
```

### Adicionar Manutenção

```java
Maintenance maintenance = new Maintenance();
maintenance.setVehicleId(vehicleId);
maintenance.setType("preventiva");
maintenance.setDescription("Troca de óleo");
maintenance.setCost(150.00);
maintenance.setDate("2026-01-10");
maintenance.setMileageRecord(50000);
maintenance.setWorkshop("Oficina Central");

singleton.addManutencaoAPI(maintenance);
```

---

## Gestão de Abastecimentos

### Listar Abastecimentos

```java
singleton.setAbastecimentosListener(this);
singleton.getAllAbastecimentosAPI();

// Filtrar por veículo
singleton.getAbastecimentosVeiculoAPI(vehicleId);
```

### Registar Abastecimento

```java
FuelLog fuelLog = new FuelLog();
fuelLog.setVehicleId(vehicleId);
fuelLog.setLiters(50.0);
fuelLog.setValue(85.50);
fuelLog.setCurrentMileage(51000);
fuelLog.setDate("2026-01-10");
fuelLog.setNotes("Tanque cheio");

singleton.addAbastecimentoAPI(fuelLog);
```

---

## Gestão de Alertas

### Listar Alertas

```java
singleton.setAlertasListener(this);
singleton.getAllAlertasAPI();
```

### Modelo Alert

```java
public class Alert implements Serializable {
    private int id;
    private int companyId;
    private String type;      // "maintenance", "document", "fuel", etc.
    private String title;
    private String description;
    private String priority;  // "high", "medium", "low"
    private String status;    // "active", "resolved"
    private String details;
    private String createdAt;
    
    // Getters e Setters...
}
```

---

## Gestão de Documentos

### Listar Documentos

```java
singleton.setDocumentosListener(this);
singleton.getAllDocumentosAPI();

// Filtrar por veículo
singleton.getDocumentosVeiculoAPI(vehicleId);
```

### Modelo Document

```java
public class Document implements Serializable {
    private int id;
    private int companyId;
    private int vehicleId;
    private int driverId;
    private String type;        // "license", "insurance", "inspection"
    private String expiryDate;
    private String notes;
    
    // Getters e Setters...
}
```

---

## Listeners e Callbacks

### Interfaces Disponíveis

| Interface | Métodos |
|-----------|---------|
| `LoginListener` | `onValidateLogin()`, `onLoginError()` |
| `RegisterListener` | `onRegisterSuccess()`, `onRegisterError()` |
| `VeiculosListener` | `onRefreshVeiculos()`, `onRefreshVeiculosError()` |
| `VeiculoListener` | `onOperacaoVeiculo()`, `onOperacaoVeiculoError()` |
| `ManutencoesListener` | `onRefreshManutencoes()`, `onRefreshManutencoesError()` |
| `AbastecimentosListener` | `onRefreshAbastecimentos()`, `onRefreshAbastecimentosError()` |
| `AlertasListener` | `onRefreshAlertas()`, `onRefreshAlertasError()` |
| `DocumentosListener` | `onRefreshDocumentos()`, `onRefreshDocumentosError()` |
| `RotasListener` | `onRefreshRotas()`, `onRefreshRotasError()` |

### Exemplo de Implementação

```java
public interface VeiculosListener {
    /**
     * Chamado quando a lista de veículos é atualizada com sucesso.
     * @param veiculos Lista atualizada de veículos
     */
    void onRefreshVeiculos(ArrayList<Vehicle> veiculos);
    
    /**
     * Chamado quando ocorre erro ao obter veículos.
     * @param error Mensagem de erro
     */
    void onRefreshVeiculosError(String error);
}
```

### Boas Práticas com Listeners

```java
public class MyFragment extends Fragment implements VeiculosListener {
    
    private SingletonVeiGest singleton;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        singleton = SingletonVeiGest.getInstance(requireContext());
        
        // ✅ Registar listener no onCreate
        singleton.setVeiculosListener(this);
    }
    
    @Override
    public void onRefreshVeiculos(ArrayList<Vehicle> veiculos) {
        // ✅ Verificar se Activity ainda existe
        if (getActivity() == null) return;
        
        // ✅ Executar na UI Thread
        getActivity().runOnUiThread(() -> {
            updateUI(veiculos);
        });
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        
        // ✅ SEMPRE remover listener para evitar memory leaks
        singleton.setVeiculosListener(null);
    }
}
```

---

## 📚 Documentação Relacionada

| Documento | Descrição |
|-----------|-----------|
| [06_SQLite_Persistencia.md](06_SQLite_Persistencia.md) | Base de dados local |
| [07_Integracao_API_REST.md](07_Integracao_API_REST.md) | Detalhes da integração API |
| [08_Listeners_Callbacks.md](08_Listeners_Callbacks.md) | Sistema de listeners |
| [09_Implementar_Novas_Funcionalidades.md](09_Implementar_Novas_Funcionalidades.md) | Guia para adicionar features |

---

**Última atualização:** Janeiro 2026
