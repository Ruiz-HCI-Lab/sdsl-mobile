# SDSL Mobile

SDSL Mobile is a project for building a portable version of the SDSL-lite library for Android using the Android NDK. 

This repository contains the necessary configurations and build commands to compile the library for different Android ABIs.

It also contains a demonstrative Android application that includes SDSL-lite tests and a specific implementation of Wavelet Forests that consumes the library. 

## Prerequisites

- **Android NDK**: Ensure you have the Android NDK installed. The paths in the build commands assume the NDK is located at `~/Android/Sdk/ndk/25.1.8937393/`.

## Build Commands

The following commands can be used to build the SDSL library for different Android ABIs. 
Set the `BUILD_PORTABLE` environment variable to `1` to ensure portability.

### Common Build Command

```sh
BUILD_PORTABLE=1 CC=<clang_path> CXX=<clang++_path>

### ABI Specific Commands

#### AARCH64 (arm64-v8a)

```sh
BUILD_PORTABLE=1 CC=~/Android/Sdk/ndk/25.1.8937393/toolchains/llvm/prebuilt/linux-x86_64/bin/aarch64-linux-android28-clang CXX=~/Android/Sdk/ndk/25.1.8937393/toolchains/llvm/prebuilt/linux-x86_64/bin/aarch64-linux-android28-clang++

#### x86_64

```sh
BUILD_PORTABLE=1 CC=~/Android/Sdk/ndk/25.1.8937393/toolchains/llvm/prebuilt/linux-x86_64/bin/x86_64-linux-android28-clang CXX=~/Android/Sdk/ndk/25.1.8937393/toolchains/llvm/prebuilt/linux-x86_64/bin/x86_64-linux-android28-clang++

#### ARMv7a (armeabi-v7a)


BUILD_PORTABLE=1 CC=~/Android/Sdk/ndk/25.1.8937393/toolchains/llvm/prebuilt/linux-x86_64/bin/armv7a-linux-androideabi28-clang CXX=~/Android/Sdk/ndk/25.1.8937393/toolchains/llvm/prebuilt/linux-x86_64/bin/armv7a-linux-androideabi28-clang++

#### x86

BUILD_PORTABLE=1 CC=~/Android/Sdk/ndk/25.1.8937393/toolchains/llvm/prebuilt/linux-x86_64/bin/i686-linux-android28-clang CXX=~/Android/Sdk/ndk/25.1.8937393/toolchains/llvm/prebuilt/linux-x86_64/bin/i686-linux-android28-clang++

## Troubleshooting

If you encounter any issues during the build process, ensure that the following flag is set:

```sh
-msse4.2

## License

This project is licensed under the MIT License. Please look at the [LICENSE](LICENSE) file for details.

## Contributing

Contributions are welcome! Please open an issue or submit a pull request with your changes.

## Contact

For any questions or inquiries, please get in touch with the repository maintainers.

---

*This file was last updated on March 19, 2025.*
