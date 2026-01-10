package com.veigest.sdk.listeners;

import com.veigest.sdk.models.Vehicle;
import java.util.ArrayList;

/**
 * Listener para operações de veículos.
 * Notifica quando a lista de veículos é atualizada.
 */
public interface VeiculosListener {
    /**
     * Chamado quando a lista de veículos é atualizada.
     * @param listaVeiculos Lista atualizada de veículos
     */
    void onRefreshListaVeiculos(ArrayList<Vehicle> listaVeiculos);
}
