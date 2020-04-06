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
		nbt.setFloat("energy", instance.getEnergy());
		return nbt;
	}

	@Override
	public void readNBT(Capability<IHeatable> capability, IHeatable instance, EnumFacing side, NBTBase base) {
		NBTTagCompound nbt = (NBTTagCompound) base;
		instance.setEnergy(nbt.getFloat("energy"));
	}
}