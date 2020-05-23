package net.avicus.atlas.sets.thebridge.atlas;

import net.avicus.atlas.module.ModuleBridge;
import net.avicus.atlas.module.results.ResultsModule;
import net.avicus.atlas.sets.thebridge.modules.capturearea.events.CaptureAreaCaptureEvent;
import net.avicus.atlas.sets.thebridge.modules.capturearea.events.CaptureAreaScoreCompleteEvent;
import net.avicus.atlas.util.Events;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

// todo: double check? I think this is what makes the module check for game complete.
public class TheBridgeResultsBridge implements ModuleBridge<ResultsModule>, Listener {

    private final ResultsModule module;

    public TheBridgeResultsBridge(ResultsModule module) {
        this.module = module;
    }

    @Override
    public void onOpen(ResultsModule module) {
        Events.register(this);
    }

    @Override
    public void onClose(ResultsModule module) {
        Events.unregister(this);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCaptureAreaScoreComplete(CaptureAreaScoreCompleteEvent event) {
        module.syncCheck();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCaptureCapture(CaptureAreaCaptureEvent event) {
        module.syncCheck();
    }
}
