package eu.europeana.publication.otherconfig;

import java.util.List;






import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import eu.europeana.publication.common.State;
import eu.europeana.publication.utility.mapconverter.QueryChoicesMap;


@XmlRootElement(name = "Other")
public class SynchronizationParametersConfBean {
	  
		private QueryChoicesMap<String, List<String>> queryChoices ;
		private int batchSize;
		private List<State> stateKeys;
  	    private String sourceType;
  	    private String destinationType ;
  	    
  	    public SynchronizationParametersConfBean()
  	    {
  	    	
  	    }
  	    
  	    
     
		public QueryChoicesMap<String, List<String>> getQueryChoices() {
			return queryChoices;
		}
		@XmlElement
		public void setQueryChoices(QueryChoicesMap<String, List<String>> queryChoices) {
			this.queryChoices = queryChoices;
		}
		public int getBatchSize() {
			return batchSize;
		}
		@XmlElement
		public void setBatchSize(int batchSize) {
			this.batchSize = batchSize;
		}
		public List<State> getStateKeys() {
			return stateKeys;
		}
		@XmlElement
		public void setStateKeys(List<State> stateKeys) {
			this.stateKeys = stateKeys;
		}
		public String getSourceType() {
			return sourceType;
		}
		@XmlElement
		public void setSourceType(String sourceType) {
			this.sourceType = sourceType;
		}
		public String getDestinationType() {
			return destinationType;
		}
		@XmlElement
		public void setDestinationType(String destinationType) {
			this.destinationType = destinationType;
		}
		
     

}
