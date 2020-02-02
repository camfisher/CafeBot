cmake_minimum_required (VERSION 3.6)
project(CafeBot)

add_executable(CafeBot Main.cpp)

add_subdirectory(sleepy-discord)
target_link_libraries(CafeBot sleepy-discord)
