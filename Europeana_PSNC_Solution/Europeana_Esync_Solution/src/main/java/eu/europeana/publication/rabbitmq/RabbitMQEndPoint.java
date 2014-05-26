package eu.europeana.publication.rabbitmq;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 
 * RabbitMQEndPoint.java
 * Purpose: an abstract class which will be extended by RabbitMQ sender and receiver 
 * 
 * @author Tarek Alkhaeir
 * @version 1.0.0
 */


public abstract class RabbitMQEndPoint {

	protected Channel channel;
	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public String getEndPointName() {
		return endPointName;
	}

	public void setEndPointName(String endPointName) {
		this.endPointName = endPointName;
	}

	protected Connection connection;
	protected String endPointName;

	public RabbitMQEndPoint(String endpointName, String hostname, int port,String userName ,String password)
			throws IOException {
		this.endPointName = endpointName;

		// Create a connection factory
		ConnectionFactory factory = new ConnectionFactory();

		// hostname of your rabbitmq server
		factory.setHost(hostname);

		factory.setPort(port);
		
		factory.setUsername(userName);
			
		factory.setPassword(password);

		// getting a connection
		connection = factory.newConnection();

		// creating a channel
		channel = connection.createChannel();

		// declaring a queue for this channel. If queue does not exist,
		// it will be created on the server.
		
		channel.queueDeclare(endpointName, true, false, false, null);
	}

	public void close() throws IOException {
		this.channel.close();
		this.connection.close();
	}
}