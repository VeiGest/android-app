package com.ipleiria.veigest;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    /**
     * Executa o login (mockado)
     * TODO: Substituir por chamada real à API POST /auth/login
     */
    private void performLogin(String username, String password) {
        // Simular login bem-sucedido
        Toast.makeText(getContext(), "Login bem-sucedido!", Toast.LENGTH_SHORT).show();

        // Navegar para o Dashboard
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).loadDashboard();
        }

        // TODO: Implementar autenticação real:
        // 1. Chamar POST /auth/login com username e password
        // 2. Guardar token de autenticação
        // 3. Guardar dados do utilizador (SharedPreferences)
        // 4. Navegar para Dashboard apenas se login for bem-sucedido
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
    }
}
