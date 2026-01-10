package com.veigest.sdk.listeners;

import com.veigest.sdk.models.Route;
import java.util.ArrayList;

/**
 * Listener para operações de rotas.
 */
public interface RotasListener {
    /**
     * Chamado quando a lista de rotas é atualizada.
     * @param listaRotas Lista atualizada de rotas
     */
    void onRefreshListaRotas(ArrayList<Route> listaRotas);
}
