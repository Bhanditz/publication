package eu.europeana.publication.test;

import java.util.ArrayList;
import java.util.List;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import eu.europeana.publication.backendconfig.BackEndConfBean;
import eu.europeana.publication.common.State;
import eu.europeana.publication.otherconfig.SynchronizationParametersConfBean;
import eu.europeana.publication.rabbitmqconfig.RabbitMQBean;
import eu.europeana.publication.restparameterswrapper.RestParametersWrapper;
import eu.europeana.publication.utility.mapconverter.QueryChoicesMap;


public class Test {
	public static void main(String[] args) {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client
				.resource("http://localhost:8080/Europeana_Esync_RestService/rest/synchronization/firstStage");

		BackEndConfBean sourceBean = new BackEndConfBean();
		sourceBean.setIp("localhost");
		sourceBean.setPort(27017);
		sourceBean.setDatabaseName("users");
		sourceBean.setUserName("");
		sourceBean.setPassword("");

		BackEndConfBean destinationBean = new BackEndConfBean();
		destinationBean.setIp("localhost");
		destinationBean.setPort(27018);
		destinationBean.setDatabaseName("users");
		destinationBean.setUserName("");
		destinationBean.setPassword("");
		
		RabbitMQBean bean = new RabbitMQBean();
		bean.setRabbitMQIp("localhost");
		bean.setRabbitMQPassword("guest");
		bean.setRabbitMQPort(5672);
		bean.setRabbitMQUserName("guest");

		// Now do the MAP stuff
		QueryChoicesMap<String, List<String>> map = new QueryChoicesMap<String, List<String>>();
		List<String> alist = new ArrayList<String>();
		alist.add("tote");
		alist.add("tata");
		map.getMapProperty().put("dataSet", alist);

		SynchronizationParametersConfBean other = new SynchronizationParametersConfBean();
		other.setBatchSize(5);
		other.setCollectionName("User");
		other.setDestinationType("Mongo");
		other.setQueryChoices(map);
		other.setSourceType("mongoCollection");
		other.setDestinationType("mongoCollection");
		
		
		List<State> values = new ArrayList<State>();
		values.add(State.ACCEPTED);
		values.add(State.TO_BE_DELETED);
		values.add(State.TO_BE_WITHDRAWN);
		other.setStateKeys(values);
		

	
		RestParametersWrapper w = new RestParametersWrapper();
		w.setSourceBean(sourceBean);
		w.setDestinationBean(destinationBean);
		w.setOtherBean(other);
		w.setRabbitBean(bean);

		ClientResponse response = service.accept("application/xml").post(ClientResponse.class,
				w);

		System.out.println(response.getEntity(String.class));
		
		
	//Stage two
		service = client
				.resource("http://localhost:8080/Europeana_Esync_RestService/rest/synchronization/secondStage");
		BackEndConfBean destinationBean1 = new BackEndConfBean();
		destinationBean1.setIp("localhost");
		destinationBean1.setPort(8983);
		destinationBean1.setDatabaseName("");
		destinationBean1.setUserName("");
		destinationBean1.setPassword("");
		
		other.setDestinationType("solrCollection");
		w.setDestinationBean(destinationBean1);
		ClientResponse response1 = service.accept("application/xml").post(ClientResponse.class,
				w);

		System.out.println(response1.getEntity(String.class));
	
		
		
	}
}
