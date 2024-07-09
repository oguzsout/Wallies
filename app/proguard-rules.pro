# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
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
-keepattributes Signature,Annotation
-keepnames class com.oguzdogdu.network.model.** { *; }
-keepnames class com.oguzdogdu.domain.model.** { *; }
-keep class * extends com.google.protobuf.GeneratedMessageLite { *; }
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
-keep class * implements android.os.Parcelable {
       *;
   }
   -dontwarn org.slf4j.**
   -keep class * extends com.google.gson.TypeAdapter
   -keep class * implements com.google.gson.TypeAdapterFactory
   -keep class * implements com.google.gson.JsonSerializer
   -keep class * implements com.google.gson.JsonDeserializer
   -keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
   -keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken
   -keep,allowobfuscation,allowshrinking interface retrofit2.Call
    -keep,allowobfuscation,allowshrinking class retrofit2.Response
    -keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

    -keep class io.ktor.server.netty.EngineMain { *; }
    -keep class io.ktor.server.config.HoconConfigLoader { *; }
    -keep class com.example.ApplicationKt { *; }
    -keep class kotlin.reflect.jvm.internal.** { *; }
    -keep class kotlin.text.RegexOption { *; }
    -keep class io.ktor.serialization.kotlinx.json.KotlinxSerializationJsonExtensionProvider { *; }

-keep @kotlinx.serialization.Serializable class * {*;}