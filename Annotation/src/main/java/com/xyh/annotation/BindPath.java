package com.xyh.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) // 类注解
@Retention(RetentionPolicy.CLASS) //设置编译注解
public @interface BindPath {
    String value();
}
