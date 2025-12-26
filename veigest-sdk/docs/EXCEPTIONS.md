# Tratamento de Exceções e Erros

Este documento descreve o sistema de exceções do VeiGest SDK e como tratar erros adequadamente.

---

## VeiGestException

Exceção base para todos os erros do SDK.

### Estrutura

```java
public class VeiGestException extends Exception {
    
    // Código HTTP do erro (0 se não aplicável)
    int getHttpCode();
    
    // Código de erro da API (se disponível)
    String getErrorCode();
    
    // Erros de validação por campo
    Map<String, List<String>> getValidationErrors();
    
    // Tipo do erro
    ErrorType getType();
    
    // Mensagem de erro (herdado de Exception)
    String getMessage();
    
    // Causa original (se houver)
    Throwable getCause();
}
```

---

## ErrorType

Enum que categoriza os tipos de erro:

| Tipo | HTTP Code | Descrição |
|------|-----------|-----------|
| `NETWORK` | - | Erro de rede (sem conexão, timeout) |
| `AUTHENTICATION` | 401 | Token inválido ou expirado |
| `AUTHORIZATION` | 403 | Sem permissão |
| `VALIDATION` | 400, 422 | Dados inválidos |
| `NOT_FOUND` | 404 | Recurso não encontrado |
| `SERVER` | 500+ | Erro interno do servidor |
| `UNKNOWN` | Outros | Erro desconhecido |

---

## Métodos de Verificação

```java
VeiGestException error = ...;

// Verificar tipo de erro
error.isNetworkError();          // true se NETWORK
error.isAuthenticationError();   // true se AUTHENTICATION
error.isValidationError();       // true se VALIDATION
error.isServerError();           // true se SERVER

// Obter informações
int httpCode = error.getHttpCode();         // Ex: 401, 404, 500
String code = error.getErrorCode();         // Ex: "INVALID_TOKEN"
ErrorType type = error.getType();           // Ex: ErrorType.AUTHENTICATION
```

---

## Tratamento Básico

```java
sdk.vehicles().list(new VeiGestCallback<List<Vehicle>>() {
    @Override
    public void onSuccess(@NonNull List<Vehicle> vehicles) {
        displayVehicles(vehicles);
    }
    
    @Override
    public void onError(@NonNull VeiGestException error) {
        // Tratamento simples
        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
    }
});
```

---

## Tratamento por Tipo de Erro

### Switch Pattern

```java
@Override
public void onError(@NonNull VeiGestException error) {
    switch (error.getType()) {
        case NETWORK:
            handleNetworkError(error);
            break;
            
        case AUTHENTICATION:
            handleAuthError(error);
            break;
            
        case AUTHORIZATION:
            handleForbiddenError(error);
            break;
            
        case VALIDATION:
            handleValidationError(error);
            break;
            
        case NOT_FOUND:
            handleNotFoundError(error);
            break;
            
        case SERVER:
            handleServerError(error);
            break;
            
        default:
            handleUnknownError(error);
    }
}
```

### Implementação dos Handlers

```java
private void handleNetworkError(VeiGestException error) {
    // Mostrar mensagem de conexão
    showError("Sem conexão à internet. Verifique sua rede.");
    
    // Oferecer opção de retry
    showRetryButton(() -> loadData());
}

private void handleAuthError(VeiGestException error) {
    // Limpar sessão e redirecionar para login
    sdk.auth().logout();
    
    showError("Sessão expirada. Faça login novamente.");
    redirectToLogin();
}

private void handleForbiddenError(VeiGestException error) {
    showError("Você não tem permissão para esta ação.");
}

private void handleValidationError(VeiGestException error) {
    // Mostrar erros de validação nos campos
    Map<String, List<String>> errors = error.getValidationErrors();
    
    if (errors != null) {
        for (Map.Entry<String, List<String>> entry : errors.entrySet()) {
            String field = entry.getKey();
            List<String> messages = entry.getValue();
            
            // Encontrar o campo e mostrar erro
            setFieldError(field, messages.get(0));
        }
    } else {
        showError(error.getMessage());
    }
}

private void handleNotFoundError(VeiGestException error) {
    showError("O recurso solicitado não foi encontrado.");
    navigateBack();
}

private void handleServerError(VeiGestException error) {
    showError("Erro no servidor. Tente novamente mais tarde.");
    
    // Log para debugging
    Log.e("ServerError", "HTTP " + error.getHttpCode() + ": " + error.getMessage());
}

private void handleUnknownError(VeiGestException error) {
    showError("Ocorreu um erro inesperado.");
    
    // Log completo para análise
    Log.e("UnknownError", error.getMessage(), error);
}
```

---

## Tratamento por HTTP Code

