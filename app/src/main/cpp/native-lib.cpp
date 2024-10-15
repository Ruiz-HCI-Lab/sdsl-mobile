#include <jni.h>
#include <string>
#include <sdsl/suffix_arrays.hpp>
#include <fstream>
#include "random_generator.h"
#include "wavelet_engine.h"


using namespace sdsl;

extern "C"
JNIEXPORT jboolean JNICALL
Java_org_ruizlab_sdslandroid_WaveletProcess_randomFileGenerator(JNIEnv *env, jobject thiz,
                                                                jstring file_storage_path,
                                                                jint file_size) {

    std::string filePath = env->GetStringUTFChars(file_storage_path, NULL);
    int res = random_file_generator(filePath, (int) file_size);
    return res==2;
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_org_ruizlab_sdslandroid_WaveletProcess_waveletEngine(JNIEnv *env, jobject thiz,
                                                          jstring file_storage_path) {
;
    std::string filePath = env->GetStringUTFChars(file_storage_path, NULL);
    int res = wavelet_engine(filePath);
    return res==0;
}