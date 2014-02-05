<!--
pandoc -t beamer -s presentation.md -o presentation.pdf
-->
Android application reverse engineering
=======================================

Summary
-------

* Introduction to APk format
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
