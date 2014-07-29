package com.zdh.crimson.model;

public class Category {
	private int id;
	private String name;
	private boolean subcat;
	
	private int idParent;
	
	public Category(){}
	
	public Category(int id, String name, boolean subcat){
		this.id = id;
		this.name = name;
		this.subcat = subcat;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public boolean getSubcat() {
		return subcat;
	}

	public void setSubcat(boolean subcat) {
		this.subcat = subcat;
	}

	public int getIdParent() {
		return idParent;
	}

	public void setIdParent(int idParent) {
		this.idParent = idParent;
	}
	
	
}
