package com.osir.tmc.api.capability;

import com.osir.tmc.Main;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class CapabilityLiquidContainer implements ILiquidContainer, ICapabilitySerializable<NBTTagCompound> {
	public static final ResourceLocation KEY = new ResourceLocation(Main.MODID, "liquid_container");

	public float rate;
	private String metal;
	private int unit, capacity, specificHeat;

	public CapabilityLiquidContainer() {
		this(1, 144, 100);
	}

	public CapabilityLiquidContainer(float rate, int capacity, int specificHeat) {
		this.rate = rate;
		this.capacity = capacity;
		this.specificHeat = specificHeat;
	}

	@Override
	public void setRate(float rate) {
		this.rate = rate;
	}

	@Override
	public float getRate() {
		return this.rate;
	}

	@Override
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	@Override
	public int getCapacity() {
		return this.capacity;
	}

	@Override
	public String getMetal() {
		return this.metal;
	}

	@Override
	public void setMetal(String metal) {
		this.metal = metal;
	}

	@Override
	public int getUnit() {
		return this.unit;
	}

	@Override
	public void setUnit(int unit) {
		this.unit = unit;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityList.LIQUID_CONTAINER;
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
		if (this.metal != null && !this.metal.isEmpty()) {
			nbt.setString("metal", this.metal);
		}
		if (this.unit != 0) {
			nbt.setInteger("unit", this.unit);
		}
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("metal")) {
			this.metal = nbt.getString("metal");
		}
		if (nbt.hasKey("unit")) {
			this.unit = nbt.getInteger("unit");
		}
	}
}