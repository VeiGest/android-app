# VeiGest - Listeners e Callbacks
## Comunicação Assíncrona

**ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO - 2025 / 2026**  
**TESP PROGRAMAÇÃO DE SISTEMAS DE INFORMAÇÃO**

---

## 📖 Índice

1. [Introdução aos Listeners](#introdução-aos-listeners)
2. [Padrão Observer](#padrão-observer)
3. [Listeners do VeiGest SDK](#listeners-do-veigest-sdk)
4. [Implementação nos Fragments](#implementação-nos-fragments)
5. [Boas Práticas](#boas-práticas)

---

## Introdução aos Listeners

**Listeners** (ou **Callbacks**) são interfaces que permitem comunicação assíncrona entre componentes. No VeiGest, são usados para notificar Fragments sobre resultados de operações na API.

### Porquê Listeners?

```
┌─────────────────────────────────────────────────────────────┐
│            PROBLEMA: Requisições Assíncronas                │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│   Fragment                  Singleton                       │
│   ┌─────────┐              ┌─────────┐                     │
│   │         │   chamada    │         │   requisição        │
│   │         │ ───────────► │         │ ───────────► API    │
│   │         │              │         │                      │
│   │    ?    │   como       │         │   resposta          │
│   │         │ ◄─ notificar │         │ ◄───────────        │
│   └─────────┘              └─────────┘                     │
│                                                             │
│   SOLUÇÃO: Listeners                                        │
│                                                             │
│   Fragment                  Singleton                       │
│   ┌─────────┐              ┌─────────┐                     │
│   │implements│   1. set    │         │                     │
│   │ Listener │ ───────────► │ listener│                     │
│   │         │              │         │ ───────────► API    │
│   │         │   3. callback│         │                      │
│   │ método()│ ◄─────────── │         │ ◄───────────        │
│   └─────────┘              └─────────┘                     │
│                 2. quando resposta chega                    │
│                    chama o método                           │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## Padrão Observer

Os Listeners implementam o **Padrão Observer**, onde:
- **Subject**: SingletonVeiGest (guarda referência ao listener)
- **Observer**: Fragment que implementa a interface

### Estrutura do Padrão

```java
// 1. Definir interface do Listener
public interface MyListener {
    void onSuccess(Object data);
    void onError(String message);
}

// 2. Subject guarda e notifica o Listener
public class Subject {
    private MyListener listener;
    
    public void setMyListener(MyListener listener) {
        this.listener = listener;
    }
    
    public void doAsyncWork() {
        // ... trabalho assíncrono ...
        
        // Notificar
        if (listener != null) {
            listener.onSuccess(result);
        }
    }
}

// 3. Observer implementa a interface
public class Observer implements MyListener {
    
    @Override
    public void onSuccess(Object data) {
        // Usar dados
    }
    
    @Override
    public void onError(String message) {
        // Tratar erro
    }
}
```

---

## Listeners do VeiGest SDK

### LoginListener

```java
package com.veigest.sdk.listeners;

public interface LoginListener {
    /**
     * Chamado quando o login é válido
     * @param token Token de autenticação
     * @param user Dados do utilizador
     */
    void onValidateLogin(String token, User user);
    
    /**
     * Chamado quando o login falha
     * @param error Mensagem de erro
     */
    void onLoginError(String error);
}
```

**Uso:**
```java
public class LoginFragment extends Fragment implements LoginListener {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SingletonVeiGest.getInstance(getContext()).setLoginListener(this);
    }
    
    @Override
    public void onValidateLogin(String token, User user) {
        getActivity().runOnUiThread(() -> {
            // Navegar para dashboard
            ((MainActivity) getActivity()).loadDashboard();
        });
    }
    
    @Override
    public void onLoginError(String error) {
        getActivity().runOnUiThread(() -> {
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        });
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SingletonVeiGest.getInstance(getContext()).setLoginListener(null);
    }
}
```

---

### VeiculosListener

```java
package com.veigest.sdk.listeners;

public interface VeiculosListener {
    /**
     * Chamado quando a lista de veículos é atualizada
     * @param vehicles Lista de veículos
     */
    void onRefreshVeiculos(ArrayList<Vehicle> vehicles);
    
    /**
     * Chamado quando há erro ao obter veículos
     * @param error Mensagem de erro
     */
    void onRefreshVeiculosError(String error);
}
```

**Uso:**
```java
public class DashboardFragment extends Fragment implements VeiculosListener {
    
    private ArrayList<Vehicle> veiculos = new ArrayList<>();
    private VeiculosAdapter adapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SingletonVeiGest.getInstance(getContext()).setVeiculosListener(this);
    }
    
    @Override
    public void onStart() {
        super.onStart();
        // Pedir dados
        SingletonVeiGest.getInstance(getContext()).getAllVeiculosAPI();
    }
    
    @Override
    public void onRefreshVeiculos(ArrayList<Vehicle> vehicles) {
        requireActivity().runOnUiThread(() -> {
            veiculos.clear();
            veiculos.addAll(vehicles);
            adapter.notifyDataSetChanged();
        });
    }
    
    @Override
    public void onRefreshVeiculosError(String error) {
        requireActivity().runOnUiThread(() -> {
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        });
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SingletonVeiGest.getInstance(getContext()).setVeiculosListener(null);
    }
}
```

---

### RotasListener

```java
package com.veigest.sdk.listeners;

public interface RotasListener {
    void onRefreshRotas(ArrayList<Route> routes);
    void onRefreshRotasError(String error);
}
```

---

### ManutencaoListener

```java
package com.veigest.sdk.listeners;

public interface ManutencaoListener {
    void onRefreshManutencoes(ArrayList<Maintenance> maintenances);
    void onRefreshManutencoesError(String error);
}
```

---

### AlertasListener

```java
package com.veigest.sdk.listeners;

public interface AlertasListener {
    void onRefreshAlertas(ArrayList<Alert> alerts);
    void onRefreshAlertasError(String error);
}
```

---

### AbastecimentoListener

```java
package com.veigest.sdk.listeners;

public interface AbastecimentoListener {
    void onRefreshAbastecimentos(ArrayList<FuelLog> fuelLogs);
    void onRefreshAbastecimentosError(String error);
}
```

---

### DocumentosListener

```java
package com.veigest.sdk.listeners;

public interface DocumentosListener {
    void onRefreshDocumentos(ArrayList<Document> documents);
    void onRefreshDocumentosError(String error);
}
```

---

### VeiculoListener (Operações CRUD)

```java
package com.veigest.sdk.listeners;

public interface VeiculoListener {
    
    int OPERACAO_ADICIONAR = 1;
    int OPERACAO_EDITAR = 2;
    int OPERACAO_REMOVER = 3;
    
    /**
     * Chamado quando uma operação é concluída com sucesso
     * @param operacao Tipo de operação realizada
     * @param vehicle Veículo afetado (null para remover)
     */
    void onOperacaoVeiculo(int operacao, Vehicle vehicle);
    
    /**
     * Chamado quando há erro numa operação
     * @param error Mensagem de erro
     */
    void onOperacaoVeiculoError(String error);
}
```

**Uso:**
```java
public class VeiculoDetalhesFragment extends Fragment implements VeiculoListener {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SingletonVeiGest.getInstance(getContext()).setVeiculoListener(this);
    }
    
    private void guardarVeiculo() {
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate(etMatricula.getText().toString());
        vehicle.setBrand(etMarca.getText().toString());
        // ... outros campos
        
        SingletonVeiGest.getInstance(getContext()).addVeiculoAPI(vehicle);
    }
    
    @Override
    public void onOperacaoVeiculo(int operacao, Vehicle vehicle) {
        requireActivity().runOnUiThread(() -> {
            switch (operacao) {
                case OPERACAO_ADICIONAR:
                    Toast.makeText(getContext(), "Veículo adicionado!", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStack();
                    break;
                case OPERACAO_EDITAR:
                    Toast.makeText(getContext(), "Veículo atualizado!", Toast.LENGTH_SHORT).show();
                    break;
                case OPERACAO_REMOVER:
                    Toast.makeText(getContext(), "Veículo removido!", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStack();
                    break;
            }
        });
    }
    
    @Override
    public void onOperacaoVeiculoError(String error) {
        requireActivity().runOnUiThread(() -> {
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        });
    }
}
```

---

## Implementação nos Fragments

### Ciclo de Vida Completo

```java
public class ExemploFragment extends Fragment implements VeiculosListener {
    
    private SingletonVeiGest singleton;
    
    // ==========================================
    // 1. REGISTAR LISTENER em onCreate
    // ==========================================
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        singleton = SingletonVeiGest.getInstance(getContext());
        singleton.setVeiculosListener(this);
    }
    
    // ==========================================
    // 2. INICIAR CARREGAMENTO em onStart/onResume
    // ==========================================
    @Override
    public void onStart() {
        super.onStart();
        carregarDados();
    }
    
    private void carregarDados() {
        // Mostrar loading
        progressBar.setVisibility(View.VISIBLE);
        
        // Pedir dados ao Singleton
        singleton.getAllVeiculosAPI();
    }
    
    // ==========================================
    // 3. TRATAR CALLBACKS (sempre na UI Thread)
    // ==========================================
    @Override
    public void onRefreshVeiculos(ArrayList<Vehicle> vehicles) {
        // IMPORTANTE: Callbacks podem vir de background threads
        requireActivity().runOnUiThread(() -> {
            progressBar.setVisibility(View.GONE);
            
            adapter.clear();
            adapter.addAll(vehicles);
            adapter.notifyDataSetChanged();
            
            // Atualizar UI conforme necessário
            if (vehicles.isEmpty()) {
                textEmpty.setVisibility(View.VISIBLE);
            } else {
                textEmpty.setVisibility(View.GONE);
            }
        });
    }
    
    @Override
    public void onRefreshVeiculosError(String error) {
        requireActivity().runOnUiThread(() -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        });
    }
    
    // ==========================================
    // 4. REMOVER LISTENER em onDestroyView
    // ==========================================
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        
        // IMPORTANTE: Evitar memory leaks e callbacks em fragment destruído
        singleton.setVeiculosListener(null);
    }
}
```

### Fragment com Múltiplos Listeners

```java
public class DashboardFragment extends Fragment 
        implements VeiculosListener, RotasListener, AlertasListener {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        SingletonVeiGest singleton = SingletonVeiGest.getInstance(getContext());
        singleton.setVeiculosListener(this);
        singleton.setRotasListener(this);
        singleton.setAlertasListener(this);
    }
    
    @Override
    public void onStart() {
        super.onStart();
        
        // Carregar todos os dados
        SingletonVeiGest singleton = SingletonVeiGest.getInstance(getContext());
        singleton.getAllVeiculosAPI();
        singleton.getAllRotasAPI();
        singleton.getAllAlertasAPI();
    }
    
    // Callbacks de Veículos
    @Override
    public void onRefreshVeiculos(ArrayList<Vehicle> vehicles) {
        requireActivity().runOnUiThread(() -> {
            tvTotalVeiculos.setText(String.valueOf(vehicles.size()));
        });
    }
    
    @Override
    public void onRefreshVeiculosError(String error) {
        // tratar erro
    }
    
    // Callbacks de Rotas
    @Override
    public void onRefreshRotas(ArrayList<Route> routes) {
        requireActivity().runOnUiThread(() -> {
            long rotasAtivas = routes.stream()
                .filter(r -> "active".equals(r.getStatus()))
                .count();
            tvRotasAtivas.setText(String.valueOf(rotasAtivas));
        });
    }
    
    @Override
    public void onRefreshRotasError(String error) {
        // tratar erro
    }
    
    // Callbacks de Alertas
    @Override
    public void onRefreshAlertas(ArrayList<Alert> alerts) {
        requireActivity().runOnUiThread(() -> {
            long alertasPendentes = alerts.stream()
                .filter(a -> "pending".equals(a.getStatus()))
                .count();
            tvAlertasPendentes.setText(String.valueOf(alertasPendentes));
        });
    }
    
    @Override
    public void onRefreshAlertasError(String error) {
        // tratar erro
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        
        // Remover TODOS os listeners
        SingletonVeiGest singleton = SingletonVeiGest.getInstance(getContext());
        singleton.setVeiculosListener(null);
        singleton.setRotasListener(null);
        singleton.setAlertasListener(null);
    }
}
```

---

## Boas Práticas

### ✅ Fazer

```java
// Sempre verificar se Fragment ainda está attached
@Override
public void onRefreshVeiculos(ArrayList<Vehicle> vehicles) {
    if (!isAdded() || getActivity() == null) {
        return;  // Fragment já não está visível
    }
    
    requireActivity().runOnUiThread(() -> {
        // atualizar UI
    });
}

