package eu.europeana.publication.mongo;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.apache.solr.client.solrj.beans.Field;

import eu.europeana.publication.common.IDocument;
import eu.europeana.publication.common.State;

/**
 * 
 * User.java
 * Purpose: The IDocument implementation for MongoDB and Solr  
 * using spring annotation for both Solr and MongoDB
 * the collection  which will contains the User document will be named user
 * 
 * @author Tarek Alkhaeir
 * @version 1.0.0
 */


@Document(collection = "user")
public class User implements IDocument ,Serializable {

	@Id
	@Field("id")
	private String id;

	@Field("name")
	private String userName;
	@Field("password")
	private String password;
	@Field("state")
	State state;
	
	@Field("dataSet")
	String dataSet ;

	public String getDataSet() {
		return dataSet;
	}

	public void setDataSet(String dataSet) {
		this.dataSet = dataSet;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public User()
	{
		
	}
	
	/*
	public User(String userName, String password, State state) {
		this.userName = userName;
		this.password = password;
		this.state = state;
	}
*/
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	// getter, setter, toString, Constructors

    @Override
    public void setClassType(String classType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getClassType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
