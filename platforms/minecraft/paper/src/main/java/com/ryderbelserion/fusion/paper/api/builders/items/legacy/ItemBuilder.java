package com.ryderbelserion.fusion.paper.api.builders.items.legacy;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.nexomc.nexo.api.NexoItems;
import com.ryderbelserion.fusion.kyori.utils.AdvUtils;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.kyori.utils.StringUtils;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.fusion.paper.api.builders.PlayerBuilder;
import com.ryderbelserion.fusion.paper.api.builders.gui.interfaces.GuiAction;
import com.ryderbelserion.fusion.paper.api.builders.gui.interfaces.GuiItem;
import com.ryderbelserion.fusion.kyori.enums.Support;
import com.ryderbelserion.fusion.paper.utils.ColorUtils;
import com.ryderbelserion.fusion.paper.utils.ItemUtils;
import dev.lone.itemsadder.api.CustomStack;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import io.papermc.paper.datacomponent.item.ItemAttributeModifiers;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import io.th0rgal.oraxen.api.OraxenItems;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.Tag;
import org.bukkit.block.Banner;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.damage.DamageType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.profile.PlayerTextures;
import org.bukkit.tag.DamageTypeTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class ItemBuilder<T extends ItemBuilder<T>> {

    private final FusionPaper fusion = (FusionPaper) FusionCore.Provider.get();

    private final NbtBuilder nbt = new NbtBuilder();

    private final Plugin plugin;

    private final Server server;

    private ItemStack itemStack;

    public ItemBuilder(@NotNull Plugin plugin) {
        this(plugin, ItemType.STONE, 1);
    }

    public ItemBuilder(@NotNull Plugin plugin, @NotNull ItemType itemType) {
        this(plugin, itemType, 1);
    }

    public ItemBuilder(@NotNull Plugin plugin, @NotNull ItemType itemType, final int amount) {
        this(plugin, itemType.createItemStack(amount), true);
    }

    public ItemBuilder(@NotNull Plugin plugin, @NotNull ItemStack itemStack, final boolean createNewStack) {
        this.itemStack = createNewStack ? itemStack.clone() : itemStack;

        this.plugin = plugin;
        this.server = this.plugin.getServer();
    }

    public ItemBuilder(@NotNull Plugin plugin, @NotNull ItemBuilder<T> itemBuilder) {
        this(plugin, itemBuilder, false);
    }

    public ItemBuilder(@NotNull Plugin plugin, @NotNull ItemBuilder<T> itemBuilder, final boolean createNewStack) {
        this.plugin = plugin;
        this.server = this.plugin.getServer();

        this.itemStack = createNewStack ? itemBuilder.itemStack.clone() : itemBuilder.itemStack;
        this.isCustom = itemBuilder.isCustom;

        this.damage = itemBuilder.damage;
        this.color = itemBuilder.color;

        this.displayLorePlaceholders = itemBuilder.displayLorePlaceholders;
        this.displayNamePlaceholders = itemBuilder.displayNamePlaceholders;

        this.displayComponentLore = itemBuilder.displayComponentLore;
        this.displayComponent = itemBuilder.displayComponent;
        this.displayLore = itemBuilder.displayLore;
        this.displayName = itemBuilder.displayName;

        this.potionEffects = itemBuilder.potionEffects;
        this.fireworkEffects = itemBuilder.fireworkEffects;

        this.entityType = itemBuilder.entityType;

        this.nutritionalValue = itemBuilder.nutritionalValue;
        this.canAlwaysEat = itemBuilder.canAlwaysEat;
        this.saturation = itemBuilder.saturation;
        this.eatSeconds = itemBuilder.eatSeconds;

        this.fireworkPower = itemBuilder.fireworkPower;

        this.patterns = itemBuilder.patterns;

        this.player = itemBuilder.player;
        this.url = itemBuilder.url;
        this.uuid = itemBuilder.uuid;

        this.potionType = itemBuilder.potionType;

        this.isHidingItemFlags = itemBuilder.isHidingItemFlags;
        this.isHidingToolTips = itemBuilder.isHidingToolTips;

        this.isUnbreakable = itemBuilder.isUnbreakable;
        this.isGlowing = itemBuilder.isGlowing;

        this.trimMaterial = itemBuilder.trimMaterial;
        this.trimPattern = itemBuilder.trimPattern;
    }

    private static final EnumSet<Material> LEATHER_ARMOR = EnumSet.of(
            Material.LEATHER_HELMET,
            Material.LEATHER_CHESTPLATE,
            Material.LEATHER_LEGGINGS,
            Material.LEATHER_BOOTS,
            Material.LEATHER_HORSE_ARMOR
    );

    private static final EnumSet<Material> POTIONS = EnumSet.of(
            Material.POTION, Material.SPLASH_POTION, Material.LINGERING_POTION
    );

    private static final EnumSet<Material> BANNERS = EnumSet.of(
            Material.WHITE_BANNER, Material.ORANGE_BANNER, Material.MAGENTA_BANNER, Material.LIGHT_BLUE_BANNER, Material.YELLOW_BANNER,
            Material.LIME_BANNER, Material.PINK_BANNER, Material.GRAY_BANNER, Material.LIGHT_GRAY_BANNER, Material.CYAN_BANNER,
            Material.PURPLE_BANNER, Material.BLUE_BANNER, Material.BROWN_BANNER, Material.GREEN_BANNER, Material.RED_BANNER,
            Material.BLACK_BANNER,
            Material.WHITE_WALL_BANNER, Material.ORANGE_WALL_BANNER, Material.MAGENTA_WALL_BANNER, Material.LIGHT_BLUE_WALL_BANNER, Material.YELLOW_WALL_BANNER,
            Material.LIME_WALL_BANNER, Material.PINK_WALL_BANNER, Material.GRAY_WALL_BANNER, Material.LIGHT_GRAY_WALL_BANNER, Material.CYAN_WALL_BANNER,
            Material.PURPLE_WALL_BANNER, Material.BLUE_WALL_BANNER, Material.BROWN_WALL_BANNER, Material.GREEN_WALL_BANNER, Material.RED_WALL_BANNER,
            Material.BLACK_WALL_BANNER
    );

    private static final EnumSet<Material> ARMOR = EnumSet.of(
            Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS,
            Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS,
            Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS,
            Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS,
            Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS,
            Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS
    );

    private static final EnumSet<Material> SHULKERS = EnumSet.of(
            Material.SHULKER_BOX, Material.BLUE_SHULKER_BOX, Material.BLACK_SHULKER_BOX, Material.CYAN_SHULKER_BOX, Material.GRAY_SHULKER_BOX,
            Material.BROWN_SHULKER_BOX, Material.LIGHT_BLUE_SHULKER_BOX, Material.LIGHT_GRAY_SHULKER_BOX, Material.LIME_SHULKER_BOX,
            Material.MAGENTA_SHULKER_BOX, Material.GREEN_SHULKER_BOX, Material.PINK_SHULKER_BOX, Material.ORANGE_SHULKER_BOX,
            Material.RED_SHULKER_BOX, Material.WHITE_SHULKER_BOX, Material.PURPLE_SHULKER_BOX, Material.YELLOW_SHULKER_BOX
    );

    public ItemBuilder(@NotNull Plugin plugin, @NotNull ItemStack itemStack) {
        this.plugin = plugin;
        this.server = this.plugin.getServer();

        this.itemStack = itemStack;

        if (hasItemMeta()) {
            final ItemMeta itemMeta = this.itemStack.getItemMeta();

            if (itemMeta.hasDisplayName()) {
                this.displayComponent = itemMeta.displayName();
            }

            if (itemMeta.hasLore()) {
                this.displayComponentLore = itemMeta.lore();
            }

            // Populates the damage
            this.damage = itemMeta instanceof final Damageable damageable ? damageable.getDamage() : 1;

            // Populates the power field
            if (isFirework() || isFireworkStar()) {
                if (itemMeta instanceof final FireworkMeta firework) {
                    this.fireworkEffects = firework.getEffects();
                    this.fireworkPower = firework.getPower();
                }
            } else if (isSpawner()) { // populates the entity type field
                if (itemMeta instanceof final BlockStateMeta blockState) {
                    @NotNull CreatureSpawner creatureSpawner = (CreatureSpawner) blockState.getBlockState();
                    @Nullable EntityType type = creatureSpawner.getSpawnedType();

                    if (type != null) this.entityType = type;
                }
            } else if (isBanner()) {
                if (itemMeta instanceof final BannerMeta banner) {
                    this.patterns.addAll(banner.getPatterns());
                }
            } else if (isShield()) {
                if (itemMeta instanceof final BlockStateMeta shield) {
                    @NotNull Banner banner = (Banner) shield.getBlockState();

                    this.patterns.addAll(banner.getPatterns());
                }
            } else if (isPlayerHead()) {
                if (itemMeta instanceof final SkullMeta skull) {
                    if (skull.hasOwner()) {
                        @Nullable OfflinePlayer target = skull.getOwningPlayer();

                        if (target != null) this.uuid = target.getUniqueId();
                    }
                }
            } else if (isArrow() || isPotion()) {
                if (itemMeta instanceof final PotionMeta potionMeta) {
                    this.color = potionMeta.getColor();
                    this.potionEffects = potionMeta.getCustomEffects();
                    this.potionType = potionMeta.getBasePotionType();
                }
            } else if (isLeather()) {
                if (itemMeta instanceof final LeatherArmorMeta armor) this.color = armor.getColor();
            } else if (isMap()) {
                if (itemMeta instanceof final MapMeta map) this.color = map.getColor();
            }

            if (itemMeta.hasDamageResistant()) {
                final Tag<DamageType> tag = itemMeta.getDamageResistant();

                if (tag != null) {
                    this.damageTags = new ArrayList<>() {{
                        add(tag);
                    }};
                }
            }
        }

        setGlowing(itemStack.hasData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE));

        final boolean hasToolTipDisplay = itemStack.hasData(DataComponentTypes.TOOLTIP_DISPLAY);

        if (hasToolTipDisplay) {
            final TooltipDisplay display = itemStack.getData(DataComponentTypes.TOOLTIP_DISPLAY);

            if (display != null) {
                setHidingToolTips(display.hideTooltip());
                setHidingItemFlags(display.hideTooltip());
            }
        }

        setUnbreakable(itemStack.hasData(DataComponentTypes.UNBREAKABLE));
    }

    private boolean isCustom = false;

    private @Nullable Color color = null;

    private int damage = 0;

    private List<String> displayLore = new ArrayList<>();

    private List<Component> displayComponentLore = new ArrayList<>();

    private Map<String, String> displayLorePlaceholders = new HashMap<>();

    private String displayName = "";

    private Component displayComponent = Component.empty();

    private Map<String, String> displayNamePlaceholders = new HashMap<>();

    private List<PotionEffect> potionEffects = new ArrayList<>();

    private List<FireworkEffect> fireworkEffects = new ArrayList<>();

    private @NotNull EntityType entityType = EntityType.PIG;

    private boolean canAlwaysEat = false;

    private int nutritionalValue = 0;

    private float saturation = 0.0f;

    private float eatSeconds = 0;

    private int fireworkPower = 1;

    private @NotNull List<Pattern> patterns = new ArrayList<>();

    private @Nullable UUID player = null;

    private @Nullable PotionType potionType = null;

    private @Nullable UUID uuid = null;

    private String url = "";

    private boolean isHidingItemFlags = false; //todo() itemflags are dead

    private boolean isHidingToolTips = false;

    private List<Tag<DamageType>> damageTags = new ArrayList<>();

    private boolean isUnbreakable = false;

    private boolean isGlowing = false;

    private TrimMaterial trimMaterial;

    private TrimPattern trimPattern;

    public @NotNull GuiItem asGuiItem(@Nullable GuiAction<@NotNull InventoryClickEvent> action) {
        return new GuiItem(asItemStack(), action);
    }

    public @NotNull GuiItem asGuiItem() {
        return new GuiItem(asItemStack(), null);
    }

    public @NotNull ItemStack asItemStack() {
        return asItemStack(null);
    }

    public @NotNull ItemStack asItemStack(@Nullable Consumer<ItemMeta> consumer) {
        if (this.isCustom) return this.itemStack;

        // Daisy chain it all!
        applyColor().applyEffects().applyEntityType().applyPattern().applySkull().applyTexture().applyDamage();

        if (this.trimPattern != null && this.trimMaterial != null) {
            applyTrim(this.trimPattern, this.trimMaterial);
        }

        this.itemStack.editMeta(itemMeta -> {
            if (consumer != null) consumer.accept(itemMeta);

            String displayName = this.displayName;

            if (!displayName.isEmpty()) {
                if (!this.displayNamePlaceholders.isEmpty()) {
                    for (final Map.Entry<String, String> entry : this.displayNamePlaceholders.entrySet()) {
                        final String key = entry.getKey().toLowerCase(); // Make the placeholder lowercase
                        final String value = entry.getValue();

                        displayName = displayName.replace(key, value);
                    }
                }

                itemMeta.displayName(this.displayComponent = AdvUtils.parse(displayName));
            }

            if (!this.displayLore.isEmpty()) {
                final boolean isEmpty = this.displayLorePlaceholders.isEmpty();

                final List<Component> components = new ArrayList<>();

                for (String line : this.displayLore) {
                    if (!isEmpty) {
                        for (final Map.Entry<String, String> entry : this.displayLorePlaceholders.entrySet()) {
                            final String key = entry.getKey().toLowerCase();
                            final String value = entry.getValue();

                            line = line.replace(key, value);
                        }
                    }

                    components.add(AdvUtils.parse(line));
                }

                itemMeta.lore(this.displayComponentLore = components);
            }

            this.damageTags.forEach(itemMeta::setDamageResistant);

            itemMeta.setHideTooltip(this.isHidingToolTips);
        });

        if (this.isHidingItemFlags) {
            final List<ItemAttributeModifiers.Entry> values = new ArrayList<>();

            if (this.itemStack.hasData(DataComponentTypes.ATTRIBUTE_MODIFIERS)) {
                final ItemAttributeModifiers modifier = this.itemStack.getData(DataComponentTypes.ATTRIBUTE_MODIFIERS);

                if (modifier != null) {
                    values.addAll(modifier.modifiers());
                }
            }

            final ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.itemAttributes();

            values.forEach(modifier -> builder.addModifier(modifier.attribute(), modifier.modifier()));

            this.itemStack.setData(DataComponentTypes.ATTRIBUTE_MODIFIERS, builder);
        }

        if (this.isGlowing) {
            this.itemStack.setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);
        }

        if (this.isHidingToolTips) {
            final TooltipDisplay display = TooltipDisplay.tooltipDisplay().hideTooltip(true).build();

            this.itemStack.setData(DataComponentTypes.TOOLTIP_DISPLAY, display);
        }

        if (this.isUnbreakable) {
            this.itemStack.setData(DataComponentTypes.UNBREAKABLE);
        }

        return this.itemStack;
    }

    public @NotNull String toBase64() {
        return ItemUtils.toBase64(asItemStack());
    }

    public @NotNull T fromBase64(@NotNull Plugin plugin, @NotNull String base64) {
        if (base64.isEmpty()) return (T) new ItemBuilder<T>(plugin);

        return (T) new ItemBuilder<T>(plugin, ItemUtils.fromBase64(base64));
    }

    public @NotNull T apply(final Consumer<ItemBuilder<T>> builder) {
        if (builder != null) {
            builder.accept(this);
        }

        return (T) this;
    }

    public @NotNull T withType(@Nullable ItemType itemType) {
        return withType(itemType, 1);
    }

    public @NotNull T withType(@Nullable ItemType itemType, final int amount) {
        if (itemType == null) return (T) this;

        final ItemStack itemStack = itemType.createItemStack(Math.max(amount, 1));

        this.itemStack = this.itemStack.withType(itemStack.getType());
        this.itemStack.setAmount(itemStack.getAmount());

        return (T) this;
    }

    public @NotNull T withType(@NotNull String key) {
        if (key.isEmpty()) return (T) this;

        switch (this.fusion.getItemsPlugin().toLowerCase()) {
            case "nexo" -> {
                if (Support.nexo.isEnabled()) {
                    getNexo(key);
                }
            }

            case "oraxen" -> {
                if (Support.oraxen.isEnabled()) {
                    getOraxen(key);
                }
            }

            case "itemsadder" -> {
                if (Support.items_adder.isEnabled()) {
                    getItemsAdder(key);
                }
            }

            default -> {
                if (Support.nexo.isEnabled()) {
                    getNexo(key);
                } else if (Support.items_adder.isEnabled()) {
                    getItemsAdder(key);
                } else if (Support.oraxen.isEnabled()) {
                    getOraxen(key);
                }
            }
        }

        // Don't override the provided material but copy it instead.
        String type = key;

        if (key.contains(":")) {
            final String[] sections = key.split(":");

            type = sections[0];
            String data = sections[1];

            if (data.contains("#")) {
                final String model = data.split("#")[1];

                final Optional<Number> customModelData = StringUtils.tryParseInt(model);

                if (customModelData.isPresent()) {
                    data = data.replace("#" + customModelData.get(), "");

                    setCustomModelData(customModelData.get().intValue());
                }
            }

            final Optional<Number> damage = StringUtils.tryParseInt(data);

            if (damage.isEmpty()) {
                @Nullable PotionEffectType potionEffect = ItemUtils.getPotionEffect(data);

                if (potionEffect != null) {
                    this.potionEffects.add(new PotionEffect(potionEffect, 1, 1));
                }

                this.potionType = ItemUtils.getPotionType(data);

                this.color = data.contains(",") ? ColorUtils.getRGB(data) : ColorUtils.getColor(data);
            } else {
                this.damage = damage.get().intValue();
            }
        } else if (key.contains("#")) {
            final String[] sections = key.split("#");
            type = sections[0];
            final String model = sections[1];

            final Optional<Number> customModelData = StringUtils.tryParseInt(model);

            customModelData.ifPresent(number -> setCustomModelData(number.intValue()));
        }

        @Nullable ItemType itemType = ItemUtils.getItemType(type);

        if (itemType == null) {
            return (T) this;
        }

        return withType(itemType);
    }

    public @NotNull T setDisplayLore(@NotNull List<String> displayLore) {
        if (displayLore.isEmpty()) return (T) this;

        this.displayLore = displayLore;

        return (T) this;
    }

    public @NotNull T addDisplayLore(@NotNull String displayLore) {
        if (displayLore.isEmpty()) return (T) this;

        this.displayLore.add(displayLore);

        return (T) this;
    }

    public @NotNull T setDisplayName(@NotNull String displayName) {
        if (displayName.isEmpty()) return (T) this;

        this.displayName = displayName;

        return (T) this;
    }

    public @NotNull T setCustomModelData(final int model) {
        if (model == -1) return (T) this;

        this.itemStack.setData(DataComponentTypes.CUSTOM_MODEL_DATA, populateData().addFloat(model).build());

        return (T) this;
    }

    public @NotNull T setCustomModelData(@NotNull String model) {
        if (model.isEmpty()) return (T) this;

        final Optional<Number> integer = StringUtils.tryParseInt(model);

        if (integer.isPresent()) {
            return setCustomModelData(integer.orElse(-1).intValue());
        }

        final CustomModelData.Builder data = populateData();

        this.itemStack.setData(DataComponentTypes.CUSTOM_MODEL_DATA, data.addString(model).build());

        return (T) this;
    }

    public @NotNull T setItemModel(@NotNull String itemModel) {
        if (itemModel.isEmpty()) return (T) this;

        this.itemStack.setData(DataComponentTypes.ITEM_MODEL, NamespacedKey.minecraft(itemModel));

        return (T) this;
    }

    public @NotNull T setItemModel(@NotNull String namespace, @NotNull String itemModel) {
        if (namespace.isEmpty() || itemModel.isEmpty()) return (T) this;

        this.itemStack.setData(DataComponentTypes.ITEM_MODEL, new NamespacedKey(namespace, itemModel));

        return (T) this;
    }

    public @NotNull T setHidingItemFlags(final boolean isHidingItemFlags) { //todo() itemflags are dead
        this.isHidingItemFlags = isHidingItemFlags;

        return (T) this;
    }

    public @NotNull T setHidingToolTips(final boolean isHidingToolTips) {
        this.isHidingToolTips = isHidingToolTips;

        return (T) this;
    }

    @Deprecated(forRemoval = true)
    public @NotNull T setFireResistant() {
        return addDamageTag(DamageTypeTags.IS_FIRE);
    }

    public @NotNull T addDamageTag(@NotNull Tag<DamageType> tag) {
        this.damageTags.add(tag);

        return (T) this;
    }

    public @NotNull T removeDamageTag(@NotNull Tag<DamageType> tag) {
        this.damageTags.remove(tag);

        return (T) this;
    }

    public @NotNull T addFireworkEffect(@NotNull FireworkEffect.Builder effect) {
        if (!isFirework() && !isFireworkStar()) return (T) this;

        this.fireworkEffects.add(effect.build());

        return (T) this;
    }

    public @NotNull T setFireworkPower(final int fireworkPower) {
        if (!isFirework() && !isFireworkStar()) return (T) this;

        this.fireworkPower = fireworkPower;

        return (T) this;
    }

    public @NotNull T setUnbreakable(final boolean isUnbreakable) {
        this.isUnbreakable = isUnbreakable;

        return (T) this;
    }

    public @NotNull T setGlowing(final boolean isGlowing) {
        this.isGlowing = isGlowing;

        return (T) this;
    }

    public @NotNull T setAmount(final int amount) {
        this.itemStack.setAmount(amount);

        return (T) this;
    }

    public @NotNull T addNamePlaceholder(@NotNull String placeholder, @NotNull String value) {
        return addPlaceholder(placeholder, value, false);
    }

    public @NotNull T setNamePlaceholders(@NotNull Map<String, String> placeholders) {
        placeholders.forEach(this::addNamePlaceholder);

        return (T) this;
    }

    public @NotNull T addLorePlaceholder(@NotNull String placeholder, @NotNull String value) {
        return addPlaceholder(placeholder, value, true);
    }

    public @NotNull T setLorePlaceholders(@NotNull Map<String, String> placeholders) {
        placeholders.forEach(this::addLorePlaceholder);

        return (T) this;
    }

    public @NotNull T addPattern(@NotNull PatternType type, @NotNull DyeColor color) {
        if (!isBanner() && !isShield()) return (T) this;

        this.patterns.add(new Pattern(color, type));

        return (T) this;
    }

    public @NotNull T addPattern(@NotNull String pattern) {
        if (!isBanner() && !isShield()) return (T) this;

        if (!pattern.contains(":")) return (T) this;

        final String[] sections = pattern.split(":");
        final PatternType type = ItemUtils.getPatternType(sections[0].toLowerCase());
        final DyeColor color = ColorUtils.getDyeColor(sections[1]);

        if (type == null) return (T) this;

        return addPattern(type, color);
    }

    public @NotNull T addPatterns(@NotNull List<String> patterns) {
        if (!isBanner() && !isShield()) return (T) this;

        patterns.forEach(this::addPattern);

        return (T) this;
    }

    public @NotNull T addPotionEffect(@NotNull PotionEffectType type, final int duration, final int amplifier) {
        if (!isArrow() && !isPotion()) return (T) this;

        this.potionEffects.add(new PotionEffect(type, duration, amplifier));

        return (T) this;
    }

    public @NotNull T setPotionType(@NotNull PotionType potionType) {
        if (!isPotion()) return (T) this;

        this.potionType = potionType;

        return (T) this;
    }

    public @NotNull T setColor(@NotNull Color color) {
        this.color = color;

        return (T) this;
    }

    public @NotNull T setCustom(final boolean isCustom) {
        this.isCustom = isCustom;

        return (T) this;
    }

    public @NotNull T setEntityType(@NotNull EntityType entityType) {
        this.entityType = entityType;

        return (T) this;
    }

    public @NotNull T setDamage(final int damage) {
        this.damage = damage;

        return (T) this;
    }

    public @NotNull T setPersistentDouble(@NotNull NamespacedKey key, final double value) {
        this.nbt.setItemStack(this.itemStack).setPersistentDouble(key, value);

        return (T) this;
    }

    public @NotNull T setPersistentInteger(@NotNull NamespacedKey key, final int value) {
        this.nbt.setItemStack(this.itemStack).setPersistentInteger(key, value);

        return (T) this;
    }

    public @NotNull T setPersistentBoolean(@NotNull NamespacedKey key, final boolean value) {
        this.nbt.setItemStack(this.itemStack).setPersistentBoolean(key, value);

        return (T) this;
    }

    public @NotNull T setPersistentString(@NotNull NamespacedKey key, @NotNull String value) {
        this.nbt.setItemStack(this.itemStack).setPersistentString(key, value);

        return (T) this;
    }

    public @NotNull T setPersistentList(@NotNull NamespacedKey key, @NotNull List<String> values) {
        this.nbt.setItemStack(this.itemStack).setPersistentList(key, values);

        return (T) this;
    }

    public final boolean getBoolean(@NotNull NamespacedKey key) {
        return this.nbt.setItemStack(this.itemStack).getBoolean(key);
    }

    public final double getDouble(@NotNull NamespacedKey key) {
        return this.nbt.setItemStack(this.itemStack).getDouble(key);
    }

    public final int getInteger(@NotNull NamespacedKey key) {
        return this.nbt.setItemStack(this.itemStack).getInteger(key);
    }

    public @NotNull List<String> getList(@NotNull NamespacedKey key) {
        return this.nbt.setItemStack(this.itemStack).getList(key);
    }

    public @NotNull String getString(@NotNull NamespacedKey key) {
        return this.nbt.setItemStack(this.itemStack).getString(key);
    }

    public @NotNull T removePersistentKey(@Nullable NamespacedKey key) {
        this.nbt.setItemStack(this.itemStack).removePersistentKey(key);

        return (T) this;
    }

    public final boolean hasKey(@NotNull NamespacedKey key) {
        return this.nbt.setItemStack(this.itemStack).hasKey(key);
    }

    public @Nullable UUID getPlayer() {
        return this.player;
    }

    public @NotNull T setPlayer(@NotNull Player player) {
        if (player.isEmpty()) return (T) this;

        this.player = player.getUniqueId();

        return (T) this;
    }

    public @NotNull T setPlayer(@NotNull String player) {
        if (player.isEmpty()) return (T) this;

        // This is temporary until HDB is updated, The dev of the plugin has a house now
        // and his plugin doesn't work on 1.20.6
        if (player.length() > 16) {
            this.url = "https://textures.minecraft.net/texture/" + player;

            return (T) this;
        }

        this.uuid = new PlayerBuilder(player).getUniqueId();

        return (T) this;
    }

    public @NotNull T setSkull(@NotNull UUID uuid) {
        if (!isPlayerHead()) return (T) this;

        this.uuid = uuid;

        return (T) this;
    }

    public @NotNull T setSkull(@NotNull String skull) {
        if (skull.isEmpty()) return (T) this;

        @NotNull HeadDatabaseAPI hdb = this.fusion.getApi();

        this.itemStack = hdb.isHead(skull) ? hdb.getItemHead(skull) : this.itemStack.withType(Material.PLAYER_HEAD);

        return (T) this;
    }

    public @NotNull T addEnchantment(@NotNull String enchant, final int level, final boolean ignoreLevelCap) {
        if (this.isCustom || enchant.isEmpty() || level < 0) return (T) this;

        @Nullable Enchantment enchantment = ItemUtils.getEnchantment(enchant);

        if (enchantment == null) return (T) this;

        this.itemStack.editMeta(itemMeta -> {
            if (isEnchantedBook() && itemMeta instanceof EnchantmentStorageMeta storageMeta) {
                storageMeta.addStoredEnchant(enchantment, level, ignoreLevelCap);

                return;
            }

            itemMeta.addEnchant(enchantment, level, ignoreLevelCap);
        });

        return (T) this;
    }

    public @NotNull T removeEnchantment(@NotNull String enchant) {
        if (this.isCustom) return (T) this;
        if (enchant.isEmpty()) return (T) this;

        @Nullable Enchantment enchantment = ItemUtils.getEnchantment(enchant);

        if (enchantment == null) return (T) this;

        if (hasItemMeta()) {
            this.itemStack.editMeta(itemMeta -> {
                if (isEnchantedBook() && itemMeta instanceof EnchantmentStorageMeta storageMeta && storageMeta.hasStoredEnchant(enchantment)) {
                    storageMeta.removeStoredEnchant(enchantment);

                    return;
                }

                if (itemMeta.hasEnchant(enchantment)) {
                    itemMeta.removeEnchant(enchantment);
                }
            });
        }

        return (T) this;
    }

    public @NotNull T addEnchantments(@NotNull Map<String, Integer> enchantments, final boolean ignoreLevelCap) {
        if (this.isCustom) return (T) this;
        if (enchantments.isEmpty()) return (T) this;

        enchantments.forEach((enchantment, level) -> addEnchantment(enchantment, level, ignoreLevelCap));

        return (T) this;
    }

    public @NotNull T removeEnchantments(@NotNull Set<String> enchantments) {
        if (this.isCustom) return (T) this;
        if (enchantments.isEmpty()) return (T) this;

        enchantments.forEach(this::removeEnchantment);

        return (T) this;
    }

    public @NotNull T applyUnbreakable() {
        if (this.isCustom) return (T) this;

        this.itemStack.editMeta(itemMeta -> itemMeta.setUnbreakable(this.isUnbreakable));

        return (T) this;
    }

    public @NotNull T applyEntityType() {
        if (this.isCustom) return (T) this;
        if (!isSpawner()) return (T) this;

        this.itemStack.editMeta(itemMeta -> {
            if (itemMeta instanceof final BlockStateMeta blockState) {
                final CreatureSpawner creatureSpawner = (CreatureSpawner) blockState.getBlockState();

                creatureSpawner.setSpawnedType(this.entityType);
                blockState.setBlockState(creatureSpawner);
            }
        });

        return (T) this;
    }

    public @NotNull T applyTrim(@NotNull String pattern, @NotNull String material) {
        if (this.isCustom) return (T) this;
        if (pattern.isEmpty() || material.isEmpty()) return (T) this;

        final TrimMaterial trimMaterial = ItemUtils.getTrimMaterial(material);

        final TrimPattern trimPattern = ItemUtils.getTrimPattern(pattern);

        if (trimPattern == null || trimMaterial == null) return (T) this;

        return applyTrim(trimPattern, trimMaterial);
    }

    public @NotNull T applyTrimPattern(@NotNull String pattern) {
        if (this.isCustom) return (T) this;
        if (pattern.isEmpty()) return (T) this;

        final TrimPattern trimPattern = ItemUtils.getTrimPattern(pattern);

        if (trimPattern == null) return (T) this;

        this.trimPattern = trimPattern;

        return (T) this;
    }

    public @NotNull T applyTrimMaterial(@NotNull String material) {
        if (this.isCustom) return (T) this;
        if (material.isEmpty()) return (T) this;

        final TrimMaterial trimMaterial = ItemUtils.getTrimMaterial(material);

        if (trimMaterial == null) return (T) this;

        this.trimMaterial = trimMaterial;

        return (T) this;
    }

    public @NotNull T applyPattern() {
        if (this.isCustom) return (T) this;
        if (this.patterns.isEmpty()) return (T) this;

        if (isBanner()) {
            this.itemStack.editMeta(itemMeta -> {
                if (itemMeta instanceof final BannerMeta banner) banner.setPatterns(this.patterns);
            });
        } else if (isShield()) {
            this.itemStack.editMeta(itemMeta -> {
                if (itemMeta instanceof final BlockStateMeta shield) {
                    Banner banner = (Banner) shield.getBlockState();
                    banner.setPatterns(this.patterns);
                    banner.update();

                    shield.setBlockState(banner);
                }
            });
        }

        return (T) this;
    }

    public @NotNull T applyDamage() {
        if (this.isCustom) return (T) this;

        this.itemStack.editMeta(itemMeta -> {
            if (itemMeta instanceof final Damageable damageable) {
                if (this.damage <= 0) return;

                if (this.damage >= getType().getMaxDurability()) {
                    damageable.setDamage(getType().getMaxDurability());
                } else {
                    damageable.setDamage(this.damage);
                }
            }
        });

        return (T) this;
    }

    public @NotNull T applyEffects() {
        if (this.isCustom) return (T) this;

        if (isFirework() || isFireworkStar() && !this.fireworkEffects.isEmpty()) {
            this.itemStack.editMeta(itemMeta -> {
                if (itemMeta instanceof final FireworkMeta firework) {
                    this.fireworkEffects.forEach(firework::addEffect);

                    firework.setPower(this.fireworkPower);
                }
            });
        } else if (isPotion() || isArrow() && !this.potionEffects.isEmpty()) {
            this.itemStack.editMeta(itemMeta -> {
                if (itemMeta instanceof final PotionMeta potion) {
                    this.potionEffects.forEach(effect -> potion.addCustomEffect(effect, true));

                    if (this.potionType != null) potion.setBasePotionType(this.potionType);
                }
            });
        }

        return (T) this;
    }

    public @NotNull T applyTexture() {
        if (this.isCustom) return (T) this;
        if (this.url.isEmpty()) return (T) this;
        if (!isPlayerHead()) return (T) this;

        this.itemStack.editMeta(itemMeta -> {
            if (itemMeta instanceof final SkullMeta skullMeta) {
                final PlayerProfile profile = Bukkit.getServer().createProfile(null, "");

                profile.setProperty(new ProfileProperty("", ""));

                PlayerTextures textures = profile.getTextures();

                try {
                    textures.setSkin(URI.create(this.url).toURL(), PlayerTextures.SkinModel.CLASSIC);
                } catch (MalformedURLException exception) {
                    throw new FusionException("Failed to load skull texture", exception);
                }

                profile.setTextures(textures);
                skullMeta.setPlayerProfile(profile);
            }
        });

        return (T) this;
    }

    public @NotNull T applySkull() {
        if (this.isCustom) return (T) this;
        if (this.uuid == null) return (T) this;
        if (!isPlayerHead()) return (T) this;

        this.itemStack.editMeta(itemMeta -> {
            if (itemMeta instanceof final SkullMeta skull) {
                if (!skull.hasOwner()) {
                    skull.setOwningPlayer(getOfflinePlayer(this.uuid));
                }
            }
        });

        return (T) this;
    }

    public @NotNull T applyTrim(@NotNull TrimPattern trimPattern, @NotNull TrimMaterial trimMaterial) {
        if (this.isCustom) return (T) this;
        if (!isArmor()) return (T) this;

        this.itemStack.editMeta(itemMeta -> {
            if (itemMeta instanceof final ArmorMeta armorMeta) armorMeta.setTrim(new ArmorTrim(trimMaterial, trimPattern));
        });

        return (T) this;
    }

    public @NotNull T applyColor() {
        if (this.isCustom) return (T) this;
        if (this.color == null) return (T) this;

        if (isArrow() || isPotion()) {
            this.itemStack.editMeta(itemMeta -> {
                if (itemMeta instanceof final PotionMeta potion) potion.setColor(this.color);
            });
        }

        else if (isLeather()) {
            this.itemStack.editMeta(itemMeta -> {
                if (itemMeta instanceof final LeatherArmorMeta armor) armor.setColor(this.color);
            });
        }

        else if (isMap()) {
            this.itemStack.editMeta(itemMeta -> {
                if (itemMeta instanceof final MapMeta map) {
                    map.setScaling(true);
                    map.setColor(this.color);
                }
            });
        }

        return (T) this;
    }

    public @NotNull String getStrippedName() {
        return PlainTextComponentSerializer.plainText().serialize(this.itemStack.displayName());
    }

    public @NotNull List<String> getStrippedLore() {
        final List<String> lore = new ArrayList<>();

        this.displayComponentLore.forEach(line -> lore.add(PlainTextComponentSerializer.plainText().serialize(line)));

        return lore;
    }

    public @NotNull List<String> getDisplayLore() {
        return this.displayLore;
    }

    public @NotNull String getDisplayName() {
        return this.displayName;
    }

    public final boolean hasItemMeta() {
        return !this.itemStack.hasItemMeta();
    }

    public @NotNull Material getType() {
        return this.itemStack.getType();
    }

    public final boolean isBanner() {
        return BANNERS.contains(getType());
    }

    public final boolean isArmor() {
        return ARMOR.contains(getType());
    }

    public final boolean isShulker() {
        return SHULKERS.contains(getType());
    }

    public final boolean isLeather() {
        return LEATHER_ARMOR.contains(getType());
    }

    public final boolean isPotion() {
        return POTIONS.contains(getType());
    }

    public final boolean isEnchantedBook() {
        return getType().equals(Material.ENCHANTED_BOOK);
    }

    public final boolean isPlayerHead() {
        return getType().equals(Material.PLAYER_HEAD);
    }

    public final boolean isFireworkStar() {
        return getType().equals(Material.FIREWORK_STAR);
    }

    public final boolean isFirework() {
        return getType().equals(Material.FIREWORK_ROCKET);
    }

    public final boolean isSpawner() {
        return getType().equals(Material.SPAWNER);
    }

    public final boolean isShield() {
        return getType().equals(Material.SHIELD);
    }

    public final boolean isArrow() {
        return getType().equals(Material.ARROW);
    }

    public final boolean isMap() {
        return getType().equals(Material.MAP);
    }

    public final boolean isHidingItemFlags() { //todo() itemflags are dead
        return this.isHidingItemFlags;
    }

    public final boolean isHidingToolTips() {
        return this.isHidingToolTips;
    }

    private @NotNull T addPlaceholder(@NotNull String placeholder, @NotNull String value, boolean isLore) {
        if (isLore) {
            this.displayLorePlaceholders.put(placeholder, value);

            return (T) this;
        }

        this.displayNamePlaceholders.put(placeholder, value);

        return (T) this;
    }

    private @NotNull OfflinePlayer getOfflinePlayer(@NotNull UUID uuid) {
        return this.server.getOfflinePlayer(uuid);
    }

    private @Nullable Player getPlayer(@NotNull UUID uuid) {
        return this.server.getPlayer(uuid);
    }

    private @NotNull CustomModelData.Builder populateData() {
        final CustomModelData.Builder data = CustomModelData.customModelData();

        if (this.itemStack.hasData(DataComponentTypes.CUSTOM_MODEL_DATA)) {
            @Nullable CustomModelData component = this.itemStack.getData(DataComponentTypes.CUSTOM_MODEL_DATA);

            if (component != null) {
                data.addFloats(component.floats()).addStrings(component.strings()).addFlags(component.flags()).addColors(component.colors());
            }
        }

        return data;
    }

    private void getItemsAdder(@NotNull String item) {
        if (!CustomStack.isInRegistry(item)) {
            return;
        }

        final CustomStack builder = CustomStack.getInstance(item);

        if (builder == null) {
            throw new FusionException("The id " + item + " is not a valid ItemsAdder item!");
        }

        this.itemStack = builder.getItemStack();
    }

    private void getOraxen(@NotNull String item) {
        if (!OraxenItems.exists(item)) {
            return;
        }

        final io.th0rgal.oraxen.items.ItemBuilder builder = OraxenItems.getItemById(item);

        if (builder == null) {
            throw new FusionException("The id " + item + " is not a valid Oraxen item!");
        }

        this.itemStack = builder.build();
    }

    private void getNexo(@NotNull String item) {
        if (!NexoItems.exists(item)) {
            return;
        }

        final com.nexomc.nexo.items.ItemBuilder builder = NexoItems.itemFromId(item);

        if (builder == null) {
            throw new FusionException("The id " + item + " is not a valid Nexo item!");
        }

        this.itemStack = builder.build();
    }
}