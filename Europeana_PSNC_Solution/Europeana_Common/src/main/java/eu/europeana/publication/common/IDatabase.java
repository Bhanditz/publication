package eu.europeana.publication.common;

import java.util.List;

public interface IDatabase {
	abstract public ICollection getCollection(String collectionName);

	abstract public List<ICollection> getCollections();

}
