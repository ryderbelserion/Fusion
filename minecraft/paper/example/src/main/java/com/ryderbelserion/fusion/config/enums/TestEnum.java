package com.ryderbelserion.fusion.config.enums;

import org.jspecify.annotations.NullMarked;

@NullMarked
public enum TestEnum {

    example_one("example_one"),
    example_two("example_two"),
    example_three("example_three");

    private final String name;

    TestEnum(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}