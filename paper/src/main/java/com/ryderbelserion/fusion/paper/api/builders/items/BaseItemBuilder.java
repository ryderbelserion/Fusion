package com.ryderbelserion.fusion.paper.api.builders.items;

import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.items.ItemBuilder;
import com.ryderbelserion.fusion.core.FusionProvider;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.core.api.utils.StringUtils;
import com.ryderbelserion.fusion.core.api.enums.Support;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.fusion.paper.api.builders.gui.interfaces.GuiAction;
import com.ryderbelserion.fusion.paper.api.builders.gui.interfaces.GuiItem;
import com.ryderbelserion.fusion.paper.api.builders.items.types.PotionBuilder;
import com.ryderbelserion.fusion.paper.api.builders.items.types.PatternBuilder;
import com.ryderbelserion.fusion.paper.api.builders.items.types.SkullBuilder;
import com.ryderbelserion.fusion.paper.api.builders.items.types.SpawnerBuilder;
import com.ryderbelserion.fusion.paper.api.builders.items.types.fireworks.FireworkBuilder;
import com.ryderbelserion.fusion.paper.api.builders.items.types.fireworks.FireworkStarBuilder;
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

    protected final FusionPaper fusion = (FusionPaper) FusionProvider.get();

    private Map<String, String> placeholders = new HashMap<>();

    private List<String> displayLore = new ArrayList<>();
    private String displayName = null;

    private boolean isStatic = false;

    private ItemType itemType;
    private ItemStack item;

    public BaseItemBuilder(@NotNull final ItemStack item) {
        this.itemType = item.getType().asItemType();
        this.item = item;
    }

    public BaseItemBuilder(@NotNull final String item) {
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

    public @NotNull ItemStack asItemStack(@NotNull final Audience audience) {
        if (this.displayName != null) {
            final Component component = this.fusion.color(audience, this.displayName, this.placeholders);

            this.item.setData(this.isStatic ? DataComponentTypes.ITEM_NAME : DataComponentTypes.CUSTOM_NAME, component);
        }

        final List<String> lore = this.displayLore;

        if (!lore.isEmpty()) {
            final List<Component> components = new ArrayList<>(lore.size());

            lore.forEach(line -> components.add(this.fusion.color(audience, line, placeholders)));

            this.item.setData(DataComponentTypes.LORE, ItemLore.lore(components));
        }

        build();

        return this.item;
    }

    public @NotNull ItemStack asItemStack() {
        return asItemStack(Audience.empty());
    }

    public @NotNull B build() {
        return (B) this;
    }

    public @NotNull B withCustomItem(@NotNull final String item) {
        switch (this.fusion.getItemsPlugin().toLowerCase()) {
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

    public @NotNull B withBase64(@NotNull final String base64) {
        if (base64.isEmpty()) return (B) this;

        try {
            this.item = ItemUtils.fromBase64(base64);
        } catch (final Exception exception) {
            this.item = ItemType.STONE.createItemStack(1);
        }

        this.itemType = this.item.getType().asItemType();

        return (B) this;
    }

    public @NotNull B withType(@Nullable final ItemType type, final int amount) {
        if (type == null) {
            throw new FusionException("The item type cannot be null!");
        }

        final ItemStack itemStack = type.createItemStack(Math.max(amount, 1));

        setItemStack(this.item == null ? itemStack : this.item.withType(itemStack.getType()));

        this.item.setAmount(itemStack.getAmount());

        return (B) this;
    }

    public @NotNull B withType(@Nullable final ItemType type) {
        return withType(type, 1);
    }

    public @NotNull B addEnchantments(@NotNull final Map<String, Integer> enchantments) {
        for (final Map.Entry<String, Integer> entry : enchantments.entrySet()) {
            addEnchantment(entry.getKey(), entry.getValue());
        }

        return (B) this;
    }

    public @NotNull B addEnchantment(@NotNull final String enchant, final int level) {
        if (enchant.isEmpty()) return (B) this;

        final Enchantment enchantment = ItemUtils.getEnchantment(enchant);

        if (enchantment == null) return (B) this;

        final ItemEnchantments.Builder builder = ItemEnchantments.itemEnchantments();

        if (isBook() && this.item.hasData(DataComponentTypes.STORED_ENCHANTMENTS)) {
            final ItemEnchantments enchantments = this.item.getData(DataComponentTypes.STORED_ENCHANTMENTS);

            if (enchantments != null) {
                builder.addAll(enchantments.enchantments());
            }
        } else {
            final Map<Enchantment, Integer> enchantments = this.item.getEnchantments();

            if (!enchantments.isEmpty()) {
                builder.addAll(enchantments);
            }
        }

        builder.add(enchantment, level);

        this.item.setData(isBook() ? DataComponentTypes.STORED_ENCHANTMENTS : DataComponentTypes.ENCHANTMENTS, builder.build());

        return (B) this;
    }

    public @NotNull B removeEnchantment(@NotNull final String enchant) {
        if (enchant.isEmpty()) return (B) this;

        final Enchantment enchantment = ItemUtils.getEnchantment(enchant);

        if (enchantment == null) return (B) this;

        this.item.removeEnchantment(enchantment);

        return (B) this;
    }

    public @NotNull B setAmount(final int amount) {
        this.item.setAmount(Math.max(amount, 1));

        return (B) this;
    }

    public @NotNull B setDisplayName(@Nullable final String displayName, final boolean isStatic) {
        this.displayName = displayName;

        this.isStatic = isStatic;

        return (B) this;
    }

    public @NotNull B setDisplayName(@Nullable final String displayName) {
        return setDisplayName(displayName, false);
    }

    public @NotNull String getPlainName() {
        Component component = Component.empty();

        if (this.item.hasData(DataComponentTypes.ITEM_NAME)) {
            final Component itemName = this.item.getData(DataComponentTypes.ITEM_NAME);

            if (itemName != null) {
                component = itemName;
            }
        } else if (this.item.hasData(DataComponentTypes.CUSTOM_NAME)) {
            final Component customName = this.item.getData(DataComponentTypes.CUSTOM_NAME);

            if (customName != null) {
                component = customName;
            }
        }

        return PlainTextComponentSerializer.plainText().serializeOr(component, "");
    }

    public @NotNull B withDisplayLore(@NotNull final List<String> displayLore) {
        this.displayLore = displayLore;

        return (B) this;
    }

    public @NotNull B addDisplayLore(@NotNull final String displayLore) {
        if (displayLore.isEmpty()) return (B) this;

        this.displayLore.add(displayLore);

        return (B) this;
    }

    public @NotNull List<String> getPlainLore() {
        final List<String> plainLore = new ArrayList<>();

        if (this.item.hasData(DataComponentTypes.LORE)) {
            final ItemLore lore = this.item.getData(DataComponentTypes.LORE);

            if (lore != null) {
                lore.lines().forEach(line -> plainLore.add(PlainTextComponentSerializer.plainText().serialize(line)));
            }
        }

        return plainLore;
    }

    public @NotNull B setEnchantGlint(final boolean isGlowing) {
        if (isGlowing && !this.item.hasData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE)) {
            this.item.setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);

            return (B) this;
        }

        if (this.item.hasData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE)) {
            this.item.unsetData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE);
        }

        return (B) this;
    }

    public @NotNull B removeEnchantGlint() {
        if (!this.item.hasData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE)) {
            return (B) this;
        }

        this.item.unsetData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE);

        return (B) this;
    }

    public @NotNull B hideToolTip() {
        this.item.setData(DataComponentTypes.TOOLTIP_DISPLAY, builder().hideTooltip(true).build());

        return (B) this;
    }

    public @NotNull B showToolTip() {
        if (!this.item.hasData(DataComponentTypes.TOOLTIP_DISPLAY)) {
            return (B) this;
        }

        this.item.setData(DataComponentTypes.TOOLTIP_DISPLAY, builder().hideTooltip(false).build());

        return (B) this;
    }

    public @NotNull B hideComponents(@NotNull final List<String> components) {
        for (final String component : components) {
            hideComponent(component);
        }

        return (B) this;
    }

    // i.e. minecraft:banner_patterns without minecraft
    public @NotNull B hideComponent(@NotNull final String component) {
        if (component.isEmpty()) return (B) this;

        final Optional<DataComponentType> type = ItemUtils.getDataComponentType(component);

        if (type.isEmpty()) return (B) this;

        final TooltipDisplay.Builder display = TooltipDisplay.tooltipDisplay();

        if (this.item.hasData(DataComponentTypes.TOOLTIP_DISPLAY)) {
            final TooltipDisplay components = this.item.getData(DataComponentTypes.TOOLTIP_DISPLAY);

            if (components != null) {
                display.hiddenComponents(components.hiddenComponents());
            }
        }

        display.addHiddenComponents(type.get());

        this.item.setData(DataComponentTypes.TOOLTIP_DISPLAY, display);

        return (B) this;
    }

    public @NotNull B setUnbreakable(final boolean isUnbreakable) {
        if (isUnbreakable && !this.item.hasData(DataComponentTypes.UNBREAKABLE)) {
            this.item.setData(DataComponentTypes.UNBREAKABLE);

            return (B) this;
        }

        if (this.item.hasData(DataComponentTypes.UNBREAKABLE)) {
            this.item.unsetData(DataComponentTypes.UNBREAKABLE);
        }

        return (B) this;
    }

    public @NotNull B setCustomModelData(final int customModelData) {
        if (customModelData == -1) return (B) this;

        this.item.setData(DataComponentTypes.CUSTOM_MODEL_DATA, populateData().addFloat(customModelData).build());

        return (B) this;
    }

    public @NotNull B setCustomModelData(@NotNull final String customModelData) {
        if (customModelData.isEmpty()) return (B) this;

        final Optional<Number> integer = StringUtils.tryParseInt(customModelData);

        if (integer.isPresent()) {
            return setCustomModelData(integer.orElse(-1).intValue());
        }

        final CustomModelData.Builder data = populateData();

        this.item.setData(DataComponentTypes.CUSTOM_MODEL_DATA, data.addString(customModelData).build());

        return (B) this;
    }

    public @NotNull B setItemModel(@NotNull final String itemModel) {
        if (itemModel.isEmpty()) return (B) this;

        this.item.setData(DataComponentTypes.ITEM_MODEL, NamespacedKey.minecraft(itemModel));

        return (B) this;
    }

    public @NotNull B setItemModel(@NotNull final String namespace, @NotNull final String itemModel) {
        if (namespace.isEmpty() || itemModel.isEmpty()) return (B) this;

        this.item.setData(DataComponentTypes.ITEM_MODEL, new NamespacedKey(namespace, itemModel));

        return (B) this;
    }

    public @NotNull B setTrim(@NotNull final String pattern, @NotNull final String material) {
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

    public @NotNull B setColor(@NotNull final String value) {
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

    public @NotNull B setItemDamage(final int damage) {
        if (damage == -1) return (B) this;

        this.item.setData(DataComponentTypes.DAMAGE, Math.min(damage, getType().getMaxDurability()));

        return (B) this;
    }

    public @NotNull B addPlaceholder(@NotNull final String placeholder, @NotNull final String value) {
        this.placeholders.put(placeholder, value);

        return (B) this;
    }

    public @NotNull B setPlaceholders(@NotNull final Map<String, String> placeholders) {
        this.placeholders = placeholders;

        return (B) this;
    }

    public boolean hasPlaceholder(@NotNull final String placeholder) {
        return this.placeholders.containsKey(placeholder);
    }

    public @NotNull B removePlaceholder(@NotNull final String placeholder) {
        this.placeholders.remove(placeholder);

        return (B) this;
    }

    public @NotNull B withSkull(@NotNull final String skull) {
        if (skull.isEmpty()) return (B) this;

        @NotNull final Optional<HeadDatabaseAPI> key = this.fusion.getHeadDatabaseAPI();

        ItemStack item = ItemType.PLAYER_HEAD.createItemStack();

        if (key.isPresent()) {
            @NotNull final HeadDatabaseAPI api = key.get();

            if (api.isHead(skull)) {
                item = api.getItemHead(skull);
            }
        }

        this.item = item;

        return (B) this;
    }

    public @NotNull final B setPersistentDouble(@NotNull final NamespacedKey key, final double value) {
        this.item.editPersistentDataContainer(container -> container.set(key, PersistentDataType.DOUBLE, value));

        return (B) this;
    }

    public @NotNull final B setPersistentInteger(@NotNull final NamespacedKey key, final int value) {
        this.item.editPersistentDataContainer(container -> container.set(key, PersistentDataType.INTEGER, value));

        return (B) this;
    }

    public @NotNull final B setPersistentBoolean(@NotNull final NamespacedKey key, final boolean value) {
        this.item.editPersistentDataContainer(container -> container.set(key, PersistentDataType.BOOLEAN, value));

        return (B) this;
    }

    public @NotNull final B setPersistentString(@NotNull final NamespacedKey key, @NotNull final String value) {
        this.item.editPersistentDataContainer(container -> container.set(key, PersistentDataType.STRING, value));

        return (B) this;
    }

    public @NotNull final B setPersistentList(@NotNull final NamespacedKey key, @NotNull final List<String> values) {
        this.item.editPersistentDataContainer(container -> container.set(key, PersistentDataType.LIST.listTypeFrom(PersistentDataType.STRING), values));

        return (B) this;
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

    public @NotNull final B removePersistentKey(@Nullable final NamespacedKey key) {
        if (key == null) return (B) this;

        this.item.editPersistentDataContainer(container -> {
            if (container.has(key)) container.remove(key);
        });

        return (B) this;
    }

    public final boolean hasKey(@NotNull final NamespacedKey key) {
        return this.item.getPersistentDataContainer().has(key);
    }

    public @NotNull final FireworkBuilder asFireworkBuilder() {
        if (!isFirework()) {
            throw new FusionException("This item type is not a firework rocket.");
        }

        return new FireworkBuilder(this.item);
    }

    public @NotNull final FireworkStarBuilder asFireworkStarBuilder() {
        if (!isFireworkStar()) {
            throw new FusionException("This item type is not a firework star.");
        }

        return new FireworkStarBuilder(this.item);
    }

    public @NotNull final PatternBuilder asPatternBuilder() {
        if (isShield() || isBanner()) {
            return new PatternBuilder(this.item);
        }

        throw new FusionException("This item type is not a shield/banner.");
    }

    public @NotNull final SkullBuilder asSkullBuilder() {
        if (!isPlayerHead()) {
            throw new FusionException("This item type is not a skull.");
        }

        return new SkullBuilder(this.item);
    }

    public @NotNull final PotionBuilder asPotionBuilder() {
        if (isPotion() || isTippedArrow()) {
            return new PotionBuilder(this.item);
        }

        throw new FusionException("This item type is not a potion / tipped arrow.");
    }

    public @NotNull final SpawnerBuilder asSpawnerBuilder() {
        if (!isSpawner()) {
            throw new FusionException("This item type is not a spawner.");
        }

        return new SpawnerBuilder(this.item);
    }

    public void setItemToInventory(@NotNull final Audience audience, @NotNull final Inventory inventory, final int slot) {
        inventory.setItem(slot, asItemStack(audience));
    }

    public void addItemToInventory(@NotNull final Audience audience, @NotNull final Inventory inventory) {
        inventory.addItem(asItemStack(audience));
    }

    public void setItemToInventory(@NotNull final Inventory inventory, final int slot) {
        setItemToInventory(Audience.empty(), inventory, slot);
    }

    public void addItemToInventory(@NotNull final Inventory inventory) {
        addItemToInventory(Audience.empty(), inventory);
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

    public final boolean isArmor() {
        return armor.contains(asString());
    }

    private static final Set<String> armor = new HashSet<>() {{
        add(ItemType.LEATHER_HELMET.key().asString());
        add(ItemType.LEATHER_CHESTPLATE.key().asString());
        add(ItemType.LEATHER_LEGGINGS.key().asString());
        add(ItemType.LEATHER_BOOTS.key().asString());
        add(ItemType.LEATHER_HORSE_ARMOR.key().asString());

        add(ItemType.CHAINMAIL_HELMET.key().asString());
        add(ItemType.CHAINMAIL_CHESTPLATE.key().asString());
        add(ItemType.CHAINMAIL_LEGGINGS.key().asString());
        add(ItemType.CHAINMAIL_BOOTS.key().asString());

        add(ItemType.IRON_HELMET.key().asString());
        add(ItemType.IRON_CHESTPLATE.key().asString());
        add(ItemType.IRON_LEGGINGS.key().asString());
        add(ItemType.IRON_BOOTS.key().asString());

        add(ItemType.DIAMOND_HELMET.key().asString());
        add(ItemType.DIAMOND_CHESTPLATE.key().asString());
        add(ItemType.DIAMOND_LEGGINGS.key().asString());
        add(ItemType.DIAMOND_BOOTS.key().asString());

        add(ItemType.GOLDEN_HELMET.key().asString());
        add(ItemType.GOLDEN_CHESTPLATE.key().asString());
        add(ItemType.GOLDEN_LEGGINGS.key().asString());
        add(ItemType.GOLDEN_BOOTS.key().asString());

        add(ItemType.NETHERITE_HELMET.key().asString());
        add(ItemType.NETHERITE_CHESTPLATE.key().asString());
        add(ItemType.NETHERITE_LEGGINGS.key().asString());
        add(ItemType.NETHERITE_BOOTS.key().asString());

        add(ItemType.TURTLE_HELMET.key().asString());
    }};

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

    public @NotNull final String getTranslationKey() {
        return this.itemType.translationKey();
    }

    public @NotNull final Key getKey() {
        return this.itemType.key();
    }

    protected final void setItemStack(@NotNull final ItemStack item) {
        this.item = item;
        this.itemType = this.item.getType().asItemType();
    }

    protected @NotNull final ItemStack getItem() {
        return this.item;
    }

    protected @NotNull final ItemType getType() {
        return this.itemType;
    }

    private @NotNull CustomModelData.Builder populateData() {
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

        setItemStack(builder.getItemStack());
    }

    private void getOraxen(@NotNull final String item) {
        if (!OraxenItems.exists(item)) {
            return;
        }

        final io.th0rgal.oraxen.items.ItemBuilder builder = OraxenItems.getItemById(item);

        if (builder == null) {
            throw new FusionException("The id " + item + " is not a valid Oraxen item!");
        }

        setItemStack(builder.build());
    }

    private void getNexo(@NotNull final String item) {
        if (!NexoItems.exists(item)) {
            return;
        }

        final ItemBuilder builder = NexoItems.itemFromId(item);

        if (builder == null) {
            throw new FusionException("The id " + item + " is not a valid Nexo item!");
        }

        setItemStack(builder.build());
    }

    private @NotNull TooltipDisplay.Builder builder() {
        final TooltipDisplay.Builder builder = TooltipDisplay.tooltipDisplay();

        if (this.item.hasData(DataComponentTypes.TOOLTIP_DISPLAY)) {
            final TooltipDisplay display = this.item.getData(DataComponentTypes.TOOLTIP_DISPLAY);

            if (display != null) {
                builder.hideTooltip(display.hideTooltip());
                builder.hiddenComponents(display.hiddenComponents());
            }
        }

        return builder;
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