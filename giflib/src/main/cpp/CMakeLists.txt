cmake_minimum_required(VERSION 3.4.1)

set(SOURCE)

#file(GLOB_RECURSE SOURCES ${CMAKE_SOURCE_DIR}/*.cpp ${CMAKE_SOURCE_DIR}/*.c)
#file(GLOB SOURCES "${CMAKE_SOURCE_DIR}/*.cpp" )
#file(GLOB SOURCES "${CMAKE_SOURCE_DIR}/*.c" )


message("==============" +${CMAKE_SOURCE_DIR})


#匹配指定目录下c、cpp
file(GLOB SRCS
        ${CMAKE_SOURCE_DIR}/*.cpp
        ${CMAKE_SOURCE_DIR}/*.c
                ${CMAKE_SOURCE_DIR}/giflib/*.cpp
            ${CMAKE_SOURCE_DIR}/giflib/*.c
        )

#file(GLOB my_source
#       giflib/egif_lib.c
#        giflib/gif_err.c
#        giflib/gifalloc.c
#        native-lib.cpp
#        )

add_library(
        native-lib
        SHARED
        giflib/dgif_lib.c
        giflib/openbsd-reallocarray.c
#        giflib/gif_hash.c
#        giflib/egif_lib.c
#        giflib/gif_err.c
#        giflib/gifalloc.c
        ${SRCS}
        #        ${my_source}
)


find_library(
        log-lib
        log)

find_library(
        jnigraphics-lib
        jnigraphics)


target_link_libraries(
        native-lib
        ${jnigraphics-lib}
        ${log-lib})
