package net.avicus.atlas.map;

import net.avicus.atlas.SpecificationVersionHistory;
import net.avicus.atlas.map.author.Minecrafter;
import net.avicus.atlas.util.xml.XmlElement;
import net.avicus.magma.game.author.Author;
import org.jdom2.Document;
import org.jdom2.Element;

public class MapConverter {
    public static Document convertPAXMLTOA(Document document, AtlasMap map) {
        final XmlElement ogXml = new XmlElement(document.getRootElement());

        // Root
        Element root = new Element("map");
        root.setAttribute("name", map.getName());
        root.setAttribute("spec", SpecificationVersionHistory.CURRENT.toString());
        root.setAttribute("version", map.getVersion().toString());

        // Includes
        if(ogXml.hasChild("include")) {
            for (Element include : document.getRootElement().getChildren("include")) {
                root.addContent(include);
            }
        }

        // Authors & Contributors
        if(ogXml.hasChild("authors")) {
            for (Author author : map.getAuthors()) {
                Element newAuthor = new Element("author");
                if(author instanceof Minecrafter) {
                    newAuthor.setAttribute("uuid", ((Minecrafter) author).getUuid().toString());
                    newAuthor.setAttribute("contribution", author.getRole().isPresent() ? author.getRole().get() : "");
                    newAuthor.setAttribute("promo", author.getPromo().isPresent() ? author.getRole().get() : "");
                    newAuthor.setAttribute("contributor", "true");
                } else { continue; }
                root.addContent(newAuthor);
            }
            for (Author author : map.getContributors()) {
                Element newAuthor = new Element("contributor");
                if(author instanceof Minecrafter) {
                    newAuthor.setAttribute("uuid", ((Minecrafter) author).getUuid().toString());
                    newAuthor.setAttribute("contribution", author.getRole().isPresent() ? author.getRole().get() : "");
                    newAuthor.setAttribute("promo", author.getPromo().isPresent() ? author.getRole().get() : "");
                    newAuthor.setAttribute("contributor", "true");
                } else { continue; }
                root.addContent(newAuthor);
            }
        }

        // Gamemode/Gametype
        if(ogXml.hasChild("gametype")) {
            for (Element gamemode : document.getRootElement().getChildren("gamemode")) {
                Element gametype = new Element("gametype");
                gametype.addContent(gamemode.getContent());
                root.addContent(gametype);
            }
        }

        // Teams
        if(ogXml.hasChild("teams")) {
            root.addContent(document.getRootElement().getChild("teams").getChildren("team"));
        }

        // Tutorials
        /*if(ogXml.hasChild("tutorial")) {
            Element tutorial = new Element("tutorial");
            for (XmlElement stage : ogXml.getChildren("stage")) {
                Element step = new Element("step");
                if(stage.hasChild("teleport")) {
                    XmlElement point = stage.getChild("teleport").get().getChild("point").get();
                    step.setAttribute("yaw", point.getAttribute("yaw").isValuePresent() ? point.getAttribute("yaw").asRequiredString() : "0");
                    step.setAttribute("pitch", point.getAttribute("pitch").isValuePresent() ? point.getAttribute("pitch").asRequiredString() : "0");
                    step.setAttribute("location", point.getText().asRequiredString());
                    step.setAttribute("countdown", "5s");
                }
            }
            if(tutorial.)
        }*/ // TODO: Later

        throw new UnsupportedOperationException("Cannot parse Project ares maps yet.");

        /*
        Document newDocument = new Document();
        newDocument.setRootElement(root);
        return newDocument;*/
    }
}