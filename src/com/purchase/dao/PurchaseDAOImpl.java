package com.purchase.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.google.gson.Gson;
import com.purchase.dataObjects.Purchase;
import com.purchase.dataObjects.PurchaseContainer;
import com.purchase.profilling.Detailed;
import com.purchase.profilling.DAO;

public class PurchaseDAOImpl implements PurchaseDAO {
	
	int metricType = 2;

	@Override
	public int storePurchase(PurchaseContainer pc) {
		return storePurchaseInLocalFile(pc);
	}

	public int storePurchaseInLocalFile(PurchaseContainer pc) {
		
		Detailed metricDetail = new Detailed();

		// Read file with all purchases
		String stringAllPurchasesJson = "";
		InputStream jsonInputStream;
		PurchaseContainer storedPurchases = new PurchaseContainer();
		BufferedReader br;
		Purchase purchase;
		try {
			Gson g = new Gson();
			File originalFile = new File(this.getClass().getClassLoader().getResource("").getPath()
					+ "com\\purchase\\dao\\all_purchases.json");
			jsonInputStream = new FileInputStream(originalFile);
			InputStreamReader jsonReader = new InputStreamReader(jsonInputStream);
			br = new BufferedReader(jsonReader);
			String line;
			while ((line = br.readLine()) != null) {
				stringAllPurchasesJson += line + "\n";
			}
			storedPurchases = g.fromJson(stringAllPurchasesJson, PurchaseContainer.class);
			br.close();

			// Increase PurchaseContainer with the purchases received
			Iterator<Purchase> itr = pc.getPurchasesList().iterator();
			while (itr.hasNext()) {
				purchase = itr.next();
				storedPurchases.addPurchase(purchase);
				System.out.println("PurchaseDAOImpl.storePurchase ==> stored pourchase: " + purchase.toString());
			}

			// Convert final PurchaseContainer into json
			String jsonStringResponse;
			jsonStringResponse = g.toJson(storedPurchases, PurchaseContainer.class);
			System.out.println("PurchaseDAOImpl.storePurchase ==> all purchases after store: " + jsonStringResponse);

			// Replace file with the new string
			File newFile = new File(this.getClass().getClassLoader().getResource("").getPath()
					+ "com\\purchase\\dao\\temp_purchases.json");
			FileWriter writer = new FileWriter(newFile);
			writer.write(jsonStringResponse);
			writer.flush();
			writer.close();

			if (!originalFile.delete()) {
				System.out.println("PurchaseDAOImpl.storePurchase ==> Could not delete file!");
				return 0;
			}
			if (!newFile.renameTo(originalFile)) {
				System.out.println("PurchaseDAOImpl.storePurchase ==> Could not rename file!");
			}

			// For testing purposes only, store local file with purchase
			// received
			jsonStringResponse = g.toJson(pc, PurchaseContainer.class);
			System.out.println("PurchaseDAOImpl.storePurchase ==> purchase stored: " + jsonStringResponse);
			File newUnitFile = new File(this.getClass().getClassLoader().getResource("").getPath()
					+ "com\\purchase\\dao\\purchase_" + pc.getPurchasesList().get(0).getId() + ".json");
			FileWriter writerUnit = new FileWriter(newUnitFile);
			writerUnit.write(jsonStringResponse);
			writerUnit.flush();
			writerUnit.close();

			Thread.sleep(2000);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return 0;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return 0;
		}
		metricDetail.erase(metricType);
		System.out.println("DAO "+DAO.instantMetrics().getMetrics());
		return 1;
	}

	@Override
	public int updatePurchase(PurchaseContainer pc) {
		return updatePurchaseInLocalFile(pc);
	}

