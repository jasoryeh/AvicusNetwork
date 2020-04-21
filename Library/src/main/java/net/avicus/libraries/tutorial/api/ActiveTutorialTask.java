package net.avicus.libraries.tutorial.api;


import net.avicus.libraries.tutorial.plugin.TutorialPlugin;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


public class ActiveTutorialTask extends BukkitRunnable {

    private final ActiveTutorial tutorial;

    public ActiveTutorialTask(ActiveTutorial tutorial) {
        this.tutorial = tutorial;
    }

    @Override
    public void run() {
        if (!this.tutorial.isStarted()) {
            return;
        }

        Player player = this.tutorial.getPlayer();

        TutorialStep step = this.tutorial.getCurrentStep();

//        // Action bar
//        {
//            String actionBar = "";
//            if (step.getCountdown().isPresent()) {
//                actionBar = ChatColor.GREEN + "Punch to continue!";
//            }
//
//            PacketContainer actionBarPacket = new PacketContainer(PacketType.Play.Server.CHAT);
//            actionBarPacket.getBytes().write(0, (byte) 2);
//            actionBarPacket.getChatComponents().write(0, WrappedChatComponent.fromText(actionBar));
//
//            try {
//                ProtocolLibrary.getProtocolManager().sendServerPacket(player, actionBarPacket);
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//            }
//        }

        // Move to next slide if enough time elapsed
        if (step.getCountdown().isPresent()) {
            long now = System.currentTimeMillis();

            long then = this.tutorial.getCurrentStepEnterTime();
            long terminate = then + (long) Math.floor(step.getCountdown().get() * 1000.0);

            if (now > terminate) {
                this.tutorial.setNextStep();
            } else {
                long period = terminate - then;
                long completed = terminate - now;

                double portionCompleted = (double) completed / (double) period;

                player.setExp(1.0F - (float) portionCompleted);
            }
        }

    }

    public void start() {
        runTaskTimer(TutorialPlugin.getInstance().getParent(), 0, 3);
    }
}
