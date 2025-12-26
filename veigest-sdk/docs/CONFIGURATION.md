# Configuração do SDK

Este documento descreve todas as opções de configuração disponíveis no VeiGest SDK.

---

## VeiGestConfig

A classe `VeiGestConfig` controla o comportamento do SDK. Use o Builder para criar uma instância:

```java
VeiGestConfig config = new VeiGestConfig.Builder()
    .setBaseUrl("https://veigestback.dryadlang.org")
    .setDebugMode(true)
    .setConnectTimeout(30)
    .setReadTimeout(30)
    .setWriteTimeout(30)
    .build();

VeiGestSDK.init(context, config);
```

---

## Opções de Configuração

### setBaseUrl(String url)

Define o URL base da API VeiGest.

| Parâmetro | Tipo | Default | Descrição |
|-----------|------|---------|-----------|
| `url` | String | `https://veigestback.dryadlang.org` | URL base da API |

```java
// Produção (padrão)
.setBaseUrl("https://veigestback.dryadlang.org")

// Staging/Testes
.setBaseUrl("https://staging.veigestback.dryadlang.org")

// Desenvolvimento local
.setBaseUrl("http://10.0.2.2:8000")  // Emulador Android
.setBaseUrl("http://192.168.1.100:8000")  // Dispositivo físico
```

> ⚠️ **Importante**: Para URLs HTTP (não HTTPS), adicione ao `AndroidManifest.xml`:
> ```xml
> <application
>     android:usesCleartextTraffic="true"
>     ...>
> ```

---

### setDebugMode(boolean enabled)

Ativa o modo de debug com logs detalhados.

| Parâmetro | Tipo | Default | Descrição |
|-----------|------|---------|-----------|
| `enabled` | boolean | `false` | Ativa logs HTTP detalhados |

```java
// Usar BuildConfig para ativar apenas em debug
.setDebugMode(BuildConfig.DEBUG)
```

**Quando ativo**:
- Logs detalhados de todas as requisições HTTP
- Headers de requisição/resposta
- Body de requisição/resposta
- Tempo de execução

**Exemplo de log em modo debug**:
```
D/OkHttp: --> POST https://veigestback.dryadlang.org/api/auth/login
D/OkHttp: Content-Type: application/json
D/OkHttp: {"email":"user@email.com","password":"***"}
D/OkHttp: --> END POST (52-byte body)
D/OkHttp: <-- 200 OK (245ms)
D/OkHttp: {"success":true,"data":{"access_token":"eyJ..."}}
D/OkHttp: <-- END HTTP (512-byte body)
```

> ⚠️ **Atenção**: **Nunca** ative o modo debug em produção - pode expor dados sensíveis nos logs.

---

### setConnectTimeout(int seconds)

Define o timeout de conexão.

| Parâmetro | Tipo | Default | Descrição |
|-----------|------|---------|-----------|
| `seconds` | int | `30` | Timeout em segundos |

```java
// Conexões mais rápidas com timeout curto
.setConnectTimeout(10)

// Redes lentas com timeout maior
.setConnectTimeout(60)
```

---

### setReadTimeout(int seconds)

Define o timeout de leitura de dados.

| Parâmetro | Tipo | Default | Descrição |
|-----------|------|---------|-----------|
| `seconds` | int | `30` | Timeout em segundos |

```java
// Para operações com muitos dados
.setReadTimeout(60)
```

---

### setWriteTimeout(int seconds)

Define o timeout de escrita/envio de dados.

| Parâmetro | Tipo | Default | Descrição |
|-----------|------|---------|-----------|
| `seconds` | int | `30` | Timeout em segundos |

```java
// Para uploads de ficheiros grandes
.setWriteTimeout(120)
```

---

## Configurações por Ambiente

### Desenvolvimento

```java
VeiGestConfig devConfig = new VeiGestConfig.Builder()
    .setBaseUrl("http://10.0.2.2:8000")
    .setDebugMode(true)
    .setConnectTimeout(60)  // Mais tolerante em dev
    .setReadTimeout(60)
    .build();
```

### Staging/QA

```java
VeiGestConfig stagingConfig = new VeiGestConfig.Builder()
    .setBaseUrl("https://staging.veigestback.dryadlang.org")
    .setDebugMode(true)
    .setConnectTimeout(30)
    .setReadTimeout(30)
    .build();
```

