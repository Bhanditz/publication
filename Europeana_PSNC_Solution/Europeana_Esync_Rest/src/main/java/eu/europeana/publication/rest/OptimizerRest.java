package eu.europeana.publication.rest;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.solr.client.solrj.SolrServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import eu.europeana.publication.configuration.AppConfiguration1;
import eu.europeana.publication.esync.Optimizer;
import eu.europeana.publication.solr.SolrCollection;


@Path("/optimize")
public class OptimizerRest {
		
		@GET
		@Path("/optimizeSolr")
		public Response optimize(@DefaultValue("http://localhost:8983/solr") @QueryParam("solrURL") String solrURL)
		{
			try
			{
			ApplicationContext context = new AnnotationConfigApplicationContext(AppConfiguration1.class);
					
			SolrCollection solrcollection = (SolrCollection) context.getBean(
					"solrCollection", solrURL);	
			SolrServer server =solrcollection.getServer();
			Optimizer optimizer =new Optimizer();
			optimizer.optimizeSolr(server);
			
		//	Logging.log.info("The solr optimaization was completed successfully");
			return Response.status(200).entity("The solr optimaization was completed successfully").build();
			}
			catch(Exception e)
			{
			//	Logging.log.error("An exception happened !!");
				return Response.status(417).entity("An exception happened !!")
						.build();	
			}
		}
	}

