package com.ipleiria.veigest;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.ipleiria.veigest.adapters.VehicleAdapter;
import com.veigest.sdk.SingletonVeiGest;
import com.veigest.sdk.listeners.VeiculoListener;
import com.veigest.sdk.listeners.VeiculosListener;
import com.veigest.sdk.models.Vehicle;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Vehicles Fragment - Gerenciamento completo de Veículos com CRUD
 * 
 * Funcionalidades:
 * - Listar veículos da API (GET)
 * - Adicionar novo veículo (POST)
 * - Editar veículo existente (PUT)
 * - Remover veículo (DELETE)
 * - Upload de foto (câmara/galeria) - ADENDA 15%
 * - Cache local em SQLite
 */
public class VehiclesFragment extends Fragment implements VeiculosListener, VeiculoListener,
        VehicleAdapter.OnVehicleClickListener {

    private static final String TAG = "VehiclesFragment";

    // Views
    private RecyclerView recyclerView;
    private VehicleAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;
    private FloatingActionButton fabAdd;
    private ProgressBar progressBar;
    private android.widget.LinearLayout tvEmpty;
    private TextView tvTitle;

    // SDK
    private SingletonVeiGest singleton;

    // Foto
    private Uri photoUri;
    private ImageView dialogPhotoView;
    private Bitmap selectedPhotoBitmap;
    private String currentPhotoPath;

    // Activity Result Launchers
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<String> permissionLauncher;

    public VehiclesFragment() {
        // Required empty public constructor
    }

    public static VehiclesFragment newInstance() {
        return new VehiclesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obter instância do Singleton
        singleton = SingletonVeiGest.getInstance(requireContext());

        // Registar listeners
        singleton.setVeiculosListener(this);
        singleton.setVeiculoListener(this);

        // Registar launchers para câmara e galeria
        setupActivityResultLaunchers();
    }

    private void setupActivityResultLaunchers() {
        // Camera launcher
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        try {
                            selectedPhotoBitmap = MediaStore.Images.Media.getBitmap(
                                    requireContext().getContentResolver(), photoUri);
                            if (dialogPhotoView != null) {
                                dialogPhotoView.setImageBitmap(selectedPhotoBitmap);
                            }
                            Log.d(TAG, "Foto da câmara obtida com sucesso");
                        } catch (IOException e) {
                            Log.e(TAG, "Erro ao carregar foto da câmara", e);
                            Toast.makeText(getContext(), "Erro ao carregar foto", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // Gallery launcher
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri selectedImage = result.getData().getData();
                        if (selectedImage != null) {
                            try {
                                selectedPhotoBitmap = MediaStore.Images.Media.getBitmap(
                                        requireContext().getContentResolver(), selectedImage);
                                if (dialogPhotoView != null) {
                                    dialogPhotoView.setImageBitmap(selectedPhotoBitmap);
                                }
                                Log.d(TAG, "Foto da galeria selecionada com sucesso");
                            } catch (IOException e) {
                                Log.e(TAG, "Erro ao carregar foto da galeria", e);
                                Toast.makeText(getContext(), "Erro ao carregar foto", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

        // Permission launcher
        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        openCamera();
                    } else {
                        Toast.makeText(getContext(), "Permissão de câmara necessária", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vehicles_list, container, false);

        // Inicializar views
        initializeViews(view);

        // Configurar RecyclerView
        setupRecyclerView();

        // Configurar listeners
        setupListeners();

        // Carregar veículos
        loadVehicles();

        return view;
    }

    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.rv_vehicles);
        swipeRefresh = view.findViewById(R.id.swipe_refresh);
        fabAdd = view.findViewById(R.id.fab_add_vehicle);
        progressBar = view.findViewById(R.id.progress_bar);
        tvEmpty = view.findViewById(R.id.tv_empty);
        // tvTitle = view.findViewById(R.id.tv_title);

        setupHeader(view);
    }

    private void setupHeader(View view) {
        View btnMenu = view.findViewById(R.id.btn_menu_global);
        TextView tvTitle = view.findViewById(R.id.tv_header_title);
        if (tvTitle != null) {
            tvTitle.setText(R.string.vehicles_title);
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
        adapter = new VehicleAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void setupListeners() {
        // Swipe to refresh
        swipeRefresh.setOnRefreshListener(this::loadVehicles);
        swipeRefresh.setColorSchemeResources(R.color.Purple);

        // FAB para adicionar
        fabAdd.setOnClickListener(v -> showVehicleDialog(null));
    }

    private void loadVehicles() {
        showLoading(true);

        // Primeiro carrega dados locais
        ArrayList<Vehicle> localVehicles = singleton.getVeiculos();
        if (!localVehicles.isEmpty()) {
            adapter.setVehicles(localVehicles);
            updateEmptyState();
        }

        // Depois busca da API
        singleton.getAllVeiculosAPI();
    }

    // ==================== Implementação VeiculosListener ====================

    @Override
    public void onRefreshListaVeiculos(ArrayList<Vehicle> listaVeiculos) {
        if (getActivity() == null)
            return;

        getActivity().runOnUiThread(() -> {
            showLoading(false);
            swipeRefresh.setRefreshing(false);

            adapter.setVehicles(listaVeiculos);
            updateEmptyState();

            Log.d(TAG, "Lista de veículos atualizada: " + listaVeiculos.size() + " veículos");
        });
    }

    // ==================== Implementação VeiculoListener ====================

    @Override
    public void onRefreshDetalhes(int operacao) {
        if (getActivity() == null)
            return;

        getActivity().runOnUiThread(() -> {
            showLoading(false);

            String message;
            switch (operacao) {
                case OPERACAO_ADICIONAR:
                    message = "Veículo adicionado com sucesso!";
                    break;
                case OPERACAO_EDITAR:
                    message = "Veículo atualizado com sucesso!";
                    break;
                case OPERACAO_REMOVER:
                    message = "Veículo removido com sucesso!";
                    break;
                default:
                    message = "Operação concluída!";
            }

            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

            // Recarregar lista
            loadVehicles();
        });
    }

    // ==================== Implementação VehicleAdapter.OnVehicleClickListener
    // ====================

    @Override
    public void onVehicleClick(Vehicle vehicle) {
        // Mostrar detalhes do veículo (Master/Detail)
        showVehicleDetails(vehicle);
    }

    @Override
    public void onEditClick(Vehicle vehicle) {
        showVehicleDialog(vehicle);
    }

    @Override
    public void onDeleteClick(Vehicle vehicle) {
        showDeleteConfirmation(vehicle);
    }

    // ==================== Dialogs ====================

    /**
     * Mostra dialog para adicionar ou editar veículo
     */
    private void showVehicleDialog(@Nullable Vehicle vehicle) {
        boolean isEdit = vehicle != null;

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_vehicle_form, null);
        builder.setView(dialogView);

        // Obter referências
        TextView tvDialogTitle = dialogView.findViewById(R.id.tv_dialog_title);
        TextInputEditText etLicensePlate = dialogView.findViewById(R.id.et_license_plate);
        TextInputEditText etBrand = dialogView.findViewById(R.id.et_brand);
        TextInputEditText etModel = dialogView.findViewById(R.id.et_model);
        TextInputEditText etYear = dialogView.findViewById(R.id.et_year);
        TextInputEditText etMileage = dialogView.findViewById(R.id.et_mileage);
        AutoCompleteTextView spFuelType = dialogView.findViewById(R.id.sp_fuel_type);
        AutoCompleteTextView spStatus = dialogView.findViewById(R.id.sp_status);
        dialogPhotoView = dialogView.findViewById(R.id.iv_vehicle_photo);
        Button btnTakePhoto = dialogView.findViewById(R.id.btn_take_photo);
        Button btnGallery = dialogView.findViewById(R.id.btn_gallery);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        Button btnSave = dialogView.findViewById(R.id.btn_save);

        // Configurar dropdowns
        String[] fuelTypes = { "Diesel", "Gasolina", "Elétrico", "Híbrido", "GPL" };
        String[] statuses = { "active", "maintenance", "inactive" };

        ArrayAdapter<String> fuelAdapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_dropdown_item_1line, fuelTypes);
        spFuelType.setAdapter(fuelAdapter);

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_dropdown_item_1line, statuses);
        spStatus.setAdapter(statusAdapter);

        // Configurar título
        tvDialogTitle.setText(isEdit ? "Editar Veículo" : "Adicionar Veículo");

        // Preencher campos se for edição
        if (isEdit) {
            etLicensePlate.setText(vehicle.getLicensePlate());
            etBrand.setText(vehicle.getBrand());
            etModel.setText(vehicle.getModel());
            etYear.setText(String.valueOf(vehicle.getYear()));
            etMileage.setText(String.valueOf(vehicle.getMileage()));
            spFuelType.setText(vehicle.getFuelType(), false);
            spStatus.setText(vehicle.getStatus(), false);
        }

        AlertDialog dialog = builder.create();

        // Botões de foto
        btnTakePhoto.setOnClickListener(v -> checkCameraPermissionAndOpen());
        btnGallery.setOnClickListener(v -> openGallery());

        // Cancelar
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        // Guardar
        btnSave.setOnClickListener(v -> {
            // Validação
            String licensePlate = etLicensePlate.getText().toString().trim();
            String brand = etBrand.getText().toString().trim();
            String model = etModel.getText().toString().trim();
            String yearStr = etYear.getText().toString().trim();
            String mileageStr = etMileage.getText().toString().trim();
            String fuelType = spFuelType.getText().toString().trim();
            String status = spStatus.getText().toString().trim();

            if (licensePlate.isEmpty() || brand.isEmpty() || model.isEmpty() || yearStr.isEmpty()) {
                Toast.makeText(getContext(), "Preencha todos os campos obrigatórios (*)", Toast.LENGTH_SHORT).show();
                return;
            }

            int year = Integer.parseInt(yearStr);
            int mileage = mileageStr.isEmpty() ? 0 : Integer.parseInt(mileageStr);

            // Criar ou atualizar veículo
            Vehicle newVehicle = isEdit ? vehicle : new Vehicle();
            newVehicle.setLicensePlate(licensePlate);
            newVehicle.setBrand(brand);
            newVehicle.setModel(model);
            newVehicle.setYear(year);
            newVehicle.setMileage(mileage);
            newVehicle.setFuelType(mapFuelType(fuelType));
            newVehicle.setStatus(status);

            // Processar e definir foto (ADENDA 15%)
            if (selectedPhotoBitmap != null) {
                String base64Photo = bitmapToBase64(selectedPhotoBitmap);
                newVehicle.setPhoto(base64Photo);
            }

            showLoading(true);

            if (isEdit) {
                singleton.editarVeiculoAPI(newVehicle);
            } else {
                singleton.adicionarVeiculoAPI(newVehicle);
            }

            dialog.dismiss();
        });

        dialog.show();
    }

    /**
     * Converte Bitmap para String Base64
     */
    private String bitmapToBase64(Bitmap bitmap) {
        if (bitmap == null)
            return null;

        java.io.ByteArrayOutputStream byteArrayOutputStream = new java.io.ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT);
    }

    /**
     * Mostra confirmação antes de apagar
     */
    private void showDeleteConfirmation(Vehicle vehicle) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Remover Veículo")
                .setMessage("Tem a certeza que deseja remover o veículo " +
                        vehicle.getLicensePlate() + "?\n\nEsta ação não pode ser desfeita.")
                .setPositiveButton("Remover", (dialog, which) -> {
                    showLoading(true);
                    singleton.removerVeiculoAPI(vehicle.getId());
                })
                .setNegativeButton("Cancelar", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * Mostra detalhes do veículo (Master/Detail)
     */
    private void showVehicleDetails(Vehicle vehicle) {
        // Mostrar detalhes num dialog simples por agora
        new AlertDialog.Builder(requireContext())
                .setTitle(vehicle.getBrand() + " " + vehicle.getModel())
                .setMessage(
                        "Matrícula: " + vehicle.getLicensePlate() + "\n" +
                                "Ano: " + vehicle.getYear() + "\n" +
                                "Quilometragem: " + vehicle.getMileage() + " km\n" +
                                "Combustível: " + vehicle.getFuelType() + "\n" +
                                "Estado: " + vehicle.getStatus())
                .setPositiveButton("Fechar", null)
                .setNeutralButton("Editar", (dialog, which) -> showVehicleDialog(vehicle))
                .show();
    }

    // ==================== Foto (ADENDA 15%) ====================

    private void checkCameraPermissionAndOpen() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireContext().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e(TAG, "Erro ao criar ficheiro de imagem", ex);
                Toast.makeText(getContext(), "Erro ao preparar câmara", Toast.LENGTH_SHORT).show();
                return;
            }

            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(requireContext(),
                        "com.ipleiria.veigest.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                cameraLauncher.launch(takePictureIntent);
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        galleryLauncher.launch(intent);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "VEHICLE_" + timeStamp + "_";
        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // ==================== Utilitários ====================

    private String mapFuelType(String displayName) {
        switch (displayName) {
            case "Diesel":
                return "diesel";
            case "Gasolina":
                return "gasoline";
            case "Elétrico":
                return "electric";
            case "Híbrido":
                return "hybrid";
            case "GPL":
                return "lpg";
            default:
                return displayName.toLowerCase();
        }
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
        if (recyclerView != null) {
            recyclerView.setVisibility(adapter.getItemCount() == 0 ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        singleton.setVeiculosListener(null);
        singleton.setVeiculoListener(null);
    }
}
