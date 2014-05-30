package eu.europeana.publication.restparameterswrapper;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import eu.europeana.publication.backendconfig.BackEndConfBean;
import eu.europeana.publication.otherconfig.SynchronizationParametersConfBean;
import eu.europeana.publication.rabbitmqconfig.RabbitMQBean;

 

@XmlRootElement(name = "Wrapper")
public class RestParametersWrapper {
	private BackEndConfBean sourceBean;
	private BackEndConfBean destinationBean;
	private RabbitMQBean rabbitBean;
    private SynchronizationParametersConfBean otherBean;
	
	public RestParametersWrapper()
	{
		
	}

	public BackEndConfBean getSourceBean() {
		return sourceBean;
	}
	@XmlElement
	public void setSourceBean(BackEndConfBean sourceBean) {
		this.sourceBean = sourceBean;
	}

	public BackEndConfBean getDestinationBean() {
		return destinationBean;
	}
	@XmlElement
	public void setDestinationBean(BackEndConfBean destinationBean) {
		this.destinationBean = destinationBean;
	}

	public RabbitMQBean getRabbitBean() {
		return rabbitBean;
	}
	@XmlElement
	public void setRabbitBean(RabbitMQBean rabbitBean) {
		this.rabbitBean = rabbitBean;
	}

	public SynchronizationParametersConfBean getOtherBean() {
		return otherBean;
	}
	@XmlElement
	public void setOtherBean(SynchronizationParametersConfBean otherBean) {
		this.otherBean = otherBean;
	}
	
	
}



