# 📚 Documentação VeiGest
## Sistema de Gestão de Frotas

**ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO - 2025 / 2026**  
**TESP PROGRAMAÇÃO DE SISTEMAS DE INFORMAÇÃO**

---

## Bem-vindo à Documentação

Esta documentação cobre todos os aspetos do desenvolvimento da aplicação Android VeiGest - um sistema de gestão de frotas de veículos.

---

## 📖 Índice de Documentos

### Introdução e Estrutura

| # | Documento | Descrição |
|---|-----------|-----------|
| 00 | [Introdução ao VeiGest](00_Introducao_VeiGest.md) | Visão geral do projeto, arquitetura e tecnologias |
| 01 | [Estrutura do Projeto](01_Estrutura_Projeto.md) | Organização de pastas, ficheiros e convenções |

### VeiGest SDK

| # | Documento | Descrição |
|---|-----------|-----------|
| 02 | [VeiGest SDK](02_VeiGest_SDK.md) | SingletonVeiGest, autenticação, operações CRUD |

### Android Fundamentals

| # | Documento | Descrição |
|---|-----------|-----------|
| 03 | [Activities e Fragments](03_Activities_Fragments.md) | Ciclo de vida, comunicação, boas práticas |
| 04 | [Navigation Drawer](04_Navigation_Drawer.md) | Menu lateral, NavigationView, navegação |
| 05 | [Layouts e XML](05_Layouts_XML.md) | Tipos de layouts, Material components, recursos |

### Persistência e Comunicação

| # | Documento | Descrição |
|---|-----------|-----------|
| 06 | [SQLite - Persistência](06_SQLite_Persistencia.md) | Base de dados local, CRUD, sincronização |
| 07 | [Integração API REST](07_Integracao_API_REST.md) | Volley, endpoints, parsing JSON |
| 08 | [Listeners e Callbacks](08_Listeners_Callbacks.md) | Comunicação assíncrona, padrão Observer |

### Guias Práticos

| # | Documento | Descrição |
|---|-----------|-----------|
| 09 | [Implementar Novas Funcionalidades](09_Implementar_Novas_Funcionalidades.md) | Passo-a-passo para adicionar features |
| 10 | [Troubleshooting](10_Troubleshooting_Erros_Comuns.md) | Erros comuns e soluções |

---

## 🚀 Início Rápido

### Para Desenvolvedores

1. **Clonar o projeto**
```bash
git clone <repository-url>
cd veigst
```

2. **Abrir no Android Studio**
   - File > Open > Selecionar pasta `veigst`

3. **Sincronizar Gradle**
   - File > Sync Project with Gradle Files

4. **Configurar API URL**
   - Editar `SingletonVeiGest.java`
   - Alterar `baseUrl` para o endereço da API

5. **Executar**
   - Run > Run 'app'

### Para Aprendizagem

Recomendamos ler os documentos na seguinte ordem:

1. [00_Introducao_VeiGest.md](00_Introducao_VeiGest.md) - Entender o projeto
2. [01_Estrutura_Projeto.md](01_Estrutura_Projeto.md) - Conhecer a organização
3. [03_Activities_Fragments.md](03_Activities_Fragments.md) - Conceitos fundamentais
4. [02_VeiGest_SDK.md](02_VeiGest_SDK.md) - Como o SDK funciona
5. [08_Listeners_Callbacks.md](08_Listeners_Callbacks.md) - Comunicação assíncrona
6. [09_Implementar_Novas_Funcionalidades.md](09_Implementar_Novas_Funcionalidades.md) - Praticar

---

## 🏗️ Arquitetura do Projeto

