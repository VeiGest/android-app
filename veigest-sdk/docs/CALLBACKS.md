# Callbacks e Padrões Assíncronos

Este documento descreve os padrões de callback utilizados no VeiGest SDK para operações assíncronas.

---

## VeiGestCallback&lt;T&gt;

Interface principal para receber resultados de operações assíncronas.

### Definição

```java
public interface VeiGestCallback<T> {
    
    /**
     * Chamado quando a operação é concluída com sucesso.
     * @param result Resultado da operação (nunca null)
     */
    void onSuccess(@NonNull T result);
    
    /**
     * Chamado quando a operação falha.
     * @param error Exceção com detalhes do erro
     */
    void onError(@NonNull VeiGestException error);
}
```

### Uso Básico

```java
sdk.vehicles().list(new VeiGestCallback<List<Vehicle>>() {
    @Override
    public void onSuccess(@NonNull List<Vehicle> vehicles) {
        // Processar lista de veículos
        for (Vehicle v : vehicles) {
            Log.d("Vehicle", v.getMatricula());
        }
    }
    
    @Override
    public void onError(@NonNull VeiGestException error) {
        // Tratar erro
        Log.e("Error", error.getMessage());
    }
});
```

---

## Thread Safety

Todos os callbacks são executados na **Main Thread (UI Thread)**, permitindo atualizar a interface diretamente:

```java
sdk.users().getProfile(new VeiGestCallback<User>() {
    @Override
    public void onSuccess(@NonNull User user) {
        // Pode atualizar UI diretamente - já estamos na Main Thread
        textViewName.setText(user.getNome());
        textViewEmail.setText(user.getEmail());
        progressBar.setVisibility(View.GONE);
    }
    
    @Override
    public void onError(@NonNull VeiGestException error) {
        // Também na Main Thread
        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
    }
});
```

---

## SimpleCallback

Callback simplificado que ignora erros. Use apenas para operações não críticas.

### Definição

```java
public abstract class SimpleCallback<T> implements VeiGestCallback<T> {
    @Override
    public void onError(@NonNull VeiGestException error) {
        // Ignora o erro silenciosamente
    }
}
```

### Uso

```java
// Carregar dados não críticos - se falhar, não faz nada
sdk.alerts().listActive(new VeiGestCallback.SimpleCallback<List<Alert>>() {
    @Override
    public void onSuccess(@NonNull List<Alert> alerts) {
        updateBadgeCount(alerts.size());
    }
});
```

> ⚠️ **Atenção**: Use `SimpleCallback` apenas para operações que podem falhar silenciosamente sem impacto na experiência do utilizador.

---

## CompletionCallback

Callback que executa uma ação tanto em sucesso quanto em erro.

### Definição

```java
public abstract class CompletionCallback<T> implements VeiGestCallback<T> {
    
    /**
     * Chamado quando a operação completa (sucesso ou erro).
     */
    public abstract void onComplete();
    
    @Override
    public void onSuccess(@NonNull T result) {
        onComplete();
    }
    
    @Override
    public void onError(@NonNull VeiGestException error) {
        onComplete();
    }
}
```

### Uso

```java
// Esconder loading independente do resultado
showLoading(true);

sdk.vehicles().list(new VeiGestCallback.CompletionCallback<List<Vehicle>>() {
    @Override
    public void onComplete() {
        showLoading(false);  // Esconde loading em qualquer caso
    }
    
    @Override
    public void onSuccess(@NonNull List<Vehicle> vehicles) {
        super.onSuccess(vehicles);  // Chama onComplete()
        displayVehicles(vehicles);
    }
    
    @Override
    public void onError(@NonNull VeiGestException error) {
        super.onError(error);  // Chama onComplete()
        showError(error.getMessage());
    }
});
```

---

## Padrões de Uso Recomendados

### 1. Tratamento Completo de Erros

```java
sdk.auth().login(email, password, new VeiGestCallback<LoginResponse>() {
    @Override
    public void onSuccess(@NonNull LoginResponse response) {
        // Login bem sucedido
        navigateToHome();
    }
    
    @Override
    public void onError(@NonNull VeiGestException error) {
        // Tratamento específico por tipo de erro
        switch (error.getType()) {
            case AUTHENTICATION:
                showError("Credenciais inválidas");
                break;
            case NETWORK:
                showError("Verifique sua conexão");
                break;
            case SERVER:
                showError("Servidor indisponível");
                break;
            case VALIDATION:
                showValidationErrors(error.getValidationErrors());
                break;
            default:
                showError(error.getMessage());
        }
    }
});
```

### 2. Com Loading State

```java
private void loadVehicles() {
    showLoading(true);
    errorView.setVisibility(View.GONE);
    
    sdk.vehicles().list(new VeiGestCallback<List<Vehicle>>() {
        @Override
        public void onSuccess(@NonNull List<Vehicle> vehicles) {
            showLoading(false);
            
            if (vehicles.isEmpty()) {
                showEmptyState();
            } else {
                adapter.setData(vehicles);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
        
        @Override
        public void onError(@NonNull VeiGestException error) {
            showLoading(false);
            showErrorView(error);
        }
    });
}
```

### 3. Operações Encadeadas

