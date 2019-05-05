package net.avicus.hook.backend.votes;

import net.avicus.hook.HookPlugin;
import net.avicus.hook.utils.Events;

public class Votes {

    public static void init() {
        try {
            Class.forName("com.vexsoftware.votifier.model.Vote");

            Events.register(new VoteListener());
        } catch (ClassNotFoundException e) {
            HookPlugin.getInstance().getLogger().info("Vote not found. Not enabling the listener.");

            e.printStackTrace();
        }
    }
}
