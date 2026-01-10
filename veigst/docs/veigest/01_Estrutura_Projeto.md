# VeiGest - Estrutura do Projeto
## Organização de Pastas e Ficheiros

**ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO - 2025 / 2026**  
**TESP PROGRAMAÇÃO DE SISTEMAS DE INFORMAÇÃO**

---

## 📖 Índice

1. [Visão Geral da Estrutura](#visão-geral-da-estrutura)
2. [Módulo App](#módulo-app)
3. [Módulo VeiGest-SDK](#módulo-veigest-sdk)
4. [Ficheiros de Configuração](#ficheiros-de-configuração)
5. [Organização de Resources](#organização-de-resources)
6. [Convenções de Nomenclatura](#convenções-de-nomenclatura)

---

## Visão Geral da Estrutura

O projeto VeiGest é composto por dois módulos principais:

```
veigst/
├── app/                          # Aplicação principal Android
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/            # Código Java da aplicação
│   │   │   ├── res/             # Recursos (layouts, drawables, etc.)
│   │   │   └── AndroidManifest.xml
│   │   ├── androidTest/         # Testes instrumentados
│   │   └── test/                # Testes unitários
│   └── build.gradle.kts         # Configuração Gradle do módulo
│
├── veigest-sdk/                  # SDK reutilizável
│   ├── src/main/java/           # Código Java do SDK
│   │   └── com/veigest/sdk/
│   │       ├── SingletonVeiGest.java
│   │       ├── api/
│   │       ├── database/
│   │       ├── listeners/
│   │       ├── models/
│   │       └── utils/
│   └── build.gradle.kts
│
├── gradle/                       # Configurações do Gradle
│   ├── libs.versions.toml       # Versões de dependências
│   └── wrapper/
│
├── build.gradle.kts             # Build script principal
├── settings.gradle.kts          # Configuração dos módulos
└── gradle.properties            # Propriedades do Gradle
```

---

## Módulo App

O módulo `app/` contém a aplicação Android principal.

### Estrutura Java (`app/src/main/java/com/ipleiria/veigest/`)

```
com/ipleiria/veigest/
├── VeiGestApplication.java      # Application - Inicialização do SDK
├── MainActivity.java            # Activity principal com Navigation Drawer
│
├── LoginFragment.java           # Fragment de autenticação
├── RegisterFragment.java        # Fragment de registo de utilizador
├── DashboardFragment.java       # Fragment painel principal
├── VehiclesFragment.java        # Fragment lista de veículos
├── RoutesFragment.java          # Fragment lista de rotas
├── DocumentsFragment.java       # Fragment documentação
├── ProfileFragment.java         # Fragment perfil do utilizador
└── SettingsFragment.java        # Fragment configurações
```

### Descrição dos Ficheiros

| Ficheiro | Responsabilidade |
|----------|------------------|
| `VeiGestApplication.java` | Classe Application que inicializa o SDK no arranque da app |
| `MainActivity.java` | Activity única que hospeda todos os fragments e gerencia o Navigation Drawer |
| `LoginFragment.java` | Interface de login, implementa `LoginListener` |
| `RegisterFragment.java` | Interface de registo, implementa `RegisterListener` |
| `DashboardFragment.java` | Painel principal do condutor com resumo de informações |
| `VehiclesFragment.java` | Lista de veículos da frota |
| `RoutesFragment.java` | Rotas atribuídas ao condutor |
| `DocumentsFragment.java` | Gestão de documentos |
| `ProfileFragment.java` | Perfil e dados do utilizador |
| `SettingsFragment.java` | Configurações da aplicação |

---

## Módulo VeiGest-SDK

O módulo `veigest-sdk/` é uma biblioteca Android reutilizável.

### Estrutura do SDK (`veigest-sdk/src/main/java/com/veigest/sdk/`)

```
com/veigest/sdk/
├── SingletonVeiGest.java        # 🔑 Classe Singleton principal
│
├── api/
│   └── VeiGestApi.java          # Interface Retrofit (opcional)
│
├── database/
│   └── VeiGestBDHelper.java     # 💾 Helper SQLite para BD local
│
├── listeners/                    # 📢 Interfaces de callback
│   ├── LoginListener.java
│   ├── RegisterListener.java
│   ├── VeiculosListener.java
│   ├── VeiculoListener.java
│   ├── ManutencoesListener.java
│   ├── AbastecimentosListener.java
│   ├── AlertasListener.java
│   ├── DocumentosListener.java
│   └── RotasListener.java
│
├── models/                       # 📦 Classes de modelo (POJOs)
│   ├── User.java
│   ├── Vehicle.java
│   ├── Maintenance.java
│   ├── FuelLog.java
│   ├── Alert.java
│   ├── Document.java
│   ├── Route.java
│   ├── Company.java
│   ├── LoginRequest.java
│   ├── LoginResponse.java
│   ├── ApiResponse.java
│   └── ...
│
└── utils/
    └── VeiGestJsonParser.java   # 🔧 Utilitários de parsing JSON
```

### Descrição dos Componentes

#### SingletonVeiGest.java
Classe principal do SDK que:
- Mantém instância única (Singleton)
- Gerencia RequestQueue do Volley
- Guarda token de autenticação
- Faz chamadas à API REST
- Notifica listeners de resultados

#### VeiGestBDHelper.java
Helper SQLite que:
- Cria estrutura da base de dados
- Implementa operações CRUD
- Persiste dados para cache offline

#### Listeners (Interfaces)
Interfaces de callback para operações assíncronas:
- Notificam sucesso ou erro
- Desacoplam lógica de negócio da UI
- Permitem múltiplos observadores

#### Models (POJOs)
Classes que representam entidades:
- Implementam `Serializable`
- Usam anotações `@SerializedName` para Gson
- Contêm getters/setters

---

## Ficheiros de Configuração

### settings.gradle.kts

Define os módulos do projeto:

```kotlin
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "veigest"
include(":app")
include(":veigest-sdk")
```

### build.gradle.kts (raiz)

```kotlin
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
}
```

### gradle/libs.versions.toml

Catálogo de versões centralizado:

```toml
[versions]
agp = "8.9.0"
appcompat = "1.7.0"
material = "1.12.0"
volley = "1.2.1"
gson = "2.10.1"

[libraries]
appcompat = { group = "androidx.appcompat", name = "appcompat", version.ref = "appcompat" }
material = { group = "com.google.android.material", name = "material", version.ref = "material" }
volley = { group = "com.android.volley", name = "volley", version.ref = "volley" }
gson = { group = "com.google.code.gson", name = "gson", version.ref = "gson" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
android-library = { id = "com.android.library", version.ref = "agp" }
```

### app/build.gradle.kts

```kotlin
plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.ipleiria.veigest"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.ipleiria.veigest"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }
    // ...
}

dependencies {
    implementation(project(":veigest-sdk"))  // Dependência do SDK
    implementation(libs.appcompat)
    implementation(libs.material)
    // ...
}
```

---

## Organização de Resources

### Estrutura de res/ (`app/src/main/res/`)

```
res/
├── drawable/                    # Ícones e drawables vetoriais
│   ├── ic_dashboard_*.xml
│   ├── ic_menu_*.xml
│   ├── ic_launcher_*.xml
│   └── ic_veigest_*.xml
│
├── layout/                      # Layouts XML
│   ├── activity_main.xml        # Layout da MainActivity
│   ├── fragment_login.xml
│   ├── fragment_register.xml
│   ├── fragment_dashboard.xml
│   ├── fragment_vehicles.xml
│   ├── fragment_routes.xml
│   ├── fragment_documents.xml
│   ├── fragment_profile.xml
│   ├── fragment_settings.xml
│   └── nav_header.xml           # Header do Navigation Drawer
│
├── menu/
│   └── nav_drawer_menu.xml      # Menu do Navigation Drawer
│
├── mipmap-*/                    # Ícones da aplicação
│   └── ic_launcher*.xml
│
├── values/
│   ├── colors.xml               # Definição de cores
│   ├── strings.xml              # Textos da aplicação
│   ├── themes.xml               # Temas e estilos
│   └── attrs.xml                # Atributos customizados
│
├── values-night/
│   └── themes.xml               # Tema para modo escuro
│
└── xml/
    ├── backup_rules.xml
    └── data_extraction_rules.xml
```

### Convenções de Nomes para Resources

| Tipo | Prefixo | Exemplo |
|------|---------|---------|
| Layouts de Activity | `activity_` | `activity_main.xml` |
| Layouts de Fragment | `fragment_` | `fragment_login.xml` |
| Layouts de Item de Lista | `item_` | `item_vehicle.xml` |
| Ícones de Menu | `ic_menu_` | `ic_menu_dashboard.xml` |
| Ícones Gerais | `ic_` | `ic_veigest.xml` |
| Backgrounds | `bg_` | `bg_button.xml` |
| Cores | sem prefixo | `colorPrimary` |
| Strings | sem prefixo | `app_name` |

---

## Convenções de Nomenclatura

### Classes Java

| Tipo | Convenção | Exemplo |
|------|-----------|---------|
| Activity | `NomeActivity` | `MainActivity` |
| Fragment | `NomeFragment` | `LoginFragment` |
| Listener | `NomeListener` | `LoginListener` |
| Model | `Nome` (singular) | `Vehicle`, `User` |
| Helper BD | `NomeBDHelper` | `VeiGestBDHelper` |
| Singleton | `SingletonNome` | `SingletonVeiGest` |
| Application | `NomeApplication` | `VeiGestApplication` |

### Variáveis e Métodos

```java
// Views - prefixo indica tipo
private EditText etUsername;
private Button btnLogin;
private TextView tvWelcome;
private ImageView ivLogo;
private ProgressBar progressBar;
private MaterialCardView cardVehicle;

// Métodos - verbos descritivos
private void initializeViews(View view) { }
private void setupListeners() { }
private void performLogin(String email, String password) { }
private void loadUserData() { }

// Callbacks - prefixo on
@Override
public void onValidateLogin(String token, User user) { }
public void onLoginError(String errorMessage) { }
```

### IDs em XML

```xml
<!-- Padrão: tipo_nome_contexto -->
<EditText android:id="@+id/et_username" />
<Button android:id="@+id/btn_login" />
<TextView android:id="@+id/tv_welcome" />
<ImageView android:id="@+id/iv_logo" />
<ProgressBar android:id="@+id/progress_bar" />

<!-- Cards e Containers -->
<MaterialCardView android:id="@+id/card_active_route" />
<FrameLayout android:id="@+id/fragment_container" />
```

---

## 📚 Documentação Relacionada

| Documento | Descrição |
|-----------|-----------|
| [00_Introducao_VeiGest.md](00_Introducao_VeiGest.md) | Introdução ao projeto |
| [02_VeiGest_SDK.md](02_VeiGest_SDK.md) | Documentação do SDK |
| [05_Layouts_XML.md](05_Layouts_XML.md) | Layouts e recursos XML |

---

**Última atualização:** Janeiro 2026
