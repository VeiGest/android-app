package com.ipleiria.veigest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;

/**
 * Dashboard Fragment - Painel principal do condutor
 * Exibe informações sobre rotas ativas, veículo atual, documentação e ações rápidas
 */
public class DashboardFragment extends Fragment {

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

        // Load mock data
        loadMockData();

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
            Toast.makeText(getContext(), "Logout - Funcionalidade a implementar", Toast.LENGTH_SHORT).show();
            // TODO: Implementar logout e voltar para LoginFragment
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

        // TODO: Substituir dados mockados por dados reais da API
        // Chamadas de API para:
        // - GET /drivers/{id} - Informações do condutor
        // - GET /routes?status=active&driver_id={id} - Rotas ativas
        // - GET /vehicles/{id} - Informações do veículo atual
        // - GET /documents?driver_id={id} - Documentação do condutor e veículo
    }
}
