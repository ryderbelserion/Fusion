package com.ryderbelserion.fusion.kyori.enums;

/**
 * Available logger types when using {@code ILogger#log(@NotNull LoggerType type, @NotNull String message, @NotNull Object... args) }
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