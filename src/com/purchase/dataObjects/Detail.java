package com.purchase.dataObjects;

public class Detail {
	private long id;
	private String description;
	private Integer quantity;
	private Double value;
	
	public Detail(long id, String description, Integer quantity, Double value) {
		this.id = id;
		this.description = description;
		this.quantity = quantity;
		this.value = value;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public String toString() {
		return "[ id = "+id+" description = "+description+" quantity = "+quantity+" value = "+value+" ]";
	}
}
