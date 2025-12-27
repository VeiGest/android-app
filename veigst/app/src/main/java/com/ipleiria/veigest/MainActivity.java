package com.ipleiria.veigest;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.veigest.sdk.VeiGestSDK;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private boolean isLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar DrawerLayout e NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Desabilitar o drawer inicialmente (até fazer login)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        // Só adiciona o fragment se for a primeira criação
        if (savedInstanceState == null) {
            // Verificar se o utilizador já tem sessão ativa
            VeiGestSDK sdk = VeiGestApplication.getSDK();
            if (sdk.auth().isAuthenticated()) {
                // Sessão ativa - ir direto para o Dashboard
                loadDashboard();
            } else {
                // Sem sessão - mostrar login
                loadFragment(new LoginFragment());
            }
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
        isLoggedIn = true;
        // Habilitar o drawer após login
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        loadFragment(new DashboardFragment());
        // Selecionar o item Dashboard no menu
        navigationView.setCheckedItem(R.id.nav_dashboard);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_dashboard) {
            loadFragment(new DashboardFragment());
        } else if (itemId == R.id.nav_routes) {
            loadFragment(new RoutesFragment());
        } else if (itemId == R.id.nav_vehicles) {
            loadFragment(new VehiclesFragment());
        } else if (itemId == R.id.nav_documents) {
            loadFragment(new DocumentsFragment());
        } else if (itemId == R.id.nav_profile) {
            loadFragment(new ProfileFragment());
        } else if (itemId == R.id.nav_settings) {
            loadFragment(new SettingsFragment());
        } else if (itemId == R.id.nav_logout) {
            performLogout();
        }

        // Fechar o drawer após selecionar um item
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Realiza o logout do utilizador
     */
    private void performLogout() {
        navigateToLogin();
    }
    
    /**
     * Navega para o ecrã de login (usado após logout)
     */
    public void navigateToLogin() {
        isLoggedIn = false;
        // Desabilitar o drawer após logout
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        // Voltar para a tela de login
        loadFragment(new LoginFragment());
    }

    /**
     * Abre o drawer quando chamado (pode ser usado pelos fragments)
     */
    public void openDrawer() {
        if (isLoggedIn) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onBackPressed() {
        // Fechar o drawer se estiver aberto
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
