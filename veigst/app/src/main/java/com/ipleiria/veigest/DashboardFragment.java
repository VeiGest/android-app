package com.ipleiria.veigest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.veigest.sdk.SingletonVeiGest;
import com.veigest.sdk.listeners.RotasListener;
import com.veigest.sdk.listeners.VeiculosListener;
import com.veigest.sdk.models.Route;
import com.veigest.sdk.models.User;
import com.veigest.sdk.models.Vehicle;

import java.util.ArrayList;

/**
 * Dashboard Fragment - Painel principal do condutor
 * Exibe informações sobre rotas ativas, veículo atual, documentação e ações
 * rápidas
 * Utiliza o VeiGest SDK (Singleton com Volley)
 */
public class DashboardFragment extends Fragment implements VeiculosListener, RotasListener {

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

    // Singleton
    private SingletonVeiGest singleton;

    // Listener de documentos
    private com.veigest.sdk.listeners.DocumentosListener documentosListener;

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

        // Obter instância do Singleton
        singleton = SingletonVeiGest.getInstance(requireContext());

        // Registar listeners
        singleton.setVeiculosListener(this);
        singleton.setRotasListener(this);

        // Listener de documentos
        documentosListener = listaDocumentos -> {
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(() -> atualizarDocumentacao(listaDocumentos));
        };
        singleton.setDocumentosListener(documentosListener);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Remover listeners ao destruir a view
        singleton.setVeiculosListener(null);
        singleton.setRotasListener(null);
        singleton.setDocumentosListener(null);
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
        });

        // Vehicle details button
        btnVehicleDetails.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Detalhes do Veículo - Funcionalidade a implementar", Toast.LENGTH_SHORT)
                    .show();
        });

        // View all documents button
        btnViewAllDocs.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Documentação - Funcionalidade a implementar", Toast.LENGTH_SHORT).show();
        });

        // Quick Actions
        cardReportIssue.setOnClickListener(v -> {
            // Ir para Reports
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).loadFragment(new ReportsFragment());
            }
        });

        cardMyRoutes.setOnClickListener(v -> {
            // Ir para Routes
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).loadFragment(new RoutesFragment());
            }
        });

        cardHistory.setOnClickListener(v -> {
            // Ir para Reports (ou historico especifico se existisse)
            if (getActivity() instanceof MainActivity) {
                // Por agora redireciona para Reports, onde tem o historico recente
                ((MainActivity) getActivity()).loadFragment(new ReportsFragment());
            }
        });

        cardSettings.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).loadFragment(new SettingsFragment());
            }
        });

        // Card Buttons
        btnViewRouteDetails.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).loadFragment(new RoutesFragment());
            }
        });

        btnVehicleDetails.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).loadFragment(new VehiclesFragment());
            }
        });

        btnViewAllDocs.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).loadFragment(new DocumentsFragment());
            }
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
     * Carrega os dados do utilizador atual usando o Singleton
     */
    private void loadUserData() {
        Log.d(TAG, "Carregando dados do utilizador...");

        // Obter utilizador atual do Singleton (armazenado após login)
        User user = singleton.getUtilizadorAtual();

        if (user != null) {
            tvDriverName.setText(user.getUsername());
            Log.d(TAG, "Utilizador: " + user.getUsername());
        } else {
            // Usar dados mock se não houver utilizador
            tvDriverName.setText("Condutor");
        }

        // Carregar veículos e rotas da API
        singleton.getAllVeiculosAPI();
        singleton.getAllRotasAPI();
        // Carregar documentos da API
        singleton.getAllDocumentosAPI();
    }

    /**
     * Carrega dados mock de documentação
     */
    private void loadMockDocumentationData() {
        // Não utilizado: agora os dados vêm da API
        tvLicenseExpiry.setText("-");
        tvLicenseStatus.setText("-");
        tvInsuranceExpiry.setText("-");
        tvInsuranceStatus.setText("-");
        tvInspectionExpiry.setText("-");
        tvInspectionStatus.setText("-");

    }

    /**
     * Atualiza os campos de documentação com dados reais da API
     */
    private void atualizarDocumentacao(java.util.List<com.veigest.sdk.models.Document> docs) {
        // Exemplo: buscar por tipo de documento
        String expiraCarta = "-", statusCarta = "-", expiraSeguro = "-", statusSeguro = "-", expiraInsp = "-",
                statusInsp = "-";
        for (com.veigest.sdk.models.Document doc : docs) {
            if (doc.getType() == null)
                continue;
            switch (doc.getType().toLowerCase()) {
                case "carta":
                case "carta_conducao":
                case "licenca":
                    expiraCarta = doc.getExpiryDate() != null ? "Expira: " + doc.getExpiryDate() : "-";
                    statusCarta = doc.getStatus() != null ? doc.getStatus() : "-";
                    break;
                case "seguro":
                    expiraSeguro = doc.getExpiryDate() != null ? "Expira: " + doc.getExpiryDate() : "-";
                    statusSeguro = doc.getStatus() != null ? doc.getStatus() : "-";
                    break;
                case "inspecao":
                case "insp":
                    expiraInsp = doc.getExpiryDate() != null ? "Expira: " + doc.getExpiryDate() : "-";
                    statusInsp = doc.getStatus() != null ? doc.getStatus() : "-";
                    break;
            }
        }
        tvLicenseExpiry.setText(expiraCarta);
        tvLicenseStatus.setText(statusCarta);
        tvInsuranceExpiry.setText(expiraSeguro);
        tvInsuranceStatus.setText(statusSeguro);
        tvInspectionExpiry.setText(expiraInsp);
        tvInspectionStatus.setText(statusInsp);
    }

    // ========== Implementação do VeiculosListener ==========

    @Override
    public void onRefreshListaVeiculos(ArrayList<Vehicle> listaVeiculos) {
        if (getActivity() == null)
            return;

        getActivity().runOnUiThread(() -> {
            Log.d(TAG, "Veículos recebidos: " + listaVeiculos.size());

            if (!listaVeiculos.isEmpty()) {
                Vehicle vehicle = listaVeiculos.get(0);
                tvVehiclePlate.setText(vehicle.getLicensePlate());
                tvVehicleModel.setText(vehicle.getBrand() + " " + vehicle.getModel());

                int km = vehicle.getMileage();
                tvVehicleKm.setText(String.format("%,d km", km).replace(",", "."));

                // Combustível - usar valor mock pois não vem da API
                tvVehicleFuel.setText("--");
            }
        });
    }

    // ========== Implementação do RotasListener ==========

    @Override
    public void onRefreshListaRotas(ArrayList<Route> listaRotas) {
        if (getActivity() == null)
            return;

        getActivity().runOnUiThread(() -> {
            Log.d(TAG, "Rotas recebidas: " + listaRotas.size());

            if (!listaRotas.isEmpty()) {
                // Procurar rota ativa (in_progress)
                Route activeRoute = null;
                for (Route route : listaRotas) {
                    if (route.isInProgress()) {
                        activeRoute = route;
                        break;
                    }
                }

                if (activeRoute == null && !listaRotas.isEmpty()) {
                    // Usar primeira rota agendada se não houver ativa
                    for (Route route : listaRotas) {
                        if (route.isScheduled()) {
                            activeRoute = route;
                            break;
                        }
                    }
                }

                if (activeRoute != null) {
                    tvRouteOriginValue.setText(activeRoute.getStartLocation());
                    tvRouteDestinationValue.setText(activeRoute.getEndLocation());

                    // Duração
                    if (activeRoute.getDurationFormatted() != null) {
                        tvRouteEta.setText(activeRoute.getDurationFormatted());
                    } else {
                        tvRouteEta.setText("--");
                    }

                    tvRouteDistance.setText("--"); // Distância não disponível na API atual
                } else {
                    setNoActiveRoute();
                }
            } else {
                setNoActiveRoute();
            }
        });
    }

    /**
     * Define estado de "sem rota ativa"
     */
    private void setNoActiveRoute() {
        tvRouteOriginValue.setText("Sem rota ativa");
        tvRouteDestinationValue.setText("-");
        tvRouteDistance.setText("-");
        tvRouteEta.setText("-");
    }

    /**
     * Executa o logout
     */
    private void performLogout() {
        // Limpar token e dados locais
        singleton.clearAuth();

        Toast.makeText(getContext(), "Sessão terminada", Toast.LENGTH_SHORT).show();

        // Navegar para login
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).navigateToLogin();
        }
    }
}
