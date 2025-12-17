package com.ipleiria.veigest;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class ForgotPasswordFragment extends Fragment {

    private static final String CHANNEL_ID = "recovery_channel";
    private static final int NOTIFICATION_ID = 1001;
    private static final int REQUEST_NOTIFICATION_PERMISSION = 1001;
    
    // Step 1: Username input
    private LinearLayout layoutStep1;
    private EditText etUsername;
    private EditText etEmail;
    private Button btnVerifyUsername;
    private LinearLayout layoutRecoveryOptions;
    private Spinner spinnerRecoveryMethod;
    private Button btnSendCode;
    
    // Step 2: Code verification
    private LinearLayout layoutStep2;
    private TextView tvCodeSentMessage;
    private EditText etRecoveryCode;
    private Button btnVerifyCode;
    private TextView tvResendCode;
    private TextView tvBackToStep1;
    
    // Step 3: New password
    private LinearLayout layoutStep3;
    private EditText etNewPassword;
    private EditText etConfirmPassword;
    private Button btnResetPassword;
    
    private ImageView ivBackArrow;
    
    private String currentUsername;
    private String generatedCode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        
        // Step 1
        layoutStep1 = view.findViewById(R.id.layoutStep1);
        etUsername = view.findViewById(R.id.etUsername);
        etEmail = view.findViewById(R.id.etEmail);
        btnVerifyUsername = view.findViewById(R.id.btnVerifyUsername);
        layoutRecoveryOptions = view.findViewById(R.id.layoutRecoveryOptions);
        spinnerRecoveryMethod = view.findViewById(R.id.spinnerRecoveryMethod);
        btnSendCode = view.findViewById(R.id.btnSendCode);
        
        // Setup spinner with recovery methods
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.recovery_methods, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRecoveryMethod.setAdapter(adapter);
        
        // Step 2
        layoutStep2 = view.findViewById(R.id.layoutStep2);
        tvCodeSentMessage = view.findViewById(R.id.tvCodeSentMessage);
        etRecoveryCode = view.findViewById(R.id.etRecoveryCode);
        btnVerifyCode = view.findViewById(R.id.btnVerifyCode);
        tvResendCode = view.findViewById(R.id.tvResendCode);
        tvBackToStep1 = view.findViewById(R.id.tvBackToStep1);
        
        // Step 3
        layoutStep3 = view.findViewById(R.id.layoutStep3);
        etNewPassword = view.findViewById(R.id.etNewPassword);
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        btnResetPassword = view.findViewById(R.id.btnResetPassword);
        
        ivBackArrow = view.findViewById(R.id.ivBackArrow);
        
        // Initial state
        showStep1();
        
        // Setup listeners
        btnVerifyUsername.setOnClickListener(v -> verifyUsername());
        btnSendCode.setOnClickListener(v -> sendRecoveryCode());
        btnVerifyCode.setOnClickListener(v -> verifyCode());
        btnResetPassword.setOnClickListener(v -> resetPassword());
        tvResendCode.setOnClickListener(v -> resendRecoveryCode());
        tvBackToStep1.setOnClickListener(v -> showStep1());
        ivBackArrow.setOnClickListener(v -> returnToLogin());
        
        return view;
    }
    
    private void showStep1() {
        layoutStep1.setVisibility(View.VISIBLE);
        layoutStep2.setVisibility(View.GONE);
        layoutStep3.setVisibility(View.GONE);
        layoutRecoveryOptions.setVisibility(View.GONE);
        etUsername.setEnabled(true);
        btnVerifyUsername.setVisibility(View.VISIBLE);
    }
    
    private void verifyUsername() {
        String username = etUsername.getText().toString().trim();
        
        if (username.isEmpty()) {
            Toast.makeText(getContext(), "Insira o nome de utilizador", Toast.LENGTH_SHORT).show();
            return;
        }
        
        AuthManager authManager = AuthManager.getInstance(requireContext());
        
        if (!authManager.userExists(username)) {
            Toast.makeText(getContext(), "Utilizador não encontrado", Toast.LENGTH_SHORT).show();
            return;
        }
        
        currentUsername = username;
        
        // Verificar se tem email associado
        String associatedEmail = authManager.getEmail(username);
        
        // Atualizar spinner com opções disponíveis
        ArrayAdapter<String> adapter;
        if (associatedEmail != null && !associatedEmail.isEmpty()) {
            // Tem email - mostrar ambas opções
            String[] methods = {"Notificação da App", "Email"};
            adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, methods);
            etEmail.setText(associatedEmail);
        } else {
            // Não tem email - apenas notificação
            String[] methods = {"Notificação da App"};
            adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, methods);
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRecoveryMethod.setAdapter(adapter);
        
        // Mostrar opções e desabilitar edição do username
        etUsername.setEnabled(false);
        btnVerifyUsername.setVisibility(View.GONE);
        layoutRecoveryOptions.setVisibility(View.VISIBLE);
        
        Toast.makeText(getContext(), "Utilizador verificado. Escolha o método de recuperação.", Toast.LENGTH_SHORT).show();
    }
    
    private void showStep2() {
        layoutStep1.setVisibility(View.GONE);
        layoutStep2.setVisibility(View.VISIBLE);
        layoutStep3.setVisibility(View.GONE);
    }
    
    private void showStep3() {
        layoutStep1.setVisibility(View.GONE);
        layoutStep2.setVisibility(View.GONE);
        layoutStep3.setVisibility(View.VISIBLE);
    }
    
    private void sendRecoveryCode() {
        if (currentUsername == null || currentUsername.isEmpty()) {
            Toast.makeText(getContext(), "Erro: utilizador não verificado", Toast.LENGTH_SHORT).show();
            return;
        }
        
        AuthManager authManager = AuthManager.getInstance(requireContext());
        
        // Gerar código
        generatedCode = authManager.generateRecoveryCode(currentUsername);
        
        if (generatedCode == null) {
            Toast.makeText(getContext(), "Erro ao gerar código", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Obter método selecionado
        String selectedMethod = spinnerRecoveryMethod.getSelectedItem().toString();
        
        // Enviar código
        if (selectedMethod.contains("Notificação") || selectedMethod.contains("Notifica")) {
            if (checkNotificationPermission()) {
                sendCodeViaNotification(generatedCode);
                tvCodeSentMessage.setText("Código enviado por notificação! Verifique as notificações.");
                showStep2();
            } else {
                requestNotificationPermission();
            }
        } else {
            // Email - TODO: Implementar envio de email via backend
            String email = etEmail.getText().toString().trim();
            tvCodeSentMessage.setText("Código de recuperação: " + generatedCode + "\n\nEnvio de email será implementado.");
            Toast.makeText(getContext(), "Funcionalidade de email em desenvolvimento. Use o código acima.", Toast.LENGTH_LONG).show();
            showStep2();
        }
    }
    
    private void sendCodeViaNotification(String code) {
        createNotificationChannel();
        
        NotificationManager notificationManager = 
            (NotificationManager) requireContext().getSystemService(requireContext().NOTIFICATION_SERVICE);
        
        Intent intent = new Intent(requireContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
            requireContext(), 
            0, 
            intent, 
            PendingIntent.FLAG_IMMUTABLE
        );
        
        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_veigest)
            .setContentTitle("Código de Recuperação - VeiGest")
            .setContentText("A sua chave de recuperação é: " + code)
            .setStyle(new NotificationCompat.BigTextStyle()
                .bigText("A sua chave de recuperação é: " + code + "\n\nEste código expira em 3 minutos."))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true);
        
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
    
    // TODO: Implementar envio de email via backend API
    // private void sendCodeViaEmail(String code, String email) {
    //     // Será implementado com chamada ao backend que envia email via serviço como SendGrid, AWS SES, etc.
    //     // Backend recebe: username, email, code
    //     // Backend envia email com template profissional
    // }
    
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Recuperação de Password";
            String description = "Notificações de código de recuperação";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            
            NotificationManager notificationManager = 
                requireContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    
    private void verifyCode() {
        String code = etRecoveryCode.getText().toString().trim();
        
        if (code.isEmpty()) {
            Toast.makeText(getContext(), "Insira o código", Toast.LENGTH_SHORT).show();
            return;
        }
        
        AuthManager authManager = AuthManager.getInstance(requireContext());
        
        if (authManager.verifyRecoveryCode(currentUsername, code)) {
            Toast.makeText(getContext(), "Código válido!", Toast.LENGTH_SHORT).show();
            showStep3();
        } else {
            Toast.makeText(getContext(), "Código inválido ou expirado", Toast.LENGTH_LONG).show();
        }
    }
    
    private void resetPassword() {
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        
        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(getContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(getContext(), "As passwords não coincidem", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (newPassword.length() < 4) {
            Toast.makeText(getContext(), "A password deve ter pelo menos 4 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }
        
        AuthManager authManager = AuthManager.getInstance(requireContext());
        String code = etRecoveryCode.getText().toString().trim();
        
        if (authManager.resetPassword(currentUsername, code, newPassword)) {
            Toast.makeText(getContext(), "Password redefinida com sucesso!", Toast.LENGTH_LONG).show();
            returnToLogin();
        } else {
            Toast.makeText(getContext(), "Erro ao redefinir password", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void resendRecoveryCode() {
        if (currentUsername == null) {
            Toast.makeText(getContext(), "Erro: utilizador não definido", Toast.LENGTH_SHORT).show();
            return;
        }
        
        AuthManager authManager = AuthManager.getInstance(requireContext());
        generatedCode = authManager.generateRecoveryCode(currentUsername);
        
        if (generatedCode == null) {
            Toast.makeText(getContext(), "Erro ao gerar novo código", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Obter método selecionado
        String selectedMethod = spinnerRecoveryMethod.getSelectedItem().toString();
        
        if (selectedMethod.contains("Notificação") || selectedMethod.contains("Notifica")) {
            sendCodeViaNotification(generatedCode);
            Toast.makeText(getContext(), "Novo código enviado por notificação", Toast.LENGTH_SHORT).show();
        } else {
            // Email - TODO: Implementar
            tvCodeSentMessage.setText("Código de recuperação: " + generatedCode + "\n\nEnvio de email será implementado.");
            Toast.makeText(getContext(), "Novo código gerado: " + generatedCode, Toast.LENGTH_LONG).show();
        }
    }
    
    private boolean checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(requireContext(), 
                Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }
    
    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(requireActivity(), 
                new String[]{Manifest.permission.POST_NOTIFICATIONS}, 
                REQUEST_NOTIFICATION_PERMISSION);
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permissão de notificação concedida. Tente novamente.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Permissão de notificação negada", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private void returnToLogin() {
        requireActivity()
            .getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.fragment_container, new LoginFragment())
            .commit();
    }
}
