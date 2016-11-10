package com.purchase.dao;

import java.util.Date;
import java.util.HashMap;

import com.purchase.dataObjects.Purchase;
import com.purchase.dataObjects.PurchaseContainer;

public interface PurchaseDAO {

	public int storePurchase(PurchaseContainer pc);
	public int updatePurchase(PurchaseContainer pc);
	public PurchaseContainer consultAllPurchases();
	public boolean purchaseExists(Long purchaseId);
	public Purchase consultPurchaseDetails(Long purchaseId);
	public HashMap<Long, Date> consultAllPurchasesByDate();

}