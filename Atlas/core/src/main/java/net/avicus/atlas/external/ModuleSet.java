package net.avicus.atlas.external;

import java.util.logging.Logger;

import net.avicus.atlas.Atlas;
import net.avicus.atlas.component.AtlasComponentManager;
import net.avicus.atlas.match.MatchFactory;
import net.avicus.compendium.commands.AvicusCommandsRegistration;

/**
 * Class to represent a jar that is loaded externally. It is typically a JAR file
 * located in plugins/Atlas/module-sets
 *
 * The module-set's main class is usually an extension of this class, and should follow
 * the same format.
 *
 * It is wise to override #setAtlas, #setMatchFactory, #setLogger, and #setRegistrar
 * and store these objects somewhere if you plan on using them.
 **/
public abstract class ModuleSet {

    /**
     * The onEnable method is called immediately after the module-set is detected and
     * is run immediately after the setter functions (setAtlas, setMatchFactory, etc.)
     * are run and set.
     */
    public abstract void onEnable();

    /**
     * This method is run just before the disable of Atlas. The match manager and component
     * manager may have been shut down already.
     */
    public abstract void onDisable();

    /**
     * Atlas instance setter. Optional implementation, does nothing if not used.
     *
     * @param atlas Atlas instance to give to this module set. Usually available
     *              through {@link net.avicus.atlas.Atlas#get()}.
     */
    public void setAtlas(Atlas atlas) { }

    /**
     * Atlas match factory setter. Optional implementation, does nothing if not used.
     *
     * @param matchFactory MatchFactory to give to this module set. Usually available
     *                     through {@link net.avicus.atlas.match.Match#getFactory()}
     */
    public void setMatchFactory(MatchFactory matchFactory) { }

    /**
     * Atlas logger setter. Optional implementation, does nothing if not used.
     *
     * @param logger Logger to give to this module set. Usually available through
     *               {@link net.avicus.atlas.Atlas#getLogger()}
     */
    public void setLogger(Logger logger) { }

    /**
     * Atlas command registrar. Optional implementation, does nothing if not used.
     *
     * @param registrar The command registrar, allows the module-set to register new
     *                  commands. Usually available through {@link net.avicus.atlas.Atlas#getRegistrar()}
     */
    public void setRegistrar(AvicusCommandsRegistration registrar) { }

    /**
     * Called immediately after registration of components
     * in net.avicus.atlas.component. It is essentially a
     * setter for the component manager.
     *
     * @param componentManager The component manager after
     *                         registration of these component
     *                         classes.
     */
    public void onComponentsEnable(AtlasComponentManager componentManager) {
    }
}
