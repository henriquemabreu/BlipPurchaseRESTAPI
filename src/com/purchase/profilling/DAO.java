package com.purchase.profilling;

import com.purchase.profilling.dataObjects.GlobalMetrics;

public class DAO {
	private static int processQuantity;
	private static Long totalTime;
	private static DAO instance;
	
	public static void init() {
		if (instance==null) {
			instance = new DAO();
			processQuantity = 0;
			totalTime = 0L;
		}
	}
	
	public static void add(Long duration) {
		processQuantity++;
		totalTime+=duration;
	}
	
	public static GlobalMetrics instantMetrics() {
		Long averageTime = totalTime/processQuantity;
		GlobalMetrics globalMetrics = new GlobalMetrics(processQuantity, averageTime, totalTime);
		return globalMetrics;
	}
}
