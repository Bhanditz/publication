package eu.europeana.publication.rabbitmq;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * RabbitMQServerUtility.java
 * Purpose: a utility class for RabbitMq 
 * producing and consuming HashMap
 * 
 * 
 * @author Tarek Alkhaeir
 * @version 1.0.0
 */

public class RabbitMQServerUtility {
	/**
	 * convert map to array of byte 
	 * 
	 *@param map 
	 *                 a map of document id and its related synchronization operation
	 *@return 
	 *                 an array of bytes which is the result of the converting of the map argument
	 *                 
	 *@throws IOException                                  
      */ 
	public byte[] produceHashMap(Map<String, List<String>> map) throws IOException {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(map);

		byte[] bytes = bos.toByteArray();
        oos.close();
		
        return bytes;

	}
	/**
	 * convert  array of byte to map
	 * 
	 *@param bytes 
	 *                an array of bytes which will be converted to a map 
	 *@return 
	 *                a map of document id and its related synchronization operation
	 *                 
	 *@throws IOException                                  
      */ 
	public Map<String, List<String>> consumeHashMap(byte[] bytes) throws IOException,ClassNotFoundException  {

		Map<String, List<String>> map = null;

		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		ObjectInputStream obj = new ObjectInputStream(bais);
		Object object = obj.readObject();
		map = (HashMap<String, List<String>>) object;
		obj.close();

		return map;

	}

}
