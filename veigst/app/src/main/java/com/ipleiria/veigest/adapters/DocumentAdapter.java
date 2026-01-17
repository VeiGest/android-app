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
import com.veigest.sdk.models.Document;

import java.util.ArrayList;
import java.util.List;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder> {

    private final Context context;
    private List<Document> documents;
    private OnDocumentClickListener listener;

    public interface OnDocumentClickListener {
        void onDocumentClick(Document document);
    }

    public DocumentAdapter(Context context, OnDocumentClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.documents = new ArrayList<>();
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
        if (this.documents == null) {
            this.documents = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DocumentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_document, parent, false);
        return new DocumentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentViewHolder holder, int position) {
        Document document = documents.get(position);
        holder.bind(document);
    }

    @Override
    public int getItemCount() {
        return documents.size();
    }

    class DocumentViewHolder extends RecyclerView.ViewHolder {

        TextView tvType, tvNotes, tvExpiry, tvStatus;
        View cardView;

        public DocumentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tv_document_type);
            tvNotes = itemView.findViewById(R.id.tv_document_notes);
            tvExpiry = itemView.findViewById(R.id.tv_document_expiry);
            tvStatus = itemView.findViewById(R.id.tv_document_status);
            cardView = itemView.findViewById(R.id.card_document);
        }

        public void bind(final Document document) {
            // Set basic info
            String type = document.getTypeLabel() != null ? document.getTypeLabel() : document.getType();
            tvType.setText(type != null ? type : "Documento");

            String notes = document.getNotes();
            if (notes != null && !notes.isEmpty() && !notes.equals("null")) {
                tvNotes.setText(notes);
                tvNotes.setVisibility(View.VISIBLE);
            } else {
                tvNotes.setVisibility(View.GONE);
            }

            tvExpiry.setText("Expira em: " + (document.getExpiryDate() != null ? document.getExpiryDate() : "-"));

            // Set status and color
            String status = document.getStatus();
            if (status == null)
                status = "unknown";

            // Tradução simples do status
            String statusLabel = status;
            if (status.equals("active"))
                statusLabel = "Válido";
            else if (status.equals("expired"))
                statusLabel = "Expirado";
            else if (status.equals("expiring"))
                statusLabel = "A Expirar";

            tvStatus.setText(statusLabel);

            // Cores baseadas no status ou daysToExpiry
            if (document.isExpired()) {
                tvStatus.setTextColor(Color.parseColor("#FF0000")); // Red
                tvStatus.setBackgroundColor(Color.parseColor("#22FF0000"));
            } else if (document.isExpiringSoon()) {
                tvStatus.setTextColor(Color.parseColor("#FFA500")); // Orange
                tvStatus.setBackgroundColor(Color.parseColor("#22FFA500"));
            } else {
                tvStatus.setTextColor(Color.parseColor("#11C7A5")); // Green
                tvStatus.setBackgroundColor(Color.parseColor("#2211C7A5"));
            }

            cardView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDocumentClick(document);
                }
            });
        }
    }
}
