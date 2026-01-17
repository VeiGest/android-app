package com.ipleiria.veigest;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.appcompat.app.AlertDialog;
import android.widget.Button;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;

import com.ipleiria.veigest.adapters.RouteAdapter;
import com.veigest.sdk.SingletonVeiGest;
import com.veigest.sdk.listeners.RotasListener;
import com.veigest.sdk.models.Route;

import java.util.ArrayList;

/**
 * Routes Fragment - Gerenciamento de Rotas
 * Exibe lista de rotas obtidas da API via SDK.
 */
public class RoutesFragment extends Fragment
        implements RotasListener, com.ipleiria.veigest.adapters.RouteAdapter.OnRouteActionListener {

    private static final String TAG = "RoutesFragment";

    private RecyclerView recyclerView;
    private RouteAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private TextView tvTitle;
    private FloatingActionButton fabAddRoute;

    private SingletonVeiGest singleton;

    public RoutesFragment() {
        // Required empty public constructor
    }

    public static RoutesFragment newInstance() {
        return new RoutesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obter instância do Singleton
        singleton = SingletonVeiGest.getInstance(requireContext());
        singleton.setRotasListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routes, container, false);

        initializeViews(view);
        setupRecyclerView();
        setupListeners();

        // Carregar dados
        loadRoutes();

        return view;
    }

    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.rv_routes);
        swipeRefresh = view.findViewById(R.id.swipe_refresh);
        progressBar = view.findViewById(R.id.progress_bar);
        tvEmpty = view.findViewById(R.id.tv_empty);
        fabAddRoute = view.findViewById(R.id.fab_add_route);
        // tvTitle = view.findViewById(R.id.tv_routes_title);

        setupHeader(view);
    }

    private void setupHeader(View view) {
        View btnMenu = view.findViewById(R.id.btn_menu_global);
        TextView tvTitle = view.findViewById(R.id.tv_header_title);
        if (tvTitle != null) {
            tvTitle.setText(R.string.routes_title);
        }
        if (btnMenu != null) {
            btnMenu.setOnClickListener(v -> {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).openDrawer();
                }
            });
        }
    }

    private void setupRecyclerView() {
        adapter = new RouteAdapter(getContext(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Swipe to Delete
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                    RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                Route route = adapter.getItem(position);

                new AlertDialog.Builder(getContext())
                        .setTitle("Remover Rota")
                        .setMessage("Tem a certeza que deseja remover esta rota?")
                        .setPositiveButton("Sim", (dialog, which) -> {
                            // Remover via API
                            singleton.removerRotaAPI(route.getId());
                            // Atualização otimista
                            adapter.removeRoute(position);
                            updateEmptyState();
                        })
                        .setNegativeButton("Não", (dialog, which) -> {
                            adapter.notifyItemChanged(position);
                        })
                        .show();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void setupListeners() {
        swipeRefresh.setOnRefreshListener(this::loadRoutes);
        swipeRefresh.setColorSchemeResources(R.color.Purple);

        if (fabAddRoute != null) {
            fabAddRoute.setOnClickListener(v -> showRouteDialog(null));
        }
    }

    private void showRouteDialog(Route routeToEdit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.dialog_route_form, null);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvTitle = view.findViewById(R.id.tv_dialog_title);
        TextInputEditText etStart = view.findViewById(R.id.et_start_location);
        TextInputEditText etEnd = view.findViewById(R.id.et_end_location);
        TextInputEditText etDate = view.findViewById(R.id.et_date);
        TextInputEditText etVehicleId = view.findViewById(R.id.et_vehicle_id);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        Button btnSave = view.findViewById(R.id.btn_save);

        if (routeToEdit != null) {
            tvTitle.setText("Editar Rota");
            etStart.setText(routeToEdit.getStartLocation());
            etEnd.setText(routeToEdit.getEndLocation());
            etDate.setText(routeToEdit.getStartTime()); // Simplificado
            etVehicleId.setText(String.valueOf(routeToEdit.getVehicleId()));
        } else {
            tvTitle.setText("Nova Rota");
        }

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnSave.setOnClickListener(v -> {
            String start = etStart.getText().toString();
            String end = etEnd.getText().toString();
            String date = etDate.getText().toString();
            String vId = etVehicleId.getText().toString();

            if (start.isEmpty() || end.isEmpty()) {
                Toast.makeText(getContext(), "Origem e Destino são obrigatórios", Toast.LENGTH_SHORT).show();
                return;
            }

            int vehicleId = 0;
            try {
                vehicleId = Integer.parseInt(vId);
            } catch (NumberFormatException e) {
            }

            Route route = routeToEdit != null ? routeToEdit : new Route();
            route.setStartLocation(start);
            route.setEndLocation(end);
            route.setStartTime(date);
            route.setVehicleId(vehicleId);
            route.setDriverId(singleton.getUserId()); // Atribuir ao user atual
            route.setStatus("scheduled");

            if (routeToEdit != null) {
                singleton.editarRotaAPI(route);
            } else {
                singleton.adicionarRotaAPI(route);
            }
            dialog.dismiss();
            showLoading(true);
        });

        dialog.show();
    }

    private void loadRoutes() {
        showLoading(true);

        // Primeiro tenta dados locais
        ArrayList<Route> localRoutes = singleton.getRotas();
        if (!localRoutes.isEmpty()) {
            adapter.setRoutes(localRoutes);
            updateEmptyState();
        }

        // Busca da API
        singleton.getAllRotasAPI();
    }

    private void showLoading(boolean show) {
        if (progressBar != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    private void updateEmptyState() {
        if (tvEmpty != null) {
            tvEmpty.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
        }
    }

    // ==================== SDK Listener Implementation ====================

    @Override
    public void onRefreshListaRotas(ArrayList<Route> listaRotas) {
        if (getActivity() == null)
            return;

        getActivity().runOnUiThread(() -> {
            // Stop loading animation immediately
            if (swipeRefresh != null) {
                swipeRefresh.setRefreshing(false);
            }
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }

            if (listaRotas != null) {
                adapter.setRoutes(listaRotas);
            } else {
                // Even if null, we stop loading. maybe show error
            }

            updateEmptyState();
            Log.d(TAG, "Lista de rotas atualizada: " + (listaRotas != null ? listaRotas.size() : "null"));
        });
    }

    // ==================== Adapter Listener Implementation ====================

    @Override
    public void onRouteClick(Route route) {
        String msg = "Rota: " + route.getStartLocation() + " -> " + route.getEndLocation();
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRouteComplete(Route route) {
        new AlertDialog.Builder(getContext())
                .setTitle("Concluir Rota")
                .setMessage("Deseja marcar esta rota como concluída?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    // Atualizar status localmente e na API
                    route.setStatus("completed");
                    // Se houver método de update na API:
                    singleton.editarRotaAPI(route);

                    Toast.makeText(getContext(), "Rota concluída!", Toast.LENGTH_SHORT).show();
                    loadRoutes(); // Recarregar para atualizar lista
                })
                .setNegativeButton("Não", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        singleton.setRotasListener(null);
    }
}
