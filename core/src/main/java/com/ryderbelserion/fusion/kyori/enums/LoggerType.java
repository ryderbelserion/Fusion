package com.ryderbelserion.fusion.kyori.enums;

/**
 * Available logger types when using {@code ILogger#log(@NotNull final LoggerType type, @NotNull final String message, @NotNull final Object... args) }
 */
public enum LoggerType {
    /**
     * Safe logging type!
     */
    SAFE,
    /**
     * Shady logging type!
     */
    WARNING,
    /**
     * WEEWOO WEEWOO
     */
    ERROR
}