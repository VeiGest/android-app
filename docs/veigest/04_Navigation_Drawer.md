# VeiGest - Navigation Drawer
## Implementação do Menu Lateral

**ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO - 2025 / 2026**  
**TESP PROGRAMAÇÃO DE SISTEMAS DE INFORMAÇÃO**

---

## 📖 Índice

1. [Conceito de Navigation Drawer](#conceito-de-navigation-drawer)
2. [Estrutura do Layout](#estrutura-do-layout)
3. [Menu de Navegação](#menu-de-navegação)
4. [Header do Drawer](#header-do-drawer)
5. [Implementação na MainActivity](#implementação-na-mainactivity)
6. [Controlo de Acesso](#controlo-de-acesso)
7. [Personalização](#personalização)

---

## Conceito de Navigation Drawer

O **Navigation Drawer** é um painel de navegação lateral que desliza da esquerda para a direita. No VeiGest, é usado para:

- Navegação principal entre secções da app
- Exibir informações do utilizador (header)
- Acesso rápido a funcionalidades
- Logout

### Componentes Necessários

| Componente | Descrição |
|------------|-----------|
| `DrawerLayout` | Container raiz que permite o drawer |
| `NavigationView` | O drawer em si com menu e header |
| `FrameLayout` | Container para os fragments |
| Menu XML | Itens do drawer |
| Header XML | Cabeçalho com info do utilizador |

---

## Estrutura do Layout

### activity_main.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.drawerlayout.widget.DrawerLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/drawer_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <!-- Conteúdo Principal -->
    <androidx.constraintlayout.widget.ConstraintLayout
        android:id="@+id/main"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <!-- Container dos Fragments -->
        <FrameLayout
            android:id="@+id/fragment_container"
            android:layout_width="0dp"
            android:layout_height="0dp"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent"/>

    </androidx.constraintlayout.widget.ConstraintLayout>

    <!-- Navigation Drawer (Menu Lateral) -->
    <com.google.android.material.navigation.NavigationView
        android:id="@+id/nav_view"
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        android:layout_gravity="start"
        android:fitsSystemWindows="true"
        app:headerLayout="@layout/nav_header"
        app:menu="@menu/nav_drawer_menu"
        app:itemIconTint="#11C7A5"
        app:itemTextColor="@color/Black" />

</androidx.drawerlayout.widget.DrawerLayout>
```

### Estrutura Visual

```
┌─────────────────────────────────────────────────────────────┐
│  DrawerLayout                                               │
│  ┌───────────────────────────────────────────────────────┐ │
│  │  ConstraintLayout (Conteúdo Principal)                │ │
│  │  ┌─────────────────────────────────────────────────┐  │ │
│  │  │                                                 │  │ │
│  │  │              FrameLayout                        │  │ │
│  │  │           (fragment_container)                  │  │ │
│  │  │                                                 │  │ │
│  │  │    ┌─────────────────────────────────────────┐ │  │ │
│  │  │    │                                         │ │  │ │
│  │  │    │        Fragment Atual                   │ │  │ │
│  │  │    │        (Login, Dashboard, etc.)         │ │  │ │
│  │  │    │                                         │ │  │ │
│  │  │    └─────────────────────────────────────────┘ │  │ │
│  │  │                                                 │  │ │
│  │  └─────────────────────────────────────────────────┘  │ │
│  └───────────────────────────────────────────────────────┘ │
│                                                             │
│  ┌──────────────┐ ◄── NavigationView (desliza da esquerda) │
│  │ Nav Header   │                                           │
│  ├──────────────┤                                           │
│  │ Menu Item 1  │                                           │
│  │ Menu Item 2  │                                           │
│  │ Menu Item 3  │                                           │
│  │ ...          │                                           │
│  └──────────────┘                                           │
└─────────────────────────────────────────────────────────────┘
```

---

## Menu de Navegação

### nav_drawer_menu.xml (res/menu/)

```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">

    <!-- Grupo Principal de Navegação -->
    <group android:checkableBehavior="single">
        
        <!-- Dashboard -->
        <item
            android:id="@+id/nav_dashboard"
            android:icon="@drawable/ic_menu_dashboard"
            android:title="@string/menu_dashboard" />

        <!-- Rotas -->
        <item
            android:id="@+id/nav_routes"
            android:icon="@drawable/ic_menu_routes"
            android:title="@string/menu_routes" />

        <!-- Veículos -->
        <item
            android:id="@+id/nav_vehicles"
            android:icon="@drawable/ic_dashboard_vehicle"
            android:title="@string/menu_vehicles" />

        <!-- Documentos -->
        <item
            android:id="@+id/nav_documents"
            android:icon="@drawable/ic_dashboard_document"
            android:title="@string/menu_documents" />

    </group>

    <!-- Separador e Configurações -->
    <item android:title="@string/menu_settings_group">
        <menu>
            
            <!-- Perfil -->
            <item
                android:id="@+id/nav_profile"
                android:icon="@drawable/ic_menu_profile"
                android:title="@string/menu_profile" />

            <!-- Definições -->
            <item
                android:id="@+id/nav_settings"
                android:icon="@drawable/ic_menu_settings"
                android:title="@string/menu_settings" />

            <!-- Logout -->
            <item
                android:id="@+id/nav_logout"
                android:icon="@drawable/ic_menu_logout"
                android:title="@string/menu_logout" />

        </menu>
    </item>

</menu>
```

### Atributos do Menu

| Atributo | Descrição |
|----------|-----------|
| `android:id` | Identificador único do item |
| `android:icon` | Ícone a apresentar |
| `android:title` | Texto do item |
| `checkableBehavior="single"` | Apenas um item selecionado de cada vez |

---

## Header do Drawer

### nav_header.xml (res/layout/)

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="180dp"
    android:orientation="vertical"
    android:gravity="bottom"
    android:padding="16dp"
    android:background="@color/veigest_primary">

    <!-- Logo ou Avatar -->
    <ImageView
        android:id="@+id/iv_nav_header_avatar"
        android:layout_width="72dp"
        android:layout_height="72dp"
        android:src="@drawable/ic_veigest"
        android:layout_marginBottom="12dp"
        android:contentDescription="@string/app_name" />

    <!-- Nome do Utilizador -->
    <TextView
        android:id="@+id/tv_nav_header_name"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/default_user_name"
        android:textSize="18sp"
        android:textStyle="bold"
        android:textColor="@android:color/white" />

    <!-- Email do Utilizador -->
    <TextView
        android:id="@+id/tv_nav_header_email"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/default_user_email"
        android:textSize="14sp"
        android:textColor="@android:color/white"
        android:alpha="0.8" />

</LinearLayout>
```

### Atualizar Header Dinamicamente

```java
// Na MainActivity
private void updateNavHeader() {
    NavigationView navigationView = findViewById(R.id.nav_view);
    View headerView = navigationView.getHeaderView(0);
    
    TextView tvName = headerView.findViewById(R.id.tv_nav_header_name);
    TextView tvEmail = headerView.findViewById(R.id.tv_nav_header_email);
    
    User user = singleton.getUtilizadorAtual();
    if (user != null) {
        tvName.setText(user.getUsername());
        tvEmail.setText(user.getEmail());
    }
}
```

---

## Implementação na MainActivity

### Configuração Inicial

```java
public class MainActivity extends AppCompatActivity 
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private boolean isLoggedIn = false;
    private SingletonVeiGest singleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obter instância do Singleton
        singleton = SingletonVeiGest.getInstance(getApplicationContext());

        // Inicializar DrawerLayout e NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        
        // Registar listener para cliques no menu
        navigationView.setNavigationItemSelectedListener(this);

        // Bloquear drawer até fazer login
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        // Carregar fragment inicial
        if (savedInstanceState == null) {
            if (singleton.isAuthenticated()) {
                loadDashboard();
            } else {
                loadFragment(new LoginFragment());
            }
        }
    }
}
```

### Tratar Seleção de Itens

```java
@Override
public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    int itemId = item.getItemId();

    if (itemId == R.id.nav_dashboard) {
        loadFragment(new DashboardFragment());
        
    } else if (itemId == R.id.nav_routes) {
        loadFragment(new RoutesFragment());
        
    } else if (itemId == R.id.nav_vehicles) {
        loadFragment(new VehiclesFragment());
        
    } else if (itemId == R.id.nav_documents) {
        loadFragment(new DocumentsFragment());
        
    } else if (itemId == R.id.nav_profile) {
        loadFragment(new ProfileFragment());
        
    } else if (itemId == R.id.nav_settings) {
        loadFragment(new SettingsFragment());
        
    } else if (itemId == R.id.nav_logout) {
        performLogout();
    }

    // Fechar drawer após seleção
    drawerLayout.closeDrawer(GravityCompat.START);
    
    return true;
}
```

### Métodos de Navegação

```java
/**
 * Carrega um fragment no container principal
 */
public void loadFragment(Fragment fragment) {
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.replace(R.id.fragment_container, fragment);
    fragmentTransaction.commit();
}

/**
 * Carrega o Dashboard após login bem-sucedido
 */
public void loadDashboard() {
    isLoggedIn = true;
    
    // Desbloquear drawer após login
    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    
    loadFragment(new DashboardFragment());
    
    // Marcar Dashboard como selecionado no menu
    navigationView.setCheckedItem(R.id.nav_dashboard);
    
    // Atualizar header com dados do utilizador
    updateNavHeader();
}

/**
 * Carrega fragment de registo
 */
public void loadRegisterFragment() {
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.replace(R.id.fragment_container, new RegisterFragment());
    fragmentTransaction.addToBackStack(null); // Permite voltar ao login
    fragmentTransaction.commit();
}
```

---

## Controlo de Acesso

### Bloquear/Desbloquear Drawer

```java
// Bloquear (utilizador não autenticado)
drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

// Desbloquear (utilizador autenticado)
drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

// Bloquear aberto (manter sempre visível - tablets)
drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
```

### Logout

```java
private void performLogout() {
    // Limpar token e dados do utilizador
    singleton.clearAuth();
    
    Toast.makeText(this, "Sessão terminada com sucesso", Toast.LENGTH_SHORT).show();
    
    navigateToLogin();
}

public void navigateToLogin() {
    isLoggedIn = false;
    
    // Bloquear drawer após logout
    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    
    // Voltar para o login
    loadFragment(new LoginFragment());
}
```

### Abrir Drawer Programaticamente

```java
/**
 * Método público para Fragments abrirem o drawer
 */
public void openDrawer() {
    if (isLoggedIn) {
        drawerLayout.openDrawer(GravityCompat.START);
    }
}

// Usar num Fragment
public class DashboardFragment extends Fragment {
    
    private void onMenuButtonClick() {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.openDrawer();
        }
    }
}
```

### Tratar Botão Back

```java
@Override
public void onBackPressed() {
    // Fechar drawer se estiver aberto
    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
        drawerLayout.closeDrawer(GravityCompat.START);
    } else {
        super.onBackPressed();
    }
}
```

---

## Personalização

### Ícones do Menu (drawables)

Os ícones são definidos como vector drawables em `res/drawable/`:

```xml
<!-- ic_menu_dashboard.xml -->
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24">
    <path
        android:fillColor="#FF000000"
        android:pathData="M3,13h8V3H3v10zm0,8h8v-6H3v6zm10,0h8V11h-8v10zm0-18v6h8V3h-8z"/>
</vector>
```

### Cores do Menu

```xml
<!-- No NavigationView -->
<com.google.android.material.navigation.NavigationView
    app:itemIconTint="#11C7A5"           <!-- Cor dos ícones -->
    app:itemTextColor="@color/Black"     <!-- Cor do texto -->
    app:itemBackground="@drawable/nav_item_background" />
```

### Selector para Background dos Itens

```xml
<!-- nav_item_background.xml -->
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:state_checked="true"
          android:drawable="@color/nav_item_selected" />
    <item android:drawable="@android:color/transparent" />
</selector>
```

### Strings para Menu

```xml
<!-- strings.xml -->
<resources>
    <string name="menu_dashboard">Dashboard</string>
    <string name="menu_routes">Rotas</string>
    <string name="menu_vehicles">Veículos</string>
    <string name="menu_documents">Documentos</string>
    <string name="menu_profile">Perfil</string>
    <string name="menu_settings">Definições</string>
    <string name="menu_logout">Terminar Sessão</string>
    <string name="menu_settings_group">Conta</string>
</resources>
```

---

## Exemplo Completo da MainActivity

```java
public class MainActivity extends AppCompatActivity 
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private boolean isLoggedIn = false;
    private SingletonVeiGest singleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        singleton = SingletonVeiGest.getInstance(getApplicationContext());

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        if (savedInstanceState == null) {
            if (singleton.isAuthenticated()) {
                loadDashboard();
            } else {
                loadFragment(new LoginFragment());
            }
        }
    }

    public void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit();
    }

    public void loadDashboard() {
        isLoggedIn = true;
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        loadFragment(new DashboardFragment());
        navigationView.setCheckedItem(R.id.nav_dashboard);
    }

    public void loadRegisterFragment() {
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.fragment_container, new RegisterFragment())
            .addToBackStack(null)
            .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_dashboard) {
            loadFragment(new DashboardFragment());
        } else if (itemId == R.id.nav_routes) {
            loadFragment(new RoutesFragment());
        } else if (itemId == R.id.nav_vehicles) {
            loadFragment(new VehiclesFragment());
        } else if (itemId == R.id.nav_documents) {
            loadFragment(new DocumentsFragment());
        } else if (itemId == R.id.nav_profile) {
            loadFragment(new ProfileFragment());
        } else if (itemId == R.id.nav_settings) {
            loadFragment(new SettingsFragment());
        } else if (itemId == R.id.nav_logout) {
            performLogout();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void performLogout() {
        singleton.clearAuth();
        Toast.makeText(this, "Sessão terminada", Toast.LENGTH_SHORT).show();
        navigateToLogin();
    }
    
    public void navigateToLogin() {
        isLoggedIn = false;
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        loadFragment(new LoginFragment());
    }

    public void openDrawer() {
        if (isLoggedIn) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
```

---

## 📚 Documentação Relacionada

| Documento | Descrição |
|-----------|-----------|
| [03_Activities_Fragments.md](03_Activities_Fragments.md) | Activities e Fragments |
| [05_Layouts_XML.md](05_Layouts_XML.md) | Layouts e recursos XML |
| [09_Implementar_Novas_Funcionalidades.md](09_Implementar_Novas_Funcionalidades.md) | Adicionar novos itens ao menu |

---

**Última atualização:** Janeiro 2026
