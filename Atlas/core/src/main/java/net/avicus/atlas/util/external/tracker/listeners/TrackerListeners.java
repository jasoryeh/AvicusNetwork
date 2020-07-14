package net.avicus.atlas.util.external.tracker.listeners;

import net.avicus.atlas.util.Events;
import net.avicus.atlas.util.external.tracker.DamageResolverManager;
import net.avicus.atlas.util.external.tracker.DamageResolvers;
import net.avicus.atlas.util.external.tracker.TrackerManager;
import net.avicus.atlas.util.external.tracker.Trackers;
import net.avicus.atlas.util.external.tracker.damage.resolvers.*;
import net.avicus.atlas.util.external.tracker.timer.TickTimer;
import net.avicus.atlas.util.external.tracker.trackers.*;
import net.avicus.atlas.util.external.tracker.trackers.base.*;
import net.avicus.atlas.util.external.tracker.trackers.base.gravity.SimpleGravityKillTracker;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;

public class TrackerListeners {

    public
    @Nullable
    TickTimer tickTimer;

    private JavaPlugin plugin;

    public TrackerListeners(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void disable() {
        Trackers.getManager().clearTracker(ExplosiveTracker.class, SimpleExplosiveTracker.class);
    }

    public void enable() {
        // basic operation listeners
        Events.register(new LifetimeListener());
        Events.register(new WorldListener(Trackers.getManager()));

        EntityDamageEventListener damageEventListener = new EntityDamageEventListener();
        damageEventListener.register(this.plugin);

        // initialize timer
        this.tickTimer = new TickTimer(this.plugin);
        this.tickTimer.start();

        // tracker setup
        TrackerManager tm = Trackers.getManager();

        ExplosiveTracker explosiveTracker = new SimpleExplosiveTracker();
        SimpleGravityKillTracker gravityKillTracker = new SimpleGravityKillTracker(this.plugin,
                this.tickTimer);

        explosiveTracker.enable();
        gravityKillTracker.enable();

        Events.register(new ExplosiveListener(explosiveTracker));
        Events.register(new GravityListener(this.plugin, gravityKillTracker));

        tm.setTracker(ExplosiveTracker.class, explosiveTracker);
        tm.setTracker(SimpleGravityKillTracker.class, gravityKillTracker);

        DispenserTracker dispenserTracker = new SimpleDispenserTracker();
        dispenserTracker.enable();

        Events.register(new DispenserListener(dispenserTracker));
        tm.setTracker(DispenserTracker.class, dispenserTracker);

        ProjectileDistanceTracker projectileDistanceTracker = new SimpleProjectileDistanceTracker();
        projectileDistanceTracker.enable();

        Events.register(new ProjectileDistanceListener(projectileDistanceTracker));
        tm.setTracker(ProjectileDistanceTracker.class, projectileDistanceTracker);

        OwnedMobTracker ownedMobTracker = new SimpleOwnedMobTracker();
        ownedMobTracker.enable();

        Events.register(new OwnedMobListener(ownedMobTracker));
        tm.setTracker(OwnedMobTracker.class, ownedMobTracker);

        AnvilTracker anvilTracker = new SimpleAnvilTracker();
        anvilTracker.enable();

        Events.register(new AnvilListener(anvilTracker));
        tm.setTracker(AnvilTracker.class, anvilTracker);

        // register damage resolvers
        DamageResolverManager drm = DamageResolvers.getManager();

        drm.register(new BlockDamageResolver());
        drm.register(new FallDamageResolver());
        drm.register(new LavaDamageResolver());
        drm.register(new MeleeDamageResolver());
        drm.register(new ProjectileDamageResolver(projectileDistanceTracker));
        drm.register(new TNTDamageResolver(explosiveTracker, dispenserTracker));
        drm.register(new VoidDamageResolver());
        drm.register(new GravityDamageResolver(gravityKillTracker));
        drm.register(
                new DispensedProjectileDamageResolver(projectileDistanceTracker, dispenserTracker));
        drm.register(new OwnedMobDamageResolver(ownedMobTracker));
        drm.register(new AnvilDamageResolver(anvilTracker));

        // debug
        Events.register(new DebugListener());
    }

    private void registerEvents(Listener listener) {
        this.plugin.getServer().getPluginManager().registerEvents(listener, this.plugin);
    }
}
