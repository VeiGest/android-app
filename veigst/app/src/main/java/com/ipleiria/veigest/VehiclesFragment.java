package com.ipleiria.veigest;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.material.card.MaterialCardView;

/**
 * Vehicles Fragment - Gerenciamento de Veículos
 * Exibe lista de veículos atribuídos ao condutor
 */
public class VehiclesFragment extends Fragment {

    private MaterialCardView cardVehicle1;
    private MaterialCardView cardVehicle2;

    public VehiclesFragment() {
        // Required empty public constructor
    }

    public static VehiclesFragment newInstance() {
        return new VehiclesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vehicles, container, false);

        // Inicializar views
        cardVehicle1 = view.findViewById(R.id.card_vehicle_1);
        cardVehicle2 = view.findViewById(R.id.card_vehicle_2);

        // Setup click listeners
        setupClickListeners();

        return view;
    }

    private void setupClickListeners() {
        cardVehicle1.setOnClickListener(v -> 
            Toast.makeText(getContext(), "Detalhes do Veículo 1", Toast.LENGTH_SHORT).show()
        );

        cardVehicle2.setOnClickListener(v -> 
            Toast.makeText(getContext(), "Detalhes do Veículo 2", Toast.LENGTH_SHORT).show()
        );
    }
}
