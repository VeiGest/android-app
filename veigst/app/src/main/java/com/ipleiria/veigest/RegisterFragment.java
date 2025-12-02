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
import android.widget.TextView;
import android.widget.Toast;

public class RegisterFragment extends Fragment {

    EditText etUser, etPass, etConfirm;
    Button btnCreate;
    TextView tvBack;

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

        btnCreate.setOnClickListener(v -> registerUser());
        tvBack.setOnClickListener(v -> returnToLogin());
    }

    private void registerUser() {

        String user = etUser.getText().toString().trim();
        String pass = etPass.getText().toString().trim();
        String confirm = etConfirm.getText().toString().trim();

        if (user.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
            Toast.makeText(getContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pass.equals(confirm)) {
            Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(getContext(), "Account created!", Toast.LENGTH_SHORT).show();

        returnToLogin();
    }

    private void returnToLogin() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new LoginFragment())
                .addToBackStack(null)
                .commit();
    }
}