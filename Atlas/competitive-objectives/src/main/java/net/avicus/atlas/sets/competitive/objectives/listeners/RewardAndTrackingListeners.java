package net.avicus.atlas.sets.competitive.objectives.listeners;

import net.avicus.atlas.Atlas;
import net.avicus.atlas.event.match.MatchCompleteEvent;
import net.avicus.atlas.module.groups.Competitor;
import net.avicus.atlas.sets.competitive.objectives.destroyable.DestroyableObjective;
import net.avicus.atlas.sets.competitive.objectives.destroyable.event.DestroyableDamageEvent;
import net.avicus.atlas.sets.competitive.objectives.destroyable.event.DestroyableTouchEvent;
import net.avicus.atlas.sets.competitive.objectives.destroyable.leakable.LeakableObjective;
import net.avicus.atlas.sets.competitive.objectives.destroyable.leakable.event.LeakableLeakEvent;
import net.avicus.atlas.sets.competitive.objectives.destroyable.monument.MonumentObjective;
import net.avicus.atlas.sets.competitive.objectives.destroyable.monument.event.MonumentDestroyEvent;
import net.avicus.atlas.sets.competitive.objectives.flag.events.FlagCaptureEvent;
import net.avicus.atlas.sets.competitive.objectives.hill.HillObjective;
import net.avicus.atlas.sets.competitive.objectives.hill.event.HillCaptureEvent;
import net.avicus.atlas.sets.competitive.objectives.wool.event.WoolPickupEvent;
import net.avicus.atlas.sets.competitive.objectives.wool.event.WoolPlaceEvent;
import net.avicus.hook.Hook;
import net.avicus.hook.HookConfig;
import net.avicus.hook.achievements.Achievements;
import net.avicus.hook.achievements.AchievementsListener;
import net.avicus.hook.credits.Credits;
import net.avicus.hook.credits.reward.CreditRewardListener;
import net.avicus.hook.credits.reward.CreditRewarder;
import net.avicus.hook.prestige.ExperienceRewardListener;
import net.avicus.hook.tracking.Tracking;
import net.avicus.hook.tracking.TrackingListener;
import net.avicus.hook.utils.HookTask;
import net.avicus.hook.utils.Messages;
import net.avicus.magma.database.model.impl.ObjectiveCompletion;
import net.avicus.magma.database.model.impl.ObjectiveType;
import net.avicus.magma.database.model.impl.User;
import net.avicus.magma.module.prestige.PrestigeModule;
import net.avicus.magma.network.user.Users;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Replaces the hard coded hook dependency on atlas's competitive objectives module
 */
public class RewardAndTrackingListeners {

    public static void register() {
        // these are all located in the listeners so we don't initialize the other static fields yet
        // since that'd cause problems :(
        AchievementsListener.ACHIEVEMENTS_LISTENERS.add(CompetitiveAchievementsListener.class);
        CreditRewardListener.CREDIT_LISTENERS.add(CompetitiveCreditRewardListener.class);
        ExperienceRewardListener.EXPERIENCE_LISTENERS.add(CompetitiveExperienceRewardListener.class);
        TrackingListener.TRACKERS.add(CompetitiveTracker.class);
    }

    public static class CompetitiveTracker extends TrackingListener {

        private final ObjectiveType monument;
        private final ObjectiveType flag;
        private final ObjectiveType hill;
        private final ObjectiveType wool;
        private final ObjectiveType leakable;

        public CompetitiveTracker(Tracking tracking) {
            super(tracking);
            this.monument = Hook.database().getObjectiveTypes().findOrCreate("monument");
            this.flag = Hook.database().getObjectiveTypes().findOrCreate("flag");
            this.hill = Hook.database().getObjectiveTypes().findOrCreate("hill");
            this.wool = Hook.database().getObjectiveTypes().findOrCreate("wool");
            this.leakable = Hook.database().getObjectiveTypes().findOrCreate("leakable");
        }

        protected static void objective(Collection<Player> players, ObjectiveType type) {
            players.stream().forEach((p) -> objective(p, type));
        }

