package com.osir.tmc.api.util;

import com.osir.tmc.api.capability.IHeatable;

public class CapabilityUtil {
	public static void heatExchange(IHeatable cap, int temp, float resistance) {
		if (cap == null) {
			return;
		}
		float exchange = (cap.getTemp() - temp) / resistance;
		cap.increaseEnergy(-exchange);
	}

	public static void heatExchange(IHeatable capA, IHeatable capB, float resistance) {
		if (capA == null || capB == null) {
			return;
		}
		float exchange = (capA.getTemp() - capB.getTemp()) / resistance;
		capA.increaseEnergy(-exchange);
		capB.increaseEnergy(exchange);
	}
}