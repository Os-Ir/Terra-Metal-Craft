package com.osir.tmc.api.capability;

import com.osir.tmc.api.heat.HeatMaterial;
import com.osir.tmc.api.heat.TempList;
import com.osir.tmc.api.util.MathTool;

import net.minecraft.client.resources.I18n;

public interface IHeatable {
	HeatMaterial getMaterial();

	int getMaxTemp();

	int getTemp();

	int getUnit();

	float getProgress();

	boolean hasEnergy();

	float getEnergy();

	float getOverEnergy();

	float getMaxEnergy();

	void setEnergy(float energy);

	void increaseEnergy(float energy);

	boolean isWorkable();

	boolean isSoft();

	boolean isWeldable();

	boolean isDanger();

	default String getColor() {
		if (!this.hasEnergy()) {
			return null;
		}
		String str = "";
		float temp = this.getTemp();
		TempList[] list = TempList.values();
		if (temp < list[list.length - 1].getTemp()) {
			for (int i = 0; i < list.length - 1; i++) {
				if (temp >= list[i + 1].getTemp()) {
					continue;
				}
				str = list[i].getColor() + I18n.format("item.heatable.color." + list[i].getId()) + " "
						+ MathTool.romanNumber(
								(int) ((temp - list[i].getTemp()) / (list[i + 1].getTemp() - list[i].getTemp()) * 5));
				break;
			}
		} else {
			str = list[list.length - 1].getColor()
					+ I18n.format("item.heatable.color." + list[list.length - 1].getId());
		}
		return str;
	}
}