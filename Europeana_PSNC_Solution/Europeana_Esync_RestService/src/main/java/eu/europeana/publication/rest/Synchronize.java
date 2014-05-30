package eu.europeana.publication.rest;



import java.io.IOException;

import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.Consumes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import eu.europeana.publication.common.ICollection;
import eu.europeana.publication.common.IDocument;
import eu.europeana.publication.common.State;
import eu.europeana.publication.configuration.AppConfiguration1;
import eu.europeana.publication.esync.FirstStageSynhcronizer;
import eu.europeana.publication.esync.SecondStageSycrhonizer;
import eu.europeana.publication.rabbitmq.RabbitMQReciever;
import eu.europeana.publication.rabbitmq.RabbitMQSender;
import eu.europeana.publication.restparameterswrapper.RestParametersWrapper;


@Path("/synchronization")
public class Synchronize {
	
		    @POST
		    @Consumes(MediaType.APPLICATION_XML)
		    @Produces(MediaType.APPLICATION_XML)
		    @Path("/firstStage")
		    public Response synchronize(RestParametersWrapper wrapper)
		    {
             
		    	RabbitMQSender sender =null;
				boolean completed = false;
				IDocument document=null;
				try {
					ApplicationContext context = new  AnnotationConfigApplicationContext(AppConfiguration1.class);
					
					
					  if (wrapper.getOtherBean().getCollectionName()!=null)
					 document = (IDocument) context.getBean(wrapper.getOtherBean().getCollectionName());

			    
			    	ICollection sourceCollection = (ICollection) context.getBean(
			    			wrapper.getOtherBean().getSourceType(), wrapper.getSourceBean().getIp(), wrapper.getSourceBean().getPort(), wrapper.getSourceBean().getDatabaseName(),
			    			wrapper.getSourceBean().getUserName(), wrapper.getSourceBean().getPassword(), document);
			     	
			    	ICollection destinationCollection = (ICollection) context.getBean(
			    			wrapper.getOtherBean().getDestinationType(), wrapper.getDestinationBean().getIp(), wrapper.getDestinationBean().getPort(), wrapper.getDestinationBean().getDatabaseName(),
			    			wrapper.getDestinationBean().getUserName(), wrapper.getDestinationBean().getPassword(), document);

				
					FirstStageSynhcronizer syncronizer = new FirstStageSynhcronizer();

					 sender = new RabbitMQSender("rabbitQueue3", wrapper.getRabbitBean().getRabbitMQIp(), wrapper.getRabbitBean().getRabbitMQPort(),wrapper.getRabbitBean().getRabbitMQUserName(),wrapper.getRabbitBean().getRabbitMQPassword());
					
					
					
					 List<State> stateValues =wrapper.getOtherBean().getStateKeys();
					 
						
					Map<String,List<String>> map =wrapper.getOtherBean().getQueryChoices().getMapProperty();
					
					
					completed = syncronizer.syncronizeToProduction(
							sourceCollection, destinationCollection,
							stateValues,map, wrapper.getOtherBean().getBatchSize(), sender);
					
					if (completed == true)
					{
						//Logging.log.info("The syncronization was completed succsessfully");
						return Response
								.status(200)
								.entity("The syncronization was completed succsessfully")
								.build();
					}
					else
					{
						//Logging.log.error("The syncronization was not completed succsessfully !! ..please resencronize");
						return Response
								.status(417)
								.entity("The syncronization was not completed succsessfully !! ..please resencronize")
								.build();
					}

				} catch (Exception e) {
					//Logging.log.error("an exception happened !!");
					e.printStackTrace();
					return Response.status(417).entity("an exception happened !!")
							.build();
				}
				finally
				{
					try
					{
					if (sender!=null)
						sender.close();
					}
					catch (IOException e)
					{
						//Logging.log.error("an error happened when closing RabbitMQ sender");
					}
				}
		    }
		    
		    
		    @POST
			@Path("/secondStage")
			public Response syncronize2(RestParametersWrapper wrapper)
			{
		    	IDocument document=null;
				try
				{
                    ApplicationContext context = new  AnnotationConfigApplicationContext(AppConfiguration1.class);
					
					
                    if (wrapper.getOtherBean().getCollectionName()!=null)
   					 document = (IDocument) context.getBean(wrapper.getOtherBean().getCollectionName());

			     	
			    	ICollection sourceCollection = (ICollection) context.getBean(
			    			wrapper.getOtherBean().getSourceType(), wrapper.getSourceBean().getIp(), wrapper.getSourceBean().getPort(), wrapper.getSourceBean().getDatabaseName(),
			    			wrapper.getSourceBean().getUserName(), wrapper.getSourceBean().getPassword(), document);
			    
			    	ICollection destinationCollection = (ICollection) context.getBean(
			    			wrapper.getOtherBean().getDestinationType(), wrapper.getDestinationBean().getIp(), wrapper.getDestinationBean().getPort(), wrapper.getDestinationBean().getDatabaseName(),
			    			wrapper.getDestinationBean().getUserName(), wrapper.getDestinationBean().getPassword(), document);

					
					RabbitMQReciever reciever = new RabbitMQReciever("rabbitQueue3",wrapper.getRabbitBean().getRabbitMQIp(), wrapper.getRabbitBean().getRabbitMQPort(),wrapper.getRabbitBean().getRabbitMQUserName(),wrapper.getRabbitBean().getRabbitMQPassword());
				SecondStageSycrhonizer synchronizer =new SecondStageSycrhonizer();
				boolean completed =synchronizer.syncronize(sourceCollection,destinationCollection, reciever);
				
				if (completed == true)
				{
					destinationCollection.commit();
				//	Logging.log.info("The documents were indexed  succsessfully to Destination");
					return Response.status(200).entity("The documents were indexed  succsessfully to Destination").build();
				}
				else
				{
					//Logging.log.error("The syncronization was not completed succsessfully !! ..please resencronize");
					return Response
							.status(417)
							.entity("The syncronization was not completed succsessfully !! ..please resencronize")
							.build();
				}
				}
				catch(Exception e)
				{
					//Logging.log.error("an exception happened in servers initiation !!");
					return Response.status(417).entity("an exception happened in servers initiation !!")
							.build();	
				}
				
			}
		}


		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		 