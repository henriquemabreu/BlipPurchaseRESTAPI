package com.purchase.profilling;

public class Detailed {

	private Long startTime;
	private Long duration;

	public Detailed() {
		startTime = System.currentTimeMillis();
	}

	public void erase(int type) {
		duration = System.currentTimeMillis() - startTime;
		switch (type) {
			case 1:
				// Global Metrics
				Global.add(duration);
				break;
			case 2:
				// DAO Metrics
				DAO.add(duration);
				break;
			case 3:
				// Control Metrics
				Control.add(duration);
				break;
			default:
				break;
		}
	}
}
