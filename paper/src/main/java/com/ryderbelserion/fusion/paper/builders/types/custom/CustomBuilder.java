package com.ryderbelserion.fusion.paper.builders.types.custom;

import com.ryderbelserion.fusion.paper.builders.BaseItemBuilder;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import java.util.Optional;

public class CustomBuilder extends BaseItemBuilder<CustomBuilder> {

    private final CustomModelData.Builder builder = CustomModelData.customModelData();

    private NamespacedKey itemModel;

    public CustomBuilder(@NotNull final ItemStack itemStack) {
        super(itemStack);

        if (this.itemStack.hasData(DataComponentTypes.CUSTOM_MODEL_DATA)) {
            final CustomModelData component = this.itemStack.getData(DataComponentTypes.CUSTOM_MODEL_DATA);

            if (component != null) this.builder.addFloats(component.floats()).addStrings(component.strings()).addFlags(component.flags()).addColors(component.colors());
        }
    }

    public @NotNull CustomBuilder setCustomModelData(final int customModelData) {
        if (customModelData == -1) return this;

        this.builder.addFloat(customModelData);

        return this;
    }

    public @NotNull CustomBuilder setCustomModelData(@NotNull final String customModelData) {
        if (customModelData.isEmpty()) return this;

        final Optional<Number> integer = this.fusion.getStringUtils().tryParseInt(customModelData);

        if (integer.isPresent()) return setCustomModelData(integer.orElse(-1).intValue());

        this.builder.addString(customModelData);

        return this;
    }

    public @NotNull CustomBuilder setItemModel(@NotNull final String namespace, @NotNull final String itemModel) {
        if (namespace.isEmpty() || itemModel.isEmpty()) return this;

        this.itemModel = new NamespacedKey(namespace, itemModel);

        return this;
    }

    public @NotNull CustomBuilder setItemModel(@NotNull final String itemModel) {
        if (itemModel.isEmpty()) return this;

        this.itemModel = NamespacedKey.minecraft(itemModel);

        return this;
    }

    @Override
    public CustomBuilder build() {
        if (this.itemModel != null) {
            this.itemStack.setData(DataComponentTypes.ITEM_MODEL, this.itemModel);

            return this;
        }

        final CustomModelData modelData = this.builder.build();

        final boolean isEmpty = modelData.colors().isEmpty() && modelData.flags().isEmpty() && modelData.floats().isEmpty() && modelData.strings().isEmpty();

        if (isEmpty) return this; // do not set if empty.

        this.itemStack.setData(DataComponentTypes.CUSTOM_MODEL_DATA, this.builder.build());

        return this;
    }
}