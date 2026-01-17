# Guia de Estudo - Desenvolvimento Android

## Índice
1. [Computação Móvel e Evolução](#1-computação-móvel-e-evolução)
2. [Estrutura de um Projeto Android](#2-estrutura-de-um-projeto-android)
3. [Layouts e Interface Gráfica](#3-layouts-e-interface-gráfica)
4. [Activities e Intents](#4-activities-e-intents)
5. [Fragmentos e Navigation](#5-fragmentos-e-navigation)
6. [ListView, GridView e RecyclerView](#6-listview-gridview-e-recyclerview)
7. [Menus e Widgets](#7-menus-e-widgets)
8. [Estilos e Temas](#8-estilos-e-temas)
9. [Persistência de Dados](#9-persistência-de-dados)
10. [SQLite e Singleton](#10-sqlite-e-singleton)
11. [API REST](#11-api-rest)
12. [Serviços e Tarefas Assíncronas](#12-serviços-e-tarefas-assíncronas)
13. [Material Design](#13-material-design)

---

## 1. Computação Móvel e Evolução

### Plataformas Principais
- **Android** (Google) - Maior quota de mercado
- **iOS** (Apple) - Segundo maior

### Evolução Tecnológica
- Telefones militares → Telemóveis básicos → Smartphones
- PDAs → Blackberry → iPhone (2007) → Android
- Futuro: AR, VR, ecrãs flexíveis, hologramas, IoT

### Conceitos Importantes
- **SDK Mínimo**: API 24 (Android 7.0 Nougat) é comum
- **Versões Android**: Cada versão tem número de API específico
- **Diversidade**: Múltiplos fabricantes, tamanhos, resoluções

---

## 2. Estrutura de um Projeto Android

### Componentes Principais

#### AndroidManifest.xml
- Arquivo obrigatório que descreve a aplicação
- Define componentes (Activities, Services)
- Declara permissões necessárias
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

#### Diretoria Java
- Contém código Java/Kotlin da aplicação
- Activities, Fragments, Classes de modelo
- Packages de teste (test, androidTest)

#### Diretoria Res
- **drawable**: Imagens da aplicação
- **layout**: Arquivos XML de interface (UI)
- **mipmap**: Ícones da aplicação
- **values**: Strings, cores, estilos
- **menu**: Definições de menus

#### Gradle
- **build.gradle.kts**: Configuração e dependências
- **libs.versions.toml**: Versões centralizadas de bibliotecas

---

## 3. Layouts e Interface Gráfica

### Tipos de Layouts

#### LinearLayout
- Organiza componentes em linha (horizontal/vertical)
- Atributo principal: `android:orientation`
```xml
<LinearLayout
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
</LinearLayout>
```

#### RelativeLayout
- Posiciona componentes relativamente a outros
- Propriedades: `layout_below`, `layout_toEndOf`, `layout_alignEnd`

#### ConstraintLayout
- Layout mais moderno e flexível
- Usa constraints para posicionamento
- Melhor performance para layouts complexos

### Componentes UI Essenciais
- **TextView**: Exibir texto
- **EditText**: Entrada de texto
- **Button**: Botão clicável
- **ImageView**: Exibir imagens
- **CheckBox**, **RadioButton**: Seleções
- **ListView**, **GridView**, **RecyclerView**: Listas e grelhas

### Propriedades Importantes
```xml
android:id="@+id/nomeId"
android:layout_width="match_parent|wrap_content|dpValue"
android:layout_height="match_parent|wrap_content|dpValue"
android:text="@string/nome_string"
android:textSize="18sp"
android:padding="16dp"
android:margin="8dp"
```

---

## 4. Activities e Intents

### Activity
Componente que fornece um ecrã com o qual os utilizadores interagem.

#### Ciclo de Vida
```
onCreate() → onStart() → onResume() → [Running] → onPause() → onStop() → onDestroy()
```

**Métodos Principais:**
- **onCreate()**: Criação da activity, inicialização
- **onStart()**: Activity visível
- **onResume()**: Utilizador pode interagir
- **onPause()**: Outra activity em primeiro plano
- **onStop()**: Activity não visível
- **onDestroy()**: Activity destruída

#### Exemplo Básico
```java
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button btn = findViewById(R.id.btnLogin);
        btn.setOnClickListener(v -> {
            // Ação do botão
        });
    }
}
```

### Intents

#### Intent Explícito (Entre activities da mesma app)
```java
Intent intent = new Intent(this, OutraActivity.class);
intent.putExtra("chave", "valor");
startActivity(intent);

// Receber dados
String valor = getIntent().getStringExtra("chave");
```

#### Intent Implícito (Outras aplicações)
```java
// Email
Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
emailIntent.setData(Uri.parse("mailto:exemplo@email.com"));
emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Assunto");
startActivity(Intent.createChooser(emailIntent, "Enviar email"));

// Telefone
Intent callIntent = new Intent(Intent.ACTION_DIAL);
callIntent.setData(Uri.parse("tel:123456789"));
startActivity(callIntent);
```

---

## 5. Fragmentos e Navigation

### Fragmentos
Porção reutilizável de UI que pode ser incorporada em Activities.

#### Vantagens
- Reutilização de código
- Modularidade
- Adaptação a diferentes tamanhos de ecrã

#### Ciclo de Vida
```
onAttach() → onCreate() → onCreateView() → onViewCreated() → 
onStart() → onResume() → [Running] → 
onPause() → onStop() → onDestroyView() → onDestroy() → onDetach()
```

#### Criar Fragment
```java
public class MeuFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meu, container, false);
        return view;
    }
    
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Inicializar componentes
        TextView tv = view.findViewById(R.id.textView);
    }
}
```

#### Substituir Fragment
```java
getSupportFragmentManager()
    .beginTransaction()
    .replace(R.id.fragmentContainer, new MeuFragment())
    .commit();
```

### Navigation Drawer
Menu lateral deslizante para navegação.

**Componentes:**
- DrawerLayout (container principal)
- NavigationView (menu lateral)
- Toolbar/AppBar
- Fragment Container

```xml
<androidx.drawerlayout.widget.DrawerLayout
    android:id="@+id/drawer_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    
    <!-- Conteúdo principal -->
    <FrameLayout
        android:id="@+id/fragmentContainer"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
    
    <!-- Menu lateral -->
    <com.google.android.material.navigation.NavigationView
        android:id="@+id/nav_view"
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        android:layout_gravity="start"
        app:menu="@menu/menu_main"/>
        
</androidx.drawerlayout.widget.DrawerLayout>
```

---

## 6. ListView, GridView e RecyclerView

### Arquitetura MVC (Model-View-Controller)
- **Model**: Dados e lógica de negócio
- **View**: Interface do utilizador (layouts)
- **Controller**: Mediação entre Model e View (Activities/Fragments)

### ListView
Apresenta itens em lista vertical com scroll.

#### Adapter Personalizado
```java
public class LivroAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Livro> livros;
    private LayoutInflater inflater;
    
    public LivroAdapter(Context context, ArrayList<Livro> livros) {
        this.context = context;
        this.livros = livros;
        this.inflater = LayoutInflater.from(context);
    }
    
    @Override
    public int getCount() {
        return livros.size();
    }
    
    @Override
    public Object getItem(int position) {
        return livros.get(position);
    }
    
    @Override
    public long getItemId(int position) {
        return livros.get(position).getId();
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_livro, parent, false);
        }
        
        TextView tvTitulo = convertView.findViewById(R.id.tvTitulo);
        ImageView ivCapa = convertView.findViewById(R.id.ivCapa);
        
        Livro livro = livros.get(position);
        tvTitulo.setText(livro.getTitulo());
        ivCapa.setImageResource(livro.getCapa());
        
        return convertView;
    }
}
```

#### Uso no Fragment
```java
ListView listView = view.findViewById(R.id.lvLivros);
LivroAdapter adapter = new LivroAdapter(getContext(), listaLivros);
listView.setAdapter(adapter);

listView.setOnItemClickListener((parent, view, position, id) -> {
    Livro livro = (Livro) parent.getItemAtPosition(position);
    // Ação ao clicar no item
});
```

### GridView
Apresenta itens em grelha (múltiplas colunas).

```xml
<GridView
    android:id="@+id/gvLivros"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:numColumns="2"
    android:horizontalSpacing="8dp"
    android:verticalSpacing="8dp"/>
```

### RecyclerView
Versão mais eficiente e flexível (recomendado).

**Vantagens:**
- Melhor performance (reutilização de views)
- Animações incorporadas
- Diferentes layouts (linear, grid, staggered)

---

## 7. Menus e Widgets

### Tipos de Menus

#### Options Menu
Menu na Action Bar (canto superior direito).

**Criar menu XML:**
```xml
<!-- res/menu/menu_pesquisa.xml -->
<menu xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:app="http://schemas.android.com/apk/res-auto">
    <item
        android:id="@+id/action_search"
        android:icon="@drawable/ic_search"
        android:title="@string/pesquisar"
        app:showAsAction="ifRoom"/>
</menu>
```

**Implementar no Activity/Fragment:**
```java
@Override
public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_pesquisa, menu);
    return true;
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.action_search) {
        // Ação de pesquisa
        return true;
    }
    return super.onOptionsItemSelected(item);
}
```

#### Context Menu
Menu que aparece ao fazer long click num componente.

#### Popup Menu
Menu modal ancorado a uma view específica.

### FloatingActionButton (FAB)
Botão flutuante para ação principal.

```xml
<com.google.android.material.floatingactionbutton.FloatingActionButton
    android:id="@+id/fab"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_gravity="bottom|end"
    android:layout_margin="16dp"
    android:src="@drawable/ic_add"
    app:tint="@color/white"/>
```

```java
FloatingActionButton fab = view.findViewById(R.id.fab);
fab.setOnClickListener(v -> {
    // Ação do FAB
});
```

### SearchView
Widget para pesquisa.

```xml
<item
    android:id="@+id/action_search"
    android:title="Pesquisar"
    app:showAsAction="always"
    app:actionViewClass="androidx.appcompat.widget.SearchView"/>
```

---

## 8. Estilos e Temas

### Estilos
Conjunto de propriedades aplicadas a Views individuais.

```xml
<!-- res/values/styles.xml -->
<resources>
    <style name="TituloGrande">
        <item name="android:textSize">24sp</item>
        <item name="android:textColor">@color/black</item>
        <item name="android:textStyle">bold</item>
        <item name="android:padding">16dp</item>
    </style>
</resources>
```

**Aplicar:**
```xml
<TextView
    style="@style/TituloGrande"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Título"/>
```

### Temas
Estilo aplicado a toda a Activity ou aplicação.

```xml
<!-- res/values/themes.xml -->
<resources>
    <style name="Theme.MinhaApp" parent="Theme.MaterialComponents.DayNight.DarkActionBar">
        <item name="colorPrimary">@color/purple_500</item>
        <item name="colorPrimaryVariant">@color/purple_700</item>
        <item name="colorOnPrimary">@color/white</item>
        <item name="colorSecondary">@color/teal_200</item>
    </style>
</resources>
```

**Aplicar no Manifest:**
```xml
<application
    android:theme="@style/Theme.MinhaApp">
    
    <activity
        android:name=".MainActivity"
        android:theme="@style/Theme.MinhaApp"/>
</application>
```

---

## 9. Persistência de Dados

### SharedPreferences
Armazenar dados primitivos em pares chave-valor.

**Ideal para:** Configurações, preferências do utilizador, dados simples.

```java
// Guardar
SharedPreferences prefs = getSharedPreferences("MinhasPrefs", MODE_PRIVATE);
SharedPreferences.Editor editor = prefs.edit();
editor.putString("email", "user@example.com");
editor.putInt("idade", 25);
editor.putBoolean("logged", true);
editor.apply(); // ou commit()

// Ler
String email = prefs.getString("email", "");
int idade = prefs.getInt("idade", 0);
boolean logged = prefs.getBoolean("logged", false);

// Remover
editor.remove("email");
editor.apply();
```

### Armazenamento Interno
Guardar ficheiros na memória interna (privados).

#### Serialização
Transformar objetos em bytes para armazenamento.

```java
// Classe deve implementar Serializable
public class Livro implements Serializable {
    private int id;
    private String titulo;
    // ...
}

// Guardar
try {
    FileOutputStream fos = openFileOutput("livros.dat", MODE_PRIVATE);
    ObjectOutputStream oos = new ObjectOutputStream(fos);
    oos.writeObject(listaLivros);
    oos.close();
} catch (IOException e) {
    e.printStackTrace();
}

// Ler
try {
    FileInputStream fis = openFileInput("livros.dat");
    ObjectInputStream ois = new ObjectInputStream(fis);
    listaLivros = (ArrayList<Livro>) ois.readObject();
    ois.close();
} catch (IOException | ClassNotFoundException e) {
    e.printStackTrace();
}
```

---

## 10. SQLite e Singleton

### Padrão Singleton
Garante que existe apenas uma instância de uma classe.

```java
public class SingletonGestorLivros {
    private static SingletonGestorLivros instance = null;
    private ArrayList<Livro> livros;
    private LivroBDHelper livroBD;
    
    // Construtor privado
    private SingletonGestorLivros(Context context) {
        livros = new ArrayList<>();
        livroBD = new LivroBDHelper(context);
    }
    
    // Método público para obter instância
    public static synchronized SingletonGestorLivros getInstance(Context context) {
        if (instance == null) {
            instance = new SingletonGestorLivros(context);
        }
        return instance;
    }
    
    public ArrayList<Livro> getLivros() {
        return livros;
    }
    
    public void adicionarLivro(Livro livro) {
        livroBD.adicionarLivroBD(livro);
        livros.add(livro);
    }
}
```

### SQLite Database

#### Helper Class
```java
public class LivroBDHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "livros.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "livros";
    
    private SQLiteDatabase db;
    
    public LivroBDHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        db = this.getWritableDatabase();
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
            "id INTEGER PRIMARY KEY, " +
            "titulo TEXT NOT NULL, " +
            "autor TEXT, " +
            "ano INTEGER, " +
            "capa TEXT)";
        db.execSQL(createTable);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
```

#### Operações CRUD

**Create (Adicionar):**
```java
public boolean adicionarLivroBD(Livro livro) {
    ContentValues values = new ContentValues();
    values.put("id", livro.getId());
    values.put("titulo", livro.getTitulo());
    values.put("autor", livro.getAutor());
    values.put("ano", livro.getAno());
    values.put("capa", livro.getCapa());
    
    long result = db.insert(TABLE_NAME, null, values);
    return result != -1;
}
```

**Read (Listar):**
```java
public ArrayList<Livro> getAllLivrosBD() {
    ArrayList<Livro> livros = new ArrayList<>();
    Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
    
    if (cursor.moveToFirst()) {
        do {
            int id = cursor.getInt(0);
            String titulo = cursor.getString(1);
            String autor = cursor.getString(2);
            int ano = cursor.getInt(3);
            String capa = cursor.getString(4);
            
            livros.add(new Livro(id, capa, ano, titulo, autor));
        } while (cursor.moveToNext());
    }
    cursor.close();
    return livros;
}
```

**Update (Atualizar):**
```java
public boolean editarLivroBD(Livro livro) {
    ContentValues values = new ContentValues();
    values.put("titulo", livro.getTitulo());
    values.put("autor", livro.getAutor());
    values.put("ano", livro.getAno());
    
    int result = db.update(TABLE_NAME, values, "id = ?", 
                          new String[]{String.valueOf(livro.getId())});
    return result > 0;
}
```

**Delete (Remover):**
```java
public boolean removerLivroBD(int id) {
    int result = db.delete(TABLE_NAME, "id = ?", 
                          new String[]{String.valueOf(id)});
    return result > 0;
}
```

---

## 11. API REST

### Conceitos Fundamentais

**API**: Interface de Programação de Aplicações - conjunto de rotinas para comunicação entre aplicações.

**REST**: Representational State Transfer - arquitetura para APIs web.

**RESTful**: API que segue os princípios REST.

### Métodos HTTP
- **GET**: Obter dados
- **POST**: Criar novo recurso
- **PUT**: Atualizar recurso completo
- **PATCH**: Atualizar parcialmente
- **DELETE**: Remover recurso

### Formato de Dados
**JSON** (JavaScript Object Notation):
```json
{
  "id": 1,
  "titulo": "Programar em Android",
  "autor": "AMSI Team",
  "ano": 2024,
  "capa": "http://exemplo.com/capa.jpg"
}
```

### Biblioteca Volley

#### Adicionar Dependência
```kotlin
// build.gradle.kts (Module: app)
dependencies {
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.github.bumptech.glide:glide:4.16.0") // Para imagens
}
```

#### Permissões no Manifest
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

#### GET Request
```java
public class BooksAPI {
    private static final String BASE_URL = "http://172.22.21.41/books/api/";
    
    public static void getAllLivros(Context context, Response.Listener<String> listener) {
        String url = BASE_URL + "livros";
        
        JsonArrayRequest request = new JsonArrayRequest(
            Request.Method.GET, url, null,
            response -> {
                // Processar resposta JSON
                ArrayList<Livro> livros = parseJsonLivros(response);
                listener.onResponse(response.toString());
            },
            error -> {
                Log.e("API", "Erro: " + error.getMessage());
            }
        );
        
        Volley.newRequestQueue(context).add(request);
    }
    
    private static ArrayList<Livro> parseJsonLivros(JSONArray jsonArray) {
        ArrayList<Livro> livros = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                int id = obj.getInt("id");
                String titulo = obj.getString("titulo");
                String autor = obj.getString("autor");
                int ano = obj.getInt("ano");
                String capa = obj.getString("capa");
                
                livros.add(new Livro(id, capa, ano, titulo, autor));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return livros;
    }
}
```

#### POST Request
```java
public static void adicionarLivro(Context context, Livro livro, 
                                   Response.Listener<String> listener) {
    String url = BASE_URL + "livros";
    
    JSONObject jsonBody = new JSONObject();
    try {
        jsonBody.put("titulo", livro.getTitulo());
        jsonBody.put("autor", livro.getAutor());
        jsonBody.put("ano", livro.getAno());
        jsonBody.put("capa", livro.getCapa());
    } catch (JSONException e) {
        e.printStackTrace();
    }
    
    JsonObjectRequest request = new JsonObjectRequest(
        Request.Method.POST, url, jsonBody,
        response -> listener.onResponse(response.toString()),
        error -> Log.e("API", "Erro: " + error.getMessage())
    );
    
    Volley.newRequestQueue(context).add(request);
}
```

#### Carregar Imagem com Glide
```java
Glide.with(context)
    .load(urlImagem)
    .placeholder(R.drawable.placeholder)
    .error(R.drawable.error_image)
    .into(imageView);
```

---

## 12. Serviços e Tarefas Assíncronas

### Main Thread (UI Thread)
- Thread principal da aplicação
- Gere eventos e desenha a UI
- **NUNCA** bloquear esta thread
- Operações longas causam ANR (Application Not Responding)

### Tarefas Assíncronas

#### Executors (Recomendado)
```java
ExecutorService executor = Executors.newSingleThreadExecutor();
Handler handler = new Handler(Looper.getMainLooper());

executor.execute(() -> {
    // Operação em background (rede, BD, etc.)
    String resultado = operacaoDemorada();
    
    handler.post(() -> {
        // Atualizar UI na main thread
        textView.setText(resultado);
    });
});
```

#### AsyncTask (Deprecated mas útil para entender)
```java
private class MinhaTask extends AsyncTask<Void, Void, String> {
    @Override
    protected void onPreExecute() {
        // Executado na UI thread antes da tarefa
        progressBar.setVisibility(View.VISIBLE);
    }
    
    @Override
    protected String doInBackground(Void... voids) {
        // Executado em background thread
        return operacaoDemorada();
    }
    
    @Override
    protected void onPostExecute(String resultado) {
        // Executado na UI thread após a tarefa
        progressBar.setVisibility(View.GONE);
        textView.setText(resultado);
    }
}

// Executar
new MinhaTask().execute();
```

### Boas Práticas
1. Operações de rede sempre em background
2. Acesso a BD em background
3. Atualizar UI apenas na main thread
4. Usar handlers para comunicar com main thread
5. Cancelar tarefas quando Activity/Fragment é destruído

---

## 13. Material Design

### Princípios Base

1. **Material é a Metáfora**
   - Baseado no mundo físico (papel, tinta)
   - Superfícies e sombras criam profundidade
   - Animações seguem leis da física

2. **Ousado, Gráfico, Intencional**
   - Tipografia clara
   - Uso intencional de cor
   - Espaços brancos estratégicos
   - Hierarquia visual clara

3. **Movimento Tem Significado**
   - Animações guiam o utilizador
   - Transições suaves
   - Feedback visual imediato

### Componentes Material

#### Material Colors
```xml
<resources>
    <color name="purple_500">#FF6200EE</color>
    <color name="purple_700">#FF3700B3</color>
    <color name="teal_200">#FF03DAC5</color>
    <color name="black">#FF000000</color>
    <color name="white">#FFFFFFFF</color>
</resources>
```

#### Material Theme
```xml
<style name="Theme.App" parent="Theme.MaterialComponents.DayNight">
    <item name="colorPrimary">@color/purple_500</item>
    <item name="colorPrimaryVariant">@color/purple_700</item>
    <item name="colorOnPrimary">@color/white</item>
    <item name="colorSecondary">@color/teal_200</item>
</style>
```

#### CardView
```xml
<androidx.cardview.widget.CardView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:cardCornerRadius="8dp"
    app:cardElevation="4dp"
    app:cardUseCompatPadding="true">
    
    <!-- Conteúdo do card -->
    
</androidx.cardview.widget.CardView>
```

#### TextInputLayout
```xml
<com.google.android.material.textfield.TextInputLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:hint="Email">
    
    <com.google.android.material.textfield.TextInputEditText
        android:id="@+id/etEmail"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:inputType="textEmailAddress"/>
        
</com.google.android.material.textfield.TextInputLayout>
```

#### Snackbar
```java
Snackbar.make(view, "Mensagem de feedback", Snackbar.LENGTH_LONG)
    .setAction("Desfazer", v -> {
        // Ação
    })
    .show();
```

### Elevação e Sombras
```xml
android:elevation="4dp"
app:cardElevation="8dp"
```

### Ripple Effect
Efeito de toque automático em componentes Material.

```xml
android:background="?attr/selectableItemBackground"
android:foreground="?attr/selectableItemBackground"
```

---

## Resumo de Boas Práticas

### Estrutura e Organização
- ✅ Usar packages separados (modelo, adaptadores, fragmentos)
- ✅ Extrair strings para strings.xml
- ✅ Usar Singleton para gestores de dados
- ✅ Separar lógica de negócio da UI

### Performance
- ✅ Operações pesadas em background threads
- ✅ Usar RecyclerView em vez de ListView
- ✅ Reutilizar views em adapters
- ✅ Carregar imagens de forma eficiente (Glide, Picasso)

### UI/UX
- ✅ Seguir Material Design
- ✅ Fornecer feedback visual (ProgressBar, Snackbar)
- ✅ Usar ConstraintLayout para layouts complexos
- ✅ Testar em diferentes tamanhos de ecrã

### Dados
- ✅ Validar inputs do utilizador
- ✅ Tratar erros de rede e BD
- ✅ Usar try-catch em operações críticas
- ✅ Persistir dados importantes

### Segurança
- ✅ Declarar permissões necessárias
- ✅ Validar dados da API
- ✅ Não armazenar passwords em plain text
- ✅ Usar HTTPS para comunicação

---

## Checklist de Projeto

### Configuração Inicial
- [ ] SDK mínimo definido (API 24+)
- [ ] Dependências adicionadas (Volley, Glide, Material)
- [ ] Permissões declaradas no Manifest
- [ ] Tema Material aplicado

### Estrutura
- [ ] Packages organizados
- [ ] Singleton para gestor de dados
- [ ] Helper para BD (se aplicável)
- [ ] Classes de modelo criadas

### UI
- [ ] Layouts responsivos
- [ ] Strings extraídas
- [ ] Componentes Material usados
- [ ] Navigation Drawer implementado (se aplicável)

### Funcionalidades
- [ ] Activities com ciclo de vida correto
- [ ] Fragments implementados
- [ ] Adapters para listas
- [ ] Menus funcionais
- [ ] Persistência de dados (SQLite/SharedPreferences)
- [ ] Integração com API (se aplicável)

### Testes
- [ ] Testar em diferentes dispositivos
- [ ] Validar rotação de ecrã
- [ ] Testar sem conexão de rede
- [ ] Verificar persistência de dados

---

## Recursos Úteis

### Documentação Oficial
- [Android Developers](https://developer.android.com/)
- [Material Design](https://material.io/)
- [API Guides](https://developer.android.com/guide)

### Ferramentas
- Android Studio
- Emulador Android
- ADB (Android Debug Bridge)
- Layout Inspector

### Bibliotecas Comuns
- **Volley**: Requisições HTTP
- **Glide/Picasso**: Carregamento de imagens
- **Gson**: Parsing JSON
- **Room**: ORM para SQLite (alternativa moderna)
- **Retrofit**: Cliente HTTP (alternativa ao Volley)

---

**Boa sorte nos seus estudos!** 🚀

Este guia cobre os conceitos essenciais do desenvolvimento Android. Pratique criando projetos e explore a documentação oficial para aprofundar cada tópico.
