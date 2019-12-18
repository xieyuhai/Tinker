package com.xyh.arouter;

import com.xyh.arouter.ARouter;
import com.xyh.arouter.IRouter;

public class ActivityUtils implements IRouter {
    @Override
    public void putActivity() {
        ARouter.getInstance().navigation("call/main", null);
    }
}
