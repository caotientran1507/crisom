package com.zdh.crisom.model;

public class Customer {
	int id;
	String email;
	String pass;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	@Override
	public String toString() {
		return "Customer [id=" + id + ", email=" + email + ", pass=" + pass
				+ "]";
	}
	
	
	
}
