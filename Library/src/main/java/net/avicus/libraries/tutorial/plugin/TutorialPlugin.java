package net.avicus.libraries.tutorial.plugin;

import net.avicus.Library;
import net.avicus.LibraryPlugin;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class TutorialPlugin extends Library implements Listener {

    private static TutorialPlugin instance;

    public TutorialPlugin(JavaPlugin parent) {
        super(parent);
    }

    public static TutorialPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        this.getParent().getServer().getPluginManager().registerEvents(this, this.getParent());
    }

    @Override
    public void onDisable() {
        instance = null;
    }
}
