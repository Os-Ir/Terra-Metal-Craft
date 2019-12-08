package com.osir.tmc.handler;

import com.osir.tmc.api.capability.CapabilityHeat;
import com.osir.tmc.api.capability.CapabilityLiquidContainer;
import com.osir.tmc.api.capability.IHeatable;
import com.osir.tmc.api.capability.ILiquidContainer;
import com.osir.tmc.capability.StorageHeat;
import com.osir.tmc.capability.StorageLiquidContainer;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityHandler {
	public static void register() {
		CapabilityManager.INSTANCE.register(IHeatable.class, new StorageHeat(), CapabilityHeat::new);
		CapabilityManager.INSTANCE.register(ILiquidContainer.class, new StorageLiquidContainer(),
				CapabilityLiquidContainer::new);
	}
}