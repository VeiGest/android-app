# VeiGest - Implementar Novas Funcionalidades
## Guia de Desenvolvimento

**ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO - 2025 / 2026**  
**TESP PROGRAMAÇÃO DE SISTEMAS DE INFORMAÇÃO**

---

## 📖 Índice

1. [Introdução](#introdução)
2. [Criar Novo Fragment](#criar-novo-fragment)
3. [Adicionar ao Navigation Drawer](#adicionar-ao-navigation-drawer)
4. [Criar Layout XML](#criar-layout-xml)
5. [Adicionar Novo Listener](#adicionar-novo-listener)
6. [Implementar CRUD Completo](#implementar-crud-completo)
7. [Adicionar Novo Modelo](#adicionar-novo-modelo)
8. [Criar Adapter para Lista](#criar-adapter-para-lista)
9. [Checklist de Nova Funcionalidade](#checklist-de-nova-funcionalidade)

---

## Introdução

Este guia mostra como adicionar novas funcionalidades ao VeiGest, passo a passo. Cada secção inclui código exemplo que pode ser adaptado.

### Estrutura de Pastas

```
app/src/main/
├── java/pt/ipleiria/estg/dei/veigest/
│   ├── MainActivity.java           ← Activity principal
│   ├── fragments/                   ← Seus Fragments
│   │   ├── LoginFragment.java
│   │   ├── DashboardFragment.java
│   │   └── NovoFragment.java       ← Adicionar aqui
│   └── adapters/                    ← Adapters de listas
│       └── NovoAdapter.java        ← Adicionar aqui
│
├── res/
│   ├── layout/
│   │   ├── fragment_novo.xml       ← Layout do Fragment
│   │   └── item_novo.xml           ← Item da lista
│   ├── menu/
│   │   └── nav_menu.xml            ← Menu do Drawer
│   └── navigation/
│       └── nav_graph.xml           ← Navegação (se usar)
│
veigest-sdk/src/main/
├── java/com/veigest/sdk/
│   ├── SingletonVeiGest.java       ← Métodos de API
│   ├── models/
│   │   └── NovoModelo.java         ← Novo modelo
│   └── listeners/
│       └── NovoListener.java       ← Novo listener
```

---

## Criar Novo Fragment

### Passo 1: Criar a Classe

Criar ficheiro: `app/src/main/java/pt/ipleiria/estg/dei/veigest/fragments/NovoFragment.java`

```java
package pt.ipleiria.estg.dei.veigest.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.veigest.sdk.SingletonVeiGest;
import com.veigest.sdk.listeners.NovoListener;
import com.veigest.sdk.models.NovoModelo;

import pt.ipleiria.estg.dei.veigest.R;
import pt.ipleiria.estg.dei.veigest.adapters.NovoAdapter;

import java.util.ArrayList;

public class NovoFragment extends Fragment implements NovoListener {
    
    // Views
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    
    // Dados
    private ArrayList<NovoModelo> lista = new ArrayList<>();
    private NovoAdapter adapter;
    
    // Singleton
    private SingletonVeiGest singleton;
    
    // ==========================================
    // CONSTRUCTOR
    // ==========================================
    public NovoFragment() {
        // Required empty public constructor
    }
    
    public static NovoFragment newInstance() {
        return new NovoFragment();
    }
    
    // ==========================================
    // LIFECYCLE
    // ==========================================
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Inicializar Singleton e registar listener
        singleton = SingletonVeiGest.getInstance(getContext());
        singleton.setNovoListener(this);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_novo, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Inicializar views
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        
        // Configurar RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NovoAdapter(getContext(), lista);
        recyclerView.setAdapter(adapter);
        
        // Configurar FAB (se existir)
        view.findViewById(R.id.fabAdicionar).setOnClickListener(v -> {
            // Abrir tela de adicionar
            abrirFormulario(null);
        });
    }
    
    @Override
    public void onStart() {
        super.onStart();
        carregarDados();
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // IMPORTANTE: Remover listener
        singleton.setNovoListener(null);
    }
    
    // ==========================================
    // MÉTODOS PRIVADOS
    // ==========================================
    private void carregarDados() {
        progressBar.setVisibility(View.VISIBLE);
        singleton.getAllNovosAPI();
    }
    
    private void abrirFormulario(NovoModelo item) {
        // Navegar para fragment de formulário
        // Passar item como argumento se for edição
    }
    
    // ==========================================
    // LISTENER CALLBACKS
    // ==========================================
    @Override
    public void onRefreshNovos(ArrayList<NovoModelo> items) {
        if (!isAdded()) return;
        
        requireActivity().runOnUiThread(() -> {
            progressBar.setVisibility(View.GONE);
            
            lista.clear();
            lista.addAll(items);
            adapter.notifyDataSetChanged();
        });
    }
    
    @Override
    public void onRefreshNovosError(String error) {
        if (!isAdded()) return;
        
        requireActivity().runOnUiThread(() -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        });
    }
}
```

---

## Adicionar ao Navigation Drawer

### Passo 2: Atualizar Menu

Editar: `app/src/main/res/menu/nav_menu.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    
    <!-- Grupo Principal -->
    <group android:checkableBehavior="single">
        
        <!-- Items existentes -->
        <item
            android:id="@+id/nav_dashboard"
            android:icon="@drawable/ic_dashboard"
            android:title="Dashboard" />
            
        <item
            android:id="@+id/nav_veiculos"
            android:icon="@drawable/ic_directions_car"
            android:title="Veículos" />
        
        <!-- NOVO ITEM -->
        <item
            android:id="@+id/nav_novo"
            android:icon="@drawable/ic_novo"
            android:title="Nova Funcionalidade" />
            
    </group>
    
</menu>
```

### Passo 3: Atualizar MainActivity

Editar: `MainActivity.java`

```java
@Override
public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    Fragment fragment = null;
    int itemId = item.getItemId();
    
    if (itemId == R.id.nav_dashboard) {
        fragment = new DashboardFragment();
    } else if (itemId == R.id.nav_veiculos) {
        fragment = new VeiculosFragment();
    } else if (itemId == R.id.nav_novo) {        // ADICIONAR
        fragment = new NovoFragment();           // ADICIONAR
    } else if (itemId == R.id.nav_logout) {
        performLogout();
        return true;
    }
    
    if (fragment != null) {
        loadFragment(fragment);
    }
    
    drawerLayout.closeDrawer(GravityCompat.START);
    return true;
}
```

---

## Criar Layout XML

### Passo 4: Layout do Fragment

Criar: `app/src/main/res/layout/fragment_novo.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.coordinatorlayout.widget.CoordinatorLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    
    <!-- ProgressBar centralizado -->
    <ProgressBar
        android:id="@+id/progressBar"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:visibility="gone" />
    
    <!-- Lista de items -->
    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:padding="8dp"
        android:clipToPadding="false" />
    
    <!-- FAB para adicionar -->
    <com.google.android.material.floatingactionbutton.FloatingActionButton
        android:id="@+id/fabAdicionar"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom|end"
        android:layout_margin="16dp"
        android:src="@drawable/ic_add"
        app:backgroundTint="@color/colorPrimary" />
        
</androidx.coordinatorlayout.widget.CoordinatorLayout>
```

### Passo 5: Layout do Item da Lista

Criar: `app/src/main/res/layout/item_novo.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<com.google.android.material.card.MaterialCardView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginBottom="8dp"
    app:cardCornerRadius="8dp"
    app:cardElevation="2dp">
    
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="16dp">
        
        <!-- Título -->
        <TextView
            android:id="@+id/tvTitulo"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:textSize="18sp"
            android:textStyle="bold"
            android:textColor="@color/textPrimary" />
        
        <!-- Subtítulo -->
        <TextView
            android:id="@+id/tvSubtitulo"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="4dp"
            android:textSize="14sp"
            android:textColor="@color/textSecondary" />
        
        <!-- Status -->
        <TextView
            android:id="@+id/tvStatus"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="8dp"
            android:paddingHorizontal="8dp"
            android:paddingVertical="4dp"
            android:textSize="12sp"
            android:background="@drawable/bg_status" />
            
    </LinearLayout>
    
</com.google.android.material.card.MaterialCardView>
```

---

## Adicionar Novo Listener

### Passo 6: Criar Interface

Criar: `veigest-sdk/src/main/java/com/veigest/sdk/listeners/NovoListener.java`

```java
package com.veigest.sdk.listeners;

import com.veigest.sdk.models.NovoModelo;
import java.util.ArrayList;

public interface NovoListener {
    
    /**
     * Chamado quando a lista é atualizada com sucesso
     */
    void onRefreshNovos(ArrayList<NovoModelo> items);
    
    /**
     * Chamado quando há erro ao obter dados
     */
    void onRefreshNovosError(String error);
}
```

### Passo 7: Adicionar ao Singleton

Editar: `SingletonVeiGest.java`

```java
// Adicionar variável do listener
private NovoListener novoListener;

// Adicionar setter
public void setNovoListener(NovoListener listener) {
    this.novoListener = listener;
}

// Adicionar URL do endpoint
private String mUrlAPINovos;

// No método atualizarEndpoints()
private void atualizarEndpoints() {
    // ... endpoints existentes ...
    mUrlAPINovos = baseUrl + "/novos";
}

// Adicionar método de API
public void getAllNovosAPI() {
    JsonObjectRequest request = new JsonObjectRequest(
        Request.Method.GET,
        mUrlAPINovos,
        null,
        response -> {
            try {
                ArrayList<NovoModelo> items = VeiGestJsonParser.parserJsonNovos(response);
                
                if (novoListener != null) {
                    novoListener.onRefreshNovos(items);
                }
            } catch (Exception e) {
                if (novoListener != null) {
                    novoListener.onRefreshNovosError("Erro ao processar dados");
                }
            }
        },
        error -> {
            if (novoListener != null) {
                novoListener.onRefreshNovosError("Erro de conexão");
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

## Implementar CRUD Completo

### CREATE (Adicionar)

```java
// No Singleton
public void addNovoAPI(NovoModelo item) {
    JSONObject body = new JSONObject();
    try {
        body.put("titulo", item.getTitulo());
        body.put("descricao", item.getDescricao());
        // ... outros campos
    } catch (JSONException e) {
        e.printStackTrace();
        return;
    }
    
    JsonObjectRequest request = new JsonObjectRequest(
        Request.Method.POST,
        mUrlAPINovos,
        body,
        response -> {
            // Processar resposta
            if (novoOperacaoListener != null) {
                novoOperacaoListener.onOperacaoNovo(OPERACAO_ADICIONAR, item);
            }
        },
        error -> {
            if (novoOperacaoListener != null) {
                novoOperacaoListener.onOperacaoNovoError("Erro ao adicionar");
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

### READ (Obter por ID)

```java
public void getNovoByIdAPI(int id) {
    String url = mUrlAPINovos + "/" + id;
    
    JsonObjectRequest request = new JsonObjectRequest(
        Request.Method.GET,
        url,
        null,
        response -> {
            NovoModelo item = VeiGestJsonParser.parserJsonNovo(response);
            if (novoDetalheListener != null) {
                novoDetalheListener.onNovoDetalhe(item);
            }
        },
        error -> {
            if (novoDetalheListener != null) {
                novoDetalheListener.onNovoDetalheError("Erro ao obter detalhes");
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

### UPDATE (Atualizar)

```java
public void editNovoAPI(NovoModelo item) {
    String url = mUrlAPINovos + "/" + item.getId();
    
    JSONObject body = new JSONObject();
    try {
        body.put("titulo", item.getTitulo());
        body.put("descricao", item.getDescricao());
    } catch (JSONException e) {
        e.printStackTrace();
        return;
    }
    
    JsonObjectRequest request = new JsonObjectRequest(
        Request.Method.PUT,
        url,
        body,
        response -> {
            if (novoOperacaoListener != null) {
                novoOperacaoListener.onOperacaoNovo(OPERACAO_EDITAR, item);
            }
        },
        error -> {
            if (novoOperacaoListener != null) {
                novoOperacaoListener.onOperacaoNovoError("Erro ao atualizar");
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

### DELETE (Remover)

```java
public void removeNovoAPI(int id) {
    String url = mUrlAPINovos + "/" + id;
    
    StringRequest request = new StringRequest(
        Request.Method.DELETE,
        url,
        response -> {
            if (novoOperacaoListener != null) {
                novoOperacaoListener.onOperacaoNovo(OPERACAO_REMOVER, null);
            }
        },
        error -> {
            if (novoOperacaoListener != null) {
                novoOperacaoListener.onOperacaoNovoError("Erro ao remover");
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

## Adicionar Novo Modelo

### Passo 8: Criar Classe de Modelo

Criar: `veigest-sdk/src/main/java/com/veigest/sdk/models/NovoModelo.java`

```java
package com.veigest.sdk.models;

import com.google.gson.annotations.SerializedName;

public class NovoModelo {
    
    // Campos com anotações Gson
    @SerializedName("id")
    private int id;
    
    @SerializedName("company_id")
    private int companyId;
    
    @SerializedName("titulo")
    private String titulo;
    
    @SerializedName("descricao")
    private String descricao;
    
    @SerializedName("status")
    private String status;
    
    @SerializedName("created_at")
    private String createdAt;
    
    @SerializedName("updated_at")
    private String updatedAt;
    
    // Construtor vazio
    public NovoModelo() {}
    
    // Construtor completo
    public NovoModelo(int id, int companyId, String titulo, 
                      String descricao, String status) {
        this.id = id;
        this.companyId = companyId;
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = status;
    }
    
    // Getters
    public int getId() { return id; }
    public int getCompanyId() { return companyId; }
    public String getTitulo() { return titulo; }
    public String getDescricao() { return descricao; }
    public String getStatus() { return status; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    
    // Setters
    public void setId(int id) { this.id = id; }
    public void setCompanyId(int companyId) { this.companyId = companyId; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}
```

### Adicionar Parser

No `VeiGestJsonParser.java`:

```java
public static ArrayList<NovoModelo> parserJsonNovos(JSONObject response) {
    ArrayList<NovoModelo> items = new ArrayList<>();
    
    try {
        JSONArray data = response.optJSONArray("data");
        if (data == null) data = response.optJSONArray("items");
        
        if (data != null) {
            for (int i = 0; i < data.length(); i++) {
                items.add(parserJsonNovo(data.getJSONObject(i)));
            }
        }
    } catch (JSONException e) {
        e.printStackTrace();
    }
    
    return items;
}

public static NovoModelo parserJsonNovo(JSONObject json) {
    NovoModelo item = new NovoModelo();
    item.setId(json.optInt("id", 0));
    item.setCompanyId(json.optInt("company_id", 0));
    item.setTitulo(json.optString("titulo", ""));
    item.setDescricao(json.optString("descricao", ""));
    item.setStatus(json.optString("status", ""));
    item.setCreatedAt(json.optString("created_at", ""));
    item.setUpdatedAt(json.optString("updated_at", ""));
    return item;
}
```

---

## Criar Adapter para Lista

### Passo 9: Criar Adapter

Criar: `app/src/main/java/pt/ipleiria/estg/dei/veigest/adapters/NovoAdapter.java`

```java
package pt.ipleiria.estg.dei.veigest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.veigest.sdk.models.NovoModelo;

import pt.ipleiria.estg.dei.veigest.R;

import java.util.ArrayList;

public class NovoAdapter extends RecyclerView.Adapter<NovoAdapter.ViewHolder> {
    
    private Context context;
    private ArrayList<NovoModelo> lista;
    private OnItemClickListener listener;
    
    // Interface para cliques
    public interface OnItemClickListener {
        void onItemClick(NovoModelo item);
        void onItemLongClick(NovoModelo item);
    }
    
    // Construtor
    public NovoAdapter(Context context, ArrayList<NovoModelo> lista) {
        this.context = context;
        this.lista = lista;
    }
    
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
            .inflate(R.layout.item_novo, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NovoModelo item = lista.get(position);
        
        holder.tvTitulo.setText(item.getTitulo());
        holder.tvSubtitulo.setText(item.getDescricao());
        holder.tvStatus.setText(item.getStatus());
        
        // Cor do status
        if ("active".equals(item.getStatus())) {
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_active);
        } else {
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_inactive);
        }
        
        // Cliques
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
        
        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) {
                listener.onItemLongClick(item);
            }
            return true;
        });
    }
    
    @Override
    public int getItemCount() {
        return lista.size();
    }
    
    // ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvSubtitulo, tvStatus;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvSubtitulo = itemView.findViewById(R.id.tvSubtitulo);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
```

---

## Checklist de Nova Funcionalidade

### ✅ Lista de Verificação

| # | Tarefa | Ficheiro |
|---|--------|----------|
| 1 | Criar modelo | `sdk/models/NovoModelo.java` |
| 2 | Criar listener | `sdk/listeners/NovoListener.java` |
| 3 | Adicionar endpoint no Singleton | `SingletonVeiGest.java` |
| 4 | Adicionar parser JSON | `VeiGestJsonParser.java` |
| 5 | Criar layout do fragment | `res/layout/fragment_novo.xml` |
| 6 | Criar layout do item | `res/layout/item_novo.xml` |
| 7 | Criar Fragment | `fragments/NovoFragment.java` |
| 8 | Criar Adapter | `adapters/NovoAdapter.java` |
| 9 | Adicionar ícone | `res/drawable/ic_novo.xml` |
| 10 | Adicionar ao menu | `res/menu/nav_menu.xml` |
| 11 | Adicionar à MainActivity | `MainActivity.java` |
| 12 | Adicionar strings | `res/values/strings.xml` |
| 13 | Testar | Build e executar |

### Comandos Úteis

```bash
# Compilar projeto
./gradlew build

# Executar testes
./gradlew test

# Limpar e compilar
./gradlew clean build

# Instalar no dispositivo
./gradlew installDebug
```

---

## 📚 Documentação Relacionada

| Documento | Descrição |
|-----------|-----------|
| [03_Activities_Fragments.md](03_Activities_Fragments.md) | Ciclo de vida de Fragments |
| [04_Navigation_Drawer.md](04_Navigation_Drawer.md) | Como adicionar ao menu |
| [05_Layouts_XML.md](05_Layouts_XML.md) | Criar layouts |
| [08_Listeners_Callbacks.md](08_Listeners_Callbacks.md) | Sistema de listeners |

---

**Última atualização:** Janeiro 2026
