package com.ipleiria.veigest;

import android.os.Bundle;
import android.util.Log;
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
import com.veigest.sdk.SingletonVeiGest;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private boolean isLoggedIn = false;
    private SingletonVeiGest singleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obter instância do Singleton
        singleton = SingletonVeiGest.getInstance(getApplicationContext());

        // Inicializar DrawerLayout e NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Desabilitar o drawer inicialmente (até fazer login)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        // Só adiciona o fragment se for a primeira criação
        if (savedInstanceState == null) {
            // Verificar se o utilizador já tem sessão ativa
            if (singleton.isAuthenticated()) {
                Log.d("MainActivity", "Sessão já autenticada, carregando Dashboard");
                loadDashboard();
            } else {
                Log.d("MainActivity", "Sem sessão, carregando LoginFragment");
                loadFragment(new LoginFragment());
            }
        }
    }

    /**
     * Carrega um fragment no container principal
     * 
     * @param fragment Fragment a ser carregado
     */
    public void loadFragment(Fragment fragment) {
        Log.d("MainActivity", "Carregando fragment: " + fragment.getClass().getSimpleName());
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
        Log.d("MainActivity", "loadDashboard chamado");
        isLoggedIn = true;
        // Habilitar o drawer após login
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        loadFragment(new DashboardFragment());
        // Selecionar o item Dashboard no menu
        navigationView.setCheckedItem(R.id.nav_dashboard);
    }

    /**
     * Carrega o fragment de registro
     * Chamado pelo LoginFragment quando o utilizador clica em "Criar Conta"
     */
    public void loadRegisterFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new RegisterFragment());
        fragmentTransaction.addToBackStack(null); // Permite voltar ao login com back button
        fragmentTransaction.commit();
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
        } else if (itemId == R.id.nav_reports) {
            loadFragment(new ReportsFragment());
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
        // Limpar token local e voltar ao login
        singleton.clearAuth();
        Toast.makeText(MainActivity.this, "Sessão terminada com sucesso", Toast.LENGTH_SHORT).show();
        navigateToLogin();
    }

    /**
     * Navega para o ecrã de login (usado após logout)
     */
    public void navigateToLogin() {
        Log.d("MainActivity", "navigateToLogin chamado");
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
