package net.avicus.atlas.util.external.tracker.damage;

import net.avicus.atlas.util.external.tracker.base.AbstractDamageInfo;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class OwnedMobDamageInfo extends AbstractDamageInfo {

    protected final
    @Nullable
    Player mobOwner;
    protected final
    @Nullable
    Projectile projectile;

    public OwnedMobDamageInfo(@Nullable LivingEntity resolvedDamager, @Nullable Player mobOwner,
                              @Nullable Projectile projectile) {
        super(resolvedDamager);

        this.mobOwner = mobOwner;
        this.projectile = projectile;
    }

    public
    @Nullable
    Player getMobOwner() {
        return this.mobOwner;
    }

    public
    @Nullable
    Projectile getProjectile() {
        return this.projectile;
    }

    @Override
    public
    @Nonnull
    String toString() {
        return "OwnedMobDamageInfo{damager=" + this.resolvedDamager + ",mobOwner=" + this.mobOwner
                + ",projectile=" + this.projectile + "}";
    }

    @Override
    public
    @Nonnull
    DamageCause getDamageCause() {
        if (getProjectile() == null) {
            return DamageCause.ENTITY_ATTACK;
        } else {
            return DamageCause.PROJECTILE;
        }
    }
}
