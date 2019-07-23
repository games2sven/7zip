
cmake_minimum_required(VERSION 3.4.1)


add_library(
             native-lib
             SHARED
             src/main/cpp/native-lib.cpp )

#设置头文件查找目录
include_directories(
    src/main/cpp/lib7zr/C
    src/main/cpp/lib7zr/CPP
)

#设置库查找目录
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}/src/main/jniLibs/${CMAKE_ANDROID_ARCH_ABI}")

target_link_libraries(
                       native-lib
                       7zr
                       log )