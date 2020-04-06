package net.avicus.libraries.grave;

import net.avicus.libraries.grave.listener.PlayerListener;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

public class GravePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    public <T extends Event> T callEvent(T event) {
        this.getServer().getPluginManager().callEvent(event);
        return event;
    }
}
