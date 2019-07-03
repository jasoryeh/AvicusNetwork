package net.avicus.atlas.module.gadget.map;

import net.avicus.atlas.SpecificationVersionHistory;
import net.avicus.atlas.documentation.ModuleDocumentation;
import net.avicus.atlas.documentation.SpecInformation;
import net.avicus.atlas.match.Match;
import net.avicus.atlas.match.MatchFactory;
import net.avicus.atlas.module.ModuleBuildException;
import net.avicus.atlas.module.ModuleFactory;
import net.avicus.atlas.util.xml.XmlElement;

import java.util.Optional;

public class MapTokenFactory implements ModuleFactory<MapTokenModule> {
    @Override
    public Optional<MapTokenModule> build(Match match, MatchFactory factory, XmlElement root) throws ModuleBuildException {
        return Optional.of(new MapTokenModule());
    }

    @Override
    public ModuleDocumentation getDocumentation() {
        return ModuleDocumentation.builder()
                .name("Map Tokens")
                .tagName("map-tokens")
                .specInformation(
                        SpecInformation.builder().added(SpecificationVersionHistory.NEW_RECTANGLES).build())
                .category(ModuleDocumentation.ModuleCategory.MISC)
                .description("Allows users to set the next map with a token.")
                .build();
    }
}
