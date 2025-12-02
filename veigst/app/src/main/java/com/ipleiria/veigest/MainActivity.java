package com.ipleiria.veigest;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Só adiciona o fragment se for a primeira criação
        if (savedInstanceState == null) {
            // Inicia com o LoginFragment
            // TODO: Verificar se o utilizador já tem sessão ativa
            // Se sim, carregar DashboardFragment diretamente
            loadFragment(new LoginFragment());
        }
    }

    /**
     * Carrega um fragment no container principal
     * @param fragment Fragment a ser carregado
     */
    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    /**
     * Carrega o Dashboard após login bem-sucedido
     * Este método deve ser chamado pelo LoginFragment após autenticação
     */
    public void loadDashboard() {
        loadFragment(new DashboardFragment());
    }
}
