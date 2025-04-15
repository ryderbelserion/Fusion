package com.ryderbelserion.fusion.paper.api.builder;

import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.utils.AdvUtils;
import com.ryderbelserion.fusion.paper.utils.ColorUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComponentBuilder {

    private final FusionCore api = FusionCore.Provider.get();

    private final Map<ResolverType, List<TagResolver>> resolvers = new HashMap<>();

    private final List<String> lines = new ArrayList<>();

    public ComponentBuilder(@NotNull final String line) {
        this(List.of(line));
    }

    public ComponentBuilder(@NotNull final List<String> lines) {
        lines.forEach(line -> this.lines.add(line.replaceAll("\\{", "<").replaceAll("}", ">")));
    }

    public List<Component> asComponents(@Nullable final Audience audience, @NotNull final Map<String, String> placeholders) {
        final List<Component> components = new ArrayList<>(this.lines.size());

        final List<TagResolver> resolvers = this.resolvers.values().stream().flatMap(List::stream).toList();

        this.lines.forEach(line -> components.add(this.api.placeholders(audience, line, placeholders, resolvers)));

        return components;
    }

    public Component asComponent(@Nullable final Audience audience, @NotNull final Map<String, String> placeholders) {
        return asComponents(audience, placeholders).getFirst();
    }

    public Component asComponent(@Nullable final Audience audience) {
        return asComponents(audience, new HashMap<>()).getFirst();
    }

    public List<Component> asComponents(@NotNull final Map<String, String> placeholders) {
        return asComponents(null, placeholders);
    }

    public List<Component> asComponents() {
        return asComponents(null, new HashMap<>());
    }

    public Component asComponent() {
        return asComponent(null);
    }

    public ComponentBuilder addClickResolver(@NotNull final String key, @NotNull final String value, @NotNull final ClickEvent.Action action) {
        return addResolver(ResolverType.CLICK_EVENT, key, value, action);
    }

    public ComponentBuilder addPlaceholderResolver(@NotNull final String key, @NotNull final String value) {
        return addResolver(ResolverType.PLACEHOLDER, key, value, null);
    }

    public ComponentBuilder addHoverResolver(@NotNull final String key, @NotNull final String value) {
        return addResolver(ResolverType.HOVER_EVENT, key, value, null);
    }

    public ComponentBuilder addColorResolver(@NotNull final String key, @NotNull final String value) {
        return addResolver(ResolverType.TEXT_COLOR, key, value, null);
    }

    public ComponentBuilder addResolver(@NotNull final ResolverType type, @NotNull final String key, @NotNull final String value, @Nullable final ClickEvent.Action action) {
        final List<TagResolver> resolvers = this.resolvers.getOrDefault(type, new ArrayList<>());

        switch (type) {
            case PLACEHOLDER -> resolvers.add(Placeholder.parsed(key.replaceAll("\\{", "").replaceAll("}", "").replaceAll("<", "").replaceAll(">", ""), value));

            case TEXT_COLOR -> {
                @NotNull final Color color = ColorUtils.getColor(value);

                resolvers.add(Placeholder.styling(key, TextColor.color(color.getRed(), color.getGreen(), color.getBlue())));
            }

            case CLICK_EVENT -> {
                if (action == null) {
                    throw new FusionException("The click action cannot be null!");
                }

                resolvers.add(Placeholder.styling(key, ClickEvent.clickEvent(action, value)));
            }

            case HOVER_EVENT -> {
                @NotNull final HoverEvent<Component> event = HoverEvent.showText(AdvUtils.parse(value));

                final TagResolver.@NotNull Single placeholder = Placeholder.styling(key, event);

                resolvers.add(placeholder);
            }
        }

        this.resolvers.put(type, resolvers);

        return this;
    }

    public ComponentBuilder addResolver(@NotNull final ResolverType type, @NotNull final TagResolver... tags) {
        final List<TagResolver> resolvers = this.resolvers.getOrDefault(type, new ArrayList<>());

        resolvers.addAll(Arrays.asList(tags));

        this.resolvers.put(type, resolvers);

        return this;
    }

    public ComponentBuilder removeResolver(@NotNull final ResolverType type) {
        this.resolvers.remove(type);

        return this;
    }

    public ComponentBuilder addLine(@NotNull final String line) {
        this.lines.add(line);

        return this;
    }

    public ComponentBuilder removeLine(@NotNull final String line) {
        this.lines.remove(line);

        return this;
    }

    public enum ResolverType {
        PLACEHOLDER(),
        CLICK_EVENT(),
        HOVER_EVENT(),
        TEXT_COLOR(),

        GENERIC_TYPE()
    }
}