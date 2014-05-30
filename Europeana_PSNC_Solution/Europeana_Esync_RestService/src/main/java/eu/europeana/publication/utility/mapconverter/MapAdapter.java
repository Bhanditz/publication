package eu.europeana.publication.utility.mapconverter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class MapAdapter extends XmlAdapter<MapElement[], Map<String, List<String>>> {
    public MapElement[] marshal(Map<String, List<String>> arg0) throws Exception {
        MapElement[] mapElements = new MapElement[arg0.size()];
        int i = 0;
        for (Map.Entry<String, List<String>> entry : arg0.entrySet())
            mapElements[i++] = new MapElement(entry.getKey(), entry.getValue());

        return mapElements;
    }

    public Map<String, List<String>> unmarshal(MapElement[] arg0) throws Exception {
        Map<String, List<String>> r = new HashMap<String, List<String>>();
        for (MapElement mapelement : arg0)
            r.put(mapelement.key, mapelement.value);
        return r;
    }

	
}