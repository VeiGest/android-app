# 🎨 Designing Layouts (XML)

Layouts define the visual structure of the app. VeiGest uses **Material Design 3** components for a modern look and feel.

## 1. Layout Types Used

- **`ConstraintLayout`**: The default and most flexible. Used for complex screens like the Profile or Dashboard.
- **`LinearLayout`**: Used for simple vertical or horizontal stacks (like item cards).
- **`FrameLayout`**: Often used as a placeholder (container) for fragment transactions in `MainActivity`.

## 2. Using Material Components

Always prefer Material versions of standard widgets:
- `com.google.android.material.button.MaterialButton`
- `com.google.android.material.textfield.TextInputLayout`
- `com.google.android.material.card.MaterialCardView`

## 3. The "Header" Pattern

Notice that most fragments include a shared header layout. This is done via `<include>`:
```xml
<include
    android:id="@+id/layout_header"
    layout="@layout/layout_fragment_header" />
```

## 4. Best Practices

- **Use DP and SP**: 
  - Use `dp` (Density-Independent Pixels) for dimensions and spacing.
  - Use `sp` (Scale-Independent Pixels) for text sizes.
- **Avoid Hardcoded Strings**: Always use `@string/your_text_id` instead of raw text. This allows for easy translation and centralization.
- **Extract Styles**: If you find yourself repeating the same padding or color 10 times, create a `<style>` in `themes.xml`.

## 📁 Key Layout Files

Layouts are located in: [`veigst/app/src/main/res/layout/`](file:///c:/xampp/htdocs/exame/veigst/app/src/main/res/layout/)

- **Main Container**: [`activity_main.xml`](file:///c:/xampp/htdocs/exame/veigst/app/src/main/res/layout/activity_main.xml)
- **Forms (Dialogs)**:
  - [`dialog_vehicle_form.xml`](file:///c:/xampp/htdocs/exame/veigst/app/src/main/res/layout/dialog_vehicle_form.xml)
  - [`dialog_route_form.xml`](file:///c:/xampp/htdocs/exame/veigst/app/src/main/res/layout/dialog_route_form.xml)
- **List Items**:
  - [`item_vehicle.xml`](file:///c:/xampp/htdocs/exame/veigst/app/src/main/res/layout/item_vehicle.xml)
  - [`item_route.xml`](file:///c:/xampp/htdocs/exame/veigst/app/src/main/res/layout/item_route.xml)
  - [`item_document.xml`](file:///c:/xampp/htdocs/exame/veigst/app/src/main/res/layout/item_document.xml)
- **Shared Headers**: [`layout_fragment_header.xml`](file:///c:/xampp/htdocs/exame/veigst/app/src/main/res/layout/layout_fragment_header.xml)
