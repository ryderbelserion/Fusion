package com.ryderbelserion.paper.builder.items.modern;

import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.items.ItemBuilder;
import com.ryderbelserion.core.util.StringUtils;
import com.ryderbelserion.paper.FusionApi;
import com.ryderbelserion.core.api.exception.FusionException;
import com.ryderbelserion.paper.Fusion;
import com.ryderbelserion.paper.builder.api.ComponentBuilder;
import com.ryderbelserion.paper.builder.items.modern.types.PotionBuilder;
import com.ryderbelserion.paper.builder.items.modern.types.PatternBuilder;
import com.ryderbelserion.paper.builder.items.modern.types.SkullBuilder;
import com.ryderbelserion.paper.builder.items.modern.types.SpawnerBuilder;
import com.ryderbelserion.paper.builder.items.modern.types.fireworks.FireworkBuilder;
import com.ryderbelserion.paper.builder.items.modern.types.fireworks.FireworkStarBuilder;
import com.ryderbelserion.paper.enums.Support;
import com.ryderbelserion.paper.util.PaperMethods;
import dev.lone.itemsadder.api.CustomStack;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import io.papermc.paper.datacomponent.item.DyedItemColor;
import io.papermc.paper.datacomponent.item.ItemArmorTrim;
import io.papermc.paper.datacomponent.item.ItemEnchantments;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.datacomponent.item.MapItemColor;
import io.papermc.paper.datacomponent.item.Unbreakable;
import io.th0rgal.oraxen.api.OraxenItems;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class BaseItemBuilder<B extends BaseItemBuilder<B>> {

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

    protected final FusionApi api = FusionApi.get();

    protected final Fusion fusion = this.api.getFusion();

    protected final Plugin plugin = this.api.getPlugin();

    private Map<String, String> placeholders = new HashMap<>();

    private List<String> displayLore = new ArrayList<>();
    private String displayName = null;

    private boolean isStatic = false;

    private ItemStack item;

    public BaseItemBuilder(final ItemStack item) {
        this.item = item;
    }

    public BaseItemBuilder(final String item) {
        withCustomItem(item);
    }

    public ItemStack asItemStack(@Nullable final Audience audience, final boolean isLegacy) {
        if (this.displayName != null) {
            if (isLegacy) { // legacy support for other plugins, only here temporarily
                this.item.editMeta(itemMeta -> {
                    String line = this.displayName;

                    for (final Map.Entry<String, String> placeholder : this.placeholders.entrySet()) {
                        if (placeholder != null) {
                            final String key = placeholder.getKey();
                            final String value = placeholder.getValue();

                            if (value != null) {
                                line = line.replace(key, value).replace(key.toLowerCase(), value);
                            }
                        }
                    }

                    itemMeta.setDisplayName(PaperMethods.color(line));
                });
            } else {
                final ComponentBuilder builder = new ComponentBuilder(this.displayName);

                this.item.setData(this.isStatic ? DataComponentTypes.ITEM_NAME : DataComponentTypes.CUSTOM_NAME, builder.asComponent(audience, this.placeholders));
            }
        }

        if (!this.displayLore.isEmpty()) {
            if (isLegacy) { // legacy support for other plugins, only here temporarily
                final List<String> lines = new ArrayList<>();

                for (String line : this.displayLore) {
                    for (final Map.Entry<String, String> placeholder : this.placeholders.entrySet()) {
                        if (placeholder != null) {
                            final String key = placeholder.getKey();
                            final String value = placeholder.getValue();

                            if (value != null) {
                                line = line.replace(key, value).replace(key.toLowerCase(), value);
                            }
                        }
                    }

                    lines.add(PaperMethods.color(line));
                }

                this.item.editMeta(itemMeta -> itemMeta.setLore(lines));
            } else {
                final ComponentBuilder builder = new ComponentBuilder(this.displayLore);

                this.item.setData(DataComponentTypes.LORE, ItemLore.lore(builder.asComponents(audience, this.placeholders)));
            }
        }

        build();

        return this.item;
    }

    public ItemStack asItemStack(boolean isLegacy) {
        return asItemStack(null, isLegacy);
    }

    public ItemStack asItemStack() {
        return asItemStack(false);
    }

    public B build() {
        return (B) this;
    }

    public B withType(@Nullable final ItemType type, final int amount) {
        if (type == null) {
            throw new FusionException("The item type cannot be null!");
        }

        if (this.item == null) {
            this.item = type.createItemStack(Math.max(amount, 1));
        }

        return (B) this;
    }

    public B withType(@Nullable final ItemType type) {
        return withType(type, 1);
    }

    public B withCustomItem(final String item) {
        switch (this.api.getFusion().getItemPlugin().toLowerCase()) {
            case "nexo" -> {
                if (Support.nexo.isEnabled()) {
                    getNexo(item);

                    return (B) this;
                }

                final ItemType itemType = PaperMethods.getItemType(item);

                if (itemType != null) {
                    this.item = itemType.createItemStack(1);
                } else {
                    try {
                        this.item = PaperMethods.fromBase64(item);
                    } catch (Exception exception) {
                        this.item = ItemType.STONE.createItemStack(1);
                    }
                }
            }

            case "oraxen" -> {
                if (Support.oraxen.isEnabled()) {
                    getOraxen(item);

                    return (B) this;
                }

                final ItemType itemType = PaperMethods.getItemType(item);

                if (itemType != null) {
                    this.item = itemType.createItemStack(1);
                } else {
                    try {
                        this.item = PaperMethods.fromBase64(item);
                    } catch (Exception exception) {
                        this.item = ItemType.STONE.createItemStack(1);
                    }
                }
            }

            case "itemsadder" -> {
                if (Support.items_adder.isEnabled()) {
                    getItemsAdder(item);

                    return (B) this;
                }

                final ItemType itemType = PaperMethods.getItemType(item);

                if (itemType != null) {
                    this.item = itemType.createItemStack(1);
                } else {
                    try {
                        this.item = PaperMethods.fromBase64(item);
                    } catch (Exception exception) {
                        this.item = ItemType.STONE.createItemStack(1);
                    }
                }
            }

            case "none" -> {
                final ItemType itemType = PaperMethods.getItemType(item);

                if (itemType != null) {
                    this.item = itemType.createItemStack(1);
                } else {
                    try {
                        this.item = PaperMethods.fromBase64(item);
                    } catch (Exception exception) {
                        this.item = ItemType.STONE.createItemStack(1);
                    }
                }
            }

            default -> {
                if (Support.nexo.isEnabled() && NexoItems.exists(item)) {
                    getNexo(item);

                    return (B) this;
                }

                if (Support.items_adder.isEnabled()) {
                    if (CustomStack.isInRegistry(item)) {
                        getItemsAdder(item);
                    }

                    return (B) this;
                }

                if (Support.oraxen.isEnabled() && OraxenItems.exists(item)) {
                    getOraxen(item);

                    return (B) this;
                }

                final ItemType itemType = PaperMethods.getItemType(item);

                if (itemType != null) {
                    this.item = itemType.createItemStack(1);
                } else {
                    try {
                        this.item = PaperMethods.fromBase64(item);
                    } catch (Exception exception) {
                        this.item = ItemType.STONE.createItemStack(1);
                    }
                }
            }
        }

        return (B) this;
    }

    @Deprecated(forRemoval = true)
    public B withType(@NotNull final String key) {
        if (key.isEmpty()) return (B) this;

        withCustomItem(key);

        if (key.contains(":")) {
            final String[] sections = key.split(":");

            String data = sections[1];

            if (data.contains("#")) {
                final String model = data.split("#")[1];

                final Optional<Number> customModelData = StringUtils.tryParseInt(model);

                if (customModelData.isPresent()) {
                    final Number number = customModelData.get();

                    data = data.replace("#" + number.intValue(), "");
                }
            }

            final Optional<Number> damage = StringUtils.tryParseInt(data);

            if (damage.isEmpty()) {
                @org.jetbrains.annotations.Nullable final PotionEffectType potionEffect = PaperMethods.getPotionEffect(data);

                final PotionBuilder potionBuilder = asPotionBuilder();

                potionBuilder.setColor(data).withPotionType(PaperMethods.getPotionType(data)).withPotionEffect(potionEffect, 1, 1);
            } else {
                setItemDamage(damage.get().intValue());
            }
        } else if (key.contains("#")) {
            final String[] sections = key.split("#");
            final String model = sections[1];

            final Optional<Number> customModelData = StringUtils.tryParseInt(model);

            customModelData.ifPresent(number -> setCustomModelData(number.intValue()));
        }

        return (B) this;
    }

    public B addEnchantment(@NotNull final String enchant, final int level) {
        if (enchant.isEmpty()) return (B) this;

        final Enchantment enchantment = PaperMethods.getEnchantment(enchant);

        if (enchantment == null) return (B) this;

        final ItemEnchantments.Builder builder = ItemEnchantments.itemEnchantments();

        if (isBook() && this.item.hasData(DataComponentTypes.STORED_ENCHANTMENTS)) {
            final ItemEnchantments enchantments = this.item.getData(DataComponentTypes.STORED_ENCHANTMENTS);

            if (enchantments != null) {
                builder.addAll(enchantments.enchantments());
            }
        } else if (this.item.hasData(DataComponentTypes.ENCHANTMENTS)) {
            final ItemEnchantments enchantments = this.item.getData(DataComponentTypes.ENCHANTMENTS);

            if (enchantments != null) {
                builder.addAll(enchantments.enchantments());
            }
        }

        builder.add(enchantment, level);

        this.item.setData(isBook() ? DataComponentTypes.STORED_ENCHANTMENTS : DataComponentTypes.ENCHANTMENTS, builder.build());

        return (B) this;
    }

    public B removeEnchantment(@NotNull final String enchant) {
        if (enchant.isEmpty()) return (B) this;

        final Enchantment enchantment = PaperMethods.getEnchantment(enchant);

        if (enchantment == null) return (B) this;

        this.item.removeEnchantment(enchantment);

        return (B) this;
    }

    public B setAmount(final int amount) {
        this.item.setAmount(Math.max(amount, 1));

        return (B) this;
    }

    public B setDisplayName(@Nullable final String displayName, final boolean isStatic) {
        this.displayName = displayName;

        this.isStatic = isStatic;

        return (B) this;
    }

    public B setDisplayName(@Nullable final String displayName) {
        return setDisplayName(displayName, false);
    }

    public String getPlainName() {
        Component component = Component.empty();

        if (this.item.hasData(DataComponentTypes.ITEM_NAME)) {
            final Component item_name = this.item.getData(DataComponentTypes.ITEM_NAME);

            if (item_name != null) {
                component = item_name;
            }
        } else if (this.item.hasData(DataComponentTypes.CUSTOM_NAME)) {
            final Component custom_name = this.item.getData(DataComponentTypes.CUSTOM_NAME);

            if (custom_name != null) {
                component = custom_name;
            }
        }

        return PlainTextComponentSerializer.plainText().serializeOr(component, "");
    }

    public B withDisplayLore(@NotNull final List<String> displayLore) {
        this.displayLore = displayLore;

        return (B) this;
    }

    public B addDisplayLore(@NotNull final String displayLore) {
        if (displayLore.isEmpty()) return (B) this;

        this.displayLore.add(displayLore);

        return (B) this;
    }

    public List<String> getPlainLore() {
        final List<String> plainLore = new ArrayList<>();

        if (this.item.hasData(DataComponentTypes.LORE)) {
            final ItemLore lore = this.item.getData(DataComponentTypes.LORE);

            if (lore != null) {
                lore.lines().forEach(line -> plainLore.add(PlainTextComponentSerializer.plainText().serialize(line)));
            }
        }

        return plainLore;
    }

    public B setEnchantGlint(final boolean enchantGlintOverride) {
        if (enchantGlintOverride && !this.item.hasData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE)) {
            this.item.setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, enchantGlintOverride);

            return (B) this;
        }

        if (this.item.hasData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE)) {
            this.item.unsetData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE);
        }

        return (B) this;
    }

    public B removeEnchantGlint() {
        if (!this.item.hasData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE)) {
            return (B) this;
        }

        this.item.unsetData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE);

        return (B) this;
    }

    public B hideToolTip() {
        if (this.item.hasData(DataComponentTypes.HIDE_TOOLTIP)) {
            return (B) this;
        }

        this.item.setData(DataComponentTypes.HIDE_TOOLTIP);

        return (B) this;
    }

    public B showToolTip() {
        if (!this.item.hasData(DataComponentTypes.HIDE_TOOLTIP)) {
            return (B) this;
        }

        this.item.unsetData(DataComponentTypes.HIDE_TOOLTIP);

        return (B) this;
    }

    public B hideAdditionalToolTip() {
        if (this.item.hasData(DataComponentTypes.HIDE_TOOLTIP)) {
            return (B) this;
        }

        this.item.setData(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP);

        return (B) this;
    }

    public B showAdditionalToolTip() {
        if (!this.item.hasData(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP)) {
            return (B) this;
        }

        this.item.unsetData(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP);

        return (B) this;
    }

    public B setUnbreakable(final boolean isUnbreakable) {
        if (isUnbreakable && !this.item.hasData(DataComponentTypes.UNBREAKABLE)) {
            this.item.setData(DataComponentTypes.UNBREAKABLE, Unbreakable.unbreakable().build());

            return (B) this;
        }

        if (this.item.hasData(DataComponentTypes.UNBREAKABLE)) {
            this.item.unsetData(DataComponentTypes.UNBREAKABLE);
        }

        return (B) this;
    }

    public B setCustomModelData(final int customModelData) {
        if (customModelData == -1) return (B) this;

        this.item.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData().addFloat(customModelData).build());

        return (B) this;
    }

    public B setItemModel(@NotNull final String itemModel) {
        if (itemModel.isEmpty()) return (B) this;

        this.item.setData(DataComponentTypes.ITEM_MODEL, NamespacedKey.minecraft(itemModel));

        return (B) this;
    }

    public B setTrim(@NotNull final String pattern, @NotNull final String material, final boolean hideToolTip) {
        if (pattern.isEmpty() || material.isEmpty()) return (B) this;

        final TrimMaterial trimMaterial = PaperMethods.getTrimMaterial(material);

        if (trimMaterial == null) {
            return (B) this;
        }

        final TrimPattern trimPattern = PaperMethods.getTrimPattern(pattern);

        if (trimPattern == null) {
            return (B) this;
        }

        final ItemArmorTrim.Builder builder = ItemArmorTrim
                .itemArmorTrim(new ArmorTrim(trimMaterial, trimPattern))
                .showInTooltip(hideToolTip);

        this.item.setData(DataComponentTypes.TRIM, builder.build());

        return (B) this;
    }

    public B setColor(@NotNull final String value) {
        if (isMap()) {
            final Color color = PaperMethods.getColor(value);

            this.item.setData(DataComponentTypes.MAP_COLOR, MapItemColor.mapItemColor().color(color).build());
        } else if (isLeather() || isPotion()) {
            final Color color = PaperMethods.getColor(value);

            this.item.setData(DataComponentTypes.DYED_COLOR, DyedItemColor.dyedItemColor().color(color).build());
        } else if (isShield()) {
            final DyeColor color = PaperMethods.getDyeColor(value);

            this.item.setData(DataComponentTypes.BASE_COLOR, color);
        }

        return (B) this;
    }

    public B setItemDamage(final int damage) {
        this.item.setData(DataComponentTypes.DAMAGE, Math.min(damage, getType().getMaxDurability()));

        return (B) this;
    }

    public B addPlaceholder(final String placeholder, final String value) {
        this.placeholders.put(placeholder, value);

        return (B) this;
    }

    public B setPlaceholders(final Map<String, String> placeholders) {
        this.placeholders = placeholders;

        return (B) this;
    }

    public boolean hasPlaceholder(final String placeholder) {
        return this.placeholders.containsKey(placeholder);
    }

    public B removePlaceholder(final String placeholder) {
        this.placeholders.remove(placeholder);

        return (B) this;
    }

    public FireworkBuilder asFireworkBuilder() {
        if (!isFirework()) {
            throw new FusionException("This item type is not a firework rocket.");
        }

        return new FireworkBuilder(this.item);
    }

    public FireworkStarBuilder asFireworkStarBuilder() {
        if (!isFireworkStar()) {
            throw new FusionException("This item type is not a firework star.");
        }

        return new FireworkStarBuilder(this.item);
    }

    public PatternBuilder asPatternBuilder() {
        if (isShield() || isBanner()) {
            return new PatternBuilder(this.item);
        }

        throw new FusionException("This item type is not a shield/banner.");
    }

    public SkullBuilder asSkullBuilder() {
        if (!isPlayerHead()) {
            throw new FusionException("This item type is not a skull.");
        }

        return new SkullBuilder(this.item);
    }

    public PotionBuilder asPotionBuilder() {
        if (!isPotion()) {
            throw new FusionException("This item type is not a potion.");
        }

        return new PotionBuilder(this.item);
    }

    public SpawnerBuilder asSpawnerBuilder() {
        if (!isSpawner()) {
            throw new FusionException("This item type is not a spawner.");
        }

        return new SpawnerBuilder(this.item);
    }

    public void setItemToInventory(final Audience audience, final Inventory inventory, final int slot, final boolean isLegacy) {
        inventory.setItem(slot, asItemStack(audience, isLegacy));
    }

    public void addItemToInventory(final Audience audience, final Inventory inventory, final boolean isLegacy) {
        inventory.addItem(asItemStack(audience, isLegacy));
    }

    public void setItemToInventory(final Audience audience, final Inventory inventory, final int slot) {
        inventory.setItem(slot, asItemStack(audience, false));
    }

    public void addItemToInventory(final Audience audience, final Inventory inventory) {
        inventory.addItem(asItemStack(audience, false));
    }

    public void setItemToInventory(final Inventory inventory, final int slot) {
        setItemToInventory(null, inventory, slot);
    }

    public void addItemToInventory(final Inventory inventory) {
        addItemToInventory(null, inventory);
    }

    public final boolean isDyeable() {
        return isTippedArrow() || isShield() || isLeather() || isMap();
    }

    public final boolean isPlayerHead() {
        return getType().equals(Material.PLAYER_HEAD);
    }

    public final boolean isFireworkStar() {
        return getType().equals(Material.FIREWORK_STAR);
    }

    public final boolean isTippedArrow() {
        return getType().equals(Material.TIPPED_ARROW);
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

    public final boolean isLeather() {
        return LEATHER_ARMOR.contains(getType());
    }

    public final boolean isPotion() {
        return POTIONS.contains(getType());
    }

    public final boolean isBanner() {
        return BANNERS.contains(getType());
    }

    public final boolean isBook() {
        return getType().equals(Material.ENCHANTED_BOOK);
    }

    public final boolean isMap() {
        return getType().equals(Material.FILLED_MAP);
    }

    protected final void setItemStack(final ItemStack item) {
        this.item = item;
    }

    protected final ItemStack getItem() {
        return this.item;
    }

    protected final Material getType() {
        return this.item.getType();
    }

    private void getItemsAdder(final String item) {
        final CustomStack builder = CustomStack.getInstance(item);

        if (builder == null) {
            throw new FusionException("The id " + item + " is not a valid ItemsAdder item!");
        }

        this.item = builder.getItemStack();
    }

    private void getOraxen(final String item) {
        final io.th0rgal.oraxen.items.ItemBuilder builder = OraxenItems.getItemById(item);

        if (builder == null) {
            throw new FusionException("The id " + item + " is not a valid Oraxen item!");
        }

        this.item = builder.build();
    }

    private void getNexo(final String item) {
        final ItemBuilder builder = NexoItems.itemFromId(item);

        if (builder == null) {
            throw new FusionException("The id " + item + " is not a valid Nexo item!");
        }

        this.item = builder.build();
    }
}