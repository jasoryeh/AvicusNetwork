package net.avicus.libraries.tracker.event;

import net.avicus.libraries.tracker.DamageInfo;
import net.avicus.libraries.tracker.Lifetime;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.joda.time.Instant;

import javax.annotation.Nonnull;

public class PlayerDamageEvent extends EntityDamageEvent<Player> {

    public PlayerDamageEvent(@Nonnull Player player, @Nonnull Lifetime lifetime, int damage,
                             @Nonnull Location location, @Nonnull Instant time, @Nonnull DamageInfo info) {
        super(player, lifetime, damage, location, time, info);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
