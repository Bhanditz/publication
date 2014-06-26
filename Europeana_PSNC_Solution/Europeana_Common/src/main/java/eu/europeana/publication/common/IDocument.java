package eu.europeana.publication.common;



/**
 * 
 * IDocument.java
 * Purpose: The interface that should be implemented by documents which will be synchronized
 * 
 *  
 *
 * @author Tarek Alkhaeir
 * @version 1.0.0
 */
public interface IDocument {

	public void setState(State state);

	public State getState();

	public String getId();
	
	public void setId(String id);
	
	public String getType();
	

}
