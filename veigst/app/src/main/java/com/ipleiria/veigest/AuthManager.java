package com.ipleiria.veigest;

import android.content.Context;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Classe para gestão de autenticação local
 * Usa BCrypt para hash de passwords e armazenamento seguro
 */
public class AuthManager {
    
    private static final String KEY_USERNAME = "current_username";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_REMEMBER_LOGIN = "remember_login";
    private static final String KEY_USER_PREFIX = "user_";
    private static final String KEY_PASS_SUFFIX = "_password_hash";
    private static final String KEY_EMAIL_SUFFIX = "_email";
    private static final String KEY_RECOVERY_CODE = "recovery_code_";
    private static final String KEY_RECOVERY_EXPIRY = "recovery_expiry_";
    private static final int RECOVERY_CODE_LENGTH = 6;
    private static final long RECOVERY_CODE_VALIDITY_MS = 3 * 60 * 1000; // 3 minutos
    
    private static AuthManager instance;
    private SecureStorage secureStorage;
    
    private AuthManager(Context context) {
        this.secureStorage = SecureStorage.getInstance(context);
    }
    
    public static synchronized AuthManager getInstance(Context context) {
        if (instance == null) {
            instance = new AuthManager(context.getApplicationContext());
        }
        return instance;
    }
    
    /**
     * Regista um novo utilizador
     * @return true se o registo foi bem-sucedido, false se o username já existe
     */
    public boolean registerUser(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()) {
            return false;
        }
        
        String normalizedUsername = username.trim().toLowerCase();
        
        // Verificar se o utilizador já existe
        if (userExists(normalizedUsername)) {
            return false;
        }
        
