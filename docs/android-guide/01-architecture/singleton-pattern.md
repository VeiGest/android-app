# 🔗 The Singleton Pattern in VeiGest

The `SingletonVeiGest` class is the heart of the application. It follows the Singleton design pattern to ensure that data remains consistent across all fragments and activities.

## 1. Why use a Singleton?

- **Global State**: Ensures that if you update a vehicle's status in the `VehiclesFragment`, the `DashboardFragment` sees the change immediately because they share the same data source in memory.
- **Shared Connection**: Manages a single `RequestQueue` (Volley) and a single Database connection.
- **Simplified Access**: Any component can access the SDK using:
  ```java
  SingletonVeiGest singleton = SingletonVeiGest.getInstance(getContext());
  ```

## 2. Key Responsibilities

- **Authentication**: Stores and provides the JWT token for all requests.
- **Memory Caching**: Holds `ArrayList<Vehicle>`, `ArrayList<Route>`, etc., in RAM for instant UI access.
- **Layer Bridge**: Connects the UI to both the Remote API and the Local SQLite database.

## 3. Initialization

The Singleton should be initialized early in the application lifecycle, usually in the `onCreate` of the `MainActivity` or a custom `Application` class.

```java
// Initialize the DB
SingletonVeiGest.getInstance(context).iniciarBD(context);

// Optional: Force a data reload from local DB
SingletonVeiGest.getInstance(context).carregarDadosLocais();
```

## 4. Best Practices

- **Context Handling**: Always pass `getApplicationContext()` to avoid memory leaks if you store the context long-term.
- **Thread Safety**: The `getInstance()` method is `synchronized` to prevent multiple instances in multithreaded environments.
