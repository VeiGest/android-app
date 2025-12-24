package com.ipleiria.veigest;

import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.card.MaterialCardView;

/**
 * Profile Fragment - Perfil do Usuário
 * Exibe informações do perfil do condutor
 */
public class ProfileFragment extends Fragment {

    private ImageView btnBack;
    private ImageView btnViewPassword;
    private MaterialCardView cardProfile;
    private Button btnEditProfile;
    
    // TextViews para dados do perfil
    private TextView tvProfileUserName;
    private TextView tvEmail;
    private TextView tvPhone;
    private TextView tvLicense;
    private TextView tvCompany;
    
    private AuthManager authManager;

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

        // Inicializar AuthManager
        authManager = AuthManager.getInstance(requireContext());

        // Inicializar views
        btnBack = view.findViewById(R.id.btn_back);
        btnViewPassword = view.findViewById(R.id.btn_view_password);
        cardProfile = view.findViewById(R.id.card_profile);
        btnEditProfile = view.findViewById(R.id.btn_edit_profile);
        
        tvProfileUserName = view.findViewById(R.id.tv_profile_user_name);
        tvEmail = view.findViewById(R.id.tv_email);
        tvPhone = view.findViewById(R.id.tv_phone);
        tvLicense = view.findViewById(R.id.tv_license);
        tvCompany = view.findViewById(R.id.tv_company);

        // Carregar dados do perfil
        loadProfileData();

        // Botão voltar - retorna para SettingsFragment
        btnBack.setOnClickListener(v -> {
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.loadFragment(new SettingsFragment());
            }
        });

        // Botão ver password - solicita código de verificação
        btnViewPassword.setOnClickListener(v -> {
            showPasswordVerificationDialog();
        });

        // Botão editar perfil - navega para EditProfileFragment
        btnEditProfile.setOnClickListener(v -> {
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.loadFragment(new EditProfileFragment());
            }
        });

        return view;
    }

    private void loadProfileData() {
        String email = authManager.getCurrentEmail();
        String name = authManager.getCurrentName();
        
        // Mostrar dados salvos
        if (name != null && !name.isEmpty()) {
            tvProfileUserName.setText(name);
        } else {
            tvProfileUserName.setText("");
        }
        
        if (email != null && !email.isEmpty()) {
            tvEmail.setText(email);
        } else {
            tvEmail.setText("");
        }
        
        // Campos que não são salvos ficam vazios
        tvPhone.setText("");
        tvLicense.setText("");
        tvCompany.setText("");
    }

    private void showPasswordVerificationDialog() {
        String email = authManager.getCurrentEmail();
        if (email == null) {
            Toast.makeText(getContext(), "Erro: utilizador não identificado", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gerar código de verificação
        String code = authManager.generatePasswordViewCode(email);
        
        if (code == null) {
            Toast.makeText(getContext(), "Erro ao gerar código de verificação", Toast.LENGTH_SHORT).show();
            return;
        }

        // Simular envio de email (não implementado)
        // Em produção, aqui seria enviado um email com o código
        
        // Mostrar diálogo informativo
        new AlertDialog.Builder(requireContext())
            .setTitle("Verificação de Segurança")
            .setMessage("Foi enviado um código de verificação para o seu email.\n\n" +
                       "Código: " + code + "\n\n" +
                       "Este código expira em 3 minutos.\n\n" +
                       "Por favor, verifique o seu email e insira o código para visualizar a sua password.")
            .setPositiveButton("Inserir Código", (dialog, which) -> {
                showCodeInputDialog(email);
            })
            .setNegativeButton("Cancelar", null)
            .show();
    }

    private void showCodeInputDialog(String email) {
        EditText input = new EditText(requireContext());
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        input.setHint("Código de 6 dígitos");
        input.setPadding(50, 40, 50, 40);

        new AlertDialog.Builder(requireContext())
            .setTitle("Inserir Código de Verificação")
            .setMessage("Digite o código que foi enviado para o seu email:")
            .setView(input)
            .setPositiveButton("Verificar", (dialog, which) -> {
                String enteredCode = input.getText().toString().trim();
                if (authManager.verifyPasswordViewCode(email, enteredCode)) {
                    Toast.makeText(getContext(), 
                        "Código verificado!\n\nNota: A visualização da password será implementada em breve.", 
                        Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), 
                        "Código inválido ou expirado", 
                        Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton("Cancelar", null)
            .show();
    }
}
