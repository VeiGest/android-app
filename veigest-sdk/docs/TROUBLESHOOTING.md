# Troubleshooting

Guia de resolução de problemas comuns no VeiGest SDK.

---

## Índice

1. [Problemas de Conexão](#problemas-de-conexão)
2. [Problemas de Autenticação](#problemas-de-autenticação)
3. [Problemas de Build](#problemas-de-build)
4. [Erros de Runtime](#erros-de-runtime)
5. [Performance](#performance)
6. [FAQ](#faq)

---

## Problemas de Conexão

### Erro: "Unable to resolve host"

**Sintoma:** A API não responde e recebe erro de DNS.

**Causas:**
- Dispositivo sem conexão à internet
- URL da API incorreta
- DNS não consegue resolver o domínio

**Soluções:**
```java
// 1. Verificar conexão antes de fazer chamadas
public boolean isNetworkAvailable(Context context) {
    ConnectivityManager cm = (ConnectivityManager) 
        context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    return activeNetwork != null && activeNetwork.isConnected();
}

// 2. Verificar URL configurada
VeiGestConfig config = new VeiGestConfig.Builder()
    .baseUrl("https://api.veigest.com/api/") // Confirmar URL correta
    .build();

// 3. Testar URL no browser primeiro
```

### Erro: "Connection timed out"

**Sintoma:** Chamadas demoram muito e falham.

**Soluções:**
```java
// Aumentar timeouts
VeiGestConfig config = new VeiGestConfig.Builder()
    .baseUrl("https://api.veigest.com/api/")
    .connectTimeout(60)  // Aumentar para 60 segundos
    .readTimeout(60)
    .writeTimeout(60)
    .build();
```

### Erro: "SSL Handshake Failed"

**Sintoma:** Erro de certificado SSL.

**Causas:**
- Certificado do servidor inválido ou expirado
- Dispositivo com data/hora incorreta
- Certificado auto-assinado (desenvolvimento)

**Soluções:**
```java
// Apenas para desenvolvimento - NUNCA em produção!
if (BuildConfig.DEBUG) {
    // Criar OkHttpClient com SSL desabilitado
    OkHttpClient unsafeClient = getUnsafeOkHttpClient();
}

// Em produção, verificar:
// 1. Data/hora do dispositivo
// 2. Certificado do servidor
// 3. Configuração de network security config
```

**Network Security Config** (`res/xml/network_security_config.xml`):
```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="false">
        <domain includeSubdomains="true">api.veigest.com</domain>
        <trust-anchors>
            <certificates src="system"/>
        </trust-anchors>
    </domain-config>
</network-security-config>
```

---

## Problemas de Autenticação

### Erro 401: "Unauthorized"

**Sintoma:** Todas as chamadas retornam 401.

**Causas:**
- Token expirado
- Token não enviado
- Token inválido

**Soluções:**
```java
// 1. Verificar se está autenticado
if (!sdk.auth().isAuthenticated()) {
    // Redirecionar para login
    startLoginActivity();
    return;
}

// 2. Tentar refresh do token
sdk.auth().refreshToken(new VeiGestCallback<Void>() {
    @Override
    public void onSuccess(Void result) {
        // Token atualizado, repetir operação
        retryOperation();
    }
    
    @Override
    public void onError(@NonNull VeiGestException error) {
        // Refresh falhou, fazer login novamente
        sdk.auth().logout();
        startLoginActivity();
    }
});

// 3. Debug: Verificar token armazenado
String token = sdk.auth().getAccessToken();
Log.d("Auth", "Token atual: " + (token != null ? "presente" : "null"));
```

### Erro 403: "Forbidden"

**Sintoma:** Acesso negado a recursos específicos.

**Causas:**
- Utilizador não tem permissões
- Recurso pertence a outra empresa
- Operação não permitida para o tipo de utilizador

**Soluções:**
```java
// 1. Verificar permissões do utilizador
sdk.users().getCurrentUser(new VeiGestCallback<User>() {
    @Override
    public void onSuccess(@NonNull User user) {
        String tipo = user.getTipo();
        Log.d("User", "Tipo: " + tipo);
        // condutor, gestor, admin
        
        if (!"admin".equals(tipo) && !"gestor".equals(tipo)) {
            showError("Apenas gestores podem aceder a esta funcionalidade");
        }
    }
    
    @Override
    public void onError(@NonNull VeiGestException error) {
        // Erro ao obter utilizador
    }
});
```

### Login sempre falha

**Sintoma:** Credenciais corretas mas login não funciona.

**Checklist:**
```java
// 1. Verificar credenciais (espaços em branco?)
String email = emailInput.getText().toString().trim();
String password = passwordInput.getText().toString();

// 2. Verificar URL da API
Log.d("Login", "URL: " + config.getBaseUrl());

// 3. Testar com curl/Postman primeiro
// curl -X POST https://api.veigest.com/api/auth/login \
//   -H "Content-Type: application/json" \
//   -d '{"email":"test@test.com","password":"123456"}'

// 4. Ativar debug para ver requisição
VeiGestConfig config = new VeiGestConfig.Builder()
    .baseUrl("...")
    .debug(true)  // Ver logs
    .build();
```

---

## Problemas de Build

### Erro: "Duplicate class"

**Sintoma:** Conflito de dependências.

**Solução no `build.gradle.kts`:**
```kotlin
dependencies {
    implementation("com.veigest:sdk:1.0.0") {
        exclude(group = "com.google.code.gson", module = "gson")
    }
    
    // Usar versão específica
    implementation("com.google.code.gson:gson:2.10.1")
}
```

### Erro: "Cannot find symbol VeiGestSDK"

**Causas:**
- SDK não adicionado às dependências
- Repositório não configurado
- Sync do Gradle não feito

**Soluções:**
```kotlin
// 1. Verificar settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") } // Se usar JitPack
    }
}

// 2. Verificar build.gradle.kts (app)
dependencies {
    implementation("com.veigest:sdk:1.0.0")
}

// 3. Sync Gradle
// File -> Sync Project with Gradle Files
```

### Erro: "Minimum SDK version"

**Sintoma:** SDK requer versão Android mais alta.

**Solução:**
```kotlin
android {
    defaultConfig {
        minSdk = 24  // SDK requer mínimo 24
    }
}
```

---

## Erros de Runtime

### NullPointerException no SDK

**Sintoma:** NPE ao aceder serviços.

**Causa:** SDK não inicializado.

**Solução:**
```java
// Inicializar no Application.onCreate()
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        
        VeiGestConfig config = new VeiGestConfig.Builder()
            .baseUrl("https://api.veigest.com/api/")
            .build();
        
        VeiGestSDK.initialize(this, config);
    }
}

// AndroidManifest.xml
<application
    android:name=".MyApp"
    ...>
```

### Callback não é chamado

**Sintoma:** Nem `onSuccess` nem `onError` são executados.

**Causas:**
- Activity destruída antes do callback
- Exceção silenciosa

**Soluções:**
```java
// 1. Verificar ciclo de vida
@Override
public void onDestroy() {
    super.onDestroy();
    // Callbacks podem não ser chamados se Activity destruída
}

// 2. Usar ViewModel para sobreviver a rotações
public class MyViewModel extends ViewModel {
    // Manter estado aqui
}

// 3. Adicionar logs para debug
sdk.vehicles().list(new VeiGestCallback<List<Vehicle>>() {
    @Override
    public void onSuccess(@NonNull List<Vehicle> vehicles) {
        Log.d("Callback", "onSuccess chamado: " + vehicles.size());
        // ...
    }
    
    @Override
    public void onError(@NonNull VeiGestException error) {
        Log.e("Callback", "onError chamado: " + error.getMessage(), error);
        // ...
    }
});
```

### Crash: "NetworkOnMainThreadException"

**Sintoma:** App crasha ao fazer chamada de rede.

**Causa:** O SDK já faz chamadas em background, mas se usar diretamente o Retrofit pode ocorrer.

**Solução:**
```java
// Sempre use os métodos assíncronos do SDK
sdk.vehicles().list(callback);  // ✓ Correto

// Nunca tente fazer chamadas síncronas na main thread
```

### Crash: "CalledFromWrongThreadException"

**Sintoma:** Crash ao atualizar UI no callback.

**Causa:** Callbacks executam em background thread.

**Solução:**
```java
sdk.vehicles().list(new VeiGestCallback<List<Vehicle>>() {
    @Override
    public void onSuccess(@NonNull List<Vehicle> vehicles) {
        // Executar na UI thread
        runOnUiThread(() -> {
            adapter.submitList(vehicles);
            progressBar.setVisibility(View.GONE);
        });
    }
    
    @Override
    public void onError(@NonNull VeiGestException error) {
        runOnUiThread(() -> {
            showError(error.getMessage());
        });
    }
});

// Ou usar Handler
new Handler(Looper.getMainLooper()).post(() -> {
    // Código UI aqui
});
```

---

## Performance

### App lenta após muitas chamadas

**Causa:** Múltiplas chamadas simultâneas, sem cache.

**Soluções:**
```java
// 1. Implementar cache
private Map<Integer, Vehicle> vehicleCache = new HashMap<>();

public void getVehicle(int id, VeiGestCallback<Vehicle> callback) {
    Vehicle cached = vehicleCache.get(id);
    if (cached != null) {
        callback.onSuccess(cached);
        return;
    }
    
    sdk.vehicles().get(id, new VeiGestCallback<Vehicle>() {
        @Override
        public void onSuccess(@NonNull Vehicle vehicle) {
            vehicleCache.put(id, vehicle);
            callback.onSuccess(vehicle);
        }
        // ...
    });
}

// 2. Limitar chamadas paralelas
Semaphore semaphore = new Semaphore(5); // Máximo 5 chamadas simultâneas

// 3. Usar paginação
sdk.vehicles().list(page, 20, callback); // 20 items por página
```

### Memory leaks

**Sintoma:** Memória cresce continuamente.

**Soluções:**
```java
// 1. Usar WeakReference em callbacks longos
public class SafeCallback<T> implements VeiGestCallback<T> {
    private final WeakReference<Activity> activityRef;
    
    public SafeCallback(Activity activity) {
        this.activityRef = new WeakReference<>(activity);
    }
    
    @Override
    public void onSuccess(@NonNull T result) {
        Activity activity = activityRef.get();
        if (activity != null && !activity.isFinishing()) {
            // Atualizar UI
        }
    }
    // ...
}

// 2. Cancelar operações em onDestroy
@Override
protected void onDestroy() {
    super.onDestroy();
    // Marcar flag para ignorar callbacks pendentes
    isDestroyed = true;
}
```

---

## FAQ

### Como testar em ambiente de desenvolvimento?

```java
// Usar URL de desenvolvimento
String baseUrl = BuildConfig.DEBUG 
    ? "https://dev-api.veigest.com/api/" 
    : "https://api.veigest.com/api/";

VeiGestConfig config = new VeiGestConfig.Builder()
    .baseUrl(baseUrl)
    .debug(BuildConfig.DEBUG)
    .build();
```

### Como ver os logs das requisições?

```java
// Ativar debug
VeiGestConfig config = new VeiGestConfig.Builder()
    .baseUrl("...")
    .debug(true)  // Ativa logging
    .build();

// Filtrar no Logcat por "OkHttp" ou "VeiGest"
```

### Como funciona o refresh automático de token?

O SDK automaticamente:
1. Deteta quando o token expira (401)
2. Tenta refresh usando o refresh token
3. Se sucesso, repete a operação original
4. Se falha, chama `onError` com UNAUTHORIZED

### Posso usar o SDK em vários módulos?

Sim, inicialize apenas uma vez:

```java
// No módulo app
VeiGestSDK.initialize(context, config);

// Noutros módulos, obter instância
VeiGestSDK sdk = VeiGestSDK.getInstance();
```

### Como fazer logout completo?

```java
sdk.auth().logout(new VeiGestCallback<Void>() {
    @Override
    public void onSuccess(Void result) {
        // Limpar dados locais
        clearLocalData();
        
        // Navegar para login
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    
    @Override
    public void onError(@NonNull VeiGestException error) {
        // Mesmo com erro, fazer logout local
        clearLocalData();
        navigateToLogin();
    }
});
```

### O SDK funciona offline?

O SDK requer conexão à internet. Para funcionalidade offline:

```java
// 1. Implementar cache local
Room database ou SharedPreferences

// 2. Verificar conexão
if (isNetworkAvailable()) {
    sdk.vehicles().list(callback);
} else {
    // Carregar do cache local
    List<Vehicle> cached = localDatabase.getVehicles();
    callback.onSuccess(cached);
}

// 3. Sincronizar quando online
if (isNetworkAvailable() && hasPendingChanges()) {
    syncPendingChanges();
}
```

### Como lidar com múltiplas empresas?

```java
// O SDK filtra automaticamente por empresa do utilizador
// Gestores veem apenas dados da sua empresa

// Para admins que gerem várias empresas:
sdk.companies().list(new VeiGestCallback<List<Company>>() {
    @Override
    public void onSuccess(@NonNull List<Company> companies) {
        // Mostrar selector de empresa
        showCompanySelector(companies);
    }
    // ...
});
```

---

## Contacto de Suporte

Se o problema persistir:

1. **Documentação:** Consulte os outros guias da documentação
2. **GitHub Issues:** Abra uma issue com:
   - Versão do SDK
   - Versão Android
   - Código que causa o erro
   - Stack trace completo
   - Passos para reproduzir

---

## Próximos Passos

- [Boas Práticas](BEST_PRACTICES.md)
- [Tratamento de Erros](EXCEPTIONS.md)
- [Quick Start](QUICK_START.md)
