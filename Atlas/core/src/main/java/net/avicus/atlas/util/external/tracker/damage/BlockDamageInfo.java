package net.avicus.atlas.util.external.tracker.damage;

import net.avicus.atlas.util.external.tracker.DamageInfo;
import org.bukkit.block.BlockState;

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
