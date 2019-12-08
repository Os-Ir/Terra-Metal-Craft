package com.osir.tmc.api.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class CapabilityList {
	@CapabilityInject(IHeatable.class)
	public static final Capability<IHeatable> HEATABLE = null;

	@CapabilityInject(ILiquidContainer.class)
	public static final Capability<ILiquidContainer> LIQUID_CONTAINER = null;
}