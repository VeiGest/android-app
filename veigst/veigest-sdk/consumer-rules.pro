# VeiGest SDK ProGuard Rules

# Keep Retrofit interfaces
-keep interface com.veigest.sdk.api.** { *; }

# Keep all SDK models
-keep class com.veigest.sdk.models.** { *; }

# Keep SDK public API
-keep class com.veigest.sdk.VeiGestSDK { *; }
-keep class com.veigest.sdk.VeiGestConfig { *; }
-keep class com.veigest.sdk.VeiGestCallback { *; }

# Retrofit
-keepattributes Signature
-keepattributes Exceptions
-keepattributes *Annotation*

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