```
┌─────────────────────────────────────────────────────────────────────┐
│                        ARQUITETURA VEIGEST                          │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│   ┌─────────────────────────────────────────────────────────────┐   │
│   │                    CAMADA DE APRESENTAÇÃO                    │   │
│   │                                                              │   │
│   │   ┌──────────────┐    ┌──────────────┐    ┌──────────────┐  │   │
│   │   │ MainActivity │    │  Fragments   │    │   Adapters   │  │   │
│   │   └──────────────┘    └──────────────┘    └──────────────┘  │   │
│   │                                                              │   │
│   └─────────────────────────────────────────────────────────────┘   │
│                                │                                     │
│                                ▼                                     │
│   ┌─────────────────────────────────────────────────────────────┐   │
│   │                      VEIGEST SDK                             │   │
│   │                                                              │   │
│   │   ┌──────────────────┐    ┌──────────────┐                  │   │
│   │   │ SingletonVeiGest │────│  Listeners   │                  │   │
│   │   └──────────────────┘    └──────────────┘                  │   │
│   │            │                                                 │   │
│   │            ├─────────────────┬─────────────────┐            │   │
│   │            ▼                 ▼                 ▼            │   │
│   │   ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     │   │
│   │   │    Models    │  │ VeiGestBDHelper│ │ JsonParser  │     │   │
│   │   └──────────────┘  │   (SQLite)    │ └──────────────┘     │   │
│   │                     └──────────────┘                        │   │
│   │                                                              │   │
│   └─────────────────────────────────────────────────────────────┘   │
│                                │                                     │
│                                ▼                                     │
│   ┌─────────────────────────────────────────────────────────────┐   │
│   │                      CAMADA DE DADOS                         │   │
│   │                                                              │   │
│   │   ┌──────────────┐              ┌──────────────┐            │   │
│   │   │    Volley    │──────────────│   REST API   │            │   │
│   │   │ (HTTP Client)│   Internet   │   (Yii2)     │            │   │
│   │   └──────────────┘              └──────────────┘            │   │
│   │                                                              │   │
│   └─────────────────────────────────────────────────────────────┘   │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 📱 Funcionalidades

### Implementadas

- ✅ **Autenticação** - Login/Logout com token JWT
- ✅ **Dashboard** - Visão geral com estatísticas
- ✅ **Veículos** - CRUD completo de veículos
- ✅ **Manutenções** - Gestão de manutenções
- ✅ **Abastecimentos** - Registo de combustível
- ✅ **Alertas** - Sistema de notificações
- ✅ **Documentos** - Gestão de documentação
- ✅ **Rotas** - Planeamento de rotas

### A Implementar

- ⬜ Modo offline completo
- ⬜ Push notifications
- ⬜ Relatórios em PDF
- ⬜ Mapas com Google Maps

---

## 🛠️ Tecnologias

| Tecnologia | Versão | Uso |
|------------|--------|-----|
| Android SDK | 36 | Plataforma de desenvolvimento |
| Kotlin/Java | - | Linguagem de programação |
| Volley | 1.2.1 | Requisições HTTP |
| Gson | 2.10.1 | Parsing JSON |
| Material Components | 1.12.0 | UI Components |
| SQLite | - | Base de dados local |

---

## 📁 Estrutura de Pastas

```
veigst/
├── app/                          # Módulo da aplicação
│   ├── src/main/
│   │   ├── java/                 # Código fonte
│   │   │   └── pt/ipleiria/.../
│   │   │       ├── MainActivity.java
│   │   │       ├── fragments/    # Fragments
│   │   │       └── adapters/     # Adapters de listas
│   │   └── res/                  # Recursos
│   │       ├── layout/           # Layouts XML
│   │       ├── menu/             # Menus
│   │       ├── values/           # Strings, cores, temas
│   │       └── drawable/         # Ícones e imagens
│   └── build.gradle.kts
│
├── veigest-sdk/                  # Módulo do SDK
│   ├── src/main/java/
│   │   └── com/veigest/sdk/
│   │       ├── SingletonVeiGest.java
│   │       ├── database/
│   │       ├── listeners/
│   │       ├── models/
│   │       └── utils/
│   └── build.gradle.kts
│
├── docs/                         # Documentação
│   └── veigest/                  # Esta documentação
│
└── gradle/                       # Configuração Gradle
```

---

## 🔗 Links Úteis

### Documentação Oficial

- [Android Developers](https://developer.android.com/)
- [Material Design](https://material.io/develop/android)
- [Volley Documentation](https://google.github.io/volley/)

### Recursos do Curso

- [Base de Conteúdo](../baseconteudo/) - Material teórico do curso
- [API Documentation](../endpoints-atualizados/API_DOCUMENTATION.md) - Documentação da API

---

## ❓ Suporte

Em caso de dúvidas ou problemas:

1. Consultar [10_Troubleshooting_Erros_Comuns.md](10_Troubleshooting_Erros_Comuns.md)
2. Verificar a documentação relacionada
3. Contactar o docente

---

## 📝 Contribuir

Para contribuir com a documentação:

1. Seguir o formato dos documentos existentes
2. Incluir exemplos de código funcionais
3. Manter as tabelas de referência atualizadas
4. Atualizar este README se adicionar novos documentos

---

**Versão:** 1.0  
**Última atualização:** Janeiro 2026  
**Autor:** Equipa VeiGest
