package com.ipleiria.veigest;

import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.button.MaterialButtonToggleGroup;

/**
 * Settings Fragment - Configurações da Aplicação
 * Permite configurar API, tema, notificações e sincronização
 */
public class SettingsFragment extends Fragment {

    // Header
    private ImageView btnMenu;

    // Profile
    private com.google.android.material.card.MaterialCardView cardProfile;

    // API Settings
    private com.google.android.material.card.MaterialCardView cardApi;
    private android.widget.TextView tvApiUrl;

    // Theme Settings
    private MaterialButtonToggleGroup themeToggleGroup;
    private ThemeManager themeManager;

    // Language Settings
    private MaterialButtonToggleGroup languageToggleGroup;
    private LanguageManager languageManager;

    // Notifications Settings
    private Switch switchNotifications;
    private Switch switchNotificationSound;

    // Sync Settings
    private Switch switchAutoSync;

    // API Settings tracking
    private String originalApiUrl;

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

        // Inicializar ThemeManager e LanguageManager
        themeManager = ThemeManager.getInstance(requireContext());
        languageManager = LanguageManager.getInstance(requireContext());

        // Inicializar views
        btnMenu = view.findViewById(R.id.btn_menu);
        cardProfile = view.findViewById(R.id.card_profile);
        cardApi = view.findViewById(R.id.card_api);
        tvApiUrl = view.findViewById(R.id.tv_api_url);
        themeToggleGroup = view.findViewById(R.id.theme_toggle_group);
        languageToggleGroup = view.findViewById(R.id.language_toggle_group);
        switchNotifications = view.findViewById(R.id.switch_notifications);
        switchNotificationSound = view.findViewById(R.id.switch_notification_sound);
        switchAutoSync = view.findViewById(R.id.switch_auto_sync);

        // Carregar configurações mockadas
        loadMockSettings();

        // Setup listeners
        setupListeners();

        return view;
    }

    private void loadMockSettings() {
        // Mock API settings
        originalApiUrl = "https://api.veigest.com/v1";
        tvApiUrl.setText(originalApiUrl);

        // Carregar tema atual
        int currentTheme = themeManager.getThemeMode();
        switch (currentTheme) {
            case ThemeManager.MODE_LIGHT:
                themeToggleGroup.check(R.id.btn_theme_light);
                break;
            case ThemeManager.MODE_DARK:
                themeToggleGroup.check(R.id.btn_theme_dark);
                break;
            case ThemeManager.MODE_SYSTEM:
            default:
                themeToggleGroup.check(R.id.btn_theme_system);
                break;
        }

        // Carregar idioma atual
        String currentLanguage = languageManager.getCurrentLanguage();
        switch (currentLanguage) {
            case LanguageManager.LANGUAGE_PORTUGUESE:
                languageToggleGroup.check(R.id.btn_language_pt);
                break;
            case LanguageManager.LANGUAGE_ENGLISH:
                languageToggleGroup.check(R.id.btn_language_en);
                break;
            default:
                languageToggleGroup.check(R.id.btn_language_pt);
                break;
        }

        // Mock notification settings
        switchNotifications.setChecked(true);
        switchNotificationSound.setChecked(true);

        // Mock sync settings
        switchAutoSync.setChecked(true);
    }

    private void setupListeners() {
        // Menu button - opens navigation drawer
        btnMenu.setOnClickListener(v -> {
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.openDrawer();
            }
        });

        // Profile card click - navegar para ProfileFragment
        cardProfile.setOnClickListener(v -> {
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.loadFragment(new ProfileFragment());
            }
        });

        // API card click
        cardApi.setOnClickListener(v -> {
            showEditApiDialog();
        });

        // Theme toggle listener (auto-save)
        themeToggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                int themeMode;
                if (checkedId == R.id.btn_theme_light) {
                    themeMode = ThemeManager.MODE_LIGHT;
                } else if (checkedId == R.id.btn_theme_dark) {
                    themeMode = ThemeManager.MODE_DARK;
                } else {
                    themeMode = ThemeManager.MODE_SYSTEM;
                }
                themeManager.setThemeMode(themeMode);
                
                // Aplicar tema na Activity atual
                if (getActivity() != null) {
                    requireActivity().recreate();
                }
            }
        });

        // Language toggle listener (auto-save)
        languageToggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                String languageCode;
                if (checkedId == R.id.btn_language_pt) {
                    languageCode = LanguageManager.LANGUAGE_PORTUGUESE;
                } else {
                    languageCode = LanguageManager.LANGUAGE_ENGLISH;
                }
                
                // Salvar idioma
                languageManager.setLanguage(languageCode);
                
                // Aplicar idioma na Activity
                if (getActivity() != null) {
                    languageManager.applyLanguageToActivity(requireActivity(), languageCode);
                    requireActivity().recreate();
                }
            }
        });

        // Notification and sync switches listeners (auto-save - just for mockup)
        CompoundButton.OnCheckedChangeListener switchListener = (buttonView, isChecked) -> {
            // Auto-save happens here (mockup - no actual saving needed)
        };

        switchNotifications.setOnCheckedChangeListener(switchListener);
        switchNotificationSound.setOnCheckedChangeListener(switchListener);
        switchAutoSync.setOnCheckedChangeListener(switchListener);
    }



    private void showEditApiDialog() {
        // Criar EditText para o diálogo
        EditText input = new EditText(requireContext());
        input.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_URI);
        input.setText(tvApiUrl.getText().toString());
        input.setHint("https://api.veigest.com/v1");
        input.setPadding(50, 40, 50, 40);

        new AlertDialog.Builder(requireContext())
            .setTitle("Editar Endereço da API")
            .setMessage("Insira o novo endereço da API:")
            .setView(input)
            .setPositiveButton("Salvar", (dialog, which) -> {
                String newApiUrl = input.getText().toString().trim();
                if (!newApiUrl.isEmpty()) {
                    tvApiUrl.setText(newApiUrl);
                    originalApiUrl = newApiUrl;
                    Toast.makeText(getContext(), 
                        "Configuração de API guardada!\n" +
                        "API: " + newApiUrl, 
                        Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Endereço da API não pode estar vazio", Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton("Cancelar", (dialog, which) -> {
                dialog.cancel();
            })
            .show();
    }

}