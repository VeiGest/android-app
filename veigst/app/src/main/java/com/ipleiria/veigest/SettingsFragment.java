package com.ipleiria.veigest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.veigest.sdk.SingletonVeiGest;

/**
 * Settings Fragment - Configurações da Aplicação
 * 
 * Funcionalidades:
 * - Configurar URL da API (obrigatório no enunciado)
 * - Configurar ID da empresa
 * - Selecionar tema (claro/escuro/sistema)
 * - Ativar/desativar notificações
 * - Ativar/desativar sincronização automática
 * 
 * Usa SharedPreferences para persistir configurações localmente
 */
public class SettingsFragment extends Fragment {

    private static final String PREFS_NAME = "veigest_settings";
    private static final String PREF_API_URL = "api_url";
    private static final String PREF_COMPANY_ID = "company_id";
    private static final String PREF_THEME = "theme";
    private static final String PREF_NOTIFICATIONS = "notifications_enabled";
    private static final String PREF_NOTIFICATION_SOUND = "notification_sound";
    private static final String PREF_AUTO_SYNC = "auto_sync";

    // API Settings
    // API Settings - Removed as per user request (handled by config)
    // private EditText etApiUrl;
    // private EditText etApiCompany;

    // Theme Settings
    private RadioGroup rgTheme;

    // Notifications Settings
    private Switch switchNotifications;
    private Switch switchNotificationSound;

    // Sync Settings
    private Switch switchAutoSync;

    // Actions
    private Button btnSaveSettings;

    private SharedPreferences sharedPreferences;
    private SingletonVeiGest singleton;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        singleton = SingletonVeiGest.getInstance(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Inicializar views
        initializeViews(view);

        // Carregar configurações guardadas
        loadSettings();

        // Setup listeners
        setupListeners();

        return view;
    }

    private void initializeViews(View view) {
        // etApiUrl = view.findViewById(R.id.et_api_url);
        // etApiCompany = view.findViewById(R.id.et_api_company);
        rgTheme = view.findViewById(R.id.rg_theme);
        switchNotifications = view.findViewById(R.id.switch_notifications);
        switchNotificationSound = view.findViewById(R.id.switch_notification_sound);
        switchAutoSync = view.findViewById(R.id.switch_auto_sync);
        btnSaveSettings = view.findViewById(R.id.btn_save_settings);

        setupHeader(view);
    }

