package net.avicus.atlas.util.external.tracker.base;

import net.avicus.atlas.util.external.tracker.DamageInfo;
import org.bukkit.entity.LivingEntity;

import javax.annotation.Nullable;

public abstract class AbstractDamageInfo implements DamageInfo {

    protected final
    @Nullable
    LivingEntity resolvedDamager;

    protected AbstractDamageInfo(@Nullable LivingEntity resolvedDamager) {
        this.resolvedDamager = resolvedDamager;
    }

    public
    @Nullable
    LivingEntity getResolvedDamager() {
        return this.resolvedDamager;
    }
}
