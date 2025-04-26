#include <iostream>
#include <vector>
#include <cmath>
#include <iomanip>
#include <algorithm>

// Naive softmax implementation
std::vector<float> naive_softmax(const std::vector<float>& input) {
    std::vector<float> output(input.size());
    
    // Calculate sum of exponentials
    float sum_exp = 0.0f;
    for (float val : input) {
        sum_exp += std::exp(val);
    }
    
    // Calculate softmax values
    for (size_t i = 0; i < input.size(); ++i) {
        output[i] = std::exp(input[i]) / sum_exp;
    }
    
    return output;
}

// Safe softmax implementation (with max subtraction for numerical stability)
std::vector<float> safe_softmax(const std::vector<float>& input) {
    std::vector<float> output(input.size());
    
    // Find maximum value for numerical stability
    float max_val = *std::max_element(input.begin(), input.end());
    
    // Calculate sum of exponentials
    float sum_exp = 0.0f;
    for (float val : input) {
        sum_exp += std::exp(val - max_val);
    }
    
    // Calculate softmax values
    for (size_t i = 0; i < input.size(); ++i) {
        output[i] = std::exp(input[i] - max_val) / sum_exp;
    }
    
    return output;
}

// Safe softmax with online normalizer calculation
std::vector<float> online_softmax(const std::vector<float>& src) {
    std::vector<float> output(src.size());
    float max_value = -99999999.f;
    float sum = 0.0f;
    float pre_value = 0.0f;
    for (float val : src) {
        max_value = std::max(max_value, val);
        sum += sum * std::exp(pre_value - max_value) + std::exp(val - max_value);
        pre_value = max_value;
    }
    
    // Calculate softmax values
    for (size_t i = 0; i < src.size(); ++i) {
        output[i] = std::exp(src[i] - max_value) / sum;
    }
    
    return output;
}

// Print vector with fixed precision
void print_vector(const std::vector<float>& vec, const std::string& name) {
    std::cout << name << ": [";
    for (size_t i = 0; i < vec.size(); ++i) {
        std::cout << std::fixed << std::setprecision(6) << vec[i];
        if (i < vec.size() - 1) {
            std::cout << ", ";
        }
    }
    std::cout << "]" << std::endl;
}

int main() {
    // Example 1: Small values
    std::vector<float> input1 = {1.0f, 2.0f, 3.0f};
    std::cout << "Example 1 - Small values:" << std::endl;
    print_vector(input1, "Input");
    print_vector(naive_softmax(input1), "Naive Softmax");
    print_vector(safe_softmax(input1), "Safe Softmax");
    print_vector(online_softmax(input1), "Online Softmax");
    std::cout << std::endl;
    
    // Example 2: Large values (to demonstrate numerical instability)
    std::vector<float> input2 = {100.0f, 101.0f, 102.0f};
    std::cout << "Example 2 - Large values:" << std::endl;
    print_vector(input2, "Input");
    print_vector(naive_softmax(input2), "Naive Softmax");
    print_vector(safe_softmax(input2), "Safe Softmax");
    print_vector(online_softmax(input2), "Online Softmax");
    std::cout << std::endl;
    
    // Example 3: Mixed values
    std::vector<float> input3 = {-5.0f, 0.0f, 5.0f};
    std::cout << "Example 3 - Mixed values:" << std::endl;
    print_vector(input3, "Input");
    print_vector(naive_softmax(input3), "Naive Softmax");
    print_vector(safe_softmax(input3), "Safe Softmax");
    print_vector(online_softmax(input3), "Online Softmax");
    
    return 0;
}
