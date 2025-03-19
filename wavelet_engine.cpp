//
// Created by ruizlab on 5/14/24.
//

#include "wavelet_engine.h"
#include <iostream>
#include <sdsl/int_vector.hpp>
#include <sdsl/wavelet_trees.hpp>
#include <sdsl/io.hpp>
#include <android/log.h>

// Logging macro for Android
#define LOG_TAG "WaveletEngine"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

int wavelet_engine(std::string testType, std::string filePath) {
    try {
        if (testType == "wavelet_tree") {
            // Wavelet Tree Test
            std::string data_file = filePath + "/random_data.bin";
            const size_t blockSize = 1 * 1024 * 1024; // Block size: 1MB

            // Load data for wavelet tree construction
            sdsl::int_vector<8> data;
            sdsl::load_from_file(data, data_file);
            LOGI("Loaded data from %s for wavelet tree test", data_file.c_str());

            // Construct and test wavelet tree
            sdsl::wt_huff<sdsl::bit_vector, sdsl::rank_support_v<>> wt;
            sdsl::construct_im(wt, data);

            size_t pos = 10;
            if (pos < wt.size()) {
                LOGI("Element at position %zu is %d", pos, int(wt[pos]));
            }

            // Store wavelet tree
            std::string wt_file = filePath + "/wt_huff.sdsl";
            int huffStoreSuccess = sdsl::store_to_file(wt, wt_file);
            LOGI("Wavelet tree stored at %s", wt_file.c_str());

            return huffStoreSuccess;
        } else if (testType == "bit_vector_generator") {
            // Bit Vector Generator Test
            std::string id = "test_id";  // Placeholder, update with dynamic input if needed
            std::string output_file = filePath + "/bit_vector_output.sdsl";

            // Create and populate bit vector
            sdsl::int_vector<8> bit_vector(100, 0); // Example size: 100
            for (size_t i = 0; i < bit_vector.size(); ++i) {
                bit_vector[i] = i % 256; // Populate with example data
            }
            LOGI("Generating bit vector for ID: %s", id.c_str());

            // Store bit vector
            sdsl::store_to_file(bit_vector, output_file);
            LOGI("Bit vector stored at %s", output_file.c_str());

            return 0;
        } else {
            LOGI("Unknown test type: %s", testType.c_str());
            return -1;
        }
    } catch (const std::exception& e) {
        LOGI("Exception in wavelet_engine: %s", e.what());
        return -1;
    } catch (...) {
        LOGI("Unknown exception occurred in wavelet_engine");
        return -1;
    }
}
