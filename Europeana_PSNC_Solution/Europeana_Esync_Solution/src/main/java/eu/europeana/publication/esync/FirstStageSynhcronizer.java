package eu.europeana.publication.esync;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import eu.europeana.publication.common.ICollection;
import eu.europeana.publication.common.IDocument;
import eu.europeana.publication.common.State;
import eu.europeana.publication.rabbitmq.RabbitMQSender;


/**
 * 
 * FirstStageSynhcronizer.java Purpose: synchronize documents from source to
 * production batch by batch (insert,delete,update) operation based on the state
 * field values
 * 
 * @author Tarek Alkhaeir
 * @version 1.0.0
 */

public class FirstStageSynhcronizer {

	/**
	 * Read the documents which will be synchronized from the source batch by batch
	 * based on their states if the document already exist in the production update/delete it. If not
	 * insert a new document .
	 * 
	 * @param sourceCollection
	 *            The ICollection implementation for the source collection which contains source documents
	 * @param destinationCollection
	 *            The ICollection implementation for the production collection
	 *            which will contain the synchronized documents
	 * @param stateValues
	 *            list of state filed values for documents which will be
	 *            synchronized
	 * @param batchSize
	 *            the number of documents which will be synchronized every batch
	 * 
	 * @param sender
	 *            RabbitMQ sender which will be used to save/publish the
	 *            synchronized document id with the appropriate operation
	 *            (update -delete - insert)
	 * 
	 * @return boolean complete : to verify the correctness of the process. true
	 *         if everything is correct .False in case of unexpected exception !
	 */
	public boolean syncronizeToProduction(ICollection sourceCollection,
			ICollection destinationCollection, List<State> stateValues,Map<String,List<String>> queryChoices,
			int batchSize, RabbitMQSender sender)

	{

		FirstStageUtility spikeUtil = new FirstStageUtility();
		List<IDocument> copiedDocuments = null;
		List<IDocument> newDocuments = new ArrayList<IDocument>();
		int patchNumber = 0;
		Iterator<IDocument> iterator = null;
		boolean completed = true;
		// this loop will break if there is no more documents to be synchronized
		while (true) {
			try {
				System.out.println("tototototot");
				patchNumber++;
				copiedDocuments = spikeUtil
						.getDocumentsByStateToBeSyncronizedUsingBatch(
								sourceCollection, stateValues,queryChoices, batchSize);
				if (copiedDocuments.isEmpty())
					break;
				else {
					iterator = copiedDocuments.iterator();
					while (iterator.hasNext()) {

						IDocument document = iterator.next();
						System.out.println(document.getId());
						System.out.println("tototototot10");

						// if the document is not in destination collection,that
						// mean, it is a new document.
						if (!spikeUtil
								.updateAndSyncronizeExisteingDocumentUsingId(
										sourceCollection,
										destinationCollection, document, sender)) {

							newDocuments.add(document);
							System.out.println("tototototot5");

						}
					}

					if (newDocuments.size() == 0) {
						
					} else {
						if (spikeUtil
								.syncronizeInsertionOneByOne(newDocuments,
										sourceCollection,
										destinationCollection, sender) == true) {

							

							newDocuments.clear();
						} else {
							completed = false;
							
							break;
						}
					}
					copiedDocuments.clear();
				}
			} catch (Exception e) {
				// System.out.println("batch number " + patchNumber
				// + " failed please resynchronize");
			

				completed = false;
				break;

			}
		}

		// committing if necessary

		if (completed == true) {
			try {
				destinationCollection.commit();
			} catch (Exception e) {
				completed = false;
				
			}
		}
		return completed;

	}

}
