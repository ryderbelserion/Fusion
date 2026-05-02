package com.ryderbelserion.fusion.commands.annotations.subs;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface Leaf { // tree i.e. /fusion help

    String value() default "";

    int weight() default 0;

}