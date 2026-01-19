package com.veigest.sdk.listeners;

/**
 * Listener para operações de perfil do utilizador.
 */
public interface ProfileListener {
    /**
     * Chamado quando o perfil é atualizado com sucesso.
     */
    void onProfileUpdated();

    /**
     * Chamado quando ocorre um erro na atualização do perfil.
     * 
     * @param message Mensagem de erro
     */
    void onProfileError(String message);
}