	public int updatePurchaseInLocalFile(PurchaseContainer pc) {
		
		Detailed metricDetail = new Detailed();

		// Read file with all purchases
		String stringAllPurchasesJson = "";
		InputStream jsonInputStream;
		PurchaseContainer storedPurchases = new PurchaseContainer();
		BufferedReader br;
		Purchase purchaseStored;
		Purchase purchaseReceived;
		try {
			Gson g = new Gson();
			File originalFile = new File(this.getClass().getClassLoader().getResource("").getPath()
					+ "com\\purchase\\dao\\all_purchases.json");
			jsonInputStream = new FileInputStream(originalFile);
			InputStreamReader jsonReader = new InputStreamReader(jsonInputStream);
			br = new BufferedReader(jsonReader);
			String line;
			while ((line = br.readLine()) != null) {
				stringAllPurchasesJson += line + "\n";
			}
			storedPurchases = g.fromJson(stringAllPurchasesJson, PurchaseContainer.class);
			br.close();

			// Update purchase
			Iterator<Purchase> itrStored = storedPurchases.getPurchasesList().iterator();
			while (itrStored.hasNext()) {
				purchaseStored = itrStored.next();
				Iterator<Purchase> itrReceived = pc.getPurchasesList().iterator();
				while (itrReceived.hasNext()) {
					purchaseReceived = itrReceived.next();
					if (purchaseStored.getId() == purchaseReceived.getId()) {
						purchaseStored.updatePurchase(purchaseReceived);
					}
				}
			}

			// Update global purchases file ----------------
			// Convert final PurchaseContainer into json
			String jsonStringResponse;
			jsonStringResponse = g.toJson(storedPurchases, PurchaseContainer.class);
			System.out.println("PurchaseDAOImpl.storePurchase ==> all purchases after store: " + jsonStringResponse);

			// Replace file with the new string
			File newFile = new File(this.getClass().getClassLoader().getResource("").getPath()
					+ "com\\purchase\\dao\\temp_purchases.json");
			FileWriter writer = new FileWriter(newFile);
			writer.write(jsonStringResponse);
			writer.flush();
			writer.close();
			if (!originalFile.delete()) {
				System.out.println("PurchaseDAOImpl.updatePurchase ==> Could not delete file [originalFile]!");
				return 0;
			} else {
				System.out.println("PurchaseDAOImpl.updatePurchase ==> File [originalFile] deleted!");
			}
			if (!newFile.renameTo(originalFile)) {
				System.out.println("PurchaseDAOImpl.updatePurchase ==> Could not rename file [newFile]!");
				return 0;
			} else {
				System.out.println("PurchaseDAOImpl.updatePurchase ==> File [newFile] renamed to [originalFile]!");
			}

			// Update global purchases file
			// Convert Single purchase into json
			String jsonString;
			jsonString = g.toJson(pc, PurchaseContainer.class);
			System.out.println("PurchaseDAOImpl.storePurchase ==> all purchases after store: " + jsonString);

			// Replace file with the new string
			File newFileUnit = new File(this.getClass().getClassLoader().getResource("").getPath()
					+ "com\\purchase\\dao\\temp_purchases.json");
			File originalFileUnit = new File(this.getClass().getClassLoader().getResource("").getPath()
					+ "com\\purchase\\dao\\purchase_" + pc.getPurchasesList().get(0).getId() + ".json");
			FileWriter writerUnit = new FileWriter(newFileUnit);
			writerUnit.write(jsonString);
			writerUnit.flush();
			writerUnit.close();
			if (!originalFileUnit.delete()) {
				System.out.println("PurchaseDAOImpl.updatePurchase ==> Could not delete file [originalFileUnit]!");
				return 0;
			} else {
				System.out.println("PurchaseDAOImpl.updatePurchase ==> File [originalFileUnit] deleted!");
			}
			if (!newFileUnit.renameTo(originalFileUnit)) {
				System.out.println("PurchaseDAOImpl.updatePurchase ==> Could not rename file [newFileUnit]!");
				return 0;
			} else {
				System.out.println(
						"PurchaseDAOImpl.updatePurchase ==> File [newFileUnit] renamed to [originalFileUnit]!");
			}

			Thread.sleep(2000);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return 0;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return 0;
		}
		metricDetail.erase(metricType);
		System.out.println("DAO "+DAO.instantMetrics().getMetrics());
		return 1;
	}

	@Override
	public HashMap<Long, Date> consultAllPurchasesByDate() {
		return consultAllPurchasesByDateInLocalFile();
	};

