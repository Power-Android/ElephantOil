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

#忽略警告
-ignorewarnings

#不要压缩(这个必须，因为开启混淆的时候 默认 会把没有被调用的代码 全都排除掉)
-dontshrink

#保持jar包里的类不被混淆
-keep class com.google.gson.**

##########################################################################默认配置

  # 代码混淆压缩比，在0~7之间，默认为5，一般不做修改
  -optimizationpasses 5

  # 混合时不使用大小写混合，混合后的类名为小写
  -dontusemixedcaseclassnames

  # 指定不去忽略非公共库的类
  -dontskipnonpubliclibraryclasses

  # 这句话能够使我们的项目混淆后产生映射文件
  # 包含有类名->混淆后类名的映射关系
  -verbose

  # 指定不去忽略非公共库的类成员
  -dontskipnonpubliclibraryclassmembers

  # 不做预校验，preverify是proguard的四个步骤之一，Android不需要preverify，去掉这一步能够加快混淆速度。
  -dontpreverify

  # 保留Annotation不混淆
  -keepattributes *Annotation*,InnerClasses

  # 避免混淆泛型
  -keepattributes Signature

  # 抛出异常时保留代码行号
  -keepattributes SourceFile,LineNumberTable

  # 指定混淆是采用的算法，后面的参数是一个过滤器
  # 这个过滤器是谷歌推荐的算法，一般不做更改
  -optimizations !code/simplification/cast,!field/*,!class/merging/*

  # apk 包内所有 class 的内部结构
  #-dump class_files.txt
  # 未混淆的类和成员
  -printseeds seeds.txt
  # 列出从 apk 中删除的代码
  -printusage unused.txt
  # 混淆前后的映射
  -printmapping mapping.txt

  #############################################
  #
  # Android开发中一些需要保留的公共部分
  #
  #############################################

  # 保留我们使用的四大组件，自定义的Application等等这些类不被混淆
  # 因为这些子类都有可能被外部调用
  -keep public class * extends android.app.Activity
  -keep public class * extends android.app.Appliction
  -keep public class * extends android.app.Service
  -keep public class * extends android.content.BroadcastReceiver
  -keep public class * extends android.content.ContentProvider
  -keep public class * extends android.app.backup.BackupAgentHelper
  -keep public class * extends android.preference.Preference
  -keep public class * extends android.view.View
  -keep public class com.android.vending.licensing.ILicensingService

  -ignorewarnings -keep class * { public private *; }

  # 保留support下的所有类及其内部类
  -keep class android.support.** { *; }
  -keep interface android.support.** { *; }
  -dontwarn android.support.**

  -keep class com.google.android.material.** {*;}
  -keep class androidx.** {*;}
  -keep public class * extends androidx.**
  -keep interface androidx.** {*;}
  -dontwarn com.google.android.material.**
  -dontnote com.google.android.material.**
  -dontwarn androidx.**

  # 保留继承的
  -keep public class * extends android.support.v4.**
  -keep public class * extends android.support.v7.**
  -keep public class * extends android.support.annotation.**

  # 保留R下面的资源
  -keep class **.R$* {*;}

  # 保留本地native方法不被混淆
  -keepclasseswithmembernames class * {
      native <methods>;
  }

  # 保留在Activity中的方法参数是view的方法，
  # 这样以来我们在layout中写的onClick就不会被影响
  -keepclassmembers class * extends android.app.Activity{
      public void *(android.view.View);
  }

  # 保留枚举类不被混淆
  -keepclassmembers enum * {
      public static **[] values();
      public static ** valueOf(java.lang.String);
  }

  # 保留我们自定义控件（继承自View）不被混淆
  -keep public class * extends android.view.View{
      *** get*();
      void set*(***);
      public <init>(android.content.Context);
      public <init>(android.content.Context, android.util.AttributeSet);
      public <init>(android.content.Context, android.util.AttributeSet, int);
  }

  # 保留Parcelable序列化类不被混淆
  -keep class * implements android.os.Parcelable {
      public static final android.os.Parcelable$Creator *;
  }

  # 保留Serializable序列化的类不被混淆
  -keepnames class * implements java.io.Serializable
  -keepclassmembers class * implements java.io.Serializable {
      static final long serialVersionUID;
      private static final java.io.ObjectStreamField[] serialPersistentFields;
      !static !transient <fields>;
      !private <fields>;
      !private <methods>;
      private void writeObject(java.io.ObjectOutputStream);
      private void readObject(java.io.ObjectInputStream);
      java.lang.Object writeReplace();
      java.lang.Object readResolve();
  }

  # 对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
  -keepclassmembers class * {
      void *(**On*Event);
      void *(**On*Listener);
  }

  # webView处理，项目中没有使用到webView忽略即可
  -keepclassmembers class fqcn.of.javascript.interface.for.webview {
      public *;
  }
  -keepclassmembers class * extends android.webkit.webViewClient {
      public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
      public boolean *(android.webkit.WebView, java.lang.String);
  }
  -keepclassmembers class * extends android.webkit.webViewClient {
      public void *(android.webkit.webView, jav.lang.String);
  }

  #############################################
  #
  # 项目中特殊处理部分
  #
  #############################################

  # BottomNavigationMenuView
  -keepclassmembers class android.support.design.internal.BottomNavigationMenuView {
      boolean mShiftingMode;
  }
  # SearchView
  -keepclassmembers class android.support.v7.widget.SearchView {
      ImageView mGoButton;
  }

  -keep class com.bjjy.buildtalk.widget.tablayout.**{*;}

  #-----------处理反射类---------------


  #-----------处理js交互---------------


  #-----------处理实体类---------------
  # 在开发的时候我们可以将所有的实体类放在一个包内，这样我们写一次混淆就行了。
  -keep class com.bjjy.buildtalk.entity.**{ *; }



  #-----------处理第三方依赖库---------

  ################ retrofit ###############
  -dontwarn retrofit2.**
  -keep class retrofit2.** { *; }
  -keepattributes Signature
  -keepattributes Exceptions

  ################ gson ###############
  -keepattributes Signature
  -keepattributes *Annotation*
  -keep class sun.misc.Unsafe { *; }
  -keep class com.google.gson.stream.** { *; }
  # Application classes that will be serialized/deserialized over Gson
  -keep class com.sunloto.shandong.bean.** { *; }

  ################ glide ###############
  -keep public class * implements com.bumptech.glide.module.AppGlideModule
  -keep public class * implements com.bumptech.glide.module.LibraryGlideModule
  -keep class com.bumptech.glide.** { *; }
  -keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
      **[] $VALUES;
      public *;
  }

  ################ okhttp ###############
  -keepattributes Signature
  -keepattributes *Annotation*
  -keep class com.squareup.okhttp.** { *; }
  -keep interface com.squareup.okhttp.** { *; }
  -keep class okhttp3.** { *; }
  -keep interface okhttp3.** { *; }
  -dontwarn com.squareup.okhttp.**

  ################ Bugly ###############
  -dontwarn com.tencent.bugly.**
  -keep public class com.tencent.bugly.**{*;}
  -keep class android.support.**{*;}

  ################ RxJava and RxAndroid ###############
  -dontwarn org.mockito.**
  -dontwarn org.junit.**
  -dontwarn org.robolectric.**

  -keep class io.reactivex.** { *; }
  -keep interface io.reactivex.** { *; }

  -keepattributes Signature
  -keepattributes *Annotation*
  -keep class com.squareup.okhttp.** { *; }
  -dontwarn okio.**
  -keep interface com.squareup.okhttp.** { *; }
  -dontwarn com.squareup.okhttp.**

  -dontwarn io.reactivex.**
  -dontwarn retrofit.**
  -keep class retrofit.** { *; }
  -keepclasseswithmembers class * {
      @retrofit.http.* <methods>;
  }

  -keep class sun.misc.Unsafe { *; }

  -dontwarn java.lang.invoke.*

  -keep class io.reactivex.schedulers.Schedulers {
      public static <methods>;
  }
  -keep class io.reactivex.schedulers.ImmediateScheduler {
      public <methods>;
  }
  -keep class io.reactivex.schedulers.TestScheduler {
      public <methods>;
  }
  -keep class io.reactivex.schedulers.Schedulers {
      public static ** test();
  }
  -keepclassmembers class io.reactivex.internal.util.unsafe.*ArrayQueue*Field* {
      long producerIndex;
      long consumerIndex;
  }
  -keepclassmembers class io.reactivex.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
      long producerNode;
      long consumerNode;
  }

  -keepclassmembers class io.reactivex.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
      io.reactivex.internal.util.atomic.LinkedQueueNode producerNode;
  }
  -keepclassmembers class io.reactivex.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
      io.reactivex.internal.util.atomic.LinkedQueueNode consumerNode;
  }

  -dontwarn io.reactivex.internal.util.unsafe.**

  ################ espresso ###############
  -keep class android.support.test.espresso.** { *; }
  -keep interface android.support.test.espresso.** { *; }

  ################ annotation ###############
  -keep class android.support.annotation.** { *; }
  -keep interface android.support.annotation.** { *; }

  ################ LeakCanary #################
  -dontwarn com.squareup.haha.guava.**
  -dontwarn com.squareup.haha.perflib.**
  -dontwarn com.squareup.haha.trove.**
  -dontwarn com.squareup.leakcanary.**
  -keep class com.squareup.haha.** { *; }
  -keep class com.squareup.leakcanary.** { *; }

  #greendao3.2.0,此是针对3.2.0，如果是之前的，可能需要更换下包名
  -keep class org.greenrobot.greendao.**{*;}
  -keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
  public static java.lang.String TABLENAME;
  }
  -keep class **$Properties

  #jakewharton
  -keep class com.jakewharton.** {*;}
  -keep class io.reactivex.** {*;}
  -keep class rx.** {*;}
  -keep class com.jakewharton.rxbinding.** {*;}

  # Dagger2
  -dontwarn com.google.errorprone.annotations.*

  # ButterKnife
  -keep public class * implements butterknife.Unbinder {
      public <init>(**, android.view.View);
  }
  -keep class butterknife.*
  -keepclasseswithmembernames class * {
      @butterknife.* <methods>;
  }
  -keepclasseswithmembernames class * {
      @butterknife.* <fields>;
  }

  # BRVAH
  -keep class com.chad.library.adapter.** { *; }
  -keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
  -keep public class * extends com.chad.library.adapter.base.BaseViewHolder
  -keepclassmembers public class * extends com.chad.library.adapter.base.BaseViewHolder {
      <init>(android.view.View);
  }

  # AndroidEventBus
  -keep class org.simple.** { *; }
  -keep interface org.simple.** { *; }
  -keepclassmembers class * {
      @org.simple.eventbus.Subscriber <methods>;
  }

  # ImmersionBar
  -keep class com.gyf.barlibrary.* {*;}
   -dontwarn com.gyf.barlibrary.**

  # agentWeb
  -keep class com.just.agentweb.** {
      *;
  }
  -dontwarn com.just.agentweb.**
  -keepclassmembers class com.just.agentweb.sample.common.AndroidInterface{ *; }

  # banner 的混淆代码
  -keep class com.youth.banner.** {
      *;
   }

  #PictureSelector 2.0
  -keep class com.luck.picture.lib.** { *; }

  #AVLoading
  -keep class com.wang.avi.** { *; }
  -keep class com.wang.avi.indicators.** { *; }

  # tencentSDK
  -keep class com.tencent.** { *; }
  -keep class com.easefun.polyvsdk.** { *; }
  -keep class tv.danmaku.ijk.media.**{*;}
  -keep class com.easefun.polyvsdk.video.PolyvVideoView
  -keep class com.chinanetcenter.wcs.**{*;}
  -keep class org.apache.http.**{*;}
  -keep class net.lingala.zip4j.**{*;}
  -keep class org.apache.commons.compress.**{*;}
  -keep class com.tencent.mm.sdk.** {#微信支付
     *;
  }

  #腾讯bugly
  -dontwarn com.tencent.bugly.**
  -keep public class com.tencent.bugly.**{*;}
  -keep class android.support.**{*;}

  # 友盟混淆代码
  -dontshrink
  -dontoptimize
  -dontwarn com.google.android.maps.**
  -dontwarn android.webkit.WebView
  -dontwarn com.umeng.**
  -dontwarn com.tencent.weibo.sdk.**
  -dontwarn com.facebook.**
  -keep public class javax.**
  -keep public class android.webkit.**
  -dontwarn android.support.v4.**
  -keep enum com.facebook.**
  -keepattributes Exceptions,InnerClasses,Signature
  -keepattributes *Annotation*
  -keepattributes SourceFile,LineNumberTable

  -keep public interface com.facebook.**
  -keep public interface com.tencent.**
  -keep public interface com.umeng.socialize.**
  -keep public interface com.umeng.socialize.sensor.**
  -keep public interface com.umeng.scrshot.**

  -keep public class com.umeng.socialize.* {*;}


  -keep class com.facebook.**
  -keep class com.facebook.** { *; }
  -keep class com.umeng.scrshot.**
  -keep public class com.tencent.** {*;}
  -keep class com.umeng.socialize.sensor.**
  -keep class com.umeng.socialize.handler.**
  -keep class com.umeng.socialize.handler.*
  -keep class com.umeng.weixin.handler.**
  -keep class com.umeng.weixin.handler.*
  -keep class com.umeng.qq.handler.**
  -keep class com.umeng.qq.handler.*
  -keep class UMMoreHandler{*;}
  -keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
  -keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
  -keep class im.yixin.sdk.api.YXMessage {*;}
  -keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}
  -keep class com.tencent.mm.sdk.** {
     *;
  }
  -keep class com.tencent.mm.opensdk.** {
     *;
  }
  -keep class com.tencent.wxop.** {
     *;
  }
  -keep class com.tencent.mm.sdk.** {
     *;
  }

  -keep class com.twitter.** { *; }

  -keep class com.tencent.** {*;}
  -dontwarn com.tencent.**
  -keep class com.kakao.** {*;}
  -dontwarn com.kakao.**
  -keep public class com.umeng.com.umeng.soexample.R$*{
      public static final int *;
  }
  -keep public class com.linkedin.android.mobilesdk.R$*{
      public static final int *;
  }
  -keepclassmembers enum * {
      public static **[] values();
      public static ** valueOf(java.lang.String);
  }

  -keep class com.tencent.open.TDialog$*
  -keep class com.tencent.open.TDialog$* {*;}
  -keep class com.tencent.open.PKDialog
  -keep class com.tencent.open.PKDialog {*;}
  -keep class com.tencent.open.PKDialog$*
  -keep class com.tencent.open.PKDialog$* {*;}
  -keep class com.umeng.socialize.impl.ImageImpl {*;}
  -keep class com.sina.** {*;}
  -dontwarn com.sina.**
  -keep class  com.alipay.share.sdk.** {
     *;
  }

  -keepnames class * implements android.os.Parcelable {
      public static final ** CREATOR;
  }

  -keep class com.linkedin.** { *; }
  -keep class com.android.dingtalk.share.ddsharemodule.** { *; }
  -keepattributes Signature
  -keep class com.umeng.** {*;}

  -keepclassmembers class * {
     public <init> (org.json.JSONObject);
  }

  -keepclassmembers enum * {
      public static **[] values();
      public static ** valueOf(java.lang.String);
  }

  #PDFViewer
  -keep class com.shockwave.**

  #阿里云认证服务
  -keep class cn.com.chinatelecom.gateway.lib.** {*;}
  -keep class com.unicom.xiaowo.login.** {*;}
  -keep class com.cmic.sso.sdk.** {*;}
  -keep class com.mobile.auth.** {*;}
  -keep class android.support.v4.** { *;}
  -keep class org.json.**{*;}
  -keep class com.alibaba.fastjson.** {*;}

-dontwarn dalvik.**
-dontwarn com.tencent.smtt.**

-keep class com.tencent.smtt.** {
    *;
}

-keep class com.tencent.tbs.** {
    *;
}

-keep class **_FragmentFinder { *; }
-keep class androidx.fragment.app.* { *; }

-keep class com.qmuiteam.qmui.arch.record.RecordIdClassMap { *; }
-keep class com.qmuiteam.qmui.arch.record.RecordIdClassMapImpl { *; }

-keep class com.qmuiteam.qmui.arch.scheme.SchemeMap {*;}
-keep class com.qmuiteam.qmui.arch.scheme.SchemeMapImpl {*;}

#混淆配置

#基线包使用，生成mapping.txt
-printmapping mapping.txt
#生成的mapping.txt在app/build/outputs/mapping/release路径下，移动到/app路径下
#修复后的项目使用，保证混淆结果一致
#-applymapping mapping.txt
#hotfix
-keep class com.taobao.sophix.**{*;}
-keep class com.ta.utdid2.device.**{*;}
#防止inline
-dontoptimize

-keepclassmembers class com.xxjy.jyyh.app.App {
    public <init>();
}
-keep class com.xxjy.jyyh.app.SophixStubApplication$RealApplicationStub

#数美风控
-keep class com.ishumei.** {*;}
