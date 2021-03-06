package login;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 329498305586709975L;
	private int id;
	private String username;
	private String password;
	private String sex;
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	public User(String username, String password, String sex) {
		super();
		this.username = username;
		this.password = password;
	
		this.sex = sex;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() throws UnsupportedEncodingException {
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
	
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password="
				+ password +  ", sex=" + sex + "]";
	}
	
}
