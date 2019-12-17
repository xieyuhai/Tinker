package com.xyh.tinker.bug;

//import android.util.Log;

public class Bug {
    public static void fix() {

        System.out.println("result=" + (1 / 0));

//        Log.e("" + Bug.class.getName(), "dsfsdfsdfsdfsd");

    }
}
