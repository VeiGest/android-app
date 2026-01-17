package com.ipleiria.veigest;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private boolean isLoggedIn = false;
    private SingletonVeiGest singleton;
    private static final String PREFS_SETTINGS = "veigest_settings";
    private static final String PREF_THEME = "theme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Restaurar tema antes do super.onCreate/setContentView
        applySavedTheme();

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

        // Populate Header
        updateNavHeader();
    }

    public void updateNavHeader() {
        View headerView = navigationView.getHeaderView(0);
        android.widget.TextView tvName = headerView.findViewById(R.id.nav_header_name);
        android.widget.TextView tvEmail = headerView.findViewById(R.id.nav_header_email);

        com.veigest.sdk.models.User user = singleton.getUtilizadorAtual();
        if (user != null) {
            if (tvName != null)
                tvName.setText(user.getUsername());
            if (tvEmail != null)
                tvEmail.setText(user.getEmail());
        }
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

    private void applySavedTheme() {
        SharedPreferences prefs = getSharedPreferences(PREFS_SETTINGS, MODE_PRIVATE);
        int theme = prefs.getInt(PREF_THEME, 0); // 0 = Sistema
        switch (theme) {
            case 1:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case 2:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Atualizar header sempre que retomar (caso perfil mude ou dados carreguem)
        updateNavHeader();
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
            logout();
        }

        // Fechar o drawer após selecionar um item
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Abre o drawer quando chamado (pode ser usado pelos fragments)
     */
    public void openDrawer() {
        if (drawerLayout != null) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    /**
     * Termina a sessão do utilizador
     */
    public void logout() {
        // Limpar autenticação no SDK
        singleton.clearAuth();

        isLoggedIn = false;

        // Fechar drawer
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(GravityCompat.START);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

        // Navegar para login
        loadFragment(new LoginFragment());

        Toast.makeText(this, "Sessão terminada", Toast.LENGTH_SHORT).show();
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
