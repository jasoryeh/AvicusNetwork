package net.avicus.libraries.tracker.damage;

import com.google.common.base.Preconditions;
import net.avicus.libraries.tracker.base.AbstractDamageInfo;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import javax.annotation.Nonnull;

public class BukkitDamageInfo extends AbstractDamageInfo {

    private final
    @Nonnull
    DamageCause cause;

    public BukkitDamageInfo(@Nonnull DamageCause cause) {
        super(null);

        Preconditions.checkNotNull(cause, "damage cause");

        this.cause = cause;
    }

    public
    @Nonnull
    DamageCause getCause() {
        return this.cause;
    }

    @Override
    public
    @Nonnull
    String toString() {
        return "BukkitDamageInfo{cause=" + this.cause + "}";
    }

    @Override
    public
    @Nonnull
    DamageCause getDamageCause() {
        return this.cause;
    }
}
