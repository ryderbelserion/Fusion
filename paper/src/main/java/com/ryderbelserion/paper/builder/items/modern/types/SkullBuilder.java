package com.ryderbelserion.paper.builder.items.modern.types;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.ryderbelserion.core.api.exception.FusionException;
import com.ryderbelserion.paper.builder.items.modern.BaseItemBuilder;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.profile.PlayerTextures;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.UUID;

public class SkullBuilder extends BaseItemBuilder<SkullBuilder> {

    private final ResolvableProfile.Builder builder;

    public SkullBuilder(@NotNull final ItemStack itemStack) {
        super(itemStack);

        this.builder = ResolvableProfile.resolvableProfile();
    }

    public SkullBuilder withAudience(@NotNull final Audience audience) {
        final UUID uuid = audience.getOrDefault(Identity.UUID, null);

        if (uuid == null) return this;

        this.builder.uuid(uuid);

        return this;
    }

    public SkullBuilder withUrl(@NotNull final String url) {
        if (url.isEmpty()) return this;

        final String newUrl = "https://textures.minecraft.net/texture/" + url;

        final PlayerProfile profile = this.plugin.getServer().createProfile(UUID.randomUUID(), null);

        final PlayerTextures textures = profile.getTextures();

        try {
            textures.setSkin(URI.create(newUrl).toURL());
        } catch (MalformedURLException exception) {
            throw new FusionException("Skull URL is malformed", exception);
        }

        this.builder.addProperties(profile.getProperties());

        return this;
    }

    public SkullBuilder withBase64(@NotNull final String base64) {
        if (base64.isEmpty()) return this;

        this.builder.addProperty(new ProfileProperty("textures", base64));

        return this;
    }

    public SkullBuilder withName(@Subst("player") @NotNull final String playerName) {
        if (playerName.isEmpty()) return this;

        if (playerName.length() > 16) {
            return withUrl(playerName);
        }

        this.builder.name(playerName);

        return this;
    }

    @Override
    public SkullBuilder build() {
        getItem().setData(DataComponentTypes.PROFILE, this.builder.build());

        return this;
    }
}