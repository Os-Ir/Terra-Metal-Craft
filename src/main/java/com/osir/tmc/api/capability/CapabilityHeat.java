package com.osir.tmc.api.capability;

import com.osir.tmc.Main;
import com.osir.tmc.api.heat.HeatMaterial;
import com.osir.tmc.api.heat.HeatMaterialList;
import com.osir.tmc.api.heat.TempList;
import com.osir.tmc.api.util.MathTool;

import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class CapabilityHeat implements IHeatable, ICapabilitySerializable<NBTTagCompound> {
	public static final ResourceLocation KEY = new ResourceLocation(Main.MODID, "heatable_item");

	private float specificHeat;
	private int meltTemp, unit, compUnit;
	private float maxEnergy, energy, overEnergy;

	public CapabilityHeat() {
		this(HeatMaterialList.EMPTY, 144);
	}

	public CapabilityHeat(HeatMaterial material, int unit) {
		this.compUnit = this.unit = unit;
		this.specificHeat = material.getSpecificHeat();
		this.meltTemp = material.getMeltTemp();
		this.maxEnergy = this.specificHeat * (this.meltTemp - 20) * unit;
	}

	@Override
	public int getMeltTemp() {
		return this.meltTemp;
	}

	@Override
	public float getSpecificHeat() {
		return this.specificHeat;
	}

	@Override
	public int getTemp() {
		return (int) ((this.energy / this.maxEnergy) * (this.meltTemp - 20) + 20);
	}

	@Override
	public String getColor() {
		if (!this.hasEnergy()) {
			return null;
		}
		String str = "";
		float temp = this.getTemp();
		TempList[] list = TempList.values();
		if (temp < list[list.length - 1].getTemp()) {
			int i;
			for (i = 0; i < list.length - 1; i++) {
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

	@Override
	public int getUnit() {
		return this.unit;
	}

	@Override
	public int getCompleteUnit() {
		return this.compUnit;
	}

	@Override
	public void setUnit(int unit) {
		this.unit = unit;
	}

	@Override
	public boolean hasEnergy() {
		return this.energy != 0;
	}

	@Override
	public float getEnergy() {
		return this.energy;
	}

	@Override
	public float getOverEnergy() {
		return this.overEnergy;
	}

	@Override
	public float getMaxEnergy() {
		return this.maxEnergy;
	}

	@Override
	public void setEnergy(float energy) {
		this.energy = Math.max(Math.min(energy, this.maxEnergy), 0);
	}

	@Override
	public void setIncreaseEnergy(float energy) {
		if (energy + this.energy > this.maxEnergy) {
			this.overEnergy += energy + this.energy - this.maxEnergy;
			this.energy = this.maxEnergy;
			this.unit = Math.max((int) ((1 - this.overEnergy / this.maxEnergy * 10) * this.compUnit), 0);
		} else {
			this.energy += energy;
		}
		this.energy = Math.max(this.energy, 0);
	}

	@Override
	public boolean hasCapability(Capability capability, EnumFacing facing) {
		return capability == CapabilityList.HEATABLE;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (this.hasCapability(capability, facing)) {
			return (T) this;
		}
		return null;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		if (this.unit != this.compUnit) {
			nbt.setInteger("unit", this.unit);
		}
		if (this.energy != 0) {
			nbt.setFloat("energy", this.energy);
		}
		if (this.overEnergy != 0) {
			nbt.setFloat("overEnergy", this.overEnergy);
		}
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		if (nbt == null) {
			return;
		}
		if (nbt.hasKey("unit")) {
			this.unit = nbt.getInteger("unit");
		}
		if (nbt.hasKey("energy")) {
			this.energy = nbt.getFloat("energy");
		}
		if (nbt.hasKey("overEnergy")) {
			this.overEnergy = nbt.getFloat("overEnergy");
		}
	}

	@Override
	public boolean isWorkable() {
		return ((float) (this.getTemp() - 20)) / (this.meltTemp - 20) >= 0.6;
	}

	@Override
	public boolean isSoft() {
		return ((float) (this.getTemp() - 20)) / (this.meltTemp - 20) >= 0.7;
	}

	@Override
	public boolean isWeldable() {
		return ((float) (this.getTemp() - 20)) / (this.meltTemp - 20) >= 0.8;
	}

	@Override
	public boolean isDanger() {
		return ((float) (this.getTemp() - 20)) / (this.meltTemp - 20) >= 0.9;
	}
}