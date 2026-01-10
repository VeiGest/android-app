package com.veigest.sdk.listeners;

/**
 * Listener para operações em um veículo específico.
 */
public interface VeiculoListener {
    int OPERACAO_ADICIONAR = 1;
    int OPERACAO_EDITAR = 2;
    int OPERACAO_REMOVER = 3;
    
    /**
     * Chamado quando uma operação em veículo é concluída.
     * @param operacao Tipo de operação (ADICIONAR, EDITAR, REMOVER)
     */
    void onRefreshDetalhes(int operacao);
}