```java
@Override
public void onError(@NonNull VeiGestException error) {
    int httpCode = error.getHttpCode();
    
    if (httpCode == 0) {
        // Erro de rede (sem HTTP code)
        handleNetworkError(error);
        return;
    }
    
    switch (httpCode) {
        case 400:
            showError("Requisição inválida: " + error.getMessage());
            break;
            
        case 401:
            redirectToLogin();
            break;
            
        case 403:
            showError("Acesso negado");
            break;
            
        case 404:
            showError("Não encontrado");
            break;
            
        case 409:
            showError("Conflito: " + error.getMessage());
            break;
            
        case 422:
            showValidationErrors(error);
            break;
            
        case 429:
            showError("Muitas requisições. Aguarde um momento.");
            break;
            
        case 500:
        case 502:
        case 503:
        case 504:
            showError("Servidor indisponível. Tente mais tarde.");
            break;
            
        default:
            showError(error.getMessage());
    }
}
```

---

## Erros de Validação

A API pode retornar erros de validação por campo:

### Estrutura do Erro

```json
{
  "success": false,
  "message": "Dados inválidos",
  "errors": {
    "email": ["O email é obrigatório", "O email deve ser válido"],
    "password": ["A senha deve ter pelo menos 8 caracteres"]
  }
}
```

### Tratamento

```java
private void handleValidationError(VeiGestException error) {
    Map<String, List<String>> validationErrors = error.getValidationErrors();
    
    if (validationErrors == null || validationErrors.isEmpty()) {
        showError(error.getMessage());
        return;
    }
    
    // Mapear campos do formulário
    Map<String, TextInputLayout> fieldMap = new HashMap<>();
    fieldMap.put("email", emailInputLayout);
    fieldMap.put("password", passwordInputLayout);
    fieldMap.put("nome", nomeInputLayout);
    
    // Mostrar erros nos campos
    for (Map.Entry<String, List<String>> entry : validationErrors.entrySet()) {
        String field = entry.getKey();
        String firstError = entry.getValue().get(0);
        
        TextInputLayout layout = fieldMap.get(field);
        if (layout != null) {
            layout.setError(firstError);
            layout.setErrorEnabled(true);
        }
    }
    
    // Scroll para o primeiro campo com erro
    String firstField = validationErrors.keySet().iterator().next();
    TextInputLayout firstLayout = fieldMap.get(firstField);
    if (firstLayout != null) {
        firstLayout.requestFocus();
    }
}
```

---

## Erro de Login

Tratamento específico para erros de login:

```java
sdk.auth().login(email, password, new VeiGestCallback<LoginResponse>() {
    @Override
    public void onSuccess(@NonNull LoginResponse response) {
        navigateToHome();
    }
    
    @Override
    public void onError(@NonNull VeiGestException error) {
        hideLoading();
        
        if (error.isAuthenticationError()) {
            // Credenciais inválidas
            showCredentialsError();
        } else if (error.isNetworkError()) {
            // Sem internet
            showNetworkError();
        } else if (error.isValidationError()) {
            // Campos inválidos
            showValidationErrors(error);
        } else {
            // Outro erro
            showGenericError(error.getMessage());
        }
    }
});

private void showCredentialsError() {
    // Limpar campo de password
    passwordInput.setText("");
    
    // Mostrar erro
    new AlertDialog.Builder(this)
        .setTitle("Erro de Login")
        .setMessage("Email ou password incorretos.\nVerifique suas credenciais e tente novamente.")
        .setPositiveButton("OK", null)
        .show();
}

private void showNetworkError() {
    new AlertDialog.Builder(this)
        .setTitle("Sem Conexão")
        .setMessage("Não foi possível conectar ao servidor.\nVerifique sua conexão à internet.")
        .setPositiveButton("Tentar Novamente", (d, w) -> doLogin())
        .setNegativeButton("Cancelar", null)
        .show();
}
```

---

## Factory Methods

O SDK fornece métodos estáticos para criar exceções:

```java
// Criar exceção de erro de rede
VeiGestException networkError = VeiGestException.networkError(cause);

// Criar exceção de autenticação
VeiGestException authError = VeiGestException.authenticationError("Token inválido");

// Criar exceção de timeout
VeiGestException timeoutError = VeiGestException.timeoutError();
```

---

## Logging de Erros

### Debug/Desenvolvimento

```java
@Override
public void onError(@NonNull VeiGestException error) {
    // Log completo para debug
    if (BuildConfig.DEBUG) {
        Log.e("VeiGestError", "Type: " + error.getType());
        Log.e("VeiGestError", "HTTP Code: " + error.getHttpCode());
        Log.e("VeiGestError", "Error Code: " + error.getErrorCode());
        Log.e("VeiGestError", "Message: " + error.getMessage());
        
        if (error.getValidationErrors() != null) {
            Log.e("VeiGestError", "Validation: " + error.getValidationErrors());
        }
        
        if (error.getCause() != null) {
            Log.e("VeiGestError", "Cause: ", error.getCause());
        }
    }
    
    handleError(error);
}
```

### Produção (Analytics/Crashlytics)

