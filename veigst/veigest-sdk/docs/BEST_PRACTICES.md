# Boas Práticas

Recomendações e padrões para utilização eficiente do VeiGest SDK.

---

## Arquitetura Recomendada

### Padrão Repository

```java
// Repository encapsula acesso ao SDK
public class VehicleRepository {
    private final VeiGestSDK sdk;
    private final VehicleCache cache;
    
    public VehicleRepository(VeiGestSDK sdk) {
        this.sdk = sdk;
        this.cache = new VehicleCache();
    }
    
    public void getVehicle(int id, VeiGestCallback<Vehicle> callback) {
        // Verificar cache primeiro
        Vehicle cached = cache.get(id);
        if (cached != null) {
            callback.onSuccess(cached);
            return;
        }
        
        // Buscar da API
        sdk.vehicles().get(id, new VeiGestCallback<Vehicle>() {
            @Override
            public void onSuccess(@NonNull Vehicle vehicle) {
                cache.put(id, vehicle);
                callback.onSuccess(vehicle);
            }
            
            @Override
            public void onError(@NonNull VeiGestException error) {
                callback.onError(error);
            }
        });
    }
    
    public void refreshVehicles(VeiGestCallback<List<Vehicle>> callback) {
        cache.clear();
        sdk.vehicles().list(new VeiGestCallback<List<Vehicle>>() {
            @Override
            public void onSuccess(@NonNull List<Vehicle> vehicles) {
                for (Vehicle v : vehicles) {
                    cache.put(v.getId(), v);
                }
                callback.onSuccess(vehicles);
            }
            
            @Override
            public void onError(@NonNull VeiGestException error) {
                callback.onError(error);
            }
        });
    }
}
```

### Padrão ViewModel (MVVM)

```java
public class VehiclesViewModel extends ViewModel {
    private final VehicleRepository repository;
    private final MutableLiveData<List<Vehicle>> vehicles = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    
    public VehiclesViewModel(VehicleRepository repository) {
        this.repository = repository;
    }
    
    public LiveData<List<Vehicle>> getVehicles() {
        return vehicles;
    }
    
    public LiveData<Boolean> isLoading() {
        return loading;
    }
    
    public LiveData<String> getError() {
        return error;
    }
    
    public void loadVehicles() {
        loading.setValue(true);
        error.setValue(null);
        
        repository.refreshVehicles(new VeiGestCallback<List<Vehicle>>() {
            @Override
            public void onSuccess(@NonNull List<Vehicle> result) {
                vehicles.postValue(result);
                loading.postValue(false);
            }
            
            @Override
            public void onError(@NonNull VeiGestException e) {
                error.postValue(e.getMessage());
                loading.postValue(false);
            }
        });
    }
}

// Na Activity/Fragment
viewModel.getVehicles().observe(this, vehicles -> {
    adapter.submitList(vehicles);
});

viewModel.isLoading().observe(this, isLoading -> {
    progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
});

viewModel.getError().observe(this, errorMsg -> {
    if (errorMsg != null) {
        Snackbar.make(view, errorMsg, Snackbar.LENGTH_LONG).show();
    }
});
```

---

## Gestão do SDK

### Singleton Global

```java
public class VeiGestApp extends Application {
    private static VeiGestSDK sdk;
    
    @Override
    public void onCreate() {
        super.onCreate();
        initializeSDK();
    }
    
    private void initializeSDK() {
        VeiGestConfig config = new VeiGestConfig.Builder()
            .baseUrl("https://api.veigest.com/api/")
            .connectTimeout(30)
            .readTimeout(30)
            .writeTimeout(30)
            .debug(BuildConfig.DEBUG)
            .build();
        
        sdk = VeiGestSDK.initialize(this, config);
    }
    
    public static VeiGestSDK getSDK() {
        if (sdk == null) {
            throw new IllegalStateException("SDK não inicializado. " +
                "Certifique-se que VeiGestApp está configurado no AndroidManifest.xml");
        }
        return sdk;
    }
}
```

### Dependency Injection com Hilt

```java
@Module
@InstallIn(SingletonComponent.class)
public class VeiGestModule {
    
    @Provides
    @Singleton
    public VeiGestConfig provideConfig() {
        return new VeiGestConfig.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .debug(BuildConfig.DEBUG)
            .build();
    }
    
    @Provides
    @Singleton
    public VeiGestSDK provideSDK(@ApplicationContext Context context, VeiGestConfig config) {
        return VeiGestSDK.initialize(context, config);
    }
    
    @Provides
    public VehicleService provideVehicleService(VeiGestSDK sdk) {
        return sdk.vehicles();
    }
    
    @Provides
    public AuthService provideAuthService(VeiGestSDK sdk) {
        return sdk.auth();
    }
}

// Uso
@AndroidEntryPoint
public class VehiclesFragment extends Fragment {
    @Inject
    VehicleService vehicleService;
    
    // ...
}
```

