package com.veigest.sdk.listeners;

import com.veigest.sdk.models.Maintenance;
import java.util.ArrayList;

/**
 * Listener para operações de manutenções.
 */
public interface ManutencoesListener {
    /**
     * Chamado quando a lista de manutenções é atualizada.
     * @param listaManutencoes Lista atualizada de manutenções
     */
    void onRefreshListaManutencoes(ArrayList<Maintenance> listaManutencoes);
}
