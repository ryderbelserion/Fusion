package com.ryderbelserion.fusion.paper.api.builder.items.modern;

import com.google.common.collect.ImmutableMultimap;
import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.items.ItemBuilder;
import com.ryderbelserion.fusion.api.exceptions.FusionException;
import com.ryderbelserion.fusion.api.utils.StringUtils;
import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.api.LoggerImpl;
import com.ryderbelserion.fusion.paper.FusionPlugin;
import com.ryderbelserion.fusion.paper.api.builder.ComponentBuilder;
import com.ryderbelserion.fusion.paper.api.builder.items.modern.types.PotionBuilder;
import com.ryderbelserion.fusion.paper.api.builder.items.modern.types.PatternBuilder;
import com.ryderbelserion.fusion.paper.api.builder.items.modern.types.SkullBuilder;
import com.ryderbelserion.fusion.paper.api.builder.items.modern.types.SpawnerBuilder;
import com.ryderbelserion.fusion.paper.api.builder.items.modern.types.fireworks.FireworkBuilder;
import com.ryderbelserion.fusion.paper.api.builder.items.modern.types.fireworks.FireworkStarBuilder;
import com.ryderbelserion.fusion.paper.api.enums.Support;
import com.ryderbelserion.fusion.paper.utils.ColorUtils;
import com.ryderbelserion.fusion.paper.utils.ItemUtils;
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
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class BaseItemBuilder<B extends BaseItemBuilder<B>> {

    protected final Plugin plugin = FusionPlugin.getPlugin();

    private final FusionCore api = FusionCore.FusionProvider.get();

    private final boolean isVerbose = this.api.isVerbose();

    private final LoggerImpl logger = this.api.getLogger();

    private final List<ItemFlag> itemflags = new ArrayList<>();

    private Map<String, String> placeholders = new HashMap<>();

    private List<String> displayLore = new ArrayList<>();
    private String displayName = null;

    private boolean isStatic = false;

    private ItemType itemType;
    private ItemStack item;

    public BaseItemBuilder(final ItemStack item) {
        this.itemType = item.getType().asItemType();
        this.item = item;
    }

    public BaseItemBuilder(final String item) {
        withCustomItem(item);
    }

    public ItemStack asItemStack(@Nullable final Audience audience, final boolean isLegacy) {
        if (this.displayName != null) {
            if (isLegacy) { // legacy support for other plugins, only here temporarily
                this.item.editMeta(itemMeta -> itemMeta.setDisplayName(ColorUtils.color(withPlaceholders(audience, this.displayName))));
            } else {
                final ComponentBuilder builder = new ComponentBuilder(this.displayName);

                this.item.setData(this.isStatic ? DataComponentTypes.ITEM_NAME : DataComponentTypes.CUSTOM_NAME, builder.asComponent(audience, this.placeholders));
            }
        }

        if (!this.displayLore.isEmpty()) {
            if (isLegacy) { // legacy support for other plugins, only here temporarily
                final List<String> lines = new ArrayList<>();

                for (String line : this.displayLore) {
                    lines.add(ColorUtils.color(withPlaceholders(audience, line)));
                }

                this.item.editMeta(itemMeta -> itemMeta.setLore(lines));
            } else {
                final ComponentBuilder builder = new ComponentBuilder(this.displayLore);

                this.item.setData(DataComponentTypes.LORE, ItemLore.lore(builder.asComponents(audience, this.placeholders)));
            }
        }

        if (!this.itemflags.isEmpty()) { // temporary for now.
            this.item.editMeta(itemMeta -> this.itemflags.forEach(flag -> {
                itemMeta.addItemFlags(flag);

                if (flag == ItemFlag.HIDE_ATTRIBUTES) {
                    itemMeta.setAttributeModifiers(ImmutableMultimap.of());
                }
            }));
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

    public B withCustomItem(final String item) {
        switch (this.api.getItemsPlugin().toLowerCase()) {
            case "nexo" -> {
                if (Support.nexo.isEnabled()) {
                    getNexo(item);

                    return (B) this;
                }

                setItem(item);
            }

            case "oraxen" -> {
                if (Support.oraxen.isEnabled()) {
                    getOraxen(item);

                    return (B) this;
                }

                setItem(item);
            }

            case "itemsadder" -> {
                if (Support.items_adder.isEnabled()) {
                    getItemsAdder(item);

                    return (B) this;
                }

                setItem(item);
            }

            case "none" -> setItem(item);

            default -> {
                if (Support.nexo.isEnabled()) {
                    getNexo(item);

                    return (B) this;
                }

                if (Support.items_adder.isEnabled()) {
                    getItemsAdder(item);

                    return (B) this;
                }

                if (Support.oraxen.isEnabled()) {
                    getOraxen(item);

                    return (B) this;
                }

                setItem(item);
            }
        }

        return (B) this;
    }

    public B withBase64(@NotNull final String base64) {
        if (base64.isEmpty()) return (B) this;

        try {
            this.item = ItemUtils.fromBase64(base64);
        } catch (Exception exception) {
            this.item = ItemType.STONE.createItemStack(1);
        }

        this.itemType = this.item.getType().asItemType();

        return (B) this;
    }

    public B withType(@Nullable final ItemType type, final int amount) {
        if (type == null) {
            throw new FusionException("The item type cannot be null!");
        }

        if (this.item == null) {
            this.item = type.createItemStack(Math.max(amount, 1));
        }

        this.itemType = this.item.getType().asItemType();

        return (B) this;
    }

    public B withType(@Nullable final ItemType type) {
        return withType(type, 1);
    }

    @Deprecated(since = "0.16.0", forRemoval = true)
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
                @org.jetbrains.annotations.Nullable final PotionEffectType potionEffect = ItemUtils.getPotionEffect(data);

                final PotionBuilder potionBuilder = asPotionBuilder();

                potionBuilder.setColor(data).withPotionType(ItemUtils.getPotionType(data)).withPotionEffect(potionEffect, 1, 1);
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

        final Enchantment enchantment = ItemUtils.getEnchantment(enchant);

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

        final Enchantment enchantment = ItemUtils.getEnchantment(enchant);

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
        if (this.item.hasData(DataComponentTypes.HIDE_TOOLTIP) || this.item.hasData(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP)) {
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

    public B addItemFlag(final ItemFlag itemFlag) {
        this.itemflags.add(itemFlag);

        return (B) this;
    }

    public B addItemFlags(final List<String> flags) {
        flags.forEach(this::addItemFlag);

        return (B) this;
    }

    public B removeItemFlags(final List<String> flags) {
        flags.forEach(this::removeItemFlag);

        return (B) this;
    }

    public B addItemFlag(final String flag) {
        final ItemFlag itemFlag = getFlag(flag);

        if (itemFlag == null) {
            throw new FusionException("Flag " + flag + " is not a valid flag");
        }

        addItemFlag(itemFlag);

        return (B) this;
    }

    public B removeItemFlag(final String flag) {
        final ItemFlag itemFlag = getFlag(flag);

        if (itemFlag == null) {
            throw new FusionException("Flag " + flag + " is not a valid flag");
        }

        this.item.editMeta(itemMeta -> itemMeta.removeItemFlags(itemFlag));
        this.itemflags.remove(itemFlag);

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

        final CustomModelData.Builder data = CustomModelData.customModelData();

        if (this.item.hasData(DataComponentTypes.CUSTOM_MODEL_DATA)) {
            final CustomModelData component = this.item.getData(DataComponentTypes.CUSTOM_MODEL_DATA);

            if (component != null) {
                data.addFloats(component.floats()).addStrings(component.strings()).addFlags(component.flags()).addColors(component.colors());
            }
        }

        this.item.setData(DataComponentTypes.CUSTOM_MODEL_DATA, data.addFloat(customModelData).build());

        return (B) this;
    }

    public B setCustomModelData(final String customModelData) {
        if (customModelData.isEmpty()) return (B) this;

        final CustomModelData.Builder data = CustomModelData.customModelData();

        if (this.item.hasData(DataComponentTypes.CUSTOM_MODEL_DATA)) {
            final CustomModelData component = this.item.getData(DataComponentTypes.CUSTOM_MODEL_DATA);

            if (component != null) {
                data.addFloats(component.floats()).addStrings(component.strings()).addFlags(component.flags()).addColors(component.colors());
            }
        }

        this.item.setData(DataComponentTypes.CUSTOM_MODEL_DATA, data.addString(customModelData).build());

        return (B) this;
    }

    public B setItemModel(@NotNull final String itemModel) {
        if (itemModel.isEmpty()) return (B) this;

        this.item.setData(DataComponentTypes.ITEM_MODEL, NamespacedKey.minecraft(itemModel));

        return (B) this;
    }

    public B setItemModel(@NotNull final String namespace, @NotNull final String itemModel) {
        if (namespace.isEmpty() || itemModel.isEmpty()) return (B) this;

        this.item.setData(DataComponentTypes.ITEM_MODEL, new NamespacedKey(namespace, itemModel));

        return (B) this;
    }

    public B setTrim(@NotNull final String pattern, @NotNull final String material, final boolean hideToolTip) {
        if (pattern.isEmpty() || material.isEmpty()) return (B) this;

        final TrimMaterial trimMaterial = ItemUtils.getTrimMaterial(material);

        if (trimMaterial == null) {
            return (B) this;
        }

        final TrimPattern trimPattern = ItemUtils.getTrimPattern(pattern);

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
        if (value.isEmpty()) return (B) this;

        if (isMap()) {
            final Color color = value.contains(",") ? ColorUtils.getRGB(value) : ColorUtils.getColor(value);

            if (color != null) {
                this.item.setData(DataComponentTypes.MAP_COLOR, MapItemColor.mapItemColor().color(color).build());
            }
        } else if (isLeather() || isPotion()) {
            final Color color = value.contains(",") ? ColorUtils.getRGB(value) : ColorUtils.getColor(value);

            if (color != null) {
                this.item.setData(DataComponentTypes.DYED_COLOR, DyedItemColor.dyedItemColor().color(color).build());
            }
        } else if (isShield()) {
            final DyeColor color = ColorUtils.getDyeColor(value);

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

    public B withSkull(final String skull) {
        final HeadDatabaseAPI hdb = this.api.getHeadDatabaseAPI();

        if (skull.isEmpty() || hdb == null) return (B) this;

        this.item = hdb.isHead(skull) ? hdb.getItemHead(skull) : ItemType.PLAYER_HEAD.createItemStack();

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
        if (isPotion() || isTippedArrow()) {
            return new PotionBuilder(this.item);
        }

        throw new FusionException("This item type is not a potion / tipped arrow.");
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
        return asString().equalsIgnoreCase(ItemType.PLAYER_HEAD.key().asString());
    }

    public final boolean isFireworkStar() {
        return asString().equalsIgnoreCase(ItemType.FIREWORK_STAR.key().asString());
    }

    public final boolean isTippedArrow() {
        return asString().equalsIgnoreCase(ItemType.TIPPED_ARROW.key().asString());
    }

    public final boolean isFirework() {
        return asString().equalsIgnoreCase(ItemType.FIREWORK_ROCKET.key().asString());
    }

    public final boolean isSpawner() {
        return asString().equalsIgnoreCase(ItemType.SPAWNER.key().asString());
    }

    public final boolean isShield() {
        return asString().equalsIgnoreCase(ItemType.SHIELD.key().asString());
    }

    public final boolean isEdible() {
        return this.itemType.isEdible();
    }

    public final boolean isLeather() {
        final String id = asString();

        return id.equalsIgnoreCase(ItemType.LEATHER_HELMET.key().asString()) || id.equalsIgnoreCase(ItemType.LEATHER_CHESTPLATE.key().asString())
                || id.equalsIgnoreCase(ItemType.LEATHER_LEGGINGS.key().asString()) || id.equalsIgnoreCase(ItemType.LEATHER_BOOTS.key().asString())
                || id.equalsIgnoreCase(ItemType.LEATHER_HORSE_ARMOR.key().asString());
    }

    public final boolean isPotion() {
        final String id = asString();

        return id.equalsIgnoreCase(ItemType.POTION.key().asString()) || id.equalsIgnoreCase(ItemType.SPLASH_POTION.key().asString()) || id.equalsIgnoreCase(ItemType.LINGERING_POTION.key().asString());
    }

    public final boolean isBanner() {
        return getId().endsWith("_banner");
    }

    public final boolean isBook() {
        return asString().equalsIgnoreCase(ItemType.ENCHANTED_BOOK.key().asString());
    }

    public final boolean isMap() {
        return asString().equalsIgnoreCase(ItemType.FILLED_MAP.key().asString());
    }

    public final String getNamespace() {
        return getKey().namespace();
    }

    public final String asString() {
        return getKey().asString();
    }

    public final String getId() {
        return getKey().value();
    }

    public final Key getKey() {
        return this.itemType.key();
    }

    protected final void setItemStack(final ItemStack item) {
        this.item = item;
        this.itemType = this.item.getType().asItemType();
    }

    protected final ItemStack getItem() {
        return this.item;
    }

    protected final ItemType getType() {
        return this.itemType;
    }

    private void getItemsAdder(final String item) {
        if (!CustomStack.isInRegistry(item)) {
            return;
        }

        final CustomStack builder = CustomStack.getInstance(item);

        if (builder == null) {
            throw new FusionException("The id " + item + " is not a valid ItemsAdder item!");
        }

        this.item = builder.getItemStack();
        this.itemType = this.item.getType().asItemType();
    }

    private void getOraxen(final String item) {
        if (!OraxenItems.exists(item)) {
            return;
        }

        final io.th0rgal.oraxen.items.ItemBuilder builder = OraxenItems.getItemById(item);

        if (builder == null) {
            throw new FusionException("The id " + item + " is not a valid Oraxen item!");
        }

        this.item = builder.build();
        this.itemType = this.item.getType().asItemType();
    }

    private void getNexo(final String item) {
        if (!NexoItems.exists(item)) {
            return;
        }

        final ItemBuilder builder = NexoItems.itemFromId(item);

        if (builder == null) {
            throw new FusionException("The id " + item + " is not a valid Nexo item!");
        }

        this.item = builder.build();
        this.itemType = this.item.getType().asItemType();
    }

    private String withPlaceholders(@Nullable final Audience audience, final String line) {
        String clonedLine = line;

        for (final Map.Entry<String, String> placeholder : this.placeholders.entrySet()) {
            if (placeholder != null) {
                final String key = placeholder.getKey();
                final String value = placeholder.getValue();

                if (value != null) {
                    clonedLine = clonedLine.replace(key, value).replace(key.toLowerCase(), value);
                }
            }
        }

        if (audience instanceof Player player && Support.placeholder_api.isEnabled()) {
            clonedLine = PlaceholderAPI.setPlaceholders(player, clonedLine);
        }

        return clonedLine;
    }

    private void setItem(final String item) {
        if (item.contains(":")) {
            final String[] split = item.split(":");

            if (split.length == 2) {
                withType(ItemType.STONE);

                setItemModel(split[0], split[1]);
            } else {
                if (this.isVerbose) {
                    this.logger.warn("The value {} does not match the correct format which is namespace:id!", item);
                }
            }
        } else {
            final ItemType itemType = ItemUtils.getItemType(item);

            if (itemType != null) {
                withType(itemType);
            } else {
                withBase64(item);
            }
        }
    }

    private @Nullable ItemFlag getFlag(final String name) {
        ItemFlag flag = null;

        for (final ItemFlag value : ItemFlag.values()) {
            if (value.name().equalsIgnoreCase(name)) {
                flag = value;

                break;
            }
        }

        return flag;
    }
}