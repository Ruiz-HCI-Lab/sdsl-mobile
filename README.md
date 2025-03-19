# SDSL Mobile

SDSL Mobile is a project for porting the Succinct Data Structure Library (SDSL-lite) to Android platforms, enabling space-efficient data structures for mobile applications. This project demonstrates the feasibility of deploying succinct data structures on mobile devices, providing new opportunities for advanced data processing in resource-constrained environments.

This repository contains the necessary configurations and build commands to compile the library for different Android ABIs.

It also contains a demonstrative Android application with SDSL-lite tests and a specific implementation of Wavelet Forests that consumes the library. 

## Acknowledgements

This project ports the Succinct Data Structure Library (SDSL-lite) to Android platforms. We thank the original developers [SDSL-lite repository](https://github.com/simongog/sdsl-lite) for providing the foundation for this work.

## Usage

To use SDSL-Mobile in your Android application, follow these steps:

1. **Add the Native Library**: Include the prebuilt SDSL library in your Android project. Prebuilt libraries are in the [Libraries Folder](app/src/main/cpp/libs). You can also build your library by following the Build Commands section.
2. **Incorporate Library in CMake**: Follow the Adding the Library section for more details.
3. **Build the Project:**
    - Use your build system to compile the project. For example, if you use Android Studio, build the project as usual.

### Example Usage

Once the library is added and included in your CMake project, you can use it in your source files. Here’s an example of how to use the new library in your code:

```cpp
#include <new_library_header.h>

void exampleFunction() {
    // Use functions from the new library
    NewLibraryClass obj;
    obj.doSomething();
}
3. **JNI Integration**: Use the Java Native Interface (JNI) to call SDSL functions from your Java code.
4. **Example Code**:
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
 ```

## Build Commands

If you want to build and include the library in your Android application, the following commands can be used to construct the SDSL library for different Android ABIs. 
Set the `BUILD_PORTABLE` environment variable to `1` to ensure portability.

### Obtain the SDSL-lite code
```sh
git clone https://github.com/simongog/sdsl-lite.git
cd sdsl-lite
```

### ABI Specific Commands

Build the libraries for your phone, depending on specific ABI.

#### AARCH64 (arm64-v8a)

```sh
BUILD_PORTABLE=1 CC=~/Android/Sdk/ndk/25.1.8937393/toolchains/llvm/prebuilt/linux-x86_64/bin/aarch64-linux-android28-clang CXX=~/Android/Sdk/ndk/25.1.8937393/toolchains/llvm/prebuilt/linux-x86_64/bin/aarch64-linux-android28-clang++ ./install.sh ~/[pathToYourApp]/app/src/main/cpp/libs/arm64-v8a
```

#### x86_64

```sh
BUILD_PORTABLE=1 CC=~/Android/Sdk/ndk/25.1.8937393/toolchains/llvm/prebuilt/linux-x86_64/bin/x86_64-linux-android28-clang CXX=~/Android/Sdk/ndk/25.1.8937393/toolchains/llvm/prebuilt/linux-x86_64/bin/x86_64-linux-android28-clang++ ./install.sh ~/[pathToYourApp]/app/src/main/cpp/libs/arm64-v8a
```

#### ARMv7a (armeabi-v7a)

```sh
BUILD_PORTABLE=1 CC=~/Android/Sdk/ndk/25.1.8937393/toolchains/llvm/prebuilt/linux-x86_64/bin/armv7a-linux-androideabi28-clang CXX=~/Android/Sdk/ndk/25.1.8937393/toolchains/llvm/prebuilt/linux-x86_64/bin/armv7a-linux-androideabi28-clang++ ./install.sh ~/[pathToYourApp]/app/src/main/cpp/libs/arm64-v8a
```

#### x86

```sh
BUILD_PORTABLE=1 CC=~/Android/Sdk/ndk/25.1.8937393/toolchains/llvm/prebuilt/linux-x86_64/bin/i686-linux-android28-clang CXX=~/Android/Sdk/ndk/25.1.8937393/toolchains/llvm/prebuilt/linux-x86_64/bin/i686-linux-android28-clang++ ./install.sh ~/[pathToYourApp]/app/src/main/cpp/libs/arm64-v8a
```

## Troubleshooting

If you encounter any issues during the build process, ensure that the following flag is set:

```sh
-msse4.2
```

## Adding the Library

To add a new library to your CMake project, follow these steps:

1. **Modify your `CMakeLists.txt` file:**
    - Add the library source files to the `add_library` command.
    - Set any required properties for the library.
    - Link the library with any necessary dependencies.

    Here’s an example of how to add a new library named `new_library`:

    ```cmake
    # Adding a new library `new_library`
    add_library(new_library STATIC IMPORTED)
    set_target_properties(new_library PROPERTIES IMPORTED_LOCATION
            ${distribution_DIR}/lib/libnew_library.a)

    # Specify include directories for the new library
    target_include_directories(new_library PRIVATE ${distribution_DIR}/include)

    # Link the new library to the main project library
    target_link_libraries(${CMAKE_PROJECT_NAME}
            # Existing libraries
            android
            sdsl
            divsufsort
            divsufsort64
            log
            # New library
            new_library)
    ```

2. **Include the Library’s Headers:**
    - In your project's source files, include the necessary headers for the new library.

    ```cpp
    #include <new_library_header.h>
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
