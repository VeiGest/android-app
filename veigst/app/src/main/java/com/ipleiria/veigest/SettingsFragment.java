package com.ipleiria.veigest;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

/**
 * Settings Fragment - Configurações da Aplicação
 * Permite configurar API, tema, notificações e sincronização
 */
public class SettingsFragment extends Fragment {

    // API Settings
    private EditText etApiUrl;
    private EditText etApiCompany;

    // Theme Settings
    private RadioGroup rgTheme;

    // Notifications Settings
    private Switch switchNotifications;
    private Switch switchNotificationSound;

    // Sync Settings
    private Switch switchAutoSync;

    // Actions
    private Button btnSaveSettings;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Inicializar views
        etApiUrl = view.findViewById(R.id.et_api_url);
        etApiCompany = view.findViewById(R.id.et_api_company);
        rgTheme = view.findViewById(R.id.rg_theme);
        switchNotifications = view.findViewById(R.id.switch_notifications);
        switchNotificationSound = view.findViewById(R.id.switch_notification_sound);
        switchAutoSync = view.findViewById(R.id.switch_auto_sync);
        btnSaveSettings = view.findViewById(R.id.btn_save_settings);

        // Carregar configurações mockadas
        loadMockSettings();

        // Setup click listener
        btnSaveSettings.setOnClickListener(v -> saveSettings());

        return view;
    }

    private void loadMockSettings() {
        // Mock API settings
        etApiUrl.setText("https://api.veigest.com/v1");
        etApiCompany.setText("1");

        // Mock theme (Sistema = default)
        rgTheme.check(R.id.rb_theme_system);

        // Mock notification settings
        switchNotifications.setChecked(true);
        switchNotificationSound.setChecked(true);

        // Mock sync settings
        switchAutoSync.setChecked(true);
    }

    private void saveSettings() {
        // Mockup - apenas mostra toast
        String apiUrl = etApiUrl.getText().toString();
        String apiCompany = etApiCompany.getText().toString();

        int selectedThemeId = rgTheme.getCheckedRadioButtonId();
        String theme = "Sistema";
        if (selectedThemeId == R.id.rb_theme_light) {
            theme = "Claro";
        } else if (selectedThemeId == R.id.rb_theme_dark) {
            theme = "Escuro";
        }

        Toast.makeText(getContext(), 
            "Configurações guardadas!\n" +
            "API: " + apiUrl + "\n" +
            "Empresa: " + apiCompany + "\n" +
            "Tema: " + theme, 
            Toast.LENGTH_LONG).show();
    }
}
