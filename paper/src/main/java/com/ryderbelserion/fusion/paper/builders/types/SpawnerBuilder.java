package com.ryderbelserion.fusion.paper.builders.types;

import com.ryderbelserion.fusion.paper.builders.BaseItemBuilder;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpawnerBuilder extends BaseItemBuilder<SpawnerBuilder> {

    public SpawnerBuilder(@NotNull final ItemStack itemStack) {
        super(itemStack);
    }

    private EntityType entityType;
    private int count = 0;
    private int delay = -1;
    private int range = 0;

    public SpawnerBuilder withEntityType(@Nullable final EntityType entityType) {
        this.entityType = entityType;

        return this;
    }

    public @NotNull SpawnerBuilder withSpawnCount(final int count) {
        this.count = count;

        return this;
    }

    public @NotNull SpawnerBuilder withSpawnDelay(final int delay) {
        this.delay = delay;

        return this;
    }

    public @NotNull SpawnerBuilder withSpawnRange(final int range) {
        this.range = range;

        return this;
    }

    @Override
    public SpawnerBuilder build() {
        if (this.entityType == null) return this;

        this.itemStack.editMeta(itemMeta -> {
            if (!(itemMeta instanceof BlockStateMeta blockStateMeta)) return;
            if (!(blockStateMeta instanceof CreatureSpawner spawner)) return;

            if (this.count > 0) {
                spawner.setSpawnCount(this.count);
            }

            if (this.delay > 0) {
                spawner.setDelay(this.delay);
            }

            if (this.range > 0) {
                spawner.setSpawnRange(this.range);
            }

            spawner.setSpawnedType(this.entityType);

            blockStateMeta.setBlockState(spawner);
        });

        return this;
    }
}