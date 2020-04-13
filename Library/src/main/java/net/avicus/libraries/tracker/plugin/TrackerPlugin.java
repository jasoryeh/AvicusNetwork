package net.avicus.libraries.tracker.plugin;

import net.avicus.Library;
import net.avicus.LibraryPlugin;
import net.avicus.libraries.tracker.DamageResolverManager;
import net.avicus.libraries.tracker.DamageResolvers;
import net.avicus.libraries.tracker.TrackerManager;
import net.avicus.libraries.tracker.Trackers;
import net.avicus.libraries.tracker.damage.resolvers.*;
import net.avicus.libraries.tracker.timer.TickTimer;
import net.avicus.libraries.tracker.trackers.*;
import net.avicus.libraries.tracker.trackers.base.*;
import net.avicus.libraries.tracker.trackers.base.gravity.SimpleGravityKillTracker;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;

public class TrackerPlugin extends Library {

    public
    @Nullable
    TickTimer tickTimer;

    public TrackerPlugin(JavaPlugin parent) {
        super(parent);
    }

    @Override
    public void onDisable() {
        Trackers.getManager().clearTracker(ExplosiveTracker.class, SimpleExplosiveTracker.class);
    }

    @Override
    public void onEnable() {
        // basic operation listeners
        this.registerEvents(new LifetimeListener());
        this.registerEvents(new WorldListener(Trackers.getManager()));

        EntityDamageEventListener damageEventListener = new EntityDamageEventListener();
        damageEventListener.register(this.getParent());

        // initialize timer
        this.tickTimer = new TickTimer(this.getParent());
        this.tickTimer.start();

        // tracker setup
        TrackerManager tm = Trackers.getManager();

        ExplosiveTracker explosiveTracker = new SimpleExplosiveTracker();
        SimpleGravityKillTracker gravityKillTracker = new SimpleGravityKillTracker(this,
                this.tickTimer);

        explosiveTracker.enable();
        gravityKillTracker.enable();

        this.registerEvents(new ExplosiveListener(explosiveTracker));
        this.registerEvents(new GravityListener(this, gravityKillTracker));
        this.registerEvents(new CustomEventListener());

        tm.setTracker(ExplosiveTracker.class, explosiveTracker);
        tm.setTracker(SimpleGravityKillTracker.class, gravityKillTracker);

        DispenserTracker dispenserTracker = new SimpleDispenserTracker();
        dispenserTracker.enable();

        this.registerEvents(new DispenserListener(dispenserTracker));
        tm.setTracker(DispenserTracker.class, dispenserTracker);

        ProjectileDistanceTracker projectileDistanceTracker = new SimpleProjectileDistanceTracker();
        projectileDistanceTracker.enable();

        this.registerEvents(new ProjectileDistanceListener(projectileDistanceTracker));
        tm.setTracker(ProjectileDistanceTracker.class, projectileDistanceTracker);

        OwnedMobTracker ownedMobTracker = new SimpleOwnedMobTracker();
        ownedMobTracker.enable();

        this.registerEvents(new OwnedMobListener(ownedMobTracker));
        tm.setTracker(OwnedMobTracker.class, ownedMobTracker);

        AnvilTracker anvilTracker = new SimpleAnvilTracker();
        anvilTracker.enable();

        this.registerEvents(new AnvilListener(anvilTracker));
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
        this.registerEvents(new DebugListener());
    }

    private void registerEvents(Listener listener) {
        this.getParent().getServer().getPluginManager().registerEvents(listener, this.getParent());
    }
}
