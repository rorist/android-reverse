% Android Reverse Engineering
% FIXME Hackerspace
% Jean-Baptiste (Rorist) Aubort - \today

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
    * Attacks
        * Code Injection using hijack
        * Android API method hooks

# Introduction to APK format

* APK is a ZIP file with some potential optimization (uncompressed file aligned to bytes boundaries, ...)
* Structure
    * META-INF              Signatures and checksums
    * lib                   Architecture dependent binaries
    * res                   Uncompiled resources
    * assets                Application assets (resources without localisation)
    * AndroidManifest.xml   Application description
    * classes.dex           Application code in Dalvik bytecode
    * resources.asc         Compiled resources (strings, images, ...)

# Introduction to the Dalvik bytecode

* Java source code is compiled to Java bytecode (with javac) and then compiled to Dalvik bytecode (with dx)
* Dalvik has some optimisations with memory consumption in mind
    * No stack, direct access to variables
    * FIXME

# Introduction to Smali

* Smali is inspired by the official DEX representation format (optained with dexdump)

## Example of a test() method extracted with dexdump

```
$ SDKPATH='/opt/android-sdk/build-tools/17.0.0'
$ $SDKPATH/dexdump -d ./bin/classes.dex | less

...
[00045c] ch.fixme.workshop.MainActivity.test:()V
0000: iget-boolean v0, v2, Lch/fixme/workshop/MainActivity;.valid:Z // field@0001
0002: if-eqz v0, 0011 // +000f
0004: const/high16 v0, #int 2131099648 // #7f06
0006: invoke-virtual {v2, v0}, Lch/fixme/workshop/MainActivity;.findViewById:(I)Landroid/view/View; // method@0005
0009: move-result-object v0
000a: check-cast v0, Landroid/widget/TextView; // type@0004
000c: const-string v1, "CONGRATULATIONS" // string@0004
000e: invoke-virtual {v0, v1}, Landroid/widget/TextView;.setText:(Ljava/lang/CharSequence;)V // method@0002
0011: return-void
...
```

---

## Example of the same test() method in Smali

```
$ java -jar ~/android-dev/baksmali-2.0.3.jar ./bin/classes.dex
$ less out/ch/fixme/workshop/MainActivity.smali

...
.method private test()V
    .registers 3
    .prologue
    .line 19
    iget-boolean v0, p0, Lch/fixme/workshop/MainActivity;->valid:Z
    if-eqz v0, :cond_11
    .line 20
    const/high16 v0, 0x7f060000
    invoke-virtual {p0, v0}, Lch/fixme/workshop/MainActivity;->findViewById(I)Landroid/view/View;
    move-result-object v0
    check-cast v0, Landroid/widget/TextView;
    const-string v1, "CONGRATULATIONS"
    invoke-virtual {v0, v1}, Landroid/widget/TextView;->setText(Ljava/lang/CharSequence;)V
    .line 22
    :cond_11
    return-void
.end method
...
```

# Use apktool

* Install the latest apktool, which does everything in one go

```
$ cd ~; mkdir apktool; cd apktool
$ wget -O apktool.jar http://miui.connortumbleson.com/other/apktool/test_versions/apktool_2.0.0b7.jar
$ wget https://android-apktool.googlecode.com/git/scripts/linux/apktool
$ chmod +x apktool
```

* Use apktool to extract resources and code

```
$ ~/apktool/apktool decode ./bin/example-app-debug.apk

I: Using Apktool 2.0.0-Beta7 on example-app-debug.apk
I: Loading resource table...
I: Decoding AndroidManifest.xml with resources...
I: Loading resource table from file: /home/zulu/apktool/framework/1.apk
I: Regular manifest package...
I: Decoding file-resources...
I: Decoding values */* XMLs...
I: Loading resource table...
I: Baksmaling...
I: Copying assets and libs...
I: Copying unknown files/dir...
I: Copying original files...
```

# Modify the application

* Open example-app-debug/smali/ch/fixme/workshop/MainActivity.smali
* Modify the "valid" field at line 7 to true

# Repackage application (compile, sign)

# Look at the code with jd-gui (Java Decompiler) and dex2jar

```
$ ~/android/dex2jar-0.0.7.11/dex2jar.sh ./insOTP.apk
1 [main] INFO com.googlecode.dex2jar.v3.Main - version:0.0.7.11-SNAPSHOT
9 [main] INFO com.googlecode.dex2jar.v3.Main - dex2jar ./insOTP.apk -> insOTP_dex2jar.jar
1990 [main] INFO com.googlecode.dex2jar.v3.Main - Done.

$ ~/android/jd-gui insOTP_dex2jar.jar
```

# Introduction to Dynamic Dalvik Instrumentation (DDI)
# What to exploit ?
## Use tcpdump
## Use Dalvik Debug Monitor Server (DDMS)
# Attacks
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
* http://www.milk.com/kodebase/dalvik-docs-mirror/docs/dalvik-bytecode.html

## Smali
* https://bitbucket.org/JesusFreke/smali/
* https://code.google.com/p/Smali/
* http://forum.xda-developers.com/showthread.php?t=2193735

