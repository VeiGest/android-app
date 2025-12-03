package com.ipleiria.veigest;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.card.MaterialCardView;

/**
 * Routes Fragment - Gerenciamento de Rotas
 * Exibe lista de rotas disponíveis e permite visualizar detalhes
 */
public class RoutesFragment extends Fragment {

    private TextView tvNoData;
    private MaterialCardView cardRoute1;
    private MaterialCardView cardRoute2;
    private MaterialCardView cardRoute3;

    public RoutesFragment() {
        // Required empty public constructor
    }

    public static RoutesFragment newInstance() {
        return new RoutesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routes, container, false);

        // Inicializar views
        tvNoData = view.findViewById(R.id.tv_no_data);
        cardRoute1 = view.findViewById(R.id.card_route_1);
        cardRoute2 = view.findViewById(R.id.card_route_2);
        cardRoute3 = view.findViewById(R.id.card_route_3);

        // Setup click listeners
        setupClickListeners();

        return view;
    }

    private void setupClickListeners() {
        cardRoute1.setOnClickListener(v -> 
            Toast.makeText(getContext(), "Detalhes da Rota 1", Toast.LENGTH_SHORT).show()
        );

        cardRoute2.setOnClickListener(v -> 
            Toast.makeText(getContext(), "Detalhes da Rota 2", Toast.LENGTH_SHORT).show()
        );

        cardRoute3.setOnClickListener(v -> 
            Toast.makeText(getContext(), "Detalhes da Rota 3", Toast.LENGTH_SHORT).show()
        );
    }
}
