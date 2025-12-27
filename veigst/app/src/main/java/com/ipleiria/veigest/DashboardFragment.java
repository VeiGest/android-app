package com.ipleiria.veigest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.veigest.sdk.VeiGestCallback;
import com.veigest.sdk.VeiGestException;
import com.veigest.sdk.VeiGestSDK;
import com.veigest.sdk.models.Route;
import com.veigest.sdk.models.User;
import com.veigest.sdk.models.Vehicle;

import java.util.List;

/**
 * Dashboard Fragment - Painel principal do condutor
 * Exibe informações sobre rotas ativas, veículo atual, documentação e ações rápidas
 * Utiliza o VeiGest SDK para carregar dados da API
 */
public class DashboardFragment extends Fragment {

    private static final String TAG = "DashboardFragment";

    // Header
    private TextView tvWelcome;
    private TextView tvDriverName;
    private ImageView btnMenu;
    private ImageView btnLogout;

    // Active Route Card
    private MaterialCardView cardActiveRoute;
    private TextView tvRouteOriginValue;
    private TextView tvRouteDestinationValue;
    private TextView tvRouteDistance;
    private TextView tvRouteEta;
    private Button btnViewRouteDetails;

    // Current Vehicle Card
    private MaterialCardView cardCurrentVehicle;
    private TextView tvVehiclePlate;
    private TextView tvVehicleModel;
    private TextView tvVehicleKm;
    private TextView tvVehicleFuel;
    private Button btnVehicleDetails;

    // Documentation Card
    private MaterialCardView cardDocumentation;
    private TextView tvLicenseExpiry;
    private TextView tvLicenseStatus;
    private TextView tvInsuranceExpiry;
    private TextView tvInsuranceStatus;
    private TextView tvInspectionExpiry;
    private TextView tvInspectionStatus;
    private Button btnViewAllDocs;

    // Quick Actions
    private MaterialCardView cardReportIssue;
    private MaterialCardView cardMyRoutes;
    private MaterialCardView cardHistory;
    private MaterialCardView cardSettings;
    
    // SDK
    private VeiGestSDK sdk;

    public DashboardFragment() {
        // Required empty public constructor
    }

    public static DashboardFragment newInstance() {
        DashboardFragment fragment = new DashboardFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Obter instância do SDK
        sdk = VeiGestApplication.getSDK();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Initialize views
        initializeViews(view);

        // Setup listeners
        setupListeners();

        // Carregar dados do utilizador atual
        loadUserData();

        return view;
    }

    private void initializeViews(View view) {
        // Header
        tvWelcome = view.findViewById(R.id.tv_welcome);
        tvDriverName = view.findViewById(R.id.tv_driver_name);
        btnMenu = view.findViewById(R.id.btn_menu);
        btnLogout = view.findViewById(R.id.btn_logout);

        // Active Route Card
        cardActiveRoute = view.findViewById(R.id.card_active_route);
        tvRouteOriginValue = view.findViewById(R.id.tv_route_origin_value);
        tvRouteDestinationValue = view.findViewById(R.id.tv_route_destination_value);
        tvRouteDistance = view.findViewById(R.id.tv_route_distance);
        tvRouteEta = view.findViewById(R.id.tv_route_eta);
        btnViewRouteDetails = view.findViewById(R.id.btn_view_route_details);

        // Current Vehicle Card
        cardCurrentVehicle = view.findViewById(R.id.card_current_vehicle);
        tvVehiclePlate = view.findViewById(R.id.tv_vehicle_plate);
        tvVehicleModel = view.findViewById(R.id.tv_vehicle_model);
        tvVehicleKm = view.findViewById(R.id.tv_vehicle_km);
        tvVehicleFuel = view.findViewById(R.id.tv_vehicle_fuel);
        btnVehicleDetails = view.findViewById(R.id.btn_vehicle_details);

        // Documentation Card
        cardDocumentation = view.findViewById(R.id.card_documentation);
        tvLicenseExpiry = view.findViewById(R.id.tv_license_expiry);
        tvLicenseStatus = view.findViewById(R.id.tv_license_status);
        tvInsuranceExpiry = view.findViewById(R.id.tv_insurance_expiry);
        tvInsuranceStatus = view.findViewById(R.id.tv_insurance_status);
        tvInspectionExpiry = view.findViewById(R.id.tv_inspection_expiry);
        tvInspectionStatus = view.findViewById(R.id.tv_inspection_status);
        btnViewAllDocs = view.findViewById(R.id.btn_view_all_docs);

        // Quick Actions
        cardReportIssue = view.findViewById(R.id.card_report_issue);
        cardMyRoutes = view.findViewById(R.id.card_my_routes);
        cardHistory = view.findViewById(R.id.card_history);
        cardSettings = view.findViewById(R.id.card_settings);
    }

    private void setupListeners() {
        // Menu button - opens navigation drawer
        btnMenu.setOnClickListener(v -> {
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.openDrawer();
            }
        });

        // Logout button
        btnLogout.setOnClickListener(v -> {
            performLogout();
        });

