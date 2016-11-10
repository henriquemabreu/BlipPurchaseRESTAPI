package com.purchase.profilling.dataObjects;

import java.util.Date;

public class GlobalMetrics {
	private int quantity;
	private Long averageTime;
	private Long totalTime;
	private Long timestamp;

	public GlobalMetrics(int quantity, Long averageTime, Long totalTime) {
		this.quantity = quantity;
		this.averageTime = averageTime;
		this.timestamp = System.currentTimeMillis();
		this.totalTime = totalTime;
	}

	public int getQuantity() {
		return quantity;
	}

	public Long getAverageTime() {
		return averageTime;
	}

	public Long getTotalTime() {
		return totalTime;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public String getMetrics() {
		return "metrics[ " + new Date(getTimestamp()).toString() + " ]: " + getQuantity()
				+ " processes with the average time of " + getAverageTime() + " milliseconds and the total time of "
				+ getTotalTime() + " milliseconds.";
	}
}
