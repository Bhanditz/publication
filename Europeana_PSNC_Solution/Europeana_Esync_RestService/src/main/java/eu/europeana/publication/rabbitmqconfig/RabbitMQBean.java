package eu.europeana.publication.rabbitmqconfig;



import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "RabbitMQ")
public class RabbitMQBean {
	private  String rabbitMQIp;
	private int rabbitMQPort;
	private String rabbitMQUserName;
	private String rabbitMQPassword;
	
	
	public RabbitMQBean()
	{
		
	}
	
	public String getRabbitMQIp() {
		return rabbitMQIp;
	}
	@XmlElement
	public void setRabbitMQIp(String rabbitMQIp) {
		this.rabbitMQIp = rabbitMQIp;
	}
	public int getRabbitMQPort() {
		return rabbitMQPort;
	}
	@XmlElement
	public void setRabbitMQPort(int rabbitMQPort) {
		this.rabbitMQPort = rabbitMQPort;
	}
	public String getRabbitMQUserName() {
		return rabbitMQUserName;
	}
	@XmlElement
	public void setRabbitMQUserName(String rabbitMQUserName) {
		this.rabbitMQUserName = rabbitMQUserName;
	}
	public String getRabbitMQPassword() {
		return rabbitMQPassword;
	}
	@XmlElement
	public void setRabbitMQPassword(String rabbitMQPassword) {
		this.rabbitMQPassword = rabbitMQPassword;
	}
}

