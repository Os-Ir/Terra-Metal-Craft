package com.osir.tmc.te;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

public abstract class TEHeatBlock extends TEInventory implements ITickable {
	protected int temp = 20, burnTime;
	protected float energy;

	public int getTemp() {
		return this.temp;
	}

	public int getBurnTime() {
		return this.burnTime;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.energy = nbt.getFloat("energy");
		this.burnTime = nbt.getInteger("burnTime");
		super.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setFloat("energy", this.energy);
		nbt.setFloat("burnTime", this.burnTime);
		return super.writeToNBT(nbt);
	}
}