# 📱 VeiGest Android Development Guide

Welcome to the comprehensive guide for developing and maintaining the VeiGest Android application. This guide is organized into thematic sections to help you navigate the codebase, understand the architecture, and implement new features.

---

## 📂 Table of Contents

### 🏗️ 1. Architecture & Core Concepts
- [Project Layer Overview](01-architecture/project-structure.md) - Understanding the App vs. SDK separation.
- [Project Directory Map](01-architecture/directory-map.md) - Quick reference for all important paths.
- [The Singleton Pattern](01-architecture/singleton-pattern.md) - Deep dive into `SingletonVeiGest` as the central hub.

### 🎨 2. UI & User Experience
- [Fragments & Lifecycle](02-ui-ux/fragments-lifecycle.md) - How to build and manage screens.
- [Designing Layouts](02-ui-ux/developing-layouts.md) - XML practices, Material Design, and Resources.
- [Navigation System](02-ui-ux/navigation-system.md) - Sidebar management and Fragment transactions.
- [Lists & Adapters](02-ui-ux/adapters-and-lists.md) - Working with RecyclerView and dynamic data.

### ⚙️ 3. SDK & Data Management
- [SDK Integration](03-sdk-core/sdk-usage.md) - How to use the VeiGest SDK in your UI.
- [API Connections](03-sdk-core/api-connections.md) - Adding endpoints and handling Volley requests.
- [Listeners & Observer Pattern](03-sdk-core/listeners-and-events.md) - Asynchronous communication between layers.
- [Local Persistence (SQLite)](03-sdk-core/sqlite-persistence.md) - Working with `VeiGestBDHelper`.
- [Models & JSON Parsing](03-sdk-core/models-parsing.md) - Creating models and using `VeiGestJsonParser`.

### 🛠️ 4. Step-by-Step Tutorials
- [Creating a New Fragment](04-tutorials/create-new-fragment.md) - From XML to Sidebar registration.
- [Adding an API Endpoint](04-tutorials/add-api-endpoint.md) - Expanding the SDK and Backend connection.
- [Implementing CRUD](04-tutorials/implement-crud.md) - Full loop: UI -> SDK -> API -> DB.
- [Auth & Session Management](04-tutorials/auth-and-sessions.md) - Tokens and Login persistence.
- [PDF Report Generation](04-tutorials/reports-pdf.md) - Advanced document printing.

### 🔍 5. Maintenance & Support
- [Troubleshooting Guide](05-maintenance/troubleshooting.md) - Common errors and their resolutions.
- [Debugging Tips](05-maintenance/debugging-tips.md) - Using Logcat, Breakpoints, and Profiler.

---

**Version:** 2.0  
**Updated:** January 2026  
**Status:** Comprehensive Tutorial Series
