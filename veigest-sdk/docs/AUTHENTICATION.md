# Autenticação

Este documento descreve o sistema de autenticação do VeiGest SDK, incluindo login, logout, gestão de tokens e sessões.

---

## Visão Geral

O SDK utiliza autenticação baseada em **tokens JWT** (JSON Web Tokens):

```
┌──────────────┐     Login      ┌─────────────────┐
│   Cliente    │ ─────────────► │   API VeiGest   │
│  (Android)   │                │                 │
│              │ ◄───────────── │                 │
│              │  access_token  │                 │
│              │  refresh_token │                 │
└──────────────┘                └─────────────────┘
       │
       │ Tokens são armazenados
       │ em EncryptedSharedPreferences
       ▼
┌──────────────────────────────┐
│        AuthManager           │
│  (Armazenamento Seguro)      │
└──────────────────────────────┘
```

---

## AuthService

O serviço de autenticação é acessado via `sdk.auth()`:

```java
VeiGestSDK sdk = VeiGestSDK.getInstance();
AuthService auth = sdk.auth();
```

---

## Login

### Login com Email ou Username

O método `login()` detecta automaticamente se é email ou username:

```java
// Com email
sdk.auth().login("utilizador@empresa.com", "minhaPassword", callback);

// Com username
sdk.auth().login("meuUsername", "minhaPassword", callback);
```

### Métodos Específicos

```java
// Login explícito com email
sdk.auth().loginWithEmail("utilizador@empresa.com", "password", callback);

// Login explícito com username
sdk.auth().loginWithUsername("meuUsername", "password", callback);
```

### Exemplo Completo de Login

```java
sdk.auth().login("utilizador@empresa.com", "password123", 
    new VeiGestCallback<LoginResponse>() {
        @Override
        public void onSuccess(@NonNull LoginResponse response) {
            // Obter token (já é salvo automaticamente)
            String accessToken = response.getAccessToken();
            String refreshToken = response.getRefreshToken();
            int expiresIn = response.getExpiresIn();  // Segundos até expirar
            
            // Obter dados do utilizador
            User user = response.getUser();
            if (user != null) {
                int userId = user.getId();
                int companyId = user.getCompanyId();
                String nome = user.getNome();
                String email = user.getEmail();
                List<String> roles = user.getRoles();
                
                Log.d("Login", "Bem-vindo, " + nome);
                Log.d("Login", "Empresa ID: " + companyId);
                Log.d("Login", "Roles: " + roles);
            }
            
            // Navegar para tela principal
            startMainActivity();
        }
        
        @Override
        public void onError(@NonNull VeiGestException error) {
            if (error.isAuthenticationError()) {
                // Credenciais inválidas (401)
                showError("Email ou password incorretos");
            } else if (error.isNetworkError()) {
                // Sem internet
                showError("Verifique sua conexão à internet");
            } else {
                // Outro erro
                showError("Erro: " + error.getMessage());
            }
        }
    }
);
```

---

## LoginResponse

A resposta de login contém:

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `getAccessToken()` | String | Token de acesso JWT |
| `getRefreshToken()` | String | Token para renovação |
| `getExpiresIn()` | int | Tempo de expiração em segundos |
| `getTokenType()` | String | Tipo do token ("Bearer") |
| `getUser()` | User | Dados do utilizador |

---

## Logout

### Com Callback

```java
sdk.auth().logout(new VeiGestCallback<Void>() {
    @Override
    public void onSuccess(@NonNull Void result) {
        // Logout bem sucedido
        // Tokens foram limpos localmente e invalidados no servidor
        redirectToLogin();
    }
    
    @Override
    public void onError(@NonNull VeiGestException error) {
        // Mesmo com erro na API, o logout local é feito
        // Podemos ignorar o erro e redirecionar
        redirectToLogin();
    }
});
```

### Sem Callback (Fire and Forget)

```java
// Logout simples - não espera resposta
sdk.auth().logout();

// Limpar dados locais e redirecionar
clearLocalData();
redirectToLogin();
```

---

## Verificar Estado de Autenticação

### Verificar se está logado

