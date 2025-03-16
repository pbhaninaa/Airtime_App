# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Keep class members for WebView with JS
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment to preserve line numbers for debugging
-keepattributes SourceFile,LineNumberTable

# Preserve important classes for debugging
-keep class com.example.** { *; }
-keep class androidx.** { *; }

# Retrofit rules
-dontwarn com.squareup.okhttp3.**
-dontwarn com.squareup.retrofit2.**
