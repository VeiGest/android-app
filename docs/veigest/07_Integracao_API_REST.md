# VeiGest - Integração API REST
## Comunicação com o Backend

**ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO - 2025 / 2026**  
**TESP PROGRAMAÇÃO DE SISTEMAS DE INFORMAÇÃO**

---

## 📖 Índice

1. [Introdução à API REST](#introdução-à-api-rest)
2. [Biblioteca Volley](#biblioteca-volley)
3. [Estrutura das Requisições](#estrutura-das-requisições)
4. [Autenticação](#autenticação)
5. [Endpoints da API](#endpoints-da-api)
6. [Parsing de JSON](#parsing-de-json)
7. [Tratamento de Erros](#tratamento-de-erros)
8. [Boas Práticas](#boas-práticas)

---

## Introdução à API REST

A aplicação VeiGest comunica com uma API REST desenvolvida em Yii2 (PHP). A comunicação é feita via HTTP usando JSON.

### Características da API

| Característica | Valor |
|----------------|-------|
| Base URL | `http://localhost:8080/api/v1` |
| Formato | JSON |
| Autenticação | Bearer Token |
| Métodos | GET, POST, PUT, DELETE |

### Fluxo de Comunicação

```
┌─────────────────────────────────────────────────────────────┐
│                    FLUXO API REST                           │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│   Android App                        API Server             │
│   ┌─────────┐                        ┌─────────┐           │
│   │         │   1. HTTP Request      │         │           │
│   │ VeiGest │ ─────────────────────► │  Yii2   │           │
│   │   App   │   (JSON + Token)       │  API    │           │
│   │         │                        │         │           │
│   │         │   2. HTTP Response     │         │           │
│   │         │ ◄───────────────────── │         │           │
│   │         │   (JSON)               │         │           │
│   └─────────┘                        └─────────┘           │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## Biblioteca Volley

O **Volley** é uma biblioteca HTTP do Google para Android. É usada no VeiGest para todas as requisições de rede.

### Vantagens do Volley

| Vantagem | Descrição |
|----------|-----------|
| **Assíncrono** | Não bloqueia a UI Thread |
| **Cache** | Cache automático de respostas |
| **Retry** | Tentativas automáticas em caso de erro |
| **Queue** | Fila de requisições com prioridade |

### Configuração no Singleton

```java
public class SingletonVeiGest {
    
    // RequestQueue do Volley (estática)
    private static RequestQueue volleyQueue = null;
    
    public static synchronized SingletonVeiGest getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SingletonVeiGest(context.getApplicationContext());
            // Inicializar Volley Queue
            volleyQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return INSTANCE;
    }
}
```

### Dependência no build.gradle

```kotlin
dependencies {
    implementation("com.android.volley:volley:1.2.1")
}
```

---

## Estrutura das Requisições

### JsonObjectRequest

Para requisições que retornam um objeto JSON:

```java
JsonObjectRequest request = new JsonObjectRequest(
    Request.Method.GET,              // Método HTTP
    url,                             // URL do endpoint
    null,                            // Body (null para GET)
    response -> {                    // Success callback
        // Processar resposta JSON
    },
    error -> {                       // Error callback
        // Tratar erro
    }
) {
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + getToken());
        headers.put("Content-Type", "application/json");
        return headers;
    }
};

// Adicionar à queue
volleyQueue.add(request);
```

### JsonArrayRequest

Para requisições que retornam um array JSON:

```java
JsonArrayRequest request = new JsonArrayRequest(
    Request.Method.GET,
    url,
    null,
    response -> {
        // response é um JSONArray
        for (int i = 0; i < response.length(); i++) {
            JSONObject obj = response.getJSONObject(i);
            // processar...
        }
    },
    error -> {
        // tratar erro
    }
);
```

### StringRequest

Para respostas que não são JSON:

```java
StringRequest request = new StringRequest(
    Request.Method.GET,
    url,
    response -> {
        // response é uma String
    },
    error -> {
        // tratar erro
    }
);
```

---

## Autenticação

### Login

```java
public void loginAPI(final String username, final String password) {
    // Criar body da requisição
    JSONObject body = new JSONObject();
    try {
        body.put("username", username);
        body.put("password", password);
    } catch (JSONException e) {
        e.printStackTrace();
        if (loginListener != null) {
            loginListener.onLoginError("Erro ao criar requisição");
        }
        return;
    }
    
    JsonObjectRequest request = new JsonObjectRequest(
        Request.Method.POST,
        mUrlAPILogin,           // "http://api/v1/auth/login"
        body,
        response -> {
            try {
                Log.d(TAG, "Login response: " + response.toString());
                
                // Parsear resposta
                Object[] result = VeiGestJsonParser.parserJsonLogin(response);
                
                if (result != null && result[0] != null) {
                    String token = (String) result[0];
                    User user = (User) result[1];
                    
                    // Guardar token
                    saveToken(token);
                    
                    // Guardar info do utilizador
                    if (user != null) {
                        utilizadorAtual = user;
                        saveUserInfo(user.getId(), user.getCompanyId());
                        
                        // Persistir na BD local
                        if (veiGestBD != null) {
                            veiGestBD.adicionarUserBD(user);
                        }
                    }
                    
                    // Notificar sucesso
                    if (loginListener != null) {
                        loginListener.onValidateLogin(token, user);
                    }
                } else {
                    if (loginListener != null) {
                        loginListener.onLoginError("Credenciais inválidas");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (loginListener != null) {
                    loginListener.onLoginError("Erro ao processar resposta");
                }
            }
        },
        error -> {
            Log.e(TAG, "Login error: " + error.toString());
            String errorMsg = "Erro de conexão";
            
            if (error.networkResponse != null) {
                try {
                    String responseBody = new String(error.networkResponse.data, "UTF-8");
                    JSONObject errorJson = new JSONObject(responseBody);
                    errorMsg = VeiGestJsonParser.parserJsonError(errorJson);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            if (loginListener != null) {
                loginListener.onLoginError(errorMsg);
            }
        }
    );
    
    volleyQueue.add(request);
}
```

### Headers de Autenticação

```java
// Requisição autenticada
JsonObjectRequest request = new JsonObjectRequest(...) {
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        
        // Token de autenticação
        String token = getToken();
        if (token != null && !token.isEmpty()) {
            headers.put("Authorization", "Bearer " + token);
        }
        
        // Content-Type
        headers.put("Content-Type", "application/json");
        
        return headers;
    }
};
```

---

## Endpoints da API

### Configuração dos Endpoints

```java
// No SingletonVeiGest
private String baseUrl = "http://localhost:8080/api/v1";

private String mUrlAPILogin;
private String mUrlAPIRegister;
private String mUrlAPIVehicles;
private String mUrlAPIMaintenances;
private String mUrlAPIFuelLogs;
private String mUrlAPIAlerts;
private String mUrlAPIDocuments;
private String mUrlAPIRoutes;
private String mUrlAPIUsers;

private void atualizarEndpoints() {
    mUrlAPILogin = baseUrl + "/auth/login";
    mUrlAPIRegister = baseUrl + "/users";
    mUrlAPIVehicles = baseUrl + "/vehicles";
    mUrlAPIMaintenances = baseUrl + "/maintenances";
    mUrlAPIFuelLogs = baseUrl + "/fuel-logs";
    mUrlAPIAlerts = baseUrl + "/alerts";
    mUrlAPIDocuments = baseUrl + "/documents";
    mUrlAPIRoutes = baseUrl + "/routes";
    mUrlAPIUsers = baseUrl + "/users";
}
```

### Tabela de Endpoints

| Entidade | Método | Endpoint | Descrição |
|----------|--------|----------|-----------|
| Auth | POST | `/auth/login` | Login |
| Users | POST | `/users` | Registar utilizador |
| Users | GET | `/users/{id}` | Obter utilizador |
| Vehicles | GET | `/vehicles` | Listar veículos |
| Vehicles | GET | `/vehicles/{id}` | Obter veículo |
| Vehicles | POST | `/vehicles` | Criar veículo |
| Vehicles | PUT | `/vehicles/{id}` | Atualizar veículo |
| Vehicles | DELETE | `/vehicles/{id}` | Remover veículo |
| Maintenances | GET | `/maintenances` | Listar manutenções |
| Fuel Logs | GET | `/fuel-logs` | Listar abastecimentos |
| Alerts | GET | `/alerts` | Listar alertas |
| Documents | GET | `/documents` | Listar documentos |
| Routes | GET | `/routes` | Listar rotas |

### Exemplo de GET

```java
public void getAllVeiculosAPI() {
    JsonObjectRequest request = new JsonObjectRequest(
        Request.Method.GET,
        mUrlAPIVehicles,
        null,  // Sem body para GET
        response -> {
            try {
                ArrayList<Vehicle> vehicles = VeiGestJsonParser.parserJsonVehicles(response);
                
                veiculos.clear();
                veiculos.addAll(vehicles);
                
                if (veiGestBD != null) {
                    for (Vehicle v : vehicles) {
                        veiGestBD.adicionarVehicleBD(v);
                    }
                }
                
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

### Exemplo de POST

```java
public void addVeiculoAPI(Vehicle vehicle) {
    JSONObject body = new JSONObject();
    try {
        body.put("license_plate", vehicle.getLicensePlate());
        body.put("brand", vehicle.getBrand());
        body.put("model", vehicle.getModel());
        body.put("year", vehicle.getYear());
        body.put("fuel_type", vehicle.getFuelType());
        body.put("mileage", vehicle.getMileage());
    } catch (JSONException e) {
        e.printStackTrace();
        return;
    }
    
    JsonObjectRequest request = new JsonObjectRequest(
        Request.Method.POST,
        mUrlAPIVehicles,
        body,  // Body com dados do veículo
        response -> {
            try {
                Vehicle createdVehicle = VeiGestJsonParser.parserJsonVehicle(response);
                
                veiculos.add(createdVehicle);
                
                if (veiGestBD != null) {
                    veiGestBD.adicionarVehicleBD(createdVehicle);
                }
                
                if (veiculoListener != null) {
                    veiculoListener.onOperacaoVeiculo(
                        VeiculoListener.OPERACAO_ADICIONAR, 
                        createdVehicle
                    );
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        },
        error -> {
            if (veiculoListener != null) {
                veiculoListener.onOperacaoVeiculoError("Erro ao criar veículo");
            }
        }
    ) {
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + getToken());
            headers.put("Content-Type", "application/json");
            return headers;
        }
    };
    
    volleyQueue.add(request);
}
```

### Exemplo de PUT

```java
public void editVeiculoAPI(Vehicle vehicle) {
    JSONObject body = new JSONObject();
    try {
        body.put("license_plate", vehicle.getLicensePlate());
        body.put("brand", vehicle.getBrand());
        body.put("model", vehicle.getModel());
        body.put("mileage", vehicle.getMileage());
        // ... outros campos
    } catch (JSONException e) {
        e.printStackTrace();
        return;
    }
    
    String url = mUrlAPIVehicles + "/" + vehicle.getId();
    
    JsonObjectRequest request = new JsonObjectRequest(
        Request.Method.PUT,
        url,
        body,
        response -> {
            // Processar resposta
        },
        error -> {
            // Tratar erro
        }
    ) {
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + getToken());
            headers.put("Content-Type", "application/json");
            return headers;
        }
    };
    
    volleyQueue.add(request);
}
```

### Exemplo de DELETE

```java
public void removeVeiculoAPI(int vehicleId) {
    String url = mUrlAPIVehicles + "/" + vehicleId;
    
    StringRequest request = new StringRequest(
        Request.Method.DELETE,
        url,
        response -> {
            // Remover localmente
            veiculos.removeIf(v -> v.getId() == vehicleId);
            
            if (veiGestBD != null) {
                veiGestBD.removerVehicleBD(vehicleId);
            }
            
            if (veiculoListener != null) {
                veiculoListener.onOperacaoVeiculo(
                    VeiculoListener.OPERACAO_REMOVER, 
                    null
                );
            }
        },
        error -> {
            if (veiculoListener != null) {
                veiculoListener.onOperacaoVeiculoError("Erro ao remover");
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

## Parsing de JSON

### Classe VeiGestJsonParser

```java
package com.veigest.sdk.utils;

public class VeiGestJsonParser {
    
    /**
     * Parsear resposta de login
     * @return Object[] {token, user}
     */
    public static Object[] parserJsonLogin(JSONObject response) {
        try {
            String token = response.optString("token", null);
            
            User user = null;
            if (response.has("user")) {
                JSONObject userJson = response.getJSONObject("user");
                user = parserJsonUser(userJson);
            }
            
            return new Object[]{token, user};
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Parsear utilizador
     */
    public static User parserJsonUser(JSONObject json) {
        User user = new User();
        user.setId(json.optInt("id", 0));
        user.setUsername(json.optString("username", ""));
        user.setEmail(json.optString("email", ""));
        user.setRole(json.optString("role", ""));
        user.setStatus(json.optString("status", ""));
        user.setCompanyId(json.optInt("company_id", 0));
        return user;
    }
    
    /**
     * Parsear lista de veículos
     */
    public static ArrayList<Vehicle> parserJsonVehicles(JSONObject response) {
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        
        try {
            JSONArray data = response.optJSONArray("data");
            if (data == null) {
                data = response.optJSONArray("vehicles");
            }
            
            if (data != null) {
                for (int i = 0; i < data.length(); i++) {
                    JSONObject vehicleJson = data.getJSONObject(i);
                    vehicles.add(parserJsonVehicle(vehicleJson));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        return vehicles;
    }
    
    /**
     * Parsear veículo individual
     */
    public static Vehicle parserJsonVehicle(JSONObject json) {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(json.optInt("id", 0));
        vehicle.setCompanyId(json.optInt("company_id", 0));
        vehicle.setLicensePlate(json.optString("license_plate", ""));
        vehicle.setBrand(json.optString("brand", ""));
        vehicle.setModel(json.optString("model", ""));
        vehicle.setYear(json.optInt("year", 0));
        vehicle.setFuelType(json.optString("fuel_type", ""));
        vehicle.setMileage(json.optInt("mileage", 0));
        vehicle.setStatus(json.optString("status", ""));
        vehicle.setDriverId(json.optInt("driver_id", 0));
        vehicle.setPhoto(json.optString("photo", null));
        return vehicle;
    }
    
    /**
     * Parsear mensagem de erro
     */
    public static String parserJsonError(JSONObject response) {
        return response.optString("message", 
               response.optString("error", "Erro desconhecido"));
    }
}
```

### Usando Gson (Alternativa)

```java
// Dependência
implementation("com.google.code.gson:gson:2.10.1")

// Usar Gson para parsing automático
Gson gson = new Gson();

// JSON para Objeto
Vehicle vehicle = gson.fromJson(jsonString, Vehicle.class);

// JSON para Lista
Type listType = new TypeToken<ArrayList<Vehicle>>(){}.getType();
ArrayList<Vehicle> vehicles = gson.fromJson(jsonArray.toString(), listType);

// Objeto para JSON
String json = gson.toJson(vehicle);
```

---

## Tratamento de Erros

### Tipos de Erro do Volley

| Erro | Causa | Ação |
|------|-------|------|
| `NetworkError` | Sem conexão | Verificar rede |
| `TimeoutError` | Timeout | Tentar novamente |
| `ServerError` | Erro 5xx | Problema no servidor |
| `AuthFailureError` | Erro 401/403 | Token inválido |
| `ParseError` | JSON inválido | Verificar resposta |

### Tratamento no Volley

```java
error -> {
    String errorMsg;
    
    if (error instanceof NetworkError) {
        errorMsg = "Sem conexão à internet";
    } else if (error instanceof TimeoutError) {
        errorMsg = "Tempo limite excedido";
    } else if (error instanceof AuthFailureError) {
        errorMsg = "Sessão expirada. Faça login novamente.";
        // Limpar token e redirecionar para login
        clearAuth();
    } else if (error instanceof ServerError) {
        errorMsg = "Erro no servidor";
    } else if (error.networkResponse != null) {
        int statusCode = error.networkResponse.statusCode;
        
        try {
            String responseBody = new String(error.networkResponse.data, "UTF-8");
            JSONObject errorJson = new JSONObject(responseBody);
            errorMsg = errorJson.optString("message", "Erro " + statusCode);
        } catch (Exception e) {
            errorMsg = "Erro " + statusCode;
        }
    } else {
        errorMsg = "Erro de conexão";
    }
    
    Log.e(TAG, "API Error: " + errorMsg);
    
    if (listener != null) {
        listener.onError(errorMsg);
    }
}
```

---

## Boas Práticas

### ✅ Fazer

```java
// Sempre verificar se listener não é null
if (veiculosListener != null) {
    veiculosListener.onRefreshVeiculos(vehicles);
}

// Usar Log para debug
Log.d(TAG, "Request URL: " + url);
Log.d(TAG, "Response: " + response.toString());

// Tratar todos os tipos de erro
error -> {
    if (error.networkResponse != null) {
        // Erro com resposta do servidor
    } else if (error instanceof TimeoutError) {
        // Timeout
    } else {
        // Outros erros
    }
}

// Usar constantes para endpoints
private static final String ENDPOINT_LOGIN = "/auth/login";
```

### ❌ Evitar

```java
// Não fazer requisições na Main Thread
// (Volley já é assíncrono, mas não criar threads manualmente)

// Não ignorar erros
error -> {
    // fazer nada - ❌
}

// Não guardar passwords em texto claro
prefs.putString("password", password);  // ❌

// Não fazer log de dados sensíveis em produção
Log.d(TAG, "Token: " + token);  // ❌ em produção
```

---

## 📚 Documentação Relacionada

| Documento | Descrição |
|-----------|-----------|
| [02_VeiGest_SDK.md](02_VeiGest_SDK.md) | Documentação do SDK |
| [06_SQLite_Persistencia.md](06_SQLite_Persistencia.md) | Base de dados local |
| [08_Listeners_Callbacks.md](08_Listeners_Callbacks.md) | Sistema de listeners |
| [10_Troubleshooting_Erros_Comuns.md](10_Troubleshooting_Erros_Comuns.md) | Problemas de API |

---

**Última atualização:** Janeiro 2026
