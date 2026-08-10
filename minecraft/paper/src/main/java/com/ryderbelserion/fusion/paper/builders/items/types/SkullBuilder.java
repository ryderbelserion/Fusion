package com.ryderbelserion.fusion.paper.builders.items.types;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.ryderbelserion.fusion.core.api.enums.Level;
import com.ryderbelserion.fusion.api.exceptions.FusionException;
import com.ryderbelserion.fusion.paper.builders.items.BaseItemBuilder;
import com.ryderbelserion.fusion.paper.utils.ItemUtils;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.inventory.ItemStack;
import org.bukkit.profile.PlayerTextures;
import org.jspecify.annotations.NullMarked;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.UUID;

@NullMarked
public final class SkullBuilder extends BaseItemBuilder<SkullBuilder> {

    private final ResolvableProfile.Builder builder;

    public SkullBuilder(final ItemStack itemStack) {
        super(itemStack);

        this.builder = ResolvableProfile.resolvableProfile();
    }

    public SkullBuilder withAudience(final Audience audience) {
        final UUID uuid = audience.getOrDefault(Identity.UUID, null);

        if (uuid == null) return this;

        this.builder.uuid(uuid);

        return this;
    }

    public SkullBuilder withUrl(final String url) {
        if (url.isEmpty()) return this;

        final String newUrl = url.startsWith("https://textures.minecraft.net/texture/") ? url : "https://textures.minecraft.net/texture/" + url;

        final PlayerProfile profile = this.fusion.createProfile(UUID.randomUUID(), null);

        profile.setProperty(new ProfileProperty("", ""));

        final PlayerTextures textures = profile.getTextures();

        try {
            textures.setSkin(URI.create(newUrl).toURL(), PlayerTextures.SkinModel.CLASSIC);
        } catch (final MalformedURLException exception) {
            throw new FusionException("Skull URL is malformed!", exception);
        }

        profile.setTextures(textures);

        this.builder.addProperties(profile.getProperties());

        return this;
    }

    @Override
    public SkullBuilder withBase64(final String base64) {
        if (base64.isEmpty()) return this;

        this.builder.addProperty(new ProfileProperty("textures", base64));

        return this;
    }

    public SkullBuilder withName(final String playerName) {
        if (playerName.isEmpty()) return this;

        if (playerName.length() > 16) return withUrl(playerName);

        this.builder.name(playerName);

        return this;
    }

    public SkullBuilder withNoteBlockSound(final String sound) {
        ItemUtils.getSound(sound).ifPresent(value -> {
            final NamespacedKey key = Registry.SOUNDS.getKey(value);

            if (key == null) {
                this.fusion.log(Level.WARNING, "No valid NamespacedKey found for %s", sound);

                return;
            }

            this.itemStack.setData(DataComponentTypes.NOTE_BLOCK_SOUND, key);
        });

        return this;
    }

    @Override
    public SkullBuilder build() {
        this.itemStack.setData(DataComponentTypes.PROFILE, this.builder.build());

        return this;
    }
}