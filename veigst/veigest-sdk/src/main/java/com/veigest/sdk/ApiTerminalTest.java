package com.veigest.sdk;

import com.veigest.sdk.listeners.RegisterListener;
import com.veigest.sdk.listeners.LoginListener;
import com.veigest.sdk.models.User;

public class ApiTerminalTest implements RegisterListener, LoginListener {
    public static void main(String[] args) {
        // Inicialização mínima para rodar fora do Android Studio
        // ATENÇÃO: O SDK depende de Android Context, então este exemplo é teórico para execução em terminal Java puro.
        // Para rodar de verdade, seria necessário adaptar o SDK para rodar em ambiente Java puro (sem Android),
        // ou rodar como teste instrumentado no Android.
        System.out.println("Iniciando teste de registro e login...");
        // Exemplo de chamada (não funcional fora do Android):
        // SingletonVeiGest sdk = SingletonVeiGest.getInstance(context);
        // sdk.setRegisterListener(new ApiTerminalTest());
        // sdk.registerAPI("testuser", "test@email.com", "123456");
    }

    @Override
    public void onRegisterSuccess(User user) {
        System.out.println("Registro OK: " + user.getUsername());
    }
    @Override
    public void onRegisterError(String msg) {
        System.err.println("Erro registro: " + msg);
    }
    @Override
    public void onValidateLogin(String token, User user) {
        System.out.println("Login OK: " + user.getUsername());
    }
    @Override
    public void onLoginError(String msg) {
        System.err.println("Erro login: " + msg);
    }
}
