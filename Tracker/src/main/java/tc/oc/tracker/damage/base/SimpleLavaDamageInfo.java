package tc.oc.tracker.damage.base;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import tc.oc.tracker.base.AbstractDamageInfo;
import tc.oc.tracker.damage.LavaDamageInfo;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SimpleLavaDamageInfo extends AbstractDamageInfo implements LavaDamageInfo {

    public SimpleLavaDamageInfo(@Nullable LivingEntity resolvedDamager) {
        super(resolvedDamager);
    }

    @Override
    public
    @Nonnull
    DamageCause getDamageCause() {
        return DamageCause.LAVA;
    }
}
