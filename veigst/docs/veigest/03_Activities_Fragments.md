# VeiGest - Activities e Fragments
## Navegação e Ciclo de Vida

**ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO - 2025 / 2026**  
**TESP PROGRAMAÇÃO DE SISTEMAS DE INFORMAÇÃO**

---

## 📖 Índice

1. [Conceitos Fundamentais](#conceitos-fundamentais)
2. [Activity no VeiGest](#activity-no-veigest)
3. [Fragments no VeiGest](#fragments-no-veigest)
4. [Ciclo de Vida](#ciclo-de-vida)
5. [Comunicação Activity-Fragment](#comunicação-activity-fragment)
6. [Navegação entre Fragments](#navegação-entre-fragments)
7. [Implementação dos Fragments Existentes](#implementação-dos-fragments-existentes)

---

## Conceitos Fundamentais

### O que é uma Activity?

Uma **Activity** é um componente de uma aplicação Android que fornece um ecrã com o qual os utilizadores podem interagir. É o ponto de entrada da aplicação.

### O que é um Fragment?

Um **Fragment** é uma porção reutilizável da interface de uma Activity. Os Fragments:

- Têm o seu próprio ciclo de vida
- Dependem da Activity que os hospeda
- Podem ser reutilizados em múltiplas Activities
- Facilitam layouts adaptativos (tablets vs smartphones)

### Vantagens de usar Fragments

| Vantagem | Descrição |
|----------|-----------|
| **Reutilização** | O mesmo Fragment pode ser usado em várias Activities |
| **Modularidade** | Cada ecrã é uma unidade independente |
| **Flexibilidade** | Fácil adaptar para diferentes tamanhos de ecrã |
| **Manutenção** | Código mais organizado e fácil de manter |
| **Navegação** | Transições suaves entre ecrãs |

---

## Activity no VeiGest

### MainActivity - Activity Principal

O VeiGest usa **Single Activity Architecture** - uma única Activity que hospeda todos os Fragments.

```java
package com.ipleiria.veigest;

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
        navigationView.setNavigationItemSelectedListener(this);

        // Bloquear drawer até fazer login
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        // Só adiciona o fragment se for a primeira criação
        if (savedInstanceState == null) {
            // Verificar sessão ativa
            if (singleton.isAuthenticated()) {
                loadDashboard();
            } else {
                loadFragment(new LoginFragment());
            }
        }
    }
}
```

### Responsabilidades da MainActivity

| Responsabilidade | Descrição |
|------------------|-----------|
| **Hospedagem** | Container para todos os Fragments |
| **Navegação** | Gerencia Navigation Drawer |
| **Estado** | Controla estado de autenticação |
| **Transições** | Executa mudanças de Fragment |

---

## Fragments no VeiGest

### Lista de Fragments Existentes

| Fragment | Descrição | Implementa |
|----------|-----------|------------|
| `LoginFragment` | Ecrã de autenticação | `LoginListener` |
| `RegisterFragment` | Ecrã de registo | `RegisterListener` |
| `DashboardFragment` | Painel principal | `VeiculosListener`, `RotasListener` |
| `VehiclesFragment` | Lista de veículos | `VeiculosListener` |
| `RoutesFragment` | Lista de rotas | `RotasListener` |
| `DocumentsFragment` | Documentação | `DocumentosListener` |
| `ProfileFragment` | Perfil do utilizador | - |
| `SettingsFragment` | Configurações | - |

### Estrutura Base de um Fragment

```java
public class ExemploFragment extends Fragment implements AlgumListener {
    
    private static final String TAG = "ExemploFragment";
    
    // Views
    private TextView tvTitulo;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    
    // Singleton
    private SingletonVeiGest singleton;

    // Construtor vazio obrigatório
    public ExemploFragment() {
        // Required empty public constructor
    }

    // Método factory para criar instância
    public static ExemploFragment newInstance() {
        ExemploFragment fragment = new ExemploFragment();
        // Pode adicionar arguments aqui
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Obter instância do Singleton
        singleton = SingletonVeiGest.getInstance(requireContext());
        
        // Registar como listener
        singleton.setAlgumListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate do layout
        View view = inflater.inflate(R.layout.fragment_exemplo, container, false);

        // Inicializar views
        initializeViews(view);

        // Configurar listeners de UI
        setupListeners();

        // Carregar dados
        loadData();

        return view;
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        
        // IMPORTANTE: Remover listener
        singleton.setAlgumListener(null);
    }

    private void initializeViews(View view) {
        tvTitulo = view.findViewById(R.id.tv_titulo);
        recyclerView = view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.progress_bar);
    }

    private void setupListeners() {
        // Configurar cliques, etc.
    }

    private void loadData() {
        progressBar.setVisibility(View.VISIBLE);
        singleton.getAllAlgoAPI();
    }

    // Callbacks do Listener
    @Override
    public void onRefreshAlgo(ArrayList<Algo> lista) {
        if (getActivity() == null) return;
        
        getActivity().runOnUiThread(() -> {
            progressBar.setVisibility(View.GONE);
            // Atualizar UI
        });
    }

    @Override
    public void onRefreshAlgoError(String error) {
        if (getActivity() == null) return;
        
        getActivity().runOnUiThread(() -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        });
    }
}
```

---

## Ciclo de Vida

### Ciclo de Vida da Activity

```
┌─────────────────────────────────────────────────────────────┐
│                      ACTIVITY LIFECYCLE                      │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│   ┌─────────┐                                               │
│   │ onCreate│ ◄── Activity criada                           │
│   └────┬────┘                                               │
│        ▼                                                    │
│   ┌─────────┐                                               │
│   │ onStart │ ◄── Activity visível                          │
│   └────┬────┘                                               │
│        ▼                                                    │
│   ┌──────────┐                                              │
│   │ onResume │ ◄── Activity em primeiro plano               │
│   └────┬─────┘     (utilizador pode interagir)              │
│        │                                                    │
│        │  ◄───── RUNNING ─────►                             │
│        │                                                    │
│        ▼                                                    │
│   ┌─────────┐                                               │
│   │ onPause │ ◄── Outra Activity em foco                    │
│   └────┬────┘                                               │
│        ▼                                                    │
│   ┌────────┐                                                │
│   │ onStop │ ◄── Activity não visível                       │
│   └────┬───┘                                                │
│        ▼                                                    │
│   ┌───────────┐                                             │
│   │ onDestroy │ ◄── Activity destruída                      │
│   └───────────┘                                             │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### Ciclo de Vida do Fragment

```
┌─────────────────────────────────────────────────────────────┐
│                      FRAGMENT LIFECYCLE                      │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│   ┌──────────┐                                              │
│   │ onAttach │ ◄── Fragment associado à Activity            │
│   └────┬─────┘                                              │
│        ▼                                                    │
│   ┌──────────┐                                              │
│   │ onCreate │ ◄── Fragment criado                          │
│   └────┬─────┘     (inicializar componentes)                │
│        ▼                                                    │
│   ┌───────────────┐                                         │
│   │ onCreateView  │ ◄── Criar interface (inflate layout)    │
│   └──────┬────────┘                                         │
│          ▼                                                  │
│   ┌───────────────┐                                         │
│   │ onViewCreated │ ◄── View criada (inicializar views)     │
│   └──────┬────────┘                                         │
│          ▼                                                  │
│   ┌─────────┐                                               │
│   │ onStart │ ◄── Fragment visível                          │
│   └────┬────┘                                               │
│        ▼                                                    │
│   ┌──────────┐                                              │
│   │ onResume │ ◄── Fragment ativo                           │
│   └────┬─────┘                                              │
│        │                                                    │
│        │  ◄───── RUNNING ─────►                             │
│        │                                                    │
│        ▼                                                    │
│   ┌─────────┐                                               │
│   │ onPause │ ◄── Fragment pausado                          │
│   └────┬────┘     (guardar dados importantes)               │
│        ▼                                                    │
│   ┌────────┐                                                │
│   │ onStop │ ◄── Fragment não visível                       │
│   └────┬───┘                                                │
│        ▼                                                    │
│   ┌───────────────┐                                         │
│   │ onDestroyView │ ◄── View destruída                      │
│   └──────┬────────┘     (libertar recursos de UI)           │
│          ▼                                                  │
│   ┌───────────┐                                             │
│   │ onDestroy │ ◄── Fragment destruído                      │
│   └─────┬─────┘                                             │
│         ▼                                                   │
│   ┌──────────┐                                              │
│   │ onDetach │ ◄── Fragment desassociado da Activity        │
│   └──────────┘                                              │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### Quando usar cada método

| Método | Usar para |
|--------|-----------|
| `onCreate()` | Inicializar Singleton, registar listeners |
| `onCreateView()` | Inflate do layout |
| `onViewCreated()` | Inicializar views, configurar adapters |
| `onStart()` | Iniciar animações, registar receivers |
| `onResume()` | Retomar operações pausadas |
| `onPause()` | Pausar operações, guardar dados temporários |
| `onStop()` | Parar animações, remover receivers |
| `onDestroyView()` | Remover listeners do Singleton |
| `onDestroy()` | Libertar recursos |

---

## Comunicação Activity-Fragment

### Fragment acede à Activity

```java
// Dentro do Fragment
public void algumMetodo() {
    // Obter referência à Activity
    MainActivity mainActivity = (MainActivity) getActivity();
    
    // Verificar se não é null
    if (mainActivity != null) {
        // Chamar métodos da Activity
        mainActivity.openDrawer();
        mainActivity.loadDashboard();
        mainActivity.navigateToLogin();
    }
}
```

### Activity acede ao Fragment

```java
// Dentro da Activity
public void algumMetodo() {
    // Encontrar Fragment pelo tag
    DashboardFragment fragment = (DashboardFragment) 
        getSupportFragmentManager().findFragmentByTag("DASHBOARD_TAG");
    
    if (fragment != null) {
        // Chamar métodos do Fragment
        fragment.refreshData();
    }
}
```

### Usando Interfaces (Recomendado)

```java
// Interface no Fragment
public interface OnFragmentInteractionListener {
    void onNavigateToDashboard();
    void onLogout();
}

// No Fragment
public class LoginFragment extends Fragment {
    
    private OnFragmentInteractionListener listener;
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() 
                + " must implement OnFragmentInteractionListener");
        }
    }
    
    private void onLoginSuccess() {
        if (listener != null) {
            listener.onNavigateToDashboard();
        }
    }
}

// Na Activity
public class MainActivity extends AppCompatActivity 
        implements LoginFragment.OnFragmentInteractionListener {
    
    @Override
    public void onNavigateToDashboard() {
        loadDashboard();
    }
    
    @Override
    public void onLogout() {
        performLogout();
    }
}
```

---

## Navegação entre Fragments

### Método loadFragment na MainActivity

```java
public void loadFragment(Fragment fragment) {
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.replace(R.id.fragment_container, fragment);
    fragmentTransaction.commit();
}
```

### Com Back Stack (permite voltar atrás)

```java
public void loadFragmentWithBackStack(Fragment fragment) {
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.replace(R.id.fragment_container, fragment);
    fragmentTransaction.addToBackStack(null);  // Adiciona ao back stack
    fragmentTransaction.commit();
}
```

### Com Animações

```java
public void loadFragmentWithAnimation(Fragment fragment) {
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    
    // Adicionar animações
    fragmentTransaction.setCustomAnimations(
        R.anim.slide_in_right,   // Enter
        R.anim.slide_out_left,   // Exit
        R.anim.slide_in_left,    // Pop enter
        R.anim.slide_out_right   // Pop exit
    );
    
    fragmentTransaction.replace(R.id.fragment_container, fragment);
    fragmentTransaction.addToBackStack(null);
    fragmentTransaction.commit();
}
```

---

## Implementação dos Fragments Existentes

### LoginFragment

```java
public class LoginFragment extends Fragment implements LoginListener {

    private static final String TAG = "LoginFragment";

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private CheckBox cbRemember;
    private TextView tvNoAccount;
    private ProgressBar progressBar;
    
    private SingletonVeiGest singleton;

    public LoginFragment() { }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        singleton = SingletonVeiGest.getInstance(requireContext());
        singleton.setLoginListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Inicializar views
        etUsername = view.findViewById(R.id.et_username);
        etPassword = view.findViewById(R.id.et_password);
        btnLogin = view.findViewById(R.id.btn_login);
        cbRemember = view.findViewById(R.id.cbRemember);
        tvNoAccount = view.findViewById(R.id.tvNoAccount);
        progressBar = view.findViewById(R.id.progress_bar);

        // Botão de login
        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            performLogin(username, password);
        });

        // Link para registo
        tvNoAccount.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).loadRegisterFragment();
            }
        });

        return view;
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        singleton.setLoginListener(null);
    }

    private void performLogin(String email, String password) {
        setLoading(true);
        singleton.loginAPI(email, password);
    }
    
    @Override
    public void onValidateLogin(String token, User user) {
        if (getActivity() == null) return;

        getActivity().runOnUiThread(() -> {
            setLoading(false);
            Toast.makeText(getContext(), "Bem-vindo, " + user.getUsername(), Toast.LENGTH_SHORT).show();

            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).loadDashboard();
            }
        });
    }
    
    @Override
    public void onLoginError(String errorMessage) {
        if (getActivity() == null) return;

        getActivity().runOnUiThread(() -> {
            setLoading(false);
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
        });
    }

    private void setLoading(boolean loading) {
        if (progressBar != null) {
            progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        }
        if (btnLogin != null) {
            btnLogin.setEnabled(!loading);
        }
    }
}
```

### DashboardFragment (Resumo)

```java
public class DashboardFragment extends Fragment 
        implements VeiculosListener, RotasListener {

    private SingletonVeiGest singleton;
    
    // Views de informação
    private TextView tvDriverName;
    private MaterialCardView cardActiveRoute;
    private MaterialCardView cardCurrentVehicle;
    // ... mais views

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        singleton = SingletonVeiGest.getInstance(requireContext());
        singleton.setVeiculosListener(this);
        singleton.setRotasListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        
        initializeViews(view);
        setupListeners();
        loadUserData();
        
        return view;
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        singleton.setVeiculosListener(null);
        singleton.setRotasListener(null);
    }

    private void loadUserData() {
        User user = singleton.getUtilizadorAtual();
        if (user != null) {
            tvDriverName.setText(user.getUsername());
        }
        
        // Carregar dados da API
        singleton.getAllVeiculosAPI();
        singleton.getAllRotasAPI();
    }

    @Override
    public void onRefreshVeiculos(ArrayList<Vehicle> veiculos) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(() -> {
            // Atualizar card de veículo
            updateVehicleCard(veiculos);
        });
    }

    @Override
    public void onRefreshRotas(ArrayList<Route> rotas) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(() -> {
            // Atualizar card de rota
            updateRouteCard(rotas);
        });
    }
    
    // ... implementações de erro
}
```

---

## 📚 Documentação Relacionada

| Documento | Descrição |
|-----------|-----------|
| [04_Navigation_Drawer.md](04_Navigation_Drawer.md) | Sistema de navegação lateral |
| [05_Layouts_XML.md](05_Layouts_XML.md) | Layouts e recursos XML |
| [08_Listeners_Callbacks.md](08_Listeners_Callbacks.md) | Sistema de listeners |
| [09_Implementar_Novas_Funcionalidades.md](09_Implementar_Novas_Funcionalidades.md) | Criar novos fragments |

---

**Última atualização:** Janeiro 2026
