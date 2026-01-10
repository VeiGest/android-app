package com.veigest.sdk.listeners;

import com.veigest.sdk.models.User;

/**
 * Listener para operações de registro de utilizador.
 * 
 * Implementar este listener para receber callbacks das operações de registro:
 * - onRegisterSuccess: Chamado quando o registro é bem-sucedido
 * - onRegisterError: Chamado quando ocorre erro no registro
 * 
 * Exemplo de uso:
 * <pre>
 * public class RegisterFragment extends Fragment implements RegisterListener {
 *     
 *     @Override
 *     public void onCreate(Bundle savedInstanceState) {
 *         super.onCreate(savedInstanceState);
 *         SingletonVeiGest.getInstance(requireContext()).setRegisterListener(this);
 *     }
 *     
 *     @Override
 *     public void onRegisterSuccess(User user) {
 *         // Registro bem-sucedido, navegar para login ou dashboard
 *     }
 *     
 *     @Override
 *     public void onRegisterError(String mensagem) {
 *         // Mostrar erro ao utilizador
 *     }
 * }
 * </pre>
 */
public interface RegisterListener {
    
    /**
     * Chamado quando o registro é realizado com sucesso.
     * 
     * @param user Objeto User com dados do utilizador registrado (pode conter id, username, email, etc.)
     */
    void onRegisterSuccess(User user);
    
    /**
     * Chamado quando ocorre um erro no registro.
     * 
     * @param mensagem Mensagem de erro descritiva (ex: "Email já registrado", "Erro de conexão")
     */
    void onRegisterError(String mensagem);
}
