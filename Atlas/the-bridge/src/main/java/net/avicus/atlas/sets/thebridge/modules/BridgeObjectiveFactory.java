package net.avicus.atlas.sets.thebridge.modules;

import net.avicus.atlas.documentation.FeatureDocumentation;
import net.avicus.atlas.match.Match;
import net.avicus.atlas.match.MatchFactory;
import net.avicus.atlas.module.objectives.Objective;
import net.avicus.atlas.util.xml.XmlElement;

public interface BridgeObjectiveFactory<T extends Objective> {
    T build(Match match, MatchFactory factory, XmlElement element);

    FeatureDocumentation getDocumentation();
}
