package com.ryderbelserion.fusion.paper.scheduler;

import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public abstract class FoliaScheduler implements Runnable {

    private final JavaPlugin plugin;

    private final Scheduler type;
    private final Server server;
    private final TimeUnit timeUnit;

    private ScheduledTask task;

    public FoliaScheduler(@NotNull final JavaPlugin plugin, @NotNull final Scheduler type, @NotNull final TimeUnit timeUnit) {
        this.plugin = plugin;
        this.server = this.plugin.getServer();
        this.timeUnit = timeUnit;
        this.type = type;
    }

    public FoliaScheduler(@NotNull final JavaPlugin plugin, @NotNull final Scheduler type) {
        this(plugin, type, TimeUnit.SECONDS);
    }

    private World world;
    private int x;
    private int z;

    public FoliaScheduler(@NotNull final JavaPlugin plugin, @NotNull final World world, final int x, final int z) {
        this(plugin, Scheduler.region_scheduler, TimeUnit.SECONDS);

        this.world = world;
        this.x = x;
        this.z = z;
    }

    public FoliaScheduler(@NotNull final JavaPlugin plugin, @NotNull final Location location) {
        this(plugin, location.getWorld(), location.getBlockX() >> 4, location.getBlockZ() >> 4);
    }

    private Runnable retired;
    private Entity entity;

    public FoliaScheduler(@NotNull final JavaPlugin plugin, @Nullable final Runnable retired, @NotNull final Entity entity) {
        this(plugin, Scheduler.entity_scheduler);

        this.retired = retired;
        this.entity = entity;
    }

    public ScheduledTask runNow() throws FusionException {
        isScheduled();

        ScheduledTask task;

        switch (this.type) {
            case global_scheduler -> task = this.server.getGlobalRegionScheduler().run(this.plugin, scheduledTask -> this.run());
            case async_scheduler -> task = this.server.getAsyncScheduler().runNow(this.plugin, scheduledTask -> this.run());
            case region_scheduler -> task = this.server.getRegionScheduler().run(this.plugin, this.world, this.x, this.z, scheduledTask -> this.run());
            case entity_scheduler -> {
                if (this.entity == null) throw new FusionException("Cannot immediately run entity task if the entity is null.");

                task = this.entity.getScheduler().run(this.plugin, scheduledTask -> this.run(), this.retired);
            }

            default -> throw new FusionException("The task type is not supported!");
        }

        return this.task = task;
    }

    public boolean execute() throws FusionException {
        isScheduled();
        
        switch (this.type) {
            case global_scheduler -> this.server.getGlobalRegionScheduler().execute(this.plugin, this);
            case async_scheduler -> this.server.getAsyncScheduler().runNow(this.plugin, scheduledTask -> this.run());
            case region_scheduler -> this.server.getRegionScheduler().run(this.plugin, this.world, this.x, this.z, scheduledTask -> this.run());
            case entity_scheduler -> {
                if (this.entity == null) throw new FusionException("Cannot immediately execute entity task if the entity is null.");
                
                this.entity.getScheduler().run(this.plugin, scheduledTask -> this.run(), this.retired);
            }
            
            default -> throw new FusionException("The task type is not supported!");
        }
        
        return true;
    }

    public ScheduledTask runDelayed(final long delay) throws FusionException {
        isScheduled();

        ScheduledTask task;

        long maxDelay = Math.max(1, delay);

        switch (this.type) {
            case global_scheduler -> task = this.server.getGlobalRegionScheduler().runDelayed(this.plugin, scheduledTask -> this.run(), maxDelay);
            case async_scheduler -> task = this.server.getAsyncScheduler().runDelayed(this.plugin, scheduledTask -> this.run(), maxDelay, this.timeUnit);
            case region_scheduler -> task = this.server.getRegionScheduler().runDelayed(this.plugin, this.world, this.x, this.z, scheduledTask -> this.run(), maxDelay);
            case entity_scheduler -> {
                if (this.entity == null) throw new FusionException("Cannot run delayed entity task if the entity is null.");

                task = this.entity.getScheduler().runDelayed(this.plugin, scheduledTask -> this.run(), this.retired, maxDelay);
            }

            default -> throw new FusionException("The task type is not supported!");
        }

        return this.task = task;
    }

    public ScheduledTask runNextTick() throws FusionException {
        isScheduled();

        ScheduledTask task;
        
        switch (this.type) {
            case global_scheduler -> task = this.server.getGlobalRegionScheduler().run(this.plugin, scheduledTask -> this.run());
            case async_scheduler -> task = this.server.getAsyncScheduler().runDelayed(this.plugin, scheduledTask -> this.run(), 50, TimeUnit.MILLISECONDS);
            case region_scheduler -> task = this.server.getRegionScheduler().run(this.plugin, this.world, this.x, this.z, scheduledTask -> this.run());
            case entity_scheduler -> {
                if (this.entity == null) throw new FusionException("Cannot run delayed entity task if the entity is null.");

                task = this.entity.getScheduler().run(this.plugin, scheduledTask -> this.run(), this.retired);
            }

            default -> throw new FusionException("The task type is not supported!");
        }
        
        return this.task = task;
    }

    public ScheduledTask runAtFixedRate(final long delay, final long interval) throws FusionException {
        isScheduled();

        ScheduledTask task;

        long maxDelay = Math.max(1, delay);
        long maxInterval = Math.max(1, interval);

        switch (this.type) {
            case global_scheduler -> task = this.server.getGlobalRegionScheduler().runAtFixedRate(this.plugin, scheduledTask -> this.run(), maxDelay, maxInterval);
            case async_scheduler -> task = this.server.getAsyncScheduler().runAtFixedRate(this.plugin, scheduledTask -> this.run(), maxDelay, maxInterval, this.timeUnit);
            case region_scheduler -> task = this.server.getRegionScheduler().runAtFixedRate(this.plugin, this.world, this.x, this.z, scheduledTask -> this.run(), maxDelay, maxInterval);
            case entity_scheduler -> {
                if (this.entity == null) throw new FusionException("Cannot run fixed rate entity task if the entity is null");

                task = this.entity.getScheduler().runAtFixedRate(this.plugin, scheduledTask -> this.run(), this.retired, maxDelay, maxInterval);
            }

            default -> throw new FusionException("The task type is not supported!");
        }

        return this.task = task;
    }

    public void cancel(@Nullable final Consumer<FoliaScheduler> consumer) {
        isNotScheduled();

        this.task.cancel();

        if (consumer != null) consumer.accept(this);
    }

    public void cancel() {
        cancel(null);
    }

    @Override
    public void run() {}

    public final int getTaskId() throws FusionException {
        isNotScheduled(); // throws an exception if task is null.

        return this.task.hashCode();
    }

    public final Scheduler getType() {
        return this.type;
    }

    public final ScheduledTask getTask() {
        return this.task;
    }

    private void isNotScheduled() throws FusionException {
        if (this.task == null) throw new FusionException("The task is not yet scheduled.");
    }

    private void isScheduled() throws FusionException {
        if (this.task != null) throw new FusionException(String.format("The task is already scheduled as %s", this.task.hashCode()));
    }
}