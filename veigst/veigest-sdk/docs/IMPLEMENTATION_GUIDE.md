# Guia de Implementação - VeiGest SDK

Este guia fornece exemplos práticos para implementar funcionalidades comuns com o VeiGest SDK.

---

## Índice

1. [Autenticação Completa](#autenticação-completa)
2. [Listagem com RecyclerView](#listagem-com-recyclerview)
3. [Detalhes de Item](#detalhes-de-item)
4. [Criar/Editar Registros](#criareditar-registros)
5. [Cache Offline](#cache-offline)
6. [Pull to Refresh](#pull-to-refresh)
7. [Busca e Filtros](#busca-e-filtros)
8. [Carregamento de Imagens](#carregamento-de-imagens)
9. [Validação de Formulários](#validação-de-formulários)
10. [Tratamento de Erros](#tratamento-de-erros)

---

## Autenticação Completa

### LoginActivity/Fragment

```java
public class LoginFragment extends Fragment implements LoginListener {
    
    private SingletonVeiGest singleton;
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private ProgressBar progressBar;
    private CheckBox cbRemember;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        singleton = SingletonVeiGest.getInstance(requireContext());
        singleton.setLoginListener(this);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        
        etEmail = view.findViewById(R.id.et_email);
        etPassword = view.findViewById(R.id.et_password);
        btnLogin = view.findViewById(R.id.btn_login);
        progressBar = view.findViewById(R.id.progress_bar);
        cbRemember = view.findViewById(R.id.cb_remember);
        
        // Verificar credenciais salvas
        loadSavedCredentials();
        
        // Botão de login
        btnLogin.setOnClickListener(v -> performLogin());
        
        return view;
    }
    
    private void loadSavedCredentials() {
        SharedPreferences prefs = requireContext()
            .getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        
        if (prefs.getBoolean("remember", false)) {
            etEmail.setText(prefs.getString("email", ""));
            cbRemember.setChecked(true);
        }
    }
    
    private void performLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        
        // Validação
        if (email.isEmpty()) {
            etEmail.setError("Email é obrigatório");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email inválido");
            return;
        }
        if (password.isEmpty()) {
            etPassword.setError("Password é obrigatória");
            return;
        }
        
        // Salvar email se "Lembrar" marcado
        if (cbRemember.isChecked()) {
            requireContext().getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
                .edit()
                .putString("email", email)
                .putBoolean("remember", true)
                .apply();
        }
        
        // Mostrar loading
        setLoading(true);
        
        // Fazer login
        singleton.loginAPI(email, password);
    }
    
    @Override
    public void onValidateLogin(String token, User user) {
        if (getActivity() == null) return;
        
        getActivity().runOnUiThread(() -> {
            setLoading(false);
            
            Toast.makeText(getContext(), 
                "Bem-vindo, " + user.getUsername() + "!", 
                Toast.LENGTH_SHORT).show();
            
            // Navegar para MainActivity/Dashboard
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
    
    @Override
    public void onLoginError(String errorMessage) {
        if (getActivity() == null) return;
        
        getActivity().runOnUiThread(() -> {
            setLoading(false);
            
            // Mensagem amigável baseada no erro
            String msg;
            if (errorMessage.contains("401")) {
                msg = "Email ou password incorretos";
            } else if (errorMessage.contains("network") || 
                       errorMessage.contains("Unable to resolve")) {
                msg = "Sem conexão à internet";
            } else {
                msg = "Erro ao fazer login. Tente novamente.";
            }
            
            Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
        });
    }
    
    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!loading);
        etEmail.setEnabled(!loading);
        etPassword.setEnabled(!loading);
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        singleton.setLoginListener(null);
    }
}
```

---

## Listagem com RecyclerView

### VeiculosFragment

```java
public class VeiculosFragment extends Fragment implements VeiculosListener {
    
    private SingletonVeiGest singleton;
    private RecyclerView recyclerView;
    private VeiculosAdapter adapter;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        singleton = SingletonVeiGest.getInstance(requireContext());
        singleton.setVeiculosListener(this);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_veiculos, container, false);
        
        recyclerView = view.findViewById(R.id.recycler_veiculos);
        progressBar = view.findViewById(R.id.progress_bar);
        tvEmpty = view.findViewById(R.id.tv_empty);
        
        setupRecyclerView();
        loadData();
        
        return view;
    }
    
    private void setupRecyclerView() {
        adapter = new VeiculosAdapter(new ArrayList<>(), vehicle -> {
            // Click no item - abrir detalhes
            Bundle args = new Bundle();
            args.putSerializable("vehicle", vehicle);
            
            VeiculoDetalheFragment fragment = new VeiculoDetalheFragment();
            fragment.setArguments(args);
            
            getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
        });
        
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
    
    private void loadData() {
        progressBar.setVisibility(View.VISIBLE);
        tvEmpty.setVisibility(View.GONE);
        
        // Carregar dados do cache primeiro
        ArrayList<Vehicle> cached = singleton.getBD().getAllVehiclesBD();
        if (!cached.isEmpty()) {
            adapter.setData(cached);
            adapter.notifyDataSetChanged();
        }
        
        // Depois carregar da API
        singleton.getAllVeiculosAPI();
    }
    
    @Override
    public void onRefreshListaVeiculos(ArrayList<Vehicle> lista) {
        if (getActivity() == null) return;
        
        getActivity().runOnUiThread(() -> {
            progressBar.setVisibility(View.GONE);
            
            if (lista.isEmpty()) {
                tvEmpty.setVisibility(View.VISIBLE);
                tvEmpty.setText("Nenhum veículo encontrado");
            } else {
                tvEmpty.setVisibility(View.GONE);
            }
            
            adapter.setData(lista);
            adapter.notifyDataSetChanged();
            
            // Salvar no cache
            saveToCache(lista);
        });
    }
    
    private void saveToCache(ArrayList<Vehicle> lista) {
        new Thread(() -> {
            singleton.getBD().removerAllVehiclesBD();
            for (Vehicle v : lista) {
                singleton.getBD().adicionarVehicleBD(v);
            }
        }).start();
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        singleton.setVeiculosListener(null);
    }
}
```

### VeiculosAdapter

```java
public class VeiculosAdapter extends RecyclerView.Adapter<VeiculosAdapter.ViewHolder> {
    
    private ArrayList<Vehicle> veiculos;
    private OnVehicleClickListener listener;
    
    public interface OnVehicleClickListener {
        void onVehicleClick(Vehicle vehicle);
    }
    
    public VeiculosAdapter(ArrayList<Vehicle> veiculos, OnVehicleClickListener listener) {
        this.veiculos = veiculos;
        this.listener = listener;
    }
    
    public void setData(ArrayList<Vehicle> veiculos) {
        this.veiculos = veiculos;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_veiculo, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Vehicle vehicle = veiculos.get(position);
        
        holder.tvPlate.setText(vehicle.getLicensePlate());
        holder.tvModel.setText(vehicle.getBrand() + " " + vehicle.getModel());
        holder.tvYear.setText(String.valueOf(vehicle.getYear()));
        holder.tvMileage.setText(String.format("%,d km", vehicle.getMileage()));
        
        // Status com cor
        holder.tvStatus.setText(vehicle.getStatusLabel());
        int color = "active".equals(vehicle.getStatus()) ? 
            R.color.green : R.color.orange;
        holder.tvStatus.setTextColor(
            ContextCompat.getColor(holder.itemView.getContext(), color));
        
        // Imagem com Glide
        if (vehicle.getPhoto() != null && !vehicle.getPhoto().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                .load(vehicle.getPhoto())
                .placeholder(R.drawable.ic_vehicle_placeholder)
                .error(R.drawable.ic_vehicle_placeholder)
                .into(holder.ivPhoto);
        }
        
        // Click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onVehicleClick(vehicle);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return veiculos.size();
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPhoto;
        TextView tvPlate, tvModel, tvYear, tvMileage, tvStatus;
        
        ViewHolder(View view) {
            super(view);
            ivPhoto = view.findViewById(R.id.iv_photo);
            tvPlate = view.findViewById(R.id.tv_plate);
            tvModel = view.findViewById(R.id.tv_model);
            tvYear = view.findViewById(R.id.tv_year);
            tvMileage = view.findViewById(R.id.tv_mileage);
            tvStatus = view.findViewById(R.id.tv_status);
        }
    }
}
```

---

## Detalhes de Item

### VeiculoDetalheFragment

```java
public class VeiculoDetalheFragment extends Fragment implements VeiculoListener {
    
    private SingletonVeiGest singleton;
    private Vehicle vehicle;
    
    private ImageView ivPhoto;
    private TextView tvPlate, tvBrand, tvModel, tvYear, tvFuel, tvMileage, tvStatus;
    private Button btnEdit, btnDelete;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        singleton = SingletonVeiGest.getInstance(requireContext());
        singleton.setVeiculoListener(this);
        
        // Obter veículo dos argumentos
        if (getArguments() != null) {
            vehicle = (Vehicle) getArguments().getSerializable("vehicle");
        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_veiculo_detalhe, container, false);
        
        initViews(view);
        
        if (vehicle != null) {
            populateData();
            // Carregar dados atualizados da API
            singleton.getVeiculoAPI(vehicle.getId());
        }
        
        return view;
    }
    
    private void initViews(View view) {
        ivPhoto = view.findViewById(R.id.iv_photo);
        tvPlate = view.findViewById(R.id.tv_plate);
        tvBrand = view.findViewById(R.id.tv_brand);
        tvModel = view.findViewById(R.id.tv_model);
        tvYear = view.findViewById(R.id.tv_year);
        tvFuel = view.findViewById(R.id.tv_fuel);
        tvMileage = view.findViewById(R.id.tv_mileage);
        tvStatus = view.findViewById(R.id.tv_status);
        btnEdit = view.findViewById(R.id.btn_edit);
        btnDelete = view.findViewById(R.id.btn_delete);
        
        btnEdit.setOnClickListener(v -> openEditScreen());
        btnDelete.setOnClickListener(v -> confirmDelete());
    }
    
    private void populateData() {
        tvPlate.setText(vehicle.getLicensePlate());
        tvBrand.setText(vehicle.getBrand());
        tvModel.setText(vehicle.getModel());
        tvYear.setText(String.valueOf(vehicle.getYear()));
        tvFuel.setText(vehicle.getFuelTypeLabel());
        tvMileage.setText(String.format("%,d km", vehicle.getMileage()));
        tvStatus.setText(vehicle.getStatusLabel());
        
        if (vehicle.getPhoto() != null) {
            Glide.with(this)
                .load(vehicle.getPhoto())
                .placeholder(R.drawable.ic_vehicle_large)
                .into(ivPhoto);
        }
    }
    
    @Override
    public void onRefreshVeiculo(Vehicle veiculo) {
        if (getActivity() == null) return;
        
        getActivity().runOnUiThread(() -> {
            this.vehicle = veiculo;
            populateData();
        });
    }
    
    private void openEditScreen() {
        Bundle args = new Bundle();
        args.putSerializable("vehicle", vehicle);
        args.putBoolean("editMode", true);
        
        VeiculoFormFragment fragment = new VeiculoFormFragment();
        fragment.setArguments(args);
        
        getParentFragmentManager()
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit();
    }
    
    private void confirmDelete() {
        new AlertDialog.Builder(requireContext())
            .setTitle("Remover Veículo")
            .setMessage("Tem certeza que deseja remover este veículo?")
            .setPositiveButton("Remover", (dialog, which) -> deleteVehicle())
            .setNegativeButton("Cancelar", null)
            .show();
    }
    
    private void deleteVehicle() {
        // TODO: Implementar deleteVeiculoAPI no SingletonVeiGest
        Toast.makeText(getContext(), "Funcionalidade em desenvolvimento", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        singleton.setVeiculoListener(null);
    }
}
```

---

## Criar/Editar Registros

### VeiculoFormFragment

```java
public class VeiculoFormFragment extends Fragment {
    
    private SingletonVeiGest singleton;
    private Vehicle vehicle;
    private boolean editMode = false;
    
    private EditText etPlate, etBrand, etModel, etYear, etMileage;
    private Spinner spFuelType, spStatus;
    private Button btnSave;
    private ProgressBar progressBar;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        singleton = SingletonVeiGest.getInstance(requireContext());
        
        if (getArguments() != null) {
            vehicle = (Vehicle) getArguments().getSerializable("vehicle");
            editMode = getArguments().getBoolean("editMode", false);
        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_veiculo_form, container, false);
        
        initViews(view);
        setupSpinners();
        
        if (editMode && vehicle != null) {
            populateForm();
        }
        
        return view;
    }
    
    private void initViews(View view) {
        etPlate = view.findViewById(R.id.et_plate);
        etBrand = view.findViewById(R.id.et_brand);
        etModel = view.findViewById(R.id.et_model);
        etYear = view.findViewById(R.id.et_year);
        etMileage = view.findViewById(R.id.et_mileage);
        spFuelType = view.findViewById(R.id.sp_fuel_type);
        spStatus = view.findViewById(R.id.sp_status);
        btnSave = view.findViewById(R.id.btn_save);
        progressBar = view.findViewById(R.id.progress_bar);
        
        btnSave.setText(editMode ? "Atualizar" : "Criar");
        btnSave.setOnClickListener(v -> saveVehicle());
    }
    
    private void setupSpinners() {
        // Spinner de tipo de combustível
        String[] fuelTypes = {"gasoline", "diesel", "electric", "hybrid"};
        String[] fuelLabels = {"Gasolina", "Diesel", "Elétrico", "Híbrido"};
        ArrayAdapter<String> fuelAdapter = new ArrayAdapter<>(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, fuelLabels);
        spFuelType.setAdapter(fuelAdapter);
        
        // Spinner de status
        String[] statuses = {"active", "maintenance", "inactive"};
        String[] statusLabels = {"Ativo", "Em Manutenção", "Inativo"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, statusLabels);
        spStatus.setAdapter(statusAdapter);
    }
    
    private void populateForm() {
        etPlate.setText(vehicle.getLicensePlate());
        etBrand.setText(vehicle.getBrand());
        etModel.setText(vehicle.getModel());
        etYear.setText(String.valueOf(vehicle.getYear()));
        etMileage.setText(String.valueOf(vehicle.getMileage()));
        
        // Selecionar valores nos spinners
        // ... (implementar lógica de seleção)
    }
    
    private void saveVehicle() {
        // Validar
        if (!validateForm()) return;
        
        // Criar JSON
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("licensePlate", etPlate.getText().toString().trim());
            jsonBody.put("brand", etBrand.getText().toString().trim());
            jsonBody.put("model", etModel.getText().toString().trim());
            jsonBody.put("year", Integer.parseInt(etYear.getText().toString()));
            jsonBody.put("mileage", Integer.parseInt(etMileage.getText().toString()));
            jsonBody.put("fuelType", getFuelTypeValue());
            jsonBody.put("status", getStatusValue());
        } catch (JSONException e) {
            Log.e("Form", "Erro JSON: " + e.getMessage());
            return;
        }
        
        setLoading(true);
        
        String url = singleton.getBaseUrl() + "/vehicles";
        int method = Request.Method.POST;
        
        if (editMode) {
            url += "/" + vehicle.getId();
            method = Request.Method.PUT;
        }
        
        JsonObjectRequest request = new JsonObjectRequest(method, url, jsonBody,
            response -> {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> {
                    setLoading(false);
                    Toast.makeText(getContext(), 
                        editMode ? "Veículo atualizado!" : "Veículo criado!", 
                        Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStack();
                });
            },
            error -> {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> {
                    setLoading(false);
                    Toast.makeText(getContext(), "Erro ao salvar", Toast.LENGTH_SHORT).show();
                });
            }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + singleton.getToken());
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        
        Volley.newRequestQueue(requireContext()).add(request);
    }
    
    private boolean validateForm() {
        boolean valid = true;
        
        if (etPlate.getText().toString().trim().isEmpty()) {
            etPlate.setError("Matrícula obrigatória");
            valid = false;
        }
        if (etBrand.getText().toString().trim().isEmpty()) {
            etBrand.setError("Marca obrigatória");
            valid = false;
        }
        if (etModel.getText().toString().trim().isEmpty()) {
            etModel.setError("Modelo obrigatório");
            valid = false;
        }
        
        String yearStr = etYear.getText().toString();
        if (yearStr.isEmpty()) {
            etYear.setError("Ano obrigatório");
            valid = false;
        } else {
            int year = Integer.parseInt(yearStr);
            if (year < 1900 || year > 2030) {
                etYear.setError("Ano inválido");
                valid = false;
            }
        }
        
        return valid;
    }
    
    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnSave.setEnabled(!loading);
    }
    
    private String getFuelTypeValue() {
        String[] values = {"gasoline", "diesel", "electric", "hybrid"};
        return values[spFuelType.getSelectedItemPosition()];
    }
    
    private String getStatusValue() {
        String[] values = {"active", "maintenance", "inactive"};
        return values[spStatus.getSelectedItemPosition()];
    }
}
```

---

## Pull to Refresh

```java
public class VeiculosFragment extends Fragment implements VeiculosListener {
    
    private SwipeRefreshLayout swipeRefresh;
    
    @Override
    public View onCreateView(...) {
        View view = inflater.inflate(R.layout.fragment_veiculos, container, false);
        
        swipeRefresh = view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setOnRefreshListener(() -> {
            singleton.getAllVeiculosAPI();
        });
        
        // Cores do indicador
        swipeRefresh.setColorSchemeResources(
            R.color.primary,
            R.color.secondary
        );
        
        return view;
    }
    
    @Override
    public void onRefreshListaVeiculos(ArrayList<Vehicle> lista) {
        if (getActivity() == null) return;
        
        getActivity().runOnUiThread(() -> {
            // Parar animação de refresh
            swipeRefresh.setRefreshing(false);
            
            adapter.setData(lista);
            adapter.notifyDataSetChanged();
        });
    }
}
```

---

## Busca e Filtros

```java
public class VeiculosFragment extends Fragment implements VeiculosListener {
    
    private SearchView searchView;
    private ArrayList<Vehicle> allVehicles = new ArrayList<>();
    
    @Override
    public View onCreateView(...) {
        // ...
        
        searchView = view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterVehicles(query);
                return true;
            }
            
            @Override
            public boolean onQueryTextChange(String newText) {
                filterVehicles(newText);
                return true;
            }
        });
        
        return view;
    }
    
    @Override
    public void onRefreshListaVeiculos(ArrayList<Vehicle> lista) {
        if (getActivity() == null) return;
        
        getActivity().runOnUiThread(() -> {
            allVehicles = new ArrayList<>(lista);
            adapter.setData(allVehicles);
            adapter.notifyDataSetChanged();
        });
    }
    
    private void filterVehicles(String query) {
        if (query.isEmpty()) {
            adapter.setData(allVehicles);
        } else {
            ArrayList<Vehicle> filtered = new ArrayList<>();
            String lowerQuery = query.toLowerCase();
            
            for (Vehicle v : allVehicles) {
                if (v.getLicensePlate().toLowerCase().contains(lowerQuery) ||
                    v.getBrand().toLowerCase().contains(lowerQuery) ||
                    v.getModel().toLowerCase().contains(lowerQuery)) {
                    filtered.add(v);
                }
            }
            
            adapter.setData(filtered);
        }
        adapter.notifyDataSetChanged();
    }
}
```

---

## Carregamento de Imagens

### Básico com Glide

```java
Glide.with(context)
    .load(vehicle.getPhoto())
    .into(imageView);
```

### Com Placeholder e Erro

```java
Glide.with(context)
    .load(vehicle.getPhoto())
    .placeholder(R.drawable.loading_spinner)
    .error(R.drawable.error_image)
    .into(imageView);
```

### Circular (para avatares)

```java
Glide.with(context)
    .load(user.getAvatarUrl())
    .circleCrop()
    .placeholder(R.drawable.default_avatar)
    .into(imageView);
```

### Com Cache

```java
Glide.with(context)
    .load(url)
    .diskCacheStrategy(DiskCacheStrategy.ALL)
    .into(imageView);
```

---

## Tratamento de Erros

### Classe Utilitária

```java
public class ErrorHandler {
    
    public static String getErrorMessage(String error) {
        if (error == null) return "Erro desconhecido";
        
        error = error.toLowerCase();
        
        if (error.contains("401") || error.contains("unauthorized")) {
            return "Sessão expirada. Faça login novamente.";
        }
        if (error.contains("403") || error.contains("forbidden")) {
            return "Acesso negado.";
        }
        if (error.contains("404") || error.contains("not found")) {
            return "Recurso não encontrado.";
        }
        if (error.contains("500") || error.contains("server")) {
            return "Erro no servidor. Tente mais tarde.";
        }
        if (error.contains("network") || error.contains("unable to resolve")) {
            return "Sem conexão à internet.";
        }
        if (error.contains("timeout")) {
            return "Tempo limite excedido. Tente novamente.";
        }
        
        return "Ocorreu um erro. Tente novamente.";
    }
    
    public static void handleError(Context context, String error) {
        String message = getErrorMessage(error);
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        
        // Se sessão expirou, redirecionar para login
        if (error.contains("401")) {
            SingletonVeiGest.getInstance(context).clearAuth();
            // Navegar para login
        }
    }
}
```

### Uso

```java
@Override
public void onLoginError(String errorMessage) {
    if (getActivity() == null) return;
    
    getActivity().runOnUiThread(() -> {
        setLoading(false);
        ErrorHandler.handleError(getContext(), errorMessage);
    });
}
```

---

## Conclusão

Este guia cobre as implementações mais comuns. Para casos específicos:

1. Consulte a documentação principal (README.md)
2. Verifique o Troubleshooting
3. Analise o código-fonte do SDK
