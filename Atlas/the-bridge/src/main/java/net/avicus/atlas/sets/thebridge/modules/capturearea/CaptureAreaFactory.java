package net.avicus.atlas.sets.thebridge.modules.capturearea;

import net.avicus.atlas.documentation.FeatureDocumentation;
import net.avicus.atlas.documentation.attributes.Attributes;
import net.avicus.atlas.documentation.attributes.EnumAttribute;
import net.avicus.atlas.documentation.attributes.GenericAttribute;
import net.avicus.atlas.match.Match;
import net.avicus.atlas.match.MatchFactory;
import net.avicus.atlas.module.FactoryUtils;
import net.avicus.atlas.sets.thebridge.modules.BridgeObjectiveFactory;
import net.avicus.atlas.util.xml.XmlElement;
import net.avicus.magma.util.region.Region;
import org.bukkit.Material;
import org.bukkit.scoreboard.Team;

import java.util.Optional;

public class CaptureAreaFactory implements BridgeObjectiveFactory<CaptureAreaModule> {

    @Override
    public CaptureAreaModule build(Match match, MatchFactory factory, XmlElement element) {
        Optional<String> name = element.getAttribute("name").asString();

        Boolean shouldFillWithBlocks = element.getAttribute("auto-fill").asBoolean()
                .orElse(false);
        Material material = element.getAttribute("material").asEnum(Material.class, true)
                .orElse(Material.ENDER_PORTAL);

        Optional<Team> defender = element.hasAttribute("defending-team-id") ?
                match.getRegistry().get(Team.class, element.getAttribute("defending-team-id").asRequiredString(),
                        true)
                : Optional.empty();

        Region captureArea = FactoryUtils.resolveRequiredRegionAs(match, Region.class,
                element.getAttribute("capture"), element.getChild("capture"));

        IllegalCaptureBehavior defenderFallInBehavior = element.getAttribute("illegal-capture-behavior")
                .asEnum(IllegalCaptureBehavior.class, true)
                .orElse(IllegalCaptureBehavior.NOTHING);
        return null;
    }

    @Override
    public FeatureDocumentation getDocumentation() {
        return FeatureDocumentation.builder()
                .name("Capture Area")
                .description("Capture points are ways players add scores to their team! Entering a capture point enables the players to capture a game point.")
                .tagName("capture-area")
                .attribute("name", new GenericAttribute(
                        String.class,
                        false,
                        "Name of this capture area"))
                .attribute("auto-fill",
                        new GenericAttribute(Boolean.class,
                                false,
                                "Whether Atlas should fill this area with a block"),
                        false)
                .attribute("material",
                        new EnumAttribute(Material.class,
                        false,
                        "Block to fil if auto-fill is true"),
                        Material.ENDER_PORTAL)
                .attribute("defending-team-id", Attributes.idOf(
                        true,
                        "team",
                        "The team who is protecting this capture area"))
                .attribute("capture", Attributes.region(
                        true,
                        "The region opponents should enter to capture a point"))
                .attribute("illegal-capture-behavior", new EnumAttribute(
                        IllegalCaptureBehavior.class,
                        false,
                        "Behavior if the defender falls into the capture region."),
                        IllegalCaptureBehavior.NOTHING)
                .build();
    }
}
