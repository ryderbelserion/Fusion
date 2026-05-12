package com.ryderbelserion.fusion.kyori.commands.api.objects.meta;

import com.ryderbelserion.fusion.core.api.FusionKey;
import com.ryderbelserion.fusion.core.api.FusionProvider;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import org.jetbrains.annotations.NotNull;
import java.util.Locale;

public class LocaleMeta {

    private final FusionKyori fusion = (FusionKyori) FusionProvider.getInstance();

    private final String namespace = this.fusion.getNamespace();

    private FusionKey locale = FusionKey.key(this.namespace, "default");

    public LocaleMeta(@NotNull final Audience audience) {
        audience.get(Identity.LOCALE).ifPresent(this::setLocale);
    }

    public void setLocale(@NotNull final Locale locale) {
        final String target = locale.toString();

        final String[] splitter = target.contains("-") ? target.split("-") : target.split("_");

        final String language = splitter[0];
        final String country = splitter[1];

        final String value = "%s_%s".formatted(language, country).toLowerCase();

        if (value.equalsIgnoreCase("en_us")) {
            return;
        }

        this.locale = FusionKey.key(this.namespace, value);
    }

    public @NotNull final FusionKey getLocale() {
        return this.locale;
    }
}