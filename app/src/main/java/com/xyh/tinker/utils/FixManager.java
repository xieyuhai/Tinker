package com.xyh.tinker.utils;

import android.content.Context;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashSet;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

public class FixManager {
    private static HashSet<File> loadedDex = new HashSet<>();

    static {
        loadedDex.clear();
    }

    public static void loadDex(Context context) {
        if (context == null) {
            return;
        }

        //加载补丁
//        获取到补丁的目录---odex的文件对象
        File odex = context.getDir("odex", Context.MODE_PRIVATE);
        //获取到odex文件夹下面所有的目录对象

        final File[] files = odex.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().startsWith("classes") || pathname.getName().endsWith(".dex");
            }
        });

//        遍历所有文件对象
        for (File file : files) {
            loadedDex.add(file);
        }


        //创建一个解压目录
        String optimizeDir = odex.getAbsolutePath() + File.separator + "opt_dex";

        //得到解压目录File对象

        File fpot = new File(optimizeDir);
        if (!fpot.exists()) {
            fpot.mkdirs();
        }

        //将补丁注入到系统的dexElements
        for (File dex : loadedDex) {
            try {
//            Class<? extends View> aClass = getClassLoader().loadClass("").asSubclass(View.class);

                //第一步 获取到系统dexElements的成员变量
                PathClassLoader pathClassLoader = (PathClassLoader) context.getClassLoader();
                //2、获取到baseDexClassLoader 的类对象
                Class<?> baseDexClassLoader = Class.forName("dalvik.system.BaseDexClassLoader");
                //3、获取到BaseDexClassLoader 中的 pathList 的成员变量的反射对象
                Field pathListField = baseDexClassLoader.getDeclaredField("pathList");
                //4、打开权限
                pathListField.setAccessible(true);
                //5、获取到pathList 成员变量系统中的值

                Object pathListValue = pathListField.get(pathClassLoader);
                Class<?> systemPathClass = pathListValue.getClass();
                Field dexElementsField = systemPathClass.getDeclaredField("dexElements");
                dexElementsField.setAccessible(true);
                Object systemDexElementsValues = dexElementsField.get(pathListValue);


                //第二步
                DexClassLoader dexClassLoader = new
                        DexClassLoader(dex.getAbsolutePath(), fpot.getAbsolutePath(), null, context.getClassLoader());


                Field myPathListField = baseDexClassLoader.getDeclaredField("pathList");
                myPathListField.setAccessible(true);

                Object myPathListValue = myPathListField.get(dexClassLoader);
                Class<?> myPathListValueClass = myPathListValue.getClass();
                Field myDexElementsField = myPathListValueClass.getDeclaredField("dexElements");
                myDexElementsField.setAccessible(true);
                Object myDexElementsValues = myDexElementsField.get(myPathListValue);


//            第三步
                int length = Array.getLength(systemDexElementsValues);
                int myLength = Array.getLength(myDexElementsValues);

                //创建一个新的长度
                int newElementLength = length + myLength;
                //创建一个新的数组
                Class<?> componentType = systemDexElementsValues.getClass().getComponentType();

                Object newElementArray = Array.newInstance(componentType, newElementLength);

                //将两个数组的内容放进 数组

                for (int x = 0; x < newElementLength; x++) {
                    //首先 ，去补丁数组中拿
                    if (x < myLength) {
                        Array.set(newElementArray, x, Array.get(myDexElementsValues, x));
                    } else {
                        Array.set(newElementArray, x, Array.get(systemDexElementsValues, x - myLength));
                    }
                }

                // 将合并好的新数组，赋值到pathlist中
//            myDexElementsField.set(pathListValue, newElementArray);
                dexElementsField.set(pathListValue, newElementArray);

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


//    /**
//     * 加载补丁包
//     *
//     * @param dexPath
//     */
//    public void load(String dexPath) {
//        try {
////            Class<? extends View> aClass = getClassLoader().loadClass("").asSubclass(View.class);
//
//
//            PathClassLoader pathClassLoader = (PathClassLoader) getClassLoader();
//            Class<?> BaseDexClassLoader = Class.forName("dalvik.system.BaseDexClassLoader");
//            Field pathListField = BaseDexClassLoader.getDeclaredField("pathList");
//            pathListField.setAccessible(true);
//
//            Object pathListValue = pathListField.get(pathClassLoader);
//            Class<?> systemPathClass = pathListValue.getClass();
//            Field dexElementsField = systemPathClass.getDeclaredField("dexElements");
//            dexElementsField.setAccessible(true);
//            Object systemDexElementsValues = dexElementsField.get(pathListValue);
//
//
//            //第二部
//            DexClassLoader dexClassLoader = new
//                    DexClassLoader(dexPath, dexPath, null, getClassLoader());
//
//
//            Field myPathListField = dexClassLoader.getClass().getDeclaredField("pathList");
//            myPathListField.setAccessible(true);
//
//            Object myPathListValue = pathListField.get(pathClassLoader);
//            Class<?> myPathListValueClass = myPathListValue.getClass();
//            Field myDexElementsField = myPathListValueClass.getDeclaredField("dexElements");
//
//            Object myDexElementsValues = myDexElementsField.get(pathListValue);
//
//
////            第三部
//            int length = Array.getLength(systemDexElementsValues);
//            int myLength = Array.getLength(myDexElementsValues);
//
//            //创建一个新的长度
//            int newElementLength = length + myLength;
//            //创建一个新的数组
//            Class<?> componentType = systemDexElementsValues.getClass().getComponentType();
//
//            Object newElementArray = Array.newInstance(componentType, newElementLength);
//
//            //将两个数组的内容放进 数组
//
//            for (int x = 0; x < newElementLength; x++) {
//                //首先 ，去补丁数组中拿
//                if (x < myLength) {
//                    Array.set(newElementArray, x, Array.get(myDexElementsValues, x));
//                } else {
//                    Array.set(newElementArray, x, Array.get(systemDexElementsValues, x - myLength));
//                }
//            }
//
//            // 将合并好的新数组，赋值到pathlist中
////            myDexElementsField.set(pathListValue, newElementArray);
//            dexElementsField.set(pathListValue, newElementArray);
//
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }


}
