package net.avicus.compendium.boss;

import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import net.avicus.compendium.network.Protocol;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * A boss bar manager.
 */
@RequiredArgsConstructor
public class BossBarManager implements Listener, Runnable {

    /**
     * The boss bar context.
     */
    final LegacyBossBarContext context;
    /**
     * A set of legacy boss bars subscribed to being respawned.
     */
    final Set<LegacyBossBar> legacyUpdateSubscribers = Sets.newHashSet();

    @Override
    public void run() {
        for (LegacyBossBar bar : this.legacyUpdateSubscribers) {
            bar.respawn();
        }
    }

    @EventHandler
    public void quit(final PlayerQuitEvent event) {
        this.context.remove(event.getPlayer());
    }

    /**
     * Create a boss bar.
     *
     * @param player the viewer of the boss bar
     * @return the boss bar
     */
    @Nonnull
    public BossBar create(@Nonnull final Player player) {
        final int version = Protocol.versionOf(player);

        if(version >= Protocol.V1_7.lowerEndpoint()
                && version <= Protocol.V1_8.upperEndpoint()) // 1.7-1.8.9
        {
            return new LegacyBossBar(this, player);
        } else if (version > Protocol.V1_8.lowerEndpoint()) // 1.9+
        {
            if(Protocol.hasVia()) {
                return new ModernBossBar(player);
            } else {
                throw new RuntimeException("ViaVersion is not present, yet is relied upon for boss " +
                        "bars for version that don't match the native server version. Please install " +
                        "it and try again.");
            }
        } else {
            throw new RuntimeException(
                    "Could not resolve BossBar for protocol " + version + " for " + player.getUniqueId());
        }
    }
}
