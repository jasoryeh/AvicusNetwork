package net.avicus.magma.util.safe;

import lombok.Getter;
import lombok.Setter;
import net.avicus.magma.Magma;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Logger;

/**
 * For Bukkit tasks that are non-fatal if
 * failed, and are more focused on pulling/updating
 * information here and there that may fail.
 *
 * Use 'perform' method instead of 'run' to
 * do these tasks, and register runnable as
 * normal.
 */
public abstract class RetryingRunnable extends BukkitRunnable {

    @Setter
    @Getter
    protected boolean showStackTrace = false;
    @Setter
    @Getter
    protected int retries = 3;
    @Getter
    @Setter
    protected boolean blockThread = true;
    @Getter
    @Setter
    protected long retryDelay = 1000 * 5; // 5 seconds

    @Override
    public void run() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Logger logger = Magma.get().getLogger();
                for (int i = 0; i < retries; i++) {
                    try {
                        if (i != 0) {
                            logger.info("Retrying [Try " + (i + 1) + "]");
                        }

                        // run original runnable
                        RetryingRunnable.this.perform();

                        // end of task.
                        if (i != 0) {
                            logger.info("Finished retried task in " + (i + 1) + " tries.");
                        }
                        break;
                    } catch (Exception e) {
                        try {
                            logger.warning("[Try " + (i + 1) + "] StatusUpdateTask failed, retrying in " + (retryDelay / 1000)
                                    + " seconds, max " + (retries) + " retries: "
                                    + e.getClass().getName() + " said " + e.getMessage());
                            if (showStackTrace) {
                                e.printStackTrace();
                            }

                            Thread.sleep(retryDelay);
                        } catch (InterruptedException interrupt) {
                            logger.warning("Unable to retry, sleep was interrupted.");
                            interrupt.printStackTrace();
                        }
                    }
                }
            }
        };

        if(blockThread) {
            runnable.run();
        } else {
            Thread thread = new Thread(runnable);
            thread.start();
        }
    }

    public abstract void perform();
}
