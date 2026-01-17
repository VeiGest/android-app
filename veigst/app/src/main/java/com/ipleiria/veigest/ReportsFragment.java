package com.ipleiria.veigest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.veigest.sdk.SingletonVeiGest;
import com.veigest.sdk.listeners.ManutencoesListener;
import com.veigest.sdk.listeners.VeiculosListener;
import com.veigest.sdk.models.Maintenance;
import com.veigest.sdk.models.Vehicle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Fragment para geração de relatórios em PDF
 * ADENDA do projeto - 5% da nota
 */
public class ReportsFragment extends Fragment implements VeiculosListener, ManutencoesListener {

    private Button btnGenerateVehiclesPdf, btnEmailVehiclesPdf;
    private Button btnGenerateMaintenancePdf, btnEmailMaintenancePdf;
    private Button btnGenerateCompletePdf, btnShareCompletePdf;
    private EditText etEmailRecipient;
    private RecyclerView rvRecentReports;
    private TextView tvNoReports;

    private PDFGenerator pdfGenerator;
    private List<Vehicle> vehiclesCache = new ArrayList<>();
    private List<Maintenance> maintenancesCache = new ArrayList<>();

    // Flags para controle de qual operação está em andamento
    private boolean waitingForVehicles = false;
    private boolean waitingForMaintenance = false;
    private boolean generateCompleteReport = false;
    private boolean sendEmail = false;
    private boolean shareReport = false;

