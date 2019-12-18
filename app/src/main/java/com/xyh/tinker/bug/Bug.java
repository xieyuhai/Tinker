package com.xyh.tinker.bug;

//import android.util.Log;

public class Bug {
    public static void fix() {

        System.out.println("result=" + (1 / 0));

//        Log.e("" + Bug.class.getName(), "dsfsdfsdfsdfsd");

    }


    private int count;

    public void increase() {
        synchronized (this) {
            count++;
        }
    }
}
