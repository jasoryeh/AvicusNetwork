package net.avicus.atlas.util.external.tracker.damage.base;

import net.avicus.atlas.util.external.tracker.base.AbstractDamageInfo;
import net.avicus.atlas.util.external.tracker.damage.LavaDamageInfo;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

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