### Produção

```java
VeiGestConfig prodConfig = new VeiGestConfig.Builder()
    .setBaseUrl("https://veigestback.dryadlang.org")
    .setDebugMode(false)  // NUNCA true em produção
    .setConnectTimeout(30)
    .setReadTimeout(30)
    .build();
```

### Configuração Dinâmica com BuildConfig

```java
VeiGestConfig config = new VeiGestConfig.Builder()
    .setBaseUrl(BuildConfig.API_BASE_URL)
    .setDebugMode(BuildConfig.DEBUG)
    .build();
```

Defina em `app/build.gradle.kts`:
```kotlin
android {
    buildTypes {
        debug {
            buildConfigField("String", "API_BASE_URL", "\"http://10.0.2.2:8000\"")
        }
        release {
            buildConfigField("String", "API_BASE_URL", "\"https://veigestback.dryadlang.org\"")
        }
    }
}
```

---

## Acessar Configurações

Após inicializar, pode acessar as configurações:

```java
VeiGestSDK sdk = VeiGestSDK.getInstance();
VeiGestConfig config = sdk.getConfig();

String baseUrl = config.getBaseUrl();
boolean isDebug = config.isDebugMode();
int connectTimeout = config.getConnectTimeout();
int readTimeout = config.getReadTimeout();
int writeTimeout = config.getWriteTimeout();

Log.d("Config", "Base URL: " + baseUrl);
Log.d("Config", "Debug Mode: " + isDebug);
Log.d("Config", "Timeouts: " + connectTimeout + "s / " + readTimeout + "s / " + writeTimeout + "s");
```

---

## Constantes Disponíveis

```java
// Valores padrão
VeiGestConfig.DEFAULT_BASE_URL        // "https://veigestback.dryadlang.org"
VeiGestConfig.DEFAULT_CONNECT_TIMEOUT // 30
VeiGestConfig.DEFAULT_READ_TIMEOUT    // 30
VeiGestConfig.DEFAULT_WRITE_TIMEOUT   // 30
```

---

## Configuração Mínima

Se não precisar personalizar nada, use a configuração padrão:

```java
// Usa todos os valores padrão
VeiGestConfig config = new VeiGestConfig.Builder().build();
VeiGestSDK.init(context, config);
```

---

## Reinicialização do SDK

Se precisar alterar as configurações em runtime:

```java
// Destruir instância atual
VeiGestSDK.destroy();

// Criar nova configuração
VeiGestConfig newConfig = new VeiGestConfig.Builder()
    .setBaseUrl("https://outro-servidor.com")
    .build();

// Reinicializar
VeiGestSDK.init(context, newConfig);
```

> ⚠️ **Nota**: `destroy()` também faz logout do utilizador.

---

## Verificar Estado do SDK

```java
// Verificar se foi inicializado
if (VeiGestSDK.isInitialized()) {
    VeiGestSDK sdk = VeiGestSDK.getInstance();
    // usar o SDK
} else {
    // inicializar primeiro
}
```

---

## Exemplo Completo

```java
public class MyApplication extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        initVeiGestSDK();
    }
    
    private void initVeiGestSDK() {
        VeiGestConfig.Builder builder = new VeiGestConfig.Builder();
        
        // Configurar URL base
        if (BuildConfig.DEBUG) {
            builder.setBaseUrl(BuildConfig.API_DEBUG_URL);
            builder.setDebugMode(true);
            builder.setConnectTimeout(60);
        } else {
            builder.setBaseUrl(BuildConfig.API_RELEASE_URL);
            builder.setDebugMode(false);
            builder.setConnectTimeout(30);
        }
        
        // Timeouts comuns
        builder.setReadTimeout(30);
        builder.setWriteTimeout(60);  // Maior para uploads
        
        VeiGestConfig config = builder.build();
        
        try {
            VeiGestSDK.init(this, config);
            Log.i("VeiGest", "SDK inicializado: " + config.getBaseUrl());
        } catch (Exception e) {
            Log.e("VeiGest", "Falha ao inicializar SDK", e);
        }
    }
}
```

---

## Próximos Passos

- [Configurar Autenticação](AUTHENTICATION.md)
- [Usar os Serviços](SERVICES.md)
- [Tratamento de Erros](EXCEPTIONS.md)