    private void setupHeader(View view) {
        View btnMenu = view.findViewById(R.id.btn_menu_global);
        if (btnMenu != null) {
            btnMenu.setOnClickListener(v -> {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).openDrawer();
                }
            });
        }
    }

    /**
     * Carrega configurações guardadas em SharedPreferences
     */
    private void loadSettings() {
        // URL da API e Company ID removidos da UI
        // String apiUrl = sharedPreferences.getString(PREF_API_URL,
        // "https://veigestback.dryadlang.org/api");
        // etApiUrl.setText(apiUrl);
        // String companyId = sharedPreferences.getString(PREF_COMPANY_ID, "1");
        // etApiCompany.setText(companyId);

        // Tema
        int theme = sharedPreferences.getInt(PREF_THEME, 0); // 0 = Sistema
        switch (theme) {
            case 1:
                rgTheme.check(R.id.rb_theme_light);
                break;
            case 2:
                rgTheme.check(R.id.rb_theme_dark);
                break;
            default:
                rgTheme.check(R.id.rb_theme_system);
        }

        // Notificações
        boolean notificationsEnabled = sharedPreferences.getBoolean(PREF_NOTIFICATIONS, true);
        switchNotifications.setChecked(notificationsEnabled);

        boolean notificationSoundEnabled = sharedPreferences.getBoolean(PREF_NOTIFICATION_SOUND, true);
        switchNotificationSound.setChecked(notificationSoundEnabled);

        // Sincronização automática
        boolean autoSyncEnabled = sharedPreferences.getBoolean(PREF_AUTO_SYNC, true);
        switchAutoSync.setChecked(autoSyncEnabled);

        // Atualizar estado do switch de som baseado no estado das notificações
        switchNotificationSound.setEnabled(notificationsEnabled);
    }

    private void setupListeners() {
        // Guardar configurações
        btnSaveSettings.setOnClickListener(v -> saveSettings());

        // Listener para switch de notificações (habilitar/desabilitar som)
        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            switchNotificationSound.setEnabled(isChecked);
            if (!isChecked) {
                switchNotificationSound.setChecked(false);
            }
        });
    }

    /**
     * Guarda todas as configurações em SharedPreferences
     */
    private void saveSettings() {
        // Obter valores
        // String apiUrl = etApiUrl.getText().toString().trim();
        // String companyId = etApiCompany.getText().toString().trim();

        // Validação removida pois os campos não existem mais
        /*
         * if (apiUrl.isEmpty()) {
         * Toast.makeText(getContext(), "O URL da API é obrigatório",
         * Toast.LENGTH_SHORT).show();
         * etApiUrl.requestFocus();
         * return;
         * }
         */

        // Determinar tema selecionado
        int theme = 0; // Sistema
        int selectedThemeId = rgTheme.getCheckedRadioButtonId();
        if (selectedThemeId == R.id.rb_theme_light) {
            theme = 1;
        } else if (selectedThemeId == R.id.rb_theme_dark) {
            theme = 2;
        }

        // Verificar se houve mudança de tema
        int currentTheme = sharedPreferences.getInt(PREF_THEME, 0);
        boolean themeChanged = (theme != currentTheme);

        // Guardar em SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // editor.putString(PREF_API_URL, apiUrl);
        // editor.putString(PREF_COMPANY_ID, companyId);
        editor.putInt(PREF_THEME, theme);
        editor.putBoolean(PREF_NOTIFICATIONS, switchNotifications.isChecked());
        editor.putBoolean(PREF_NOTIFICATION_SOUND, switchNotificationSound.isChecked());
        editor.putBoolean(PREF_AUTO_SYNC, switchAutoSync.isChecked());
        editor.apply();

        // Atualizar URL no Singleton - removido, usa config
        // singleton.setBaseUrl(apiUrl);

        // Aplicar tema
        applyTheme(theme);

        if (themeChanged && getActivity() != null) {
            getActivity().recreate();
        }

        // Feedback
        String themeLabel = "Sistema";
        if (theme == 1)
            themeLabel = "Claro";
        else if (theme == 2)
            themeLabel = "Escuro";

        Toast.makeText(getContext(),
                "Configurações guardadas!\n" +
                        "Tema: " + themeLabel,
                Toast.LENGTH_LONG).show();
    }

    /**
     * Aplica o tema selecionado
     */
    private void applyTheme(int theme) {
        switch (theme) {
            case 1:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case 2:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }

    /**
     * Limpa cache local (BD SQLite)
     */
    private void clearCache() {
        new android.app.AlertDialog.Builder(requireContext())
                .setTitle("Limpar Cache")
                .setMessage(
                        "Tem a certeza que deseja limpar todos os dados em cache?\n\nIsto irá remover todos os dados locais guardados.")
                .setPositiveButton("Limpar", (dialog, which) -> {
                    // Limpar BD local via Singleton
                    if (singleton.getBD() != null) {
                        singleton.getBD().limparTudo();
                    }
                    Toast.makeText(getContext(), "Cache limpo com sucesso!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * Método estático para obter configurações de qualquer lugar
     */
    public static String getApiUrl(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(PREF_API_URL, "https://veigestback.dryadlang.org/api");
    }

    public static boolean isAutoSyncEnabled(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(PREF_AUTO_SYNC, true);
    }

    public static boolean areNotificationsEnabled(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(PREF_NOTIFICATIONS, true);
    }
}
