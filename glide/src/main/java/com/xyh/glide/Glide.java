package com.xyh.glide;

import android.content.Context;

public class Glide {


    private Glide() {
    }

    public static BitmapRequest get(Context context) {
        return new BitmapRequest(context);
    }
}
