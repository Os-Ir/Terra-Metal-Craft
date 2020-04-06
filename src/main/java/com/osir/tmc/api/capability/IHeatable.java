package com.osir.tmc.api.capability;

import com.osir.tmc.api.heat.HeatMaterial;
import com.osir.tmc.api.heat.TempList;
import com.osir.tmc.api.util.MathTool;

import net.minecraft.client.resources.I18n;

public interface IHeatable {
	HeatMaterial getMaterial();

	int getMeltTemp();

	int getTemp();

	void setMeltTemp(int temp);

	int getUnit();

	void setUnit(int unit, boolean lockTemp);

	void increaseUnit(int unit, boolean lockTemp);

	float getMeltProgress();

	boolean hasEnergy();

	float getEnergy();

	void setEnergy(float energy);

	void increaseEnergy(float energy);

	boolean isMelt();

	default boolean isWorkable() {
		return ((float) (this.getTemp() - 20)) / (this.getMeltTemp() - 20) >= 0.6;
	}

	default boolean isSoft() {
		return ((float) (this.getTemp() - 20)) / (this.getMeltTemp() - 20) >= 0.7;
	}

	default boolean isWeldable() {
		return ((float) (this.getTemp() - 20)) / (this.getMeltTemp() - 20) >= 0.8;
	}

	default boolean isDanger() {
		return ((float) (this.getTemp() - 20)) / (this.getMeltTemp() - 20) >= 0.9;
	}

	default int getKelvinTemp() {
		return this.getTemp() + 273;
	}

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
								(int) ((temp - list[i].getTemp()) / (list[i + 1].getTemp() - list[i].getTemp()) * 5)
										+ 1);
				break;
			}
		} else {
			str = list[list.length - 1].getColor()
					+ I18n.format("item.heatable.color." + list[list.length - 1].getId());
		}
		return str;
	}
}