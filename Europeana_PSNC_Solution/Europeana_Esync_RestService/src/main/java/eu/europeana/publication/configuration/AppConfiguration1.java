package eu.europeana.publication.configuration;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClient;

import eu.europeana.publication.common.IDocument;
import eu.europeana.publication.mongo.MongoCollection;
import eu.europeana.publication.solr.SolrCollection;

public class AppConfiguration1 {
	
	public @Bean
	(name="mongoFactory")
	@Scope("prototype")
	
	MongoDbFactory mongoDbFactory(String host,int port ,String databaseName,UserCredentials credintials) throws Exception {
		
		return new SimpleMongoDbFactory (new MongoClient(host, port),
				databaseName,credintials);
	}

	public @Bean
	(name="mongoTemplate")
	@Scope("prototype")
	
	MongoTemplate mongoTemplate(String host,int port ,String databaseName,UserCredentials credentials) throws Exception {

		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory(host, port,databaseName,credentials));
		
		return mongoTemplate;

	}
	
	public @Bean
	(name="mongoCollection")
	@Scope("prototype")
	MongoCollection mongoTCollection(String host,int port ,String databaseName,String userName,String password,IDocument collectionName) throws Exception {
		UserCredentials credintials =new UserCredentials(userName, password);
		MongoCollection mongoCollection = new MongoCollection(mongoTemplate(host, port,databaseName,credintials),collectionName);
		return mongoCollection;

	}
	
	
	@Bean
	@Scope("prototype")
	public SolrServer solrServer(String host, int port,
			String databaseName,String userName,String password) {
		String url= "http://"+host+":"+port+"/solr"; 
		
		return new HttpSolrServer(url);
	}

		
	@Bean 
	(name="solrCollection")
	@Scope("prototype")
	
	public SolrCollection solrCollection(String host, int port,
			String databaseName,String userName,String password) throws Exception
	{
		
		return new SolrCollection(solrServer(host, port,
				databaseName,userName,password));
	}
	
	
	
}
