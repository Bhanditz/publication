package eu.europeana.publication.rabbitmq;

import java.io.IOException;

import com.rabbitmq.client.AMQP;

/**
 * 
 * RabbitMQSender.java
 * Purpose: define the RabbitMQ sender class 
 * 
 * @author Tarek Alkhaeir
 * @version 1.0.0
 */
public class RabbitMQSender extends RabbitMQEndPoint {
	
	public RabbitMQSender(String endpointName,String hostname,int port,String userName,String password) throws IOException
	{
		super(endpointName,hostname,port,userName,password);
	}
	
	
	/**
	 * publish a message to the RabbitM queue 
	 * 
	 *@param bytpeMap 
	 *                 an array of byte which is actually a map of (document id,appropriate operation (insert -update-delete))
      */  
	public void sendMessage(byte[] bytpeMap) throws IOException {
		

		channel.basicPublish("", endPointName,
                new AMQP.BasicProperties.Builder().contentType("text/plain").deliveryMode(2).build(),
                  bytpeMap);
		
		
		}   

}
