package net.avicus.compendium.plugin;

import net.avicus.compendium.utils.Task;
import org.bukkit.Bukkit;

public class AntiSuspendTask implements Runnable {

    public static Task start() {
        AntiSuspendTask antiSuspendTask = new AntiSuspendTask();
        return new CompendiumTask() {
            @Override
            public void run() {
                antiSuspendTask.run();
            }
        }.repeatAsync(0, 1000);
    }

    @Override
    public void run() {
        Bukkit.getServer().setSuspended(false);
    }

}
