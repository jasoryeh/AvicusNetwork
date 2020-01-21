package tc.oc.tracker.damage.base;

import com.google.common.base.Preconditions;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import tc.oc.tracker.base.AbstractDamageInfo;
import tc.oc.tracker.damage.FallDamageInfo;

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
