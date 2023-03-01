# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

## SDK自身混淆规则
#-keep class com.sf.appupdater.entity.** { *; }
#-keep class * extends com.sf.appupdater.tinkerpatch.app.HotfixApplicationProxy{ *; }
#-keep class * extends com.sf.appupdater.tinkerpatch.app.HotFixApplication{ *; }
#-dontwarn com.sf.appupdater.tinkerpatch.**
#

# OkHttp混淆规则
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**


# Gson混淆规则
-keepattributes Signature
-keepattributes *Annotation*
#-keep class sun.misc.Unsafe { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer


# stetho混淆规则
-dontwarn com.facebook.stetho.**


# tinker 混淆规则
-keep class com.tencent.tinker.** { *; }
-dontwarn com.tencent.tinker.**