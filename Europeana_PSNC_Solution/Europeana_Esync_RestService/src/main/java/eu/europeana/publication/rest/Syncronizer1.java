package eu.europeana.publication.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

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
import eu.europeana.publication.utility.mapconverter.QueryChoicesMap;

@Path("/synchronization1")
public class Syncronizer1 {

	@POST
	@Path("/firstStage")
	public Response syncronize(
			QueryChoicesMap<String, List<String>> map1,
			@HeaderParam("sourceip") @DefaultValue("localhost") String sourceIp,
			@HeaderParam("sourceport") @DefaultValue("27017") int sourcePort,
			@HeaderParam("sourcedatabasename") @DefaultValue("users") String sourceDatabaseName,
			@HeaderParam("sourceusername") @DefaultValue("") String sourceUserName,
			@HeaderParam("sourcepassword") @DefaultValue("") String sourcePassword,

			@HeaderParam("destinationip") @DefaultValue("localhost") String destinationIp,
			@HeaderParam("destinationport") @DefaultValue("27018") int destinationPort,
			@HeaderParam("destinationdatabase_name") @DefaultValue("users") String destinationDatabaseName,
			@HeaderParam("destinationusername") @DefaultValue("") String destinationUserName,
			@HeaderParam("destinationpassword") @DefaultValue("") String destinationPassword,

			@HeaderParam("rabbitmqip") @DefaultValue("localhost") String rabbitMQIp,
			@HeaderParam("rabbitmqport") @DefaultValue("5672") int rabbitMQPort,
			@HeaderParam("rabbitmqusername") @DefaultValue("guest") String rabbitMQUserName,
			@HeaderParam("rabbitmqpassword") @DefaultValue("guest") String rabbitMQPassword,

			@HeaderParam("batchsize") @DefaultValue("5") int batchSize,
			@HeaderParam("states") @DefaultValue("all") List<String> stateKeys,

			@HeaderParam("sourcetype") @DefaultValue("mongoCollection") String sourceType,
			@HeaderParam("destinationtype") @DefaultValue("mongoCollection") String destinationType

	)

	{
		RabbitMQSender sender = null;
		boolean completed = false;
		IDocument document = null;
		try {
			ApplicationContext context = new AnnotationConfigApplicationContext(
					AppConfiguration1.class);

			if (map1.getMapProperty().get("collection") != null)
			{
				System.out.println(map1.getMapProperty()
						.get("collection").get(0).toString());
				document = (IDocument) context.getBean(map1.getMapProperty()
						.get("collection").get(0).toString());
			}
			ICollection sourceCollection = (ICollection) context.getBean(
					sourceType, sourceIp, sourcePort, sourceDatabaseName,
					sourceUserName, sourcePassword, document);

			ICollection destinationCollection = (ICollection) context.getBean(
					destinationType, destinationIp, destinationPort,
					destinationDatabaseName, destinationUserName,
					destinationPassword, document);

			FirstStageSynhcronizer syncronizer = new FirstStageSynhcronizer();

			sender = new RabbitMQSender("rabbitQueue3", rabbitMQIp,
					rabbitMQPort, rabbitMQUserName, rabbitMQPassword);

			List<String> listStateKeys = Arrays.asList(stateKeys.get(0)
					.replace("[", "").replace("]", "").split(","));
			List<State> stateValues = new ArrayList<State>();

			if (listStateKeys.get(0).toString().equalsIgnoreCase("All")) {
				stateValues.add(State.ACCEPTED);
				stateValues.add(State.TO_BE_WITHDRAWN);
				stateValues.add(State.TO_BE_DELETED);
			} else {

				Iterator<String> iterator = listStateKeys.iterator();
				while (iterator.hasNext()) {

					String value = iterator.next();
					stateValues.add(State.valueOf(value.toUpperCase().trim()));
				}
			}

			Map<String, List<String>> map = map1.getMapProperty();

			completed = syncronizer.syncronizeToProduction(sourceCollection,
					destinationCollection, stateValues, map, batchSize, sender);

			if (completed == true) {
				// Logging.log.info("The syncronization was completed succsessfully");
				return Response
						.status(200)
						.entity("The syncronization was completed succsessfully")
						.build();
			} else {
				// Logging.log.error("The syncronization was not completed succsessfully !! ..please resencronize");
				return Response
						.status(417)
						.entity("The syncronization was not completed succsessfully !! ..please resencronize")
						.build();
			}

		} catch (Exception e) {
			// Logging.log.error("an exception happened !!");
			e.printStackTrace();
			return Response.status(417).entity("an exception happened !!")
					.build();
		} finally {
			try {
				if (sender != null)
					sender.close();
			} catch (IOException e) {
				// Logging.log.error("an error happened when closing RabbitMQ sender");
			}
		}
	}