```java
@Override
public void onError(@NonNull VeiGestException error) {
    // Enviar para analytics apenas erros relevantes
    if (error.isServerError() || error.getType() == ErrorType.UNKNOWN) {
        // Firebase Crashlytics
        FirebaseCrashlytics.getInstance().recordException(error);
        
        // Ou outro serviço de analytics
        Analytics.logError("api_error", Map.of(
            "type", error.getType().name(),
            "http_code", String.valueOf(error.getHttpCode()),
            "message", error.getMessage()
        ));
    }
    
    handleError(error);
}
```

---

## Retry com Backoff

```java
public class RetryHelper {
    private int retryCount = 0;
    private static final int MAX_RETRIES = 3;
    private static final long BASE_DELAY = 1000; // 1 segundo
    
    public void execute(Runnable operation, Consumer<VeiGestException> onFinalError) {
        retryCount = 0;
        tryOperation(operation, onFinalError);
    }
    
    private void tryOperation(Runnable operation, Consumer<VeiGestException> onFinalError) {
        // Executar operação...
        // Se falhar:
    }
    
    public void handleError(VeiGestException error, 
                           Runnable operation, 
                           Consumer<VeiGestException> onFinalError) {
        // Só retry para erros de rede ou servidor
        if (!shouldRetry(error)) {
            onFinalError.accept(error);
            return;
        }
        
        if (retryCount >= MAX_RETRIES) {
            onFinalError.accept(error);
            return;
        }
        
        retryCount++;
        long delay = BASE_DELAY * (long) Math.pow(2, retryCount - 1); // Exponential backoff
        
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            tryOperation(operation, onFinalError);
        }, delay);
    }
    
    private boolean shouldRetry(VeiGestException error) {
        return error.isNetworkError() || error.isServerError();
    }
}
```

---

## Tratamento Global

Criar um handler centralizado:

```java
public class ErrorHandler {
    
    private final Context context;
    private final Runnable onAuthError;
    
    public ErrorHandler(Context context, Runnable onAuthError) {
        this.context = context;
        this.onAuthError = onAuthError;
    }
    
    public void handle(VeiGestException error) {
        handle(error, null);
    }
    
    public void handle(VeiGestException error, @Nullable Runnable onRetry) {
        switch (error.getType()) {
            case NETWORK:
                showNetworkError(onRetry);
                break;
                
            case AUTHENTICATION:
                onAuthError.run();
                break;
                
            case AUTHORIZATION:
                showToast("Sem permissão para esta ação");
                break;
                
            case VALIDATION:
                showToast(error.getMessage());
                break;
                
            case NOT_FOUND:
                showToast("Recurso não encontrado");
                break;
                
            case SERVER:
                showServerError(onRetry);
                break;
                
            default:
                showToast("Erro: " + error.getMessage());
        }
    }
    
    private void showNetworkError(@Nullable Runnable onRetry) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
            .setTitle("Sem Conexão")
            .setMessage("Verifique sua conexão à internet.");
        
        if (onRetry != null) {
            builder.setPositiveButton("Tentar novamente", (d, w) -> onRetry.run());
            builder.setNegativeButton("Cancelar", null);
        } else {
            builder.setPositiveButton("OK", null);
        }
        
        builder.show();
    }
    
    private void showServerError(@Nullable Runnable onRetry) {
        // Similar ao anterior
    }
    
    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}

// Uso
ErrorHandler errorHandler = new ErrorHandler(this, this::redirectToLogin);

sdk.vehicles().list(new VeiGestCallback<List<Vehicle>>() {
    @Override
    public void onSuccess(@NonNull List<Vehicle> vehicles) {
        displayVehicles(vehicles);
    }
    
    @Override
    public void onError(@NonNull VeiGestException error) {
        errorHandler.handle(error, () -> loadVehicles());
    }
});
```

---

## Códigos de Erro Comuns

| Código | Tipo | Significado | Ação Recomendada |
|--------|------|-------------|------------------|
| 400 | VALIDATION | Requisição malformada | Corrigir dados |
| 401 | AUTHENTICATION | Não autenticado | Fazer login |
| 403 | AUTHORIZATION | Sem permissão | Informar utilizador |
| 404 | NOT_FOUND | Não encontrado | Voltar/Atualizar |
| 409 | VALIDATION | Conflito (duplicado) | Informar utilizador |
| 422 | VALIDATION | Dados inválidos | Mostrar erros |
| 429 | UNKNOWN | Rate limit | Aguardar |
| 500 | SERVER | Erro interno | Retry/Aguardar |
| 502 | SERVER | Bad Gateway | Retry/Aguardar |
| 503 | SERVER | Serviço indisponível | Retry/Aguardar |
| 504 | SERVER | Gateway Timeout | Retry/Aumentar timeout |

---

## Próximos Passos

- [Callbacks e Padrões Assíncronos](CALLBACKS.md)
- [Boas Práticas](BEST_PRACTICES.md)
- [Troubleshooting](TROUBLESHOOTING.md)
