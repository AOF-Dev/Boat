cmake_minimum_required(VERSION 3.4.1)

# build loadme

add_library(loadme SHARED loadme.c)

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -std=c99 -O2 -Wall -Werror")
target_link_libraries(loadme dl log)
