package net.avicus.atlas.module;

import net.avicus.atlas.documentation.ModuleDocumentation;
import net.avicus.atlas.match.Match;
import net.avicus.atlas.match.MatchFactory;
import net.avicus.atlas.util.xml.XmlElement;

import java.util.Optional;

/**
 * Necessary for every module, implementations should be registered
 * in {@link MatchFactory}'s constructor if you wish to use your module
 * for every {@link Match}
 * @param <M>
 */
public interface ModuleFactory<M extends Module> {
    /**
     * Build's the module this module factory is responsible for creating. This allows the module creator
     * to have lots of modules but not require a heck ton of new Module() statements, and allows
     * the factory to manage finding and providing all the necessary information to the module.
     *
     * If this is an implementation, run this method in order to get the Module for the information provided
     * to the method
     *
     * @param match Match this module is being built for
     * @param factory MatchFactory responsible for creating this Match object
     * @param root XML data that may be necessary for this Module (map XML configuration)
     * @return Module for the information provided
     * @throws ModuleBuildException
     */
    Optional<M> build(Match match, MatchFactory factory, XmlElement root) throws ModuleBuildException;

    /**
     * Builds documentation for this module.
     * @return null if no documentation
     */
    ModuleDocumentation getDocumentation();
}
