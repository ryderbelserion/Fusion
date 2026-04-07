package com.ryderbelserion.fusion.files.interfaces;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface IConfigurate {

    /**
     * Retrieves a string value from the configuration with a specified default.
     *
     * @param defaultValue the default value to return if the key is not found
     * @param path         the path to the configuration key
     * @return the string value or the default if missing
     */
    default @NotNull String getStringValueWithDefault(@NotNull final String defaultValue, @NotNull final Object... path) {
        return "";
    }

    /**
     * Retrieves a string value from the configuration.
     *
     * @param path the path to the configuration key
     * @return the string value or an empty string if missing
     */
    default @NotNull String getStringValue(@NotNull final Object... path) {
        return getStringValueWithDefault("", path);
    }

    /**
     * Retrieves a boolean value from the configuration with a specified default.
     *
     * @param defaultValue the default value to return if the key is not found
     * @param path         the path to the configuration key
     * @return the boolean value or the default if missing
     */
    default boolean getBooleanValueWithDefault(final boolean defaultValue, @NotNull final Object... path) {
        return false;
    }

    /**
     * Retrieves a boolean value from the configuration.
     *
     * @param path the path to the configuration key
     * @return the boolean value or false if missing
     */
    default boolean getBooleanValue(@NotNull final Object... path) {
        return getBooleanValueWithDefault(false, path);
    }

    /**
     * Retrieves a double value from the configuration with a specified default.
     *
     * @param defaultValue the default value to return if the key is not found
     * @param path         the path to the configuration key
     * @return the double value or the default if missing
     */
    default double getDoubleValueWithDefault(final double defaultValue, @NotNull final Object... path) {
        return -1.0;
    }

    /**
     * Retrieves a double value from the configuration.
     *
     * @param path the path to the configuration key
     * @return the double value or 0.0 if missing
     */
    default double getDoubleValue(@NotNull final Object... path) {
        return getDoubleValueWithDefault(0.0, path);
    }

    /**
     * Retrieves a long value from the configuration with a specified default.
     *
     * @param defaultValue the default value to return if the key is not found
     * @param path         the path to the configuration key
     * @return the long value or the default if missing
     */
    default long getLongValueWithDefault(final long defaultValue, @NotNull final Object... path) {
        return -1L;
    }

    /**
     * Retrieves a long value from the configuration.
     *
     * @param path the path to the configuration key
     * @return the long value or 0L if missing
     */
    default long getLongValue(@NotNull final Object... path) {
        return getLongValueWithDefault(0L, path);
    }

    /**
     * Retrieves an integer value from the configuration with a specified default.
     *
     * @param defaultValue the default value to return if the key is not found
     * @param path         the path to the configuration key
     * @return the integer value or the default if missing
     */
    default int getIntValueWithDefault(final int defaultValue, @NotNull final Object... path) {
        return -1;
    }

    /**
     * Retrieves an integer value from the configuration.
     *
     * @param path the path to the configuration key
     * @return the integer value or 0 if missing
     */
    default int getIntValue(@NotNull final Object... path) {
        return getIntValueWithDefault(0, path);
    }

    /**
     * Retrieves a list of string values from the configuration.
     *
     * @param defaultValue the default value to return if the key is not found
     * @param path the path to the configuration key
     * @return the list of string values or an empty list if missing
     */
    default @NotNull List<String> getStringList(@NotNull final List<String> defaultValue, @NotNull final Object... path) {
        return List.of();
    }

    /**
     * Retrieves a list of string values from the configuration.
     *
     * @param path the path to the configuration key
     * @return the list of string values or an empty list if missing
     */
    default @NotNull List<String> getStringList(@NotNull final Object... path) {
        return getStringList(List.of(), path);
    }
}