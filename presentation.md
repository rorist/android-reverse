% Android application reverse engineering
% Jean-Baptiste (Rorist) Aubort
% 2014-02-13 at FIXME
<!--
pandoc -V theme:Warsaw --variable fontsize=8pt -t beamer -s presentation.md -o presentation.pdf
-->

# Summary

* Introduction to APK format
* Static analysis
    * Introduction to Dalvik bytecode and to smali
    * Decompress resources and decompile to smali
    * Modify smali code
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

## Introduction to Dalvik bytecode and to smali

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
$ $SDKPATH/dexdump ./bin/classes.dex | less

    Class #1            -
      Class descriptor  : 'Lch/fixme/status/Main$1;'
      Access flags      : 0x0000 ()
      Superclass        : 'Ljava/lang/Object;'
      Interfaces        -
      Static fields     -
      Instance fields   -
        #0              : (in Lch/fixme/status/Main$1;)
          name          : 'this$0'
          type          : 'Lch/fixme/status/Main;'
          access        : 0x1010 (FINAL SYNTHETIC)
        #1              : (in Lch/fixme/status/Main$1;)
          name          : 'val$url'
          type          : 'Ljava/util/ArrayList;'
          access        : 0x1010 (FINAL SYNTHETIC)
      Direct methods    -
      Virtual methods   -
  ...
```


## Decompress resources and decompile to smali

* Using apktool

> 

## Modify smali code
## Repackage application (compile, sign)

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
* https://code.google.com/p/smali/