	@POST
	@Path("/secondStage")
	public Response syncronize2(
			@FormParam("sourceip") @DefaultValue("localhost") String sourceIp,
			@FormParam("sourceport") @DefaultValue("27018") int sourcePort,
			@FormParam("sourcedatabasename") @DefaultValue("users") String sourceDatabaseName,
			@FormParam("sourceusername") @DefaultValue("") String sourceUserName,
			@FormParam("sourcepassword") @DefaultValue("") String sourcePassword,
			@FormParam("collectionName") @DefaultValue("User") String collectionName,
			

			@FormParam("destinationip") @DefaultValue("localhost") String destinationIp,
			@FormParam("destinationport") @DefaultValue("8983") int destinationPort,
			@FormParam("destinationdatabase_name") @DefaultValue("") String destinationDatabaseName,
			@FormParam("destinationusername") @DefaultValue("") String destinationUserName,
			@FormParam("destinationpassword") @DefaultValue("") String destinationPassword,

			@FormParam("rabbitmqip") @DefaultValue("localhost") String rabbitMQIp,
			@FormParam("rabbitmqport") @DefaultValue("5672") int rabbitMQPort,
			@FormParam("rabbitmqusername") @DefaultValue("guest") String rabbitMQUserName,
			@FormParam("rabbitmqpassword") @DefaultValue("guest") String rabbitMQPassword,

			@FormParam("sourcetype") @DefaultValue("mongoCollection") String sourceType,
			@FormParam("destinationtype") @DefaultValue("solrCollection") String destinationType

	)

	{

		IDocument document = null;
		try {
			ApplicationContext context = new AnnotationConfigApplicationContext(
					AppConfiguration1.class);

			if (collectionName != null)
				document = (IDocument) context.getBean(collectionName);

			ICollection sourceCollection = (ICollection) context.getBean(
					sourceType, sourceIp, sourcePort, sourceDatabaseName,
					sourceUserName, sourcePassword, document);

			ICollection destinationCollection = (ICollection) context.getBean(
					destinationType, destinationIp, destinationPort,
					destinationDatabaseName, destinationUserName,
					destinationPassword, document);

			RabbitMQReciever reciever = new RabbitMQReciever("rabbitQueue3",
					rabbitMQIp, rabbitMQPort, rabbitMQUserName,
					rabbitMQPassword);

			SecondStageSycrhonizer synchronizer = new SecondStageSycrhonizer();
			boolean completed = synchronizer.syncronize(sourceCollection,
					destinationCollection, reciever);

			if (completed == true) {
				destinationCollection.commit();
				// Logging.log.info("The documents were indexed  successfully to Destination");
				return Response
						.status(200)
						.entity("The documents were indexed  succsessfully to Destination")
						.build();
			} else {
				// Logging.log.error("The synchronization was not completed successfully !! ..please resynchronized");
				return Response
						.status(417)
						.entity("The syncronization was not completed succsessfully !! ..please resencronize")
						.build();
			}
		} catch (Exception e) {
			// Logging.log.error("an exception happened in servers initiation !!");
			return Response.status(417)
					.entity("an exception happened in servers initiation !!")
					.build();
		}

	}
}
