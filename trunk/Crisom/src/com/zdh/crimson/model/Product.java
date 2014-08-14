package com.zdh.crimson.model;

import java.util.ArrayList;

public class Product {
	private int id;
	private String name;
	private String image;
	private String shortDes;
	private String des;
	private String url;
	private String faq; 
	private ArrayList<OptionObject> listOption;
	private ArrayList<SpecsObject> listSpecs;
	private ArrayList<DocumentObject> listDocument;
	private ArrayList<VideoObject> listVideo;
	
	public Product(){
		id = 0 ;
		name ="";
		image ="";
		shortDes ="";
		des ="";
		listOption = new ArrayList<OptionObject>();
		listDocument = new ArrayList<DocumentObject>();
		listSpecs = new ArrayList<SpecsObject>();
		listVideo = new ArrayList<VideoObject>();
		
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getFaq() {
		return faq;
	}
	public void setFaq(String faq) {
		this.faq = faq;
	}
	public ArrayList<SpecsObject> getListSpecs() {
		return listSpecs;
	}
	public void setListSpecs(ArrayList<SpecsObject> listSpecs) {
		this.listSpecs = listSpecs;
	}
	public ArrayList<VideoObject> getListVideo() {
		return listVideo;
	}
	public void setListVideo(ArrayList<VideoObject> listVideo) {
		this.listVideo = listVideo;
	}
	
	
}
