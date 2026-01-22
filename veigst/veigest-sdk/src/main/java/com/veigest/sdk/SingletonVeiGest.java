package com.veigest.sdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
// Para acessar a configuração centralizada
import com.veigest.sdk.config.ApiConfig;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.veigest.sdk.database.VeiGestBDHelper;
import com.veigest.sdk.listeners.*;
import com.veigest.sdk.listeners.RegisterListener;
import com.veigest.sdk.models.*;
import com.veigest.sdk.utils.VeiGestJsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Singleton principal do SDK VeiGest.
 * 
 * Gerencia:
 * - RequestQueue do Volley para requisições HTTP
 * - Base de dados SQLite local
 * - Token de autenticação
 * - Listeners para notificação de atualizações
 * 
 * Exemplo de uso:
 * 
 * <pre>
 * // Inicializar (uma vez, no Application)
 * SingletonVeiGest.getInstance(context).setBaseUrl("http://api.exemplo.com/api/v1");
 * 
 * // Login
 * SingletonVeiGest.getInstance(context).loginAPI("user@email.com", "password");
 * 
 * // Listar veículos
 * SingletonVeiGest.getInstance(context).getAllVeiculosAPI();
 * </pre>
 */
public class SingletonVeiGest {

    private static final String TAG = "SingletonVeiGest";

    // Instância única
    private static SingletonVeiGest INSTANCE = null;

    // Volley RequestQueue
    private static RequestQueue volleyQueue = null;

    // Base de dados
    private VeiGestBDHelper veiGestBD = null;

    // SharedPreferences para token e configurações
    private static final String PREFS_NAME = "veigest_prefs";
    private static final String PREF_TOKEN = "auth_token";
    private static final String PREF_API_URL = "api_url";
    private static final String PREF_USER_ID = "user_id";
    private static final String PREF_COMPANY_ID = "company_id";
    private SharedPreferences prefs;

    // URL base da API (configurável)
    private String baseUrl = ApiConfig.API_BASE_URL;

    // Endpoints da API
    private String mUrlAPILogin;
    private String mUrlAPIRegister;
    private String mUrlAPIVehicles;
    private String mUrlAPIMaintenances;
    private String mUrlAPIFuelLogs;
    private String mUrlAPIAlerts;
    private String mUrlAPIDocuments;
    private String mUrlAPIRoutes;
    private String mUrlAPIUsers;

    // Dados em memória
    private ArrayList<Vehicle> veiculos;
    private ArrayList<Maintenance> manutencoes;
    private ArrayList<FuelLog> abastecimentos;
    private ArrayList<Alert> alertas;
    private ArrayList<Document> documentos;
    private ArrayList<Route> rotas;
    private User utilizadorAtual;

    // Listeners
    private LoginListener loginListener;
    private RegisterListener registerListener;
    private VeiculosListener veiculosListener;
    private VeiculoListener veiculoListener;
    private ManutencoesListener manutencoesListener;
    private AbastecimentosListener abastecimentosListener;
    private AlertasListener alertasListener;
    private DocumentosListener documentosListener;
    private RotasListener rotasListener;
    private ProfileListener profileListener;

    /**
     * Construtor privado - usar getInstance()
     */
    private SingletonVeiGest(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        String savedApiUrl = prefs.getString("api_url", ApiConfig.API_BASE_URL);
        if (savedApiUrl != null && !savedApiUrl.isEmpty()) {
            this.baseUrl = savedApiUrl;
        } else {
            this.baseUrl = ApiConfig.API_BASE_URL;
        }

        veiculos = new ArrayList<>();
        manutencoes = new ArrayList<>();
        abastecimentos = new ArrayList<>();
        alertas = new ArrayList<>();
        documentos = new ArrayList<>();
        rotas = new ArrayList<>();
        atualizarEndpoints();
    }

