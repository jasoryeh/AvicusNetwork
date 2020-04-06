package net.avicus.libraries.tracker.damage.resolvers;

import net.avicus.libraries.tracker.DamageInfo;
import net.avicus.libraries.tracker.DamageResolver;
import net.avicus.libraries.tracker.Lifetime;
import net.avicus.libraries.tracker.damage.base.SimpleMeleeDamageInfo;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MeleeDamageResolver implements DamageResolver {

    public
    @Nullable
    DamageInfo resolve(@Nonnull LivingEntity entity, @Nonnull Lifetime lifetime,
                       @Nonnull EntityDamageEvent damageEvent) {
        if (damageEvent instanceof EntityDamageByEntityEvent
                && damageEvent.getCause() == DamageCause.ENTITY_ATTACK) {
            EntityDamageByEntityEvent entityEvent = (EntityDamageByEntityEvent) damageEvent;

            if (entityEvent.getDamager() instanceof LivingEntity) {
                LivingEntity attacker = (LivingEntity) entityEvent.getDamager();

                Material weaponMaterial;
                ItemStack held = attacker.getEquipment().getItemInHand();
                if (held != null) {
                    weaponMaterial = held.getType();
                } else {
                    weaponMaterial = Material.AIR;
                }

                return new SimpleMeleeDamageInfo(attacker, held);
            }
        }

        return null;
    }
}
