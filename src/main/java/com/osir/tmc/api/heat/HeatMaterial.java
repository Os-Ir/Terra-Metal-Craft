package com.osir.tmc.api.heat;

import java.util.List;

public class HeatMaterial {
	private List<String> shape;
	private float specificHeat;
	private int meltTemp;

	public HeatMaterial(List<String> shape, float specificHeat, int meltTemp) {
		this.shape = shape;
		this.specificHeat = specificHeat;
		this.meltTemp = meltTemp;
	}

	public float getSpecificHeat() {
		return this.specificHeat;
	}

	public int getMeltTemp() {
		return this.meltTemp;
	}
}