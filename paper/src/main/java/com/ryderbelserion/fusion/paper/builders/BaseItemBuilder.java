package com.ryderbelserion.fusion.paper.builders;

import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.items.ItemBuilder;
import com.ryderbelserion.fusion.core.api.enums.ItemState;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.core.api.support.ModSupport;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.fusion.paper.FusionProvider;
import com.ryderbelserion.fusion.paper.builders.types.PatternBuilder;
import com.ryderbelserion.fusion.paper.builders.types.PotionBuilder;
import com.ryderbelserion.fusion.paper.builders.types.SkullBuilder;
import com.ryderbelserion.fusion.paper.builders.types.SpawnerBuilder;
import com.ryderbelserion.fusion.paper.builders.types.fireworks.FireworkBuilder;
import com.ryderbelserion.fusion.paper.builders.types.fireworks.FireworkStarBuilder;
import com.ryderbelserion.fusion.paper.utils.ColorUtils;
import com.ryderbelserion.fusion.paper.utils.ItemUtils;
import dev.lone.itemsadder.api.CustomStack;
import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.*;
import io.papermc.paper.persistence.PersistentDataContainerView;
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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import java.util.*;
import java.util.function.Consumer;

public abstract class BaseItemBuilder<B extends BaseItemBuilder<B>> {

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

        //todo() add copper armor
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

    protected final FusionPaper fusion = FusionProvider.getInstance();
    protected ItemStack itemStack;
    protected ItemType itemType;

    private DataComponentType.Valued<Component> type = DataComponentTypes.ITEM_NAME;
    private Map<String, String> placeholders = new HashMap<>();
    private List<String> displayLore = new ArrayList<>();
    private String displayName = "";

    public BaseItemBuilder(@NotNull final ItemType itemType, final int amount, @NotNull final Consumer<BaseItemBuilder> consumer) {
        this.itemStack = itemType.createItemStack(Math.min(amount, 1));

        // bind item type.
        this.itemType = itemType;

        // apply extra data to this class.
        consumer.accept(this);
    }

    public BaseItemBuilder(@NotNull final ItemType itemType, @NotNull final Consumer<BaseItemBuilder> consumer) {
        this(itemType, 1, consumer);
    }

    public BaseItemBuilder(@NotNull final ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemType = this.itemStack.getType().asItemType();
    }

    public BaseItemBuilder(@NotNull final String itemStack) {
        setItem(itemStack);
    }

    public B build() {
        return (B) this;
    }

    public @NotNull ItemStack asItemStack(@NotNull final Audience audience) {
        if (!this.displayName.isEmpty()) {
            this.itemStack.setData(this.type, this.fusion.parse(audience, this.displayName, this.placeholders));
        }

        final List<String> lore = this.displayLore;

        if (!lore.isEmpty()) {
            final List<Component> components = new ArrayList<>(lore.size());

            lore.forEach(line -> components.add(this.fusion.parse(audience, line, placeholders)));

            this.itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(components));
        }

        build();

