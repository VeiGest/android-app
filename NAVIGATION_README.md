# VeiGest - Sistema de Navegação e Páginas Adicionais

## 📱 Resumo das Alterações

Este commit adiciona um sistema completo de navegação lateral (sidebar) e novas páginas para a aplicação VeiGest Android.

## ✨ Novas Funcionalidades

### 1. **Sistema de Navegação com Sidebar**
- Implementado `DrawerLayout` com `NavigationView`
- Menu lateral deslizante com ícones personalizados
- Header do drawer com informações do usuário
- Bloqueio/desbloqueio automático da sidebar baseado no estado de login

### 2. **Novas Páginas (Fragments)**

#### **RoutesFragment** - Gestão de Rotas
- Exibe lista de rotas (ativas, pendentes, concluídas)
- Cards com informações de origem, destino, distância e ETA
- Status visual com cores diferentes para cada estado

#### **VehiclesFragment** - Gestão de Veículos
- Lista de veículos atribuídos ao condutor
- Informações de matrícula, modelo, quilometragem e combustível
- Indicadores visuais de status do veículo

#### **DocumentsFragment** - Documentação
- Exibe documentos do condutor (carta de condução, certificados, seguros)
- Prazos de validade com alertas visuais
- Status de validação dos documentos

#### **ProfileFragment** - Perfil do Usuário
- Informações pessoais do condutor
- Dados de contato e credenciais profissionais
- Botão para editar perfil (mockup)

#### **SettingsFragment** - Configurações
Configurações completas incluindo:
- **API Settings**: Endereço da API e ID da empresa
- **Aparência**: Seleção de tema (Claro, Escuro, Sistema)
- **Notificações**: Toggle para notificações e sons
- **Sincronização**: Configuração de sync automático
- **Sobre**: Versão da aplicação

### 3. **Melhorias no Dashboard**
- Adicionado botão de menu (hamburger) no header
- Integração com a sidebar para navegação rápida
- Mantidas todas as funcionalidades existentes

### 4. **MainActivity Atualizado**
- Implementação do `NavigationView.OnNavigationItemSelectedListener`
- Gerenciamento do estado de login
- Controle de acesso à sidebar (bloqueada até login)
- Navegação entre fragments via menu lateral
- Função de logout com retorno ao LoginFragment

## 🎨 Recursos Visuais Adicionados

### Ícones Criados
- `ic_menu_dashboard.xml` - Ícone do dashboard
- `ic_menu_routes.xml` - Ícone de rotas/localização
- `ic_menu_profile.xml` - Ícone de perfil
- `ic_menu_settings.xml` - Ícone de configurações
- `ic_menu_logout.xml` - Ícone de logout
- `ic_menu_hamburger.xml` - Ícone do menu lateral

### Layouts Criados
- `fragment_routes.xml` - Layout da página de rotas
- `fragment_vehicles.xml` - Layout da página de veículos
- `fragment_documents.xml` - Layout da página de documentos
- `fragment_profile.xml` - Layout da página de perfil
- `fragment_settings.xml` - Layout da página de configurações
- `nav_drawer_menu.xml` - Menu do drawer de navegação
- `nav_header.xml` - Header do drawer com info do usuário

## 📝 Strings Adicionadas

Todas as strings necessárias foram adicionadas em `strings.xml`:
- Menu de navegação
- Títulos de páginas
- Labels de configurações
- Status e estados
- Mensagens de interface

## 🔧 Configurações Implementadas (Mockup)

### API Settings
- **Endereço da API**: Campo editável para URL da API
- **Empresa da API**: Campo para ID da empresa
- Valores padrão: `https://api.veigest.com/v1` e empresa `1`

### Tema
- Opções: Claro, Escuro, Sistema
- Seleção via RadioGroup
- Padrão: Sistema

### Notificações
- Toggle para ativar/desativar notificações
- Toggle para sons de notificação
- Padrão: Ambos ativados

### Sincronização
- Toggle para sincronização automática
- Padrão: Ativado

## 🚀 Como Usar

1. **Login**: Faça login na aplicação (tela inicial)
2. **Dashboard**: Após login, você é direcionado ao dashboard
3. **Abrir Sidebar**: 
   - Clique no ícone de menu (☰) no topo esquerdo do dashboard
   - Ou deslize da esquerda para direita
4. **Navegar**: Selecione qualquer item do menu para navegar
5. **Configurações**: Acesse as configurações pelo menu e ajuste conforme necessário
6. **Logout**: Use o botão de logout no menu para sair

## 📂 Estrutura de Arquivos

```
app/src/main/
├── java/com/ipleiria/veigest/
│   ├── MainActivity.java (atualizado)
│   ├── DashboardFragment.java (atualizado)
│   ├── RoutesFragment.java (novo)
│   ├── VehiclesFragment.java (novo)
│   ├── DocumentsFragment.java (novo)
│   ├── ProfileFragment.java (novo)
│   └── SettingsFragment.java (novo)
└── res/
    ├── drawable/
    │   ├── ic_menu_dashboard.xml (novo)
    │   ├── ic_menu_routes.xml (novo)
    │   ├── ic_menu_profile.xml (novo)
    │   ├── ic_menu_settings.xml (novo)
    │   ├── ic_menu_logout.xml (novo)
    │   └── ic_menu_hamburger.xml (novo)
    ├── layout/
    │   ├── activity_main.xml (atualizado)
    │   ├── fragment_dashboard.xml (atualizado)
    │   ├── fragment_routes.xml (novo)
    │   ├── fragment_vehicles.xml (novo)
    │   ├── fragment_documents.xml (novo)
    │   ├── fragment_profile.xml (novo)
    │   ├── fragment_settings.xml (novo)
    │   └── nav_header.xml (novo)
    ├── menu/
    │   └── nav_drawer_menu.xml (novo)
    └── values/
        └── strings.xml (atualizado)
```

## ⚠️ Notas Importantes

- **Mockup**: Todas as funcionalidades são mockups. Os dados são hardcoded e não conectam a API.
- **Próximos Passos**: 
  - Implementar conexão real com API
  - Adicionar persistência de dados (SharedPreferences/Room)
  - Implementar autenticação real
  - Adicionar validações nos formulários de settings
  - Implementar tema dinâmico baseado na seleção

## 🎯 Requisitos Atendidos

✅ Criação de páginas adicionais (Rotas, Veículos, Documentos, Perfil, Configurações)  
✅ Sidebar de navegação funcional  
✅ Configurações com:  
  - Endereço da API  
  - Empresa da API  
  - Tema padrão  
  - Outras configurações pertinentes (notificações, sync)  
✅ Todas as páginas conectadas ao Dashboard  
✅ Tudo em modo mockup (sem funcionalidade backend)

## 🔄 Fluxo de Navegação

```
LoginFragment
    ↓
DashboardFragment (com sidebar ativada)
    ├── RoutesFragment
    ├── VehiclesFragment
    ├── DocumentsFragment
    ├── ProfileFragment
    ├── SettingsFragment
    └── Logout → LoginFragment
```

---

**Desenvolvido para**: VeiGest - Sistema de Gestão de Frotas  
**Data**: 03/12/2025  
**Branch**: main-dashboard
