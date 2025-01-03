package com.ryderbelserion.core.commands.annotation;

import org.jetbrains.annotations.NotNull;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Inherited
public @interface Command {

    @NotNull String value() default "fusion-default";

}