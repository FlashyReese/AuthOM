package me.wilsonhu.authom;

public class Account {
	private String name;
	private String password;
	private String ip;
	
	public Account(String name, String password, String ip) {
		this.setName(name);
		this.setPassword(password);
		this.setIp(ip);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}

}
