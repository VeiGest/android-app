package com.ipleiria.veigest;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.veigest.sdk.SingletonVeiGest;
import com.veigest.sdk.listeners.RegisterListener;
import com.veigest.sdk.models.User;

/**
 * Fragment de Registro
 * Permite ao utilizador criar uma nova conta na aplicação.
 * Utiliza o VeiGest SDK (Singleton com Volley) seguindo o padrão Listener.
 * 
 * Implementa RegisterListener para receber callbacks da operação de registro.
 */
public class RegisterFragment extends Fragment implements RegisterListener {

    private static final String TAG = "RegisterFragment";

    // Views
    private EditText etName;
    private EditText etEmail;
    private EditText etUsername;
    private EditText etPhone;
    private EditText etPassword;
    private EditText etPasswordConfirm;
    private Button btnRegister;
    private TextView tvBackToLogin;
    private ProgressBar progressBar;

    // SDK Singleton
    private SingletonVeiGest singleton;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Obter instância do Singleton
        singleton = SingletonVeiGest.getInstance(requireContext());
        
        // Registar este fragment como listener de registro
        singleton.setRegisterListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        // Initialize views
        etName = view.findViewById(R.id.et_name);
        etEmail = view.findViewById(R.id.et_email);
        etUsername = view.findViewById(R.id.et_username);
        etPhone = view.findViewById(R.id.et_phone);
        etPassword = view.findViewById(R.id.et_password);
        etPasswordConfirm = view.findViewById(R.id.et_password_confirm);
        btnRegister = view.findViewById(R.id.btn_register);
        tvBackToLogin = view.findViewById(R.id.tv_back_to_login);
        progressBar = view.findViewById(R.id.progress_bar);

        // Setup register button
        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String username = etUsername.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String password = etPassword.getText().toString();
            String passwordConfirm = etPasswordConfirm.getText().toString();

            if (validateInput(name, email, username, phone, password, passwordConfirm)) {
                performRegister(username, email, password);
            }
        });

        // Setup back to login link
        tvBackToLogin.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Remover listener ao destruir a view
        singleton.setRegisterListener(null);
    }

    /**
     * Valida os dados de entrada
     */
    private boolean validateInput(String name, String email, String username, 
                                  String phone, String password, String passwordConfirm) {
        // Validar nome
        if (name.isEmpty()) {
            Toast.makeText(getContext(), "Por favor insira o seu nome completo", Toast.LENGTH_SHORT).show();
            etName.requestFocus();
            return false;
        }

        if (name.length() < 3) {
            Toast.makeText(getContext(), "O nome deve ter pelo menos 3 caracteres", Toast.LENGTH_SHORT).show();
            etName.requestFocus();
            return false;
        }

        // Validar email
        if (email.isEmpty()) {
            Toast.makeText(getContext(), "Por favor insira o seu email", Toast.LENGTH_SHORT).show();
            etEmail.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getContext(), "Por favor insira um email válido", Toast.LENGTH_SHORT).show();
            etEmail.requestFocus();
            return false;
        }

        // Validar username
        if (username.isEmpty()) {
            Toast.makeText(getContext(), "Por favor insira um nome de utilizador", Toast.LENGTH_SHORT).show();
            etUsername.requestFocus();
            return false;
        }

        if (username.length() < 3) {
            Toast.makeText(getContext(), "O nome de utilizador deve ter pelo menos 3 caracteres", Toast.LENGTH_SHORT).show();
            etUsername.requestFocus();
            return false;
        }

        // Validar telefone (opcional mas se preenchido deve ter formato válido)
        if (!phone.isEmpty() && phone.length() < 9) {
            Toast.makeText(getContext(), "Por favor insira um número de telefone válido", Toast.LENGTH_SHORT).show();
            etPhone.requestFocus();
            return false;
        }

        // Validar password
        if (password.isEmpty()) {
            Toast.makeText(getContext(), "Por favor insira uma password", Toast.LENGTH_SHORT).show();
            etPassword.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            Toast.makeText(getContext(), "A password deve ter pelo menos 6 caracteres", Toast.LENGTH_SHORT).show();
            etPassword.requestFocus();
            return false;
        }

        // Validar confirmação de password
        if (!password.equals(passwordConfirm)) {
            Toast.makeText(getContext(), "As passwords não coincidem", Toast.LENGTH_SHORT).show();
            etPasswordConfirm.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Executa o registro usando o VeiGest Singleton.
     * A resposta será recebida através do RegisterListener.
     */
    private void performRegister(String username, String email, String password) {
        setLoading(true);

        Log.d(TAG, "Tentando registrar utilizador: " + username);

        // Obter dados completos do formulário
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        
        // Usar o Singleton para fazer registro com todos os campos
        singleton.registerAPI(username, email, password, name, phone.isEmpty() ? null : phone);
    }
    
    // ========== Implementação do RegisterListener ==========
    
    @Override
    public void onRegisterSuccess(User user) {
        if (getActivity() == null) return;

        getActivity().runOnUiThread(() -> {
            setLoading(false);

            String username = user != null ? user.getUsername() : "";

            Log.d(TAG, "Registro bem-sucedido! Utilizador: " + username);
            
            Toast.makeText(getContext(), 
                "Conta criada com sucesso!\nPode fazer login agora.", 
                Toast.LENGTH_LONG).show();

            // Voltar para o ecrã de login
            if (getActivity() instanceof MainActivity) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
    
    @Override
    public void onRegisterError(String errorMessage) {
        if (getActivity() == null) return;

        getActivity().runOnUiThread(() -> {
            setLoading(false);

            Log.e(TAG, "Erro no registro: " + errorMessage);

            String displayMessage;
            if (errorMessage.toLowerCase().contains("email")) {
                displayMessage = "Este email já está registrado";
            } else if (errorMessage.toLowerCase().contains("username") || 
                       errorMessage.toLowerCase().contains("utilizador")) {
                displayMessage = "Este nome de utilizador já existe";
            } else if (errorMessage.toLowerCase().contains("permissão") || 
                       errorMessage.toLowerCase().contains("permission") ||
                       errorMessage.contains("401") || 
                       errorMessage.contains("403")) {
                displayMessage = "Sem permissão para criar conta.\nContacte o administrador.";
            } else if (errorMessage.toLowerCase().contains("network") || 
                       errorMessage.toLowerCase().contains("conexão")) {
                displayMessage = "Erro de conexão. Verifique a sua internet.";
            } else {
                displayMessage = "Erro no registro: " + errorMessage;
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
        if (btnRegister != null) {
            btnRegister.setEnabled(!loading);
        }
        // Desabilitar campos durante loading
        if (etName != null) etName.setEnabled(!loading);
        if (etEmail != null) etEmail.setEnabled(!loading);
        if (etUsername != null) etUsername.setEnabled(!loading);
        if (etPhone != null) etPhone.setEnabled(!loading);
        if (etPassword != null) etPassword.setEnabled(!loading);
        if (etPasswordConfirm != null) etPasswordConfirm.setEnabled(!loading);
    }
}
