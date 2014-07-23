package com.zdh.crisom.model;

import java.util.ArrayList;

public class Product {
	private int id;
	private String name;
	private String image;
	private String shortDes;
	private String des;
	private ArrayList<OptionObject> listOption;
	private ArrayList<DocumentObject> listDocument;
	
	public Product(){
		id = 0 ;
		name ="";
		image ="";
		shortDes ="";
		des ="";
		listOption = new ArrayList<OptionObject>();
		listDocument = new ArrayList<DocumentObject>();
		
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
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getShortDes() {
		return shortDes;
	}
	public void setShortDes(String shortDes) {
		this.shortDes = shortDes;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public ArrayList<OptionObject> getListOption() {
		return listOption;
	}
	public void setListOption(ArrayList<OptionObject> listOption) {
		this.listOption = listOption;
	}
	public ArrayList<DocumentObject> getListDocument() {
		return listDocument;
	}
	public void setListDocument(ArrayList<DocumentObject> listDocument) {
		this.listDocument = listDocument;
	}
	
	
}
