package com.oliweb.mail;

public class PropertiesMail {
	private String host;
	private String port;
	private String from;
	private String username;
	private String password;

	public PropertiesMail(String host, String port, String from, String username, String password) {
		super();
		this.host = host;
		this.port = port;
		this.from = from;
		this.username = username;
		this.password = password;
	}
	
	
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
