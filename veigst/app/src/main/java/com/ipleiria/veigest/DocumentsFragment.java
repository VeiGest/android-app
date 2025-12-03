package com.ipleiria.veigest;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.material.card.MaterialCardView;

/**
 * Documents Fragment - Gerenciamento de Documentação
 * Exibe documentos do condutor e prazos de validade
 */
public class DocumentsFragment extends Fragment {

    private MaterialCardView cardDoc1;
    private MaterialCardView cardDoc2;
    private MaterialCardView cardDoc3;

    public DocumentsFragment() {
        // Required empty public constructor
    }

    public static DocumentsFragment newInstance() {
        return new DocumentsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_documents, container, false);

        // Inicializar views
        cardDoc1 = view.findViewById(R.id.card_doc_1);
        cardDoc2 = view.findViewById(R.id.card_doc_2);
        cardDoc3 = view.findViewById(R.id.card_doc_3);

        // Setup click listeners
        setupClickListeners();

        return view;
    }

    private void setupClickListeners() {
        cardDoc1.setOnClickListener(v -> 
            Toast.makeText(getContext(), "Ver Documento - Carta de Condução", Toast.LENGTH_SHORT).show()
        );

        cardDoc2.setOnClickListener(v -> 
            Toast.makeText(getContext(), "Ver Documento - Certificado", Toast.LENGTH_SHORT).show()
        );

        cardDoc3.setOnClickListener(v -> 
            Toast.makeText(getContext(), "Ver Documento - Seguro", Toast.LENGTH_SHORT).show()
        );
    }
}
