package com.ryderbelserion.fusion.kyori.components.builders;

import com.ryderbelserion.fusion.kyori.FusionKyori;
import com.ryderbelserion.fusion.kyori.utils.AdvUtils;
import com.ryderbelserion.fusion.kyori.utils.StringUtils;
import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Builds a component to send to a player.
 */
public class ComponentBuilder {

    private final FusionKyori kyori = (FusionKyori) FusionCore.Provider.get();

    private final Map<ResolverType, List<TagResolver>> resolvers = new EnumMap<>(ResolverType.class);
    private final List<String> lines = new ArrayList<>();

    private final Audience target;

    /**
     * Creates the component builder an audience provided.
     *
     * @param target the audience target
     * @param line   the lines of text for the component
     */
    public ComponentBuilder(@NotNull final Audience target, @NotNull final String line) {
        this(target, new ArrayList<>() {{
            add(line);
        }});
    }

    /**
     * Creates the component builder an audience provided.
     *
     * @param target the audience target
     * @param lines  the text for the component
     */
    public ComponentBuilder(@NotNull final Audience target, @NotNull final List<String> lines) {
        this.lines.addAll(lines);
        this.target = target;
    }

    /**
     * Creates the component builder with no audience provided.
     *
     * @param lines the lines of text for the component
     */
    public ComponentBuilder(@NotNull final List<String> lines) {
        this(Audience.empty(), lines);
    }

    /**
     * Creates the component builder with no audience provided.
     *
     * @param line the text for the component
     */
    public ComponentBuilder(@NotNull final String line) {
        this(Audience.empty(), line);
    }

    /**
     * Returns a list of components with configurable placeholders.
     *
     * @param placeholders a map of placeholders
     * @return {@link Component}
     */
    public @NotNull final List<Component> asComponents(@NotNull final Map<String, String> placeholders) {
        final List<Component> components = new ArrayList<>(this.lines.size());

        final List<TagResolver> resolvers = this.resolvers.values().stream().flatMap(List::stream).toList();

        this.lines.forEach(message -> components.add(this.kyori.color(this.target, message, placeholders, resolvers)));

        return components;
    }

    /**
     * Returns the first component with configurable placeholders.
     *
     * @param placeholders a map of placeholders
     * @return {@link Component}
     */
    public @NotNull final Component asComponent(@NotNull final Map<String, String> placeholders) {
        return asComponents(placeholders).getFirst();
    }

    /**
     * Returns a list of components with no placeholders.
     *
     * @return {@link Component}
     */
    public @NotNull final List<Component> asComponents() {
        return asComponents(new HashMap<>());
    }

    /**
     * Returns the first component with no placeholders
     *
     * @return {@link Component}
     */
    public @NotNull final Component asComponent() {
        return asComponents(new HashMap<>()).getFirst();
    }

    /**
     * Adds a click resolver to the list.
     *
     * @param key    the key
     * @param value  the action on click
     * @param action the click action type
     * @return {@link ComponentBuilder}
     */
    public @NotNull final ComponentBuilder addClickResolver(@NotNull final String key, @NotNull final String value, @NotNull final ClickEvent.Action action) {
        return addResolver(ResolverType.CLICK_EVENT, key, value, action);
    }

    /**
     * Adds a placeholder resolver to the list.
     *
     * @param key   the key
     * @param value the value
     * @return {@link ComponentBuilder}
     */
    public @NotNull final ComponentBuilder addPlaceholderResolver(@NotNull final String key, @NotNull final String value) {
        return addResolver(ResolverType.PLACEHOLDER, key, value, null);
    }

    /**
     * Adds a hover resolver to the list.
     *
     * @param key   the key
     * @param value the hover text
     * @return {@link ComponentBuilder}
     */
    public @NotNull final ComponentBuilder addHoverResolver(@NotNull final String key, @NotNull final String value) {
        return addResolver(ResolverType.HOVER_EVENT, key, value, null);
    }

    /**
     * Adds a text color resolver to the list.
     *
     * @param key the key
     * @param value the color
     * @return {@link ComponentBuilder}
     */
    public @NotNull final ComponentBuilder addColorResolver(@NotNull final String key, @NotNull final String value) {
        return addResolver(ResolverType.TEXT_COLOR, key, value, null);
    }

    /**
     * Adds a resolver to the list with a configurable key/value and optional click event action
     * This method will switch between placeholders, text colors, click events, and hover events.
     *
     * @param type the type of resolver
     * @param key the placeholder or id
     * @param value the value
     * @param action the click action
     * @return {@link ComponentBuilder}
     */
    public @NotNull final ComponentBuilder addResolver(@NotNull final ResolverType type, @NotNull final String key, @NotNull final String value, @Nullable final ClickEvent.Action action) {
        final List<TagResolver> resolvers = this.resolvers.getOrDefault(type, new ArrayList<>());

        switch (type) {
            case PLACEHOLDER -> resolvers.add(Placeholder.parsed(StringUtils.replaceBrackets(key).toLowerCase(), value));

            case TEXT_COLOR -> {
                final TextColor color = TextColor.fromHexString(value);

                if (color != null) {
                    resolvers.add(Placeholder.styling(key, color));
                }
            }

            case CLICK_EVENT -> {
                if (action == null) {
                    throw new FusionException("The click action cannot be null!");
                }

                resolvers.add(Placeholder.styling(key, ClickEvent.clickEvent(action, value)));
            }

            case HOVER_EVENT -> {
                @NotNull final HoverEvent<Component> event = HoverEvent.showText(AdvUtils.parse(value));

                @NotNull final TagResolver.@NotNull Single placeholder = Placeholder.styling(key, event);

                resolvers.add(placeholder);
            }
        }

        this.resolvers.put(type, resolvers);

        return this;
    }

    /**
     * Adds a resolver type to the list.
     *
     * @param type the {@link ResolverType}
     * @return {@link ComponentBuilder}
     */
    public @NotNull final ComponentBuilder addResolver(@NotNull final ResolverType type, @NotNull final TagResolver... tags) {
        final List<TagResolver> resolvers = this.resolvers.getOrDefault(type, new ArrayList<>());

        resolvers.addAll(Arrays.asList(tags));

        this.resolvers.put(type, resolvers);

        return this;
    }

    /**
     * Removes a resolver type from the list.
     *
     * @param type the {@link ResolverType}
     * @return {@link ComponentBuilder}
     */
    public @NotNull final ComponentBuilder removeResolver(@NotNull final ResolverType type) {
        this.resolvers.remove(type);

        return this;
    }

    /**
     * Adds a line to the list.
     *
     * @param line the line to add
     * @return {@link ComponentBuilder}
     */
    public @NotNull final ComponentBuilder addLine(@NotNull final String line) {
        this.lines.add(line);

        return this;
    }

    /**
     * Removes a line from the list.
     *
     * @param line the line to remove
     * @return {@link ComponentBuilder}
     */
    public @NotNull final ComponentBuilder removeLine(@NotNull final String line) {
        this.lines.remove(line);

        return this;
    }

    /**
     * A collection of resolver types
     */
    public enum ResolverType {
        /**
         * Placeholder resolver type
         */
        PLACEHOLDER(),
        /**
         * Click event resolver type
         */
        CLICK_EVENT(),
        /**
         * Hover event resolver type
         */
        HOVER_EVENT(),
        /**
         * Text color resolver type
         */
        TEXT_COLOR(),
        /**
         * Generic resolver type
         */
        GENERIC_TYPE()
    }
}