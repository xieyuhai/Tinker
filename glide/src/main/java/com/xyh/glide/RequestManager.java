package com.xyh.glide;

import java.util.concurrent.LinkedBlockingQueue;

public class RequestManager {


    private static final class SingleTon {
        private static final RequestManager INSTANCE = new RequestManager();
    }

    private RequestManager() {
        start();
    }

    public static RequestManager getInstance() {
        return SingleTon.INSTANCE;
    }

    public void start() {
        stop();
        startAllDispatchers();
    }


    private LinkedBlockingQueue requestQueue = new LinkedBlockingQueue();

    public void addBitmapRequest(BitmapRequest bitmapRequest) {
        if (bitmapRequest != null) {
            if (!requestQueue.contains(bitmapRequest)) {
                requestQueue.add(bitmapRequest);
            }
        }
    }

    private void stop() {
        if (bitmapDispatchers != null && bitmapDispatchers.length > 0) {
            for (BitmapDispatcher bitmapDispatcher : bitmapDispatchers) {
                if (!bitmapDispatcher.isInterrupted()) {
                    bitmapDispatcher.interrupt();
                }
            }
        }
    }

    private BitmapDispatcher[] bitmapDispatchers;

    private void startAllDispatchers() {
        //
        int processorCount = Runtime.getRuntime().availableProcessors();
        bitmapDispatchers = new BitmapDispatcher[processorCount];

        for (int i = 0; i < processorCount; i++) {
            BitmapDispatcher bitmapDispatcher = new BitmapDispatcher(requestQueue);
            bitmapDispatcher.start();
            bitmapDispatchers[i] = bitmapDispatcher;
        }
    }
}
