package eu.europeana.publication.utility.mapconverter;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
public class QueryChoicesMap<String,String1> {
    private Map<String,String1> mapProperty;

    public QueryChoicesMap() {
        mapProperty = new HashMap<String,String1>();
    }

    @XmlJavaTypeAdapter(MapAdapter.class) // NOTE: Our custom XmlAdaper
    public Map<String,String1> getMapProperty() {
        return mapProperty;
    }

    public void setMapProperty(Map<String,String1> map) {
        this.mapProperty = map;
    }
}