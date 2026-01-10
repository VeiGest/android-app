# Troubleshooting - VeiGest SDK

Este guia ajuda a resolver problemas comuns durante o desenvolvimento com o VeiGest SDK.

---

## Problemas de Compilação

### Erro: "Cannot find symbol: SingletonVeiGest"

**Causa:** SDK não está incluído como dependência.

**Solução:**
1. Verificar `settings.gradle.kts`:
   ```kotlin
   include(":veigest-sdk")
   ```

2. Verificar `app/build.gradle.kts`:
   ```kotlin
   dependencies {
       implementation(project(":veigest-sdk"))
   }
   ```

3. Sincronizar projeto (Sync Now)

---

### Erro: "Duplicate class com.android.volley..."

**Causa:** Volley duplicado no projeto.

**Solução:** Remover Volley do `app/build.gradle.kts` (já está no SDK):
```kotlin
// REMOVER esta linha do app
// implementation("com.android.volley:volley:1.2.1")
```

---

### Erro: "Java 11 required but using Java 8"

**Causa:** Versão do Java incompatível.

**Solução:** Verificar `compileOptions` no `build.gradle.kts`:
```kotlin
android {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
```

---

## Problemas em Runtime

### NullPointerException: "getInstance() returns null"

**Causa:** Application não configurada ou contexto inválido.

**Solução:**

1. Verificar AndroidManifest.xml:
   ```xml
   <application
       android:name=".VeiGestApplication"
       ...>
   ```

2. Usar contexto válido:
   ```java
   // Em Activity
   SingletonVeiGest.getInstance(this);
   
   // Em Fragment
   SingletonVeiGest.getInstance(requireContext());
   
   // NÃO usar getContext() que pode ser null
   ```

---

### NetworkError: "Unable to resolve host"

**Causa:** Problemas de rede ou URL inválida.

**Diagnóstico:**
```java
// Verificar conectividade
if (!VeiGestJsonParser.isConnectionInternet(context)) {
    Log.e(TAG, "Sem conexão à internet");
}

// Verificar URL configurada
Log.d(TAG, "URL: " + singleton.getBaseUrl());
```

**Soluções:**
1. Verificar permissão INTERNET no manifest
2. Verificar URL base (não esquecer `/api/v1`)
3. Se usando HTTP (não HTTPS), adicionar ao manifest:
   ```xml
   <application
       android:usesCleartextTraffic="true"
       ...>
   ```

---

### AuthError: "401 Unauthorized"

**Causa:** Token expirado ou inválido.

**Diagnóstico:**
```java
Log.d(TAG, "Autenticado: " + singleton.isAuthenticated());
Log.d(TAG, "Token: " + singleton.getToken());
```

**Soluções:**
1. Verificar se o login foi bem-sucedido
2. Fazer novo login se token expirou
3. Verificar se header Authorization está correto:
   ```java
   // O SingletonVeiGest já faz isso automaticamente
   headers.put("Authorization", "Bearer " + getToken());
   ```

---

### Listener não é chamado

**Causa:** Listener não registado ou removido prematuramente.

**Diagnóstico:**
```java
// Verificar se listener está registado
Log.d(TAG, "Listener registado: " + (veiculosListener != null));
```

**Soluções:**

1. Registar no momento certo:
   ```java
   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       // Registar AQUI, antes de chamar API
       singleton.setVeiculosListener(this);
   }
   ```

2. Não remover antes da resposta:
   ```java
   @Override
   public void onDestroyView() {
       super.onDestroyView();
       // Remover apenas no destroy
       singleton.setVeiculosListener(null);
   }
   ```

---

### UI não atualiza após resposta da API

**Causa:** Tentando atualizar UI fora da thread principal.

**Solução:**
```java
@Override
public void onRefreshListaVeiculos(ArrayList<Vehicle> lista) {
    // VERIFICAR se activity/fragment ainda existe
    if (getActivity() == null) return;
    
    // USAR runOnUiThread
    getActivity().runOnUiThread(() -> {
        adapter.setData(lista);
        adapter.notifyDataSetChanged();
    });
}
```

---

### Memory Leak com Listeners

**Causa:** Listener mantém referência para Activity/Fragment destruído.

**Diagnóstico:** Usar LeakCanary ou Android Profiler.

**Solução:**
```java
@Override
public void onDestroyView() {
    super.onDestroyView();
    // SEMPRE remover listeners
    singleton.setLoginListener(null);
    singleton.setVeiculosListener(null);
    singleton.setRotasListener(null);
    // ... outros listeners
}
```

