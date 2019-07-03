package net.avicus.atlas.module.gadget.map;

import net.avicus.atlas.event.match.MatchCompleteEvent;
import net.avicus.atlas.module.Module;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class MapTokenModule implements Module {
    private MapTokenManager mapTokenManager;

    public MapTokenModule() {
        // Initialize Map tokens
        this.mapTokenManager = MapTokenManager.getInstance();
    }

    @EventHandler
    public void chatIn(AsyncPlayerChatEvent e) {
        this.mapTokenManager.chatIn(e);
    }

    @EventHandler
    public void matchComplete(MatchCompleteEvent e) {
        this.mapTokenManager.onMatchComplete(e);
    }
}
