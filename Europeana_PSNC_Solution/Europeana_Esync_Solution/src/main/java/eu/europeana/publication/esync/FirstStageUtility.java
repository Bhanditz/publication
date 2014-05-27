package eu.europeana.publication.esync;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import eu.europeana.publication.common.ICollection;
import eu.europeana.publication.common.IDocument;
import eu.europeana.publication.common.State;
import eu.europeana.publication.rabbitmq.RabbitMQSender;
import eu.europeana.publication.rabbitmq.RabbitMQServerUtility;

/**
 * 
 * FirstStageUtility.java
 * Purpose: a utility class for the first stage synchronization mechanism  
 * 
 * @author Tarek Alkhaeir
 * @version 1.0.0
 */

public class FirstStageUtility {
	/**
	 * return batch of list of documents which will  be synchronized using their state field values
	 * 
	 *@param collection 
	 *                The ICollection implementation for the source collection which contains source documents  
	 *@param stateValues
	 *                 list of state filed values for documents which will be synchronized
	 *@param   batch
	 *                 the number of documents which will be returned every batch   
	 * 
	 *@return  batch of list of documents which need to be synchronized                 
	 */                     
	public List<IDocument> getDocumentsByStateToBeSyncronizedUsingBatch(
			ICollection collection, List<State> stateValues,Map<String,List<String>> queryChoices, int batch) {
		List<IDocument> documents = collection.getDocumentsByStatesUsingBatch(
				stateValues,queryChoices, batch);

		return documents;

	}
	
	/**
	 * update the state field value of a document
	 * 
	 *@param collection 
	 *                 The ICollection implementation for the collection which contain the document 
	 *@param documentId
	 *                 the String id for document which need to be updated
	 *@param   newState
	 *                 the new State value for the document
	 * 
	 *               
	 */    
	public void UpdateDocumentStateFieldByDocumentId(ICollection collection,
			String documentId, State newState)  {
		

			IDocument document = collection.getDocumentById(documentId);
			document.setState(newState);
			
		

	}
	/**
	 * update an existing document in the destination collection after the synchronization and update the state 
	 * field in the source document also . based on the state of the synchronized document delete or update an already existing document
	 *  in the destination collection and change the state value of the document in the source collection
	 * 
	 *@param sourceCollection 
	 *                     The ICollection implementation for the source collection which contains source documents  
	 *@param destinationCollection 
	 *                     The ICollection implementation for the production collection which will contain the synchronized documents 
	 *@param sourceDocument
	 *                  the Document which will be synchronized 
	 * @param sender
	 *              RabbitMQ sender which will be used to save/publish the synchronized document id with the appropriate operation (update -delete ) 
	 *  
	 *@return 
	 *               boolean True when the synchronization and the update processes are completed successfully .False otherwise.
	 *@throws IOException
	 *               
	 */ 
	public boolean updateAndSyncronizeExisteingDocumentUsingId(
			ICollection sourceCollection, ICollection destinationCollection,
			IDocument sourceDocument,RabbitMQSender sender) throws IOException {
		
		
	     Map <String,String > queueForRabbitMQ =new HashMap<String,String>();
		
	     RabbitMQServerUtility rabitMQUtil =new RabbitMQServerUtility();

		IDocument destinationDocument = destinationCollection
				.getDocumentById(sourceDocument.getId());

		if (destinationDocument != null) {

			destinationCollection.cloneDocument(sourceDocument,
					destinationDocument);

			if (sourceDocument.getState().equals(State.ACCEPTED)) {

				destinationDocument.setState(State.IN_PRODUCTION);
				sourceDocument.setState(State.IN_PRODUCTION);
				destinationCollection
						.updateDocumentUsingId(destinationDocument);
				
				queueForRabbitMQ.put(destinationDocument.getId(), "update");

			} else

				if (sourceDocument.getState().equals(State.TO_BE_DELETED)) {
					destinationDocument.setState(State.DELETED);
					sourceDocument.setState(State.DELETED);
					destinationCollection
					.updateDocumentUsingId(destinationDocument);
					
					queueForRabbitMQ.put(destinationDocument.getId(), "delete");

				}

				else if (sourceDocument.getState().equals(State.TO_BE_WITHDRAWN)) {
					destinationDocument.setState(State.DELETED);
					sourceDocument.setState(State.FOR_EDITING);
					destinationCollection
					.updateDocumentUsingId(destinationDocument);
					
					queueForRabbitMQ.put(destinationDocument.getId(), "delete");
				}
			sourceCollection.updateDocumentUsingId(sourceDocument);
			
			
            byte [] bytes =rabitMQUtil.produceHashMap(queueForRabbitMQ);
			
            queueForRabbitMQ.clear();
			sender.sendMessage(bytes);
			
			return true;
		}

		return false;

	}

	
	/**
	 * insert list of documents to the destination collection  and update the state 
	 * field in the source document also . 
	 * 
	 *@param sourceCollection 
	 *                     The ICollection implementation for the source collection which contains source documents  
	 *@param destinationCollection 
	 *                     The ICollection implementation for the production collection which will contain the synchronized documents 
	 *@param newDocuments
	 *                  List of Documents which will be synchronized and inserted to the destination collection 
	 * @param sender
	 *              RabbitMQ sender which will be used to save/produce the synchronized document id with the appropriate procedure (insert in this case ) 
	 *  
	 *               @return 
	 *                boolean True when the synchronization and insertion processes are completed successfully .False otherwise.
	 */ 
	public boolean syncronizeInsertionOneByOne(List<IDocument> newDocuments,
			ICollection sourceCollection, ICollection destinatCollection ,RabbitMQSender sender)  {
		
		  boolean completed =true;
		  Iterator<IDocument> it = newDocuments.iterator();
		
		  Map <String,String > queueForRabbitMQ =new HashMap<String,String>();
		
	      RabbitMQServerUtility rabitMQUtil =new RabbitMQServerUtility();
		
		while (it.hasNext()) {
			try {
				IDocument document = it.next();

				if (document.getState().equals(State.ACCEPTED)) {
					document.setState(State.IN_PRODUCTION);
					destinatCollection.insertDocument(document);
					sourceCollection.updateDocumentUsingId(document);
					
					queueForRabbitMQ.put(document.getId(),"insert");
					
					byte [] bytes =rabitMQUtil.produceHashMap(queueForRabbitMQ);
					
		            queueForRabbitMQ.clear();
					sender.sendMessage(bytes);
					

				}
		
				
			} catch (Exception e) {
				//e.printStackTrace();
				completed =false;
				
				break;
			}

		}
        
		return completed;
	}

}
