package net.avicus.libraries.tracker.trackers;

import net.avicus.libraries.tracker.Tracker;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface OwnedMobTracker extends Tracker {

    boolean hasOwner(@Nonnull LivingEntity entity);

    @Nullable
    Player getOwner(@Nonnull LivingEntity entity);

    @Nullable
    Player setOwner(@Nonnull LivingEntity entity, @Nullable Player player);
}
