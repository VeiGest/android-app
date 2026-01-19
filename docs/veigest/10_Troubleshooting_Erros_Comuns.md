# VeiGest - Troubleshooting e Erros Comuns
## Guia de Resolução de Problemas

**ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO - 2025 / 2026**  
**TESP PROGRAMAÇÃO DE SISTEMAS DE INFORMAÇÃO**

---

## 📖 Índice

1. [Erros de Compilação](#erros-de-compilação)
2. [Erros de Runtime](#erros-de-runtime)
3. [Erros de Rede/API](#erros-de-redeapi)
4. [Erros de Fragments](#erros-de-fragments)
5. [Erros de Layouts](#erros-de-layouts)
6. [Erros de SQLite](#erros-de-sqlite)
7. [Erros do SDK](#erros-do-sdk)
8. [Debugging](#debugging)

---

## Erros de Compilação

### ❌ Cannot find symbol

**Erro:**
```
error: cannot find symbol
   symbol: class SingletonVeiGest
```

**Causa:** Classe não importada ou não existe.

**Solução:**
```java
// Adicionar import correto
import com.veigest.sdk.SingletonVeiGest;
```

---

### ❌ Incompatible types

**Erro:**
```
error: incompatible types: String cannot be converted to int
```

**Causa:** Tipo de dado errado.

**Solução:**
```java
// Errado
int id = jsonObject.getString("id");  // ❌

// Correto
int id = jsonObject.getInt("id");     // ✅
// Ou
int id = Integer.parseInt(jsonObject.getString("id"));  // ✅
```

---

### ❌ Cannot resolve symbol 'R'

**Erro:**
```
error: cannot find symbol
   symbol: variable R
```

**Causas e Soluções:**

| Causa | Solução |
|-------|---------|
| Erro no XML | Corrigir erros em res/layout/*.xml |
| Import errado | Usar `import pt.ipleiria.estg.dei.veigest.R;` |
| Projeto não compilado | Build > Clean Project, Build > Rebuild |
| Sync necessário | File > Sync Project with Gradle Files |

---

### ❌ Duplicate class found

**Erro:**
```
Duplicate class com.google.gson.Gson found in modules
```

**Solução em build.gradle:**
```kotlin
dependencies {
    implementation("com.google.code.gson:gson:2.10.1") {
        exclude(group = "com.google.code.gson", module = "gson")
    }
}
```

---

### ❌ SDK version mismatch

**Erro:**
```
The SDK location setting is incorrect
```

**Solução:**
1. File > Project Structure > SDK Location
2. Verificar caminho do Android SDK
3. Ou criar `local.properties`:
```properties
sdk.dir=C\:\\Users\\SEU_USUARIO\\AppData\\Local\\Android\\Sdk
```

---

## Erros de Runtime

### ❌ NullPointerException

**Erro:**
```
java.lang.NullPointerException: Attempt to invoke virtual method on a null object reference
```

**Causas Comuns:**

```java
// 1. View não encontrada
TextView tv = view.findViewById(R.id.tvNome);
tv.setText("..."); // ❌ tv é null se id errado

// Solução: Verificar null
if (tv != null) {
    tv.setText("...");
}

// 2. Contexto null
SingletonVeiGest.getInstance(getContext()); // ❌ getContext() pode ser null

// Solução: Verificar ou usar requireContext()
if (getContext() != null) {
    SingletonVeiGest.getInstance(getContext());
}

// 3. Activity null em Fragment
getActivity().runOnUiThread(...); // ❌

// Solução:
if (getActivity() != null && isAdded()) {
    requireActivity().runOnUiThread(...);
}
```

---

### ❌ ClassCastException

**Erro:**
```
java.lang.ClassCastException: cannot be cast to
```

**Solução:**
```java
// Errado
MainActivity activity = (MainActivity) getActivity();  // ❌ pode crashar

// Correto
if (getActivity() instanceof MainActivity) {
    MainActivity activity = (MainActivity) getActivity();  // ✅
}
```

---

### ❌ IllegalStateException: Fragment not attached

**Erro:**
```
java.lang.IllegalStateException: Fragment not attached to a context
```

**Causa:** Callback executa após Fragment ser destruído.

**Solução:**
```java
@Override
public void onRefreshVeiculos(ArrayList<Vehicle> vehicles) {
    // Verificar se Fragment ainda está attached
    if (!isAdded() || getActivity() == null) {
        return;  // ✅ Sair se não estiver attached
    }
    
    requireActivity().runOnUiThread(() -> {
        // atualizar UI
    });
}
```

---

### ❌ NetworkOnMainThreadException

**Erro:**
```
android.os.NetworkOnMainThreadException
```

**Causa:** Requisição de rede na Main Thread.

**Solução:** Usar Volley (já é assíncrono) ou:
```java
// Com Thread
new Thread(() -> {
    // código de rede aqui
    
    runOnUiThread(() -> {
        // atualizar UI
    });
}).start();

// Com AsyncTask (deprecated) - usar Executors
Executors.newSingleThreadExecutor().execute(() -> {
    // código de rede
    
    new Handler(Looper.getMainLooper()).post(() -> {
        // atualizar UI
    });
});
```

---

## Erros de Rede/API

### ❌ Timeout

**Erro:**
```
com.android.volley.TimeoutError
```

**Soluções:**

```java
// 1. Aumentar timeout
JsonObjectRequest request = new JsonObjectRequest(...);
request.setRetryPolicy(new DefaultRetryPolicy(
    30000,  // 30 segundos timeout
    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
));

// 2. Verificar URL e servidor
Log.d(TAG, "URL: " + url);  // Verificar se URL está correta
```

---

### ❌ AuthFailureError (401/403)

**Erro:**
```
com.android.volley.AuthFailureError
```

**Causas e Soluções:**

| Causa | Solução |
|-------|---------|
| Token expirado | Fazer logout e login novamente |
| Token não enviado | Verificar header Authorization |
| Token inválido | Verificar formato "Bearer TOKEN" |

```java
// Verificar se token está a ser enviado
@Override
public Map<String, String> getHeaders() throws AuthFailureError {
    Map<String, String> headers = new HashMap<>();
    String token = getToken();
    Log.d(TAG, "Token: " + token);  // Debug
    
    if (token != null && !token.isEmpty()) {
        headers.put("Authorization", "Bearer " + token);
    }
    return headers;
}
```

---

### ❌ ServerError (500)

**Erro:**
```
com.android.volley.ServerError: 500
```

**Solução:**
1. Verificar logs do servidor API
2. Verificar formato do JSON enviado
3. Verificar se todos os campos obrigatórios estão presentes

---

### ❌ NoConnectionError

**Erro:**
```
com.android.volley.NoConnectionError
```

**Soluções:**

1. **Verificar permissão de Internet:**
```xml
<!-- AndroidManifest.xml -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

2. **Verificar cleartext (HTTP) no Android 9+:**
```xml
<!-- AndroidManifest.xml -->
<application
    android:usesCleartextTraffic="true"
    ...>
```

3. **Verificar URL localhost em emulador:**
```java
// localhost não funciona no emulador
// Usar 10.0.2.2 para emulador Android
String baseUrl = "http://10.0.2.2:8080/api/v1";

// Para dispositivo físico na mesma rede, usar IP do servidor
String baseUrl = "http://192.168.1.100:8080/api/v1";
```

---

## Erros de Fragments

### ❌ Fragment already added

**Erro:**
```
java.lang.IllegalStateException: Fragment already added
```

**Solução:**
```java
// Verificar antes de adicionar
Fragment fragment = new DashboardFragment();
if (!fragment.isAdded()) {
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.container, fragment)
        .commit();
}
```

---

### ❌ Can not perform this action after onSaveInstanceState

**Erro:**
```
java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
```

**Solução:**
```java
// Usar commitAllowingStateLoss() em vez de commit()
getSupportFragmentManager()
    .beginTransaction()
    .replace(R.id.container, fragment)
    .commitAllowingStateLoss();  // ✅

// Ou verificar estado
if (!isFinishing() && !isDestroyed()) {
    // fazer transação
}
```

---

### ❌ Fragment não recebe callbacks

**Causa:** Listener não registado ou removido.

**Solução:**
```java
// Registar em onCreate
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    SingletonVeiGest.getInstance(getContext()).setVeiculosListener(this);
}

// NÃO esquecer de verificar implementação da interface
public class MeuFragment extends Fragment implements VeiculosListener {
    // implementar métodos da interface
}
```

---

## Erros de Layouts

### ❌ View não encontrada

**Erro:**
```
java.lang.NullPointerException: findViewById returns null
```

**Soluções:**

```java
// 1. Verificar se está a usar a view correta
// Em Fragment:
View view = inflater.inflate(R.layout.fragment_x, container, false);
TextView tv = view.findViewById(R.id.tvNome);  // ✅ usar view.

// 2. Verificar nome do ID
// No XML:
android:id="@+id/tvNome"
// No Java:
R.id.tvNome  // deve corresponder

// 3. Verificar se layout correto
R.layout.fragment_correto  // verificar nome do ficheiro
```

---

### ❌ Layout não aparece

**Possíveis Causas:**

| Problema | Solução |
|----------|---------|
| visibility="gone" | Mudar para visible |
| width/height = 0 | Usar wrap_content ou match_parent |
| Cor igual ao fundo | Verificar textColor |
| Z-order | Verificar ordem dos elementos |

---

### ❌ RecyclerView vazia

```java
// 1. Verificar se adapter está configurado
recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));  // ✅
recyclerView.setAdapter(adapter);  // ✅

// 2. Verificar se notifyDataSetChanged é chamado
lista.addAll(novosItems);
adapter.notifyDataSetChanged();  // ✅ Não esquecer!

// 3. Verificar se getItemCount retorna valor correto
@Override
public int getItemCount() {
    return lista.size();  // ✅
}
```

---

## Erros de SQLite

### ❌ Table does not exist

**Erro:**
```
android.database.sqlite.SQLiteException: no such table: vehicles
```

**Soluções:**

1. **Incrementar versão da BD:**
```java
private static final int DB_VERSION = 2;  // Incrementar
```

2. **Limpar dados da app:**
   - Configurações > Apps > VeiGest > Limpar dados

3. **Desinstalar e reinstalar app**

---

### ❌ Column does not exist

**Erro:**
```
no such column: nova_coluna
```

**Solução em onUpgrade:**
```java
@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    if (oldVersion < 2) {
        // Adicionar nova coluna
        db.execSQL("ALTER TABLE vehicles ADD COLUMN nova_coluna TEXT");
    }
}
```

---

### ❌ Cursor não fechado (Memory Leak)

```java
// Errado ❌
Cursor cursor = database.rawQuery(...);
if (cursor.moveToFirst()) {
    // processar
}
// cursor nunca é fechado!

// Correto ✅
Cursor cursor = database.rawQuery(...);
try {
    if (cursor.moveToFirst()) {
        // processar
    }
} finally {
    cursor.close();
}
```

---

## Erros do SDK

### ❌ Singleton não inicializado

**Erro:**
```
NullPointerException ao chamar SingletonVeiGest.getInstance()
```

**Solução:**
```java
// Sempre passar contexto válido
SingletonVeiGest singleton = SingletonVeiGest.getInstance(
    getApplicationContext()  // ✅ ou getContext()
);
```

---

### ❌ Listener null

```java
// No Singleton, sempre verificar
if (veiculosListener != null) {
    veiculosListener.onRefreshVeiculos(vehicles);  // ✅
}
```

---

### ❌ Dados não persistidos

**Verificações:**
1. Verificar se `VeiGestBDHelper` é inicializado
2. Verificar se métodos de BD são chamados
3. Verificar logs para exceções silenciosas

```java
// Adicionar logs para debug
public void adicionarVehicleBD(Vehicle v) {
    Log.d(TAG, "Adicionando veículo: " + v.getId());
    try {
        // código...
        Log.d(TAG, "Veículo adicionado com sucesso");
    } catch (Exception e) {
        Log.e(TAG, "Erro ao adicionar: " + e.getMessage());
    }
}
```

---

## Debugging

### Logcat

```java
// Níveis de Log
Log.v(TAG, "Verbose");   // Mais detalhado
Log.d(TAG, "Debug");     // Debug
Log.i(TAG, "Info");      // Informação
Log.w(TAG, "Warning");   // Aviso
Log.e(TAG, "Error");     // Erro

// Usar TAG constante
private static final String TAG = "MeuFragment";

// Logar objetos
Log.d(TAG, "User: " + user.toString());
Log.d(TAG, "Response: " + response.toString());
```

### Breakpoints no Android Studio

1. Clicar na margem esquerda do código
2. Run > Debug (ou Shift+F9)
3. Usar Step Over (F8), Step Into (F7), Step Out (Shift+F8)

### Network Profiler

1. View > Tool Windows > Profiler
2. Selecionar sessão
3. Aba Network para ver requisições

### Layout Inspector

1. Tools > Layout Inspector
2. Selecionar processo
3. Inspecionar hierarquia de views

---

## Tabela de Referência Rápida

| Erro | Causa Comum | Solução Rápida |
|------|-------------|----------------|
| NullPointer | View/objeto null | Verificar null antes de usar |
| Fragment not attached | Callback após destroy | Verificar isAdded() |
| Network error | Sem internet/URL errada | Verificar conexão e URL |
| 401 Unauthorized | Token inválido | Verificar header Auth |
| R not found | Erro em XML | Clean + Rebuild |
| Table not exists | BD desatualizada | Incrementar DB_VERSION |

---

## 📚 Documentação Relacionada

| Documento | Descrição |
|-----------|-----------|
| [02_VeiGest_SDK.md](02_VeiGest_SDK.md) | Erros do SDK |
| [03_Activities_Fragments.md](03_Activities_Fragments.md) | Ciclo de vida |
| [06_SQLite_Persistencia.md](06_SQLite_Persistencia.md) | Erros de BD |
| [07_Integracao_API_REST.md](07_Integracao_API_REST.md) | Erros de API |

---

**Última atualização:** Janeiro 2026
