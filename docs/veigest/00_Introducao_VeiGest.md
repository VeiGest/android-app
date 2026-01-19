# VeiGest - Sistema de Gestão de Frotas
## Introdução e Visão Geral

**ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO - 2025 / 2026**  
**TESP PROGRAMAÇÃO DE SISTEMAS DE INFORMAÇÃO**

---

## 📖 Índice

1. [Visão Geral](#visão-geral)
2. [Funcionalidades Principais](#funcionalidades-principais)
3. [Arquitetura da Aplicação](#arquitetura-da-aplicação)
4. [Tecnologias Utilizadas](#tecnologias-utilizadas)
5. [Requisitos do Sistema](#requisitos-do-sistema)
6. [Primeiros Passos](#primeiros-passos)

---

## Visão Geral

O **VeiGest** é uma aplicação Android para gestão de frotas empresariais. A aplicação permite:

- Gestão de veículos da frota
- Acompanhamento de rotas e trajetos
- Registo de manutenções
- Controlo de abastecimentos
- Gestão de documentação (licenças, seguros, inspeções)
- Sistema de alertas e notificações

A aplicação foi desenvolvida seguindo o padrão **Singleton** para gestão centralizada de dados e comunicação com a API REST.

---

## Funcionalidades Principais

### 🚗 Gestão de Veículos
- Listagem de todos os veículos da frota
- Detalhes de cada veículo (matrícula, marca, modelo, quilometragem)
- Estado do veículo (ativo, em manutenção, etc.)

### 🗺️ Rotas
- Visualização de rotas atribuídas
- Origem e destino das viagens
- Distância e tempo estimado de chegada

### 🔧 Manutenções
- Histórico de manutenções realizadas
- Agendamento de novas manutenções
- Custos e oficinas utilizadas

### ⛽ Abastecimentos
- Registo de abastecimentos
- Litros, valor e quilometragem atual
- Controlo de consumo de combustível

### 📄 Documentos
- Gestão de documentação legal
- Alertas de expiração de documentos
- Carta de condução, seguros, inspeções

### 🔔 Alertas
- Notificações de eventos importantes
- Alertas de manutenção preventiva
- Documentos a expirar

---

## Arquitetura da Aplicação

A aplicação segue uma arquitetura modular composta por:

```
┌─────────────────────────────────────────────────────────────┐
│                     APLICAÇÃO (app/)                        │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐ │
│  │ MainActivity │  │  Fragments  │  │   Resources (XML)   │ │
│  │             │  │             │  │                     │ │
│  │ • Navegação │  │ • Login     │  │ • Layouts           │ │
│  │ • Drawer    │  │ • Dashboard │  │ • Drawables         │ │
│  │ • Lifecycle │  │ • Vehicles  │  │ • Strings           │ │
│  │             │  │ • Routes    │  │ • Menus             │ │
│  └─────────────┘  └─────────────┘  └─────────────────────┘ │
│                           │                                 │
│                           ▼                                 │
│  ┌─────────────────────────────────────────────────────────┐│
│  │                  VEIGEST SDK (veigest-sdk/)             ││
│  │  ┌────────────────┐  ┌─────────────┐  ┌──────────────┐ ││
│  │  │SingletonVeiGest│  │  Listeners  │  │   Models     │ ││
│  │  │                │  │             │  │              │ ││
│  │  │ • API Calls    │  │ • Login     │  │ • User       │ ││
│  │  │ • Token Mgmt   │  │ • Veículos  │  │ • Vehicle    │ ││
│  │  │ • BD Local     │  │ • Rotas     │  │ • Route      │ ││
│  │  │ • Volley Queue │  │ • Alertas   │  │ • Alert      │ ││
│  │  └────────────────┘  └─────────────┘  └──────────────┘ ││
│  │                                                         ││
│  │  ┌─────────────────────────────────────────────────────┐││
│  │  │              VeiGestBDHelper (SQLite)               │││
│  │  │  • Persistência local de dados                      │││
│  │  │  • Cache offline                                    │││
│  │  └─────────────────────────────────────────────────────┘││
│  └─────────────────────────────────────────────────────────┘│
└─────────────────────────────────────────────────────────────┘
                             │
                             ▼
              ┌──────────────────────────┐
              │      API REST (Yii2)     │
              │  • Autenticação          │
              │  • CRUD Entidades        │
              │  • Bearer Token          │
              └──────────────────────────┘
```

### Componentes Principais

| Componente | Descrição |
|------------|-----------|
| **MainActivity** | Activity principal que gerencia a navegação entre fragments |
| **Fragments** | Cada ecrã é um Fragment (Login, Dashboard, Vehicles, etc.) |
| **SingletonVeiGest** | Classe Singleton que gerencia toda a comunicação com API e BD |
| **VeiGestBDHelper** | Helper SQLite para persistência local de dados |
| **Listeners** | Interfaces para callbacks de operações assíncronas |
| **Models** | Classes POJO que representam as entidades do sistema |

---

## Tecnologias Utilizadas

### Android SDK
- **Min SDK**: 24 (Android 7.0 Nougat)
- **Target SDK**: 36
- **Compile SDK**: 36

### Bibliotecas Principais

| Biblioteca | Versão | Função |
|------------|--------|--------|
| **Volley** | 1.2.1 | Requisições HTTP à API |
| **Gson** | 2.10.1 | Parsing JSON |
| **Material Components** | 1.12.0 | Componentes visuais Material Design |
| **ConstraintLayout** | 2.2.1 | Layouts responsivos |
| **AppCompat** | 1.7.0 | Compatibilidade retroativa |

### Padrões de Projeto Utilizados

- **Singleton**: Gestão centralizada de estado e comunicação
- **Observer/Listener**: Notificação de eventos assíncronos
- **MVC/MVP**: Separação de responsabilidades

---

## Requisitos do Sistema

### Ambiente de Desenvolvimento

- Android Studio Arctic Fox ou superior
- JDK 17
- Gradle 8.13+
- Android SDK Platform 36

### Dispositivo

- Android 7.0 (API 24) ou superior
- Conexão à internet para sincronização
- ~50MB de espaço livre

---

## Primeiros Passos

### 1. Clonar o Repositório

```bash
git clone https://github.com/VeiGest/android-app.git
cd android-app/veigst
```

### 2. Abrir no Android Studio

1. Abrir Android Studio
2. File → Open → Selecionar pasta `veigst`
3. Aguardar sincronização do Gradle

### 3. Configurar a API

No ficheiro `VeiGestApplication.java`:

```java
public class VeiGestApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        
        // Configurar URL da API
        SingletonVeiGest singleton = SingletonVeiGest.getInstance(this);
        singleton.setBaseUrl("http://SEU_SERVIDOR:8080/api/v1");
        singleton.iniciarBD(this);
    }
}
```

### 4. Executar a Aplicação

1. Conectar dispositivo físico ou iniciar emulador
2. Clicar em Run (▶️) ou `Shift + F10`
3. Selecionar o dispositivo alvo

---

## 📚 Documentação Relacionada

| Documento | Descrição |
|-----------|-----------|
| [01_Estrutura_Projeto.md](01_Estrutura_Projeto.md) | Estrutura de pastas e ficheiros |
| [02_VeiGest_SDK.md](02_VeiGest_SDK.md) | Documentação completa do SDK |
| [03_Activities_Fragments.md](03_Activities_Fragments.md) | Activities e Fragments |
| [04_Navigation_Drawer.md](04_Navigation_Drawer.md) | Sistema de navegação |
| [05_Layouts_XML.md](05_Layouts_XML.md) | Layouts e recursos XML |
| [06_SQLite_Persistencia.md](06_SQLite_Persistencia.md) | Base de dados local |
| [07_Integracao_API_REST.md](07_Integracao_API_REST.md) | Integração com API |
| [08_Listeners_Callbacks.md](08_Listeners_Callbacks.md) | Sistema de listeners |
| [09_Implementar_Novas_Funcionalidades.md](09_Implementar_Novas_Funcionalidades.md) | Guia para novas features |
| [10_Troubleshooting_Erros_Comuns.md](10_Troubleshooting_Erros_Comuns.md) | Resolução de problemas |

---

**Última atualização:** Janeiro 2026
