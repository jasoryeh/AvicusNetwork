package net.avicus.libraries.tracker.damage.base;

import net.avicus.libraries.tracker.base.AbstractDamageInfo;
import net.avicus.libraries.tracker.damage.LavaDamageInfo;
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
