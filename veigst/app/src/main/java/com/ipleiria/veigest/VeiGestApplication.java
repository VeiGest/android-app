package com.ipleiria.veigest;

import android.app.Application;
import android.util.Log;

import com.veigest.sdk.VeiGestConfig;
import com.veigest.sdk.VeiGestSDK;

/**
 * Classe Application para inicialização do VeiGest SDK.
 * 
 * Esta classe é responsável por:
 * - Inicializar o SDK na criação da aplicação
 * - Configurar parâmetros de conexão com a API
 * - Disponibilizar acesso global ao SDK
 */
public class VeiGestApplication extends Application {
    
    private static final String TAG = "VeiGestApp";
    
    // URL base da API VeiGest
    private static final String API_BASE_URL = "https://veigestback.dryadlang.org/";
    
    // Instância do SDK (acessível globalmente)
    private static VeiGestSDK sdk;
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        Log.d(TAG, "Inicializando VeiGest Application...");
        
        // Inicializar o SDK
        initializeSDK();
        
        Log.d(TAG, "VeiGest Application inicializada com sucesso!");
    }
    
    /**
     * Inicializa o VeiGest SDK com as configurações adequadas.
     */
    private void initializeSDK() {
        // Configurar o SDK
        VeiGestConfig config = new VeiGestConfig.Builder()
                .baseUrl(API_BASE_URL)
                .connectTimeout(30)     // Timeout de conexão: 30 segundos
                .readTimeout(30)        // Timeout de leitura: 30 segundos
                .writeTimeout(30)       // Timeout de escrita: 30 segundos
                .debug(BuildConfig.DEBUG) // Ativar logs apenas em debug
                .build();
        
        // Inicializar o SDK
        sdk = VeiGestSDK.initialize(this, config);
        
        Log.d(TAG, "VeiGest SDK inicializado");
        Log.d(TAG, "API URL: " + API_BASE_URL);
        Log.d(TAG, "Debug mode: " + BuildConfig.DEBUG);
    }
    
    /**
     * Obtém a instância do SDK.
     * 
     * @return Instância do VeiGestSDK
     * @throws IllegalStateException se o SDK não foi inicializado
     */
    public static VeiGestSDK getSDK() {
        if (sdk == null) {
            throw new IllegalStateException(
                "VeiGest SDK não inicializado. " +
                "Certifique-se que VeiGestApplication está configurado no AndroidManifest.xml"
            );
        }
        return sdk;
    }
    
    /**
     * Verifica se o SDK está inicializado.
     * 
     * @return true se o SDK está pronto para uso
     */
    public static boolean isSDKInitialized() {
        return sdk != null;
    }
    
    /**
     * Verifica se o utilizador está autenticado.
     * 
     * @return true se existe uma sessão ativa
     */
    public static boolean isAuthenticated() {
        return sdk != null && sdk.auth().isAuthenticated();
    }
}
