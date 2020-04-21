package net.avicus.libraries.tracker.base;

import net.avicus.libraries.tracker.DamageInfo;
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
