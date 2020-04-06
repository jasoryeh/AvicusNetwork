package net.avicus.libraries.tracker.base;

import com.google.common.base.Preconditions;
import net.avicus.libraries.tracker.Damage;
import net.avicus.libraries.tracker.DamageInfo;
import org.bukkit.Location;
import org.joda.time.Instant;

import javax.annotation.Nonnull;

public class SimpleDamage implements Damage {

    private final int damage;
    private final
    @Nonnull
    Location location;
    private final
    @Nonnull
    Instant time;
    private final
    @Nonnull
    DamageInfo info;

    public SimpleDamage(int damage, @Nonnull Location location, @Nonnull Instant time,
                        @Nonnull DamageInfo info) {
        Preconditions.checkArgument(damage >= 0, "damage must be greater than or equal to zero");
        Preconditions.checkNotNull(location, "location");
        Preconditions.checkNotNull(time, "time");
        Preconditions.checkNotNull(info, "info");

        this.damage = damage;
        this.location = location;
        this.time = time;
        this.info = info;
    }

    public int getDamage() {
        return this.damage;
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
}
