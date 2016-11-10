package com.purchase.control;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.purchase.dao.PurchaseDAOImpl;
import com.purchase.dataObjects.PurchaseContainer;
import com.purchase.profilling.Control;
import com.purchase.profilling.Detailed;

public class PurchaseManager {
	
	int metricType = 3;

	public PurchaseContainer validPurchases() {
		
		Detailed metricDetail = new Detailed();

		PurchaseDAOImpl purchaseDAO = new PurchaseDAOImpl();
		PurchaseContainer validPurchases = new PurchaseContainer();
		Date agora = new Date();
		HashMap<Long, Date> pourChasesByDate;
		ArrayList<Long> idsPurchasesValid = new ArrayList<Long>();

		// Collect purchases ids and dates
		pourChasesByDate = purchaseDAO.consultAllPurchasesByDate();

		// Filter the purchases valid
		Iterator<Entry<Long, Date>> it = pourChasesByDate.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			System.out.println("PurchaseManager.validPurchases ==> Evaluating purchase with id = "+pair.getKey() + " and date = " + pair.getValue());
			if (((Date) pair.getValue()).getTime() >= agora.getTime()) {
				idsPurchasesValid.add((Long) pair.getKey());
				System.out.println("PurchaseManager.validPurchases ==> Added valid purchase: " + pair.getKey() + " = " + pair.getValue());

				// Collect the details of the valid purchases
				validPurchases.addPurchase(purchaseDAO.consultPurchaseDetails((Long) pair.getKey()));
			}
		}
		metricDetail.erase(metricType);
		System.out.println("Control "+Control.instantMetrics().getMetrics());
		return validPurchases;
	}

	public int storeOrUpdatePurchase(PurchaseContainer pc) {
		
		Detailed metricDetail = new Detailed();
		
		Long purchaseId = pc.getPurchasesList().get(0).getId();
		if (purchaseExists(purchaseId)) {
			metricDetail.erase(metricType);
			System.out.println("Control "+Control.instantMetrics().getMetrics());
			return updatePurchase(pc);
		} else {
			metricDetail.erase(metricType);
			System.out.println("Control "+Control.instantMetrics().getMetrics());
			return storePurchase(pc);
		}
	}

	public int storePurchase(PurchaseContainer pc) {
		Detailed metricDetail = new Detailed();
		PurchaseDAOImpl purchaseDAO = new PurchaseDAOImpl();
		int result = purchaseDAO.storePurchase(pc);
		metricDetail.erase(metricType);
		System.out.println("Control "+Control.instantMetrics().getMetrics());
		return result;
	}

	public int updatePurchase(PurchaseContainer pc) {
		Detailed metricDetail = new Detailed();
		PurchaseDAOImpl purchaseDAO = new PurchaseDAOImpl();
		int result = purchaseDAO.updatePurchase(pc);
		metricDetail.erase(metricType);
		System.out.println("Control "+Control.instantMetrics().getMetrics());
		return result;
	}

	public boolean purchaseExists(Long purchaseId) {
		Detailed metricDetail = new Detailed();
		PurchaseDAOImpl purchaseDAO = new PurchaseDAOImpl();
		boolean result = purchaseDAO.purchaseExists(purchaseId);
		metricDetail.erase(metricType);
		System.out.println("Control "+Control.instantMetrics().getMetrics());
		return result;
	}

}
