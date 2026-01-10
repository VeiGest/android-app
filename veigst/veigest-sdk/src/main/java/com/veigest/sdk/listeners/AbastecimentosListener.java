package com.veigest.sdk.listeners;

import com.veigest.sdk.models.FuelLog;
import java.util.ArrayList;

/**
 * Listener para operações de abastecimentos.
 */
public interface AbastecimentosListener {
    /**
     * Chamado quando a lista de abastecimentos é atualizada.
     * @param listaAbastecimentos Lista atualizada de abastecimentos
     */
    void onRefreshListaAbastecimentos(ArrayList<FuelLog> listaAbastecimentos);
}
