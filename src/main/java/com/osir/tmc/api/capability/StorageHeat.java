package com.osir.tmc.api.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class StorageHeat implements IStorage<IHeatable> {
	@Override
	public NBTBase writeNBT(Capability<IHeatable> capability, IHeatable instance, EnumFacing side) {
		NBTTagCompound nbt = new NBTTagCompound();
		boolean flag = false;
		if (instance.getEnergy() != 0) {
			nbt.setFloat("energy", instance.getEnergy());
			flag = true;
		}
		if (instance.getOverEnergy() != 0) {
			nbt.setFloat("overEnergy", instance.getOverEnergy());
			flag = true;
		}
		if (flag) {
			return nbt;
		}
		return null;
	}

	@Override
	public void readNBT(Capability<IHeatable> capability, IHeatable instance, EnumFacing side, NBTBase base) {
		NBTTagCompound nbt = (NBTTagCompound) base;
		if (nbt == null) {
			return;
		}
		float energy = 0;
		if (nbt.hasKey("energy")) {
			energy += nbt.getFloat("energy");
		}
		if (nbt.hasKey("overEnergy")) {
			energy += nbt.getFloat("overEnergy");
		}
		instance.increaseEnergy(energy);
	}
}