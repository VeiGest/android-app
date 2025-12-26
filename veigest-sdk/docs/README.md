# VeiGest Android SDK - Documentação Completa

[![API Version](https://img.shields.io/badge/API-v1.0.0-blue.svg)](https://veigestback.dryadlang.org)
[![Platform](https://img.shields.io/badge/platform-Android-green.svg)](https://developer.android.com)
[![Min SDK](https://img.shields.io/badge/minSdk-24-yellow.svg)](https://developer.android.com)
[![License](https://img.shields.io/badge/license-MIT-lightgrey.svg)](LICENSE)

SDK Android oficial para integração com a **API VeiGest** - Sistema de Gestão de Frotas. Este SDK fornece uma interface simples e intuitiva para todas as operações de gestão de frotas, incluindo veículos, manutenções, abastecimentos, rotas, documentos e muito mais.

---

## 📚 Índice da Documentação

### Guias Principais
| Documento | Descrição |
|-----------|-----------|
| [Início Rápido](QUICK_START.md) | Guia rápido para começar a usar o SDK |
| [Instalação](INSTALLATION.md) | Instruções detalhadas de instalação |
| [Configuração](CONFIGURATION.md) | Opções de configuração do SDK |
| [Autenticação](AUTHENTICATION.md) | Gestão de login, logout e tokens |

### Referência de API
| Documento | Descrição |
|-----------|-----------|
| [Serviços](SERVICES.md) | Documentação completa de todos os serviços |
| [Modelos](MODELS.md) | Referência de todos os modelos de dados |
| [Callbacks](CALLBACKS.md) | Padrões de callback e tratamento de erros |
| [Exceções](EXCEPTIONS.md) | Tipos de exceções e tratamento de erros |

### Guias Avançados
| Documento | Descrição |
|-----------|-----------|
| [Builders](BUILDERS.md) | Padrões Builder para criação de objetos |
| [Upload de Ficheiros](FILE_UPLOAD.md) | Guia completo de upload/download |
| [Boas Práticas](BEST_PRACTICES.md) | Recomendações e padrões de uso |
| [Troubleshooting](TROUBLESHOOTING.md) | Resolução de problemas comuns |

### Referências Rápidas
| Documento | Descrição |
|-----------|-----------|
| [API Reference](API_REFERENCE.md) | Referência rápida de todos os endpoints |
| [Changelog](CHANGELOG.md) | Histórico de versões e alterações |

---

## 🏗️ Arquitetura do SDK

```
┌─────────────────────────────────────────────────────────────────┐
│                         VeiGestSDK                               │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │                    Serviços (Services)                    │   │
│  │  ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────────────┐ │   │
│  │  │  Auth   │ │ Vehicle │ │  User   │ │  Maintenance    │ │   │
│  │  └─────────┘ └─────────┘ └─────────┘ └─────────────────┘ │   │
│  │  ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────────────┐ │   │
│  │  │ FuelLog │ │  Route  │ │Document │ │     Alert       │ │   │
│  │  └─────────┘ └─────────┘ └─────────┘ └─────────────────┘ │   │
│  │  ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────────────┐ │   │
│  │  │ Report  │ │ Company │ │ Ticket  │ │      File       │ │   │
│  │  └─────────┘ └─────────┘ └─────────┘ └─────────────────┘ │   │
│  └──────────────────────────────────────────────────────────┘   │
│                              │                                   │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │                   VeiGestApiClient                        │   │
│  │  ┌────────────────────┐  ┌────────────────────────────┐  │   │
│  │  │    VeiGestApi      │  │     AuthInterceptor        │  │   │
│  │  │   (Retrofit)       │  │   (Token Management)       │  │   │
│  │  └────────────────────┘  └────────────────────────────┘  │   │
│  └──────────────────────────────────────────────────────────┘   │
│                              │                                   │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │                     AuthManager                           │   │
│  │  ┌────────────────────────────────────────────────────┐  │   │
│  │  │         EncryptedSharedPreferences                 │  │   │
│  │  │         (Secure Token Storage)                     │  │   │
│  │  └────────────────────────────────────────────────────┘  │   │
│  └──────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
```

---

## 📦 Estrutura do Pacote

```
com.veigest.sdk/
├── VeiGestSDK.java              # Ponto de entrada principal
├── VeiGestConfig.java           # Configurações do SDK
├── VeiGestCallback.java         # Interface de callback
├── VeiGestException.java        # Exceções personalizadas
├── api/
│   ├── VeiGestApi.java          # Interface Retrofit
│   └── VeiGestApiClient.java    # Cliente HTTP
├── auth/
│   └── AuthManager.java         # Gestão de tokens
├── models/                      # Modelos de dados (20 classes)
│   ├── Alert.java
│   ├── ApiResponse.java
│   ├── Company.java
│   ├── CompanyStats.java
│   ├── Document.java
│   ├── FileInfo.java
│   ├── FuelAlert.java
│   ├── FuelEfficiencyReport.java
│   ├── FuelLog.java
│   ├── GpsEntry.java
│   ├── LoginRequest.java
│   ├── LoginResponse.java
│   ├── Maintenance.java
│   ├── MaintenanceReport.java
│   ├── ReportStats.java
│   ├── Route.java
│   ├── Ticket.java
│   ├── User.java
│   ├── Vehicle.java
│   └── VehicleStats.java
└── services/                    # Serviços (13 classes)
    ├── AlertService.java
    ├── AuthService.java
    ├── BaseService.java
    ├── CompanyService.java
    ├── DocumentService.java
    ├── FileService.java
    ├── FuelLogService.java
    ├── MaintenanceService.java
    ├── ReportService.java
    ├── RouteService.java
    ├── TicketService.java
    ├── UserService.java
    └── VehicleService.java
```

---

## 🚀 Início Rápido (5 minutos)

### 1. Adicionar Dependência

```kotlin
// settings.gradle.kts
include(":veigest-sdk")

// app/build.gradle.kts
dependencies {
    implementation(project(":veigest-sdk"))
}
```

### 2. Inicializar o SDK

```java
// Na Application ou Activity principal
VeiGestConfig config = new VeiGestConfig.Builder()
    .setBaseUrl("https://veigestback.dryadlang.org")
    .setDebugMode(BuildConfig.DEBUG)
    .build();

VeiGestSDK sdk = VeiGestSDK.init(getApplicationContext(), config);
```

### 3. Fazer Login

```java
sdk.auth().login("user@email.com", "password", new VeiGestCallback<LoginResponse>() {
    @Override
    public void onSuccess(@NonNull LoginResponse response) {
        // Login bem sucedido!
        User user = response.getUser();
        Log.d("VeiGest", "Bem-vindo, " + user.getNome());
    }
    
    @Override
    public void onError(@NonNull VeiGestException error) {
        // Tratar erro
        Log.e("VeiGest", error.getMessage());
    }
});
```

### 4. Usar os Serviços

```java
// Listar veículos
sdk.vehicles().list(new VeiGestCallback<List<Vehicle>>() {
    @Override
    public void onSuccess(@NonNull List<Vehicle> vehicles) {
        for (Vehicle v : vehicles) {
            Log.d("Vehicle", v.getMatricula() + " - " + v.getMarca());
        }
    }
    
    @Override
    public void onError(@NonNull VeiGestException error) {
        // Tratar erro
    }
});
```

---

## 📊 Serviços Disponíveis

| Serviço | Acesso | Descrição |
|---------|--------|-----------|
| `AuthService` | `sdk.auth()` | Autenticação, login, logout, tokens |
| `VehicleService` | `sdk.vehicles()` | CRUD de veículos, estatísticas |
| `UserService` | `sdk.users()` | CRUD de utilizadores, condutores |
| `MaintenanceService` | `sdk.maintenances()` | Manutenções, agendamentos, relatórios |
| `FuelLogService` | `sdk.fuelLogs()` | Abastecimentos, alertas, eficiência |
| `RouteService` | `sdk.routes()` | Rotas, tracking GPS |
| `DocumentService` | `sdk.documents()` | Documentos, validades |
| `AlertService` | `sdk.alerts()` | Alertas do sistema |
| `ReportService` | `sdk.reports()` | Relatórios e estatísticas |
| `CompanyService` | `sdk.companies()` | Gestão de empresas |
| `TicketService` | `sdk.tickets()` | Tickets/bilhetes de suporte |
| `FileService` | `sdk.files()` | Upload/download de ficheiros |

---

## 🔧 Requisitos

| Requisito | Versão |
|-----------|--------|
| Android SDK | 24+ (Android 7.0) |
| Target SDK | 36 |
| Java | 11 |
| Kotlin (opcional) | 1.9+ |

### Dependências

| Biblioteca | Versão | Uso |
|------------|--------|-----|
| Retrofit | 2.9.0 | HTTP Client |
| OkHttp | 4.12.0 | Networking |
| Gson | 2.10.1 | JSON Parsing |
| AndroidX Security | 1.1.0-alpha06 | Encrypted Storage |

---

## 📝 Changelog

### v1.0.0 (Dezembro 2024)
- Release inicial
- Suporte completo a todos os endpoints da API VeiGest
- Autenticação com tokens JWT
- Armazenamento seguro de credenciais
- Upload/download de ficheiros
- Relatórios e estatísticas

---

## 📄 Licença

Este SDK é distribuído sob a licença MIT. Veja o arquivo [LICENSE](../LICENSE) para mais detalhes.

---

## 🤝 Suporte

- **Documentação**: [docs/](.)
- **Issues**: [GitHub Issues](https://github.com/VeiGest/android-sdk/issues)
- **Email**: suporte@veigest.com

---

**VeiGest SDK** © 2024 - Sistema de Gestão de Frotas
