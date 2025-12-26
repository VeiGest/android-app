package com.ipleiria.veigest.examples;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ipleiria.veigest.MainActivity;
import com.ipleiria.veigest.R;
import com.veigest.sdk.VeiGestCallback;
import com.veigest.sdk.VeiGestException;
import com.veigest.sdk.VeiGestSDK;
import com.veigest.sdk.models.LoginResponse;
import com.veigest.sdk.models.User;

/**
 * Exemplo de LoginFragment utilizando o VeiGest SDK.
 * 
 * Este é um exemplo de como integrar o SDK na aplicação.
 * Copie e adapte este código conforme necessário.
 */
public class LoginFragmentExample extends Fragment {

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private ProgressBar progressBar;

    public LoginFragmentExample() {
    }

    public static LoginFragmentExample newInstance() {
        return new LoginFragmentExample();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Initialize views
        etUsername = view.findViewById(R.id.et_username);
        etPassword = view.findViewById(R.id.et_password);
        btnLogin = view.findViewById(R.id.btn_login);
        // progressBar = view.findViewById(R.id.progress_bar); // Adicione ao layout se quiser

        // Verificar se já está logado
        if (VeiGestSDK.isInitialized() && VeiGestSDK.getInstance().isAuthenticated()) {
            // Já está logado, ir para Dashboard
            navigateToDashboard();
            return view;
        }

        // Setup login button
        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Validação básica
            if (username.isEmpty()) {
                etUsername.setError("Username é obrigatório");
                etUsername.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                etPassword.setError("Password é obrigatória");
                etPassword.requestFocus();
                return;
            }

            performLogin(username, password);
        });

        return view;
    }

    /**
     * Executa o login usando o VeiGest SDK.
     */
    private void performLogin(String username, String password) {
        // Mostrar loading
        setLoading(true);

        // Usar o SDK para fazer login
        VeiGestSDK.getInstance().auth().login(username, password, new VeiGestCallback<LoginResponse>() {
            @Override
            public void onSuccess(@NonNull LoginResponse response) {
                setLoading(false);

                // Obter dados do utilizador
                User user = response.getUser();
                String welcomeMessage = user != null 
                    ? "Bem-vindo, " + user.getNome() + "!"
                    : "Login bem-sucedido!";

                Toast.makeText(getContext(), welcomeMessage, Toast.LENGTH_SHORT).show();

                // Navegar para o Dashboard
                navigateToDashboard();
            }

            @Override
            public void onError(@NonNull VeiGestException error) {
                setLoading(false);

                // Tratar diferentes tipos de erro
                String errorMessage;
                
                switch (error.getType()) {
                    case AUTHENTICATION:
                        errorMessage = "Credenciais inválidas. Verifique o username e password.";
                        break;
                    case NETWORK:
                        errorMessage = "Sem conexão com a internet. Verifique sua conexão.";
                        break;
                    case SERVER:
                        errorMessage = "Erro no servidor. Tente novamente mais tarde.";
                        break;
                    default:
                        errorMessage = error.getMessage();
                        break;
                }

                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Mostra/esconde loading.
     */
    private void setLoading(boolean loading) {
        btnLogin.setEnabled(!loading);
        etUsername.setEnabled(!loading);
        etPassword.setEnabled(!loading);
        
        if (progressBar != null) {
            progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        }
        
        btnLogin.setText(loading ? "A entrar..." : "Entrar");
    }

    /**
     * Navega para o Dashboard.
     */
    private void navigateToDashboard() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).loadDashboard();
        }
    }
}
