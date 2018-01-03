cmake_minimum_required(VERSION 3.4.1)
set(CMAKE_VERBOSE_MAKEFILE on)


set(LINK_DIR ${CMAKE_CURRENT_LIST_DIR}/../jniLibs/armeabi-v7a)
link_directories(${LINK_DIR})

set(INCLUDE_DIR "/home/hm/ffmpeg/ffmpeg_source/ffmpeg-3.4.1")
#头文件目录设置为ffmpeg-3.4.1源码目录及CMakeLists.txt文件所在目录
#也可以不显式指定CMakeLists.txt文件所在目录，该目录默认包含在搜索目录中
include_directories(${CMAKE_CURRENT_LIST_DIR} ${INCLUDE_DIR})

#指定生成的so包的输出路径，注意此语句要在add_library语句之前，否则不能生效
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${CMAKE_CURRENT_LIST_DIR}/outputlibs)

# 查找当前目录下的所有源文件
# 并将名称保存到 SOURCE_DIR 变量
aux_source_directory(${CMAKE_CURRENT_LIST_DIR} SOURCE_DIR)

add_library(ffmpeg SHARED
            ${SOURCE_DIR})

target_link_libraries(ffmpeg
                      avcodec-57
                      avdevice-57
                      avfilter-6
                      avformat-57
                      avutil-55
                      swresample-2
                      swscale-4
                      log)
