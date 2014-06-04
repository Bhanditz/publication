package eu.europeana.publication.common;

import java.util.List;
import java.util.Map;


/**
 * 
 * ICollection.java
 * Purpose: The interface that should be implemented by the source and the destination collections 
 * Contain all the template methods which are needed for the synchronization mechanism 
 *  
 *
 * @author Tarek Alkhaeir
 * @version 1.0.0
 */
public interface ICollection {

	
	/**
	 * return batch of list of documents based on a list of values for the state filed
	 * 
	 *@param stateValues
	 *                 list of state filed values for documents which will be returned
	 *@param queryChoices
	 *      		    map with keys and 
						list of values related to those keys ,this variable could be used to specify the  query 
						when selecting the documents to be synchronized .Examples of keys will be (collection -dataSet
	 *                                   
	 *@param   batchSize
	 *                 the number of documents which will be returned   
	 * 
	 *@return   list of documents                 
	 */                     
	abstract public List<IDocument> getDocumentsByStatesUsingBatch(
			List<State> stateVlues,Map<String ,List<String>> queryChoices , int batchSize);

	
	
	          
	abstract public IDocument getDocumentById(String id);
	

	abstract public void insertDocument(IDocument document);

	abstract public void deleteDocument(IDocument document);

	/**
	 * update an existing document ..the old document will be replaced by the new document based on its id
	 * find the old document based on (document id which will be passed as an argument) and then replace all this old document fields
	 *  values with the values from the new document (the argument document)
	 * 
	 *@param document
	 *                 IDocument implementation for the new document
	 */
	abstract public void updateDocumentUsingId(IDocument document);

	/**
	 * clone the original document to the cloned document
	 * 
	 
	 */
	abstract public void cloneDocument(IDocument originalDocument,
			IDocument clonedDocument);
	
	/**
	 *  needed for the collection implementation where the commit operation is very costly and will be executed only once at the end
	 * of the synchronization
	 * 
	 
	 */
	abstract public void commit() throws Exception;
	

}
