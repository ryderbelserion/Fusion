package com.ryderbelserion.paper.builder.items.modern.types;

import com.ryderbelserion.paper.builder.items.modern.BaseItemBuilder;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SpawnerBuilder extends BaseItemBuilder<SpawnerBuilder> {

    public SpawnerBuilder(@NotNull final ItemStack itemStack) {
        super(itemStack);
    }

    private EntityType entityType;
    private int count = 0;
    private int delay = 3;
    private int range = 0;

    public void withEntityType(final EntityType entityType) {
        this.entityType = entityType;
    }

    public SpawnerBuilder withSpawnCount(final int count) {
        this.count = count;

        return this;
    }

    public SpawnerBuilder withSpawnDelay(final int delay) {
        this.delay = delay;

        return this;
    }

    public SpawnerBuilder withSpawnRange(final int range) {
        this.range = range;

        return this;
    }

    @Override
    public SpawnerBuilder build() {
        getItem().editMeta(itemMeta -> {
            if (itemMeta instanceof CreatureSpawner spawner) {
                if (count > 0) {
                    spawner.setSpawnCount(count);
                }

                if (delay > 0) {
                    spawner.setDelay(delay);
                }

                if (range > 0) {
                    spawner.setSpawnRange(range);
                }

                spawner.setSpawnedType(this.entityType);
            }
        });

        return this;
    }
}