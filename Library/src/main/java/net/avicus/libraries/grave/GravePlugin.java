package net.avicus.libraries.grave;

import net.avicus.Library;
import net.avicus.LibraryPlugin;
import net.avicus.libraries.grave.listener.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

public class GravePlugin extends Library {
    public GravePlugin(JavaPlugin parent) {
        super(parent);
    }

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("Enabling Grave...");
        this.getParent().getServer().getPluginManager().registerEvents(new PlayerListener(this), this.getParent());
        Bukkit.getLogger().info("Grave enabled.");
    }

    @Override
    public void onDisable() { }

    public <T extends Event> T callEvent(T event) {
        Bukkit.getLogger().info("Grave calling event " + event.getClass().getName());
        this.getParent().getServer().getPluginManager().callEvent(event);
        return event;
    }
}
