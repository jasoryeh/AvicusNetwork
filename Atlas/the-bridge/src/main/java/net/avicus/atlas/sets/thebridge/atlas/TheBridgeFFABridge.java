package net.avicus.atlas.sets.thebridge.atlas;

import net.avicus.atlas.module.ModuleBridge;
import net.avicus.atlas.module.groups.ffa.FFAModule;
import net.avicus.atlas.module.states.StatesModule;
import net.avicus.atlas.util.Events;
import org.bukkit.event.Listener;

public class TheBridgeFFABridge implements ModuleBridge<FFAModule>, Listener {

    private final StatesModule statesModule;
    private final FFAModule module;

    public TheBridgeFFABridge(FFAModule module) {
        this.statesModule = module.getMatch().getRequiredModule(StatesModule.class);
        this.module = module;
    }

    @Override
    public void onOpen(FFAModule module) {
        Events.register(this);
    }

    @Override
    public void onClose(FFAModule module) {
        Events.unregister(this);
    }
}
