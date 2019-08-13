package com.osir.tmc.handler;

import com.osir.tmc.api.osir.tmc.inter.IHeatable;
import com.osir.tmc.capability.CapabilityHeat;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityHandler {
	@CapabilityInject(IHeatable.class)
	public static Capability<IHeatable> heatable;

	public static void register() {
		CapabilityManager.INSTANCE.register(IHeatable.class, new CapabilityHeat.Storage(),
				CapabilityHeat.Implementation::new);
	}
}