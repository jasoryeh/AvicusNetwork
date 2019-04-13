package net.avicus.atlas.sets.bedwars;

import net.avicus.atlas.SpecificationVersionHistory;
import net.avicus.atlas.documentation.FeatureDocumentation;
import net.avicus.atlas.documentation.ModuleDocumentation;
import net.avicus.atlas.documentation.SpecInformation;
import net.avicus.atlas.documentation.attributes.Attributes;
import net.avicus.atlas.documentation.attributes.GenericAttribute;
import net.avicus.atlas.match.Match;
import net.avicus.atlas.match.MatchFactory;
import net.avicus.atlas.module.ModuleBuildException;
import net.avicus.atlas.module.ModuleFactory;
import net.avicus.atlas.util.xml.XmlElement;

import java.util.Optional;

public class BedwarsFactory implements ModuleFactory<BedwarsModule> {
    @Override
    public Optional<BedwarsModule> build(Match match, MatchFactory factory, XmlElement root) throws ModuleBuildException {
        return Optional.empty();
    }

    @Override
    public ModuleDocumentation getDocumentation() {
        return ModuleDocumentation.builder()
                .name("Bedwars")
                .tagName("bedwars")
                .category(ModuleDocumentation.ModuleCategory.OBJECTIVES)
                .specInformation(SpecInformation.builder()
                        .added(SpecificationVersionHistory.INTRO_BEDWARS)
                        .build())
                .description("The module is designed to add additional functions to support the Bedwars game type.")
                .description("This module is still in the works, and is not intended to be perfect, or considered to be complete.")
                .feature(FeatureDocumentation.builder()
                        .name("Base Configuration")
                        .description("This describes the base configuration for the bedwars module")
                        .attribute("sudden-death-time", Attributes.duration(true, true,
                                "Time it takes until the beds are all destroyed, and players lose ability to respawn."), "15m")
                        .attribute("repairable", new GenericAttribute(Boolean.class, false,
                                "If a bed is able to be fixed by their own team."), false)
                        .build())
                .feature(FeatureDocumentation.builder()
                        .name("Beds")
                        .tagName("beds")
                        .description("A list of beds and their respective teams")
                        .subFeature(FeatureDocumentation.builder()
                                .name("Bed")
                                .tagName("bed")
                                .description("A bed, describing both blocks.")
                                .attribute("Bed blocks", Attributes.region(true, "Region describing the bed."))
                                .build())
                        .build())
                .build();
    }
}
