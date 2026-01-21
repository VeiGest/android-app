# 🗺️ Project Directory Map

Use this page as a quick reference to find the most important files and folders in the VeiGest project.

## 📱 Application Layer (`veigst/app/`)

| Path | Description |
|------|-------------|
| [`.../ipleiria/veigest/`](file:///c:/xampp/htdocs/exame/veigst/app/src/main/java/com/ipleiria/veigest/) | Main Fragments & Host Activity |
| [`.../adapters/`](file:///c:/xampp/htdocs/exame/veigst/app/src/main/java/com/ipleiria/veigest/adapters/) | RecyclerView Adapters |
| [`.../res/layout/`](file:///c:/xampp/htdocs/exame/veigst/app/src/main/res/layout/) | XML UI Layouts |
| [`.../res/menu/`](file:///c:/xampp/htdocs/exame/veigst/app/src/main/res/menu/) | Navigation Menus |
| [`.../res/values/`](file:///c:/xampp/htdocs/exame/veigst/app/src/main/res/values/) | Strings, Colors, Themes |

## 📦 SDK Logic Layer (`veigst/veigest-sdk/`)

| Path | Description |
|------|-------------|
| [`.../SingletonVeiGest.java`](file:///c:/xampp/htdocs/exame/veigst/veigest-sdk/src/main/java/com/veigest/sdk/SingletonVeiGest.java) | The central API & Data Hub |
| [`.../models/`](file:///c:/xampp/htdocs/exame/veigst/veigest-sdk/src/main/java/com/veigest/sdk/models/) | Data Entity Classes (POJOs) |
| [`.../database/`](file:///c:/xampp/htdocs/exame/veigst/veigest-sdk/src/main/java/com/veigest/sdk/database/) | SQLite Database Helper |
| [`.../listeners/`](file:///c:/xampp/htdocs/exame/veigst/veigest-sdk/src/main/java/com/veigest/sdk/listeners/) | Callback Interfaces |
| [`.../utils/VeiGestJsonParser.java`](file:///c:/xampp/htdocs/exame/veigst/veigest-sdk/src/main/java/com/veigest/sdk/utils/VeiGestJsonParser.java) | JSON to Java Converter |

## 📚 Documentation (`docs/`)

| Path | Description |
|------|-------------|
| [`/android-guide/index.md`](file:///c:/xampp/htdocs/exame/docs/android-guide/index.md) | **Start Here!** Core Guide Index |
| [`/veigest/`](file:///c:/xampp/htdocs/exame/docs/veigest/) | Legacy/Original Documentation |

## 🛠️ Build & Config

| Path | Description |
|------|-------------|
| [`veigst/build.gradle.kts`](file:///c:/xampp/htdocs/exame/veigst/build.gradle.kts) | Project-level dependencies |
| [`app/build.gradle.kts`](file:///c:/xampp/htdocs/exame/veigst/app/build.gradle.kts) | App module config |
| [`app/src/main/AndroidManifest.xml`](file:///c:/xampp/htdocs/exame/veigst/app/src/main/AndroidManifest.xml) | Permissions & Activity manifest |
