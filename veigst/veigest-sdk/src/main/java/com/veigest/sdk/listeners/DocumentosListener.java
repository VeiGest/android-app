package com.veigest.sdk.listeners;

import com.veigest.sdk.models.Document;
import java.util.ArrayList;

/**
 * Listener para operações de documentos.
 */
public interface DocumentosListener {
    /**
     * Chamado quando a lista de documentos é atualizada.
     * @param listaDocumentos Lista atualizada de documentos
     */
    void onRefreshListaDocumentos(ArrayList<Document> listaDocumentos);
}
