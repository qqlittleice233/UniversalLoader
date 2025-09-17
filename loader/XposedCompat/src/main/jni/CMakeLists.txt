cmake_minimum_required(VERSION 3.18.1)
project(xposed-util)

set(CMAKE_CXX_STANDARD 20)

add_library(xposed-util SHARED library.cpp)
