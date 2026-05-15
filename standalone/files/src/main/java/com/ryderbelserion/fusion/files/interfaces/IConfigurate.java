package com.ryderbelserion.fusion.files.interfaces;

import org.jspecify.annotations.NonNull;

import java.util.List;

public interface IConfigurate {

    /**
     * Retrieves a string value from the configuration with a specified default.
     *
     * @param defaultValue the default value to return if the key is not found
     * @param path         the path to the configuration key
     * @return the string value or the default if missing
     */
    default @NonNull String getStringValueWithDefault(@NonNull final String defaultValue, @NonNull final Object... path) {
        return "";
    }

    /**
     * Retrieves a string value from the configuration.
     *
     * @param path the path to the configuration key
     * @return the string value or an empty string if missing
     */
    default @NonNull String getStringValue(@NonNull final Object... path) {
        return getStringValueWithDefault("", path);
    }

    /**
     * Retrieves a boolean value from the configuration with a specified default.
     *
     * @param defaultValue the default value to return if the key is not found
     * @param path         the path to the configuration key
     * @return the boolean value or the default if missing
     */
    default boolean getBooleanValueWithDefault(final boolean defaultValue, @NonNull final Object... path) {
        return false;
    }

    /**
     * Retrieves a boolean value from the configuration.
     *
     * @param path the path to the configuration key
     * @return the boolean value or false if missing
     */
    default boolean getBooleanValue(@NonNull final Object... path) {
        return getBooleanValueWithDefault(false, path);
    }

    /**
     * Retrieves a double value from the configuration with a specified default.
     *
     * @param defaultValue the default value to return if the key is not found
     * @param path         the path to the configuration key
     * @return the double value or the default if missing
     */
    default double getDoubleValueWithDefault(final double defaultValue, @NonNull final Object... path) {
        return -1.0;
    }

    /**
     * Retrieves a double value from the configuration.
     *
     * @param path the path to the configuration key
     * @return the double value or 0.0 if missing
     */
    default double getDoubleValue(@NonNull final Object... path) {
        return getDoubleValueWithDefault(0.0, path);
    }

    /**
     * Retrieves a long value from the configuration with a specified default.
     *
     * @param defaultValue the default value to return if the key is not found
     * @param path         the path to the configuration key
     * @return the long value or the default if missing
     */
    default long getLongValueWithDefault(final long defaultValue, @NonNull final Object... path) {
        return -1L;
    }

    /**
     * Retrieves a long value from the configuration.
     *
     * @param path the path to the configuration key
     * @return the long value or 0L if missing
     */
    default long getLongValue(@NonNull final Object... path) {
        return getLongValueWithDefault(0L, path);
    }

    /**
     * Retrieves an integer value from the configuration with a specified default.
     *
     * @param defaultValue the default value to return if the key is not found
     * @param path         the path to the configuration key
     * @return the integer value or the default if missing
     */
    default int getIntValueWithDefault(final int defaultValue, @NonNull final Object... path) {
        return -1;
    }

    /**
     * Retrieves an integer value from the configuration.
     *
     * @param path the path to the configuration key
     * @return the integer value or 0 if missing
     */
    default int getIntValue(@NonNull final Object... path) {
        return getIntValueWithDefault(0, path);
    }

    /**
     * Retrieves a list of string values from the configuration.
     *
     * @param defaultValue the default value to return if the key is not found
     * @param path the path to the configuration key
     * @return the list of string values or an empty list if missing
     */
    default @NonNull List<String> getStringList(@NonNull final List<String> defaultValue, @NonNull final Object... path) {
        return List.of();
    }

    /**
     * Retrieves a list of string values from the configuration.
     *
     * @param path the path to the configuration key
     * @return the list of string values or an empty list if missing
     */
    default @NonNull List<String> getStringList(@NonNull final Object... path) {
        return getStringList(List.of(), path);
    }
}