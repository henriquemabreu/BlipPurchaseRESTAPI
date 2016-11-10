package com.purchase.dataObjects;

import java.util.ArrayList;
import java.util.List;

public class PurchaseContainer {
	private List<Purchase> purchases = new ArrayList<Purchase>();
	
	public List<Purchase> getPurchasesList() {
		return purchases;
	}
	
	public void addPurchase(Purchase purchase) {
		purchases.add(purchase);
	}
}