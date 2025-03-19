cmake_minimum_required(VERSION 3.22.1)

project("sdslruiz")

set(CMAKE_CXX_STANDARD 11)
add_compile_options(-O3 -DNDEBUG -msse4.2 -fPIC)

set(distribution_DIR ${CMAKE_CURRENT_SOURCE_DIR}/libs/${ANDROID_ABI})

# Define source files
set(SOURCE_FILES
        native-lib.cpp
        random_generator.cpp
        bit_vector_generator.cpp
        wavelet_engine.cpp
)

# Add the shared library
add_library(${CMAKE_PROJECT_NAME} SHARED ${SOURCE_FILES})

# Include directories
target_include_directories(${CMAKE_PROJECT_NAME} PRIVATE
        ${distribution_DIR}/include
        ${CMAKE_CURRENT_SOURCE_DIR}/includes
)

# Prebuilt external libraries
add_library(sdsl STATIC IMPORTED)
set_target_properties(sdsl PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/lib/libsdsl.a)

add_library(divsufsort STATIC IMPORTED)
set_target_properties(divsufsort PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/lib/libdivsufsort.a)

add_library(divsufsort64 STATIC IMPORTED)
set_target_properties(divsufsort64 PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/lib/libdivsufsort64.a)

# Link libraries
target_link_libraries(${CMAKE_PROJECT_NAME}
        android
        sdsl
        divsufsort
        divsufsort64
        log
)
