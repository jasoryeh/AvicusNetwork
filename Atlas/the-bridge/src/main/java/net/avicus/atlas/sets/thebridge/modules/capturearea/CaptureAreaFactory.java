package net.avicus.atlas.sets.thebridge.modules.capturearea;

import net.avicus.atlas.documentation.FeatureDocumentation;
import net.avicus.atlas.documentation.attributes.Attributes;
import net.avicus.atlas.documentation.attributes.EnumAttribute;
import net.avicus.atlas.documentation.attributes.GenericAttribute;
import net.avicus.atlas.match.Match;
import net.avicus.atlas.match.MatchFactory;
import net.avicus.atlas.module.FactoryUtils;
import net.avicus.atlas.module.groups.teams.Team;
import net.avicus.atlas.module.objectives.ObjectiveFactory;
import net.avicus.atlas.util.xml.XmlElement;
import net.avicus.magma.util.region.Region;
import org.bukkit.Material;

import java.util.Optional;

public class CaptureAreaFactory implements ObjectiveFactory<CaptureAreaObjective> {

    @Override
    public CaptureAreaObjective build(Match match, MatchFactory factory, XmlElement element) {
        element.inheritAttributes("capture-areas");

        Optional<Team> capturer = element.hasAttribute("capturing-team-id") ?
                match.getRegistry().get(Team.class, element.getAttribute("capturing-team-id").asRequiredString(),
                        true)
                : Optional.empty();

        Region captureArea = FactoryUtils.resolveRequiredRegionAs(match, Region.class,
                element.getAttribute("region"), element.getChild("region"));

        Optional<String> name = element.getAttribute("name").asString();

        Boolean shouldFillWithBlocks = element.getAttribute("auto-fill").asBoolean()
                .orElse(false);

        Material material = element.getAttribute("material").asEnum(Material.class, true)
                .orElse(Material.ENDER_PORTAL);

        IllegalCaptureBehavior nonCapturerFallInBehavior = element.getAttribute("illegal-capture-behavior")
                .asEnum(IllegalCaptureBehavior.class, true)
                .orElse(IllegalCaptureBehavior.NOTHING);

        int pointsToComplete = element.getAttribute("points-to-complete").asInteger().orElse(3);

        return new CaptureAreaObjective(match, name, pointsToComplete, material, shouldFillWithBlocks, capturer, captureArea, nonCapturerFallInBehavior);
    }

    @Override
    public FeatureDocumentation getDocumentation() {
        return FeatureDocumentation.builder()
                .name("Capture Area")
                .description("Capture points are ways players add scores to their team! Entering a capture point enables the players to capture a game point.")
                .tagName("capture-area").tagName("capture-areas")
                .attribute("capturing-team-id", Attributes.idOf(
                        true,
                        "team",
                        "The team who can enter this point to capture a point"))
                .attribute("region", Attributes.region(
                        true,
                        "The region opponents should enter to capture a point"))
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
                                "Block to fill if auto-fill is true"),
                        Material.ENDER_PORTAL)
                .attribute("illegal-capture-behavior", new EnumAttribute(
                                IllegalCaptureBehavior.class,
                                false,
                                "Behavior if someone not the capturer falls into the capture region."),
                        IllegalCaptureBehavior.NOTHING)
                .attribute("points-to-complete",
                        new GenericAttribute(Integer.class,
                                false,
                                "How many points the player needs to have (# of times jumped into this capture area) to finish this game."),
                        3)
                .build();
    }
}
