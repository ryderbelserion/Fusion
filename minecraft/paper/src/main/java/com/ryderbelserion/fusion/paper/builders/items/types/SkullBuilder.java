package com.ryderbelserion.fusion.paper.builders.items.types;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.ryderbelserion.fusion.core.api.enums.Level;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.paper.builders.items.BaseItemBuilder;
import com.ryderbelserion.fusion.paper.utils.ItemUtils;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.profile.PlayerTextures;
import org.jspecify.annotations.NonNull;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Map;
import java.util.UUID;

public class SkullBuilder extends BaseItemBuilder<SkullBuilder> {

    private final ResolvableProfile.Builder builder;

    public SkullBuilder(@NonNull final ItemStack itemStack) {
        super(itemStack);

        this.builder = ResolvableProfile.resolvableProfile();
    }

    public @NonNull SkullBuilder withAudience(@NonNull final Audience audience) {
        final UUID uuid = audience.getOrDefault(Identity.UUID, null);

        if (uuid == null) return this;

        this.builder.uuid(uuid);

        return this;
    }

    public @NonNull SkullBuilder withUrl(@NonNull final String url) {
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
    public @NonNull SkullBuilder withBase64(@NonNull final String base64) {
        if (base64.isEmpty()) return this;

        this.builder.addProperty(new ProfileProperty("textures", base64));

        return this;
    }

    public @NonNull SkullBuilder withName(@NonNull final String playerName) {
        if (playerName.isEmpty()) return this;

        if (playerName.length() > 16) return withUrl(playerName);

        this.builder.name(playerName);

        return this;
    }

    public @NonNull SkullBuilder withNoteBlockSound(@NonNull final String sound) {
        if (sound.isEmpty()) return this;

        final Sound value = ItemUtils.getSound(sound);

        if (value == null) return this;

        final NamespacedKey key = Registry.SOUNDS.getKey(value);

        if (key == null) {
            this.fusion.log(Level.WARNING, "No valid NamespacedKey found for %s", sound);

            return this;
        }

        this.itemStack.setData(DataComponentTypes.NOTE_BLOCK_SOUND, key);

        return this;
    }

    @Override
    public @NonNull SkullBuilder build() {
        this.itemStack.setData(DataComponentTypes.PROFILE, this.builder.build());

        return this;
    }
}