```java
// Login -> Carregar Perfil -> Carregar Dashboard
sdk.auth().login(email, password, new VeiGestCallback<LoginResponse>() {
    @Override
    public void onSuccess(@NonNull LoginResponse response) {
        // Após login, carregar perfil
        loadUserProfile();
    }
    
    @Override
    public void onError(@NonNull VeiGestException error) {
        showLoginError(error);
    }
});

private void loadUserProfile() {
    sdk.users().getProfile(new VeiGestCallback<User>() {
        @Override
        public void onSuccess(@NonNull User user) {
            // Após perfil, carregar dashboard
            saveUserLocally(user);
            loadDashboard();
        }
        
        @Override
        public void onError(@NonNull VeiGestException error) {
            // Continuar mesmo com erro no perfil
            loadDashboard();
        }
    });
}
```

### 4. Retry Pattern

```java
private int retryCount = 0;
private static final int MAX_RETRIES = 3;

private void loadDataWithRetry() {
    sdk.vehicles().list(new VeiGestCallback<List<Vehicle>>() {
        @Override
        public void onSuccess(@NonNull List<Vehicle> vehicles) {
            retryCount = 0;  // Reset counter
            displayVehicles(vehicles);
        }
        
        @Override
        public void onError(@NonNull VeiGestException error) {
            if (error.isNetworkError() && retryCount < MAX_RETRIES) {
                retryCount++;
                // Aguardar e tentar novamente
                new Handler(Looper.getMainLooper()).postDelayed(
                    () -> loadDataWithRetry(), 
                    2000 * retryCount  // Backoff exponencial
                );
            } else {
                retryCount = 0;
                showError(error);
            }
        }
    });
}
```

### 5. Com SwipeRefreshLayout

```java
private SwipeRefreshLayout swipeRefresh;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    swipeRefresh = findViewById(R.id.swipeRefresh);
    swipeRefresh.setOnRefreshListener(this::loadVehicles);
    
    loadVehicles();
}

private void loadVehicles() {
    sdk.vehicles().list(new VeiGestCallback<List<Vehicle>>() {
        @Override
        public void onSuccess(@NonNull List<Vehicle> vehicles) {
            swipeRefresh.setRefreshing(false);
            adapter.setData(vehicles);
        }
        
        @Override
        public void onError(@NonNull VeiGestException error) {
            swipeRefresh.setRefreshing(false);
            Snackbar.make(rootView, error.getMessage(), Snackbar.LENGTH_LONG)
                .setAction("Tentar novamente", v -> loadVehicles())
                .show();
        }
    });
}
```

---

## Uso com Kotlin

### Callback Básico

```kotlin
sdk.vehicles().list(object : VeiGestCallback<List<Vehicle>> {
    override fun onSuccess(vehicles: List<Vehicle>) {
        vehicles.forEach { vehicle ->
            println("${vehicle.matricula} - ${vehicle.marca}")
        }
    }
    
    override fun onError(error: VeiGestException) {
        println("Erro: ${error.message}")
    }
})
```

### Extensão para Coroutines (Opcional)

```kotlin
// Criar extensão
suspend fun <T> suspendCallback(call: (VeiGestCallback<T>) -> Unit): T {
    return suspendCancellableCoroutine { continuation ->
        call(object : VeiGestCallback<T> {
            override fun onSuccess(result: T) {
                continuation.resume(result)
            }
            
            override fun onError(error: VeiGestException) {
                continuation.resumeWithException(error)
            }
        })
    }
}

// Uso
lifecycleScope.launch {
    try {
        val vehicles = suspendCallback<List<Vehicle>> { callback ->
            sdk.vehicles().list(callback)
        }
        displayVehicles(vehicles)
    } catch (e: VeiGestException) {
        showError(e.message)
    }
}
```

### Extensão com Result

```kotlin
// Criar extensão
suspend fun <T> safeCallback(call: (VeiGestCallback<T>) -> Unit): Result<T> {
    return suspendCancellableCoroutine { continuation ->
        call(object : VeiGestCallback<T> {
            override fun onSuccess(result: T) {
                continuation.resume(Result.success(result))
            }
            
            override fun onError(error: VeiGestException) {
                continuation.resume(Result.failure(error))
            }
        })
    }
}

// Uso
lifecycleScope.launch {
    val result = safeCallback<List<Vehicle>> { callback ->
        sdk.vehicles().list(callback)
    }
    
    result.onSuccess { vehicles ->
        displayVehicles(vehicles)
    }.onFailure { error ->
        showError(error.message)
    }
}
```

---

## Operações sem Callback

Algumas operações permitem uso sem callback:

```java
// Logout sem esperar resposta
sdk.auth().logout();

// Equivalente a:
sdk.auth().logout(null);
```

---

## Cancelamento de Operações

Atualmente o SDK não suporta cancelamento de operações em andamento. O callback sempre será chamado quando a operação completar.

Para cenários onde a Activity/Fragment pode ser destruída:

```java
public class MyFragment extends Fragment {
    private boolean isActive = true;
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isActive = false;  // Marcar como inativo
    }
    
    private void loadData() {
        sdk.vehicles().list(new VeiGestCallback<List<Vehicle>>() {
            @Override
            public void onSuccess(@NonNull List<Vehicle> vehicles) {
                if (!isActive) return;  // Verificar antes de usar views
                displayVehicles(vehicles);
            }
            
            @Override
            public void onError(@NonNull VeiGestException error) {
                if (!isActive) return;
                showError(error);
            }
        });
    }
}
```

---

## Próximos Passos

- [Tratamento de Erros](EXCEPTIONS.md)
- [Referência de Serviços](SERVICES.md)
- [Boas Práticas](BEST_PRACTICES.md)
