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
    protected boolean blockThreadOnRun = true;
    @Getter
    @Setter
    protected long retryDelay = 1000 * 5; // 5 seconds

    private static String LOGPREFIX = "[Retry] ";

    @Override
    public void run() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Logger logger = Magma.get().getLogger();
                for (int tri = 0; tri < retries; tri++) {
                    try {
                        if (tri != 0) {
                            // notify of retry if this isn't the first run. (won't say anything if done once)
                            logger.info(LOGPREFIX + "Retrying [Try " + tri + "]");
                        }

                        // run original runnable
                        RetryingRunnable.this.perform();

                        // end of task.
                        if (tri != 0) {
                            logger.info(LOGPREFIX + "Finished retried task in " + tri + " tries.");
                        }

                        break;
                    } catch (Exception e) {
                        try {
                            logger.warning(LOGPREFIX + "[Try " + tri + "] " +
                                    "StatusUpdateTask failed, retrying in " + (retryDelay / 1000)
                                    + " seconds, max. " + (retries) + " retries.");
                            logger.warning(LOGPREFIX + e.getClass().getName() + " said " + e.getMessage());

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

        if(blockThreadOnRun) {
            runnable.run();
        } else {
            Thread thread = new Thread(runnable);
            thread.start();
        }
    }

    public abstract void perform();
}
