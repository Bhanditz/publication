package eu.europeana.publication.esync;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.QueueingConsumer;

import eu.europeana.publication.common.ICollection;
import eu.europeana.publication.common.IDocument;
import eu.europeana.publication.logging.Logging;
import eu.europeana.publication.rabbitmq.RabbitMQReciever;
import eu.europeana.publication.rabbitmq.RabbitMQServerUtility;

/**
 * 
 * SecondStageSycrhonizer.java Purpose: synchronize documents from source to
 * destination one document by one
 * 
 * @author Tarek Alkhaeir
 * @version 1.0.0
 */

public class SecondStageSycrhonizer {

	/**
	 * synchronize documents from source to destination one document by one
	 * consume the documents Ids from RabbitMQ receiver with its related
	 * operation (insert update delete) use those Ids to query and return
	 * documents from the source collection before synchronizing them to the
	 * destination collection.
	 * TimeoutException will accrued only if there is no
	 * more data to consume from the RabbitMQ which mean the data consumed
	 * correctly
	 * 
	 * @param sourceCollection
	 *            The ICollection implementation for the source collection which
	 *            contains source documents
	 * @param destinationCollection
	 *            The ICollection implementation for the production collection
	 * @param reciever
	 *            RabbitMQ receiver which will be used to consume the
	 *            synchronized document id with the appropriate operation
	 *            (insert- update -delete )
	 * @return boolean True when the synchronization and update processes are
	 *         completed successfully .False otherwise.
	 */
	public boolean syncronize(ICollection sourceCollection,
			ICollection destinationCollection, RabbitMQReciever reciever) {

		boolean completed = true;

		try {

			RabbitMQServerUtility utility = new RabbitMQServerUtility();

			QueueingConsumer consumer = new QueueingConsumer(
					reciever.getChannel());

			reciever.getChannel().basicConsume(reciever.getEndPointName(),
					true, consumer);
			Map<String, String> map;

			while (true) {
				try {

					map = reciever.recieveMessage(utility, consumer);

					for (String key : map.keySet()) {

						if (map.get(key).equals("insert")) {
							IDocument sourceDocument = sourceCollection
									.getDocumentById(key);
							destinationCollection
									.insertDocument(sourceDocument);

						} else if (map.get(key).equals("update")) {
							IDocument sourceDocument = sourceCollection
									.getDocumentById(key);
							destinationCollection
									.updateDocumentUsingId(sourceDocument);

						}

						else if (map.get(key).equals("delete")) {
							IDocument sourceDocument = sourceCollection
									.getDocumentById(key);
							destinationCollection
									.deleteDocument(sourceDocument);

						}

					}

				} catch (TimeoutException e) {
					// System.out.println("timed out no data to be consumed");
					Logging.log.info("timed out no data to be consumed ");
					// committing to destination if necessary
					try {
						Logging.log
								.info("commiting your changes to destination");
						destinationCollection.commit();
						Logging.log
								.info("synchronization and commiting your changes to destination were executed successfully ");
					} catch (Exception e1) {
						completed = false;
						Logging.log
								.error("an error accoured during commiting ");
						break;

					}
					Logging.log
							.info("the synchronization completed successfully");
					break;
				} catch (Exception e) {

					e.printStackTrace();
					Logging.log
							.error("an exception happened when synchronizing to destination");
					completed = false;
					break;
				}
			}

		} catch (IOException e) {
			completed = false;

			Logging.log
					.error("an IOException happened when synchronizing to destination it is related to RabbitMq");

		} finally {
			try {
				reciever.close();

			} catch (IOException e) {
				// System.out.println("an exception just happened when trying to close the rabbitMQ reciever");
				Logging.log
						.error("an exception just happened when trying to close the rabbitMQ reciever");
			}
		}

		return completed;
	}
}
