package net.avicus.libraries.tracker.damage.base;

import com.google.common.base.Preconditions;
import net.avicus.libraries.tracker.base.AbstractDamageInfo;
import net.avicus.libraries.tracker.damage.BlockDamageInfo;
import org.bukkit.block.BlockState;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AbstractBlockDamageInfo extends AbstractDamageInfo implements BlockDamageInfo {

    private final
    @Nonnull
    BlockState blockDamager;

    public AbstractBlockDamageInfo(@Nullable LivingEntity resolvedDamager,
                                   @Nonnull BlockState blockDamager) {
        super(resolvedDamager);

        Preconditions.checkNotNull(blockDamager, "block damager");

        this.blockDamager = blockDamager;
    }

    public
    @Nonnull
    BlockState getBlockDamager() {
        return this.blockDamager;
    }

    @Override
    public
    @Nonnull
    DamageCause getDamageCause() {
        return DamageCause.CONTACT;
    }
}
