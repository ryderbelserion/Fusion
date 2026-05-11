package com.ryderbelserion.fusion.kyori.commands.api.annotations.other;

import com.ryderbelserion.fusion.kyori.commands.api.enums.PermissionMode;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Permission {

    PermissionMode mode() default PermissionMode.OP;

    String permission() default "";

    String description() default "";

}