        protected static void objective(Player player, ObjectiveType type) {
            if (!HookConfig.Tracking.isObjectives()) {
                return;
            }

            User user = Users.user(player);
            ObjectiveCompletion objective = new ObjectiveCompletion(user.getId(), type, new Date());
            HookTask.of(() -> Hook.database().getObjectiveCompletions().insert(objective).execute())
                    .nowAsync();
        }

        @EventHandler
        public void onMonumentDestroy(MonumentDestroyEvent event) {
            objective(event.getPlayers(), monument);
        }

        @EventHandler
        public void onFlagCapture(FlagCaptureEvent event) {
            objective(event.getPlayers(), flag);
        }

        @EventHandler
        public void onFlagCapture(HillCaptureEvent event) {
            objective(event.getPlayers(), hill);
        }

        @EventHandler
        public void onWoolPlace(WoolPlaceEvent event) {
            objective(event.getPlayers().get(0), wool);
        }

        @EventHandler
        public void onLeakableLeak(LeakableLeakEvent event) {
            objective(event.getPlayers().get(0), leakable);
        }
    }

    public static class CompetitiveExperienceRewardListener extends ExperienceRewardListener {

        private final Map<HillObjective, Competitor> hillOwners;

        public CompetitiveExperienceRewardListener(PrestigeModule module) {
            super(module);
            this.hillOwners = new HashMap<>();
        }

        @EventHandler
        public void onMonumentDestroy(MonumentDestroyEvent event) {
            int reward = HookConfig.Experience.Rewards.getMonumentDestroy();
            if (reward > 0) {
                this.module
                        .reward(event.getPlayers().get(0), reward, Messages.UI_REWARD_MONUMENT_DESTROYED,
                                Atlas.getMatch().getMap().getGenre().name());
            }

        }

        @EventHandler
        public void onFlagCapture(FlagCaptureEvent event) {
            int reward = HookConfig.Experience.Rewards.getFlagCapture();
            if (reward > 0) {
                this.module.reward(event.getPlayers().get(0), reward, Messages.UI_REWARD_FLAG_CAPTURED,
                        Atlas.getMatch().getMap().getGenre().name());
            }
        }

        @EventHandler
        public void onDestroyableDamage(DestroyableDamageEvent event) {
            int reward = HookConfig.Experience.Rewards.getMonumentDamage();
            if (reward > 0) {
                int blocks = ((DestroyableObjective) event.getObjective()).getOriginals().size();
                if (blocks > reward) {
                    reward = Math.max(1, (reward / blocks) * reward);
                }

                this.module.reward(event.getInfo().getActor(), reward, Messages.UI_REWARD_MONUMENT_DAMAGED,
                        Atlas.getMatch().getMap().getGenre().name());
            }
        }

        @EventHandler
        public void onWoolPlace(WoolPlaceEvent event) {
            int reward = HookConfig.Experience.Rewards.getWoolPlace();
            if (reward > 0) {
                this.module.reward(event.getPlayers().get(0), reward, Messages.UI_REWARD_WOOL_PLACE,
                        Atlas.getMatch().getMap().getGenre().name());
            }
        }

        @EventHandler
        public void onWoolPickup(WoolPickupEvent event) {
            int reward = HookConfig.Experience.Rewards.getWoolPickup();
            if (reward > 0) {
                this.module.reward(event.getPlayer(), reward, Messages.UI_REWARD_WOOL_PICKUP,
                        Atlas.getMatch().getMap().getGenre().name());
            }
        }

        @EventHandler
        public void onLeakableLeak(LeakableLeakEvent event) {
            int reward = HookConfig.Experience.Rewards.getLeakableLeak();
            if (reward > 0) {
                this.module.reward(event.getPlayers().get(0), reward, Messages.UI_REWARD_LEAKABLE_LEAK,
                        Atlas.getMatch().getMap().getGenre().name());
            }
        }

