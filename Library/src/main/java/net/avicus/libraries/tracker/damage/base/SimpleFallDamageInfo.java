package net.avicus.libraries.tracker.damage.base;

import com.google.common.base.Preconditions;
import net.avicus.libraries.tracker.base.AbstractDamageInfo;
import net.avicus.libraries.tracker.damage.FallDamageInfo;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SimpleFallDamageInfo extends AbstractDamageInfo implements FallDamageInfo {

    private final float fallDistance;

    public SimpleFallDamageInfo(@Nullable LivingEntity resolvedDamager, float fallDistance) {
        super(resolvedDamager);

        Preconditions.checkArgument(fallDistance >= 0, "fall distance must be >= 0");

        this.fallDistance = fallDistance;
    }

    public float getFallDistance() {
        return this.fallDistance;
    }

    @Override
    public
    @Nonnull
    DamageCause getDamageCause() {
        return DamageCause.FALL;
    }
}