    /**
     * Obtém a instância única do Singleton.
     */
    public static synchronized SingletonVeiGest getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SingletonVeiGest(context.getApplicationContext());
            volleyQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return INSTANCE;
    }

    /**
     * Verifica se o SDK foi inicializado.
     */
    public static boolean isInitialized() {
        return INSTANCE != null;
    }

    // ==================== CONFIGURAÇÃO ====================

    /**
     * Define a URL base da API.
     */
    public void setBaseUrl(String baseUrl) {
        if (baseUrl != null && !baseUrl.isEmpty()) {
            // Remove barra final se houver
            this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
            atualizarEndpoints();
        }
    }

    /**
     * Obtém a URL base da API.
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    private void atualizarEndpoints() {
        // Endpoints de autenticação
        mUrlAPILogin = baseUrl + "/auth/login";
        mUrlAPIRegister = baseUrl + "/auth/register";

        // Endpoints principais (sem 's' no plural - conforme API atualizada)
        mUrlAPIVehicles = baseUrl + "/vehicle";
        mUrlAPIMaintenances = baseUrl + "/maintenance";
        mUrlAPIFuelLogs = baseUrl + "/fuel-log";
        mUrlAPIAlerts = baseUrl + "/alert";
        mUrlAPIDocuments = baseUrl + "/document";
        mUrlAPIRoutes = baseUrl + "/route";
        mUrlAPIUsers = baseUrl + "/user";
    }

    // ==================== INICIALIZAÇÃO DA BD ====================

    /**
     * Inicializa a base de dados SQLite.
     * Deve ser chamado antes de usar funcionalidades de persistência local.
     */
    public void iniciarBD(Context context) {
        if (veiGestBD == null) {
            veiGestBD = new VeiGestBDHelper(context);
        }
    }

    /**
     * Obtém o helper da base de dados.
     */
    public VeiGestBDHelper getBD() {
        return veiGestBD;
    }

    // ==================== TOKEN E AUTENTICAÇÃO ====================

    /**
     * Salva o token de autenticação.
     */
    public void saveToken(String token) {
        prefs.edit().putString(PREF_TOKEN, token).apply();
    }

    /**
     * Obtém o token de autenticação.
     */
    public String getToken() {
        return prefs.getString(PREF_TOKEN, null);
    }

    /**
     * Verifica se há um token válido.
     */
    public boolean isAuthenticated() {
        return getToken() != null && !getToken().isEmpty();
    }

    /**
     * Limpa o token e dados do utilizador.
     */
    public void clearAuth() {
        prefs.edit()
                .remove(PREF_TOKEN)
                .remove(PREF_USER_ID)
                .remove(PREF_COMPANY_ID)
                .apply();
        utilizadorAtual = null;
    }

    /**
     * Salva informações do utilizador.
     */
    public void saveUserInfo(int userId, int companyId) {
        prefs.edit()
                .putInt(PREF_USER_ID, userId)
                .putInt(PREF_COMPANY_ID, companyId)
                .apply();
    }

    public int getUserId() {
        return prefs.getInt(PREF_USER_ID, 0);
    }

    public int getCompanyId() {
        return prefs.getInt(PREF_COMPANY_ID, 0);
    }

    /**
     * Verifica se o utilizador logado tem papel de admin.
     */
    public boolean isAdmin() {
        User user = getUtilizadorAtual();
        return user != null && user.getRole() != null && user.getRole().trim().equalsIgnoreCase("admin");
    }

    /**
     * Verifica se o utilizador logado tem papel de manager.
     */
    public boolean isManager() {
        User user = getUtilizadorAtual();
        return user != null && user.getRole() != null && user.getRole().trim().equalsIgnoreCase("manager");
    }

    /**
     * Verifica se o utilizador logado tem papel de condutor (driver).
     */
    public boolean isDriver() {
        User user = getUtilizadorAtual();
        return user != null && user.getRole() != null && user.getRole().trim().equalsIgnoreCase("driver");
    }

    /**
     * Helper para verificar se o utilizador é gestor ou admin.
     */
    public boolean isManagerOrAdmin() {
        return isManager() || isAdmin();
    }

    // ==================== SETTERS DE LISTENERS ====================

    public void setLoginListener(LoginListener listener) {
        this.loginListener = listener;
    }

    public void setRegisterListener(RegisterListener listener) {
        this.registerListener = listener;
    }

    public void setVeiculosListener(VeiculosListener listener) {
        this.veiculosListener = listener;
    }

    public void setVeiculoListener(VeiculoListener listener) {
        this.veiculoListener = listener;
    }

    public void setManutencoesListener(ManutencoesListener listener) {
        this.manutencoesListener = listener;
    }

    public void setAbastecimentosListener(AbastecimentosListener listener) {
        this.abastecimentosListener = listener;
    }

    public void setAlertasListener(AlertasListener listener) {
        this.alertasListener = listener;
    }

    public void setDocumentosListener(DocumentosListener listener) {
        this.documentosListener = listener;
    }

    public void setRotasListener(RotasListener listener) {
        this.rotasListener = listener;
    }

    public void setProfileListener(ProfileListener listener) {
        this.profileListener = listener;
    }

    // ==================== GETTERS DE DADOS ====================

    public ArrayList<Vehicle> getVeiculos() {
        return new ArrayList<>(veiculos);
    }

    public Vehicle getVeiculo(int id) {
        for (Vehicle v : veiculos) {
            if (v.getId() == id)
                return v;
        }
        return null;
    }

    public ArrayList<Maintenance> getManutencoes() {
        return new ArrayList<>(manutencoes);
    }

    public ArrayList<FuelLog> getAbastecimentos() {
        return new ArrayList<>(abastecimentos);
    }

    public ArrayList<Alert> getAlertas() {
        return new ArrayList<>(alertas);
    }

    public ArrayList<Document> getDocumentos() {
        return new ArrayList<>(documentos);
    }

    public ArrayList<Route> getRotas() {
        return new ArrayList<>(rotas);
    }

    public User getUtilizadorAtual() {
        return utilizadorAtual;
    }

    // ==================== LOGIN API ====================

    /**
     * Realiza login na API.
     * Notifica através do LoginListener.
     */
    public void loginAPI(final String username, final String password) {
        JSONObject body = new JSONObject();
        try {
            body.put("username", username);
            body.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            if (loginListener != null) {
                loginListener.onLoginError("Erro ao criar requisição");
            }
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                mUrlAPILogin,
                body,
                response -> {
                    try {
                        Log.d(TAG, "Login response: " + response.toString());

                        Object[] result = VeiGestJsonParser.parserJsonLogin(response);
                        if (result != null && result[0] != null) {
                            String token = (String) result[0];
                            User user = (User) result[1];

                            // Salva token e info do utilizador
                            saveToken(token);
                            if (user != null) {
                                utilizadorAtual = user;
                                saveUserInfo(user.getId(), user.getCompanyId());

                                // Persiste na BD local
                                if (veiGestBD != null) {
                                    veiGestBD.adicionarUserBD(user);
                                }
                            }

                            if (loginListener != null) {
                                loginListener.onValidateLogin(token, user);
                            }
                        } else {
                            if (loginListener != null) {
                                loginListener.onLoginError("Credenciais inválidas");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (loginListener != null) {
                            loginListener.onLoginError("Erro ao processar resposta");
                        }
                    }
                },
                error -> {
                    Log.e(TAG, "Login error: " + error.toString());
                    String errorMsg = "Erro de conexão";
                    if (error.networkResponse != null) {
                        try {
                            String responseBody = new String(error.networkResponse.data, "UTF-8");
                            JSONObject errorJson = new JSONObject(responseBody);
                            errorMsg = VeiGestJsonParser.parserJsonError(errorJson);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (loginListener != null) {
                        loginListener.onLoginError(errorMsg);
                    }
                });

        volleyQueue.add(request);
    }

    /**
     * Realiza registro de novo utilizador na API com todos os campos.
     * 
     * @param username Nome de utilizador
     * @param email    Email do utilizador
     * @param password Password
     * @param name     Nome completo
     * @param phone    Telefone (pode ser null)
     */
    public void registerAPI(final String username, final String email, final String password,
            final String name, final String phone) {
        JSONObject body = new JSONObject();
        try {
            body.put("username", username);
            body.put("email", email);
            body.put("password", password);
            body.put("role", "driver");
        } catch (JSONException e) {
            e.printStackTrace();
            if (registerListener != null) {
                registerListener.onRegisterError("Erro ao criar requisição");
            }
            return;
        }
        Log.d(TAG, "Tentando registro: " + username + ", " + email);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                mUrlAPIRegister,
                body,
                response -> {
                    try {
                        Log.d(TAG, "Register response: " + response.toString());

                        // Parse da resposta - pode retornar o utilizador criado
                        User user = VeiGestJsonParser.parserJsonUser(response);

                        // Persiste na BD local se tiver dados
                        if (user != null && veiGestBD != null) {
                            veiGestBD.adicionarUserBD(user);
                        }

                        if (registerListener != null) {
                            registerListener.onRegisterSuccess(user);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (registerListener != null) {
                            registerListener.onRegisterError("Erro ao processar resposta");
                        }
                    }
                },
                error -> {
                    Log.e(TAG, "Register error: " + error.toString());
                    String errorMsg = "Erro de conexão";

                    if (error.networkResponse != null) {
                        int statusCode = error.networkResponse.statusCode;
                        try {
                            String responseBody = new String(error.networkResponse.data, "UTF-8");
                            Log.e(TAG, "Register error response: " + responseBody);
                            JSONObject errorJson = new JSONObject(responseBody);
                            errorMsg = VeiGestJsonParser.parserJsonError(errorJson);

                            // Tratamento de erros específicos de registro
                            if (statusCode == 422) {
                                // Validação falhou (email/username já existe, etc)
                                if (responseBody.contains("email")) {
                                    errorMsg = "Email já registrado";
                                } else if (responseBody.contains("username")) {
                                    errorMsg = "Nome de utilizador já existe";
                                }
                            } else if (statusCode == 401 || statusCode == 403) {
                                errorMsg = "Sem permissão para criar utilizador";
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (registerListener != null) {
                        registerListener.onRegisterError(errorMsg);
                    }
                });

        volleyQueue.add(request);
    }

    // ==================== LOGOUT ====================

    /**
     * Realiza logout.
     */
    public void logout() {
        clearAuth();

        // Limpa dados em memória
        veiculos.clear();
        manutencoes.clear();
        abastecimentos.clear();
        alertas.clear();
        documentos.clear();
        rotas.clear();

        // Limpa BD local
        if (veiGestBD != null) {
            veiGestBD.limparTudo();
        }
    }

    // ==================== VEÍCULOS API ====================

    /**
     * Obtém todos os veículos da API.
     */
    public void getAllVeiculosAPI() {
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                mUrlAPIVehicles,
                null,
                response -> {
                    try {
                        ArrayList<Vehicle> lista = VeiGestJsonParser.parserJsonVehicles(response);
                        adicionarVeiculosBD(lista);
                        if (veiculosListener != null) {
                            veiculosListener.onRefreshListaVeiculos(veiculos);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e(TAG, "Erro ao obter veículos: " + error.toString());
                    // Tenta carregar da BD local
                    if (veiGestBD != null) {
                        veiculos = veiGestBD.getAllVehiclesBD();
                        if (veiculosListener != null) {
                            veiculosListener.onRefreshListaVeiculos(veiculos);
                        }
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getAuthHeaders();
            }

        };
        volleyQueue.add(request);
    }

    /**
     * Adiciona um veículo via API.
     */
    public void adicionarVeiculoAPI(final Vehicle vehicle) {
        JSONObject body = vehicleToJson(vehicle);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                mUrlAPIVehicles,
                body,
                response -> {
                    try {
                        Vehicle novoVeiculo = VeiGestJsonParser.parserJsonVehicle(response);
                        if (novoVeiculo != null) {
                            veiculos.add(novoVeiculo);
                            if (veiGestBD != null) {
                                veiGestBD.adicionarVehicleBD(novoVeiculo);
                            }
                        }
                        if (veiculoListener != null) {
                            veiculoListener.onRefreshDetalhes(VeiculoListener.OPERACAO_ADICIONAR);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e(TAG, "Erro ao adicionar veículo: " + error.toString());
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getAuthHeaders();
            }

        };

        volleyQueue.add(request);
    }

    /**
     * Edita um veículo via API.
     */
    public void editarVeiculoAPI(final Vehicle vehicle) {
        JSONObject body = vehicleToJson(vehicle);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                mUrlAPIVehicles + "/" + vehicle.getId(),
                body,
                response -> {
                    try {
                        Vehicle veiculoAtualizado = VeiGestJsonParser.parserJsonVehicle(response);
                        if (veiculoAtualizado != null) {
                            // Atualiza na lista em memória
                            for (int i = 0; i < veiculos.size(); i++) {
                                if (veiculos.get(i).getId() == veiculoAtualizado.getId()) {
                                    veiculos.set(i, veiculoAtualizado);
                                    break;
                                }
                            }
                            if (veiGestBD != null) {
                                veiGestBD.editarVehicleBD(veiculoAtualizado);
                            }
                        }
                        if (veiculoListener != null) {
                            veiculoListener.onRefreshDetalhes(VeiculoListener.OPERACAO_EDITAR);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e(TAG, "Erro ao editar veículo: " + error.toString());
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getAuthHeaders();
            }

        };

        volleyQueue.add(request);
    }

    /**
     * Remove um veículo via API.
     */
    public void removerVeiculoAPI(final int vehicleId) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                mUrlAPIVehicles + "/" + vehicleId,
                null,
                response -> {
                    // Remove da lista em memória
                    for (int i = 0; i < veiculos.size(); i++) {
                        if (veiculos.get(i).getId() == vehicleId) {
                            veiculos.remove(i);
                            break;
                        }
                    }
                    if (veiGestBD != null) {
                        veiGestBD.removerVehicleBD(vehicleId);
                    }
                    if (veiculoListener != null) {
                        veiculoListener.onRefreshDetalhes(VeiculoListener.OPERACAO_REMOVER);
                    }
                },
                error -> {
                    Log.e(TAG, "Erro ao remover veículo: " + error.toString());
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getAuthHeaders();
            }

        };

        volleyQueue.add(request);
    }

    private void adicionarVeiculosBD(ArrayList<Vehicle> lista) {
        veiculos.clear();
        veiculos.addAll(lista);

        if (veiGestBD != null) {
            veiGestBD.removerAllVehiclesBD();
            for (Vehicle v : lista) {
                veiGestBD.adicionarVehicleBD(v);
            }
        }
    }

    // ==================== MANUTENÇÕES API ====================

    /**
     * Obtém todas as manutenções da API.
     */
    public void getAllManutencoesAPI() {
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                mUrlAPIMaintenances,
                null,
                response -> {
                    try {
                        ArrayList<Maintenance> lista = VeiGestJsonParser.parserJsonMaintenances(response);
                        adicionarManutencoesBD(lista);
                        if (manutencoesListener != null) {
                            manutencoesListener.onRefreshListaManutencoes(manutencoes);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e(TAG, "Erro ao obter manutenções: " + error.toString());
                    if (veiGestBD != null) {
                        manutencoes = veiGestBD.getAllMaintenancesBD();
                        if (manutencoesListener != null) {
                            manutencoesListener.onRefreshListaManutencoes(manutencoes);
                        }
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getAuthHeaders();
            }

        };
        volleyQueue.add(request);
    }

    /**
     * Adiciona uma manutenção via API.
     */
    public void adicionarManutencaoAPI(final Maintenance maintenance) {
        JSONObject body = maintenanceToJson(maintenance);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                mUrlAPIMaintenances,
                body,
                response -> {
                    try {
                        Maintenance novaManutencao = VeiGestJsonParser.parserJsonMaintenance(response);
                        if (novaManutencao != null) {
                            manutencoes.add(novaManutencao);
                            if (veiGestBD != null) {
                                veiGestBD.adicionarMaintenanceBD(novaManutencao);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e(TAG, "Erro ao adicionar manutenção: " + error.toString());
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getAuthHeaders();
            }

        };

        volleyQueue.add(request);
    }

    private void adicionarManutencoesBD(ArrayList<Maintenance> lista) {
        manutencoes.clear();
        manutencoes.addAll(lista);

        if (veiGestBD != null) {
            veiGestBD.removerAllMaintenancesBD();
            for (Maintenance m : lista) {
                veiGestBD.adicionarMaintenanceBD(m);
            }
        }
    }

    // ==================== ABASTECIMENTOS API ====================

    /**
     * Obtém todos os abastecimentos da API.
     */
    public void getAllAbastecimentosAPI() {
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                mUrlAPIFuelLogs,
                null,
                response -> {
                    try {
                        ArrayList<FuelLog> lista = VeiGestJsonParser.parserJsonFuelLogs(response);
                        adicionarAbastecimentosBD(lista);
                        if (abastecimentosListener != null) {
                            abastecimentosListener.onRefreshListaAbastecimentos(abastecimentos);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e(TAG, "Erro ao obter abastecimentos: " + error.toString());
                    if (veiGestBD != null) {
                        abastecimentos = veiGestBD.getAllFuelLogsBD();
                        if (abastecimentosListener != null) {
                            abastecimentosListener.onRefreshListaAbastecimentos(abastecimentos);
                        }
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getAuthHeaders();
            }

        };
        volleyQueue.add(request);
    }

    /**
     * Adiciona um abastecimento via API.
     */
    public void adicionarAbastecimentoAPI(final FuelLog fuelLog) {
        JSONObject body = fuelLogToJson(fuelLog);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                mUrlAPIFuelLogs,
                body,
                response -> {
                    try {
                        FuelLog novoAbastecimento = VeiGestJsonParser.parserJsonFuelLog(response);
                        if (novoAbastecimento != null) {
                            abastecimentos.add(novoAbastecimento);
                            if (veiGestBD != null) {
                                veiGestBD.adicionarFuelLogBD(novoAbastecimento);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e(TAG, "Erro ao adicionar abastecimento: " + error.toString());
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getAuthHeaders();
            }

        };

        volleyQueue.add(request);
    }

    private void adicionarAbastecimentosBD(ArrayList<FuelLog> lista) {
        abastecimentos.clear();
        abastecimentos.addAll(lista);

        if (veiGestBD != null) {
            veiGestBD.removerAllFuelLogsBD();
            for (FuelLog f : lista) {
                veiGestBD.adicionarFuelLogBD(f);
            }
        }
    }

    // ==================== ALERTAS API ====================

    /**
     * Obtém todos os alertas da API.
     */
    public void getAllAlertasAPI() {
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                mUrlAPIAlerts,
                null,
                response -> {
                    try {
                        ArrayList<Alert> lista = VeiGestJsonParser.parserJsonAlerts(response);
                        adicionarAlertasBD(lista);
                        if (alertasListener != null) {
                            alertasListener.onRefreshListaAlertas(alertas);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e(TAG, "Erro ao obter alertas: " + error.toString());
                    if (veiGestBD != null) {
                        alertas = veiGestBD.getAllAlertsBD();
                        if (alertasListener != null) {
                            alertasListener.onRefreshListaAlertas(alertas);
                        }
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getAuthHeaders();
            }

        };
        volleyQueue.add(request);
    }

    private void adicionarAlertasBD(ArrayList<Alert> lista) {
        alertas.clear();
        alertas.addAll(lista);

        if (veiGestBD != null) {
            veiGestBD.removerAllAlertsBD();
            for (Alert a : lista) {
                veiGestBD.adicionarAlertBD(a);
            }
        }
    }

    // ==================== DOCUMENTOS API ====================

    /**
     * Obtém todos os documentos da API.
     */
    public void getAllDocumentosAPI() {
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                mUrlAPIDocuments,
                null,
                response -> {
                    try {
                        ArrayList<Document> lista = VeiGestJsonParser.parserJsonDocuments(response);
                        adicionarDocumentosBD(lista);
                        if (documentosListener != null) {
                            documentosListener.onRefreshListaDocumentos(documentos);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e(TAG, "Erro ao obter documentos: " + error.toString());
                    if (veiGestBD != null) {
                        documentos = veiGestBD.getAllDocumentsBD();
                        if (documentosListener != null) {
                            documentosListener.onRefreshListaDocumentos(documentos);
                        }
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getAuthHeaders();
            }

        };
        volleyQueue.add(request);
    }

    private void adicionarDocumentosBD(ArrayList<Document> lista) {
        documentos.clear();
        documentos.addAll(lista);

        if (veiGestBD != null) {
            veiGestBD.removerAllDocumentsBD();
            for (Document d : lista) {
                veiGestBD.adicionarDocumentBD(d);
            }
        }
    }

    // ==================== ROTAS API ====================

    /**
     * Obtém todas as rotas da API.
     */
    public void getAllRotasAPI() {
        String url = mUrlAPIRoutes;
        Log.d(TAG, "GET " + url + " | headers=" + getAuthHeaders());
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        ArrayList<Route> lista = VeiGestJsonParser.parserJsonRoutes(response);
                        adicionarRotasBD(lista);
                        if (rotasListener != null) {
                            rotasListener.onRefreshListaRotas(rotas);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e(TAG, "Erro ao obter rotas: " + error.toString());
                    if (error.networkResponse != null) {
                        try {
                            String resp = new String(error.networkResponse.data, "UTF-8");
                            Log.e(TAG, "GET rotas - status=" + error.networkResponse.statusCode + " body=" + resp);
                        } catch (Exception ex) {
                            /* ignore */ }
                    }
                    if (veiGestBD != null) {
                        rotas = veiGestBD.getAllRoutesBD();
                    }
                    if (rotasListener != null) {
                        rotasListener.onRefreshListaRotas(rotas);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getAuthHeaders();
            }
        };
        volleyQueue.add(request);
    }

    private void adicionarRotasBD(ArrayList<Route> lista) {
        rotas.clear();
        rotas.addAll(lista);

        if (veiGestBD != null) {
            veiGestBD.removerAllRoutesBD();
            for (Route r : lista) {
                veiGestBD.adicionarRouteBD(r);
            }
        }
    }

    /**
     * Adiciona uma rota via API.
     */
    public void adicionarRotaAPI(final Route route) {
        JSONObject body = routeToJson(route);
        String url = mUrlAPIRoutes;
        Log.d(TAG, "POST " + url + " | body=" + body.toString() + " | headers=" + getAuthHeaders());
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                response -> {
                    try {
                        Route novaRota = VeiGestJsonParser.parserJsonRoute(response);
                        if (novaRota != null) {
                            rotas.add(novaRota);
                            if (veiGestBD != null) {
                                veiGestBD.adicionarRouteBD(novaRota);
                            }
                        }
                        getAllRotasAPI();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e(TAG, "Erro ao adicionar rota: " + error.toString());
                    if (error.networkResponse != null) {
                        try {
                            String resp = new String(error.networkResponse.data, "UTF-8");
                            Log.e(TAG, "POST rota - status=" + error.networkResponse.statusCode + " body=" + resp);
                        } catch (Exception ex) {
                            /* ignore */ }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getAuthHeaders();
            }
        };
        volleyQueue.add(request);
    }

    /**
     * Edita uma rota via API.
     */
    public void editarRotaAPI(final Route route) {
        JSONObject body = routeToJson(route);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                mUrlAPIRoutes + "/" + route.getId(),
                body,
                response -> {
                    try {
                        // Recarrega a lista para garantir sincronia e atualizar UI
                        getAllRotasAPI();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e(TAG, "Erro ao editar rota: " + error.toString());
                    if (error.networkResponse != null) {
                        try {
                            String resp = new String(error.networkResponse.data, "UTF-8");
                            Log.e(TAG, "PUT rota - status=" + error.networkResponse.statusCode + " body=" + resp);
                        } catch (Exception ex) {
                            /* ignore */ }
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getAuthHeaders();
            }

        };

        volleyQueue.add(request);
    }

    /**
     * Conclui uma rota via API (POST /route/{id}/complete).
     */
    public void concluirRotaAPI(final int routeId) {
        String url = baseUrl + "/routes/" + routeId + "/complete";
        Log.d(TAG, "POST " + url + " | headers=" + getAuthHeaders());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null,
                response -> {
                    try {
                        Log.d(TAG, "Rota concluída com sucesso via API");
                        // Recarrega a lista para atualizar UI
                        getAllRotasAPI();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e(TAG, "Erro ao concluir rota: " + error.toString());
                    if (error.networkResponse != null) {
                        try {
                            String resp = new String(error.networkResponse.data, "UTF-8");
                            Log.e(TAG, "POST complete rota - status=" + error.networkResponse.statusCode + " body="
                                    + resp);
                        } catch (Exception ex) {
                            /* ignore */ }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getAuthHeaders();
            }
        };

        volleyQueue.add(request);
    }

    /**
     * Remove uma rota via API.
     */
    public void removerRotaAPI(final int routeId) {
        String url = mUrlAPIRoutes + "/" + routeId;
        Log.d(TAG, "DELETE " + url + " | headers=" + getAuthHeaders());
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                response -> {
                    for (int i = 0; i < rotas.size(); i++) {
                        if (rotas.get(i).getId() == routeId) {
                            rotas.remove(i);
                            break;
                        }
                    }
                    if (veiGestBD != null) {
                        veiGestBD.removerRouteBD(routeId);
                    }
                    if (rotasListener != null) {
                        rotasListener.onRefreshListaRotas(rotas);
                    }
                },
                error -> {
                    Log.e(TAG, "Erro ao remover rota: " + error.toString());
                    if (error.networkResponse != null) {
                        try {
                            String resp = new String(error.networkResponse.data, "UTF-8");
                            Log.e(TAG, "DELETE rota - status=" + error.networkResponse.statusCode + " body=" + resp);
                        } catch (Exception ex) {
                            /* ignore */ }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getAuthHeaders();
            }
        };
        volleyQueue.add(request);
    }

    // ==================== UTILITÁRIOS ====================

    /**
     * Obtém headers com autenticação.
     */
    private Map<String, String> getAuthHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");

        String token = getToken();
        if (token != null && !token.isEmpty()) {
            headers.put("Authorization", "Bearer " + token);
        }

        return headers;
    }

    /**
     * Converte Vehicle para JSON.
     */
    private JSONObject vehicleToJson(Vehicle vehicle) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("license_plate", vehicle.getLicensePlate());
            jsonBody.put("brand", vehicle.getBrand());
            jsonBody.put("model", vehicle.getModel());
            jsonBody.put("year", vehicle.getYear());
            jsonBody.put("fuel_type", vehicle.getFuelType());
            jsonBody.put("mileage", vehicle.getMileage());
            jsonBody.put("status", vehicle.getStatus());

            // Adicionar foto se existir (Base64)
            if (vehicle.getPhoto() != null && !vehicle.getPhoto().isEmpty()) {
                jsonBody.put("photo", vehicle.getPhoto());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonBody;
    }

    /**
     * Converte Maintenance para JSON.
     */
    private JSONObject maintenanceToJson(Maintenance maintenance) {
        JSONObject json = new JSONObject();
        try {
            json.put("vehicle_id", maintenance.getVehicleId());
            json.put("type", maintenance.getType());
            json.put("description", maintenance.getDescription());
            json.put("cost", maintenance.getCost());
            json.put("date", maintenance.getDate());
            json.put("mileage_record", maintenance.getMileageRecord());
            json.put("workshop", maintenance.getWorkshop());
            json.put("status", maintenance.getStatus());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * Converte FuelLog para JSON.
     */
    private JSONObject fuelLogToJson(FuelLog fuelLog) {
        JSONObject json = new JSONObject();
        try {
            json.put("vehicle_id", fuelLog.getVehicleId());
            json.put("liters", fuelLog.getLiters());
            json.put("value", fuelLog.getValue());
            json.put("current_mileage", fuelLog.getCurrentMileage());
            json.put("date", fuelLog.getDate());
            json.put("notes", fuelLog.getNotes());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * Carrega dados da BD local para memória.
     */
    public void carregarDadosLocais() {
        if (veiGestBD != null) {
            veiculos = veiGestBD.getAllVehiclesBD();
            manutencoes = veiGestBD.getAllMaintenancesBD();
            abastecimentos = veiGestBD.getAllFuelLogsBD();
            alertas = veiGestBD.getAllAlertsBD();
            documentos = veiGestBD.getAllDocumentsBD();
            rotas = veiGestBD.getAllRoutesBD();

            int userId = getUserId();
            if (userId > 0) {
                utilizadorAtual = veiGestBD.getUserBD(userId);
            }
        }
    }

    private JSONObject routeToJson(Route route) {
        JSONObject json = new JSONObject();
        try {
            json.put("start_location", route.getStartLocation());
            json.put("end_location", route.getEndLocation());
            json.put("start_time", route.getStartTime());
            json.put("vehicle_id", route.getVehicleId());
            json.put("driver_id", route.getDriverId());
            json.put("status", route.getStatus());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * Obtém os dados do utilizador atual da API para atualizar cache e BD local.
     */
    public void getUtilizadorAtualAPI() {
        int userId = getUserId();
        if (userId <= 0)
            return;

        String url = mUrlAPIUsers + "/" + userId;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        User user = VeiGestJsonParser.parserJsonUser(response);
                        if (user != null) {
                            utilizadorAtual = user;
                            if (veiGestBD != null) {
                                veiGestBD.adicionarUserBD(user);
                            }
                            if (profileListener != null) {
                                profileListener.onProfileUpdated();
                            }
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Erro ao processar dados do utilizador", e);
                    }
                },
                error -> Log.e(TAG, "Erro ao obter dados do utilizador: " + error.toString())) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getAuthHeaders();
            }
        };

        volleyQueue.add(request);
    }

    /**
     * Edita um utilizador via API.
     */
    public void editarUtilizadorAPI(final User user) {
        if (user == null || user.getId() == 0) {
            Log.e(TAG, "editarUtilizadorAPI: User inválido");
            return;
        }

        JSONObject body = new JSONObject();
        try {
            body.put("email", user.getEmail());
            body.put("phone", user.getPhone());
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        String url = mUrlAPIUsers + "/" + user.getId();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                body,
                response -> {
                    try {
                        User updatedUser = VeiGestJsonParser.parserJsonUser(response);
                        if (updatedUser != null) {
                            utilizadorAtual = updatedUser;
                            if (veiGestBD != null) {
                                veiGestBD.adicionarUserBD(updatedUser);
                            }
                            Log.d(TAG, "Utilizador atualizado com sucesso (API + Local)");
                            if (profileListener != null) {
                                profileListener.onProfileUpdated();
                            }
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Erro ao processar resposta", e);
                        if (profileListener != null) {
                            profileListener.onProfileError("Erro ao processar resposta do servidor");
                        }
                    }
                },
                error -> {
                    Log.e(TAG, "Erro ao editar utilizador: " + error.toString());
                    if (profileListener != null) {
                        profileListener.onProfileError("Erro na ligação ao servidor");
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getAuthHeaders();
            }
        };

        volleyQueue.add(request);
    }
}
