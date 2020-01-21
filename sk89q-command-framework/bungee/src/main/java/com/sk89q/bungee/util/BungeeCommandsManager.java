package com.sk89q.bungee.util;

import com.sk89q.minecraft.util.commands.CommandsManager;
import net.md_5.bungee.api.CommandSender;

public class BungeeCommandsManager extends CommandsManager<CommandSender> {
    @Override
    public boolean hasPermission(CommandSender player, String perm) {
        return player.hasPermission(perm);
    }
}
