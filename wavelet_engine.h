//
// Created by ruizlab on 5/14/24.
//

#ifndef SDSLRUIZ_WAVELET_ENGINE_H
#define SDSLRUIZ_WAVELET_ENGINE_H

#include <iostream>
#include <sdsl/bit_vectors.hpp>
#include <sdsl/wt_huff.hpp>
#include <sdsl/wavelet_trees.hpp>
#include <android/log.h>

#define LOGI(TAG, ...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)

int wavelet_engine(std::string testType, std::string filePath);

#endif //SDSLRUIZ_WAVELET_ENGINE_H

