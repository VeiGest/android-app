package com.ipleiria.veigest;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.TextView;

/**
 * Fragment de Login
 * Permite ao utilizador autenticar-se na aplicação
 */
public class LoginFragment extends Fragment {

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private ImageView ivPasswordToggle;
    private CheckBox cbRemember;
    private boolean isPasswordVisible = false;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Initialize views
        etUsername = view.findViewById(R.id.et_username);
        etPassword = view.findViewById(R.id.et_password);
        btnLogin = view.findViewById(R.id.btn_login);
        ivPasswordToggle = view.findViewById(R.id.iv_password_toggle);
        cbRemember = view.findViewById(R.id.cbRemember);

        // Setup password toggle
        if (ivPasswordToggle != null) {
            ivPasswordToggle.setOnClickListener(v -> togglePasswordVisibility());
        }

        // Setup login button
        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Validação básica
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Por favor preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // TODO: Implementar autenticação real com API
            // Por agora, simular login bem-sucedido
            performLogin(username, password);
        });

        return view;
    }

    private void togglePasswordVisibility() {
        if (etPassword == null || ivPasswordToggle == null) return;

        if (isPasswordVisible) {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            ivPasswordToggle.setImageResource(R.drawable.eye_closed);
            isPasswordVisible = false;
        } else {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            ivPasswordToggle.setImageResource(R.drawable.eye_open);
            isPasswordVisible = true;
        }
        etPassword.setSelection(etPassword.getText().length());
    }

    /**
     * Executa o login com autenticação local
     */
    private void performLogin(String username, String password) {
        AuthManager authManager = AuthManager.getInstance(requireContext());
        
        // Verificar se "Manter Login" está marcado
        boolean rememberMe = cbRemember != null && cbRemember.isChecked();
        
        // Tentar fazer login
        boolean success = authManager.login(username, password, rememberMe);
        
        if (success) {
            Toast.makeText(getContext(), "Login bem-sucedido!", Toast.LENGTH_SHORT).show();
            
            // Navegar para o Dashboard
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).loadDashboard();
            }
        } else {
            // Verificar se o utilizador existe
            if (!authManager.userExists(username)) {
                Toast.makeText(getContext(), "Utilizador não existe. Por favor registe-se.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "Password incorreta. Tente novamente.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // BOTÃO DE IR PARA A PÁGINA DE REGISTO
        TextView tvNoAccount = view.findViewById(R.id.tvNoAccount);

        tvNoAccount.setOnClickListener(v -> {
            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new RegisterFragment())
                    .addToBackStack(null)
                    .commit();
        });
        
        // BOTÃO DE ESQUECI A PASSWORD
        TextView tvForgotPassword = view.findViewById(R.id.tvForgotPassword);
        
        tvForgotPassword.setOnClickListener(v -> {
            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new ForgotPasswordFragment())
                    .addToBackStack(null)
                    .commit();
        });
    }
}
