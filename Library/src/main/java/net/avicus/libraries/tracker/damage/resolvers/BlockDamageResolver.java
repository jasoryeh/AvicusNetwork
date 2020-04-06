package net.avicus.libraries.tracker.damage.resolvers;

import net.avicus.libraries.tracker.DamageInfo;
import net.avicus.libraries.tracker.DamageResolver;
import net.avicus.libraries.tracker.Lifetime;
import net.avicus.libraries.tracker.damage.base.SimpleBlockDamageInfo;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockDamageResolver implements DamageResolver {

    public
    @Nullable
    DamageInfo resolve(@Nonnull LivingEntity entity, @Nonnull Lifetime lifetime,
                       @Nonnull EntityDamageEvent damageEvent) {
        if (damageEvent instanceof EntityDamageByBlockEvent
                && damageEvent.getCause() == DamageCause.CONTACT) {
            EntityDamageByBlockEvent blockEvent = (EntityDamageByBlockEvent) damageEvent;

            return new SimpleBlockDamageInfo(blockEvent.getDamager().getState());
        }

        return null;
    }
}
