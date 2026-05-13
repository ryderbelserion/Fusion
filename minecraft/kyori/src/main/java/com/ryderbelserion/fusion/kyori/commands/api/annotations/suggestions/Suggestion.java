package com.ryderbelserion.fusion.kyori.commands.api.annotations.suggestions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
public @interface Suggestion {

    Class<?> type();

    String name();

}