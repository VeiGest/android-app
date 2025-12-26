package com.ipleiria.veigest.examples;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ipleiria.veigest.R;
import com.veigest.sdk.VeiGestCallback;
import com.veigest.sdk.VeiGestException;
import com.veigest.sdk.VeiGestSDK;
import com.veigest.sdk.models.Vehicle;

import java.util.ArrayList;
import java.util.List;

/**
 * Exemplo de VehiclesFragment utilizando o VeiGest SDK.
 * 
 * Este é um exemplo de como listar veículos usando o SDK.
 */
public class VehiclesFragmentExample extends Fragment {

    private static final String TAG = "VehiclesFragment";

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private List<Vehicle> vehicleList = new ArrayList<>();

    public VehiclesFragmentExample() {
    }

    public static VehiclesFragmentExample newInstance() {
        return new VehiclesFragmentExample();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vehicles, container, false);

        // Inicializar views (adapte aos IDs do seu layout)
        // recyclerView = view.findViewById(R.id.recycler_vehicles);
        // progressBar = view.findViewById(R.id.progress_bar);
        // tvEmpty = view.findViewById(R.id.tv_empty);

        // Configurar RecyclerView
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            // recyclerView.setAdapter(new VehicleAdapter(vehicleList));
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Carregar veículos
        loadVehicles();
    }

    /**
     * Carrega a lista de veículos usando o SDK.
     */
    private void loadVehicles() {
        setLoading(true);

        // Listar todos os veículos
        VeiGestSDK.getInstance().vehicles().list(new VeiGestCallback<List<Vehicle>>() {
            @Override
            public void onSuccess(@NonNull List<Vehicle> vehicles) {
                setLoading(false);
                
                vehicleList.clear();
                vehicleList.addAll(vehicles);
                
                // Atualizar adapter
                if (recyclerView != null && recyclerView.getAdapter() != null) {
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
                
                // Mostrar mensagem vazia se não houver veículos
                if (tvEmpty != null) {
                    tvEmpty.setVisibility(vehicles.isEmpty() ? View.VISIBLE : View.GONE);
                }
                
                // Log para debug
                Log.d(TAG, "Veículos carregados: " + vehicles.size());
                for (Vehicle v : vehicles) {
                    Log.d(TAG, "- " + v.getMatricula() + ": " + v.getFullName());
                }
            }

            @Override
            public void onError(@NonNull VeiGestException error) {
                setLoading(false);
                
                Log.e(TAG, "Erro ao carregar veículos", error);
                
                if (error.isAuthenticationError()) {
                    // Token expirado - redirecionar para login
                    Toast.makeText(getContext(), 
                        "Sessão expirada. Por favor faça login novamente.", 
                        Toast.LENGTH_LONG).show();
                    // Navegar para login...
                } else {
                    Toast.makeText(getContext(), 
                        "Erro ao carregar veículos: " + error.getMessage(), 
                        Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Carrega apenas veículos ativos.
     */
    private void loadActiveVehicles() {
        setLoading(true);

        VeiGestSDK.getInstance().vehicles().listActive(new VeiGestCallback<List<Vehicle>>() {
            @Override
            public void onSuccess(@NonNull List<Vehicle> vehicles) {
                setLoading(false);
                vehicleList.clear();
                vehicleList.addAll(vehicles);
                // Atualizar UI...
            }

            @Override
            public void onError(@NonNull VeiGestException error) {
                setLoading(false);
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Atualiza a quilometragem de um veículo.
     */
    private void updateVehicleMileage(int vehicleId, int newMileage) {
        VeiGestSDK.getInstance().vehicles().updateMileage(vehicleId, newMileage, 
            new VeiGestCallback<Vehicle>() {
                @Override
                public void onSuccess(@NonNull Vehicle vehicle) {
                    Toast.makeText(getContext(), 
                        "Quilometragem atualizada: " + vehicle.getQuilometragem() + " km", 
                        Toast.LENGTH_SHORT).show();
                    
                    // Recarregar lista
                    loadVehicles();
                }

                @Override
                public void onError(@NonNull VeiGestException error) {
                    Toast.makeText(getContext(), 
                        "Erro: " + error.getMessage(), 
                        Toast.LENGTH_LONG).show();
                }
            });
    }

    /**
     * Cria um novo veículo.
     */
    private void createVehicle() {
        VeiGestSDK.getInstance().vehicles().create(
            "AA-00-BB",
            "Toyota",
            "Corolla",
            2024,
            "gasolina",
            new VeiGestCallback<Vehicle>() {
                @Override
                public void onSuccess(@NonNull Vehicle vehicle) {
                    Toast.makeText(getContext(), 
                        "Veículo criado: " + vehicle.getMatricula(), 
                        Toast.LENGTH_SHORT).show();
                    loadVehicles();
                }

                @Override
                public void onError(@NonNull VeiGestException error) {
                    Toast.makeText(getContext(), 
                        "Erro: " + error.getMessage(), 
                        Toast.LENGTH_LONG).show();
                }
            });
    }

    private void setLoading(boolean loading) {
        if (progressBar != null) {
            progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        }
        if (recyclerView != null) {
            recyclerView.setVisibility(loading ? View.GONE : View.VISIBLE);
        }
    }
}
