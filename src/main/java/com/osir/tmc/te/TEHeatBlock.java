package com.osir.tmc.te;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

public abstract class TEHeatBlock extends TEInventory implements ITickable {
	protected int temp = 20;
	protected float energy;

	public int getTemp() {
		return this.temp;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.energy = nbt.getFloat("energy");
		super.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setFloat("energy", this.energy);
		return super.writeToNBT(nbt);
	}
}