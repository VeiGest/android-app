package com.veigest.sdk.listeners;

import com.veigest.sdk.models.Alert;
import java.util.ArrayList;

/**
 * Listener para operações de alertas.
 */
public interface AlertasListener {
    /**
     * Chamado quando a lista de alertas é atualizada.
     * @param listaAlertas Lista atualizada de alertas
     */
    void onRefreshListaAlertas(ArrayList<Alert> listaAlertas);
}
