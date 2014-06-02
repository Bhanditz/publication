package eu.europeana.publication.test;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.representation.Form;

import eu.europeana.publication.common.State;
import eu.europeana.publication.utility.mapconverter.QueryChoicesMap;

public class Test1 {
	
	public static void main(String[] args)
	{
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client
				.resource("http://localhost:8080/Europeana_Esync_RestService/rest/synchronization1/firstStage");
		
		
		
				// Now do the MAP stuff
				QueryChoicesMap<String, List<String>> map = new QueryChoicesMap<String, List<String>>();
				List<String> alist = new ArrayList<String>();
				alist.add("tote");
				alist.add("tata");
				map.getMapProperty().put("dataSet", alist);
				
				List<String> blist = new ArrayList<String>();
				blist.add("User");
				map.getMapProperty().put("collection", blist);
				
				
				List<String> states =new ArrayList<String>();
				states.add("all");
				
				
				
				
				ClientResponse response = service.type(MediaType.APPLICATION_XML)
						.accept(MediaType.APPLICATION_XML).header("sourceip", "localhost").header("sourceport", "27017").
						header("sourcedatabasename", "users").header("sourceusername", "").header("sourcepassword", "").
						header("destinationip", "localhost").
						header("destinationport", "27018").header("destinationdatabase_name", "users").
						header("destinationusername", "").header("destinationpassword", "").
						header("rabbitmqip", "localhost").header("rabbitmqport", "5672").
						header("rabbitmqusername", "guest").header("rabbitmqpassword", "guest").
						header("batchsize", "5").header("states", states).
						header("sourcetype", "mongoCollection").header("destinationtype", "mongoCollection").
						post(ClientResponse.class, map);
				
				
				service = client
						.resource("http://localhost:8080/Europeana_Esync_RestService/rest/synchronization1/secondStage");
				
				Form form = new Form();    
				   
				form.add("sourceip", "localhost");    
				form.add("sourceport", "27018");  
				form.add("sourcedatabasename", "users"); 
				
				form.add("sourceusername", "");    
				form.add("sourcepassword", "");    
												
				form.add("destinationip", "localhost");    
				form.add("destinationport", "8983");    
				form.add("destinationdatabase_name", ""); 
				
				form.add("destinationusername", "");    
				form.add("destinationpassword", "");    
				form.add("rabbitmqip", "localhost");  
				
				form.add("rabbitmqport", "5672");    
				form.add("rabbitmqusername", "guest");    
				form.add("rabbitmqpassword", "guest");  
				
				form.add("sourcetype", "mongoCollection");    
				form.add("destinationtype", "solrCollection");  
				
				
				 response = service.type(MediaType.APPLICATION_XML)
							.accept(MediaType.APPLICATION_XML).header("sourceip", "localhost").header("sourceport", "27018").
							header("sourcedatabasename", "users").header("sourceusername", "").header("sourcepassword", "").
							header("destinationip", "localhost").
							header("destinationport", "8983").header("destinationdatabase_name", "").
							header("destinationusername", "").header("destinationpassword", "").
							header("rabbitmqip", "localhost").header("rabbitmqport", "5672").
							header("rabbitmqusername", "guest").header("rabbitmqpassword", "guest").
							header("sourcetype", "mongoCollection").header("destinationtype", "solrCollection").
							post(ClientResponse.class, map);
				
				System.out.println(response.getEntity(String.class));
				

	}

}
