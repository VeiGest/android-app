# Dashboard do Condutor - VeiGest

## 📱 Visão Geral

O Dashboard foi criado como interface principal para condutores da aplicação VeiGest. É um mockup funcional que exibe informações essenciais para o dia-a-dia do condutor de forma simples e intuitiva.

## 🎯 Características Implementadas

### 1. **Header com Informações do Condutor**
- Nome do condutor
- Mensagem de boas-vindas
- Botão de logout (mockado)

### 2. **Card de Rotas Ativas**
Exibe informação sobre a rota atual do condutor:
- Origem da rota
- Destino da rota
- Distância total
- Tempo estimado de chegada (ETA)
- Botão para ver detalhes completos da rota

### 3. **Card do Veículo Atual**
Informações do veículo atribuído ao condutor:
- Matrícula do veículo
- Modelo do veículo
- Quilometragem atual
- Nível de combustível
- Botão para ver detalhes completos do veículo

### 4. **Card de Documentação**
Status da documentação importante:
- **Carta de Condução**: Data de expiração e status
- **Seguro do Veículo**: Data de expiração e status
- **Inspeção do Veículo**: Data de expiração e status
- Indicadores visuais de status (Válido/Expirado)
- Botão para ver todos os documentos

### 5. **Ações Rápidas (Grid 2x2)**
Acesso rápido a funcionalidades importantes:
- **Reportar Problema**: Para reportar incidentes ou problemas
- **Minhas Rotas**: Histórico e lista de rotas
- **Histórico**: Consultar histórico de viagens
- **Definições**: Configurações da aplicação

## 📂 Arquivos Criados

### Layouts
- `fragment_dashboard.xml` - Layout principal do dashboard com ScrollView e CardViews

### Classes Java
- `DashboardFragment.java` - Fragment com lógica do dashboard e dados mockados

### Recursos
- `ic_dashboard_route.xml` - Ícone para rotas
- `ic_dashboard_vehicle.xml` - Ícone para veículos
- `ic_dashboard_document.xml` - Ícone para documentação

### Strings (values/strings.xml)
Adicionadas strings em português para:
- Boas-vindas e navegação
- Labels de rotas (origem, destino, distância, ETA)
- Labels de veículos (matrícula, modelo, km, combustível)
- Labels de documentação
- Ações rápidas

## 🔧 Navegação Implementada

### MainActivity
Atualizada com métodos para navegação entre fragments:
- `loadFragment(Fragment)` - Método genérico para carregar fragments
- `loadDashboard()` - Carrega o dashboard após login

### LoginFragment
Atualizado para navegar para o dashboard após login bem-sucedido:
- Validação básica de campos
- Simulação de login (mockado)
- Navegação para DashboardFragment

## 📊 Dados Mockados

Todos os dados atualmente exibidos são mockados para demonstração:

```java
// Condutor
Nome: "João Silva"

// Rota Ativa
Origem: "Leiria, Portugal"
Destino: "Lisboa, Portugal"
Distância: "145 km"
ETA: "1h 45min"

// Veículo
Matrícula: "AA-12-BB"
Modelo: "Mercedes Actros"
Quilometragem: "125.340 km"
Combustível: "75%"

// Documentação
Carta de Condução: Expira 15/08/2026 (Válida)
Seguro: Expira 30/06/2025 (Válido)
Inspeção: Expira 22/03/2025 (Válida)
```

## 🚀 Próximos Passos (TODOs)

### 1. Integração com API
Substituir dados mockados por chamadas reais aos endpoints:

```java
// Endpoints a integrar (API_ENDPOINTS.md)
GET /drivers/{id}                    // Dados do condutor
GET /routes?status=active&driver_id  // Rotas ativas
GET /vehicles/{id}                   // Dados do veículo
GET /documents?driver_id={id}        // Documentação
```

### 2. Funcionalidades de Navegação
Implementar navegação para as seguintes telas:
- `RouteDetailsFragment` - Detalhes completos da rota
- `VehicleDetailsFragment` - Informações completas do veículo
- `DocumentationFragment` - Lista completa de documentos
- `ReportIssueFragment` - Formulário para reportar problemas
- `MyRoutesFragment` - Lista de rotas do condutor
- `HistoryFragment` - Histórico de viagens
- `SettingsFragment` - Configurações da aplicação

### 3. Autenticação Real
- Implementar chamada ao endpoint `POST /auth/login`
- Guardar token JWT em SharedPreferences
- Adicionar interceptor HTTP para incluir token
- Verificar sessão ativa ao iniciar app
- Implementar logout com limpeza de sessão

### 4. Notificações
- Alertas de documentos próximos de expirar
- Notificações de novas rotas atribuídas
- Atualizações de status da rota

### 5. Melhorias de UI/UX
- Adicionar pull-to-refresh para atualizar dados
- Implementar skeleton loaders durante carregamento
- Adicionar animações entre transições
- Modo offline com cache de dados

### 6. Sistema de Refresh
- Auto-refresh periódico dos dados
- Indicador visual de última atualização
- Sincronização em background

## 🎨 Design

### Tema
O dashboard respeita o tema da aplicação:
- Suporte a modo claro e escuro
- Cores personalizadas via atributos (`?attr/...`)
- Cards com Material Design 3
- Ícones consistentes com a marca VeiGest

### Responsividade
- ScrollView para suportar diferentes tamanhos de tela
- Grid adaptativo para ações rápidas
- Layouts flexíveis com peso (weight)

## 📱 Como Testar

1. Executar a aplicação no emulador ou dispositivo
2. Fazer login (qualquer username/password funcionará no mockup)
3. O dashboard será carregado automaticamente
4. Clicar nos botões mostrará mensagens Toast indicando funcionalidades futuras
5. Testar em modo claro e escuro

## 🔗 Referências

- **API Documentation**: `API_ENDPOINTS.md`
- **Project Documentation**: `projeto.md`
- **Database Schema**: `schema_simplifyed.sql`
