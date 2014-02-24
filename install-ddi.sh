#!/bin/bash

# Install NDK
wget http://dl.google.com/android/ndk/android-ndk-r9c-linux-x86.tar.bz2
tar xvf android-ndk-r9c-linux-x86.tar.bz2
echo 'PATH=$PATH:~/android-ndk-r9c/' >> .bashrc

# Run emulator
emulator -avd Android-2.3&
echo -n 'Starting emulator (wait 60s) .'
for i in $(seq 60); do
    sleep 1
    echo -n '.'
done

# Install ADBI
git clone https://github.com/crmulliner/adbi.git
cd adbi/hijack/jni/
ndk-build
cd ..
adb push libs/armeabi/hijack /data/local/tmp/
cd ~/adbi/instruments/base/jni
ndk-build

# Install DDI
cd
git clone https://github.com/crmulliner/ddi.git
cd ddi/dalvikhook/jni/libs
adb pull /system/lib/libdl.so
adb pull /system/lib/libdvm.so
cd ~/ddi/dalvikhook/jni
ndk-build

# To build the examples, add -w to LOCAL_CFLAGS in the Android.mk files (newest version of the ndk check for security)
# LOCAL_CFLAGS := -g -w
