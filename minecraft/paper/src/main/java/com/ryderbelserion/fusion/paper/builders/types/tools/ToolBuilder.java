package com.ryderbelserion.fusion.paper.builders.types.tools;

import com.ryderbelserion.fusion.paper.builders.BaseItemBuilder;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Equippable;
import io.papermc.paper.datacomponent.item.Weapon;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import java.util.function.Consumer;

public class ToolBuilder extends BaseItemBuilder<ToolBuilder> {

    private Equippable.Builder equippable = null;
    private Weapon.Builder weapon = null;

    public ToolBuilder(@NotNull final ItemStack itemStack) {
        super(itemStack);
    }

    public void addEquipmentComponent(@NotNull final String equipmentSlot, @NotNull final Consumer<Equippable.Builder> consumer) {
        if (!isArmor()) return;

        final EquipmentSlot slot = EquipmentSlot.valueOf(equipmentSlot);

        this.equippable = Equippable.equippable(slot);

        consumer.accept(this.equippable);
    }

    public void addWeaponComponent(@NotNull final Consumer<Weapon.Builder> consumer) {
        if (!isTool()) return;

        this.weapon = Weapon.weapon();

        consumer.accept(this.weapon);
    }

    @Override
    public ToolBuilder build() {
        if (this.equippable != null) {
            this.itemStack.setData(DataComponentTypes.EQUIPPABLE, this.equippable.build());
        }

        if (this.weapon != null) {
            this.itemStack.setData(DataComponentTypes.WEAPON, this.weapon.build());
        }

        return this;
    }
}