% Android Reverse Engineering 101
% FIXME Hackerspace
% Jean-Baptiste (Rorist) Aubort - \today

![Inside the Droid](resources/android-re.jpg)

# Summary

* Introduction to APK format
* Static analysis
    * Dalvik bytecode and Smali
    * Decompress resources and decompile to Smali
    * Modify Smali code
    * Repackage application (compile, sign)
    * What to exploit ?
        * Use tcpdump
        * Use Dalvik Debug Monitor Server (DDMS)
* Dynamic analysis
    * Introduction to Dynamic Dalvik Instrumentation (DDI)
    * Attacks
        * Code Injection using hijack
        * Android API method hooks

![Skating droids](resources/droid.png)

# Introduction to APK format

* APK is basically a ZIP file, with some optimization (see zipalign)
* Structure

```
 AndroidManifest.xml...Definitions of the application
 classes.dex...........Java class compiled to Dalvik bytecode
 resources.asc.........Resources index (generated from R.java)
 res...................Compressed resources (xml, images)
 lib...................Architecture dependent binaries
 META-INF..............Signatures and checksums
```

* aapt is an sdk tool to APKs (package, extract information, ..)

```
$ aapt dump strings example-app-debug.apk #dump strings from the resources table
$ aapt dump permissions example-app-debug.apk
$ aapt dump resources example-app-debug.apk
```

# Introduction to the Dalvik VM

## History

* Dalvik is a town in Iceland, Smali means assembler in Icelandic :)
* It has been developed by Android Inc. which was aquired by Google in 2007

## Technology

* Dalvik has some optimisations with memory consumption in mind
* No stack, register based VM -> Less instructions but bigger filesize
* Java source code is compiled to Java bytecode (with javac) and then compiled to Dalvik bytecode (with dx)
* Dex is compressed (code reuse)
* Zygote model:
    * Zygote process is spawned after boot time, instancating a DVM
    * This VM loads core libraries from Android
    * When an app is started, it forks from Zygote
    * App share core libraries, and copy-on-write to their own process when needed

# Introduction to Smali

## Smali is based on the DEX format obtained with dexdump
* Invoke a (private) method

```
invoke-direct {p0}, Lch/fixme/workshop2/MainActivity;->checkSerial()Z
```

* v0 is a local register, p0 is a parameter register
* local registers are reset when invoke is called
* The first parameter of a method is always a reference to its object
* The method signature is constructed like this: Lpackage/name/ObjectName;
* Types in brakets one after the other: (ILjava/lang/String) = (int, String)

## Valid types:

* V = void - can only be used for return types
* Z = boolean, B = byte, S = short, C = char
* I = int, J = long (64 bits), F = float, D = double (64 bits)

# Introduction to Smali

## Example of a test() method extracted with dexdump

```
$ SDKPATH='/opt/android-sdk/build-tools/17.0.0'
$ $SDKPATH/dexdump -d ./bin/classes.dex | less

...
[00045c] ch.fixme.workshop.MainActivity.test:()V
0000: iget-boolean v0, v2, Lch/fixme/workshop/MainActivity;.valid:Z
0002: if-eqz v0, 0011 // +000f
0004: const/high16 v0, #int 2131099648 // #7f06
0006: invoke-virtual {v2, v0}, L..;.findViewById:(I)Landroid/view/View;
0009: move-result-object v0
000a: check-cast v0, Landroid/widget/TextView; // type@0004
000c: const-string v1, "CONGRATZ!" // string@0004
000e: invoke-virtual {v0, v1}, L..;.setText:(Ljava/lang/CharSequence;)V
0011: return-void
...
```

# Introduction to Smali

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
invoke-virtual {p0, v0}, L..;->findViewById(I)Landroid/view/View;
move-result-object v0
check-cast v0, Landroid/widget/TextView;
const-string v1, "CONGRATZ!"
invoke-virtual {v0, v1}, L..;->setText(Ljava/lang/CharSequence;)V
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
$ wget -O apktool.jar http://goo.gl/9ne870
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

## Example 1
* Open and edit the Main class
* Find where to modify the "valid" field in MainActivity.smali:7 to true
* Repackage and sign

```
$ apktool build example-app-debug
$ cd example-app-debug/dist
# Install opendjk-7-jdk and use jarsigner from it
# update-alternative --config jarsigner #on debian
$ jarsigner -digestalg SHA1 -sigalg MD5withRSA -verbose \
    -keystore ~/.android/debug.keystore ./example-app-debug.apk \
    -storepass android androiddebugkey
$ adb install ./example-app-debug.apk
```

## Example 2

