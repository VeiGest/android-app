package com.ipleiria.veigest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.veigest.sdk.VeiGestCallback;
import com.veigest.sdk.VeiGestException;
import com.veigest.sdk.VeiGestSDK;
import com.veigest.sdk.models.User;

/**
 * Fragment de Login
 * Permite ao utilizador autenticar-se na aplicação usando o VeiGest SDK
 */
public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private ProgressBar progressBar;
    
    private VeiGestSDK sdk;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Obter instância do SDK
        sdk = VeiGestApplication.getSDK();
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
        progressBar = view.findViewById(R.id.progress_bar);

        // Setup login button
        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Validação básica
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Por favor preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Executar login com SDK
            performLogin(username, password);
        });

        return view;
    }

    /**
     * Executa o login usando o VeiGest SDK
     */
    private void performLogin(String email, String password) {
        // Mostrar loading
        setLoading(true);
        
        Log.d(TAG, "Tentando login para: " + email);
        
        // Usar o SDK para fazer login
        sdk.auth().login(email, password, new VeiGestCallback<User>() {
            @Override
            public void onSuccess(@NonNull User user) {
                if (getActivity() == null) return;
                
                getActivity().runOnUiThread(() -> {
                    setLoading(false);
                    
                    Log.d(TAG, "Login bem-sucedido! Utilizador: " + user.getNome());
                    Toast.makeText(getContext(), 
                        "Bem-vindo, " + user.getNome() + "!", 
                        Toast.LENGTH_SHORT).show();

                    // Navegar para o Dashboard
                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).loadDashboard();
                    }
                });
            }

            @Override
            public void onError(@NonNull VeiGestException error) {
                if (getActivity() == null) return;
                
                getActivity().runOnUiThread(() -> {
                    setLoading(false);
                    
                    Log.e(TAG, "Erro no login: " + error.getMessage());
                    
                    // Tratar diferentes tipos de erro
                    String errorMessage;
                    switch (error.getErrorType()) {
                        case UNAUTHORIZED:
                            errorMessage = "Email ou password incorretos";
                            break;
                        case NETWORK_ERROR:
                            errorMessage = "Erro de conexão. Verifique a sua internet.";
                            break;
                        case VALIDATION_ERROR:
                            errorMessage = "Dados inválidos: " + error.getMessage();
                            break;
                        case SERVER_ERROR:
                            errorMessage = "Erro no servidor. Tente novamente mais tarde.";
                            break;
                        default:
                            errorMessage = "Erro: " + error.getMessage();
                    }
                    
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                });
            }
        });
    }
    
    /**
     * Ativa/desativa o estado de loading
     */
    private void setLoading(boolean loading) {
        if (progressBar != null) {
            progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        }
        btnLogin.setEnabled(!loading);
        etUsername.setEnabled(!loading);
        etPassword.setEnabled(!loading);
    }
}