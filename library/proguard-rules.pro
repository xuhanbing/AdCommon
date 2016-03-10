# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#waps start
-keep public class cn.waps.** {*;}
-keep public interface cn.waps.** {*;}
#对于使用4.0.3以上android-sdk进行项目编译时产生异常的情况时,加入以下内容：
-dontwarn cn.waps.**
#waps end

#youmi start
-dontwarn ofs.ahd.dii.**
-keepclassmembers class ofs.ahd.dii.libs.adsbase.js.base.JsInterface_Impl {
    *;
}
#youmi end

# weixin sdk
-dontwarn com.tencent.mm.sdk.**
-keep class com.tencent.mm.sdk.** {
    *;
}
# weixin sdk end