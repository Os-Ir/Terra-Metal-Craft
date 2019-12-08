package com.osir.tmc.capability;

import com.osir.tmc.api.capability.ILiquidContainer;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class StorageLiquidContainer implements IStorage<ILiquidContainer> {
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