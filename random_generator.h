//
// Created by ruizlab on 5/11/24.
//

#ifndef SDSLRUIZ_RANDOM_GENERATOR_H
#define SDSLRUIZ_RANDOM_GENERATOR_H

#include <iostream>
#include <fstream>
#include <random>
#include <sdsl/int_vector.hpp>
#include <android/log.h>

#define LOGI(TAG, ...) __android_log_print(ANDROID_LOG_INFO   , TAG,__VA_ARGS__)

int random_file_generator(std::string string, int i);

#endif //SDSLRUIZ_RANDOM_GENERATOR_H
