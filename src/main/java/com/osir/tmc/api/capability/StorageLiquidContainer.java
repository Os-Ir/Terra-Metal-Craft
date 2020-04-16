package com.osir.tmc.api.capability;

import java.util.Iterator;
import java.util.List;

import com.osir.tmc.api.heat.HeatMaterial;
import com.osir.tmc.api.heat.HeatMaterialList;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class StorageLiquidContainer implements IStorage<ILiquidContainer> {
	@Override
	public NBTBase writeNBT(Capability<ILiquidContainer> capability, ILiquidContainer instance, EnumFacing side) {
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagList list = new NBTTagList();
		IStorage<IHeatable> storage = CapabilityList.HEATABLE.getStorage();
		for (IHeatable heat : instance.getMaterial()) {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setInteger("material", HeatMaterialList.REGISTRY_HEATABLE_MATERIAL.getIDForObject(heat.getMaterial()));
			tag.setInteger("unit", heat.getUnit());
			tag.setTag("capability", storage.writeNBT(CapabilityList.HEATABLE, heat, null));
			list.appendTag(tag);
		}
		nbt.setTag("material", list);
		return nbt;
	}

	@Override
	public void readNBT(Capability<ILiquidContainer> capability, ILiquidContainer instance, EnumFacing side,
			NBTBase base) {
		NBTTagCompound nbt = (NBTTagCompound) base;
		NBTTagList list = nbt.getTagList("material", 10);
		Iterator<NBTBase> ite = list.iterator();
		IStorage<IHeatable> storage = CapabilityList.HEATABLE.getStorage();
		List<IHeatable> materialList = instance.getMaterial();
		materialList.clear();
		while (ite.hasNext()) {
			NBTTagCompound tag = (NBTTagCompound) ite.next();
			HeatMaterial material = HeatMaterialList.REGISTRY_HEATABLE_MATERIAL
					.getObjectById(tag.getInteger("material"));
			IHeatable heat = new CapabilityHeat(material, tag.getInteger("unit"));
			storage.readNBT(CapabilityList.HEATABLE, heat, null, tag.getTag("capability"));
			materialList.add(heat);
		}
	}
}