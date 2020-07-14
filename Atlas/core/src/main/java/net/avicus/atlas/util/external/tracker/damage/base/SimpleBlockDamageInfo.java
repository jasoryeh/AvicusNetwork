package net.avicus.atlas.util.external.tracker.damage.base;

import org.bukkit.block.BlockState;

import javax.annotation.Nonnull;

public class SimpleBlockDamageInfo extends AbstractBlockDamageInfo {

    public SimpleBlockDamageInfo(@Nonnull BlockState blockDamager) {
        super(null, blockDamager);
    }
}
