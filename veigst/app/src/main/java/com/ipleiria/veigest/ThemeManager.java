package com.ipleiria.veigest;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;

/**
 * ThemeManager - Gerencia o tema da aplicação
 * Suporta 3 modos: Claro, Escuro e Sistema
 */
public class ThemeManager {
    private static final String PREFS_NAME = "theme_prefs";
    private static final String KEY_THEME_MODE = "theme_mode";
    
    // Modos de tema
    public static final int MODE_LIGHT = 0;
    public static final int MODE_SYSTEM = 1;
    public static final int MODE_DARK = 2;
    
    private static ThemeManager instance;
    private final SharedPreferences prefs;
    
    private ThemeManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
    
    public static synchronized ThemeManager getInstance(Context context) {
        if (instance == null) {
            instance = new ThemeManager(context);
        }
        return instance;
    }
    
    /**
     * Obtém o modo de tema atual
     * @return MODE_LIGHT, MODE_SYSTEM ou MODE_DARK
     */
    public int getThemeMode() {
        return prefs.getInt(KEY_THEME_MODE, MODE_SYSTEM);
    }
    
    /**
     * Define o modo de tema (salva a preferência)
     * @param mode MODE_LIGHT, MODE_SYSTEM ou MODE_DARK
     */
    public void setThemeMode(int mode) {
        prefs.edit().putInt(KEY_THEME_MODE, mode).apply();
    }
    
    /**
     * Define o modo de tema e aplica imediatamente
     * @param mode MODE_LIGHT, MODE_SYSTEM ou MODE_DARK
     */
    public void setThemeModeAndApply(int mode) {
        prefs.edit().putInt(KEY_THEME_MODE, mode).apply();
        applyTheme(mode);
    }
    
    /**
     * Aplica o tema selecionado
     */
    public void applyTheme() {
        applyTheme(getThemeMode());
    }
    
    /**
     * Aplica um tema específico
     * @param mode MODE_LIGHT, MODE_SYSTEM ou MODE_DARK
     */
    private void applyTheme(int mode) {
        switch (mode) {
            case MODE_LIGHT:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case MODE_DARK:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case MODE_SYSTEM:
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }
    
    /**
     * Retorna o nome do tema atual em português
     * @return "Claro", "Sistema" ou "Escuro"
     */
    public String getThemeName() {
        switch (getThemeMode()) {
            case MODE_LIGHT:
                return "Claro";
            case MODE_DARK:
                return "Escuro";
            case MODE_SYSTEM:
            default:
                return "Sistema";
        }
    }
}
