package com.osir.tmc.api.heat;

public class HeatMaterial {
	private int specificHeat, meltTemp;

	public HeatMaterial(int specificHeat, int meltTemp) {
		this.specificHeat = specificHeat;
		this.meltTemp = meltTemp;
	}

	public int getSpecificHeat() {
		return this.specificHeat;
	}

	public int getMeltTemp() {
		return this.meltTemp;
	}
}