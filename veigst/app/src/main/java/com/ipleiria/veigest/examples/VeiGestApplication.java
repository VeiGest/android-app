package com.ipleiria.veigest.examples;

import android.app.Application;
import android.util.Log;

import com.veigest.sdk.VeiGestConfig;
import com.veigest.sdk.VeiGestSDK;

/**
 * Exemplo de Application que inicializa o VeiGest SDK.
 * 
 * Para usar este exemplo:
 * 1. Crie uma classe Application no seu projeto (ou use uma existente)
 * 2. Adicione ao AndroidManifest.xml:
 *    <application android:name=".VeiGestApplication" ...>
 * 3. Copie o código de inicialização do SDK
 */
public class VeiGestApplication extends Application {

    private static final String TAG = "VeiGestApp";
    
    // URL da API VeiGest
    private static final String API_BASE_URL = "https://veigestback.dryadlang.org";

    @Override
    public void onCreate() {
        super.onCreate();

        // Inicializar o VeiGest SDK
        initializeSDK();
    }

    /**
     * Inicializa o VeiGest SDK com as configurações necessárias.
     */
    private void initializeSDK() {
        try {
            // Configurar o SDK
            VeiGestConfig config = new VeiGestConfig.Builder()
                    .setBaseUrl(API_BASE_URL)
                    .setDebugMode(true)  // Ativar logs em debug - desativar em produção!
                    .setConnectTimeout(30)  // Timeout de conexão em segundos
                    .setReadTimeout(30)     // Timeout de leitura em segundos
                    .setWriteTimeout(30)    // Timeout de escrita em segundos
                    .setRetryOnConnectionFailure(true)  // Tentar reconectar automaticamente
                    .build();

            // Inicializar o SDK
            VeiGestSDK.init(this, config);

            Log.d(TAG, "VeiGest SDK inicializado com sucesso!");
            Log.d(TAG, "API URL: " + API_BASE_URL);

        } catch (Exception e) {
            Log.e(TAG, "Erro ao inicializar VeiGest SDK", e);
        }
    }

    /**
     * Exemplo de como verificar se o utilizador está autenticado.
     */
    public static boolean isUserAuthenticated() {
        return VeiGestSDK.isInitialized() && VeiGestSDK.getInstance().isAuthenticated();
    }

    /**
     * Exemplo de como fazer logout global.
     */
    public static void logout() {
        if (VeiGestSDK.isInitialized()) {
            VeiGestSDK.getInstance().auth().logout();
        }
    }
}
