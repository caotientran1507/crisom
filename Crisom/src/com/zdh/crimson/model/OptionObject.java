package com.zdh.crimson.model;

public class OptionObject {
	String sku;
	String color;
	String otherFieldTitle;
	String otherFieldValue;
	double weight;
	String price;
	String msrp;
	int oid;
	int value;
	int inCart;
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getMsrp() {
		return msrp;
	}
	public void setMsrp(String msrp) {
		this.msrp = msrp;
	}
	public int getOid() {
		return oid;
	}
	public void setOid(int oid) {
		this.oid = oid;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public String getOtherFieldTitle() {
		return otherFieldTitle;
	}
	public void setOtherFieldTitle(String otherFieldTitle) {
		this.otherFieldTitle = otherFieldTitle;
	}
	public String getOtherFieldValue() {
		return otherFieldValue;
	}
	public void setOtherFieldValue(String otherFieldValue) {
		this.otherFieldValue = otherFieldValue;
	}
	public int getInCart() {
		return inCart;
	}
	public void setInCart(int inCart) {
		this.inCart = inCart;
	}
	
	
}