* Decompile to smali
* Print the key somewhere, using this debug code (you also have to modify 
the .locals directive to match the number of local register accessible in 
the method

```
const-string v0, "KEY"
const-string v1, "TEST"
invoke-static {v0, v1}, Landroid/util/Log;
    ->e(Ljava/lang/String;Ljava/lang/String;)I
```

* Repackage and test

# What to exploit ?
## Use tcpdump

* Start the emulator from the CLI, saving all of its traffic in a pcap file

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

## JNI/NDK introduction

* NDK provides tools to work with native code
    * Full access to Android SDK
    * Cross compilation for ARM
    * Performances of native code is now balanced since JIT appeared (Froyo)
* JNI is an interface between native code and Java
    * Call JAVA methods from C and vice-versa

## How does DDI work ?

* Based on ADBI (Android Dynamic Binary Instrumentation)
    * Injects binary at run time
    * Hijack: library (shared object) injection to a running PID
    * LibBase: 
* ADBI transforms Dalvik method to native code using JNI (java native interface)
* DDI provides helpers for hooking dalvik methods, importing dex classes at run time
* Then it calls the original method from C
* You have access to everything in C (there is no private/protected method)
* Injects an .so library to a running Dalvik VM
    * Since all DVM are forked from the Zygot process, that is were you want to be

# Introduction to Dynamic Dalvik Instrumentation (DDI)

## Code Injection using hijack

* Steps
    * Push library (.so) and Dalvik code (.dex) to /data/local/tmp
    * Enable dex loading: chmod 777 /data/dalvik-cache/
    * ./hijack -p PID -l /data/local/tmp/lib.so
* This injects the library lib.so into the running process with PID
* But we want to inject before app is loaded, that is why we inject into Zygote!

```
PID=$(adb shell ps | grep zygote | awk '{print $2}')
adb shell "/data/local/tmp/hijack -p $PID -l /data/local/tmp/lib.so \
             -s org.mulliner.collin.work"
```

# Introduction to Dynamic Dalvik Instrumentation (DDI)

## I am hooking, they are hating

* Hooking and calling the original function, using LibDalvikHook

```
struct dalvik_hook_t h;   // hook data, remembers stuff for you
static struct dexstuff_t libdhook;
// setup the hook
dalvik_hook_setup(
   &h,                                  // hook data
   "Lch/fixme/workshop3/MainActivity;", // class name
   "test",                              // method name
   "()Ljava/lang/String;",              // method signature
   1,                                   // insSize
   hook_func_test                       // hook function
);
// place hook
dalvik_hook(&libdhook, &h);
```

# Introduction to Dynamic Dalvik Instrumentation (DDI)

## Place the hooking function

```
static jstring hook_func_test(JNIEnv *env, jobject this) {
    return (*env)->NewStringUTF(env, "IM HOOKING");
}
```

## I am hooking, they are hating

* Compile and exploit

```
cd jni
ndk-build
cd ..
ant debug install
adb push libs/armeabi/libtest.so /data/local/tmp/
PID=$(adb shell ps | grep workshop3 | awk '{print $2}')
adb shell "/data/local/tmp/hijack -p $PID -l /data/local/tmp/libtest.so"
```

# Introduction to Dynamic Dalvik Instrumentation (DDI)

## Android API method hooks

* Find and load the Class

```
cls = dvmFindLoadedClass("Ljava/lang/String;");
met = dvmFindVirtualMethodHierByDescriptor(cls, 
    "compareTo", "(Ljava/lang/String;)I");
```

* Create a hook function and bind String.compareTo() to it

```
int dalvik_func_hook(JNIEnv *env, jobject this, jobject str) {
    /* evil code */
}
dvmUseJNIBridge(met, dalvik_func_hook);
```

#References

## General
* <https://en.wikipedia.org/wiki/APK_(file_format)>
* <https://en.wikipedia.org/wiki/Dalvik_(software)>

## Dalvik
* <http://source.android.com/devices/tech/dalvik/dalvik-bytecode.html>
* <http://pallergabor.uw.hu/androidblog/dalvik_opcodes.html>
* <http://www.milk.com/kodebase/dalvik-docs-mirror/docs/dalvik-bytecode.html>
* <http://davidehringer.com/software/android/The_Dalvik_Virtual_Machine.pdf>
* <http://projekter.aau.dk/projekter/files/63640573/rapport.pdf>

## Smali
* <https://bitbucket.org/JesusFreke/smali/>
* <https://code.google.com/p/smali/>
* <https://code.google.com/p/smali/w/list>
* <http://forum.xda-developers.com/showthread.php?t=2193735>
* <http://wiki.smartphonefrance.info/reversing-android.ashx>

#References

## Dynamic instrumentation
* <http://www.cydiasubstrate.com/>
* <https://github.com/crmulliner/ddi>

