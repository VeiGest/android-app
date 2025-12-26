# Guia de Instalação

Este documento descreve todas as formas de instalar e configurar o VeiGest SDK no seu projeto Android.

---

## Requisitos do Sistema

### Ambiente de Desenvolvimento
| Componente | Requisito Mínimo |
|------------|------------------|
| Android Studio | Arctic Fox 2020.3.1+ |
| Gradle | 8.0+ |
| JDK | 17 |

### Requisitos Android
| Especificação | Valor |
|---------------|-------|
| minSdk | 24 (Android 7.0 Nougat) |
| targetSdk | 36 |
| compileSdk | 36 |
| Java Version | 11 |

---

## Método 1: Módulo Local (Recomendado)

### Passo 1: Copiar o SDK

Copie a pasta `veigest-sdk` para o diretório raiz do seu projeto:

```
seu-projeto/
├── app/
├── veigest-sdk/      ← Copie aqui
├── build.gradle.kts
├── settings.gradle.kts
└── ...
```

### Passo 2: Incluir no settings.gradle.kts

```kotlin
// settings.gradle.kts
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "SeuProjeto"
include(":app")
include(":veigest-sdk")  // ← Adicione esta linha
```

### Passo 3: Adicionar Dependência

```kotlin
// app/build.gradle.kts
dependencies {
    // VeiGest SDK
    implementation(project(":veigest-sdk"))
    
    // Outras dependências...
}
```

### Passo 4: Sincronizar o Projeto

Clique em **"Sync Now"** no Android Studio ou execute:
```bash
./gradlew sync
```

---

## Método 2: Maven/JitPack (Futuro)

> ⚠️ **Nota**: Esta opção estará disponível em breve.

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

// app/build.gradle.kts
dependencies {
    implementation("com.github.VeiGest:android-sdk:1.0.0")
}
```

---

## Método 3: AAR Manual

### Passo 1: Gerar o AAR

No diretório `veigest-sdk`, execute:
```bash
./gradlew assembleRelease
```

O ficheiro AAR será gerado em:
```
veigest-sdk/build/outputs/aar/veigest-sdk-release.aar
```

### Passo 2: Copiar para o Projeto

Copie o AAR para `app/libs/`:
```
app/
└── libs/
    └── veigest-sdk-release.aar
```

### Passo 3: Adicionar Dependências

```kotlin
// app/build.gradle.kts
dependencies {
    // AAR local
    implementation(files("libs/veigest-sdk-release.aar"))
    
    // Dependências transitivas (obrigatórias)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("androidx.annotation:annotation:1.7.1")
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
}
```

---

## Configuração do ProGuard

O SDK já inclui regras de ProGuard. Se tiver problemas em builds de release, adicione ao `proguard-rules.pro`:

```proguard
# VeiGest SDK
-keep class com.veigest.sdk.** { *; }
-keep interface com.veigest.sdk.api.** { *; }
-keep class com.veigest.sdk.models.** { *; }

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

# Gson
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
```

---

## Permissões

O SDK declara automaticamente as permissões necessárias:

```xml
<!-- Incluídas no SDK -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

### Permissões Opcionais

Para funcionalidades de GPS (rotas), adicione manualmente:

```xml
<!-- AndroidManifest.xml do seu app -->
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

Para download de ficheiros em armazenamento externo:

```xml
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" 
    android:maxSdkVersion="28" />
```

---

## Configuração para Kotlin

O SDK é escrito em Java mas é totalmente compatível com Kotlin:

```kotlin
// Exemplo em Kotlin
class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val config = VeiGestConfig.Builder()
            .setBaseUrl("https://veigestback.dryadlang.org")
            .setDebugMode(BuildConfig.DEBUG)
            .build()
        
        VeiGestSDK.init(applicationContext, config)
    }
}

// Uso com Kotlin
VeiGestSDK.getInstance().vehicles().list(object : VeiGestCallback<List<Vehicle>> {
    override fun onSuccess(vehicles: List<Vehicle>) {
        vehicles.forEach { vehicle ->
            println("${vehicle.matricula} - ${vehicle.marca}")
        }
    }
    
    override fun onError(error: VeiGestException) {
        println("Erro: ${error.message}")
    }
})
```

### Com Coroutines (Extensão)

```kotlin
// Extensão para usar com coroutines
suspend fun <T> VeiGestCallback<T>.await(): T = suspendCancellableCoroutine { cont ->
    // Implementar wrapper...
}
```

---

## Verificar Instalação

Após a instalação, verifique se tudo está correto:

```java
// No onCreate() da sua Activity
try {
    VeiGestConfig config = new VeiGestConfig.Builder().build();
    VeiGestSDK sdk = VeiGestSDK.init(this, config);
    
    Log.d("VeiGest", "SDK inicializado com sucesso!");
    Log.d("VeiGest", "Base URL: " + sdk.getConfig().getBaseUrl());
    Log.d("VeiGest", "Debug Mode: " + sdk.getConfig().isDebugMode());
    
} catch (Exception e) {
    Log.e("VeiGest", "Erro ao inicializar SDK: " + e.getMessage());
}
```

---

## Problemas Comuns na Instalação

### Erro: "Cannot resolve symbol 'VeiGestSDK'"

**Causa**: O módulo não foi incluído corretamente.

**Solução**:
1. Verifique se `include(":veigest-sdk")` está no `settings.gradle.kts`
2. Execute `File > Sync Project with Gradle Files`
3. Se persistir, execute `File > Invalidate Caches / Restart`

### Erro: "Duplicate class found in modules"

**Causa**: Conflito de versões de dependências.

**Solução**:
```kotlin
// app/build.gradle.kts
configurations.all {
    resolutionStrategy {
        force("com.squareup.okhttp3:okhttp:4.12.0")
        force("com.google.code.gson:gson:2.10.1")
    }
}
```

### Erro: "minSdk 24 cannot be lower than..."

**Causa**: Sua app tem minSdk inferior a 24.

**Solução**: Atualize o minSdk da sua app para 24 ou superior:
```kotlin
android {
    defaultConfig {
        minSdk = 24  // Mínimo suportado pelo SDK
    }
}
```

### Erro: "Java 11 is required"

**Solução**: Configure o Java 11 no seu projeto:
```kotlin
android {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
```

---

## Próximos Passos

Após a instalação bem sucedida:
1. [Configure o SDK](CONFIGURATION.md)
2. [Configure a Autenticação](AUTHENTICATION.md)
3. [Comece a usar os Serviços](SERVICES.md)

---

## Suporte

Se encontrar problemas na instalação:
- Consulte o [Troubleshooting](TROUBLESHOOTING.md)
- Abra uma issue no GitHub
- Contacte: suporte@veigest.com
