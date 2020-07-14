package net.avicus.atlas.util.external.tracker.damage;

import com.google.common.base.Preconditions;
import net.avicus.atlas.util.external.tracker.base.AbstractDamageInfo;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ProjectileDamageInfo extends AbstractDamageInfo {

    protected final
    @Nonnull
    Projectile projectile;
    protected final
    @Nullable
    Double projectileDistance;

    public ProjectileDamageInfo(@Nonnull Projectile projectile,
                                @Nullable LivingEntity resolvedDamager, @Nullable Double projectileDistance) {
        super(resolvedDamager);

        Preconditions.checkNotNull(projectile, "projectile");

        this.projectile = projectile;
        this.projectileDistance = projectileDistance;
    }

    public
    @Nonnull
    Projectile getProjectile() {
        return this.projectile;
    }

    public
    @Nullable
    Double getDistance() {
        return this.projectileDistance;
    }

    @Override
    public
    @Nonnull
    String toString() {
        return "ProjectileDamageInfo{shooter=" + this.resolvedDamager + ",projectile=" + this.projectile
                + ",distance=" + this.projectileDistance + "}";
    }


    @Override
    public
    @Nonnull
    DamageCause getDamageCause() {
        return DamageCause.PROJECTILE;
    }
}
