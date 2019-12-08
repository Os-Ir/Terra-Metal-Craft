package com.osir.tmc.api.heat;

import gregtech.api.unification.material.type.Material;

public class HeatMaterial {
	protected Material material;
	protected float specificHeat;
	protected int meltTemp;

	public HeatMaterial(Material material, float specificHeat, int meltTemp) {
		this.material = material;
		this.specificHeat = specificHeat;
		this.meltTemp = meltTemp;
	}

	public Material getMaterial() {
		return this.material;
	}

	public float getSpecificHeat() {
		return this.specificHeat;
	}

	public int getMeltTemp() {
		return this.meltTemp;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof HeatMaterial)) {
			return false;
		}
		HeatMaterial mat = (HeatMaterial) obj;
		return this.material == mat.material && mat.meltTemp == this.meltTemp && mat.specificHeat == this.specificHeat;
	}
}