---

### Dados não persistem após fechar app

**Causa:** Usando apenas dados em memória.

**Soluções:**

1. Token já persiste automaticamente via SharedPreferences

2. Para outros dados, usar base de dados local:
   ```java
   // Salvar após receber da API
   @Override
   public void onRefreshListaVeiculos(ArrayList<Vehicle> lista) {
       singleton.getBD().removerAllVehiclesBD();
       for (Vehicle v : lista) {
           singleton.getBD().adicionarVehicleBD(v);
       }
   }
   
   // Carregar ao iniciar (antes de chamar API)
   ArrayList<Vehicle> cached = singleton.getBD().getAllVehiclesBD();
   if (!cached.isEmpty()) {
       adapter.setData(cached);
   }
   ```

---

### JSONException durante parsing

**Causa:** JSON da API diferente do esperado.

**Diagnóstico:**
```java
// Logar resposta raw
Log.d(TAG, "Response: " + response.toString());
```

**Soluções:**

1. Verificar estrutura do JSON esperada
2. Usar `optString()`, `optInt()` ao invés de `getString()`, `getInt()`:
   ```java
   // Seguro - retorna valor default se não existir
   String nome = obj.optString("nome", "");
   int id = obj.optInt("id", 0);
   
   // NÃO usar - lança exceção se não existir
   // String nome = obj.getString("nome");
   ```

---

### Glide não carrega imagens

**Causa:** Permissões ou URL inválida.

**Soluções:**

1. Verificar permissão INTERNET
2. Verificar URL da imagem:
   ```java
   Log.d(TAG, "Image URL: " + vehicle.getPhoto());
   ```

3. Usar placeholder e error:
   ```java
   Glide.with(context)
       .load(url)
       .placeholder(R.drawable.loading)
       .error(R.drawable.error)
       .into(imageView);
   ```

---

## Problemas com Emulador

### VolleyError: "Connection refused"

**Causa:** Emulador não consegue acessar localhost.

**Soluções:**

1. Usar IP especial do emulador:
   ```java
   // Para emulador Android padrão
   String BASE_URL = "http://10.0.2.2:8080/api/v1";
   
   // Para Genymotion
   String BASE_URL = "http://10.0.3.2:8080/api/v1";
   ```

2. Usar IP da máquina na rede local:
   ```java
   String BASE_URL = "http://192.168.1.100:8080/api/v1";
   ```

---

### SSL/TLS Handshake Error

**Causa:** Certificado inválido ou auto-assinado.

**Solução para desenvolvimento:**
```xml
<!-- res/xml/network_security_config.xml -->
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">10.0.2.2</domain>
        <domain includeSubdomains="true">192.168.1.100</domain>
    </domain-config>
</network-security-config>
```

```xml
<!-- AndroidManifest.xml -->
<application
    android:networkSecurityConfig="@xml/network_security_config"
    ...>
```

⚠️ **Não usar em produção!**

---

## Debug e Logs

### Ativar logs detalhados do SDK

```java
// No SingletonVeiGest, os logs usam tag "SingletonVeiGest"
// Filtrar no Logcat:
// Tag: SingletonVeiGest
```

### Verificar requisições HTTP

```java
// Adicionar interceptor de log (se necessário)
// O Volley não tem logging nativo como OkHttp
// Usar Android Profiler > Network
```

### Inspecionar resposta da API

```java
// No listener, logar a resposta completa
@Override
public void onRefreshListaVeiculos(ArrayList<Vehicle> lista) {
    Log.d(TAG, "Recebidos " + lista.size() + " veículos");
    for (Vehicle v : lista) {
        Log.d(TAG, "Veículo: " + v.getId() + " - " + v.getLicensePlate());
    }
}
```

---

## Checklist de Verificação

Antes de reportar um problema, verificar:

- [ ] AndroidManifest.xml tem `android:name=".VeiGestApplication"`
- [ ] Permissão INTERNET declarada
- [ ] URL base configurada corretamente (com `/api/v1`)
- [ ] Para HTTP, `android:usesCleartextTraffic="true"`
- [ ] Listener registado antes de chamar API
- [ ] Listener removido no onDestroy/onDestroyView
- [ ] runOnUiThread usado para atualizar UI
- [ ] Verificar logs no Logcat com tag correta

---

## Contato para Suporte

Se o problema persistir após verificar este guia:

1. Coletar logs relevantes do Logcat
2. Descrever passos para reproduzir
3. Informar versão do SDK e Android
4. Abrir issue no repositório
