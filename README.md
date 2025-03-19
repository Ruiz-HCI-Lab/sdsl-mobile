# SDSL Mobile

SDSL Mobile is a project for porting the Succinct Data Structure Library (SDSL-lite) to Android platforms, enabling space-efficient data structures for mobile applications. This project demonstrates the feasibility of deploying succinct data structures on mobile devices, providing new opportunities for advanced data processing in resource-constrained environments.

This repository contains the necessary configurations and build commands to compile the library for different Android ABIs.

It also contains a demonstrative Android application with SDSL-lite tests and a specific implementation of Wavelet Forests that consumes the library. 

## Usage

To use SDSL-Mobile in your Android application, follow these steps:

1. **Add the Native Library**: Include the prebuilt SDSL library in your Android project, or build them using the provided ABI commands. Prebuilt libraries are in the [Libraries Folder](app/src/main/cpp/libs).
2. **JNI Integration**: Use the Java Native Interface (JNI) to call SDSL functions from your Java code.
3. **Example Code**:
   ```java
   public class MainActivity extends AppCompatActivity {
       static {
           System.loadLibrary("sdsl-mobile");
       }

       private native void sdslFunction();

       @Override
       protected void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           setContentView(R.layout.activity_main);
           sdslFunction();
       }
   }

## Build Commands

If you want to build and include the library in your Android application, the following commands can be used to construct the SDSL library for different Android ABIs. 
Set the `BUILD_PORTABLE` environment variable to `1` to ensure portability.

### Common Build Command

```sh
BUILD_PORTABLE=1 CC=<clang_path> CXX=<clang++_path>
```

### ABI Specific Commands

#### AARCH64 (arm64-v8a)

```sh
BUILD_PORTABLE=1 CC=~/Android/Sdk/ndk/25.1.8937393/toolchains/llvm/prebuilt/linux-x86_64/bin/aarch64-linux-android28-clang CXX=~/Android/Sdk/ndk/25.1.8937393/toolchains/llvm/prebuilt/linux-x86_64/bin/aarch64-linux-android28-clang++
```

#### x86_64

```sh
BUILD_PORTABLE=1 CC=~/Android/Sdk/ndk/25.1.8937393/toolchains/llvm/prebuilt/linux-x86_64/bin/x86_64-linux-android28-clang CXX=~/Android/Sdk/ndk/25.1.8937393/toolchains/llvm/prebuilt/linux-x86_64/bin/x86_64-linux-android28-clang++
```

#### ARMv7a (armeabi-v7a)

```sh
BUILD_PORTABLE=1 CC=~/Android/Sdk/ndk/25.1.8937393/toolchains/llvm/prebuilt/linux-x86_64/bin/armv7a-linux-androideabi28-clang CXX=~/Android/Sdk/ndk/25.1.8937393/toolchains/llvm/prebuilt/linux-x86_64/bin/armv7a-linux-androideabi28-clang++
```

#### x86

```sh
BUILD_PORTABLE=1 CC=~/Android/Sdk/ndk/25.1.8937393/toolchains/llvm/prebuilt/linux-x86_64/bin/i686-linux-android28-clang CXX=~/Android/Sdk/ndk/25.1.8937393/toolchains/llvm/prebuilt/linux-x86_64/bin/i686-linux-android28-clang++
```

## Troubleshooting

If you encounter any issues during the build process, ensure that the following flag is set:

```sh
-msse4.2
```

## Performance Evaluation

We performed a comprehensive evaluation comparing the performance of SDSL-Mobile on Android and desktop platforms. Key metrics include CPU time, wall time, RAM usage, and CPU temperature. The results indicate that while performance is lower on Android due to inherent hardware constraints, SDSL-Mobile remains functional and viable for lightweight applications.

| Metric            | Desktop  | Android  |
|-------------------|----------|----------|
| CPU Time (ms)     | 408      | 868      |
| Wall Time (ms)    | 454      | 885      |
| Avg RAM Usage (MB)| 2596.23  | 2760.47  |
| Max RAM Usage (MB)| 3498.35  | 3781.52  |
| Avg CPU Temp (°C) | 58.6     | 53.6     |
| Max CPU Temp (°C) | 76       | 72.6     |


## License

This project is licensed under the MIT License. Please look at the [LICENSE](LICENSE) file for details.

## Contributing

Contributions are welcome! Please open an issue or submit a pull request with your changes.

## Contact

If you have any questions, please contact the repository maintainers.

---

*This file was last updated on March 19, 2025.*
