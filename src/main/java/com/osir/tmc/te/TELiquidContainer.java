package com.osir.tmc.te;

import com.osir.tmc.api.capability.ILiquidContainer;
import com.osir.tmc.capability.CapabilityLiquidContainer;
import com.osir.tmc.handler.CapabilityHandler;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.ItemStackHandler;

public class TELiquidContainer extends TEHeatBlock {
	protected ILiquidContainer liquid;

	public TELiquidContainer() {
		this(new CapabilityLiquidContainer.Implementation(1, 144, 230), new ItemStackHandler(1));
	}

	public TELiquidContainer(ILiquidContainer liquid, ItemStackHandler handler) {
		this.liquid = liquid;
		this.inventory = handler;
	}

	@Override
	public boolean hasCapability(Capability capability, EnumFacing facing) {
		return (capability == CapabilityHandler.LIQUID_CONTAINER && facing == null)
				|| super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return (capability == CapabilityHandler.LIQUID_CONTAINER && facing == null) ? (T) this.liquid
				: super.getCapability(capability, facing);
	}

	@Override
	public void update() {

	}

	@Override
	public void onBlockBreak() {

	}
}