---

## Tratamento de Erros

### Centralizador de Erros

```java
public class ErrorHandler {
    private final Context context;
    private final AuthService authService;
    
    public ErrorHandler(Context context, AuthService authService) {
        this.context = context;
        this.authService = authService;
    }
    
    public void handle(VeiGestException error, ErrorCallback callback) {
        switch (error.getErrorType()) {
            case NETWORK_ERROR:
                showNetworkError(callback);
                break;
                
            case UNAUTHORIZED:
                handleUnauthorized(callback);
                break;
                
            case FORBIDDEN:
                showForbiddenError();
                break;
                
            case NOT_FOUND:
                callback.onError("Recurso não encontrado");
                break;
                
            case VALIDATION_ERROR:
                callback.onValidationError(error.getMessage());
                break;
                
            case SERVER_ERROR:
                showServerError(callback);
                break;
                
            default:
                callback.onError(error.getMessage());
        }
    }
    
    private void showNetworkError(ErrorCallback callback) {
        new AlertDialog.Builder(context)
            .setTitle("Sem Conexão")
            .setMessage("Verifique a sua ligação à internet.")
            .setPositiveButton("Tentar novamente", (d, w) -> callback.onRetry())
            .setNegativeButton("Cancelar", null)
            .show();
    }
    
    private void handleUnauthorized(ErrorCallback callback) {
        // Tentar refresh do token
        authService.refreshToken(new VeiGestCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                callback.onRetry();
            }
            
            @Override
            public void onError(@NonNull VeiGestException e) {
                // Redirecionar para login
                Intent intent = new Intent(context, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
        });
    }
    
    public interface ErrorCallback {
        void onRetry();
        void onError(String message);
        default void onValidationError(String message) { onError(message); }
    }
}
```

### Wrapper para Callbacks

```java
public abstract class SafeCallback<T> implements VeiGestCallback<T> {
    private final ErrorHandler errorHandler;
    private final ErrorHandler.ErrorCallback errorCallback;
    
    public SafeCallback(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        this.errorCallback = new ErrorHandler.ErrorCallback() {
            @Override
            public void onRetry() {
                SafeCallback.this.retry();
            }
            
            @Override
            public void onError(String message) {
                SafeCallback.this.onHandledError(message);
            }
        };
    }
    
    @Override
    public void onError(@NonNull VeiGestException error) {
        errorHandler.handle(error, errorCallback);
    }
    
    protected void retry() {
        // Override para implementar retry
    }
    
    protected void onHandledError(String message) {
        // Override para mostrar erro na UI
    }
}

// Uso
sdk.vehicles().list(new SafeCallback<List<Vehicle>>(errorHandler) {
    @Override
    public void onSuccess(@NonNull List<Vehicle> vehicles) {
        adapter.submitList(vehicles);
    }
    
    @Override
    protected void retry() {
        loadVehicles();
    }
    
    @Override
    protected void onHandledError(String message) {
        showSnackbar(message);
    }
});
```

---

## Performance

### Cache Local

```java
public class VehicleCache {
    private final Map<Integer, Vehicle> cache = new ConcurrentHashMap<>();
    private final long CACHE_DURATION = 5 * 60 * 1000; // 5 minutos
    private long lastUpdate = 0;
    
    public Vehicle get(int id) {
        if (isExpired()) {
            return null;
        }
        return cache.get(id);
    }
    
    public void put(int id, Vehicle vehicle) {
        cache.put(id, vehicle);
        lastUpdate = System.currentTimeMillis();
    }
    
    public void clear() {
        cache.clear();
        lastUpdate = 0;
    }
    
    public boolean isExpired() {
        return System.currentTimeMillis() - lastUpdate > CACHE_DURATION;
    }
}
```

### Paginação

