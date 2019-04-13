package net.avicus.atlas.sets.bedwars;

import net.avicus.atlas.external.ModuleSet;
import org.bukkit.Bukkit;

/**
 * Sample module set for the Atlas module-set "plug-ins".
 *
 * Each module depends on Atlas to run, and is formatted in a way that resembles
 * org.bukkit.plugin.java.JavaPlugin.
 */
public class Main extends ModuleSet {

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("Hello World!");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("Goodbye Cruel World!");
    }
}
