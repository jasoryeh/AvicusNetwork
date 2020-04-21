package net.avicus.libraries.tracker.trackers;

import net.avicus.libraries.tracker.Tracker;
import org.bukkit.Location;
import org.bukkit.entity.Projectile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ProjectileDistanceTracker extends Tracker {

    boolean hasLaunchLocation(@Nonnull Projectile entity);

    @Nullable
    Location getLaunchLocation(@Nonnull Projectile projectile);

    @Nullable
    Location setLaunchLocation(@Nonnull Projectile projectile, @Nullable Location location);
}
