package api.osir.tmc.inter;

import api.osir.tmc.heat.HeatMaterial;
import net.minecraft.item.ItemStack;

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

	int setEnergy(float energy);

	void setIncreaseEnergy(float energy);
}