	public HashMap<Long, Date> consultAllPurchasesByDateInLocalFile() {
		
		Detailed metricDetail = new Detailed();
		
		String stringAllPurchasesJson = "";
		InputStream jsonInputStream;
		PurchaseContainer pc = new PurchaseContainer();
		BufferedReader br;
		HashMap<Long, Date> pourChasesByDate = new HashMap<Long, Date>();
		Purchase purchase;

		// Build some purchases from local json file, simulating another source
		// of data
		try {
			jsonInputStream = new FileInputStream(new File(this.getClass().getClassLoader().getResource("").getPath()
					+ "com\\purchase\\dao\\all_purchases.json"));
			InputStreamReader jsonReader = new InputStreamReader(jsonInputStream);
			br = new BufferedReader(jsonReader);
			String line;
			while ((line = br.readLine()) != null) {
				stringAllPurchasesJson += line + "\n";
			}

			Gson g = new Gson();
			pc = g.fromJson(stringAllPurchasesJson, PurchaseContainer.class);

			br.close();

			Thread.sleep(2000);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}

		Iterator<Purchase> itr = pc.getPurchasesList().iterator();
		while (itr.hasNext()) {
			purchase = itr.next();
			pourChasesByDate.put(purchase.getId(), purchase.getExpired());
			System.out.println("PurchaseDAOImpl.consultAllPurchasesByDate ==> single purchase: " + purchase.toString());
		}
		metricDetail.erase(metricType);
		System.out.println("DAO "+DAO.instantMetrics().getMetrics());
		return pourChasesByDate;
	};

	private PurchaseContainer consultAllPurchasesFromLocalFile() {
		
		Detailed metricDetail = new Detailed();

		String stringAllPurchasesJson = "";
		InputStream jsonInputStream;
		PurchaseContainer pc = new PurchaseContainer();
		BufferedReader br;

		// Build some purchases from local json file, simulating another source
		// of data
		try {
			jsonInputStream = new FileInputStream(new File(this.getClass().getClassLoader().getResource("").getPath()
					+ "com\\purchase\\dao\\all_purchases.json"));
			InputStreamReader jsonReader = new InputStreamReader(jsonInputStream);
			br = new BufferedReader(jsonReader);
			String line;
			while ((line = br.readLine()) != null) {
				stringAllPurchasesJson += line + "\n";
			}

			Gson g = new Gson();
			pc = g.fromJson(stringAllPurchasesJson, PurchaseContainer.class);

			br.close();

			Thread.sleep(2000);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
		metricDetail.erase(metricType);
		System.out.println("DAO "+DAO.instantMetrics().getMetrics());
		return pc;
	}

	@Override
	public PurchaseContainer consultAllPurchases() {
		return consultAllPurchasesFromLocalFile();
	}

	@Override
	public Purchase consultPurchaseDetails(Long purchaseId) {
		return consultPurchaseDetailsFromLocalFile(purchaseId);
	}

	public Purchase consultPurchaseDetailsFromLocalFile(Long purchaseId) {
		
		Detailed metricDetail = new Detailed();

		String stringAllPurchasesJson = "";
		InputStream jsonInputStream;
		Purchase purchase = null;
		BufferedReader br;

		// Build some purchases from local json file, simulating another source
		// of data
		try {
			jsonInputStream = new FileInputStream(new File(this.getClass().getClassLoader().getResource("").getPath()
					+ "com\\purchase\\dao\\purchase_" + purchaseId + ".json"));
			InputStreamReader jsonReader = new InputStreamReader(jsonInputStream);
			br = new BufferedReader(jsonReader);
			String line;
			while ((line = br.readLine()) != null) {
				stringAllPurchasesJson += line + "\n";
			}

			Gson g = new Gson();
			purchase = g.fromJson(stringAllPurchasesJson, PurchaseContainer.class).getPurchasesList().get(0);

			br.close();

			Thread.sleep(2000);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
		metricDetail.erase(metricType);
		System.out.println("DAO "+DAO.instantMetrics().getMetrics());
		return purchase;
	}

	public boolean purchaseExistsInLocalFile(Long purchaseId) {
		Detailed metricDetail = new Detailed();
		InputStream jsonInputStream;
		try {
			jsonInputStream = new FileInputStream(new File(this.getClass().getClassLoader().getResource("").getPath()
					+ "com\\purchase\\dao\\purchase_" + purchaseId + ".json"));
			jsonInputStream.close();
			Thread.sleep(2000);
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		metricDetail.erase(metricType);
		System.out.println("DAO "+DAO.instantMetrics().getMetrics());
		return true;
	}

	@Override
	public boolean purchaseExists(Long purchaseId) {
		return purchaseExistsInLocalFile(purchaseId);

	}

}
