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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ipleiria.veigest.adapters.DocumentAdapter;
import com.veigest.sdk.SingletonVeiGest;
import com.veigest.sdk.listeners.DocumentosListener;
import com.veigest.sdk.models.Document;

import java.util.ArrayList;

/**
 * Documents Fragment - Gerenciamento de Documentação
 * Exibe lista de documentos obtidos da API via SDK.
 */
public class DocumentsFragment extends Fragment implements DocumentosListener, DocumentAdapter.OnDocumentClickListener {

    private static final String TAG = "DocumentsFragment";

    private RecyclerView recyclerView;
    private DocumentAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private TextView tvTitle;

    private SingletonVeiGest singleton;

    public DocumentsFragment() {
        // Required empty public constructor
    }

    public static DocumentsFragment newInstance() {
        return new DocumentsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obter instância do Singleton
        singleton = SingletonVeiGest.getInstance(requireContext());
        singleton.setDocumentosListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_documents, container, false);

        initializeViews(view);
        setupRecyclerView();
        setupListeners();

        // Carregar dados
        loadDocuments();

        return view;
    }

    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.rv_documents);
        swipeRefresh = view.findViewById(R.id.swipe_refresh);
        progressBar = view.findViewById(R.id.progress_bar);
        tvEmpty = view.findViewById(R.id.tv_empty);
        tvEmpty = view.findViewById(R.id.tv_empty);
        // tvTitle = view.findViewById(R.id.tv_documents_title); Removed

        setupHeader(view);
    }

    private void setupHeader(View view) {
        View btnMenu = view.findViewById(R.id.btn_menu_global);
        TextView tvTitle = view.findViewById(R.id.tv_header_title);
        if (tvTitle != null) {
            tvTitle.setText(R.string.documents_title);
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
        adapter = new DocumentAdapter(getContext(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void setupListeners() {
        swipeRefresh.setOnRefreshListener(this::loadDocuments);
        swipeRefresh.setColorSchemeResources(R.color.Purple);
    }

    private void loadDocuments() {
        showLoading(true);

        // Primeiro tenta dados locais
        ArrayList<Document> localDocs = singleton.getDocumentos();
        if (!localDocs.isEmpty()) {
            adapter.setDocuments(localDocs);
            updateEmptyState();
        }

        // Busca da API
        singleton.getAllDocumentosAPI();
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
    public void onRefreshListaDocumentos(ArrayList<Document> listaDocumentos) {
        if (getActivity() == null)
            return;

        getActivity().runOnUiThread(() -> {
            showLoading(false);
            swipeRefresh.setRefreshing(false);

            adapter.setDocuments(listaDocumentos);
            updateEmptyState();

            Log.d(TAG, "Lista de documentos atualizada: " + listaDocumentos.size());
        });
    }

    // ==================== Adapter Listener Implementation ====================

    @Override
    public void onDocumentClick(Document document) {
        String msg = "Documento: " + (document.getTypeLabel() != null ? document.getTypeLabel() : document.getType());
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        // Futuro: Abrir detalhe ou PDF
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        singleton.setDocumentosListener(null);
    }
}
