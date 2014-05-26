package eu.europeana.publication.common;

/**
 * 
 * State.java
 * Purpose:  documents state field values should be filled with those definite states  
 * 
 * @author Tarek Alkhaeir
 * @version 1.0.0
 */

public enum State {
	ACCEPTED("accepted"), TO_BE_DELETED("to be deleted"), TO_BE_WITHDRAWN(
			"to be withdrawn"), FOR_EDITING("for editing"), IN_PRODUCTION(
			"in production"), DELETED("deleted");

	private String stateCode;

	private State(String s) {
		stateCode = s;
	}

	public String getStateValue() {
		return stateCode;
	}

}