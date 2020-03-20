package com.osir.tmc.api.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class StorageWork implements IStorage<IWorkable> {
	@Override
	public NBTBase writeNBT(Capability<IWorkable> capability, IWorkable instance, EnumFacing side) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("progress", instance.getWorkProgress());
		return nbt;
	}

	@Override
	public void readNBT(Capability<IWorkable> capability, IWorkable instance, EnumFacing side, NBTBase nbt) {
		NBTTagCompound tag = (NBTTagCompound) nbt;
		instance.setWorkProgress(tag.getInteger("progress"));
	}
}