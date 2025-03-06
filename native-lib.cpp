#include <jni.h>
#include <string>
#include <sdsl/suffix_arrays.hpp>
#include <fstream>
#include "random_generator.h"
#include "wavelet_engine.h"

using namespace sdsl;

extern "C" {

// JNI method for random file generation
JNIEXPORT jboolean JNICALL
Java_org_ruizlab_sdslandroid_WaveletProcess_randomFileGenerator(JNIEnv *env, jobject thiz,
                                                                jstring file_storage_path,
                                                                jint file_size) {
    const char *filePath = env->GetStringUTFChars(file_storage_path, nullptr);
    std::string filePathStr(filePath);
    int res = random_file_generator(filePathStr, static_cast<int>(file_size));
    env->ReleaseStringUTFChars(file_storage_path, filePath);
    return (res == 2) ? JNI_TRUE : JNI_FALSE;
}

// JNI method for bit vector generation
JNIEXPORT jboolean JNICALL
Java_org_ruizlab_sdslandroid_WaveletProcess_bitVectorGenerator(JNIEnv *env, jobject thiz,
                                                               jstring file_storage_path,
                                                               jint file_size) {
    const char *filePath = env->GetStringUTFChars(file_storage_path, nullptr);
    std::string filePathStr(filePath);

    // Example implementation of bit vector generation
    bit_vector bv(file_size, 0);
    for (size_t i = 0; i < bv.size(); ++i) {
        bv[i] = i % 2; // Alternate 0 and 1
    }

    bool success = sdsl::store_to_file(bv, filePathStr + "/bit_vector_output.sdsl");
    env->ReleaseStringUTFChars(file_storage_path, filePath);
    return success ? JNI_TRUE : JNI_FALSE;
}

// JNI method for wavelet engine processing
JNIEXPORT jboolean JNICALL
Java_org_ruizlab_sdslandroid_WaveletProcess_waveletEngine(JNIEnv *env, jobject thiz,
                                                          jstring file_storage_path) {
    const char *filePath = env->GetStringUTFChars(file_storage_path, nullptr);
    std::string filePathStr(filePath);

    std::string testType = "WAVELET_TREE"; // Example: can change to other types
    int res = wavelet_engine(testType, filePathStr);

    env->ReleaseStringUTFChars(file_storage_path, filePath);
    return (res == 0) ? JNI_TRUE : JNI_FALSE;
}

} // extern "C"



