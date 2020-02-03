package com.osir.tmc.api.capability;

import com.osir.tmc.Main;
import com.osir.tmc.api.heat.HeatMaterial;
import com.osir.tmc.api.heat.HeatMaterialList;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class CapabilityHeat implements IHeatable, ICapabilitySerializable<NBTTagCompound> {
	public static final ResourceLocation KEY = new ResourceLocation(Main.MODID, "heatable_item");

	protected HeatMaterial material;
	protected int maxTemp, unit;
	protected float maxEnergy, energy, overEnergy;

	public CapabilityHeat() {
		this(HeatMaterialList.EMPTY, 1, 20);
	}

	public CapabilityHeat(HeatMaterial material, int unit) {
		this(material, unit, material.getMeltTemp());
	}

	public CapabilityHeat(HeatMaterial material, int unit, int maxTemp) {
		this.material = material;
		this.unit = unit;
		this.maxTemp = maxTemp;
		this.maxEnergy = material.getSpecificHeat() * (this.maxTemp - 20) * unit;
	}

	@Override
	public HeatMaterial getMaterial() {
		return this.material;
	}

	@Override
	public int getMaxTemp() {
		return this.maxTemp;
	}

	@Override
	public int getTemp() {
		return (int) ((this.energy / this.maxEnergy) * (this.maxTemp - 20) + 20);
	}

	@Override
	public int getUnit() {
		return this.unit;
	}

	@Override
	public float getProgress() {
		return Math.max(Math.min(this.overEnergy / this.maxEnergy * 10, 1), 0);
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
	public void increaseEnergy(float energy) {
		if (energy + this.energy > this.maxEnergy) {
			this.overEnergy += energy + this.energy - this.maxEnergy;
			this.energy = this.maxEnergy;
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
		if (nbt.hasKey("energy")) {
			this.energy = nbt.getFloat("energy");
		}
		if (nbt.hasKey("overEnergy")) {
			this.overEnergy = nbt.getFloat("overEnergy");
		}
	}

	@Override
	public boolean isWorkable() {
		return ((float) (this.getTemp() - 20)) / (this.maxTemp - 20) >= 0.6;
	}

	@Override
	public boolean isSoft() {
		return ((float) (this.getTemp() - 20)) / (this.maxTemp - 20) >= 0.7;
	}

	@Override
	public boolean isWeldable() {
		return ((float) (this.getTemp() - 20)) / (this.maxTemp - 20) >= 0.8;
	}

	@Override
	public boolean isDanger() {
		return ((float) (this.getTemp() - 20)) / (this.maxTemp - 20) >= 0.9;
	}
}