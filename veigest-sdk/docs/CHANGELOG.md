# Changelog

Todas as alterações notáveis do VeiGest SDK serão documentadas neste ficheiro.

O formato é baseado em [Keep a Changelog](https://keepachangelog.com/pt-BR/1.0.0/),
e este projeto adere ao [Semantic Versioning](https://semver.org/lang/pt-BR/).

---

## [1.0.0] - 2024-12-23

### Adicionado

#### Core
- **VeiGestSDK** - Classe principal do SDK com inicialização e acesso a todos os serviços
- **VeiGestConfig** - Configuração com Builder pattern (baseUrl, timeouts, debug mode)
- **VeiGestCallback** - Interface de callback assíncrono para todas as operações
- **VeiGestException** - Sistema de exceções com tipos de erro categorizados

#### Serviços
- **AuthService** - Autenticação completa (login, logout, register, refresh token)
- **VehicleService** - CRUD de veículos, estatísticas, manutenções e rotas associadas
- **UserService** - CRUD de utilizadores, gestão de condutores
- **MaintenanceService** - CRUD de manutenções, agendamento, conclusão e cancelamento
- **FuelLogService** - CRUD de abastecimentos, estatísticas e eficiência
- **RouteService** - CRUD de rotas, tracking GPS, início e finalização
- **DocumentService** - CRUD de documentos, controlo de validade
- **AlertService** - Gestão de alertas, marcação como lido
- **ReportService** - Relatórios e dashboards, exportação PDF/Excel
- **CompanyService** - Gestão de empresas (multi-tenant)
- **TicketService** - Sistema de tickets de suporte
- **FileService** - Upload e download de ficheiros

#### Modelos
- `User` - Modelo de utilizador
- `Vehicle` - Modelo de veículo
- `Company` - Modelo de empresa
- `Maintenance` - Modelo de manutenção
- `FuelLog` - Modelo de abastecimento
- `Route` - Modelo de rota
- `GpsEntry` - Modelo de ponto GPS
- `Document` - Modelo de documento
- `Alert` - Modelo de alerta
- `Ticket` - Modelo de ticket
- `FileInfo` - Modelo de informação de ficheiro
- `LoginResponse` - Resposta de login
- `ReportStats` - Estatísticas gerais
- `VehicleStats` - Estatísticas de veículo
- `CompanyStats` - Estatísticas de empresa
- `MaintenanceReport` - Relatório de manutenção
- `FuelEfficiencyReport` - Relatório de eficiência de combustível
- `FuelAlert` - Alerta de combustível
- `ApiResponse` - Wrapper de resposta da API

#### Segurança
- Armazenamento seguro de tokens com EncryptedSharedPreferences
- Interceptor automático para autenticação
- Refresh automático de token em caso de expiração

#### Builders
- `VehicleBuilder` - Builder para criação de veículos
- `UserBuilder` - Builder para criação de utilizadores
- `MaintenanceBuilder` - Builder para criação de manutenções
- `ScheduleBuilder` - Builder para agendamento de manutenções
- `FuelLogBuilder` - Builder para criação de abastecimentos
- `RouteBuilder` - Builder para criação de rotas
- `DocumentBuilder` - Builder para criação de documentos
- `TicketBuilder` - Builder para criação de tickets
- `AlertBuilder` - Builder para criação de alertas
- `CompanyBuilder` - Builder para criação de empresas

### Dependências
- Retrofit 2.9.0
- OkHttp 4.12.0
- Gson 2.10.1
- AndroidX Security 1.1.0-alpha06

### Requisitos
- Android SDK 24+ (Android 7.0 Nougat)
- Java 11

---

## Roadmap

### [1.1.0] - Planeado

#### Funcionalidades
- [ ] Suporte a WebSockets para atualizações em tempo real
- [ ] Cache local com Room Database
- [ ] Modo offline com sincronização
- [ ] Compressão automática de imagens no upload
- [ ] Progress callback para uploads/downloads grandes

#### Melhorias
- [ ] Retry automático em erros de rede
- [ ] Batching de requisições
- [ ] Suporte a Kotlin Coroutines
- [ ] Extensões Kotlin

### [1.2.0] - Futuro

#### Funcionalidades
- [ ] Integração com Firebase Analytics
- [ ] Push notifications para alertas
- [ ] Suporte a multi-idioma
- [ ] Mapa integrado para visualização de rotas

---

## Notas de Versão

### Compatibilidade
- O SDK é compatível com Android 7.0 (API 24) e superior
- Requer Java 11 ou superior
- Funciona com Gradle 8.x

### Migrações
- Esta é a primeira versão - não há migrações necessárias

### Problemas Conhecidos
- Nenhum problema conhecido na versão inicial

---

## Contribuição

Para reportar bugs ou sugerir funcionalidades:
1. Abra uma issue no repositório GitHub
2. Use os templates de issue disponíveis
3. Inclua versão do SDK, Android e passos para reproduzir

---

## Links Úteis

- [Documentação Principal](README.md)
- [Quick Start](QUICK_START.md)
- [API Reference](API_REFERENCE.md)
- [Troubleshooting](TROUBLESHOOTING.md)
