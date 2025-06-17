package com.ryderbelserion.fusion.paper.api.builders.items.types;

import com.ryderbelserion.fusion.paper.api.builders.items.BaseItemBuilder;
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
    private int delay = 3;
    private int range = 0;

    public @NotNull SpawnerBuilder withEntityType(@Nullable final EntityType entityType) {
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
    public @NotNull SpawnerBuilder build() {
        if (this.entityType == null) return this;

        getItem().editMeta(itemMeta -> {
            if (itemMeta instanceof BlockStateMeta state) {
                final CreatureSpawner spawner = (CreatureSpawner) state.getBlockState();

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

                // re-set to the block state
                state.setBlockState(spawner);
            }
        });

        return this;
    }
}