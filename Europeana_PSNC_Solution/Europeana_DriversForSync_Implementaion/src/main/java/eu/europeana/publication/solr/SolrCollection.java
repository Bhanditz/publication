package eu.europeana.publication.solr;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.stereotype.Component;

import eu.europeana.publication.common.ICollection;
import eu.europeana.publication.common.IDocument;
import eu.europeana.publication.common.State;


/**
 * 
 * SolrCollection.java
 * Purpose: The ICollection implementation for Solr  
 * all the methods were implemented as a spike solution 
 * using spring and User.java as bean which mean
 * (User bean documents shall be synchronized)
 * 
 *  
 *
 * @author Tarek Alkhaeir
 * @version 1.0.0
 */


@Component("solr")
public class SolrCollection  implements ICollection {
	private SolrServer server;
	
	public SolrCollection(SolrServer server)
	{
		this.server=server;
	}
	
	
	public SolrServer getServer() {
		return server;
	}

	public void setServer(SolrServer server) {
		this.server = server;
	}

	
	
	
	public List<IDocument> getDocumentsByStatesUsingBatch(State[] stateVlues,
			int batchSize) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public List<IDocument> getDocumentsByStateUsingBatch(State state,
			int batchSize) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public IDocument getDocumentById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void insertDocument(IDocument document)  {
		// TODO Auto-generated method stub
		
		
		try {
			server.addBean(document);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("io");
						
			e.printStackTrace();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			System.out.println("sexce");
			e.printStackTrace();
		}
		
				
		
			
			
		
	}

	
	public void deleteDocument(IDocument document) {
		// TODO Auto-generated method stub
		
		try
		{
		server.deleteById(document.getId());
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		

	}

	
	public void updateDocumentUsingId(IDocument document) {
		// TODO Auto-generated method stub
		try
		{
		
		server.addBean(document);
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	
	public void cloneDocument(IDocument originalDocument,
			IDocument clonedDocument) {
		// TODO Auto-generated method stub

	}


	public List<IDocument> getDocumentsByStatesUsingBatch(
			List<State> stateVlues,Map<String,List<String>> queryChoices, int batchSize) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void commit() throws Exception
	{
		
		server.commit(true,true);
		
		
	}

}
