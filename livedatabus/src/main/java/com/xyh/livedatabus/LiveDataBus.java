package com.xyh.livedatabus;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class LiveDataBus {

    private static final LiveDataBus liveDataBus = new LiveDataBus();

    private LiveDataBus() {
        bus = new HashMap<>();
    }

    public static LiveDataBus getInstance() {
        return liveDataBus;
    }

    private Map<String, BusMutableLiveData<Object>> bus;

    public synchronized <DATA> BusMutableLiveData<DATA> with(String key, Class<DATA> type) {
        if (!bus.containsKey(key)) {
            bus.put(key, new BusMutableLiveData<Object>());
        }
        return (BusMutableLiveData<DATA>) bus.get(key);
    }


    public static class BusMutableLiveData<DATA> extends MutableLiveData<DATA> {
        @Override
        public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<DATA> observer) {
            super.observe(owner, observer);


            hook(observer);

        }

        private void hook(Observer<DATA> observer) {
            //获取LiveData 的 mVersion
            Class<LiveData> liveDataClass = LiveData.class;
            try {
                Field mVersionField = liveDataClass.getDeclaredField("mVersion");
                mVersionField.setAccessible(true);
                Object mVersion = mVersionField.get(this);

                //获取到ObverserWraper 的 mLastVersion
                Field mObserversField = liveDataClass.getDeclaredField("mObservers");
                mObserversField.setAccessible(true);

                // 获取 observers 实例对象
                Object observers = mObserversField.get(this);

                Method method = observers.getClass().getDeclaredMethod("get", Object.class);
                method.setAccessible(true);

                Object invokeEntry = method.invoke(observers, observer);

                Object observerWrapper = null;
                if (invokeEntry != null && invokeEntry instanceof Map.Entry) {
                    observerWrapper = ((Map.Entry) invokeEntry).getValue();
                }

                if (observerWrapper == null) {
                    throw new NullPointerException(" observerWrapper cannot is null");
                }

                //实现类的 父类 
                Field mLastVersionField = observerWrapper.getClass().getSuperclass().getDeclaredField("mLastVersion");
                mLastVersionField.setAccessible(true);

                //将mVersion 赋值给mLastVersion
                mLastVersionField.set(observerWrapper, mVersion);


            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
