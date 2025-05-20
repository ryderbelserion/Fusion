package com.ryderbelserion.fusion.paper.api.builders.items.modern;

import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.items.ItemBuilder;
import com.ryderbelserion.fusion.adventure.api.builders.ComponentBuilder;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.core.utils.StringUtils;
import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.fusion.paper.FusionPlugin;
import com.ryderbelserion.fusion.paper.api.builders.gui.interfaces.GuiAction;
import com.ryderbelserion.fusion.paper.api.builders.gui.interfaces.GuiItem;
import com.ryderbelserion.fusion.paper.api.builders.items.modern.types.PotionBuilder;
import com.ryderbelserion.fusion.paper.api.builders.items.modern.types.PatternBuilder;
import com.ryderbelserion.fusion.paper.api.builders.items.modern.types.SkullBuilder;
import com.ryderbelserion.fusion.paper.api.builders.items.modern.types.SpawnerBuilder;
import com.ryderbelserion.fusion.paper.api.builders.items.modern.types.fireworks.FireworkBuilder;
import com.ryderbelserion.fusion.paper.api.builders.items.modern.types.fireworks.FireworkStarBuilder;
import com.ryderbelserion.fusion.paper.api.enums.Support;
import com.ryderbelserion.fusion.paper.utils.ColorUtils;
import com.ryderbelserion.fusion.paper.utils.ItemUtils;
import dev.lone.itemsadder.api.CustomStack;
import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import io.papermc.paper.datacomponent.item.DyedItemColor;
import io.papermc.paper.datacomponent.item.ItemArmorTrim;
import io.papermc.paper.datacomponent.item.ItemEnchantments;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.datacomponent.item.MapItemColor;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import io.th0rgal.oraxen.api.OraxenItems;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public abstract class BaseItemBuilder<B extends BaseItemBuilder<B>> {

    protected final Plugin plugin = FusionPlugin.getPlugin();

    private final FusionPaper api = (FusionPaper) FusionCore.Provider.get();

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

    public @NotNull GuiItem asGuiItem(@Nullable final GuiAction<@NotNull InventoryClickEvent> action) {
        return new GuiItem(asItemStack(), action);
    }

    public @NotNull GuiItem asGuiItem() {
        return new GuiItem(asItemStack(), null);
    }

    public @NotNull GuiItem asGuiItem(@NotNull final Audience audience, @Nullable final GuiAction<@NotNull InventoryClickEvent> action) {
        return new GuiItem(asItemStack(audience), action);
    }

    public @NotNull GuiItem asGuiItem(@NotNull final Audience audience) {
        return new GuiItem(asItemStack(audience), null);
    }

    public ItemStack asItemStack(@NotNull final Audience audience) {
        if (this.displayName != null) {
            final ComponentBuilder builder = new ComponentBuilder(audience, this.displayName);

            this.item.setData(this.isStatic ? DataComponentTypes.ITEM_NAME : DataComponentTypes.CUSTOM_NAME, builder.asComponent(this.placeholders));
        }

        if (!this.displayLore.isEmpty()) {
            final ComponentBuilder builder = new ComponentBuilder(audience, this.displayLore);

            this.item.setData(DataComponentTypes.LORE, ItemLore.lore(builder.asComponents(this.placeholders)));
        }

        build();

        return this.item;
    }

    public ItemStack asItemStack() {
        return asItemStack(Audience.empty());
    }

    public B build() {
        return (B) this;
    }

    public B withCustomItem(@NotNull final String item) {
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

        this.item = type.createItemStack(Math.max(amount, 1));
        this.itemType = this.item.getType().asItemType();

        return (B) this;
    }

    public B withType(@Nullable final ItemType type) {
        return withType(type, 1);
    }

    @Deprecated(since = "0.16.0", forRemoval = true)
    public B withType(@NotNull final String key) {
        if (key.isEmpty()) return (B) this;

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
            type = sections[0];
            final String model = sections[1];

            final Optional<Number> customModelData = StringUtils.tryParseInt(model);

            customModelData.ifPresent(number -> setCustomModelData(number.intValue()));
        }

        withCustomItem(type);

        return (B) this;
    }

    public B addEnchantments(@NotNull final HashMap<String, Integer> enchantments) {
        for (final Map.Entry<String, Integer> entry : enchantments.entrySet()) {
            addEnchantment(entry.getKey(), entry.getValue());
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
        final TooltipDisplay.Builder builder = TooltipDisplay.tooltipDisplay();

        if (this.item.hasData(DataComponentTypes.TOOLTIP_DISPLAY)) {
            final TooltipDisplay display = this.item.getData(DataComponentTypes.TOOLTIP_DISPLAY);

            if (display != null) {
                builder.hideTooltip(display.hideTooltip());
                builder.hiddenComponents(display.hiddenComponents());
            }
        }

        this.item.setData(DataComponentTypes.TOOLTIP_DISPLAY, builder.hideTooltip(true).build());

        return (B) this;
    }

    public B showToolTip() {
        if (!this.item.hasData(DataComponentTypes.TOOLTIP_DISPLAY)) {
            return (B) this;
        }

        final TooltipDisplay.Builder builder = TooltipDisplay.tooltipDisplay();

        if (this.item.hasData(DataComponentTypes.TOOLTIP_DISPLAY)) {
            final TooltipDisplay display = this.item.getData(DataComponentTypes.TOOLTIP_DISPLAY);

            if (display != null) {
                builder.hideTooltip(display.hideTooltip());
                builder.hiddenComponents(display.hiddenComponents());
            }
        }

        this.item.setData(DataComponentTypes.TOOLTIP_DISPLAY, builder.hideTooltip(false).build());

        return (B) this;
    }

    public B hideComponents(final List<String> components) {
        for (final String component : components) {
            hideComponent(component);
        }

        return (B) this;
    }

    // i.e. minecraft:banner_patterns
    public B hideComponent(final String component) {
        if (component.isEmpty()) return (B) this;

        final Optional<DataComponentType> type = ItemUtils.getDataComponentType(component);

        if (type.isEmpty()) return (B) this;

        if (this.item.hasData(DataComponentTypes.TOOLTIP_DISPLAY)) {
            final TooltipDisplay display = this.item.getData(DataComponentTypes.TOOLTIP_DISPLAY);

            if (display != null) {
                display.hiddenComponents().add(type.get());
            }

            return (B) this;
        }

        final TooltipDisplay.Builder display = TooltipDisplay.tooltipDisplay();

        display.addHiddenComponents(type.get());

        this.item.setData(DataComponentTypes.TOOLTIP_DISPLAY, display);

        return (B) this;
    }

    public B setUnbreakable(final boolean isUnbreakable) {
        if (isUnbreakable && !this.item.hasData(DataComponentTypes.UNBREAKABLE)) {
            this.item.setData(DataComponentTypes.UNBREAKABLE);

            return (B) this;
        }

        if (this.item.hasData(DataComponentTypes.UNBREAKABLE)) {
            this.item.unsetData(DataComponentTypes.UNBREAKABLE);
        }

        return (B) this;
    }

    public B setCustomModelData(final int customModelData) {
        if (customModelData == -1) return (B) this;

        this.item.setData(DataComponentTypes.CUSTOM_MODEL_DATA, populateData().addFloat(customModelData).build());

        return (B) this;
    }

    public B setCustomModelData(final String customModelData) {
        if (customModelData.isEmpty()) return (B) this;

        final Optional<Number> integer = StringUtils.tryParseInt(customModelData);

        if (integer.isPresent()) {
            return setCustomModelData(integer.orElse(-1).intValue());
        }

        final CustomModelData.Builder data = populateData();

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

    public B setTrim(@NotNull final String pattern, @NotNull final String material) {
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
                .itemArmorTrim(new ArmorTrim(trimMaterial, trimPattern));

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
        if (damage == -1) return (B) this;

        this.item.setData(DataComponentTypes.DAMAGE, Math.min(damage, getType().getMaxDurability()));

        return (B) this;
    }

    public B addPlaceholder(@NotNull final String placeholder, @NotNull final String value) {
        this.placeholders.put(placeholder, value);

        return (B) this;
    }

    public B setPlaceholders(@NotNull final Map<String, String> placeholders) {
        this.placeholders = placeholders;

        return (B) this;
    }

    public boolean hasPlaceholder(@NotNull final String placeholder) {
        return this.placeholders.containsKey(placeholder);
    }

    public B removePlaceholder(@NotNull final String placeholder) {
        this.placeholders.remove(placeholder);

        return (B) this;
    }

    public B withSkull(@NotNull final String skull) {
        if (skull.isEmpty()) return (B) this;

        @NotNull final HeadDatabaseAPI hdb = this.api.getApi();

        this.item = hdb.isHead(skull) ? hdb.getItemHead(skull) : ItemType.PLAYER_HEAD.createItemStack();

        return (B) this;
    }

    public final BaseItemBuilder<B> setPersistentDouble(@NotNull final NamespacedKey key, final double value) {
        this.item.editPersistentDataContainer(container -> container.set(key, PersistentDataType.DOUBLE, value));

        return this;
    }

    public final BaseItemBuilder<B> setPersistentInteger(@NotNull final NamespacedKey key, final int value) {
        this.item.editPersistentDataContainer(container -> container.set(key, PersistentDataType.INTEGER, value));

        return this;
    }

    public final BaseItemBuilder<B> setPersistentBoolean(@NotNull final NamespacedKey key, final boolean value) {
        this.item.editPersistentDataContainer(container -> container.set(key, PersistentDataType.BOOLEAN, value));

        return this;
    }

    public final BaseItemBuilder<B> setPersistentString(@NotNull final NamespacedKey key, @NotNull final String value) {
        this.item.editPersistentDataContainer(container -> container.set(key, PersistentDataType.STRING, value));

        return this;
    }

    public final BaseItemBuilder<B> setPersistentList(@NotNull final NamespacedKey key, @NotNull final List<String> values) {
        this.item.editPersistentDataContainer(container -> container.set(key, PersistentDataType.LIST.listTypeFrom(PersistentDataType.STRING), values));

        return this;
    }

    public final boolean getBoolean(@NotNull final NamespacedKey key) {
        return this.item.getPersistentDataContainer().getOrDefault(key, PersistentDataType.BOOLEAN, false);
    }

    public final double getDouble(@NotNull final NamespacedKey key) {
        return this.item.getPersistentDataContainer().getOrDefault(key, PersistentDataType.DOUBLE, 0.0);
    }

    public final int getInteger(@NotNull final NamespacedKey key) {
        return this.item.getPersistentDataContainer().getOrDefault(key, PersistentDataType.INTEGER, 0);
    }

    public @NotNull final List<String> getList(@NotNull final NamespacedKey key) {
        return this.item.getPersistentDataContainer().getOrDefault(key, PersistentDataType.LIST.strings(), Collections.emptyList());
    }

    public @NotNull final String getString(@NotNull final NamespacedKey key) {
        return this.item.getPersistentDataContainer().getOrDefault(key, PersistentDataType.STRING, "");
    }

    public final BaseItemBuilder<B> removePersistentKey(@Nullable final NamespacedKey key) {
        if (key == null) return this;

        this.item.editPersistentDataContainer(container -> {
            if (container.has(key)) container.remove(key);
        });

        return this;
    }

    public final boolean hasKey(@NotNull final NamespacedKey key) {
        return this.item.getPersistentDataContainer().has(key);
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

    public void setItemToInventory(@Nullable final Audience audience, @NotNull final Inventory inventory, final int slot) {
        inventory.setItem(slot, asItemStack(audience));
    }

    public void addItemToInventory(@Nullable final Audience audience, @NotNull final Inventory inventory) {
        inventory.addItem(asItemStack(audience));
    }

    public void setItemToInventory(@NotNull final Inventory inventory, final int slot) {
        setItemToInventory(null, inventory, slot);
    }

    public void addItemToInventory(@NotNull final Inventory inventory) {
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

    private static final Set<String> leather_items = new HashSet<>() {{
        add(ItemType.LEATHER_HELMET.key().asString());
        add(ItemType.LEATHER_CHESTPLATE.key().asString());
        add(ItemType.LEATHER_LEGGINGS.key().asString());
        add(ItemType.LEATHER_BOOTS.key().asString());
        add(ItemType.LEATHER_HORSE_ARMOR.key().asString());
    }};

    private static final Set<String> potions = new HashSet<>() {{
        add(ItemType.POTION.key().asString());
        add(ItemType.SPLASH_POTION.key().asString());
        add(ItemType.LINGERING_POTION.key().asString());
    }};

    public final boolean isLeather() {
        return leather_items.contains(asString());
    }

    public final boolean isPotion() {
        return potions.contains(asString());
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

    protected final void setItemStack(@NotNull final ItemStack item) {
        this.item = item;
        this.itemType = this.item.getType().asItemType();
    }

    protected final ItemStack getItem() {
        return this.item;
    }

    protected final ItemType getType() {
        return this.itemType;
    }

    private CustomModelData.Builder populateData() {
        final CustomModelData.Builder data = CustomModelData.customModelData();

        if (this.item.hasData(DataComponentTypes.CUSTOM_MODEL_DATA)) {
            final CustomModelData component = this.item.getData(DataComponentTypes.CUSTOM_MODEL_DATA);

            if (component != null) {
                data.addFloats(component.floats()).addStrings(component.strings()).addFlags(component.flags()).addColors(component.colors());
            }
        }

        return data;
    }

    private void getItemsAdder(@NotNull final String item) {
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

    private void getOraxen(@NotNull final String item) {
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

    private void getNexo(@NotNull final String item) {
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

    private void setItem(@NotNull final String item) {
        final ItemType itemType = ItemUtils.getItemType(item);

        if (itemType != null) {
            withType(itemType);
        } else {
            withBase64(item);
        }
    }
}