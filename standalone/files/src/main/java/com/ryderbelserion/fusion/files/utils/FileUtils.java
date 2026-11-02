package com.ryderbelserion.fusion.files.utils;

import com.ryderbelserion.fusion.files.exceptions.FileException;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import java.util.List;

public class FileUtils {

    private static final char LF = '\n';
    private static final char CR = '\r';

    public static List<String> getStringList(final CommentedConfigurationNode node, final List<String> defaultValues) {
        try {
            final List<String> list = node.getList(String.class);

            return list != null ? list : defaultValues;
        } catch (SerializationException exception) {
            throw new FileException(String.format("Failed to serialize %s!", node.path()), exception);
        }
    }

    public static List<String> getStringList(final CommentedConfigurationNode node, final String defaultValue) {
        return getStringList(node, List.of(defaultValue));
    }

    public static List<String> getStringList(final CommentedConfigurationNode node) {
        return getStringList(node, List.of());
    }

    public static List<String> getStringList(final BasicConfigurationNode node, final List<String> defaultValues) {
        try {
            final List<String> list = node.getList(String.class);

            return list != null ? list : defaultValues;
        } catch (SerializationException exception) {
            throw new FileException(String.format("Failed to serialize %s!", node.path()), exception);
        }
    }

    public static String toString(final List<String> list) {
        if (list.isEmpty()) return "";

        final StringBuilder message = new StringBuilder(list.size());

        for (final String line : list) {
            message.append(line).append("\n");
        }

        return chomp(message.toString());
    }

    public static String chomp(final String value) {
        if (value.isEmpty()) {
            return value;
        }

        if (value.length() == 1) {
            final char character = value.charAt(0);

            return character == CR || character == LF ? "" : value;
        }

        int lastIdx = value.length() - 1;

        final char last = value.charAt(lastIdx);

        if (last == LF) {
            if (value.charAt(lastIdx - 1) == CR) {
                lastIdx--;
            }
        } else if (last != CR) {
            lastIdx++;
        }

        return value.substring(0, lastIdx);
    }
}
