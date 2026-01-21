# 📑 Advanced: PDF Generation

VeiGest uses a custom helper to generate maintenance and route reports.

## 1. The PDFGenerator Class

Localizado em `com.ipleiria.veigest.PDFGenerator`. Utiliza o componente `PdfDocument` nativo do Android.

### Concept:
- It creates a virtual Canvas.
- You "draw" text, lines, and bitmaps onto this canvas.
- The canvas is then saved into a `.pdf` file in the device's storage.

## 2. Steps to Generate a Report

1. **Gathers Data**: The Fragment retrieves the list of items from the SDK.
2. **Prepare File**: Choose a location (usually `getExternalFilesDir`).
3. **Call Generator**: Pass the data and the file path.

```java
PDFGenerator generator = new PDFGenerator();
generator.generateReport(getContext(), items, "report_name.pdf");
```

## 3. Storage Permissions

From Android 10+, you don't need the old `WRITE_EXTERNAL_STORAGE` permission if you save to the app's internal "Scoped Storage" folders.
If you want to save to the public "Documents" or "Downloads" folder, you must handle the MediaStore API or the Storage Access Framework (SAF).

## 4. Customizing the Design

The `PDFGenerator` calculates line positions manually:
- `static final int LINE_HEIGHT = 20;`
- Each row increments the `y` position. Be careful with page breaks! If your list is too long, you need to call `startPage` again to create Page 2.
