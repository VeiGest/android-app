package com.veigest.sdk.listeners;

import com.veigest.sdk.models.User;

/**
 * Listener para operações de login.
 */
public interface LoginListener {
    /**
     * Chamado quando o login é validado.
     * @param token Token de acesso
     * @param user Dados do utilizador
     */
    void onValidateLogin(String token, User user);
    
    /**
     * Chamado quando o login falha.
     * @param mensagem Mensagem de erro
     */
    void onLoginError(String mensagem);
}
