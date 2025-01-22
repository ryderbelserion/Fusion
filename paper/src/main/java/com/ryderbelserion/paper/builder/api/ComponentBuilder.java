package com.ryderbelserion.paper.builder.api;

import com.ryderbelserion.core.api.exception.FusionException;
import com.ryderbelserion.core.util.StringUtils;
import com.ryderbelserion.paper.Fusion;
import com.ryderbelserion.paper.FusionApi;
import com.ryderbelserion.paper.util.PaperMethods;
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

    private final FusionApi api = FusionApi.get();

    private final Fusion fusion = this.api.getFusion();

    private final Map<ResolverType, List<TagResolver>> resolvers = new HashMap<>();

    private final List<String> lines = new ArrayList<>();

    public ComponentBuilder(final String line) {
        this(List.of(line));
    }

    public ComponentBuilder(final List<String> lines) {
        this.lines.addAll(lines);
    }

    public List<Component> asComponents(@Nullable final Audience audience, @NotNull final Map<String, String> placeholders) {
        final List<Component> components = new ArrayList<>(this.lines.size());

        this.lines.forEach(line -> {
            final List<TagResolver> resolvers = this.resolvers.values().stream().flatMap(List::stream).toList();

            components.add(this.fusion.placeholders(audience, line, placeholders, resolvers));
        });

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

    public ComponentBuilder addClickResolver(final String key, final String value, @NotNull final ClickEvent.Action action) {
        return addResolver(ResolverType.CLICK_EVENT, key, value, action);
    }

    public ComponentBuilder addPlaceholderResolver(final String key, final String value) {
        return addResolver(ResolverType.PLACEHOLDER, key, value, null);
    }

    public ComponentBuilder addHoverResolver(final String key, final String value) {
        return addResolver(ResolverType.HOVER_EVENT, key, value, null);
    }

    public ComponentBuilder addColorResolver(final String key, final String value) {
        return addResolver(ResolverType.TEXT_COLOR, key, value, null);
    }

    public ComponentBuilder addResolver(final ResolverType type, final String key, final String value, @Nullable final ClickEvent.Action action) {
        final List<TagResolver> resolvers = this.resolvers.getOrDefault(type, new ArrayList<>());

        switch (type) {
            case PLACEHOLDER -> resolvers.add(Placeholder.parsed(key.replaceAll("\\{", "").replaceAll("}", ""), value));

            case TEXT_COLOR -> {
                final @NotNull Color color = PaperMethods.getColor(value);

                resolvers.add(Placeholder.styling(key, TextColor.color(color.getRed(), color.getGreen(), color.getBlue())));
            }

            case CLICK_EVENT -> {
                if (action == null) {
                    throw new FusionException("The click action cannot be null!");
                }

                resolvers.add(Placeholder.styling(key, ClickEvent.clickEvent(action, value)));
            }

            case HOVER_EVENT -> {
                final @NotNull HoverEvent<Component> event = HoverEvent.showText(StringUtils.parse(value));

                final TagResolver.@NotNull Single placeholder = Placeholder.styling(key, event);

                resolvers.add(placeholder);
            }
        }

        this.resolvers.put(type, resolvers);

        return this;
    }

    public ComponentBuilder addResolver(final ResolverType type, final TagResolver... tags) {
        final List<TagResolver> resolvers = this.resolvers.getOrDefault(type, new ArrayList<>());

        resolvers.addAll(Arrays.asList(tags));

        this.resolvers.put(type, resolvers);

        return this;
    }

    public ComponentBuilder removeResolver(final ResolverType type) {
        this.resolvers.remove(type);

        return this;
    }

    public ComponentBuilder addLine(final String line) {
        this.lines.add(line);

        return this;
    }

    public ComponentBuilder removeLine(final String line) {
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