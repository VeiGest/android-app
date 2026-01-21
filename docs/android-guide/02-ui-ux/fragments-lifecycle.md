# 🧩 Fragments & Lifecycle

In VeiGest, almost every screen is a `Fragment`. Fragments are modular sections of an Activity's user interface.

## 1. Why Fragments?

- **Modularity**: You can reuse a fragment (like a search bar or a profile card) in different parts of the app.
- **Navigation Drawer**: The app uses a "Single Activity" architecture where the `MainActivity` swaps between different fragments when you click on the side menu.

## 2. Important Lifecycle Methods

When working with fragments, pay attention to these three methods:

1. **`onCreateView`**: This is where you inflate the XML layout.
   ```java
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       return inflater.inflate(R.layout.fragment_your_feature, container, false);
   }
   ```
2. **`onViewCreated`**: The best place to initialize your logic, listeners, and SDK calls after the view is ready.
   ```java
   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
       super.onViewCreated(view, savedInstanceState);
       singleton = SingletonVeiGest.getInstance(getContext());
       // Trigger data fetch
       singleton.getAllDataAPI();
   }
   ```
3. **`onPause/onDestroy`**: Use these to cancel pending requests or remove listeners to prevent memory leaks.

## 3. Communication with Activity

To update the Toolbar title or open the drawer from within a fragment:
```java
if (getActivity() != null) {
    getActivity().setTitle("New Title");
}
```

## 4. Best Practices

- **Avoid logic in onCreateView**: Keep it strictly for inflating. Move logic to `onViewCreated`.
- **Check getContext()**: Always check if `getContext()` or `getActivity()` is null before performing UI updates, especially after an async API call returns.

## 📁 Where are the Fragments?

All fragments are located in: [`veigst/app/src/main/java/com/ipleiria/veigest/`](file:///c:/xampp/htdocs/exame/veigst/app/src/main/java/com/ipleiria/veigest/)

- **Dashboard**: [`DashboardFragment.java`](file:///c:/xampp/htdocs/exame/veigst/app/src/main/java/com/ipleiria/veigest/DashboardFragment.java)
- **Vehicles**: [`VehiclesFragment.java`](file:///c:/xampp/htdocs/exame/veigst/app/src/main/java/com/ipleiria/veigest/VehiclesFragment.java)
- **Routes**: [`RoutesFragment.java`](file:///c:/xampp/htdocs/exame/veigst/app/src/main/java/com/ipleiria/veigest/RoutesFragment.java)
- **Profile**: [`ProfileFragment.java`](file:///c:/xampp/htdocs/exame/veigst/app/src/main/java/com/ipleiria/veigest/ProfileFragment.java)
- **Login**: [`LoginFragment.java`](file:///c:/xampp/htdocs/exame/veigst/app/src/main/java/com/ipleiria/veigest/LoginFragment.java)
- **Register**: [`RegisterFragment.java`](file:///c:/xampp/htdocs/exame/veigst/app/src/main/java/com/ipleiria/veigest/RegisterFragment.java)
- **Documents**: [`DocumentsFragment.java`](file:///c:/xampp/htdocs/exame/veigst/app/src/main/java/com/ipleiria/veigest/DocumentsFragment.java)
- **Reports**: [`ReportsFragment.java`](file:///c:/xampp/htdocs/exame/veigst/app/src/main/java/com/ipleiria/veigest/ReportsFragment.java)
- **Settings**: [`SettingsFragment.java`](file:///c:/xampp/htdocs/exame/veigst/app/src/main/java/com/ipleiria/veigest/SettingsFragment.java)
