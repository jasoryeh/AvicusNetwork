package tc.oc.tracker.base;

import org.bukkit.entity.LivingEntity;
import tc.oc.tracker.DamageInfo;

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
