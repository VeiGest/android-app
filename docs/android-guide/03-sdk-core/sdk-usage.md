# ⚙️ SDK Integration & Usage

The VeiGest SDK is the bridge between your UI and your data. Here is how to use it correctly inside your app module.

## 1. Getting the Instance

All interactions begin with the `SingletonVeiGest` instance. It is recommended to initialize it in `onViewCreated` or `onCreate`.

```java
public class MyFragment extends Fragment {
    private SingletonVeiGest singleton;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get the global singleton instance
        singleton = SingletonVeiGest.getInstance(getContext());
    }
}
```

## 2. Common Operations

### 🔄 Fetching Data (Async)
Most methods that end in `API` are asynchronous. They start a network request but don't return data immediately.
```java
singleton.getAllVehiclesAPI(); // Triggers a GET /api/vehicle
```

### 💾 Accessing Memory Storage
The SDK maintains internal lists of data. These are populated after a successful API call or after loading from the local database.
```java
ArrayList<Vehicle> vehicles = singleton.getVeiculos();
```

### 👤 User Profile
You can easily get information about the logged-in user:
```java
User currentUser = singleton.getUtilizadorAtual();
int userId = singleton.getUserId();
String token = singleton.getToken();
```

## 3. The Lifecycle of an Action

1. **Trigger**: User clicks "Refresh".
2. **Call**: Fragment calls `singleton.getAllRotasAPI()`.
3. **Wait**: Volley performs the request in the background.
4. **Callback**: The API returns JSON. The SDK parses it into `Route` objects and updates its internal `ArrayList`.
5. **Update**: The SDK notifies all registered `RotasListener` objects.
6. **UI Refresh**: The Fragment receives the update and tells the `RouteAdapter` to refresh the view.

---

## 4. Best Practices

- **Never update UI directly from an SDK thread**: Volley handled this for you (it returns on the Main Thread), but be careful if you add custom background logic.
- **Check for Initialization**: Ensure `singleton.iniciarBD()` was called in your `Application` or `MainActivity` before performing DB operations.

## 📁 Main SDK Files

- **The Hub**: [`SingletonVeiGest.java`](file:///c:/xampp/htdocs/exame/veigst/veigest-sdk/src/main/java/com/veigest/sdk/SingletonVeiGest.java)
- **API Configuration**: [`ApiConfig.java`](file:///c:/xampp/htdocs/exame/veigst/veigest-sdk/src/main/java/com/veigest/sdk/config/ApiConfig.java)
- **App Context**: [`VeiGestApplication.java`](file:///c:/xampp/htdocs/exame/veigst/app/src/main/java/com/ipleiria/veigest/VeiGestApplication.java)
