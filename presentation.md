% Android reverse engineering
% Jean-Baptiste (Rorist) Aubort
% 2014-02-13 at FIXME

# Summary

* Introduction to APK format
* Static analysis
    * Dalvik bytecode and Smali
    * Decompress resources and decompile to Smali
    * Modify Smali code
    * Repackage application (compile, sign)
* Dynamic analysis
    * Introduction to Dynamic Dalvik Instrumentation (DDI)
    * What to exploit ?
        * Use tcpdump
        * Use Dalvik Debug Monitor Server (DDMS)
    * Code Injection using hijack
    * Android API method hooks

# Introduction to APK format

* APK is ZIP
* Structure
    * META-INF              Signatures and checksums
    * lib                   Architecture dependent binaries
    * res                   Uncompiled resources
    * assets                Application assets (resources without localisation)
    * AndroidManifest.xml   Application description
    * classes.dex           Application code in Dalvik bytecode
    * resources.asc         Compiled resources (strings, images, ...)

# Static analysis

## Introduction to Dalvik bytecode and to Smali

### Dalvik bytecode

* Java source code is compiled to Java bytecode (with javac) and then compiled to Dalvik bytecode (with dx)
* Dalvik has some optimisations with memory consumption in mind
    * No stack, direct access to variables
    * FIXME

---

### Smali

* Smali is inspired by the official DEX representation format (optained with dexdump)

```
$ SDKPATH='/opt/android-sdk/build-tools/17.0.0'
$ $SDKPATH/dexdump -d ./bin/classes.dex | less

  Virtual methods   -
  #0              : (in Lch/fixme/status/Main$GetImage;)
    name          : 'doInBackground'
    type          : '([Ljava/lang/String;)Landroid/graphics/Bitmap;'

  003150: ch.fixme.status.Main.GetImage.doInBackground:([Ljava/lang/String;)Landroid/graphics/Bitmap;
  003160: new-instance v1, Lch/fixme/status/Net; // type@0044
  003164: const/4 v2, #int 0 // #0
  003166: aget-object v2, v4, v2
  00316a: invoke-direct {v1, v2}, Lch/fixme/status/Net;.<init>:(Ljava/lang/String;)V // method@00b9
  003170: invoke-virtual {v1}, Lch/fixme/status/Net;.getBitmap:()Landroid/graphics/Bitmap; // method@00ba
  003176: move-result-object v1
  003178: return-object v1
    ...
```

---

## Decompress resources and decompile to Smali

* Use apktool

```
$ ~/android/apktool/apktool decode insOTP.apk
I: Using Apktool 2.0.0-Beta7 on insOTP.apk
I: Loading resource table...
I: Decoding AndroidManifest.xml with resources...
I: Loading resource table from file: /home/rorist/apktool/framework/1.apk
I: Regular manifest package...
I: Decoding file-resources...
I: Decoding values */* XMLs...
I: Loading resource table...
I: Baksmaling...
I: Copying assets and libs...
I: Copying unknown files/dir...
I: Copying original files...
```

---

## Modify Smali code
## Repackage application (compile, sign)

## Look at the code with jd-gui (Java Decompiler) and dex2jar

```
$ ~/android/dex2jar-0.0.7.11/dex2jar.sh ./insOTP.apk
1 [main] INFO com.googlecode.dex2jar.v3.Main - version:0.0.7.11-SNAPSHOT
9 [main] INFO com.googlecode.dex2jar.v3.Main - dex2jar ./insOTP.apk -> insOTP_dex2jar.jar
1990 [main] INFO com.googlecode.dex2jar.v3.Main - Done.

$ ~/android/jd-gui insOTP_dex2jar.jar
```

#Dynamic analysis

## Introduction to Dynamic Dalvik Instrumentation (DDI)
## What to exploit ?
### Use tcpdump
### Use Dalvik Debug Monitor Server (DDMS)
## Code Injection using hijack
## Android API method hooks

#References

## General
* https://en.wikipedia.org/wiki/APK_(file_format)
* https://en.wikipedia.org/wiki/Dalvik_(software)
* https://github.com/crmulliner/ddi

## Dalvik
* http://source.android.com/devices/tech/dalvik/dalvik-bytecode.html
* http://pallergabor.uw.hu/androidblog/dalvik_opcodes.html

## Smali
* https://code.google.com/p/Smali/

