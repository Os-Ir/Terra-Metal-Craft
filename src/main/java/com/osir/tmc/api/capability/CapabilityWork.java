package com.osir.tmc.api.capability;

import com.osir.tmc.Main;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class CapabilityWork implements IWorkable, ICapabilitySerializable<NBTTagCompound> {
	public static final ResourceLocation KEY = new ResourceLocation(Main.MODID, "workable_item");

	protected int workProgress;

	@Override
	public int getWorkProgress() {
		return this.workProgress;
	}

	@Override
	public void setWorkProgress(int progress) {
		this.workProgress = Math.min(Math.max(progress, 0), 150);
	}

	@Override
	public void addWorkProgress(int add) {
		this.workProgress += add;
		this.workProgress = Math.min(Math.max(this.workProgress, 0), 150);
	}

	@Override
	public boolean isWorked() {
		return this.workProgress > 0;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityList.WORKABLE;
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
		nbt.setInteger("progress", this.workProgress);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		this.workProgress = nbt.getInteger("progress");
	}
}