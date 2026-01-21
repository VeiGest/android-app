# 🎓 Tutorial: Creating a New Fragment

Want to add a new screen like "Settings" or "Fuel History"? Here is the step-by-step guide.

## Phase 1: The Layout (XML)

1. Right-click `res/layout` -> New -> Layout Resource File.
2. Name it `fragment_my_new_feature.xml`.
3. Design your UI. Use `ConstraintLayout` as the root.
4. Include the standard header:
   ```xml
   <include
       android:id="@+id/layout_header"
       layout="@layout/layout_fragment_header" />
   ```

## Phase 2: The Java Class

1. Create a new class extending `Fragment`.
2. Inflate the layout:
   ```java
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       return inflater.inflate(R.layout.fragment_my_new_feature, container, false);
   }
   ```
3. Initialize views in `onViewCreated`.

## Phase 3: Registration

### 1. Menu Item
Open `res/menu/nav_drawer_menu.xml` and add a new entry:
```xml
<item
    android:id="@+id/nav_new_feature"
    android:icon="@drawable/ic_some_icon"
    android:title="My Feature" />
```

### 2. Sidebar Navigation
Open `MainActivity.java` and find the `setNavigationItemSelectedListener`. Add your logic:
```java
} else if (id == R.id.nav_new_feature) {
    selectedFragment = new MyNewFeatureFragment();
}
```

## Phase 4: Strings & Resources
Don't forget to define the title and any other text in `res/values/strings.xml`.
