package net.avicus.libraries.tabbed;

import net.avicus.Library;
import net.avicus.LibraryPlugin;
import org.bukkit.ChatColor;

public class TabbedPlugin extends Library {
    public TabbedPlugin(LibraryPlugin parent) {
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