```java
public class PaginatedLoader<T> {
    private int currentPage = 1;
    private boolean hasMore = true;
    private boolean isLoading = false;
    
    public interface PageLoader<T> {
        void loadPage(int page, VeiGestCallback<List<T>> callback);
    }
    
    private final PageLoader<T> loader;
    private final List<T> items = new ArrayList<>();
    
    public PaginatedLoader(PageLoader<T> loader) {
        this.loader = loader;
    }
    
    public void loadMore(PaginationCallback<T> callback) {
        if (!hasMore || isLoading) return;
        
        isLoading = true;
        loader.loadPage(currentPage, new VeiGestCallback<List<T>>() {
            @Override
            public void onSuccess(@NonNull List<T> page) {
                isLoading = false;
                if (page.isEmpty()) {
                    hasMore = false;
                } else {
                    items.addAll(page);
                    currentPage++;
                }
                callback.onLoaded(new ArrayList<>(items), hasMore);
            }
            
            @Override
            public void onError(@NonNull VeiGestException error) {
                isLoading = false;
                callback.onError(error);
            }
        });
    }
    
    public void refresh(PaginationCallback<T> callback) {
        items.clear();
        currentPage = 1;
        hasMore = true;
        loadMore(callback);
    }
    
    public interface PaginationCallback<T> {
        void onLoaded(List<T> allItems, boolean hasMore);
        void onError(VeiGestException error);
    }
}

// Uso
PaginatedLoader<Vehicle> loader = new PaginatedLoader<>(
    (page, callback) -> sdk.vehicles().list(page, 20, callback)
);

// No RecyclerView.OnScrollListener
recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
    @Override
    public void onScrolled(@NonNull RecyclerView rv, int dx, int dy) {
        LinearLayoutManager lm = (LinearLayoutManager) rv.getLayoutManager();
        int totalItems = lm.getItemCount();
        int lastVisible = lm.findLastVisibleItemPosition();
        
        if (lastVisible >= totalItems - 5) {
            loader.loadMore(paginationCallback);
        }
    }
});
```

### Debounce para Pesquisa

```java
public class SearchDebouncer {
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable pendingSearch;
    private final long DEBOUNCE_DELAY = 300; // ms
    
    public void search(String query, Runnable searchAction) {
        // Cancelar pesquisa anterior
        if (pendingSearch != null) {
            handler.removeCallbacks(pendingSearch);
        }
        
        // Agendar nova pesquisa
        pendingSearch = searchAction;
        handler.postDelayed(pendingSearch, DEBOUNCE_DELAY);
    }
}

// Uso
SearchDebouncer debouncer = new SearchDebouncer();

searchEditText.addTextChangedListener(new TextWatcher() {
    @Override
    public void afterTextChanged(Editable s) {
        debouncer.search(s.toString(), () -> {
            sdk.vehicles().search(s.toString(), callback);
        });
    }
    // ...
});
```

---

## Segurança

### Validação de Input

```java
public class InputValidator {
    
    public static boolean isValidEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    
    public static boolean isValidPassword(String password) {
        // Mínimo 8 caracteres, 1 maiúscula, 1 número
        return password != null && 
               password.length() >= 8 &&
               password.matches(".*[A-Z].*") &&
               password.matches(".*[0-9].*");
    }
    
    public static boolean isValidMatricula(String matricula) {
        // Formato: XX-XX-XX ou XX-00-XX
        return matricula != null && 
               matricula.matches("^[A-Z]{2}-\\d{2}-[A-Z]{2}$|^\\d{2}-[A-Z]{2}-\\d{2}$");
    }
    
    public static boolean isValidNIF(String nif) {
        return nif != null && nif.matches("^\\d{9}$");
    }
}

// Uso
public void registerUser(String email, String password) {
    if (!InputValidator.isValidEmail(email)) {
        showError("Email inválido");
        return;
    }
    
    if (!InputValidator.isValidPassword(password)) {
        showError("Password deve ter mínimo 8 caracteres, 1 maiúscula e 1 número");
        return;
    }
    
    sdk.auth().register(email, password, callback);
}
```

### Ofuscação de Logs em Produção

```java
public class SecureLogger {
    private static final boolean DEBUG = BuildConfig.DEBUG;
    
    public static void d(String tag, String message) {
        if (DEBUG) {
            Log.d(tag, message);
        }
    }
    
    public static void e(String tag, String message, Throwable t) {
        if (DEBUG) {
            Log.e(tag, message, t);
        } else {
            // Em produção, enviar para crash reporting
            Crashlytics.recordException(t);
        }
    }
    
    // Nunca logar dados sensíveis
    public static void logApiCall(String endpoint) {
        if (DEBUG) {
            Log.d("API", "Chamada: " + endpoint);
        }
    }
}
```

---

## UI/UX

### Estados de Loading

