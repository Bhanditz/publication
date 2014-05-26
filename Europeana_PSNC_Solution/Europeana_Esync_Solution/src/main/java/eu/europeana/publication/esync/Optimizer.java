package eu.europeana.publication.esync;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;



/**
 * 
 * Optimizer.java
 * Purpose: optimize solr and compact MongoDB (still need to be verified based on Europeana requirements !!)
 *
 * @author Tarek Alkhaeir
 * @version 1.0.0
 */
public class Optimizer {
	
	public void optimizeSolr(SolrServer server) throws IOException ,SolrServerException
	{
		server.optimize(true, true) ;
	}
	
	public void compactCollection(DB db)
	{
		db.command(new BasicDBObject("repairDatabase", 1));
		
	}

}
