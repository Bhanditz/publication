package eu.europeana.publication.backendconfig;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "AbstractBean")
    public  class BackEndConfBean {
	
	
	private String ip;
	private  int port;
    private String databaseName;
	private String userName;
	private String password;
		
	
		
	public BackEndConfBean()
	{
		
	}

	public String getIp() {
		return ip;
	}

	@XmlElement
	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}
	@XmlElement
	public void setPort(int port) {
		this.port = port;
	}

	public String getDatabaseName() {
		return databaseName;
	}
	@XmlElement
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getUserName() {
		return userName;
	}
	@XmlElement
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}
	@XmlElement
	public void setPassword(String password) {
		this.password = password;
	}
}	