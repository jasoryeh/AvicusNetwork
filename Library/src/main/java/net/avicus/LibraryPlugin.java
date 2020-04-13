package net.avicus;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class LibraryPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getLogger().setLevel(Level.ALL);
        Library.loadLibraries(this);
    }

    @Override
    public void onDisable() {
        Library.unloadLibraries(this);
    }
}
