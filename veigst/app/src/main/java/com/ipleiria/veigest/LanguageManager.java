package com.ipleiria.veigest;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

/**
 * LanguageManager - Gestão de Idiomas da Aplicação
 * Permite alterar e persistir o idioma selecionado pelo utilizador
 */
public class LanguageManager {
    private static final String PREFS_NAME = "language_prefs";
    private static final String KEY_LANGUAGE = "app_language";
    
    // Códigos de idioma
    public static final String LANGUAGE_PORTUGUESE = "pt";
    public static final String LANGUAGE_ENGLISH = "en";
    public static final String LANGUAGE_SYSTEM = "system";
    
    private static LanguageManager instance;
    private Context context;
    private SharedPreferences prefs;
    
    private LanguageManager(Context context) {
        this.context = context.getApplicationContext();
        this.prefs = this.context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
    
    public static synchronized LanguageManager getInstance(Context context) {
        if (instance == null) {
            instance = new LanguageManager(context);
        }
        return instance;
    }
    
    /**
     * Obtém o idioma atual
     */
    public String getCurrentLanguage() {
        return prefs.getString(KEY_LANGUAGE, LANGUAGE_SYSTEM);
    }
    
    /**
     * Define o idioma da aplicação
     */
    public void setLanguage(String languageCode) {
        prefs.edit().putString(KEY_LANGUAGE, languageCode).apply();
    }
    
    /**
     * Aplica o idioma salvo
     */
    public void applyLanguage() {
        String languageCode = getCurrentLanguage();
        applyLanguage(languageCode);
    }
    
    /**
     * Aplica um idioma específico
     */
    public void applyLanguage(String languageCode) {
        Locale locale;
        
        if (languageCode.equals(LANGUAGE_SYSTEM)) {
            // Usar idioma do sistema
            locale = Resources.getSystem().getConfiguration().getLocales().get(0);
        } else {
            // Usar idioma específico
            locale = new Locale(languageCode);
        }
        
        Locale.setDefault(locale);
        
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        
        // Para Android 7.0+ (API 24+)
        context.createConfigurationContext(config);
        // Para manter compatibilidade com versões antigas
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
    
    /**
     * Define o idioma e aplica imediatamente
     */
    public void setLanguageAndApply(String languageCode) {
        setLanguage(languageCode);
        applyLanguage(languageCode);
    }
    
    /**
     * Aplica o idioma em uma Activity específica
     */
    public void applyLanguageToActivity(Context activityContext, String languageCode) {
        Locale locale;
        
        if (languageCode.equals(LANGUAGE_SYSTEM)) {
            locale = Resources.getSystem().getConfiguration().getLocales().get(0);
        } else {
            locale = new Locale(languageCode);
        }
        
        Locale.setDefault(locale);
        
        Resources resources = activityContext.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        
        activityContext.createConfigurationContext(config);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
    
    /**
     * Obtém o nome do idioma atual para exibição
     */
    public String getLanguageName() {
        String languageCode = getCurrentLanguage();
        switch (languageCode) {
            case LANGUAGE_PORTUGUESE:
                return "Português";
            case LANGUAGE_ENGLISH:
                return "English";
            case LANGUAGE_SYSTEM:
            default:
                return "Sistema";
        }
    }
}
