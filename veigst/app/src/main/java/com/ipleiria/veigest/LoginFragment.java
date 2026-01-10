package com.ipleiria.veigest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.veigest.sdk.SingletonVeiGest;
import com.veigest.sdk.listeners.LoginListener;
import com.veigest.sdk.models.User;

/**
 * Fragment de Login
 * Permite ao utilizador autenticar-se na aplicação usando o VeiGest SDK (Singleton com Volley)
 */
public class LoginFragment extends Fragment implements LoginListener {

    private static final String TAG = "LoginFragment";

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private CheckBox cbRemember;
    private TextView tvNoAccount;
    private ProgressBar progressBar;
    
    private SingletonVeiGest singleton;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Obter instância do Singleton
        singleton = SingletonVeiGest.getInstance(requireContext());
        // Registar este fragment como listener de login
        singleton.setLoginListener(this);
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
        cbRemember = view.findViewById(R.id.cbRemember);
        tvNoAccount = view.findViewById(R.id.tvNoAccount);
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

            // Executar login com Singleton
            performLogin(username, password);
        });

        // Setup navigation to register
        tvNoAccount.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).loadRegisterFragment();
            }
        });

        return view;
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Remover listener ao destruir a view
        singleton.setLoginListener(null);
    }

    /**
     * Executa o login usando o VeiGest Singleton
     */
    private void performLogin(String email, String password) {
        // Mostrar loading
        setLoading(true);
        
        Log.d(TAG, "Tentando login para: " + email);
        
        // Usar o Singleton para fazer login (resposta via listener)
        singleton.loginAPI(email, password);
    }
    
    // ========== Implementação do LoginListener ==========
    
    @Override
    public void onValidateLogin(String token, User user) {
        if (getActivity() == null) return;

        getActivity().runOnUiThread(() -> {
            setLoading(false);

            String name = user != null ? user.getUsername() : "";

            Log.d(TAG, "Login bem-sucedido! Utilizador: " + name);
            Toast.makeText(getContext(), "Bem-vindo, " + name + "!", Toast.LENGTH_SHORT).show();

            // Navegar para o Dashboard
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).loadDashboard();
            }
        });
    }
    
    @Override
    public void onLoginError(String errorMessage) {
        if (getActivity() == null) return;

        getActivity().runOnUiThread(() -> {
            setLoading(false);

            Log.e(TAG, "Erro no login: " + errorMessage);

            String displayMessage;
            if (errorMessage.contains("401") || errorMessage.toLowerCase().contains("unauthorized")) {
                displayMessage = "Email ou password incorretos";
            } else if (errorMessage.toLowerCase().contains("network") || 
                       errorMessage.toLowerCase().contains("conexão")) {
                displayMessage = "Erro de conexão. Verifique a sua internet.";
            } else {
                displayMessage = "Erro no login: " + errorMessage;
            }

            Toast.makeText(getContext(), displayMessage, Toast.LENGTH_LONG).show();
        });
    }

    /**
     * Mostra ou esconde o indicador de loading
     */
    private void setLoading(boolean loading) {
        if (progressBar != null) {
            progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        }
        if (btnLogin != null) {
            btnLogin.setEnabled(!loading);
        }
        if (etUsername != null) {
            etUsername.setEnabled(!loading);
        }
        if (etPassword != null) {
            etPassword.setEnabled(!loading);
        }
    }
}
