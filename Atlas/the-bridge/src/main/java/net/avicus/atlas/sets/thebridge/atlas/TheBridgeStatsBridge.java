package net.avicus.atlas.sets.thebridge.atlas;

import net.avicus.atlas.module.ModuleBridge;
import net.avicus.atlas.module.stats.StatsModule;
import net.avicus.atlas.util.Events;
import org.bukkit.event.Listener;

// TODO: Store actions for potentially MVP calculations?
public class TheBridgeStatsBridge implements ModuleBridge<StatsModule>, Listener {

    private final StatsModule module;

    public TheBridgeStatsBridge(StatsModule module) {
        this.module = module;
    }

    @Override
    public void onOpen(StatsModule module) {
        Events.register(this);
    }

    @Override
    public void onClose(StatsModule module) {
        Events.unregister(this);
    }
}
