package com.ipleiria.veigest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

/**
 * EditProfileFragment - Edição do Perfil do Usuário
 * Permite alterar foto, nome, telefone, carta de condução e empresa
 */
public class EditProfileFragment extends Fragment {

    private ImageView btnBack;
    private ImageView ivProfilePhoto;
    private ImageView btnChangePhoto;
    private EditText etName;
    private TextView tvEmailReadonly;
    private EditText etPhone;
    private EditText etLicense;
    private EditText etCompany;
    private Button btnSaveProfile;
    
    private AuthManager authManager;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    public static EditProfileFragment newInstance() {
        return new EditProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Configurar launcher para galeria
        galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri selectedImage = result.getData().getData();
                    try {
                        InputStream imageStream = requireActivity().getContentResolver().openInputStream(selectedImage);
                        Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                        ivProfilePhoto.setImageBitmap(bitmap);
                        ivProfilePhoto.setPadding(0, 0, 0, 0);
                        Toast.makeText(getContext(), "Foto atualizada", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(getContext(), "Erro ao carregar imagem", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        );
        
        // Configurar launcher para câmera
        cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Bitmap photo = (Bitmap) result.getData().getExtras().get("data");
                    if (photo != null) {
                        ivProfilePhoto.setImageBitmap(photo);
                        ivProfilePhoto.setPadding(0, 0, 0, 0);
                        Toast.makeText(getContext(), "Foto capturada", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        // Inicializar AuthManager
        authManager = AuthManager.getInstance(requireContext());

        // Inicializar views
        btnBack = view.findViewById(R.id.btn_back);
        ivProfilePhoto = view.findViewById(R.id.iv_profile_photo);
        btnChangePhoto = view.findViewById(R.id.btn_change_photo);
        etName = view.findViewById(R.id.et_name);
        tvEmailReadonly = view.findViewById(R.id.tv_email_readonly);
        etPhone = view.findViewById(R.id.et_phone);
        etLicense = view.findViewById(R.id.et_license);
        etCompany = view.findViewById(R.id.et_company);
        btnSaveProfile = view.findViewById(R.id.btn_save_profile);

        // Carregar dados do perfil
        loadProfileData();

        // Botão voltar
        btnBack.setOnClickListener(v -> {
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.loadFragment(new ProfileFragment());
            }
        });

        // Botão alterar foto
        btnChangePhoto.setOnClickListener(v -> showPhotoOptionsDialog());

        // Botão guardar
        btnSaveProfile.setOnClickListener(v -> saveProfileData());

        return view;
    }

    private void loadProfileData() {
        String email = authManager.getCurrentEmail();
        String name = authManager.getCurrentName();
        
        // Email (read-only)
        if (email != null && !email.isEmpty()) {
            tvEmailReadonly.setText(email);
        }
        
        // Nome
        if (name != null && !name.isEmpty()) {
            etName.setText(name);
        }
        
        // Outros campos ficam vazios por enquanto
        // TODO: Carregar telefone, carta e empresa do armazenamento local ou API
    }

    private void showPhotoOptionsDialog() {
        String[] options = {"Galeria", "Câmera", "Cancelar"};
        
        new AlertDialog.Builder(requireContext())
            .setTitle("Alterar Foto de Perfil")
            .setItems(options, (dialog, which) -> {
                switch (which) {
                    case 0: // Galeria
                        openGallery();
                        break;
                    case 1: // Câmera
                        openCamera();
                        break;
                    case 2: // Cancelar
                        dialog.dismiss();
                        break;
                }
            })
            .show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        galleryLauncher.launch(intent);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            cameraLauncher.launch(intent);
        } else {
            Toast.makeText(getContext(), "Câmera não disponível", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveProfileData() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String license = etLicense.getText().toString().trim();
        String company = etCompany.getText().toString().trim();
        
        // Validar nome (obrigatório)
        if (name.isEmpty()) {
            Toast.makeText(getContext(), "Nome é obrigatório", Toast.LENGTH_SHORT).show();
            etName.requestFocus();
            return;
        }
        
        // Salvar nome no AuthManager
        String email = authManager.getCurrentEmail();
        if (email != null) {
            authManager.saveName(email, name);
        }
        
        // TODO: Salvar telefone, carta e empresa
        // Pode ser em SharedPreferences, EncryptedSharedPreferences ou via API
        
        Toast.makeText(getContext(), "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show();
        
        // Voltar para ProfileFragment
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.loadFragment(new ProfileFragment());
        }
    }
}
