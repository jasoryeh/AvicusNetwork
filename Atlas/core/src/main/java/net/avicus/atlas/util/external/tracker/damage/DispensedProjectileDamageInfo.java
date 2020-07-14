package net.avicus.atlas.util.external.tracker.damage;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DispensedProjectileDamageInfo extends ProjectileDamageInfo {

    protected final
    @Nullable
    OfflinePlayer dispenserOwner;

    public DispensedProjectileDamageInfo(@Nonnull Projectile projectile,
                                         @Nullable LivingEntity resolvedDamager, @Nullable Double projectileDistance,
                                         @Nullable OfflinePlayer dispenserOwner) {
        super(projectile, resolvedDamager, projectileDistance);

        this.dispenserOwner = dispenserOwner;
    }

    public
    @Nullable
    OfflinePlayer getDispenserOwner() {
        return this.dispenserOwner;
    }

    @Override
    public
    @Nonnull
    String toString() {
        return "DispensedProjectileDamageInfo{shooter=" + this.resolvedDamager + ",projectile="
                + this.projectile + ",distance=" + this.projectileDistance + ",dispenserOwner="
                + this.dispenserOwner + "}";
    }

    @Override
    public
    @Nonnull
    DamageCause getDamageCause() {
        return DamageCause.PROJECTILE;
    }
}
