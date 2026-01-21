# 📜 Lists & Adapters (RecyclerView)

Displaying lists of data (Vehicles, Routes, Documents) is done using `RecyclerView`.

## 1. The Three Components

1. **`RecyclerView`** (in Fragment XML): The container widget.
2. **`Item Layout`** (e.g., `item_vehicle.xml`): How a single row looks.
3. **`Adapter`** (e.g., `VehicleAdapter`): The "middleman" that binds your Data to the View.

## 2. Implementing the Adapter

The adapter should extend `RecyclerView.Adapter<YourVH>`. It must implement:
- `onCreateViewHolder`: Inflates the item XML.
- `onBindViewHolder`: Sets values (text, colors) based on the specific object in the list.
- `getItemCount`: Tells the recycler how many items there are.

## 3. Handling Clicks

Define an interface inside your adapter to notify the Fragment when an item is tapped:

```java
public interface OnVehicleClickListener {
    void onVehicleClick(Vehicle vehicle);
}
```

## 4. Updating Data

Instead of creating a new adapter every time the data changes, use a method to update the internal list and notify the component:

```java
public void setVehicles(List<Vehicle> newVehicles) {
    this.vehicles = newVehicles;
    notifyDataSetChanged(); // Refreshes the UI
}
```

## 5. Performance Note

For very large lists with frequent updates, consider using `DiffUtil` instead of `notifyDataSetChanged()` for smoother animations and better performance.

## 📁 Where are the Adapters?

Adapters are found in: [`veigst/app/src/main/java/com/ipleiria/veigest/adapters/`](file:///c:/xampp/htdocs/exame/veigst/app/src/main/java/com/ipleiria/veigest/adapters/)

- **Vehicle List**: [`VehicleAdapter.java`](file:///c:/xampp/htdocs/exame/veigst/app/src/main/java/com/ipleiria/veigest/adapters/VehicleAdapter.java)
- **Route List**: [`RouteAdapter.java`](file:///c:/xampp/htdocs/exame/veigst/app/src/main/java/com/ipleiria/veigest/adapters/RouteAdapter.java)
- **Document List**: [`DocumentAdapter.java`](file:///c:/xampp/htdocs/exame/veigst/app/src/main/java/com/ipleiria/veigest/adapters/DocumentAdapter.java)
