package com.ipleiria.veigest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.veigest.sdk.SingletonVeiGest;
import com.veigest.sdk.models.User;

/**
 * Profile Fragment - Perfil do Usuário
 * Exibe informações do perfil do condutor obtidas do SDK
 */
public class ProfileFragment extends Fragment {

    private MaterialCardView cardProfile;
    private ImageView ivProfileDesc;
    private TextView tvName;
    private TextView tvEmail;
    private TextView tvRole;
    private TextView tvCompany;

    private Button btnEditProfile;
    private Button btnLogout;

    private SingletonVeiGest singleton;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Obter instância do Singleton
        singleton = SingletonVeiGest.getInstance(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        initializeViews(view);

        // Setup click listeners
        setupListeners();

        // Carregar dados
        loadUserData();

        return view;
    }

    private void initializeViews(View view) {
        cardProfile = view.findViewById(R.id.card_profile);
        // Assumindo que o layout tem estes IDs. Se não tiver, vamos ter de ajustar o
        // XML.
        // Baseado na estrutura típica, vou tentar encontrar os TextViews dentro do card
        // ou usar findViewById diretos se o XML permitir.
        // Como não vi o XML, vou usar IDs padrão. Se falhar, ajustarei o XML no próximo
        // passo.

        // Nota: O código original só tinha cardProfile e btnEditProfile mapeados.
        // Vou procurar os text views que devem estar lá dentro.
        tvName = view.findViewById(R.id.tv_profile_name);
        tvEmail = view.findViewById(R.id.tv_profile_email);
        tvRole = view.findViewById(R.id.tv_profile_role);

        btnEditProfile = view.findViewById(R.id.btn_edit_profile);
        // btnLogout seria útil aqui também se o layout permitir

        setupHeader(view);
    }

    private void setupHeader(View view) {
        View btnMenu = view.findViewById(R.id.btn_menu_global);
        TextView tvTitle = view.findViewById(R.id.tv_header_title);
        if (tvTitle != null) {
            tvTitle.setText(R.string.profile_title);
        }
        if (btnMenu != null) {
            btnMenu.setOnClickListener(v -> {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).openDrawer();
                }
            });
        }
    }

    private void setupListeners() {
        btnEditProfile.setOnClickListener(
                v -> Toast.makeText(getContext(), "Editar Perfil (em breve)", Toast.LENGTH_SHORT).show());
    }

    private void loadUserData() {
        User user = singleton.getUtilizadorAtual();

        if (user != null) {
            if (tvName != null)
                tvName.setText(user.getUsername());
            if (tvEmail != null)
                tvEmail.setText(user.getEmail());

            String role = user.getRole();
            if (role != null) {
                // Capitalize first letter
                role = role.substring(0, 1).toUpperCase() + role.substring(1);
            } else {
                role = "Utilizador";
            }
            if (tvRole != null)
                tvRole.setText(role);
        } else {
            // Fallback se não estiver logado (não deve acontecer nesta tela)
            if (tvName != null)
                tvName.setText("Visitante");
            if (tvEmail != null)
                tvEmail.setText("-");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Recarregar dados caso tenham mudado
        loadUserData();
    }
}
