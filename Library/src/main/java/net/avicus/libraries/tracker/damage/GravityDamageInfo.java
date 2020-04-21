package net.avicus.libraries.tracker.damage;

import com.google.common.base.Preconditions;
import net.avicus.libraries.tracker.base.AbstractDamageInfo;
import net.avicus.libraries.tracker.trackers.base.gravity.Fall;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GravityDamageInfo extends AbstractDamageInfo {

    private final
    @Nonnull
    Fall.Cause cause;
    private final
    @Nonnull
    Fall.From from;
    private final
    @Nullable
    Location fallLocation;

    public GravityDamageInfo(@Nullable LivingEntity resolvedDamager, @Nonnull Fall.Cause cause,
                             @Nonnull Fall.From from, @Nonnull Location fallLocation) {
        super(resolvedDamager);

        Preconditions.checkNotNull(resolvedDamager, "damager");
        Preconditions.checkNotNull(cause, "cause");
        Preconditions.checkNotNull(from, "from");

        this.cause = cause;
        this.from = from;
        this.fallLocation = fallLocation;
    }

    public
    @Nonnull
    Fall.Cause getCause() {
        return this.cause;
    }

    public
    @Nullable
    Fall.From getFrom() {
        return this.from;
    }

    public
    @Nonnull
    Location getFallLocation() {
        return this.fallLocation;
    }

    @Override
    public
    @Nonnull
    String toString() {
        return "GravityDamageInfo{damager=" + this.resolvedDamager + ",cause=" + this.cause + ",from="
                + this.from + "}";
    }

    @Override
    public
    @Nonnull
    DamageCause getDamageCause() {
        return DamageCause.FALL;
    }
}
