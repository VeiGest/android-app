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
import android.widget.TextView;
import android.widget.Toast;

public class RegisterFragment extends Fragment {

    EditText etUser, etPass, etConfirm;
    Button btnCreate;
    TextView tvBack;
    ImageView ivRegisterPasswordToggle;
    CheckBox cbRememberRegister;
    private boolean isRegisterPasswordVisible = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        etUser = view.findViewById(R.id.etRegisterUsername);
        etPass = view.findViewById(R.id.etRegisterPassword);
        etConfirm = view.findViewById(R.id.etRegisterPasswordConfirm);
        btnCreate = view.findViewById(R.id.btnCreateAccount);
        tvBack = view.findViewById(R.id.tvBackToLogin);
        ivRegisterPasswordToggle = view.findViewById(R.id.iv_register_password_toggle);
        cbRememberRegister = view.findViewById(R.id.cbRememberRegister);

        // Toggle for register password (NOT for confirm)
        if (ivRegisterPasswordToggle != null) {
            ivRegisterPasswordToggle.setOnClickListener(v -> toggleRegisterPasswordVisibility());
        }

        btnCreate.setOnClickListener(v -> registerUser());
        tvBack.setOnClickListener(v -> returnToLogin());
    }

    private void toggleRegisterPasswordVisibility() {
        if (etPass == null || ivRegisterPasswordToggle == null) return;

        if (isRegisterPasswordVisible) {
            etPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            ivRegisterPasswordToggle.setImageResource(R.drawable.eye_closed);
            isRegisterPasswordVisible = false;
        } else {
            etPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            ivRegisterPasswordToggle.setImageResource(R.drawable.eye_open);
            isRegisterPasswordVisible = true;
        }
        etPass.setSelection(etPass.getText().length());
    }

    private void registerUser() {

        String user = etUser.getText().toString().trim();
        String pass = etPass.getText().toString().trim();
        String confirm = etConfirm.getText().toString().trim();

        if (user.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
            Toast.makeText(getContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pass.equals(confirm)) {
            Toast.makeText(getContext(), "As passwords não coincidem", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Validação básica de password
        if (pass.length() < 4) {
            Toast.makeText(getContext(), "A password deve ter pelo menos 4 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Tentar registar o utilizador
        AuthManager authManager = AuthManager.getInstance(requireContext());
        boolean success = authManager.registerUser(user, pass);
        
        if (success) {
            // Verificar se "Manter Login" está marcado
            boolean rememberMe = cbRememberRegister != null && cbRememberRegister.isChecked();
            
            // Fazer login automático após registo
            boolean loginSuccess = authManager.login(user, pass, rememberMe);
            
            if (loginSuccess) {
                Toast.makeText(getContext(), "Conta criada com sucesso!", Toast.LENGTH_SHORT).show();
                
                // Navegar diretamente para o Dashboard
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).loadDashboard();
                }
            } else {
                // Fallback caso o login automático falhe (improvável)
                Toast.makeText(getContext(), "Conta criada! Faça login.", Toast.LENGTH_SHORT).show();
                returnToLogin();
            }
        } else {
            Toast.makeText(getContext(), "Este utilizador já existe. Escolha outro nome.", Toast.LENGTH_LONG).show();
        }
    }

    private void returnToLogin() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new LoginFragment())
                .addToBackStack(null)
                .commit();
    }
}