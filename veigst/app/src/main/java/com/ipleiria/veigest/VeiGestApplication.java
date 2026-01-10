package com.ipleiria.veigest;

import android.app.Application;
import android.util.Log;

import com.veigest.sdk.SingletonVeiGest;

/**
 * Classe Application para inicialização do VeiGest SDK.
 * 
 * Esta classe é responsável por:
 * - Inicializar o SDK Singleton na criação da aplicação
 * - Configurar a URL base da API
 * - Disponibilizar acesso global ao Singleton
 * 
 * Utiliza o padrão Singleton com Volley (baseconteudo).
 */
public class VeiGestApplication extends Application {
    
    private static final String TAG = "VeiGestApp";
    
    // URL base da API VeiGest
    private static final String API_BASE_URL = "https://veigestback.dryadlang.org/api/v1";
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        Log.d(TAG, "Inicializando VeiGest Application...");
        
        // Inicializar o SDK Singleton
        initializeSDK();
        
        Log.d(TAG, "VeiGest Application inicializada com sucesso!");
    }
    
    /**
     * Inicializa o VeiGest SDK com as configurações adequadas.
     * Utiliza o padrão Singleton com Volley.
     */
    private void initializeSDK() {
        // Obter instância do Singleton e configurar URL base
        SingletonVeiGest singleton = SingletonVeiGest.getInstance(this);
        singleton.setBaseUrl(API_BASE_URL);
        
        Log.d(TAG, "VeiGest SDK (Singleton) inicializado");
        Log.d(TAG, "API URL: " + API_BASE_URL);
        Log.d(TAG, "Debug mode: " + BuildConfig.DEBUG);
    }
    
    /**
     * Obtém a instância do SingletonVeiGest.
     * 
     * @return Instância do SingletonVeiGest
     */
    public SingletonVeiGest getVeiGestSingleton() {
        return SingletonVeiGest.getInstance(this);
    }
    
    /**
     * Verifica se o utilizador está autenticado.
     * 
     * @return true se há token de autenticação válido
     */
    public boolean isAuthenticated() {
        return SingletonVeiGest.getInstance(this).isAuthenticated();
    }
}