// Sempre remover listeners em onDestroyView
@Override
public void onDestroyView() {
    super.onDestroyView();
    singleton.setVeiculosListener(null);
}

// Usar runOnUiThread para atualizações de UI
requireActivity().runOnUiThread(() -> {
    textView.setText("...");
});

// Verificar null antes de chamar listener no Singleton
if (veiculosListener != null) {
    veiculosListener.onRefreshVeiculos(vehicles);
}
```

### ❌ Evitar

```java
// Não atualizar UI diretamente no callback
@Override
public void onRefreshVeiculos(ArrayList<Vehicle> vehicles) {
    textView.setText("...");  // ❌ Pode crashar se não estiver na UI Thread
}

// Não esquecer de remover listeners
// Se não remover: Memory Leak + Crashes

// Não ignorar erros
@Override
public void onRefreshVeiculosError(String error) {
    // fazer nada ❌
}

// Não assumir que getActivity() não é null
getActivity().runOnUiThread(...);  // ❌ Pode ser null
// Usar:
requireActivity().runOnUiThread(...);  // Ou verificar null primeiro
```

### Tabela de Verificações

| Verificação | Quando | Porquê |
|-------------|--------|--------|
| `isAdded()` | Em callbacks | Fragment pode ter sido removido |
| `getActivity() != null` | Antes de usar | Activity pode ser null |
| `listener != null` | No Singleton | Listener pode não estar registado |
| `runOnUiThread()` | Atualizar UI | Callbacks vêm de background |

---

## 📚 Documentação Relacionada

| Documento | Descrição |
|-----------|-----------|
| [02_VeiGest_SDK.md](02_VeiGest_SDK.md) | SingletonVeiGest |
| [03_Activities_Fragments.md](03_Activities_Fragments.md) | Ciclo de vida |
| [07_Integracao_API_REST.md](07_Integracao_API_REST.md) | Onde listeners são chamados |
| [10_Troubleshooting_Erros_Comuns.md](10_Troubleshooting_Erros_Comuns.md) | Erros com listeners |

---

**Última atualização:** Janeiro 2026
