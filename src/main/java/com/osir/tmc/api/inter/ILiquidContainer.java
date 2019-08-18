package com.osir.tmc.api.inter;

public interface ILiquidContainer {
	int getCapacity();

	void setCapacity(int capacity);

	String getMetal();

	void setMetal(String metal);

	int getUnit();

	void setUnit(int unit);
}