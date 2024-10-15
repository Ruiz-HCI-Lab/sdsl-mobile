/home/ruizlab/Android/Sdk/ndk/25.1.8937393/toolchains/llvm/prebuilt/linux-x86_64/bin/x86_64-linux-android28-clang

/home/ruizlab/Android/Sdk/ndk/25.1.8937393/toolchains/llvm/prebuilt/linux-x86_64/bin/x86_64-linux-android28-clang++

BUILD_PORTABLE=1 CC=~/Android/Sdk/ndk/25.1.8937393/toolchains/llvm/prebuilt/linux-x86_64/bin/x86_64-linux-android28-clang CXX=~/Android/Sdk/ndk/25.1.8937393/toolchains/llvm/prebuilt/linux-x86_64/bin/x86_64-linux-android28-clang++ ./install.sh ~/AndroidStudioProjects/SDSLRuiz/app/src/main/cpp/libs


//ABI SPECIFIC

//CLANG - ABI
//AARCH64 : arm64-v8a
BUILD_PORTABLE=1 CC=~/Android/Sdk/ndk/25.1.8937393/toolchains/llvm/prebuilt/linux-x86_64/bin/aarch64-linux-android28-clang CXX=~/Android/Sdk/ndk/25.1.8937393/toolchains/llvm/prebuilt/linux-x86_64/bin/aarch64-linux-android28-clang++ ./install.sh ~/AndroidStudioProjects/SDSLRuiz/app/src/main/cpp/libs/arm64-v8a

//x86_64 : x86_64
BUILD_PORTABLE=1 CC=~/Android/Sdk/ndk/25.1.8937393/toolchains/llvm/prebuilt/linux-x86_64/bin/x86_64-linux-android28-clang CXX=~/Android/Sdk/ndk/25.1.8937393/toolchains/llvm/prebuilt/linux-x86_64/bin/x86_64-linux-android28-clang++ ./install.sh ~/AndroidStudioProjects/SDSLRuiz/app/src/main/cpp/libs/x86_64

//armv7a-linux-androideabi28-clang++ : armeabi-v7a
BUILD_PORTABLE=1 CC=~/Android/Sdk/ndk/25.1.8937393/toolchains/llvm/prebuilt/linux-x86_64/bin/armv7a-linux-androideabi28-clang CXX=~/Android/Sdk/ndk/25.1.8937393/toolchains/llvm/prebuilt/linux-x86_64/bin/armv7a-linux-androideabi28-clang++ ./install.sh ~/AndroidStudioProjects/SDSLRuiz/app/src/main/cpp/libs/armeabi-v7a

//i686-linux-android28-clang : x86
BUILD_PORTABLE=1 CC=~/Android/Sdk/ndk/25.1.8937393/toolchains/llvm/prebuilt/linux-x86_64/bin/i686-linux-android28-clang CXX=~/Android/Sdk/ndk/25.1.8937393/toolchains/llvm/prebuilt/linux-x86_64/bin/i686-linux-android28-clang++ ./install.sh ~/AndroidStudioProjects/SDSLRuiz/app/src/main/cpp/libs/x86

#if issues check here:
-msse4.2
