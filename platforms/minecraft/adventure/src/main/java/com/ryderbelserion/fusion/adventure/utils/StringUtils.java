package com.ryderbelserion.fusion.adventure.utils;

import com.ryderbelserion.fusion.adventure.FusionAdventure;
import com.ryderbelserion.fusion.core.FusionCore;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class StringUtils {

    private static final FusionAdventure api = (FusionAdventure) FusionCore.Provider.get();

    public static String toString(@NotNull final List<String> list) {
        if (list.isEmpty()) return "";

        final StringBuilder message = new StringBuilder();

        for (final String line : list) {
            message.append(line).append("\n");
        }

        return api.chomp(message.toString());
    }

    public static String replaceBrackets(@NotNull final String input) {
        return input.replaceAll("\\{", "<").replaceAll("}", ">").replaceAll("<", "").replaceAll(">", "");
    }
}