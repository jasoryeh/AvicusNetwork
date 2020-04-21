package net.avicus.libraries.tracker.event;

import com.google.common.base.Preconditions;
import net.avicus.libraries.tracker.Damage;
import net.avicus.libraries.tracker.DamageInfo;
import net.avicus.libraries.tracker.Lifetime;
import net.avicus.libraries.tracker.base.SimpleDamage;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.joda.time.Instant;

import javax.annotation.Nonnull;

/**
 * Called when an entity undergoes some type of damage.
 */
public class EntityDamageEvent<T extends LivingEntity> extends Event implements Cancellable {

    // Bukkit event junk
    public static final HandlerList handlers = new HandlerList();
    private final
    @Nonnull
    T entity;
    private final
    @Nonnull
    Lifetime lifetime;
    private final
    @Nonnull
    Location location;
    private final
    @Nonnull
    Instant time;
    private final
    @Nonnull
    DamageInfo info;
    private int damage;
    private boolean cancelled = false;

    public EntityDamageEvent(@Nonnull T entity, @Nonnull Lifetime lifetime, int damage,
                             @Nonnull Location location, @Nonnull Instant time, @Nonnull DamageInfo info) {
        Preconditions.checkNotNull(entity, "entity");
        Preconditions.checkNotNull(lifetime, "lifetime");
        Preconditions.checkArgument(damage >= 0, "damage must be greater than or equal to zero");
        Preconditions.checkNotNull(location, "location");
        Preconditions.checkNotNull(time, "time");
        Preconditions.checkNotNull(info, "damage info");

        this.entity = entity;
        this.lifetime = lifetime;
        this.damage = damage;
        this.location = location.clone();
        this.time = time;
        this.info = info;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public
    @Nonnull
    T getEntity() {
        return this.entity;
    }

    public
    @Nonnull
    Lifetime getLifetime() {
        return this.lifetime;
    }

    public int getDamage() {
        return this.damage;
    }

    public void setDamage(int damage) {
        Preconditions.checkArgument(damage >= 0, "damage must be greater than or equal to zero");

        this.damage = damage;
    }

    public
    @Nonnull
    Location getLocation() {
        return this.location;
    }

    public
    @Nonnull
    Instant getTime() {
        return this.time;
    }

    public
    @Nonnull
    DamageInfo getInfo() {
        return this.info;
    }

    public
    @Nonnull
    Damage toDamageObject() {
        return new SimpleDamage(this.damage, this.location, this.time, this.info);
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
