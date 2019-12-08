package com.osir.tmc.api.capability;

import com.osir.tmc.Main;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class CapabilityInventory implements ICapabilitySerializable<NBTTagCompound> {
	public static final ResourceLocation KEY = new ResourceLocation(Main.MODID, "item_inventory");
	private IItemHandler handler;

	public CapabilityInventory(int size) {
		this.handler = new ItemStackHandler(size);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T) this.handler;
		}
		return null;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		return ((ItemStackHandler) this.handler).serializeNBT();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		((ItemStackHandler) this.handler).deserializeNBT(nbt);
	}
}