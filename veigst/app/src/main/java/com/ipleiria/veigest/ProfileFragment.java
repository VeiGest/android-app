package com.ipleiria.veigest;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.card.MaterialCardView;

/**
 * Profile Fragment - Perfil do Usuário
 * Exibe informações do perfil do condutor
 */
public class ProfileFragment extends Fragment {

    private MaterialCardView cardProfile;
    private Button btnEditProfile;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Inicializar views
        cardProfile = view.findViewById(R.id.card_profile);
        btnEditProfile = view.findViewById(R.id.btn_edit_profile);

        // Setup click listener
        btnEditProfile.setOnClickListener(v -> 
            Toast.makeText(getContext(), "Editar Perfil (em breve)", Toast.LENGTH_SHORT).show()
        );

        return view;
    }
}
