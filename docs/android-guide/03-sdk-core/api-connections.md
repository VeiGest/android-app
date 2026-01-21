# 🌐 API Connections (Volley)

Comunicação com o servidor é realizada através da biblioteca **Volley**. O `SingletonVeiGest` centraliza todas estas operações.

## 1. Request Structure

Todas as requisições API seguem um padrão no SDK:
1. **URL Construction**: Base URL + Endpoint.
2. **Header Logic**: Autenticação via Bearer Token.
3. **Request Type**: `JsonObjectRequest` (para um objeto) ou `JsonArrayRequest` (para listas).

### Example: GET Request
```java
String url = mUrlAPIVehicles;
JsonArrayRequest request = new JsonArrayRequest(
    Request.Method.GET, url, null,
    response -> {
        // SUCCESS CASE
        ArrayList<Vehicle> list = VeiGestJsonParser.parserJsonVehicles(response);
        // ... notify listeners
    },
    error -> {
        // ERROR CASE
        Log.e(TAG, "API Error: " + error.toString());
    }
) {
    @Override
    public Map<String, String> getHeaders() {
        return getAuthHeaders(); // Adds Authorization: Bearer <token>
    }
};
volleyQueue.add(request);
```

## 2. Common HTTP Methods used

- **GET**: Retrieve data (e.g., `getAllVehiclesAPI`).
- **POST**: Create new resources or perform specific actions (e.g., `adicionarVeiculoAPI`, `concluirRotaAPI`).
- **PUT**: Update existing resources (e.g., `editarVeiculoAPI`).
- **DELETE**: Remove resources (e.g., `removerVeiculoAPI`).

## 3. Authentication

O método `getAuthHeaders()` insere automaticamente o Token guardado nas `SharedPreferences`. Certifique-se de que o utilizador está logado antes de fazer chamadas a endpoints protegidos.

## 4. Debugging API Calls

Sempre verifique o **Logcat** em caso de falha:
- **401**: Token expirado ou inválido.
- **404**: Endpoint não encontrado (verifique se usou plural ou singular).
- **422**: Erro de validação no backend (os campos enviados não coincidem com as regras do modelo).
- **500**: Erro interno no servidor.