```java
public class LoadingStateManager {
    private final View contentView;
    private final View loadingView;
    private final View errorView;
    private final View emptyView;
    
    public enum State {
        LOADING, CONTENT, ERROR, EMPTY
    }
    
    public LoadingStateManager(View content, View loading, View error, View empty) {
        this.contentView = content;
        this.loadingView = loading;
        this.errorView = error;
        this.emptyView = empty;
    }
    
    public void setState(State state) {
        contentView.setVisibility(state == State.CONTENT ? View.VISIBLE : View.GONE);
        loadingView.setVisibility(state == State.LOADING ? View.VISIBLE : View.GONE);
        errorView.setVisibility(state == State.ERROR ? View.VISIBLE : View.GONE);
        emptyView.setVisibility(state == State.EMPTY ? View.VISIBLE : View.GONE);
    }
}

// Uso
stateManager.setState(LoadingStateManager.State.LOADING);

sdk.vehicles().list(new VeiGestCallback<List<Vehicle>>() {
    @Override
    public void onSuccess(@NonNull List<Vehicle> vehicles) {
        if (vehicles.isEmpty()) {
            stateManager.setState(LoadingStateManager.State.EMPTY);
        } else {
            stateManager.setState(LoadingStateManager.State.CONTENT);
            adapter.submitList(vehicles);
        }
    }
    
    @Override
    public void onError(@NonNull VeiGestException error) {
        stateManager.setState(LoadingStateManager.State.ERROR);
    }
});
```

### Pull to Refresh

```java
swipeRefreshLayout.setOnRefreshListener(() -> {
    sdk.vehicles().list(new VeiGestCallback<List<Vehicle>>() {
        @Override
        public void onSuccess(@NonNull List<Vehicle> vehicles) {
            swipeRefreshLayout.setRefreshing(false);
            adapter.submitList(vehicles);
        }
        
        @Override
        public void onError(@NonNull VeiGestException error) {
            swipeRefreshLayout.setRefreshing(false);
            showError(error);
        }
    });
});
```

---

## Testes

### Mock do SDK

```java
public class MockVeiGestSDK extends VeiGestSDK {
    private final MockVehicleService vehicleService = new MockVehicleService();
    
    @Override
    public VehicleService vehicles() {
        return vehicleService;
    }
}

public class MockVehicleService extends VehicleService {
    private List<Vehicle> mockVehicles = Arrays.asList(
        createMockVehicle(1, "AA-00-BB"),
        createMockVehicle(2, "CC-11-DD")
    );
    
    @Override
    public void list(VeiGestCallback<List<Vehicle>> callback) {
        // Simular delay de rede
        new Handler().postDelayed(() -> {
            callback.onSuccess(mockVehicles);
        }, 500);
    }
    
    private Vehicle createMockVehicle(int id, String matricula) {
        Vehicle v = new Vehicle();
        v.setId(id);
        v.setMatricula(matricula);
        return v;
    }
}
```

### Testes com Espresso

```java
@RunWith(AndroidJUnit4.class)
public class VehicleListTest {
    
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = 
        new ActivityScenarioRule<>(MainActivity.class);
    
    @Before
    public void setup() {
        // Injetar mock SDK
        VeiGestApp.setSDK(new MockVeiGestSDK());
    }
    
    @Test
    public void testVehicleListDisplayed() {
        // Navegar para lista de veículos
        onView(withId(R.id.nav_vehicles)).perform(click());
        
        // Verificar que lista está visível
        onView(withId(R.id.recycler_vehicles)).check(matches(isDisplayed()));
        
        // Verificar que tem items
        onView(withId(R.id.recycler_vehicles))
            .check(matches(hasMinimumChildCount(1)));
    }
}
```

---

## Checklist de Implementação

### Antes de Produção

- [ ] Configurar URL da API de produção
- [ ] Desativar modo debug
- [ ] Implementar tratamento de erros robusto
- [ ] Testar refresh de token
- [ ] Testar comportamento offline
- [ ] Implementar retry em erros de rede
- [ ] Validar todos os inputs do utilizador
- [ ] Configurar ProGuard rules para o SDK
- [ ] Testar em diferentes versões Android

### ProGuard Rules

```proguard
# VeiGest SDK
-keep class com.veigest.sdk.** { *; }
-keep class com.veigest.sdk.models.** { *; }

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }
-keep class okio.** { *; }
```

---

## Próximos Passos

- [Troubleshooting](TROUBLESHOOTING.md)
- [Callbacks](CALLBACKS.md)
- [Tratamento de Erros](EXCEPTIONS.md)
