package eu.europeana.publication.utility.mapconverter;
import java.util.List;

import javax.xml.bind.annotation.*;

public class MapElement {
    @XmlElement
    public String key;
    @XmlElement
    public List<String> value;

    private MapElement() {
    }

    public MapElement(String key, List<String> value) {
        this.key = key;
        this.value = value;
    }
}