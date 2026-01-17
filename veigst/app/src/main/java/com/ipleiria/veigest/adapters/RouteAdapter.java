package com.ipleiria.veigest.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ipleiria.veigest.R;
import com.veigest.sdk.models.Route;

import java.util.ArrayList;
import java.util.List;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder> {

    private final Context context;
    private List<Route> routes;
    private OnRouteClickListener listener;

    public interface OnRouteClickListener {
        void onRouteClick(Route route);
    }

    public interface OnRouteActionListener extends OnRouteClickListener {
        void onRouteComplete(Route route);
    }

    public RouteAdapter(Context context, OnRouteClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.routes = new ArrayList<>();
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
        if (this.routes == null) {
            this.routes = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    public Route getItem(int position) {
        if (routes != null && position >= 0 && position < routes.size()) {
            return routes.get(position);
        }
        return null;
    }

    public void removeRoute(int position) {
        if (routes != null && position >= 0 && position < routes.size()) {
            routes.remove(position);
            notifyItemRemoved(position);
        }
    }

    @NonNull
    @Override
    public RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_route, parent, false);
        return new RouteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteViewHolder holder, int position) {
        Route route = routes.get(position);
        holder.bind(route);
    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    class RouteViewHolder extends RecyclerView.ViewHolder {

        TextView tvLocations, tvStatus, tvDuration, tvDate;
        View cardView;

        public RouteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLocations = itemView.findViewById(R.id.tv_route_locations);
            tvStatus = itemView.findViewById(R.id.tv_route_status);
            tvDuration = itemView.findViewById(R.id.tv_route_duration);
            tvDate = itemView.findViewById(R.id.tv_route_date);
            cardView = itemView.findViewById(R.id.card_route);
        }

        public void bind(final Route route) {
            String start = route.getStartLocation() != null ? route.getStartLocation() : "?";
            String end = route.getEndLocation() != null ? route.getEndLocation() : "?";
            tvLocations.setText(start + " → " + end);

            // Status
            String status = route.getStatus();
            if (status == null)
                status = "unknown";

            String statusLabel = route.getStatusLabel() != null ? route.getStatusLabel() : status;

            // Override labels for translation if needed
            if (status.equals("completed"))
                statusLabel = "Concluída";
            else if (status.equals("in_progress"))
                statusLabel = "Em Andamento";
            else if (status.equals("scheduled") || status.equals("pending"))
                statusLabel = "Agendada";
            else if (status.equals("cancelled"))
                statusLabel = "Cancelada";

            tvStatus.setText(statusLabel);

            // Cores
            if (route.isCompleted()) {
                tvStatus.setTextColor(Color.GRAY);
                tvStatus.setBackgroundColor(Color.parseColor("#22808080"));
            } else if (route.isInProgress()) {
                tvStatus.setTextColor(Color.parseColor("#11C7A5")); // Green
                tvStatus.setBackgroundColor(Color.parseColor("#2211C7A5"));
            } else if (route.isScheduled()) {
                tvStatus.setTextColor(Color.parseColor("#FFA500")); // Orange
                tvStatus.setBackgroundColor(Color.parseColor("#22FFA500"));
            } else {
                // Cancelled or other
                tvStatus.setTextColor(Color.RED);
                tvStatus.setBackgroundColor(Color.parseColor("#22FF0000"));
            }

            // Duration
            String duration = route.getDurationFormatted();
            if (duration == null)
                duration = route.getDuration() + " min";
            tvDuration.setText("Duração: " + duration);

            // Date
            String date = route.getStartTime();
            if (date == null)
                date = "-";
            tvDate.setText("Data: " + date);

            cardView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onRouteClick(route);
                }
            });

            android.widget.ImageButton btnComplete = itemView.findViewById(R.id.btn_complete_route);
            if (btnComplete != null) {
                // Show completion button only for active/scheduled routes
                if (route.isInProgress() || route.isScheduled()) {
                    btnComplete.setVisibility(View.VISIBLE);
                    btnComplete.setOnClickListener(v -> {
                        // Implement completion logic
                        // Ideally we should add a specific method to interface, but for now we can use
                        // onRouteClick or similar
                        // Let's assume we want to just notify the user it's a future feature or handle
                        // it
                        // For now, let's trigger a click but usually we'd want a separate action
                        if (listener != null) {
                            // Assuming we extend listener or handle it.
                            // For simplicity given current interface, we will just Toast or Log?
                            // No, let's extend the interface properly.
                            if (listener instanceof OnRouteActionListener) {
                                ((OnRouteActionListener) listener).onRouteComplete(route);
                            }
                        }
                    });
                } else {
                    btnComplete.setVisibility(View.INVISIBLE);
                }
            }
        }
    }
}
