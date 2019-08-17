package com.osir.tmc.handler;

import com.osir.tmc.api.inter.IHeatable;
import com.osir.tmc.api.inter.ILiquidContainer;
import com.osir.tmc.capability.CapabilityHeat;
import com.osir.tmc.capability.CapabilityLiquidContainer;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityHandler {
	@CapabilityInject(IHeatable.class)
	public static final Capability<IHeatable> HEATABLE = null;

	@CapabilityInject(ILiquidContainer.class)
	public static final Capability<ILiquidContainer> LIQUID_CONTAINER = null;

	public static void register() {
		CapabilityManager.INSTANCE.register(IHeatable.class, new CapabilityHeat.Storage(),
				CapabilityHeat.Implementation::new);
		CapabilityManager.INSTANCE.register(ILiquidContainer.class, new CapabilityLiquidContainer.Storage(),
				CapabilityLiquidContainer.Implementation::new);
	}
}