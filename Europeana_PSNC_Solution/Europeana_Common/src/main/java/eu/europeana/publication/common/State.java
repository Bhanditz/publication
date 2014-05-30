package eu.europeana.publication.common;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * 
 * State.java
 * Purpose:  documents state field values should be filled with those definite states  
 * 
 * @author Tarek Alkhaeir
 * @version 1.0.0
 */
@XmlRootElement
public enum State {
	ACCEPTED("accepted"), TO_BE_DELETED("to_be_deleted"), TO_BE_WITHDRAWN(
			"to_be_withdrawn"), FOR_EDITING("for_editing"), IN_PRODUCTION(
			"in_production"), DELETED("deleted");

	private String stateCode;

	private State(String s) {
		stateCode = s;
	}

	public String getStateValue() {
		return stateCode;
	}

}