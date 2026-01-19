package com.ipleiria.veigest.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ipleiria.veigest.R;
import com.veigest.sdk.models.Vehicle;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Adapter para exibir lista de veículos em RecyclerView.
 * Suporta operações de clique, edição e exclusão.
 */
public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder> {

    private List<Vehicle> vehicles;
    private OnVehicleClickListener listener;

    /**
     * Interface para callbacks de interação com veículos
     */
    public interface OnVehicleClickListener {
        void onVehicleClick(Vehicle vehicle);

        void onEditClick(Vehicle vehicle);

        void onDeleteClick(Vehicle vehicle);
    }

    public VehicleAdapter(OnVehicleClickListener listener) {
        this.vehicles = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vehicle, parent, false);
        return new VehicleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {
        Vehicle vehicle = vehicles.get(position);
        holder.bind(vehicle);
    }

    @Override
    public int getItemCount() {
        return vehicles.size();
    }

    /**
     * Atualiza a lista de veículos
     */
    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles.clear();
        if (vehicles != null) {
            this.vehicles.addAll(vehicles);
        }
        notifyDataSetChanged();
    }

    /**
     * Adiciona um veículo à lista
     */
    public void addVehicle(Vehicle vehicle) {
        this.vehicles.add(vehicle);
        notifyItemInserted(vehicles.size() - 1);
    }

    /**
     * Atualiza um veículo existente
     */
    public void updateVehicle(Vehicle vehicle) {
        for (int i = 0; i < vehicles.size(); i++) {
            if (vehicles.get(i).getId() == vehicle.getId()) {
                vehicles.set(i, vehicle);
                notifyItemChanged(i);
                break;
            }
        }
    }

    /**
     * Remove um veículo da lista
     */
    public void removeVehicle(int vehicleId) {
        for (int i = 0; i < vehicles.size(); i++) {
            if (vehicles.get(i).getId() == vehicleId) {
                vehicles.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    /**
     * ViewHolder para item de veículo
     */
    class VehicleViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhoto;
        private TextView tvModel;
        private TextView tvPlate;
        private TextView tvYear;
        private TextView tvStatus;
        private TextView tvMileage;
        private TextView tvFuel;
        private ImageButton btnEdit;
        private ImageButton btnDelete;

        public VehicleViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.iv_vehicle_photo);
            tvModel = itemView.findViewById(R.id.tv_vehicle_model);
            tvPlate = itemView.findViewById(R.id.tv_vehicle_plate);
            tvYear = itemView.findViewById(R.id.tv_vehicle_year);
            tvStatus = itemView.findViewById(R.id.tv_vehicle_status);
            tvMileage = itemView.findViewById(R.id.tv_vehicle_mileage);
            tvFuel = itemView.findViewById(R.id.tv_vehicle_fuel);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }

        public void bind(Vehicle vehicle) {
            // Modelo e Marca
            String modelText = vehicle.getBrand() + " " + vehicle.getModel();
            tvModel.setText(modelText);

            // Matrícula
            tvPlate.setText(vehicle.getLicensePlate());

            // Ano
            tvYear.setText(String.valueOf(vehicle.getYear()));

            // Status com cor
            String status = vehicle.getStatus();
            tvStatus.setText(getStatusLabel(status));
            setStatusColor(tvStatus, status);

            // Quilometragem formatada
            NumberFormat nf = NumberFormat.getInstance(new Locale("pt", "PT"));
            tvMileage.setText(nf.format(vehicle.getMileage()) + " km");

            // Tipo de combustível
            tvFuel.setText(getFuelTypeLabel(vehicle.getFuelType()));

            // Click listeners
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onVehicleClick(vehicle);
                }
            });

            // RBAC: Check permissions
            boolean canEdit = com.veigest.sdk.SingletonVeiGest.getInstance(itemView.getContext()).isManagerOrAdmin();

            if (canEdit) {
                btnEdit.setVisibility(View.VISIBLE);
                btnDelete.setVisibility(View.VISIBLE);

                btnEdit.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onEditClick(vehicle);
                    }
                });

                btnDelete.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onDeleteClick(vehicle);
                    }
                });
            } else {
                btnEdit.setVisibility(View.GONE);
                btnDelete.setVisibility(View.GONE);
            }
        }

        private String getStatusLabel(String status) {
            if (status == null)
                return "Desconhecido";
            switch (status.toLowerCase()) {
                case "active":
                    return "Ativo";
                case "maintenance":
                    return "Manutenção";
                case "inactive":
                    return "Inativo";
                default:
                    return status;
            }
        }

        private void setStatusColor(TextView tv, String status) {
            if (status == null)
                status = "";
            switch (status.toLowerCase()) {
                case "active":
                    tv.setTextColor(0xFF11C7A5);
                    tv.setBackgroundColor(0x2211C7A5);
                    break;
                case "maintenance":
                    tv.setTextColor(0xFFFF9800);
                    tv.setBackgroundColor(0x22FF9800);
                    break;
                case "inactive":
                    tv.setTextColor(0xFFFF5252);
                    tv.setBackgroundColor(0x22FF5252);
                    break;
                default:
                    tv.setTextColor(0xFF757575);
                    tv.setBackgroundColor(0x22757575);
            }
        }

        private String getFuelTypeLabel(String fuelType) {
            if (fuelType == null)
                return "N/D";
            switch (fuelType.toLowerCase()) {
                case "diesel":
                    return "Diesel";
                case "gasoline":
                case "gasolina":
                    return "Gasolina";
                case "electric":
                case "eletrico":
                    return "Elétrico";
                case "hybrid":
                case "hibrido":
                    return "Híbrido";
                case "lpg":
                case "gpl":
                    return "GPL";
                default:
                    return fuelType;
            }
        }
    }
}
