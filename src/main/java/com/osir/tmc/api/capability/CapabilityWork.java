package com.osir.tmc.api.capability;

import java.util.LinkedList;
import java.util.Queue;

import com.osir.tmc.Main;
import com.osir.tmc.api.recipe.AnvilWorkType;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class CapabilityWork implements IWorkable, ICapabilitySerializable<NBTTagCompound> {
	public static final ResourceLocation KEY = new ResourceLocation(Main.MODID, "workable_item");

	protected Queue<AnvilWorkType> lastStep;
	protected int workProgress;

	public CapabilityWork() {
		this.lastStep = new LinkedList<AnvilWorkType>();
		for (int i = 0; i < 3; i++) {
			this.lastStep.offer(AnvilWorkType.NONE);
		}
	}

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
	public Queue<AnvilWorkType> getLastSteps() {
		return this.lastStep;
	}

	@Override
	public void putStep(AnvilWorkType type) {
		while (this.lastStep.size() >= 3) {
			this.lastStep.poll();
		}
		if (type == null) {
			this.lastStep.offer(AnvilWorkType.NONE);
		} else {
			this.lastStep.offer(type);
		}
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
		int[] steps = new int[3];
		AnvilWorkType[] copy = this.lastStep.toArray(new AnvilWorkType[0]);
		for (int i = 0; i < 3; i++) {
			steps[i] = copy[i].ordinal();
		}
		nbt.setIntArray("lastStep", steps);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		this.workProgress = nbt.getInteger("progress");
		int[] steps = nbt.getIntArray("lastStep");
		for (int i = 0; i < 3; i++) {
			this.putStep(AnvilWorkType.values()[steps[i]]);
		}
	}
}