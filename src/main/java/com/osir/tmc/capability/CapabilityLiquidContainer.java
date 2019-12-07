package com.osir.tmc.capability;

import com.osir.tmc.Main;
import com.osir.tmc.api.capability.ILiquidContainer;
import com.osir.tmc.handler.CapabilityHandler;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class CapabilityLiquidContainer {
	public static final ResourceLocation KEY = new ResourceLocation(Main.MODID, "liquid_container");

	public static class Storage implements IStorage<ILiquidContainer> {
		@Override
		public NBTBase writeNBT(Capability<ILiquidContainer> capability, ILiquidContainer instance, EnumFacing side) {
			NBTTagCompound nbt = new NBTTagCompound();
			if (instance.getMetal() != null && instance.getMetal() != "") {
				nbt.setString("metal", instance.getMetal());
			}
			if (instance.getUnit() != 0) {
				nbt.setInteger("unit", instance.getUnit());
			}
			return nbt;
		}

		@Override
		public void readNBT(Capability<ILiquidContainer> capability, ILiquidContainer instance, EnumFacing side,
				NBTBase base) {
			NBTTagCompound nbt = (NBTTagCompound) base;
			if (nbt.hasKey("metal")) {
				instance.setMetal(nbt.getString("metal"));
			}
			if (nbt.hasKey("unit")) {
				instance.setUnit(nbt.getInteger("unit"));
			}
		}
	}

	public static class Implementation implements ILiquidContainer, ICapabilitySerializable<NBTTagCompound> {
		public float rate;
		private String metal;
		private int unit, capacity, specificHeat;

		public Implementation() {
			this(1, 144, 100);
		}

		public Implementation(float rate, int capacity, int specificHeat) {
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
			return capability == CapabilityHandler.LIQUID_CONTAINER;
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
}