        @EventHandler
        public void onHillCapture(HillCaptureEvent event) {
            if (!event.getNewOwner().isPresent()) {
                return;
            }

            HillObjective objective = (HillObjective) event.getObjective();

            // This means the same team capped the hill again, don't reward XP.
            if (this.hillOwners.containsKey(objective) && this.hillOwners.get(objective)
                    .equals(event.getNewOwner().orElse(null))) {
                return;
            }

            this.hillOwners.put(objective, event.getNewOwner().orElse(null));
            int reward = HookConfig.Experience.Rewards.getLeakableLeak();
            if (reward > 0) {
                event.getPlayers()
                        .forEach(p -> this.module.reward(p, reward, Messages.UI_REWARD_HILL_CAPTURE,
                                Atlas.getMatch().getMap().getGenre().name()));
            }
        }

        // TODO: Score box

        @EventHandler
        public void onComplete(MatchCompleteEvent event) {
            this.hillOwners.clear();
        }
    }

    public static class CompetitiveAchievementsListener extends AchievementsListener {

        public CompetitiveAchievementsListener(Achievements achievements) {
            super(achievements);
        }

        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        public void onMonumentDestroy(MonumentDestroyEvent event) {
            achievements.increment("monument-completions", event.getPlayers().get(0));
        }

        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        public void onDamage(DestroyableTouchEvent event) {
            if (event.getObjective() instanceof MonumentObjective) {
                achievements.increment("monument-touches", event.getPlayer());
            } else if (event.getObjective() instanceof LeakableObjective) {
                achievements.increment("leakable-touches", event.getPlayer());
            }
        }

        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        public void onFlagCapture(FlagCaptureEvent event) {
            achievements.increment("flag-captures", event.getPlayers().get(0));
        }

        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        public void onHillCapture(HillCaptureEvent event) {
            achievements.increment("hill-captures", event.getPlayers().get(0));
        }

        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        public void onWoolPlace(WoolPlaceEvent event) {
            achievements.increment("wool-places", event.getPlayers().get(0));
        }

        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        public void onPickup(WoolPickupEvent event) {
            achievements.increment("wool-touches", event.getPlayer());
        }

        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        public void onLeakableLeak(LeakableLeakEvent event) {
            achievements.increment("leakable-leaks", event.getPlayers().get(0));
        }
    }

    public static class CompetitiveCreditRewardListener extends CreditRewardListener {

        public CompetitiveCreditRewardListener(CreditRewarder creditRewarder) {
            super(creditRewarder);
        }

        @EventHandler
        public void onMonumentDestroy(MonumentDestroyEvent event) {
            int reward = HookConfig.Credits.Rewards.getMonumentDestroy();
            if (reward > 0) {
                Credits.reward(event.getPlayers().get(0), reward, Messages.UI_REWARD_MONUMENT_DESTROYED);
            }
        }

        @EventHandler
        public void onFlagCapture(FlagCaptureEvent event) {
            int reward = HookConfig.Credits.Rewards.getFlagCapture();
            if (reward > 0) {
                Credits.reward(event.getPlayers().get(0), reward, Messages.UI_REWARD_FLAG_CAPTURED);
            }
        }

        @EventHandler
        public void onWoolPlace(WoolPlaceEvent event) {
            int reward = HookConfig.Credits.Rewards.getWoolPlace();
            if (reward > 0) {
                Credits.reward(event.getPlayers().get(0), reward, Messages.UI_REWARD_WOOL_PLACE);
            }
        }

        @EventHandler
        public void onWoolPickup(WoolPickupEvent event) {
            int reward = HookConfig.Credits.Rewards.getWoolPickup();
            if (reward > 0) {
                Credits.reward(event.getPlayer(), reward, Messages.UI_REWARD_WOOL_PICKUP);
            }
        }

        @EventHandler
        public void onLeakableLeak(LeakableLeakEvent event) {
            int reward = HookConfig.Credits.Rewards.getLeakableLeak();
            if (reward > 0) {
                Credits.reward(event.getPlayers().get(0), reward, Messages.UI_REWARD_LEAKABLE_LEAK);
            }
        }
    }

}
