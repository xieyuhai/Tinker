//
// Created by 谢玉海 on 2020-01-05.
//
#include "giflib/gif_lib.h"
#include <android/bitmap.h>


#ifndef TINKER_GIF_H
#define TINKER_GIF_H

#endif //TINKER_GIF_H


extern "C"
int drawFrame(GifFileType *gif, AndroidBitmapInfo *info,int *pixels,int frame_no,bool force_dispose);
