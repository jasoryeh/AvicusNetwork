package net.avicus.atlas.map;

import net.avicus.atlas.util.xml.XmlElement;
import org.jdom2.Document;

public enum MapSourcePluginType {
    ATLAS, ARES, UNKNOWN;

    public static MapSourcePluginType detect(Document document) {
        final XmlElement root = new XmlElement(document.getRootElement());
        if(root.hasAttribute("spec") || root.hasAttribute("name")) {
            return ATLAS;
        } else if(root.hasAttribute("proto") || root.hasChild("name")) {
            return ARES;
        } else {
            return UNKNOWN;
        }
    }
}
