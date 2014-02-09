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

* APK is basically a ZIP file, with some optimization (see zipalign)
* Structure
    * AndroidManifest.xml   Definitions of application, activities, services, permissions, api level
    * classes.dex           Java class compiled to Dalvik bytecode
    * resources.asc         Resources index (generated from R.java)
    * res                   Compressed resources (xml, images)
    * lib                   Architecture dependent binaries
    * assets                Application assets (resources without localization)
    * META-INF              Signatures and checksums
* aapt is the sdk tool used to manipulate APKs (package, extract information, ..)
```
$ aapt dump strings example-app-debug.apk #dump strings from the resources table
$ aapt dump permissions example-app-debug.apk
$ aapt dump resources example-app-debug.apk
```

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
000c: const-string v1, "CONGRATZ!" // string@0004
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
    const-string v1, "CONGRATZ!"
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
$ ln -s ~/apktool/apktool /usr/local/bin/apktool
```

* Use apktool to extract resources and code

```
$ cd ~/apps/example-app
$ ant debug #This generates debug certificate
$ apktool decode ./bin/example-app-debug.apk
```

# Modify and repackage the application

* Open and edit the Main class
* Find where to modify the "valid" field of line 7 to true

```
$ vim example-app-debug/smali/ch/fixme/workshop/MainActivity.smali
```

* Repackage and sign

```
$ apktool build example-app-debug
$ cd example-app-debug/dist
$ jarsigner -digestalg SHA1 -sigalg MD5withRSA -verbose \
    -keystore ~/.android/debug.keystore ./example-app-debug.apk \
    -storepass android androiddebugkey
$ adb install ./example-app-debug.apk
```

# What to exploit ?
## Use tcpdump

* Start the emulator from the command line, saving all of its traffic in a pcap file

```
android -avd MyEmulator -tcpdump /tmp/android.cap
wireshark /tmp/android.cap
```

## Use Dalvik Debug Monitor Server (DDMS)

* DDMS is part of the Android SDK
* Start DDMS, select the application and starts monitoring
* You can monitor threads activity, memory allocation and method calls
* As well as send the emulator some event (call, network change, etc)

## Look at the code with jd-gui (Java Decompiler) and dex2jar

```
$ cd ~/apps/example-app
$ ant debug
$ dex2jar.sh ./bin/example-app-debug.apk
$ jd-gui example-app-debug_dex2jar.jar
```

# Introduction to Dynamic Dalvik Instrumentation (DDI)

## How does this work ?

* DDI transforms Dalvik method to native code using JNI (java native interface)
* Then it calls the original method from C
* You have access to everything in C (there is no private/protected method)
* Injects an .so library to a running Dalvik VM
    * Since all DVM are forked from the Zygot process, that is were you want to be

## Code Injection using hijack
## Android API method hooks

#References

## General
* <https://en.wikipedia.org/wiki/APK_(file_format)>
* <https://en.wikipedia.org/wiki/Dalvik_(software)>
* <https://github.com/crmulliner/ddi>

## Dalvik
* <http://source.android.com/devices/tech/dalvik/dalvik-bytecode.html>
* <http://pallergabor.uw.hu/androidblog/dalvik_opcodes.html>
* <http://www.milk.com/kodebase/dalvik-docs-mirror/docs/dalvik-bytecode.html>

## Smali
* <https://bitbucket.org/JesusFreke/smali/>
* <https://code.google.com/p/smali/>
* <http://forum.xda-developers.com/showthread.php?t=2193735>

