package com.osir.tmc.api.capability;

import java.util.List;

import com.osir.tmc.api.heat.HeatMaterial;

public interface ILiquidContainer {
	List<IHeatable> getMaterial();

	boolean hasMaterial(HeatMaterial material);

	int addMaterial(IHeatable material);

	int getCapacity();

	void setCapacity(int capacity);

	int getUsedCapacity();
}