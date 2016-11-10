package com.purchase.dataObjects;

import java.util.Date;
import java.util.List;

public class Purchase {
	private Long id;
	private String productType;
	private Date expires;
	private List<Detail> details;
	
	public Purchase(Long id, String productType, Date expired, List<Detail> details) {
		this.id = id;
		this.productType = productType;
		this.expires = expired;
		this.details = details;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public Date getExpired() {
		return expires;
	}

	public void setExpired(Date expired) {
		this.expires = expired;
	}

	public List<Detail> getDetails() {
		return details;
	}

	public void setDetails(List<Detail> details) {
		this.details = details;
	}
	
	public void updatePurchase(Purchase purchase) {
		setExpired(purchase.getExpired());
		setProductType(purchase.getProductType());
		setDetails(purchase.getDetails());;
	}
	
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		String detailsString;
		for (Detail detail : details) {
			stringBuilder.append(detail.toString()+" ");
		}
		detailsString = stringBuilder.toString();
		return "[ id = "+id+" productType = "+productType+" expired = "+expires+" purchaseDetails = "+detailsString+" ]";
	}
}
