# VeiGest SDK - Documentação

SDK Android para gestão de frotas VeiGest, desenvolvido seguindo o padrão Singleton com Volley para comunicação REST.

## Índice

1. [Instalação](#instalação)
2. [Início Rápido](#início-rápido)
3. [Arquitetura](#arquitetura)
4. [SingletonVeiGest](#singletonveigest)
5. [Modelos](#modelos)
6. [Listeners](#listeners)
7. [Base de Dados Local](#base-de-dados-local)
8. [Exemplos de Uso](#exemplos-de-uso)
9. [Erros Comuns](#erros-comuns)
10. [Implementar Novas Funcionalidades](#implementar-novas-funcionalidades)

---

## Instalação

### 1. Adicionar dependência no `settings.gradle.kts`

```kotlin
include(":veigest-sdk")
```

### 2. Adicionar ao `build.gradle.kts` do app

```kotlin
dependencies {
    implementation(project(":veigest-sdk"))
}
```

### 3. Configurar AndroidManifest.xml

```xml
<application
    android:name=".VeiGestApplication"
    ...>
    
    <!-- Permissões necessárias -->
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
</application>
```

---

## Início Rápido

### 1. Criar classe Application

```java
public class VeiGestApplication extends Application {
    
    private static final String API_BASE_URL = "https://api.veigest.com/api/v1";
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        // Inicializar Singleton
        SingletonVeiGest singleton = SingletonVeiGest.getInstance(this);
        singleton.setBaseUrl(API_BASE_URL);
    }
}
```

### 2. Implementar Login

```java
public class LoginFragment extends Fragment implements LoginListener {
    
    private SingletonVeiGest singleton;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        singleton = SingletonVeiGest.getInstance(requireContext());
        singleton.setLoginListener(this);
    }
    
    private void fazerLogin(String email, String password) {
        singleton.loginAPI(email, password);
    }
    
    @Override
    public void onValidateLogin(String token, User user) {
        // Login bem-sucedido!
        // Navegar para dashboard
    }
    
    @Override
    public void onLoginError(String errorMessage) {
        // Mostrar erro ao utilizador
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        singleton.setLoginListener(null);
    }
}
```

---

## Arquitetura

O SDK segue o padrão **Singleton** com **Volley** para requisições HTTP, conforme material de referência.

```
com.veigest.sdk/
├── SingletonVeiGest.java     # Classe principal (API + gestão de dados)
├── database/
│   └── VeiGestBDHelper.java  # SQLite local
├── listeners/
│   ├── LoginListener.java
│   ├── VeiculosListener.java
│   ├── VeiculoListener.java
│   ├── ManutencoesListener.java
│   ├── AbastecimentosListener.java
│   ├── AlertasListener.java
│   ├── DocumentosListener.java
│   └── RotasListener.java
├── models/
│   ├── Vehicle.java
│   ├── User.java
│   ├── Maintenance.java
│   ├── FuelLog.java
│   ├── Alert.java
│   ├── Document.java
│   └── Route.java
└── utils/
    └── VeiGestJsonParser.java  # Parsing JSON manual
```

### Bibliotecas Utilizadas

| Biblioteca | Versão | Uso |
|------------|--------|-----|
| Volley | 1.2.1 | Requisições HTTP REST |
| Glide | 4.16.0 | Carregamento de imagens |
| Gson | 2.10.1 | Serialização JSON |

---

## SingletonVeiGest

A classe `SingletonVeiGest` é o ponto central do SDK, gerindo:

- RequestQueue do Volley
- Token de autenticação (SharedPreferences)
- Dados em memória (listas de veículos, rotas, etc.)
- Listeners para notificação de atualizações

### Obter Instância

```java
SingletonVeiGest singleton = SingletonVeiGest.getInstance(context);
```

### Configuração

```java
// Definir URL base da API
singleton.setBaseUrl("https://api.exemplo.com/api/v1");

// Iniciar base de dados local (opcional)
singleton.iniciarBD(context);
```

### Autenticação

```java
// Verificar se está autenticado
boolean logado = singleton.isAuthenticated();

// Obter token atual
String token = singleton.getToken();

// Fazer logout
singleton.clearAuth();
```

### Métodos da API

| Método | Descrição | Listener |
|--------|-----------|----------|
| `loginAPI(email, password)` | Autenticação | `LoginListener` |
| `getAllVeiculosAPI()` | Lista veículos | `VeiculosListener` |
| `getVeiculoAPI(id)` | Veículo específico | `VeiculoListener` |
| `getAllManutencoesAPI()` | Lista manutenções | `ManutencoesListener` |
| `getAllAbastecimentosAPI()` | Lista abastecimentos | `AbastecimentosListener` |
| `getAllAlertasAPI()` | Lista alertas | `AlertasListener` |
| `getAllDocumentosAPI()` | Lista documentos | `DocumentosListener` |
| `getAllRotasAPI()` | Lista rotas | `RotasListener` |

---

## Modelos

Todos os modelos implementam `Serializable` para fácil transferência entre Activities/Fragments.

### Vehicle

```java
public class Vehicle implements Serializable {
    int id;
    String licensePlate;   // Matrícula
    String brand;          // Marca
    String model;          // Modelo
    int year;              // Ano
    String fuelType;       // Tipo combustível
    int mileage;           // Quilometragem
    String status;         // Estado (active, maintenance, etc)
    String photo;          // URL da foto
    // ... getters/setters
}
```

### User

```java
public class User implements Serializable {
    int id;
    String username;
    String email;
    String role;           // admin, manager, driver
    String status;         // active, inactive
    int companyId;
    // ... getters/setters
}
```

### Maintenance

```java
public class Maintenance implements Serializable {
    int id;
    int vehicleId;
    String type;           // oil_change, tire_rotation, etc
    String description;
    double cost;
    String date;
    int mileageRecord;
    String status;         // scheduled, completed, cancelled
    // ... getters/setters
}
```

### FuelLog

```java
public class FuelLog implements Serializable {
    int id;
    int vehicleId;
    double liters;
    double value;
    int currentMileage;
    String date;
    double pricePerLiter;
    // ... getters/setters
}
```

### Alert

```java
public class Alert implements Serializable {
    int id;
    String type;           // maintenance, document, fuel, etc
    String title;
    String description;
    String priority;       // low, medium, high, critical
    String status;         // active, resolved, dismissed
    // ... getters/setters
    
    // Métodos auxiliares
    boolean isActive();
    boolean isCritical();
}
```

### Document

```java
public class Document implements Serializable {
    int id;
    int vehicleId;
    String type;           // license, insurance, inspection
    String expiryDate;
    int daysToExpiry;
    String status;         // valid, expiring_soon, expired
    // ... getters/setters
    
    // Métodos auxiliares
    boolean isExpired();
    boolean isExpiringSoon();
}
```

### Route

```java
public class Route implements Serializable {
    int id;
    int vehicleId;
    int driverId;
    String startLocation;
    String endLocation;
    String startTime;
    String endTime;
    String status;         // scheduled, in_progress, completed
    int duration;          // em minutos
    // ... getters/setters
    
    // Métodos auxiliares
    boolean isScheduled();
    boolean isInProgress();
    boolean isCompleted();
}
```

---

## Listeners

Os listeners são interfaces que notificam a aplicação quando dados são recebidos da API.

### Padrão de Uso

```java
// 1. Implementar o listener na classe
public class MinhaActivity extends AppCompatActivity implements VeiculosListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 2. Registar o listener
        SingletonVeiGest.getInstance(this).setVeiculosListener(this);
        
        // 3. Chamar método da API
        SingletonVeiGest.getInstance(this).getAllVeiculosAPI();
    }
    
    // 4. Implementar callback
    @Override
    public void onRefreshListaVeiculos(ArrayList<Vehicle> listaVeiculos) {
        // IMPORTANTE: Atualizar UI na thread principal
        runOnUiThread(() -> {
            // Processar lista de veículos
        });
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 5. Remover listener para evitar memory leaks
        SingletonVeiGest.getInstance(this).setVeiculosListener(null);
    }
}
```

### Lista de Listeners

| Listener | Métodos |
|----------|---------|
| `LoginListener` | `onValidateLogin(token, user)`, `onLoginError(msg)` |
| `VeiculosListener` | `onRefreshListaVeiculos(lista)` |
| `VeiculoListener` | `onRefreshVeiculo(veiculo)` |
| `ManutencoesListener` | `onRefreshListaManutencoes(lista)` |
| `AbastecimentosListener` | `onRefreshListaAbastecimentos(lista)` |
| `AlertasListener` | `onRefreshListaAlertas(lista)` |
| `DocumentosListener` | `onRefreshListaDocumentos(lista)` |
| `RotasListener` | `onRefreshListaRotas(lista)` |

---

## Base de Dados Local

O SDK inclui um `SQLiteOpenHelper` para persistência local.

### Inicialização

```java
SingletonVeiGest singleton = SingletonVeiGest.getInstance(context);
singleton.iniciarBD(context);
```

### Operações CRUD

```java
VeiGestBDHelper bd = singleton.getBD();

// Veículos
bd.adicionarVehicleBD(vehicle);
ArrayList<Vehicle> veiculos = bd.getAllVehiclesBD();
bd.editarVehicleBD(vehicle);
bd.removerVehicleBD(id);
bd.removerAllVehiclesBD();

// Manutenções
bd.adicionarMaintenanceBD(maintenance);
ArrayList<Maintenance> manutencoes = bd.getAllMaintenancesBD();
bd.getAllMaintenancesByVehicleBD(vehicleId);

// Abastecimentos
bd.adicionarFuelLogBD(fuelLog);
ArrayList<FuelLog> abastecimentos = bd.getAllFuelLogsBD();
bd.getAllFuelLogsByVehicleBD(vehicleId);

// Similar para outras entidades...
```

---

## Exemplos de Uso

### Exemplo 1: Listar Veículos

```java
public class VeiculosFragment extends Fragment implements VeiculosListener {
    
    private SingletonVeiGest singleton;
    private RecyclerView recyclerView;
    private VeiculosAdapter adapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        singleton = SingletonVeiGest.getInstance(requireContext());
        singleton.setVeiculosListener(this);
    }
    
    @Override
    public View onCreateView(...) {
        // Setup RecyclerView...
        
        // Carregar veículos
        singleton.getAllVeiculosAPI();
        
        return view;
    }
    
    @Override
    public void onRefreshListaVeiculos(ArrayList<Vehicle> listaVeiculos) {
        if (getActivity() == null) return;
        
        getActivity().runOnUiThread(() -> {
            adapter.setVeiculos(listaVeiculos);
            adapter.notifyDataSetChanged();
        });
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        singleton.setVeiculosListener(null);
    }
}
```

### Exemplo 2: Dashboard com Múltiplos Listeners

```java
public class DashboardFragment extends Fragment 
        implements VeiculosListener, RotasListener, AlertasListener {
    
    private SingletonVeiGest singleton;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        singleton = SingletonVeiGest.getInstance(requireContext());
        
        // Registar múltiplos listeners
        singleton.setVeiculosListener(this);
        singleton.setRotasListener(this);
        singleton.setAlertasListener(this);
    }
    
    @Override
    public View onCreateView(...) {
        // Carregar dados
        singleton.getAllVeiculosAPI();
        singleton.getAllRotasAPI();
        singleton.getAllAlertasAPI();
        
        return view;
    }
    
    @Override
    public void onRefreshListaVeiculos(ArrayList<Vehicle> lista) {
        // Atualizar card de veículos
    }
    
    @Override
    public void onRefreshListaRotas(ArrayList<Route> lista) {
        // Atualizar card de rotas
    }
    
    @Override
    public void onRefreshListaAlertas(ArrayList<Alert> lista) {
        // Mostrar alertas prioritários
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        singleton.setVeiculosListener(null);
        singleton.setRotasListener(null);
        singleton.setAlertasListener(null);
    }
}
```

### Exemplo 3: Criar Manutenção

```java
// Usar Volley diretamente para POST (quando SingletonVeiGest não tiver método)
private void criarManutencao(Maintenance m) {
    JSONObject jsonBody = new JSONObject();
    try {
        jsonBody.put("vehicleId", m.getVehicleId());
        jsonBody.put("type", m.getType());
        jsonBody.put("description", m.getDescription());
        jsonBody.put("cost", m.getCost());
        jsonBody.put("date", m.getDate());
    } catch (JSONException e) {
        Log.e(TAG, "Erro JSON: " + e.getMessage());
        return;
    }
    
    String url = singleton.getBaseUrl() + "/maintenances";
    
    JsonObjectRequest request = new JsonObjectRequest(
        Request.Method.POST, url, jsonBody,
        response -> {
            // Sucesso - processar resposta
            Toast.makeText(context, "Manutenção criada!", Toast.LENGTH_SHORT).show();
        },
        error -> {
            // Erro
            Log.e(TAG, "Erro ao criar manutenção: " + error.getMessage());
        }
    ) {
        @Override
        public Map<String, String> getHeaders() {
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + singleton.getToken());
            headers.put("Content-Type", "application/json");
            return headers;
        }
    };
    
    // Adicionar à fila do Volley
    singleton.getRequestQueue().add(request);
}
```

---

## Erros Comuns

### 1. NullPointerException ao usar Singleton

**Problema:** Chamar `SingletonVeiGest.getInstance()` antes da Application inicializar.

**Solução:** Garantir que a Application está configurada no AndroidManifest:
```xml
<application android:name=".VeiGestApplication" ...>
```

### 2. Listener não é chamado

**Problema:** O listener não foi registado ou foi removido prematuramente.

**Solução:** 
- Registar o listener no `onCreate()`
- Remover apenas no `onDestroy()` ou `onDestroyView()`

### 3. UI não atualiza com dados da API

**Problema:** Tentar atualizar UI fora da thread principal.

**Solução:** Usar `runOnUiThread()`:
```java
@Override
public void onRefreshListaVeiculos(ArrayList<Vehicle> lista) {
    if (getActivity() == null) return;
    
    getActivity().runOnUiThread(() -> {
        // Atualizar UI aqui
    });
}
```

### 4. Memory Leak com Listeners

**Problema:** Não remover listeners ao destruir Activity/Fragment.

**Solução:** Sempre remover no ciclo de vida:
```java
@Override
public void onDestroyView() {
    super.onDestroyView();
    singleton.setVeiculosListener(null);
}
```

### 5. Token não persiste após fechar app

**Problema:** Token é salvo em memória mas não em SharedPreferences.

**Solução:** O Singleton já faz isso automaticamente via `saveToken()`.

### 6. Erro 401 Unauthorized

**Problema:** Token expirado ou inválido.

**Solução:** 
- Verificar `isAuthenticated()` antes de fazer chamadas
- Fazer novo login se necessário
- Implementar refresh token (se API suportar)

### 7. Erro de conexão (VolleyError)

**Problema:** Sem internet ou servidor indisponível.

**Solução:** Verificar conectividade antes de chamar API:
```java
if (VeiGestJsonParser.isConnectionInternet(context)) {
    singleton.getAllVeiculosAPI();
} else {
    Toast.makeText(context, "Sem conexão à internet", Toast.LENGTH_SHORT).show();
}
```

---

## Implementar Novas Funcionalidades

### Adicionar Novo Endpoint

1. **Criar/Atualizar Modelo** em `models/`

```java
public class NovoModelo implements Serializable {
    private int id;
    private String campo;
    // getters/setters
}
```

2. **Criar Listener** em `listeners/`

```java
public interface NovoModeloListener {
    void onRefreshListaNovoModelo(ArrayList<NovoModelo> lista);
}
```

3. **Adicionar ao SingletonVeiGest**

```java
// Variáveis
private ArrayList<NovoModelo> novosModelos;
private NovoModeloListener novoModeloListener;

// Setter do listener
public void setNovoModeloListener(NovoModeloListener listener) {
    this.novoModeloListener = listener;
}

// Método da API
public void getAllNovosModelosAPI() {
    String url = baseUrl + "/novo-endpoint";
    
    JsonArrayRequest request = new JsonArrayRequest(
        Request.Method.GET, url, null,
        response -> {
            novosModelos = VeiGestJsonParser.parserJsonNovosModelos(response);
            if (novoModeloListener != null) {
                novoModeloListener.onRefreshListaNovoModelo(novosModelos);
            }
        },
        error -> Log.e(TAG, "Erro: " + error.getMessage())
    ) {
        @Override
        public Map<String, String> getHeaders() {
            return getAuthHeaders();
        }
    };
    
    volleyQueue.add(request);
}
```

4. **Adicionar Parser** em `VeiGestJsonParser`

```java
public static ArrayList<NovoModelo> parserJsonNovosModelos(JSONArray response) {
    ArrayList<NovoModelo> lista = new ArrayList<>();
    try {
        for (int i = 0; i < response.length(); i++) {
            JSONObject obj = response.getJSONObject(i);
            NovoModelo m = new NovoModelo();
            m.setId(obj.optInt("id"));
            m.setCampo(obj.optString("campo"));
            lista.add(m);
        }
    } catch (JSONException e) {
        Log.e(TAG, "Erro parsing: " + e.getMessage());
    }
    return lista;
}
```

5. **Adicionar à Base de Dados** (opcional) em `VeiGestBDHelper`

```java
// Na criação de tabelas
private static final String CREATE_TABLE_NOVO_MODELO = 
    "CREATE TABLE novo_modelo (" +
    "id INTEGER PRIMARY KEY, " +
    "campo TEXT)";

// Métodos CRUD
public void adicionarNovoModeloBD(NovoModelo m) { ... }
public ArrayList<NovoModelo> getAllNovosModelosBD() { ... }
```

### Adicionar Suporte a Imagens com Glide

```java
// No Adapter ou Fragment
ImageView imageView = view.findViewById(R.id.imageView);

Glide.with(context)
    .load(vehicle.getPhoto())
    .placeholder(R.drawable.placeholder_vehicle)
    .error(R.drawable.error_image)
    .into(imageView);
```

---

## API Endpoints de Referência

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/auth/login` | Autenticação |
| GET | `/auth/me` | Dados do utilizador atual |
| POST | `/auth/logout` | Encerrar sessão |
| GET | `/vehicles` | Lista veículos |
| GET | `/vehicles/{id}` | Veículo específico |
| POST | `/vehicles` | Criar veículo |
| PUT | `/vehicles/{id}` | Atualizar veículo |
| DELETE | `/vehicles/{id}` | Remover veículo |
| GET | `/maintenances` | Lista manutenções |
| GET | `/fuel-logs` | Lista abastecimentos |
| GET | `/alerts` | Lista alertas |
| GET | `/documents` | Lista documentos |
| GET | `/routes` | Lista rotas |
| GET | `/users` | Lista utilizadores |

---

## Suporte

Para dúvidas ou problemas:

1. Verificar esta documentação
2. Consultar seção de [Erros Comuns](#erros-comuns)
3. Verificar logs do Logcat com tag `SingletonVeiGest`
4. Abrir issue no repositório

---

**Versão:** 1.0.0  
**Última Atualização:** Junho 2025  
**Compatibilidade:** Android API 24+ (Android 7.0+)