        return this.itemStack;
    }

    public @NotNull ItemStack asItemStack() {
        return asItemStack(Audience.empty());
    }

    public @NotNull B withDisplayName(@NotNull final String displayName, @NotNull final ItemState itemState) {
        this.displayName = displayName;

        this.type = switch (itemState) {
            case ITEM_NAME -> DataComponentTypes.ITEM_NAME;
            case CUSTOM_NAME -> DataComponentTypes.CUSTOM_NAME;
        };

        return (B) this;
    }

    public @NotNull B withDisplayName(@NotNull final String displayName) {
        return withDisplayName(displayName, ItemState.ITEM_NAME);
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

    public @NotNull B withCustomItem(@NotNull final String itemStack) {
        final String plugin = this.fusion.getConfig().getItemsPlugin();

        switch (plugin.toLowerCase()) {
            case "nexo" -> {
                if (this.fusion.isModReady(ModSupport.nexo)) {
                    getNexo(itemStack);

                    return (B) this;
                }

                setItem(itemStack);
            }

            case "oraxen" -> {
                if (this.fusion.isModReady(ModSupport.oraxen)) {
                    getOraxen(itemStack);

                    return (B) this;
                }

                setItem(itemStack);
            }

            case "itemsadder" -> {
                if (this.fusion.isModReady(ModSupport.items_adder)) {
                    getItemsAdder(itemStack);

                    return (B) this;
                }

                setItem(itemStack);
            }

            case "none" -> setItem(itemStack);

            default -> {
                if (this.fusion.isModReady(ModSupport.nexo)) {
                    getNexo(itemStack);

                    return (B) this;
                }

                if (this.fusion.isModReady(ModSupport.items_adder)) {
                    getItemsAdder(itemStack);

                    return (B) this;
                }

                if (this.fusion.isModReady(ModSupport.oraxen)) {
                    getOraxen(itemStack);

                    return (B) this;
                }

                setItem(itemStack);
            }
        }

        return (B) this;
    }

    public @NotNull B withBase64(@NotNull final String itemStack) {
        if (itemStack.isEmpty()) return (B) this;

        try {
            this.itemStack = ItemUtils.fromBase64(itemStack);
        } catch (final Exception exception) {
            final ItemType stone = ItemType.STONE;

            this.itemStack = stone.createItemStack();
            this.itemType = stone;
        }

        this.itemType = this.itemStack.getType().asItemType();

        return (B) this;
    }

    public @NotNull B withType(@NotNull final ItemType itemType, final int amount) {
        final ItemStack itemStack = itemType.createItemStack(Math.max(amount, 1));

        this.itemStack = this.itemStack == null ? itemStack : this.itemStack.withType(itemStack.getType());
        this.itemStack.setAmount(itemStack.getAmount());

        this.itemType = itemType;

        return (B) this;
    }

    public @NotNull B withType(@NotNull final ItemType itemType) {
        return withType(itemType, 1);
    }

    public @NotNull B setAmount(final int amount) {
        this.itemStack.setAmount(Math.max(amount, 1));

        return (B) this;
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

        if (isBook() && this.itemStack.hasData(DataComponentTypes.STORED_ENCHANTMENTS)) {
            final ItemEnchantments enchantments = this.itemStack.getData(DataComponentTypes.STORED_ENCHANTMENTS);

            if (enchantments != null) {
                builder.addAll(enchantments.enchantments());
            }
        } else {
            final Map<Enchantment, Integer> enchantments = this.itemStack.getEnchantments();

            if (!enchantments.isEmpty()) {
                builder.addAll(enchantments);
            }
        }

        builder.add(enchantment, level);

        this.itemStack.setData(isBook() ? DataComponentTypes.STORED_ENCHANTMENTS : DataComponentTypes.ENCHANTMENTS, builder.build());

        return (B) this;
    }

    public @NotNull B removeEnchantment(@NotNull final String enchant) {
        if (enchant.isEmpty()) return (B) this;

        final Enchantment enchantment = ItemUtils.getEnchantment(enchant);

        if (enchantment == null) return (B) this;

        this.itemStack.removeEnchantment(enchantment);

        return (B) this;
    }

    public @NotNull String getPlainName() {
        Component component = Component.empty();

        if (this.itemStack.hasData(DataComponentTypes.ITEM_NAME)) {
            final Component itemName = this.itemStack.getData(DataComponentTypes.ITEM_NAME);

            if (itemName != null) component = itemName;
        } else if (this.itemStack.hasData(DataComponentTypes.CUSTOM_NAME)) {
            final Component customName = this.itemStack.getData(DataComponentTypes.CUSTOM_NAME);

            if (customName != null) component = customName;
        }

        return PlainTextComponentSerializer.plainText().serializeOr(component, "");
    }

    public @NotNull List<String> getPlainLore() {
        final List<String> plainLore = new ArrayList<>();

        if (this.itemStack.hasData(DataComponentTypes.LORE)) {
            final ItemLore lore = this.itemStack.getData(DataComponentTypes.LORE);

            if (lore != null) lore.lines().forEach(line -> plainLore.add(PlainTextComponentSerializer.plainText().serialize(line)));
        }

        return plainLore;
    }

    public @NotNull B addEnchantGlint(final boolean isGlowing) {
        if (!isGlowing && this.itemStack.hasData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE)) {
            this.itemStack.unsetData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE);

            return (B) this;
        }

        this.itemStack.setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);

        return (B) this;
    }

    public @NotNull B removeEnchantGlint() {
        if (!this.itemStack.hasData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE)) return (B) this;

        this.itemStack.unsetData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE);

        return (B) this;
    }

    public @NotNull B hideToolTip() {
        this.itemStack.setData(DataComponentTypes.TOOLTIP_DISPLAY, builder().hideTooltip(true).build());

        return (B) this;
    }

    public @NotNull B showToolTip() {
        if (!this.itemStack.hasData(DataComponentTypes.TOOLTIP_DISPLAY)) return (B) this;

        this.itemStack.setData(DataComponentTypes.TOOLTIP_DISPLAY, builder().hideTooltip(false).build());

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

        if (this.itemStack.hasData(DataComponentTypes.TOOLTIP_DISPLAY)) {
            final TooltipDisplay components = this.itemStack.getData(DataComponentTypes.TOOLTIP_DISPLAY);

            if (components != null) display.hiddenComponents(components.hiddenComponents());
        }

        display.addHiddenComponents(type.get());

        this.itemStack.setData(DataComponentTypes.TOOLTIP_DISPLAY, display);

        return (B) this;
    }

    public @NotNull B setUnbreakable(final boolean isUnbreakable) {
        if (isUnbreakable && !this.itemStack.hasData(DataComponentTypes.UNBREAKABLE)) {
            this.itemStack.setData(DataComponentTypes.UNBREAKABLE);

            return (B) this;
        }

        if (this.itemStack.hasData(DataComponentTypes.UNBREAKABLE)) this.itemStack.unsetData(DataComponentTypes.UNBREAKABLE);

        return (B) this;
    }

    public @NotNull B setRepairCost(final int repairCost) {
        if (repairCost == -1) return (B) this;

        this.itemStack.setData(DataComponentTypes.REPAIR_COST, repairCost);

        return (B) this;
    }

    public @NotNull B setCustomModelData(final int customModelData) {
        if (customModelData == -1) return (B) this;

        this.itemStack.setData(DataComponentTypes.CUSTOM_MODEL_DATA, model().addFloat(customModelData).build());

        return (B) this;
    }

    public @NotNull B setCustomModelData(@NotNull final String customModelData) {
        if (customModelData.isEmpty()) return (B) this;

        final Optional<Number> integer = this.fusion.getStringUtils().tryParseInt(customModelData);

        if (integer.isPresent()) return setCustomModelData(integer.orElse(-1).intValue());

        final CustomModelData.Builder data = model();

        this.itemStack.setData(DataComponentTypes.CUSTOM_MODEL_DATA, data.addString(customModelData).build());

        return (B) this;
    }

    public @NotNull B setItemModel(@NotNull final String itemModel) {
        if (itemModel.isEmpty()) return (B) this;

        this.itemStack.setData(DataComponentTypes.ITEM_MODEL, NamespacedKey.minecraft(itemModel));

        return (B) this;
    }

    public @NotNull B setItemModel(@NotNull final String namespace, @NotNull final String itemModel) {
        if (namespace.isEmpty() || itemModel.isEmpty()) return (B) this;

        this.itemStack.setData(DataComponentTypes.ITEM_MODEL, new NamespacedKey(namespace, itemModel));

        return (B) this;
    }

    public @NotNull B setTrim(@NotNull final String pattern, @NotNull final String material) {
        if (pattern.isEmpty() || material.isEmpty()) return (B) this;

        final TrimMaterial trimMaterial = ItemUtils.getTrimMaterial(material);

        if (trimMaterial == null) return (B) this;

        final TrimPattern trimPattern = ItemUtils.getTrimPattern(pattern);

        if (trimPattern == null) return (B) this;

        final ItemArmorTrim.Builder builder = ItemArmorTrim.itemArmorTrim(new ArmorTrim(trimMaterial, trimPattern));

        this.itemStack.setData(DataComponentTypes.TRIM, builder.build());

        return (B) this;
    }

    public @NotNull B setColor(@NotNull final String value) {
        if (value.isEmpty()) return (B) this;

        if (isMap()) {
            final Color color = value.contains(",") ? ColorUtils.getRGB(value) : ColorUtils.getColor(value);

            if (color != null) this.itemStack.setData(DataComponentTypes.MAP_COLOR, MapItemColor.mapItemColor().color(color).build());
        } else if (isLeather()) {
            final Color color = value.contains(",") ? ColorUtils.getRGB(value) : ColorUtils.getColor(value);

            if (color != null) this.itemStack.setData(DataComponentTypes.DYED_COLOR, DyedItemColor.dyedItemColor().color(color).build());
        } else if (isShield()) {
            final DyeColor color = ColorUtils.getDyeColor(value);

            this.itemStack.setData(DataComponentTypes.BASE_COLOR, color);
        }

        return (B) this;
    }

    public @NotNull B setItemDamage(final int damage) {
        if (damage == -1) return (B) this;

        this.itemStack.setData(DataComponentTypes.DAMAGE, Math.min(damage, this.itemType.getMaxDurability()));

        return (B) this;
    }

    public @NotNull B withSkull(@NotNull final String skull) {
        if (skull.isEmpty()) return (B) this;

        final Optional<HeadDatabaseAPI> key = this.fusion.getHeadDatabaseAPI();

        ItemStack itemStack = ItemType.PLAYER_HEAD.createItemStack();

        if (key.isPresent()) {
            final HeadDatabaseAPI api = key.get();

            if (api.isHead(skull)) itemStack = api.getItemHead(skull);
        }

        this.itemStack = itemStack;

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

    public @NotNull B removePlaceholder(@NotNull final String placeholder) {
        this.placeholders.remove(placeholder);

        return (B) this;
    }

    public boolean hasPlaceholder(@NotNull final String placeholder) {
        return this.placeholders.containsKey(placeholder);
    }

    public @NotNull final B setPersistentDouble(@NotNull final NamespacedKey key, final double value) {
        this.itemStack.editPersistentDataContainer(container -> container.set(key, PersistentDataType.DOUBLE, value));

        return (B) this;
    }

    public @NotNull final B setPersistentInteger(@NotNull final NamespacedKey key, final int value) {
        this.itemStack.editPersistentDataContainer(container -> container.set(key, PersistentDataType.INTEGER, value));

        return (B) this;
    }

    public @NotNull final B setPersistentBoolean(@NotNull final NamespacedKey key, final boolean value) {
        this.itemStack.editPersistentDataContainer(container -> container.set(key, PersistentDataType.BOOLEAN, value));

        return (B) this;
    }

    public @NotNull final B setPersistentString(@NotNull final NamespacedKey key, @NotNull final String value) {
        this.itemStack.editPersistentDataContainer(container -> container.set(key, PersistentDataType.STRING, value));

        return (B) this;
    }

    public @NotNull final B setPersistentList(@NotNull final NamespacedKey key, @NotNull final List<String> values) {
        this.itemStack.editPersistentDataContainer(container -> container.set(key, PersistentDataType.LIST.listTypeFrom(PersistentDataType.STRING), values));

        return (B) this;
    }

    public final boolean getBoolean(@NotNull final NamespacedKey key) {
        return getContainer().getOrDefault(key, PersistentDataType.BOOLEAN, false);
    }

    public final double getDouble(@NotNull final NamespacedKey key) {
        return getContainer().getOrDefault(key, PersistentDataType.DOUBLE, 0.0);
    }

    public final int getInteger(@NotNull final NamespacedKey key) {
        return getContainer().getOrDefault(key, PersistentDataType.INTEGER, 0);
    }

    public @NotNull final List<String> getList(@NotNull final NamespacedKey key) {
        return getContainer().getOrDefault(key, PersistentDataType.LIST.strings(), Collections.emptyList());
    }

    public @NotNull final String getString(@NotNull final NamespacedKey key) {
        return getContainer().getOrDefault(key, PersistentDataType.STRING, "");
    }

    public @NotNull final B removePersistentKey(@Nullable final NamespacedKey key) {
        if (key == null) return (B) this;

        this.itemStack.editPersistentDataContainer(container -> {
            if (container.has(key)) container.remove(key);
        });

        return (B) this;
    }

    public final boolean hasKey(@NotNull final NamespacedKey key) {
        return getContainer().has(key);
    }

    public @NotNull final PersistentDataContainerView getContainer() {
        return this.itemStack.getPersistentDataContainer();
    }

    public @NotNull final FireworkBuilder asFireworkBuilder() {
        if (!isFirework()) throw new FusionException("This item type is not a firework rocket.");

        return new FireworkBuilder(this.itemStack);
    }

    public @NotNull final FireworkStarBuilder asFireworkStarBuilder() {
        if (!isFireworkStar()) throw new FusionException("This item type is not a firework star.");

        return new FireworkStarBuilder(this.itemStack);
    }

    public @NotNull final PatternBuilder asPatternBuilder() {
        if (isShield() || isBanner()) return new PatternBuilder(this.itemStack);

        throw new FusionException("This item type is not a shield/banner.");
    }

    public @NotNull final SkullBuilder asSkullBuilder() {
        if (!isPlayerHead()) throw new FusionException("This item type is not a skull.");

        return new SkullBuilder(this.itemStack);
    }

    public @NotNull final PotionBuilder asPotionBuilder() {
        if (isPotion() || isTippedArrow()) return new PotionBuilder(this.itemStack);

        throw new FusionException("This item type is not a potion / tipped arrow.");
    }

    public @NotNull final SpawnerBuilder asSpawnerBuilder() {
        if (!isSpawner()) throw new FusionException("This item type is not a spawner.");

        return new SpawnerBuilder(this.itemStack);
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

    public @NotNull final String getTranslationKey() {
        return this.itemType.translationKey();
    }

    public @NotNull final ItemType getType() {
        return this.itemType;
    }

    public @NotNull final Key getKey() {
        return this.itemType.key();
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

    private @NotNull CustomModelData.Builder model() {
        final CustomModelData.Builder data = CustomModelData.customModelData();

        if (this.itemStack.hasData(DataComponentTypes.CUSTOM_MODEL_DATA)) {
            final CustomModelData component = this.itemStack.getData(DataComponentTypes.CUSTOM_MODEL_DATA);

            if (component != null) data.addFloats(component.floats()).addStrings(component.strings()).addFlags(component.flags()).addColors(component.colors());
        }

        return data;
    }

    private @NotNull TooltipDisplay.Builder builder() {
        final TooltipDisplay.Builder builder = TooltipDisplay.tooltipDisplay();

        if (this.itemStack.hasData(DataComponentTypes.TOOLTIP_DISPLAY)) {
            final TooltipDisplay display = this.itemStack.getData(DataComponentTypes.TOOLTIP_DISPLAY);

            if (display != null) {
                builder.hideTooltip(display.hideTooltip());
                builder.hiddenComponents(display.hiddenComponents());
            }
        }

        return builder;
    }

    private void getItemsAdder(@NotNull final String itemStack) {
        if (!CustomStack.isInRegistry(itemStack)) {
            this.fusion.log("warn", "The id " + itemStack + " does not exist as an ItemsAdder item!");

            return;
        }

        final CustomStack builder = CustomStack.getInstance(itemStack);

        if (builder == null) throw new FusionException("The id " + itemStack + " is not a valid ItemsAdder item!");

        this.itemStack = builder.getItemStack();
        this.itemType = this.itemStack.getType().asItemType();
    }

    private void getOraxen(@NotNull final String itemStack) {
        if (!OraxenItems.exists(itemStack)) {
            this.fusion.log("warn", "The id " + itemStack + " does not exist as an Oraxen item!");

            return;
        }

        final io.th0rgal.oraxen.items.ItemBuilder builder = OraxenItems.getItemById(itemStack);

        if (builder == null) throw new FusionException("The id " + itemStack + " is not a valid Oraxen item!");

        this.itemStack = builder.build();
        this.itemType = this.itemStack.getType().asItemType();
    }

    private void getNexo(@NotNull final String itemStack) {
        if (!NexoItems.exists(itemStack)) {
            this.fusion.log("warn", "The id " + itemStack + " does not exist as a Nexo item!");

            return;
        }

        final ItemBuilder builder = NexoItems.itemFromId(itemStack);

        if (builder == null) throw new FusionException("The id " + itemStack + " is not a valid Nexo item!");

        this.itemStack = builder.build();
        this.itemType = this.itemStack.getType().asItemType();
    }

    private void setItem(@NotNull final String itemStack) {
        final ItemType itemType = ItemUtils.getItemType(itemStack);

        if (itemType == null) {
            withBase64(itemStack);

            return;
        }

        withType(itemType);
    }
}