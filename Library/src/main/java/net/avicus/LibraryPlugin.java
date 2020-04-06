package net.avicus;

import org.bukkit.plugin.java.JavaPlugin;

public class LibraryPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Library.loadLibraries(this);
    }

    @Override
    public void onDisable() {
        Library.unloadLibraries(this);
    }
}
