package net.avicus.libraries.tracker.damage;

import net.avicus.libraries.tracker.base.AbstractDamageInfo;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AnvilDamageInfo extends AbstractDamageInfo {

    protected final
    @Nonnull
    FallingBlock anvil;
    protected final
    @Nullable
    OfflinePlayer offlinePlayer;

    public AnvilDamageInfo(@Nullable FallingBlock anvil, @Nullable LivingEntity resolvedDamager,
                           @Nullable OfflinePlayer offlinePlayer) {
        super(resolvedDamager);

        this.anvil = anvil;
        this.offlinePlayer = offlinePlayer;
    }

    public
    @Nonnull
    FallingBlock getAnvil() {
        return this.anvil;
    }

    public
    @Nullable
    OfflinePlayer getOffinePlayer() {
        return this.offlinePlayer;
    }

    @Override
    public
    @Nonnull
    String toString() {
        return "AnvilDamageInfo{anvil=" + this.anvil + ",damager=" + this.resolvedDamager
                + ",offlinePlayer=" + this.offlinePlayer + "}";
    }

    @Override
    public
    @Nonnull
    DamageCause getDamageCause() {
        return DamageCause.FALLING_BLOCK;
    }
}
