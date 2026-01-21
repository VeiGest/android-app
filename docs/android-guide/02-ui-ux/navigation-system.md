# 🗺️ Navigation System

The VeiGest app uses a **Navigation Drawer** (Side Menu) to move between main features.

## 1. Components

- **`DrawerLayout`**: The top-level container in `activity_main.xml`.
- **`NavigationView`**: The actual menu inside the drawer.
- **`nav_drawer_menu.xml`**: Defined in `res/menu/`, it contains the list of items (Dashboard, Vehicles, etc.).

## 2. How Fragment Swapping Works

Inside `MainActivity.java`, we listen for menu selections:

```java
navigationView.setNavigationItemSelectedListener(item -> {
    Fragment selectedFragment = null;
    int id = item.getItemId();

    if (id == R.id.nav_dashboard) {
        selectedFragment = new DashboardFragment();
    } else if (id == R.id.nav_vehicles) {
        selectedFragment = new VehiclesFragment();
    }
    // ...

    if (selectedFragment != null) {
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, selectedFragment)
            .commit();
    }
    drawerLayout.closeDrawers();
    return true;
});
```

## 3. The Back Stack

Standard navigation in VeiGest replaces the fragment completely. If you want to allow the user to go "back" to the previous screen using the system back button, you must add the transaction to the back stack:

```java
fragmentManager.beginTransaction()
    .replace(R.id.fragment_container, nextFragment)
    .addToBackStack(null) // Allows going back
    .commit();
```

- **Closing the Drawer Programmatically**: If you perform an action (like a button click) that should navigate the user, remember to close the drawer:
  ```java
  drawerLayout.closeDrawer(GravityCompat.START);
  ```

## 📁 Core Navigation Files

- **Menu Definition**: [`veigst/app/src/main/res/menu/nav_drawer_menu.xml`](file:///c:/xampp/htdocs/exame/veigst/app/src/main/res/menu/nav_drawer_menu.xml)
- **Host Activity (Logic)**: [`veigst/app/src/main/java/com/ipleiria/veigest/MainActivity.java`](file:///c:/xampp/htdocs/exame/veigst/app/src/main/java/com/ipleiria/veigest/MainActivity.java)
- **Sidebar Header Layout**: [`veigst/app/src/main/res/layout/nav_header.xml`](file:///c:/xampp/htdocs/exame/veigst/app/src/main/res/layout/nav_header.xml)
