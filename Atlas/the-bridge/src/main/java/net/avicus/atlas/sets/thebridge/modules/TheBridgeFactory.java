package net.avicus.atlas.sets.thebridge.modules;

import net.avicus.atlas.documentation.FeatureDocumentation;
import net.avicus.atlas.documentation.ModuleDocumentation;
import net.avicus.atlas.match.Match;
import net.avicus.atlas.match.MatchFactory;
import net.avicus.atlas.module.ModuleBuildException;
import net.avicus.atlas.module.ModuleFactory;
import net.avicus.atlas.util.xml.XmlElement;

import java.util.Optional;

public class TheBridgeFactory implements ModuleFactory<TheBridgeModule> {
    @Override
    public ModuleDocumentation getDocumentation() {
        return ModuleDocumentation.builder()
                .name("The Bridge")
                .tagName("the-bridge")
                .category(ModuleDocumentation.ModuleCategory.SPECIAL)
                .description(
                        "The Bridge is a game where players rush against each other at all costs in order to " +
                                "score a point in the opposite team's score portal. It is a fast paced minigame" +
                                " that requires a lot of quick thinking and skill to master."
                ).description(
                        "This module enables The Bridge gamemodes to be played, given the correct map."
                ).feature(
                        FeatureDocumentation.builder()
                                .name("Configuration")
                                //.attribute("tbd", new GenericAttribute(TheBridgeMain.class, true, "Portals to be used in this game."))
                                .build()
                ).build();
    }

    @Override
    public Optional<TheBridgeModule> build(Match match, MatchFactory factory, XmlElement root) throws ModuleBuildException {
        // TODO: WIP
        if(root.hasChild("the-bridge")) {
            return Optional.of(new TheBridgeModule());
        }
        return Optional.empty();
    }
}
