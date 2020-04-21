package net.avicus.libraries.tabbed;

import net.avicus.Library;
import net.avicus.LibraryPlugin;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class TabbedPlugin extends Library {
    public TabbedPlugin(JavaPlugin parent) {
        super(parent);
    }

    @Override
    public void onEnable() {
        this.getParent().getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Tabbed library is ready to be used...");
    }

    @Override
    public void onDisable() {
        this.getParent().getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Tabbed library is disabled...");
    }
}
