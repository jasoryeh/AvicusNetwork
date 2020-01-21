package tc.oc.tracker.damage;

import org.bukkit.block.BlockState;
import tc.oc.tracker.DamageInfo;

import javax.annotation.Nonnull;

/**
 * Represents a damage caused by a specific block in the world.
 */
public interface BlockDamageInfo extends DamageInfo {

    /**
     * Gets the world block responsible for this damage.
     *
     * @return Snapshot of the damaging block
     */
    @Nonnull
    BlockState getBlockDamager();
}
