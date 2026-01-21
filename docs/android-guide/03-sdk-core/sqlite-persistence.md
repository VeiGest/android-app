# 💾 Local Persistence (SQLite)

O VeiGest usa o SQLite para permitir que a aplicação funcione (ou pelo menos exiba dados) sem internet.

## 1. The Helper Class

`VeiGestBDHelper` (no SDK) gere a criação das tabelas e as operações CRUD locais.

### Key Tasks:
- **`onCreate`**: define o esquema da base de dados (CREATE TABLE).
- **`onUpgrade`**: lida com migrações quando a estrutura muda.

## 2. Sync Strategy

A estratégia padrão é **Cache on Success**:
1. O SDK faz o pedido à API.
2. No sucesso (`onResponse`), o SDK limpa a cache local daquela tabela e reinsere os novos dados.
3. Se o pedido falhar (offline), o SDK lê da base de dados local para que o utilizador não veja uma lista vazia.

### Example logic in SDK:
```java
public void getAllVehiclesAPI() {
    // ... setup request
    response -> {
        ArrayList<Vehicle> list = parser(response);
        saveToLocalBD(list); // Persist
        notifyUI(list);
    },
    error -> {
        ArrayList<Vehicle> localList = veiGestBD.getAllVehiclesBD(); // Read from disk
        notifyUI(localList);
    }
}
```

## 3. Working with Cursor

If you are modifying the database directly:
- Use `ContentValues` for inserts/updates.
- Remember to close your `Cursor` objects to avoid memory performance issues.

## 4. Database Location
The database file is typically located in `/data/data/com.ipleiria.veigest/databases/veigest.db`. You can view it during development using the **App Inspection** tool in Android Studio.

## 📁 Database Files

- **Main Helper**: [`VeiGestBDHelper.java`](file:///c:/xampp/htdocs/exame/veigst/veigest-sdk/src/main/java/com/veigest/sdk/database/VeiGestBDHelper.java)
- **Local Persistence Logic**: Managed within [`SingletonVeiGest.java`](file:///c:/xampp/htdocs/exame/veigst/veigest-sdk/src/main/java/com/veigest/sdk/SingletonVeiGest.java)
