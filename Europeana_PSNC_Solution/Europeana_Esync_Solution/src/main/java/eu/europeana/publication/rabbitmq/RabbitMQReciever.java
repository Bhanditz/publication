package eu.europeana.publication.rabbitmq;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

import eu.europeana.publication.common.IDocument;

/**
 * 
 * RabbitMQReciever.java
 * Purpose: define the RabbitMQ receiver class 
 * 
 * @author Tarek Alkhaeir
 * @version 1.0.0
 */

public class RabbitMQReciever extends RabbitMQEndPoint {

	public RabbitMQReciever(String endpointName, String hostname, int port,String userName,String password)
			throws IOException {
		super(endpointName, hostname, port,userName,password);
	}

	
	/**
	 * consume a message from a RabbitM queue 
	 * 
	 *
	 *@param util
	 *         
	 *@param consumer 
	 *                a QueueingConsumer used to consume the next queue delivery
	 *                 
	 *@throws TimeoutException 
	 *                  if 20 seconds has passed without new data to consume (no more data to consume) this exception will be thrown
	 *                  and it should be caught to announce the completion of the synchronization process       
	 *@throws InterruptedException , IOException , ClassNotFoundException
	 *                  re throws RabbitMQ api exceptions                            
      */ 
	public Map<IDocument, String> recieveMessage(RabbitMQServerUtility util,
			QueueingConsumer consumer) throws InterruptedException,TimeoutException,
			IOException, ClassNotFoundException {

		Delivery delivery = consumer.nextDelivery(20000);

		if (delivery == null)
			throw new TimeoutException();
			

		channel.basicConsume(endPointName, true, consumer);

		byte[] bytes = delivery.getBody();

		Map<IDocument, String> map = util.consumeHashMap(bytes);
		
		return map;
	}
}
