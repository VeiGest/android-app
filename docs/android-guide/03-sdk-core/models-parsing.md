# 📦 Models & JSON Parsing

Data transfer between Backend and Android uses **JSON**. The SDK provides a dedicated utility for this.

## 1. The Model Class (POJO)

Models are located in `com.veigest.sdk.models`. They are basic Java classes with:
- Private fields (e.g., `id`, `brand`, `license_plate`).
- Public Getters and Setters.
- Sometimes a constructor for quick creation.

**Important**: The field names in your Java class don't have to match the JSON keys exactly, but it's good practice for clarity.

## 2. VeiGestJsonParser

Este utilitário é responsável por transformar `JSONObject` e `JSONArray` em instâncias das classes Java.

### Parsing a single Object:
```java
public static Vehicle parserJsonVehicle(JSONObject json) {
    Vehicle v = new Vehicle();
    try {
        v.setId(json.getInt("id"));
        v.setBrand(json.getString("brand"));
        // ...
    } catch (JSONException e) {
        e.printStackTrace();
    }
    return v;
}
```

### Parsing a List:
A method that loops through a `JSONArray` call the single object parser for each index.

## 3. Sending Data (Serialization)

When creating or editing data, you must transform the Java object back to JSON. This is usually done in `SingletonVeiGest` via helper methods like `vehicleToJson(vehicle)`.

```java
private JSONObject vehicleToJson(Vehicle vehicle) {
    JSONObject json = new JSONObject();
    json.put("brand", vehicle.getBrand());
    // ...
    return json;
}
```

## 4. Best Practices

- **Check for optString/optInt**: Use these methods if a field might be optional in the API to avoid `JSONException`.
- **Validation**: Perform basic data validation (e.g., empty strings) before sending a JSON object to the server.

## 📁 Data Models & Parsing

- **Parser Utility**: [`VeiGestJsonParser.java`](file:///c:/xampp/htdocs/exame/veigst/veigest-sdk/src/main/java/com/veigest/sdk/utils/VeiGestJsonParser.java)
- **Existing Models** ([`veigest-sdk/.../models/`](file:///c:/xampp/htdocs/exame/veigst/veigest-sdk/src/main/java/com/veigest/sdk/models/)):
  - [`Vehicle.java`](file:///c:/xampp/htdocs/exame/veigst/veigest-sdk/src/main/java/com/veigest/sdk/models/Vehicle.java)
  - [`Route.java`](file:///c:/xampp/htdocs/exame/veigst/veigest-sdk/src/main/java/com/veigest/sdk/models/Route.java)
  - [`Document.java`](file:///c:/xampp/htdocs/exame/veigst/veigest-sdk/src/main/java/com/veigest/sdk/models/Document.java)
  - [`User.java`](file:///c:/xampp/htdocs/exame/veigst/veigest-sdk/src/main/java/com/veigest/sdk/models/User.java)
  - [`Alert.java`](file:///c:/xampp/htdocs/exame/veigst/veigest-sdk/src/main/java/com/veigest/sdk/models/Alert.java)
  - [`Maintenance.java`](file:///c:/xampp/htdocs/exame/veigst/veigest-sdk/src/main/java/com/veigest/sdk/models/Maintenance.java)
  - [`FuelLog.java`](file:///c:/xampp/htdocs/exame/veigst/veigest-sdk/src/main/java/com/veigest/sdk/models/FuelLog.java)