        // Route details button
        btnViewRouteDetails.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Detalhes da Rota - Funcionalidade a implementar", Toast.LENGTH_SHORT).show();
            // TODO: Navegar para RouteDetailsFragment
        });

        // Vehicle details button
        btnVehicleDetails.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Detalhes do Veículo - Funcionalidade a implementar", Toast.LENGTH_SHORT).show();
            // TODO: Navegar para VehicleDetailsFragment
        });

        // View all documents button
        btnViewAllDocs.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Documentação - Funcionalidade a implementar", Toast.LENGTH_SHORT).show();
            // TODO: Navegar para DocumentationFragment
        });

        // Quick Actions
        cardReportIssue.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Reportar Problema - Funcionalidade a implementar", Toast.LENGTH_SHORT).show();
            // TODO: Navegar para ReportIssueFragment
        });

        cardMyRoutes.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Minhas Rotas - Funcionalidade a implementar", Toast.LENGTH_SHORT).show();
            // TODO: Navegar para MyRoutesFragment
        });

        cardHistory.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Histórico - Funcionalidade a implementar", Toast.LENGTH_SHORT).show();
            // TODO: Navegar para HistoryFragment
        });

        cardSettings.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Definições - Funcionalidade a implementar", Toast.LENGTH_SHORT).show();
            // TODO: Navegar para SettingsFragment
        });
    }

    private void loadMockData() {
        // Mock driver data
        tvDriverName.setText("João Silva");

        // Mock active route data
        tvRouteOriginValue.setText("Leiria, Portugal");
        tvRouteDestinationValue.setText("Lisboa, Portugal");
        tvRouteDistance.setText("145 km");
        tvRouteEta.setText("1h 45min");

        // Mock vehicle data
        tvVehiclePlate.setText("AA-12-BB");
        tvVehicleModel.setText("Mercedes Actros");
        tvVehicleKm.setText("125.340 km");
        tvVehicleFuel.setText("75%");

        // Mock documentation data
        tvLicenseExpiry.setText("Expira: 15/08/2026");
        tvLicenseStatus.setText("Válida");
        
        tvInsuranceExpiry.setText("Expira: 30/06/2025");
        tvInsuranceStatus.setText("Válido");
        
        tvInspectionExpiry.setText("Expira: 22/03/2025");
        tvInspectionStatus.setText("Válida");
    }
    
    /**
     * Carrega os dados do utilizador atual usando o SDK
     */
    private void loadUserData() {
        Log.d(TAG, "Carregando dados do utilizador...");
        
        // Carregar dados do utilizador atual
        sdk.users().getCurrentUser(new VeiGestCallback<User>() {
            @Override
            public void onSuccess(@NonNull User user) {
                if (getActivity() == null) return;
                
                getActivity().runOnUiThread(() -> {
                    Log.d(TAG, "Utilizador carregado: " + user.getNome());
                    tvDriverName.setText(user.getNome());
                    
                    // Carregar rotas e veículos do utilizador
                    loadUserRoutes(user.getId());
                    loadUserVehicles(user.getId());
                });
            }

            @Override
            public void onError(@NonNull VeiGestException error) {
                if (getActivity() == null) return;
                
                getActivity().runOnUiThread(() -> {
                    Log.e(TAG, "Erro ao carregar utilizador: " + error.getMessage());
                    // Carregar dados mock em caso de erro
                    loadMockData();
                });
            }
        });
    }
    
    /**
     * Carrega as rotas do utilizador
     */
    private void loadUserRoutes(int userId) {
        sdk.routes().getActive(new VeiGestCallback<List<Route>>() {
            @Override
            public void onSuccess(@NonNull List<Route> routes) {
                if (getActivity() == null) return;
                
                getActivity().runOnUiThread(() -> {
                    if (!routes.isEmpty()) {
                        Route activeRoute = routes.get(0);
                        tvRouteOriginValue.setText(activeRoute.getOrigem());
                        tvRouteDestinationValue.setText(activeRoute.getDestino());
                        
                        // Calcular distância se disponível
                        int kmInicial = activeRoute.getKmInicial();
                        int kmFinal = activeRoute.getKmFinal();
                        if (kmFinal > 0) {
                            tvRouteDistance.setText((kmFinal - kmInicial) + " km");
                        }
                    } else {
                        tvRouteOriginValue.setText("Sem rota ativa");
                        tvRouteDestinationValue.setText("-");
                        tvRouteDistance.setText("-");
                        tvRouteEta.setText("-");
                    }
                });
            }

            @Override
            public void onError(@NonNull VeiGestException error) {
                Log.e(TAG, "Erro ao carregar rotas: " + error.getMessage());
            }
        });
    }
    
    /**
     * Carrega os veículos do utilizador
     */
    private void loadUserVehicles(int userId) {
        sdk.users().getVehicles(userId, new VeiGestCallback<List<Vehicle>>() {
            @Override
            public void onSuccess(@NonNull List<Vehicle> vehicles) {
                if (getActivity() == null) return;
                
                getActivity().runOnUiThread(() -> {
                    if (!vehicles.isEmpty()) {
                        Vehicle vehicle = vehicles.get(0);
                        tvVehiclePlate.setText(vehicle.getMatricula());
                        tvVehicleModel.setText(vehicle.getMarca() + " " + vehicle.getModelo());
                        
                        int km = vehicle.getQuilometragem();
                        tvVehicleKm.setText(String.format("%,d km", km).replace(",", "."));
                    }
                });
            }

            @Override
            public void onError(@NonNull VeiGestException error) {
                Log.e(TAG, "Erro ao carregar veículos: " + error.getMessage());
            }
        });
    }
    
    /**
     * Executa o logout usando o SDK
     */
    private void performLogout() {
        sdk.auth().logout(new VeiGestCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                if (getActivity() == null) return;
                
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Sessão terminada", Toast.LENGTH_SHORT).show();
                    
                    // Navegar para login
                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).navigateToLogin();
                    }
                });
            }

            @Override
            public void onError(@NonNull VeiGestException error) {
                if (getActivity() == null) return;
                
                getActivity().runOnUiThread(() -> {
                    Log.e(TAG, "Erro no logout: " + error.getMessage());
                    // Mesmo com erro, fazer logout local
                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).navigateToLogin();
                    }
                });
            }
        });
    }
}
