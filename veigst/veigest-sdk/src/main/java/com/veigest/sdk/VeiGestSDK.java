package com.veigest.sdk;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.veigest.sdk.api.VeiGestApiClient;
import com.veigest.sdk.auth.AuthManager;
import com.veigest.sdk.services.*;

/**
 * VeiGest SDK - Ponto de entrada principal para interagir com a API VeiGest.
 * 
 * <p>Exemplo de uso:</p>
 * <pre>
 * // Inicializar SDK
 * VeiGestSDK sdk = VeiGestSDK.init(context, new VeiGestConfig.Builder()
 *     .setBaseUrl("https://veigestback.dryadlang.org")
 *     .setDebugMode(true)
 *     .build());
 * 
 * // Login
 * sdk.auth().login("user@email.com", "password", new VeiGestCallback<>() {
 *     @Override
 *     public void onSuccess(LoginResponse response) {
 *         // Sucesso
 *     }
 *     
 *     @Override
 *     public void onError(VeiGestException error) {
 *         // Erro
 *     }
 * });
 * 
 * // Listar veículos
 * sdk.vehicles().list(new VeiGestCallback<>() {
 *     @Override
 *     public void onSuccess(List<Vehicle> vehicles) {
 *         // Processar veículos
 *     }
 * });
 * </pre>
 * 
 * @author VeiGest Team
 * @version 1.0.0
 */
public final class VeiGestSDK {
    
    private static VeiGestSDK instance;
    
    private final Context context;
    private final VeiGestConfig config;
    private final VeiGestApiClient apiClient;
    private final AuthManager authManager;
    
    // Serviços
    private AuthService authService;
    private VehicleService vehicleService;
    private UserService userService;
    private MaintenanceService maintenanceService;
    private FuelLogService fuelLogService;
    private RouteService routeService;
    private DocumentService documentService;
    private AlertService alertService;
    private ReportService reportService;
    private CompanyService companyService;
    private TicketService ticketService;
    private FileService fileService;
    
    /**
     * Construtor privado - usar init() para instanciar.
     */
    private VeiGestSDK(@NonNull Context context, @NonNull VeiGestConfig config) {
        this.context = context.getApplicationContext();
        this.config = config;
        this.authManager = new AuthManager(this.context);
        this.apiClient = new VeiGestApiClient(config, authManager);
    }
    
    /**
     * Inicializa o SDK com as configurações fornecidas.
     * 
     * @param context Contexto da aplicação
     * @param config Configurações do SDK
     * @return Instância do SDK
     */
    @NonNull
    public static synchronized VeiGestSDK init(@NonNull Context context, @NonNull VeiGestConfig config) {
        if (instance == null) {
            instance = new VeiGestSDK(context, config);
        }
        return instance;
    }
    
    /**
     * Obtém a instância do SDK.
     * Deve ser chamado após init().
     * 
     * @return Instância do SDK
     * @throws IllegalStateException se o SDK não foi inicializado
     */
    @NonNull
    public static synchronized VeiGestSDK getInstance() {
        if (instance == null) {
            throw new IllegalStateException("VeiGestSDK não foi inicializado. Chame VeiGestSDK.init() primeiro.");
        }
        return instance;
    }
    
    /**
     * Verifica se o SDK foi inicializado.
     * 
     * @return true se inicializado, false caso contrário
     */
    public static boolean isInitialized() {
        return instance != null;
    }
    
    /**
     * Limpa a instância do SDK (útil para testes ou logout completo).
     */
    public static synchronized void destroy() {
        if (instance != null) {
            instance.authManager.clearTokens();
            instance = null;
        }
    }
    
    // ==================== SERVIÇOS ====================
    
    /**
     * Serviço de autenticação.
     * Login, logout, refresh token, dados do usuário.
     */
    @NonNull
    public AuthService auth() {
        if (authService == null) {
            authService = new AuthService(apiClient, authManager);
        }
        return authService;
    }
    
    /**
     * Serviço de veículos.
     * CRUD de veículos, atribuição de condutores.
     */
    @NonNull
    public VehicleService vehicles() {
        if (vehicleService == null) {
            vehicleService = new VehicleService(apiClient);
        }
        return vehicleService;
    }
    
    /**
     * Serviço de utilizadores.
     * CRUD de utilizadores, reset de password.
     */
    @NonNull
    public UserService users() {
        if (userService == null) {
            userService = new UserService(apiClient);
        }
        return userService;
    }
    
    /**
     * Serviço de manutenções.
     * CRUD de registos de manutenção.
     */
    @NonNull
    public MaintenanceService maintenances() {
        if (maintenanceService == null) {
            maintenanceService = new MaintenanceService(apiClient);
        }
        return maintenanceService;
    }
    
    /**
     * Serviço de abastecimentos.
     * CRUD de registos de combustível.
     */
    @NonNull
    public FuelLogService fuelLogs() {
        if (fuelLogService == null) {
            fuelLogService = new FuelLogService(apiClient);
        }
        return fuelLogService;
    }
    
    /**
     * Serviço de rotas.
     * CRUD de rotas, pontos GPS.
     */
    @NonNull
    public RouteService routes() {
        if (routeService == null) {
            routeService = new RouteService(apiClient);
        }
        return routeService;
    }
    
    /**
     * Serviço de documentos.
     * CRUD de documentos, documentos a expirar.
     */
    @NonNull
    public DocumentService documents() {
        if (documentService == null) {
            documentService = new DocumentService(apiClient);
        }
        return documentService;
    }
    
    /**
     * Serviço de alertas.
     * CRUD de alertas, resolver/ignorar alertas.
     */
    @NonNull
    public AlertService alerts() {
        if (alertService == null) {
            alertService = new AlertService(apiClient);
        }
        return alertService;
    }
    
    /**
     * Serviço de relatórios.
     * Estatísticas, custos, consumo, performance.
     */
    @NonNull
    public ReportService reports() {
        if (reportService == null) {
            reportService = new ReportService(apiClient);
        }
        return reportService;
    }
    
    /**
     * Serviço de empresas.
     * CRUD de empresas.
     */
    @NonNull
    public CompanyService companies() {
        if (companyService == null) {
            companyService = new CompanyService(apiClient);
        }
        return companyService;
    }
    
    /**
     * Serviço de tickets/bilhetes.
     * CRUD de tickets, cancelar e completar tickets.
     */
    @NonNull
    public TicketService tickets() {
        if (ticketService == null) {
            ticketService = new TicketService(apiClient);
        }
        return ticketService;
    }
    
    /**
     * Serviço de ficheiros.
     * Upload, download e listagem de ficheiros.
     */
    @NonNull
    public FileService files() {
        if (fileService == null) {
            fileService = new FileService(apiClient);
        }
        return fileService;
    }
    
    // ==================== UTILITÁRIOS ====================
    
    /**
     * Verifica se o utilizador está autenticado.
     * 
     * @return true se autenticado, false caso contrário
     */
    public boolean isAuthenticated() {
        return authManager.isAuthenticated();
    }
    
    /**
     * Obtém o token de acesso atual.
     * 
     * @return Token de acesso ou null se não autenticado
     */
    @Nullable
    public String getAccessToken() {
        return authManager.getAccessToken();
    }
    
    /**
     * Obtém as configurações atuais do SDK.
     * 
     * @return Configurações do SDK
     */
    @NonNull
    public VeiGestConfig getConfig() {
        return config;
    }
    
    /**
     * Obtém o contexto da aplicação.
     * 
     * @return Contexto da aplicação
     */
    @NonNull
    public Context getContext() {
        return context;
    }
}
