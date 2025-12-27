# Passando a instância do SDK entre Fragments

Este documento explica formas seguras e recomendadas para partilhar/aceder à instância do VeiGest SDK (após login) entre diferentes *views*/*fragments* na aplicação.

**Onde colocar a instância:**
- Recomendado: inicializar o SDK no `Application` (já existe `VeiGestApplication`) e aceder a partir daí.
- Alternativa recomendada para dados transitórios: usar um `Shared ViewModel` com *activity scope*.
- Não recomendado: serializar a instância do SDK num `Bundle`/`Parcelable` ou armazenar token em texto plano em intents.

**Arquivos de referência**
- [app/src/main/java/com/ipleiria/veigest/VeiGestApplication.java](app/src/main/java/com/ipleiria/veigest/VeiGestApplication.java)
- [app/src/main/java/com/ipleiria/veigest/LoginFragment.java](app/src/main/java/com/ipleiria/veigest/LoginFragment.java)
- [app/src/main/java/com/ipleiria/veigest/DashboardFragment.java](app/src/main/java/com/ipleiria/veigest/DashboardFragment.java)

**1) Acesso via Application singleton (mais simples)**

- Ideia: a aplicação inicializa uma única instância do `VeiGestSDK`. Fragments obtêm a instância através do `Application`.

Exemplo (esquema, Java):

```java
// VeiGestApplication.java (exemplo de getter)
public class VeiGestApplication extends Application {
    private VeiGestSDK veiGestSdk;

    @Override
    public void onCreate() {
        super.onCreate();
        VeiGestConfig config = new VeiGestConfig.Builder()
            .setBaseUrl("https://api.veigest.com/")
            .build();
        veiGestSdk = new VeiGestSDK(this, config);
    }

    public VeiGestSDK getVeiGestSdk(){
        return veiGestSdk;
    }
}
```

Uso dentro de um Fragment:

```java
VeiGestSDK sdk = ((VeiGestApplication) requireActivity().getApplication()).getVeiGestSdk();
// agora pode usar sdk.auth(), sdk.users(), etc.
```

Vantagens:
- Simples, centralizado e seguro.
- `AuthManager` interno do SDK guarda/renova tokens; o fragment só usa `sdk`.

**2) Shared ViewModel (recomendado para dados transitórios / seleção após login)**

- Use um `ViewModel` partilhado entre fragments ligados à mesma Activity quando precisar propagar pequenos objetos, flags ou seleções (por exemplo, veículo selecionado, usuário carregado) sem acoplar UI ao `Application` diretamente.

Exemplo (Java):

```java
// SharedSdkViewModel.java
public class SharedSdkViewModel extends ViewModel {
    private VeiGestSDK sdk;

    public void setSdk(VeiGestSDK sdk){ this.sdk = sdk; }
    public VeiGestSDK getSdk(){ return sdk; }
}
```

No `LoginFragment` (após sucesso do login):

```java
// obter SDK do Application
VeiGestSDK appSdk = ((VeiGestApplication) requireActivity().getApplication()).getVeiGestSdk();
// guardar no ViewModel partilhado
SharedSdkViewModel vm = new ViewModelProvider(requireActivity()).get(SharedSdkViewModel.class);
vm.setSdk(appSdk);

// navegar para o Dashboard (o Dashboard obterá o SDK do VM)
```

No `DashboardFragment`:

```java
SharedSdkViewModel vm = new ViewModelProvider(requireActivity()).get(SharedSdkViewModel.class);
VeiGestSDK sdk = vm.getSdk();
if (sdk == null) {
    // fallback: ler do Application (mais seguro)
    sdk = ((VeiGestApplication) requireActivity().getApplication()).getVeiGestSdk();
}
// usar sdk para carregar dados
```

Vantagens:
- Evita chamadas repetidas ao `Application` e mantém o estado da UI enquanto a Activity existe.
- Boa para compartilhar seleções, flags ou dados carregados em memória.

**3) Passagem via Bundle / SafeArgs (apenas dados pequenos)**

- Quando precisar passar apenas um `id` ou flag (por exemplo `userId`, `isFirstLogin`) entre fragments, use `Bundle`/`SafeArgs`.
- Evite passar objetos complexos (como a instância do SDK) no `Bundle`.

Exemplo simples:

```java
Bundle args = new Bundle();
args.putString("userId", user.getId());
navController.navigate(R.id.action_login_to_dashboard, args);
```

No `DashboardFragment`:

```java
String userId = getArguments().getString("userId");
```

**Boas práticas e observações**
- Confie no `AuthManager` do SDK para armazenar/renovar tokens (EncryptedSharedPreferences já usado no SDK). Evite expor o token em texto plano.
- O SDK faz chamadas assíncronas e normalmente invoca callbacks no *main thread* (veja `BaseService` no SDK). Atualize UI dentro dos callbacks com segurança.
- Sempre verifique `sdk.auth().isAuthenticated()` antes de fazer chamadas protegidas.
- Use `Application` singleton para a instância única do SDK + `Shared ViewModel` para estado transitório entre fragments.
- Para arquiteturas maiores, considere usar DI (Hilt) e fornecer `VeiGestSDK` como singleton injetável.

**Exemplo rápido de fluxo recomendado**
1. `VeiGestApplication` inicializa o `VeiGestSDK`.
2. `LoginFragment` usa `((VeiGestApplication) getApplication()).getVeiGestSdk()` para autenticar.
3. Após login bem-sucedido, `LoginFragment` popula `SharedSdkViewModel` (opcional) e navega para `DashboardFragment`.
4. `DashboardFragment` obtém o SDK do `SharedSdkViewModel` (ou diretamente do `Application`) e carrega dados.

---

Se quiser, eu adapto estes exemplos diretamente para os ficheiros do projeto (`LoginFragment.java` e `DashboardFragment.java`) ou adiciono exemplos em Kotlin/Hilt. Quer que eu insira o código de `SharedSdkViewModel.java` no projeto agora?