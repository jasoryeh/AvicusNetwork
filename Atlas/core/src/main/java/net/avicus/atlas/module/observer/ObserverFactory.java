package net.avicus.atlas.module.observer;

import net.avicus.atlas.documentation.ModuleDocumentation;
import net.avicus.atlas.match.Match;
import net.avicus.atlas.match.MatchFactory;
import net.avicus.atlas.module.ModuleBuildException;
import net.avicus.atlas.module.ModuleFactory;
import net.avicus.atlas.util.xml.XmlElement;

import java.util.Optional;

public class ObserverFactory implements ModuleFactory<ObserverModule> {
    @Override
    public Optional<ObserverModule> build(Match match, MatchFactory factory, XmlElement root) throws ModuleBuildException {
        return Optional.of(new ObserverModule(match));
    }

    @Override
    public ModuleDocumentation getDocumentation() {
        return null;
    }
}
