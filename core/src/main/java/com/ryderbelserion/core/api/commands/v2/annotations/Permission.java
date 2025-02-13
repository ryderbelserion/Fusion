package com.ryderbelserion.core.api.commands.v2.annotations;

import com.ryderbelserion.core.api.commands.v2.enums.PermissionMode;
import org.jetbrains.annotations.NotNull;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
public @interface Permission {

    @NotNull String value();

    @NotNull PermissionMode mode() default PermissionMode.OP;

    @NotNull String description() default "";

}