    private File lastGeneratedPdf = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);
        
        initializeViews(view);
        setupListeners();
        
        pdfGenerator = new PDFGenerator(requireContext());
        
        // Carregar dados iniciais
        loadData();
        
        return view;
    }

    private void initializeViews(View view) {
        btnGenerateVehiclesPdf = view.findViewById(R.id.btn_generate_vehicles_pdf);
        btnEmailVehiclesPdf = view.findViewById(R.id.btn_email_vehicles_pdf);
        btnGenerateMaintenancePdf = view.findViewById(R.id.btn_generate_maintenance_pdf);
        btnEmailMaintenancePdf = view.findViewById(R.id.btn_email_maintenance_pdf);
        btnGenerateCompletePdf = view.findViewById(R.id.btn_generate_complete_pdf);
        btnShareCompletePdf = view.findViewById(R.id.btn_share_complete_pdf);
        etEmailRecipient = view.findViewById(R.id.et_email_recipient);
        rvRecentReports = view.findViewById(R.id.rv_recent_reports);
        tvNoReports = view.findViewById(R.id.tv_no_reports);

        rvRecentReports.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setupListeners() {
        // Relatório de Veículos
        btnGenerateVehiclesPdf.setOnClickListener(v -> {
            if (vehiclesCache.isEmpty()) {
                waitingForVehicles = true;
                sendEmail = false;
                loadVehicles();
            } else {
                generateVehiclesPdf();
            }
        });

        btnEmailVehiclesPdf.setOnClickListener(v -> {
            if (vehiclesCache.isEmpty()) {
                waitingForVehicles = true;
                sendEmail = true;
                loadVehicles();
            } else {
                generateAndEmailVehiclesPdf();
            }
        });

        // Relatório de Manutenções
        btnGenerateMaintenancePdf.setOnClickListener(v -> {
            if (maintenancesCache.isEmpty()) {
                waitingForMaintenance = true;
                sendEmail = false;
                loadMaintenances();
            } else {
                generateMaintenancePdf();
            }
        });

        btnEmailMaintenancePdf.setOnClickListener(v -> {
            if (maintenancesCache.isEmpty()) {
                waitingForMaintenance = true;
                sendEmail = true;
                loadMaintenances();
            } else {
                generateAndEmailMaintenancePdf();
            }
        });

        // Relatório Completo
        btnGenerateCompletePdf.setOnClickListener(v -> {
            generateCompleteReport = true;
            shareReport = false;
            loadAllData();
        });

        btnShareCompletePdf.setOnClickListener(v -> {
            generateCompleteReport = true;
            shareReport = true;
            loadAllData();
        });
    }

    private void loadData() {
        loadVehicles();
        loadMaintenances();
    }

    private void loadVehicles() {
        SingletonVeiGest singleton = SingletonVeiGest.getInstance(requireContext());
        singleton.setVeiculosListener(this);
        singleton.getAllVeiculosAPI();
    }

    private void loadMaintenances() {
        SingletonVeiGest singleton = SingletonVeiGest.getInstance(requireContext());
        singleton.setManutencoesListener(this);
        singleton.getAllManutencoesAPI();
    }

    private void loadAllData() {
        waitingForVehicles = true;
        waitingForMaintenance = true;
        loadVehicles();
        loadMaintenances();
    }

    // ============ PDF Generation Methods ============

    private void generateVehiclesPdf() {
        if (vehiclesCache.isEmpty()) {
            Toast.makeText(getContext(), "Nenhum veículo disponível", Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading(true);
        
        new Thread(() -> {
            File pdf = pdfGenerator.generateVehicleReport(vehiclesCache);
            requireActivity().runOnUiThread(() -> {
                showLoading(false);
                if (pdf != null) {
                    lastGeneratedPdf = pdf;
                    Toast.makeText(getContext(), "PDF gerado com sucesso!", Toast.LENGTH_SHORT).show();
                    pdfGenerator.openPdf(pdf);
                } else {
                    Toast.makeText(getContext(), "Erro ao gerar PDF", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void generateAndEmailVehiclesPdf() {
        if (vehiclesCache.isEmpty()) {
            Toast.makeText(getContext(), "Nenhum veículo disponível", Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading(true);
        
        new Thread(() -> {
            File pdf = pdfGenerator.generateVehicleReport(vehiclesCache);
            requireActivity().runOnUiThread(() -> {
                showLoading(false);
                if (pdf != null) {
                    lastGeneratedPdf = pdf;
                    String recipient = etEmailRecipient.getText().toString().trim();
                    String[] recipients = recipient.isEmpty() ? new String[0] : new String[]{recipient};
                    pdfGenerator.sendPdfByEmail(pdf, 
                            "Relatório de Veículos - VeiGest",
                            "Segue em anexo o relatório de veículos da frota.\n\nAtenciosamente,\nVeiGest",
                            recipients);
                } else {
                    Toast.makeText(getContext(), "Erro ao gerar PDF", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void generateMaintenancePdf() {
        if (maintenancesCache.isEmpty()) {
            Toast.makeText(getContext(), "Nenhuma manutenção disponível", Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading(true);
        
        new Thread(() -> {
            File pdf = pdfGenerator.generateMaintenanceReport(maintenancesCache);
            requireActivity().runOnUiThread(() -> {
                showLoading(false);
                if (pdf != null) {
                    lastGeneratedPdf = pdf;
                    Toast.makeText(getContext(), "PDF gerado com sucesso!", Toast.LENGTH_SHORT).show();
                    pdfGenerator.openPdf(pdf);
                } else {
                    Toast.makeText(getContext(), "Erro ao gerar PDF", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void generateAndEmailMaintenancePdf() {
        if (maintenancesCache.isEmpty()) {
            Toast.makeText(getContext(), "Nenhuma manutenção disponível", Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading(true);
        
        new Thread(() -> {
            File pdf = pdfGenerator.generateMaintenanceReport(maintenancesCache);
            requireActivity().runOnUiThread(() -> {
                showLoading(false);
                if (pdf != null) {
                    lastGeneratedPdf = pdf;
                    String recipient = etEmailRecipient.getText().toString().trim();
                    String[] recipients = recipient.isEmpty() ? new String[0] : new String[]{recipient};
                    pdfGenerator.sendPdfByEmail(pdf,
                            "Relatório de Manutenções - VeiGest",
                            "Segue em anexo o relatório de manutenções.\n\nAtenciosamente,\nVeiGest",
                            recipients);
                } else {
                    Toast.makeText(getContext(), "Erro ao gerar PDF", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void generateCompletePdf() {
        if (vehiclesCache.isEmpty() && maintenancesCache.isEmpty()) {
            Toast.makeText(getContext(), "Nenhum dado disponível", Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading(true);
        
        new Thread(() -> {
            // Gera relatório de veículos (o mais completo)
            File pdf = pdfGenerator.generateVehicleReport(vehiclesCache);
            requireActivity().runOnUiThread(() -> {
                showLoading(false);
                if (pdf != null) {
                    lastGeneratedPdf = pdf;
                    if (shareReport) {
                        pdfGenerator.sharePdf(pdf, "Relatório VeiGest");
                    } else {
                        Toast.makeText(getContext(), "PDF gerado com sucesso!", Toast.LENGTH_SHORT).show();
                        pdfGenerator.openPdf(pdf);
                    }
                } else {
                    Toast.makeText(getContext(), "Erro ao gerar PDF", Toast.LENGTH_SHORT).show();
                }
                generateCompleteReport = false;
                shareReport = false;
            });
        }).start();
    }

    private void showLoading(boolean show) {
        btnGenerateVehiclesPdf.setEnabled(!show);
        btnEmailVehiclesPdf.setEnabled(!show);
        btnGenerateMaintenancePdf.setEnabled(!show);
        btnEmailMaintenancePdf.setEnabled(!show);
        btnGenerateCompletePdf.setEnabled(!show);
        btnShareCompletePdf.setEnabled(!show);
    }

    // ============ Listeners ============

    @Override
    public void onRefreshListaVeiculos(ArrayList<Vehicle> vehicles) {
        if (vehicles != null) {
            vehiclesCache = new ArrayList<>(vehicles);
            
            if (waitingForVehicles) {
                waitingForVehicles = false;
                
                if (generateCompleteReport && !waitingForMaintenance) {
                    generateCompletePdf();
                } else if (!generateCompleteReport) {
                    if (sendEmail) {
                        generateAndEmailVehiclesPdf();
                    } else {
                        generateVehiclesPdf();
                    }
                }
            }
        }
    }

    @Override
    public void onRefreshListaManutencoes(ArrayList<Maintenance> maintenances) {
        if (maintenances != null) {
            maintenancesCache = new ArrayList<>(maintenances);
            
            if (waitingForMaintenance) {
                waitingForMaintenance = false;
                
                if (generateCompleteReport && !waitingForVehicles) {
                    generateCompletePdf();
                } else if (!generateCompleteReport) {
                    if (sendEmail) {
                        generateAndEmailMaintenancePdf();
                    } else {
                        generateMaintenancePdf();
                    }
                }
            }
        }
    }
}