```java
if (sdk.isAuthenticated()) {
    // Utilizador está autenticado
    showMainScreen();
} else {
    // Não autenticado - mostrar login
    showLoginScreen();
}

// Ou via AuthService
if (sdk.auth().isAuthenticated()) {
    // ...
}
```

### Verificar se o Token Expirou

```java
if (sdk.auth().isTokenExpired()) {
    // Token expirou - renovar ou fazer login novamente
    refreshOrLogin();
}
```

### Obter Informações do Utilizador

```java
// ID do utilizador
int userId = sdk.auth().getUserId();

// ID da empresa
int companyId = sdk.auth().getCompanyId();

// Token de acesso (se precisar usar externamente)
String token = sdk.auth().getAccessToken();

// Ou via SDK
String token = sdk.getAccessToken();
```

---

## Obter Utilizador Atual

```java
// Buscar dados completos do utilizador da API
sdk.auth().getCurrentUser(new VeiGestCallback<User>() {
    @Override
    public void onSuccess(@NonNull User user) {
        String nome = user.getNome();
        String email = user.getEmail();
        String telefone = user.getTelefone();
        List<String> roles = user.getRoles();
        List<String> permissions = user.getPermissions();
        
        updateUserProfile(user);
    }
    
    @Override
    public void onError(@NonNull VeiGestException error) {
        showError("Erro ao carregar perfil: " + error.getMessage());
    }
});

// Alias para getCurrentUser
sdk.auth().me(callback);
```

---

## Renovação de Token (Refresh)

O SDK tenta renovar automaticamente tokens expirados. Mas pode fazer manualmente:

```java
sdk.auth().refreshToken(new VeiGestCallback<LoginResponse>() {
    @Override
    public void onSuccess(@NonNull LoginResponse response) {
        // Novo token obtido e salvo automaticamente
        Log.d("Auth", "Token renovado com sucesso");
    }
    
    @Override
    public void onError(@NonNull VeiGestException error) {
        // Refresh falhou - utilizador precisa fazer login novamente
        Log.e("Auth", "Sessão expirada: " + error.getMessage());
        redirectToLogin();
    }
});
```

---

## Armazenamento Seguro

O SDK usa `EncryptedSharedPreferences` para armazenar tokens de forma segura:

```
┌─────────────────────────────────────────┐
│          AuthManager                     │
├─────────────────────────────────────────┤
│  EncryptedSharedPreferences             │
│  ├── access_token (criptografado)       │
│  ├── refresh_token (criptografado)      │
│  ├── token_expires_at (criptografado)   │
│  ├── user_id (criptografado)            │
│  └── company_id (criptografado)         │
└─────────────────────────────────────────┘
```

Os dados são criptografados usando:
- **AES-256-GCM** para valores
- **AES-256-SIV** para chaves
- Chave mestra armazenada no Android Keystore

---

## Fluxo de Autenticação

```
┌─────────────────────────────────────────────────────────────────┐
│                    Fluxo de Autenticação                         │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  1. App Inicia                                                   │
│        │                                                         │
│        ▼                                                         │
│  ┌─────────────┐                                                │
│  │ isAuthenticated? │───No──► Mostrar LoginScreen               │
│  └─────────────┘                     │                          │
│        │                              │                          │
│       Yes                        Login Success                   │
│        │                              │                          │
│        ▼                              ▼                          │
│  ┌─────────────┐              ┌─────────────────┐               │
│  │isTokenExpired?│───Yes──►   │ Salvar Tokens   │               │
│  └─────────────┘              └─────────────────┘               │
│        │                              │                          │
│       No                              │                          │
│        │                              │                          │
│        ▼                              ▼                          │
│  ┌─────────────────────────────────────────┐                    │
│  │          Mostrar MainScreen             │                    │
│  └─────────────────────────────────────────┘                    │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## Exemplo: Tela de Login Completa

```java
public class LoginActivity extends AppCompatActivity {
    
    private EditText emailInput;
    private EditText passwordInput;
    private Button loginButton;
    private ProgressBar progressBar;
    private VeiGestSDK sdk;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        sdk = VeiGestSDK.getInstance();
        
        // Verificar se já está logado
        if (sdk.isAuthenticated() && !sdk.auth().isTokenExpired()) {
            goToMain();
            return;
        }
        