        // Hash da password usando BCrypt
        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt(12));
        
        // Guardar utilizador
        String userKey = KEY_USER_PREFIX + normalizedUsername + KEY_PASS_SUFFIX;
        secureStorage.saveString(userKey, passwordHash);
        
        return true;
    }
    
    /**
     * Faz login do utilizador
     * @param username Username do utilizador
     * @param password Password do utilizador
     * @param rememberMe Se true, mantém a sessão ativa entre aberturas da app
     * @return true se as credenciais são válidas, false caso contrário
     */
    public boolean login(String username, String password, boolean rememberMe) {
        if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()) {
            return false;
        }
        
        String normalizedUsername = username.trim().toLowerCase();
        
        // Verificar se o utilizador existe
        if (!userExists(normalizedUsername)) {
            return false;
        }
        
        // Obter o hash da password guardado
        String userKey = KEY_USER_PREFIX + normalizedUsername + KEY_PASS_SUFFIX;
        String storedPasswordHash = secureStorage.getString(userKey, null);
        
        if (storedPasswordHash == null) {
            return false;
        }
        
        // Verificar se a password corresponde ao hash
        boolean passwordMatches = BCrypt.checkpw(password, storedPasswordHash);
        
        if (passwordMatches) {
            // Guardar sessão
            secureStorage.saveString(KEY_USERNAME, normalizedUsername);
            secureStorage.saveBoolean(KEY_IS_LOGGED_IN, true);
            secureStorage.saveBoolean(KEY_REMEMBER_LOGIN, rememberMe);
            return true;
        }
        
        return false;
    }
    
    /**
     * Faz login do utilizador (versão compatível - assume rememberMe = true)
     * @deprecated Use login(username, password, rememberMe) instead
     */
    public boolean login(String username, String password) {
        return login(username, password, true);
    }
    
    /**
     * Faz logout do utilizador atual
     */
    public void logout() {
        secureStorage.remove(KEY_USERNAME);
        secureStorage.saveBoolean(KEY_IS_LOGGED_IN, false);
    }
    
    /**
     * Verifica se existe um utilizador com sessão ativa
     * Apenas retorna true se o utilizador tiver marcado "Manter Login"
     */
    public boolean isLoggedIn() {
        boolean isLoggedIn = secureStorage.getBoolean(KEY_IS_LOGGED_IN, false);
        boolean rememberLogin = secureStorage.getBoolean(KEY_REMEMBER_LOGIN, false);
        return isLoggedIn && rememberLogin;
    }
    
    /**
     * Obtém o username do utilizador atual (se estiver logado)
     */
    public String getCurrentUsername() {
        if (isLoggedIn()) {
            return secureStorage.getString(KEY_USERNAME, null);
        }
        return null;
    }
    
    /**
     * Verifica se um utilizador existe
     */
    public boolean userExists(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        String normalizedUsername = username.trim().toLowerCase();
        String userKey = KEY_USER_PREFIX + normalizedUsername + KEY_PASS_SUFFIX;
        return secureStorage.contains(userKey);
    }
    
    /**
     * Remove todos os dados de autenticação (útil para testes)
     */
    public void clearAllData() {
        secureStorage.clear();
    }
    
    /**
     * Gera e guarda um código de recuperação para o utilizador
     * @param username Username do utilizador
     * @return O código gerado (6 dígitos)
     */
    public String generateRecoveryCode(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }
        
        String normalizedUsername = username.trim().toLowerCase();
        
        // Verificar se o utilizador existe
        if (!userExists(normalizedUsername)) {
            return null;
        }
        
        // Gerar código de 6 dígitos
        int code = (int) (Math.random() * 900000) + 100000;
        String recoveryCode = String.valueOf(code);
        
        // Guardar código e data de expiração
        String codeKey = KEY_RECOVERY_CODE + normalizedUsername;
        String expiryKey = KEY_RECOVERY_EXPIRY + normalizedUsername;
        
        secureStorage.saveString(codeKey, recoveryCode);
        secureStorage.saveString(expiryKey, String.valueOf(System.currentTimeMillis() + RECOVERY_CODE_VALIDITY_MS));
        
        return recoveryCode;
    }
    
    /**
     * Verifica se um código de recuperação é válido
     * @param username Username do utilizador
     * @param code Código a verificar
     * @return true se o código é válido e não expirou
     */
    public boolean verifyRecoveryCode(String username, String code) {
        if (username == null || username.trim().isEmpty() || code == null || code.trim().isEmpty()) {
            return false;
        }
        
        String normalizedUsername = username.trim().toLowerCase();
        String codeKey = KEY_RECOVERY_CODE + normalizedUsername;
        String expiryKey = KEY_RECOVERY_EXPIRY + normalizedUsername;
        
        // Obter código guardado
        String storedCode = secureStorage.getString(codeKey, null);
        String expiryStr = secureStorage.getString(expiryKey, null);
        
        if (storedCode == null || expiryStr == null) {
            return false;
        }
        
        // Verificar se o código corresponde
        if (!storedCode.equals(code.trim())) {
            return false;
        }
        
        // Verificar se não expirou
        long expiry = Long.parseLong(expiryStr);
        if (System.currentTimeMillis() > expiry) {
            // Código expirado, limpar
            secureStorage.remove(codeKey);
            secureStorage.remove(expiryKey);
            return false;
        }
        
        return true;
    }
    
    /**
     * Redefine a password do utilizador após validação do código
     * @param username Username do utilizador
     * @param code Código de recuperação
     * @param newPassword Nova password
     * @return true se a password foi redefinida com sucesso
     */
    public boolean resetPassword(String username, String code, String newPassword) {
        if (!verifyRecoveryCode(username, code)) {
            return false;
        }
        
        String normalizedUsername = username.trim().toLowerCase();
        
        // Hash da nova password
        String passwordHash = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));
        
        // Atualizar password
        String userKey = KEY_USER_PREFIX + normalizedUsername + KEY_PASS_SUFFIX;
        secureStorage.saveString(userKey, passwordHash);
        
        // Limpar código de recuperação
        String codeKey = KEY_RECOVERY_CODE + normalizedUsername;
        String expiryKey = KEY_RECOVERY_EXPIRY + normalizedUsername;
        secureStorage.remove(codeKey);
        secureStorage.remove(expiryKey);
        
        return true;
    }
    
    /**
     * Guarda o email associado ao utilizador
     */
    public void saveEmail(String username, String email) {
        if (username == null || email == null) {
            return;
        }
        String normalizedUsername = username.trim().toLowerCase();
        String emailKey = KEY_USER_PREFIX + normalizedUsername + KEY_EMAIL_SUFFIX;
        secureStorage.saveString(emailKey, email);
    }
    
    /**
     * Obtém o email associado ao utilizador
     */
    public String getEmail(String username) {
        if (username == null) {
            return null;
        }
        String normalizedUsername = username.trim().toLowerCase();
        String emailKey = KEY_USER_PREFIX + normalizedUsername + KEY_EMAIL_SUFFIX;
        return secureStorage.getString(emailKey, null);
    }
}
