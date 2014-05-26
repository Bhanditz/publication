package eu.europeana.publication.mongo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.mongodb.MongoClient;

import eu.europeana.publication.common.ICollection;
import eu.europeana.publication.common.IDocument;
import eu.europeana.publication.common.State;


/**
 * 
 * MongoCollection.java
 * Purpose: The ICollection implementation for MongoDB  
 * all the methods were implemented as a spike solution 
 * using spring and User.java as bean which mean
 * (User bean documents shall be synchronized)
 * 
 *  
 *
 * @author Tarek Alkhaeir
 * @version 1.0.0
 */

@Component("mongo")
public class MongoCollection implements ICollection {
	private MongoOperations mongoOperation;
	private IDocument collectionName;

	
	public MongoCollection(MongoOperations mongoOperation , IDocument collectionName) {
		this.mongoOperation = mongoOperation;
		this.collectionName=collectionName;
	}
	
	
	public MongoOperations getMongoClient() {
	
		return mongoOperation;
	}

	public List<IDocument> getDocumentsByStatesUsingBatch(List<State> stateVlues,Map<String,List<String>> queryChoices,
			int batch) {

		Query searchUserQuery = new Query();
		
		State[] stateValuesToArray = stateVlues.toArray(new State[stateVlues.size()]);
		Criteria serachCreteria =Criteria.where("state").in(stateValuesToArray);
		
		for (String key : queryChoices.keySet()) {
		String[] queryChoicesValue=	queryChoices.get(key).toArray(new String[queryChoices.get(key).size()]);
			serachCreteria =serachCreteria.andOperator(Criteria.where(key).in(queryChoicesValue));
			
		}
		searchUserQuery.addCriteria(serachCreteria);
         
         
		List<IDocument> savedUser = (List<IDocument>) mongoOperation.find (
				searchUserQuery.limit(batch),collectionName.getClass());
		Iterator<IDocument> it = savedUser.iterator();
		List<IDocument> documentList = new ArrayList<IDocument>();

		while (it.hasNext()) {
			documentList.add(it.next());
			}

		return documentList;

	}

	public IDocument getDocumentById(String id) {

		Query query2 = new Query();
		query2.addCriteria(Criteria.where("_id").is(id));
		IDocument userTest2 = mongoOperation.findOne(query2, collectionName.getClass());

		return userTest2;

	}

	public void insertDocument(IDocument document) {

		mongoOperation.insert(document);

	}

	public void deleteDocument(IDocument docuemnt) {

		Query query2 = new Query();
		query2.addCriteria(Criteria.where("_id").is(docuemnt.getId()));
		IDocument userTest2 = mongoOperation.findOne(query2,collectionName.getClass());
		mongoOperation.remove(userTest2);

	}

	public void updateDocumentUsingId(IDocument document) {

		Query query2 = new Query();
		query2.addCriteria(Criteria.where("_id").is(document.getId()));
		IDocument userTest2 = mongoOperation.findOne(query2, collectionName.getClass());
		if (userTest2 != null)
			mongoOperation.save(document);
		
		

	}

	public void cloneDocument(IDocument originalDocument,
			IDocument clonedDocument) {

		Mapper mapper = new DozerBeanMapper();
		mapper.map(originalDocument, clonedDocument);

	}

	
	public List<IDocument> getDocumentsByStateUsingBatch(State state,int batchSize) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public  void commit() throws Exception
	{
		
	}

}