        initViews();
        setupListeners();
    }
    
    private void initViews() {
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progressBar);
    }
    
    private void setupListeners() {
        loginButton.setOnClickListener(v -> doLogin());
    }
    
    private void doLogin() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString();
        
        // Validação básica
        if (email.isEmpty()) {
            emailInput.setError("Email obrigatório");
            return;
        }
        if (password.isEmpty()) {
            passwordInput.setError("Password obrigatória");
            return;
        }
        
        // Mostrar loading
        setLoading(true);
        
        // Fazer login
        sdk.auth().login(email, password, new VeiGestCallback<LoginResponse>() {
            @Override
            public void onSuccess(@NonNull LoginResponse response) {
                setLoading(false);
                
                User user = response.getUser();
                Toast.makeText(LoginActivity.this, 
                    "Bem-vindo, " + user.getNome() + "!", 
                    Toast.LENGTH_SHORT).show();
                
                goToMain();
            }
            
            @Override
            public void onError(@NonNull VeiGestException error) {
                setLoading(false);
                handleLoginError(error);
            }
        });
    }
    
    private void handleLoginError(VeiGestException error) {
        String message;
        
        switch (error.getType()) {
            case AUTHENTICATION:
                message = "Email ou password incorretos";
                break;
            case NETWORK:
                message = "Verifique sua conexão à internet";
                break;
            case SERVER:
                message = "Servidor indisponível. Tente mais tarde.";
                break;
            default:
                message = error.getMessage();
        }
        
        new AlertDialog.Builder(this)
            .setTitle("Erro no Login")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show();
    }
    
    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        loginButton.setEnabled(!loading);
        emailInput.setEnabled(!loading);
        passwordInput.setEnabled(!loading);
    }
    
    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
```

---

## Exemplo: Verificação de Sessão na MainActivity

```java
public class MainActivity extends AppCompatActivity {
    
    private VeiGestSDK sdk;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        sdk = VeiGestSDK.getInstance();
        
        // Verificar autenticação
        checkAuthentication();
    }
    
    private void checkAuthentication() {
        if (!sdk.isAuthenticated()) {
            redirectToLogin();
            return;
        }
        
        if (sdk.auth().isTokenExpired()) {
            // Tentar renovar token
            sdk.auth().refreshToken(new VeiGestCallback<LoginResponse>() {
                @Override
                public void onSuccess(@NonNull LoginResponse response) {
                    // Token renovado - continuar normalmente
                    loadDashboard();
                }
                
                @Override
                public void onError(@NonNull VeiGestException error) {
                    // Não foi possível renovar - fazer login novamente
                    Toast.makeText(MainActivity.this, 
                        "Sessão expirada. Faça login novamente.", 
                        Toast.LENGTH_LONG).show();
                    redirectToLogin();
                }
            });
        } else {
            loadDashboard();
        }
    }
    
    private void redirectToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    
    private void loadDashboard() {
        // Carregar dados do dashboard
    }
    
    private void logout() {
        sdk.auth().logout();
        redirectToLogin();
    }
}
```

---

## Referência Rápida

| Método | Descrição |
|--------|-----------|
| `login(email, password, callback)` | Login com email ou username |
| `loginWithEmail(email, password, callback)` | Login explícito com email |
| `loginWithUsername(username, password, callback)` | Login explícito com username |
| `logout(callback)` | Logout com callback |
| `logout()` | Logout sem callback |
| `refreshToken(callback)` | Renovar token de acesso |
| `getCurrentUser(callback)` | Obter dados do utilizador atual |
| `me(callback)` | Alias para getCurrentUser |
| `isAuthenticated()` | Verificar se está autenticado |
| `isTokenExpired()` | Verificar se o token expirou |
| `getAccessToken()` | Obter o token de acesso |
| `getUserId()` | Obter ID do utilizador |
| `getCompanyId()` | Obter ID da empresa |

---

## Próximos Passos

- [Usar os Serviços](SERVICES.md)
- [Tratamento de Erros](EXCEPTIONS.md)
- [Boas Práticas](BEST_PRACTICES.md)
