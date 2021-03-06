package com.osir.tmc.api.capability;

import com.osir.tmc.api.capability.te.IBlowable;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class ModCapabilities {
	@CapabilityInject(IHeatable.class)
	public static final Capability<IHeatable> HEATABLE = null;

	@CapabilityInject(IWorkable.class)
	public static final Capability<IWorkable> WORKABLE = null;

	@CapabilityInject(ILiquidContainer.class)
	public static final Capability<ILiquidContainer> LIQUID_CONTAINER = null;

	@CapabilityInject(IBlowable.class)
	public static final Capability<IBlowable> BLOWABLE = null;
}