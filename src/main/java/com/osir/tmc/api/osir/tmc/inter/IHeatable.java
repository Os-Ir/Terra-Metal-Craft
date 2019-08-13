package com.osir.tmc.api.osir.tmc.inter;

public interface IHeatable {
	int getMeltTemp();

	int getSpecificHeat();

	int getTemp();

	String getColor();

	int getUnit();

	int getCompleteUnit();

	void setUnit(int unit);

	boolean hasEnergy();

	float getEnergy();

	float getOverEnergy();

	float getMaxEnergy();

	void setEnergy(float energy);

	void setIncreaseEnergy(float energy);
}