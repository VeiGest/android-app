# Dashboard - Estrutura Visual

```
┌──────────────────────────────────────────────┐
│  🚛 VeiGest    Bem-vindo        [↪ Logout]  │
│                João Silva                    │
├──────────────────────────────────────────────┤
│                                              │
│  📍 ROTAS ATIVAS                             │
│  ┌──────────────────────────────────────┐   │
│  │ Origem: Leiria, Portugal            │   │
│  │ Destino: Lisboa, Portugal           │   │
│  │                                     │   │
│  │ Distância: 145 km  |  ETA: 1h 45min │   │
│  │                                     │   │
│  │ [  Ver Detalhes da Rota  ]          │   │
│  └──────────────────────────────────────┘   │
│                                              │
│  🚗 VEÍCULO ATUAL                            │
│  ┌──────────────────────────────────────┐   │
│  │ Matrícula: AA-12-BB | Modelo: Mercedes│   │
│  │ Actros                              │   │
│  │                                     │   │
│  │ Quilometragem: 125.340 km           │   │
│  │ Combustível: 75%                    │   │
│  │                                     │   │
│  │ [  Ver Detalhes do Veículo  ]       │   │
│  └──────────────────────────────────────┘   │
│                                              │
│  📄 DOCUMENTAÇÃO                             │
│  ┌──────────────────────────────────────┐   │
│  │ ✓ Carta de Condução    [VÁLIDA]    │   │
│  │   Expira: 15/08/2026                │   │
│  │                                     │   │
│  │ ✓ Seguro do Veículo    [VÁLIDO]    │   │
│  │   Expira: 30/06/2025                │   │
│  │                                     │   │
│  │ ✓ Inspeção do Veículo  [VÁLIDA]    │   │
│  │   Expira: 22/03/2025                │   │
│  │                                     │   │
│  │ [  Ver Todos os Documentos  ]       │   │
│  └──────────────────────────────────────┘   │
│                                              │
│  AÇÕES RÁPIDAS                               │
│  ┌─────────────┐  ┌─────────────┐           │
│  │  ⚠️         │  │  🗺️         │           │
│  │ Reportar    │  │  Minhas     │           │
│  │ Problema    │  │  Rotas      │           │
│  └─────────────┘  └─────────────┘           │
│  ┌─────────────┐  ┌─────────────┐           │
│  │  📊         │  │  ⚙️         │           │
│  │ Histórico   │  │ Definições  │           │
│  └─────────────┘  └─────────────┘           │
│                                              │
└──────────────────────────────────────────────┘
```

## Componentes

### 1. Header
- Logo VeiGest
- Mensagem de boas-vindas
- Nome do condutor
- Botão de logout

### 2. Card: Rotas Ativas
- **Cor de destaque**: Verde Brand (#11C7A5)
- **Ícone**: Seta/Direção
- **Dados exibidos**:
  - Origem da rota
  - Destino da rota
  - Distância total
  - Tempo estimado de chegada
- **Ação**: Ver detalhes completos

### 3. Card: Veículo Atual
- **Cor de destaque**: Verde Brand (#11C7A5)
- **Ícone**: Caminhão
- **Dados exibidos**:
  - Matrícula
  - Modelo do veículo
  - Quilometragem
  - Nível de combustível
- **Ação**: Ver detalhes completos

### 4. Card: Documentação
- **Cor de destaque**: Verde Brand (#11C7A5)
- **Ícone**: Documento
- **Dados exibidos**:
  - Status da carta de condução
  - Status do seguro
  - Status da inspeção
  - Indicadores visuais (Verde=Válido, Vermelho=Expirado)
- **Ação**: Ver todos os documentos

### 5. Grid de Ações Rápidas (2x2)
Cada card com ícone e label:
1. **Reportar Problema** - Alertas e incidentes
2. **Minhas Rotas** - Histórico de rotas
3. **Histórico** - Viagens anteriores
4. **Definições** - Configurações

## Cores e Tema

### Modo Claro
- Background: Branco (#FFFFFF)
- Cards: Cinza claro (#F2F2F2)
- Texto: Preto (#000000)
- Destaques: Verde VeiGest (#11C7A5)

### Modo Escuro
- Background: Preto (#0A0A0A)
- Cards: Cinza escuro (#222222)
- Texto: Branco (#FFFFFF)
- Destaques: Verde VeiGest (#11C7A5)

## Interações

Todas as ações atualmente mostram um Toast com mensagem:
- "Funcionalidade a implementar"
- Preparado para integração futura com API
- Navegação para fragments específicos (TODOs no código)

## Responsividade

- **ScrollView**: Permite scroll vertical em telas pequenas
- **CardViews**: Adaptam-se à largura da tela
- **Grid 2x2**: Distribui espaço igualmente
- **Padding consistente**: 16dp entre elementos
