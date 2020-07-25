package com.osir.tmc.api.capability;

import java.util.Iterator;
import java.util.List;

import com.osir.tmc.Main;
import com.osir.tmc.api.heat.HeatMaterial;
import com.osir.tmc.api.heat.HeatMaterialList;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class CapabilityLiquidContainer implements ILiquidContainer, ICapabilitySerializable<NBTTagCompound> {
	public static final ResourceLocation KEY = new ResourceLocation(Main.MODID, "liquid_container");

	protected int capacity;
	protected List<IHeatable> materials;

	public CapabilityLiquidContainer() {
		this(144);
	}

	public CapabilityLiquidContainer(int capacity) {
		this.capacity = capacity;
		this.materials = NonNullList.create();
	}

	@Override
	public List<IHeatable> getMaterial() {
		return this.materials;
	}

	@Override
	public boolean hasMaterial(HeatMaterial material) {
		for (IHeatable heat : this.materials) {
			if (heat.getMaterial().equals(material)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int addMaterial(IHeatable material) {
		if (material.getUnit() <= 0) {
			return 0;
		}
		int unit = Math.min(this.capacity - this.getUsedCapacity(), material.getUnit());
		for (int i = 0; i < this.materials.size(); i++) {
			IHeatable heat = this.materials.get(i);
			if (heat.getMaterial().equals(material.getMaterial())) {
				heat.increaseUnit(unit, false);
				heat.setEnergy(heat.getEnergy() + material.getEnergy() * unit / material.getUnit());
				if (heat.getUnit() == 0) {
					this.materials.remove(i);
				}
				return material.getUnit() - unit;
			}
		}
		IHeatable copy = new CapabilityHeat(material.getMaterial(), unit);
		copy.setEnergy(material.getEnergy() * unit / material.getUnit());
		this.materials.add(copy);
		return material.getUnit() - unit;
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
	public int getUsedCapacity() {
		int used = 0;
		for (IHeatable heat : this.materials) {
			used += heat.getUnit();
		}
		return used;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == ModCapabilities.LIQUID_CONTAINER;
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
		NBTTagList list = new NBTTagList();
		IStorage<IHeatable> storage = ModCapabilities.HEATABLE.getStorage();
		for (IHeatable heat : this.materials) {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setInteger("material", HeatMaterialList.REGISTRY_HEATABLE_MATERIAL.getIDForObject(heat.getMaterial()));
			tag.setInteger("unit", heat.getUnit());
			tag.setTag("capability", storage.writeNBT(ModCapabilities.HEATABLE, heat, null));
			list.appendTag(tag);
		}
		nbt.setTag("material", list);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		NBTTagList list = nbt.getTagList("material", 10);
		Iterator<NBTBase> ite = list.iterator();
		IStorage<IHeatable> storage = ModCapabilities.HEATABLE.getStorage();
		this.materials.clear();
		while (ite.hasNext()) {
			NBTTagCompound tag = (NBTTagCompound) ite.next();
			HeatMaterial material = HeatMaterialList.REGISTRY_HEATABLE_MATERIAL
					.getObjectById(tag.getInteger("material"));
			IHeatable heat = new CapabilityHeat(material, tag.getInteger("unit"));
			storage.readNBT(ModCapabilities.HEATABLE, heat, null, tag.getTag("capability"));
			this.materials.add(heat);
		}
	}
}