package com.osir.tmc.te;

import com.osir.tmc.api.capability.CapabilityLiquidContainer;
import com.osir.tmc.api.capability.CapabilityList;
import com.osir.tmc.api.capability.IHeatable;
import com.osir.tmc.api.capability.ILiquidContainer;
import com.osir.tmc.api.util.CapabilityUtil;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;

public class TEMould extends TileEntity implements ITickable {
	public static final int RESISTANCE = 100;

	protected ILiquidContainer liquid;

	public TEMould() {
		this.liquid = new CapabilityLiquidContainer(144);
	}

	public TEMould(ILiquidContainer liquid) {
		this.liquid = liquid;
	}

	@Override
	public void update() {
		for (IHeatable heat : this.liquid.getMaterial()) {
			CapabilityUtil.heatExchange(heat, 20, RESISTANCE);
		}
	}

	@Override
	public boolean hasCapability(Capability capability, EnumFacing facing) {
		return super.hasCapability(capability, facing) || capability == CapabilityList.LIQUID_CONTAINER;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (super.hasCapability(capability, facing)) {
			return super.getCapability(capability, facing);
		}
		if (capability == CapabilityList.LIQUID_CONTAINER) {
			return (T) this.liquid;
		}
		return null;
	}
}