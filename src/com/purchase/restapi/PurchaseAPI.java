package com.purchase.restapi;

/**
 * @author Henrique
 *
 */

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import com.purchase.control.PurchaseManager;
import com.purchase.dataObjects.Purchase;
import com.purchase.dataObjects.PurchaseContainer;
import com.purchase.profilling.Control;
import com.purchase.profilling.DAO;
import com.purchase.profilling.Detailed;
import com.purchase.profilling.Global;
import com.purchase.profilling.dataObjects.GlobalMetrics;

@Path("/restapi")
public class PurchaseAPI {

	int metricType = 1;

	@Path("/consult")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultPurchasesJson() {

		Detailed metricDetail = new Detailed();

		PurchaseManager purchaseManager = new PurchaseManager();

		// Collect Valid Purchases (and details)
		PurchaseContainer validPurchases = purchaseManager.validPurchases();
		System.out.println("PurchaseAPI.consultPurchasesJson ==> Size of validPurchases: "
				+ validPurchases.getPurchasesList().size());

		Iterator<Purchase> itr = validPurchases.getPurchasesList().iterator();
		while (itr.hasNext()) {
			Purchase purchase = itr.next();
			System.out.println("PurchaseAPI.consultPurchasesJson ==> single valid purchase: " + purchase.toString());
		}

		// Transform purchases into json format
		String jsonStringResponse;
		Gson g = new Gson();
		jsonStringResponse = g.toJson(validPurchases, PurchaseContainer.class);
		System.out.println("PurchaseAPI.consultPurchasesJson ==> responsee: " + jsonStringResponse);

		metricDetail.erase(metricType);
		System.out.println("Global " + Global.instantMetrics().getMetrics());

		// Build response
		return Response.status(200).entity(jsonStringResponse).build();
	}

	@Path("/save")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response savePurchases(InputStream input) {

		Detailed metricDetail = new Detailed();

		PurchaseContainer pc = new PurchaseContainer();

		StringBuilder stringBuilder = new StringBuilder();
		String stringPurchase = "";
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(input));
			String line = null;
			while ((line = in.readLine()) != null) {
				stringBuilder.append(line);
			}
			stringPurchase = stringBuilder.toString();
		} catch (Exception e) {
			System.out.println("PurchaseAPI.savePurchases ==> Error Parsing: - " + e);
		}

		Gson g = new Gson();
		pc = g.fromJson(stringPurchase, PurchaseContainer.class);

		// Save the purchase
		PurchaseManager purchaseManager = new PurchaseManager();
		if (purchaseManager.storeOrUpdatePurchase(pc) == 1) {
			metricDetail.erase(metricType);
			return Response.status(200).entity("Purchase saved with success!").build();
		} else {
			metricDetail.erase(metricType);
			return Response.status(201).entity("Error saving purchase!").build();
		}
	}
	

	// Metrics

	@Path("/metrics/global")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultGlobalMetrics() {
		String jsonStringResponse;
		Gson g = new Gson();
		jsonStringResponse = g.toJson(Global.instantMetrics(), GlobalMetrics.class);
		return Response.status(200).entity(jsonStringResponse).build();
	}

	@Path("/metrics/dao")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultDAOMetrics() {
		String jsonStringResponse;
		Gson g = new Gson();
		jsonStringResponse = g.toJson(DAO.instantMetrics(), GlobalMetrics.class);
		return Response.status(200).entity(jsonStringResponse).build();
	}
	
	@Path("/metrics/control")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultControlMetrics() {
		String jsonStringResponse;
		Gson g = new Gson();
		jsonStringResponse = g.toJson(Control.instantMetrics(), GlobalMetrics.class);
		return Response.status(200).entity(jsonStringResponse).build();
	}
}
