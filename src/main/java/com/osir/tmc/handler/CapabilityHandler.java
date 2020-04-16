package com.osir.tmc.handler;

import com.osir.tmc.api.capability.CapabilityHeat;
import com.osir.tmc.api.capability.CapabilityLiquidContainer;
import com.osir.tmc.api.capability.CapabilityList;
import com.osir.tmc.api.capability.CapabilityWork;
import com.osir.tmc.api.capability.IHeatable;
import com.osir.tmc.api.capability.ILiquidContainer;
import com.osir.tmc.api.capability.IWorkable;
import com.osir.tmc.api.capability.StorageHeat;
import com.osir.tmc.api.capability.StorageLiquidContainer;
import com.osir.tmc.api.capability.StorageWork;
import com.osir.tmc.api.capability.te.IBlowable;
import com.osir.tmc.api.container.ContainerListenerCapability;
import com.osir.tmc.api.util.CapabilityUtil;

import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityHandler {
	public static void register() {
		CapabilityManager.INSTANCE.register(IHeatable.class, new StorageHeat(), CapabilityHeat::new);
		CapabilityManager.INSTANCE.register(IWorkable.class, new StorageWork(), CapabilityWork::new);
		CapabilityManager.INSTANCE.register(ILiquidContainer.class, new StorageLiquidContainer(),
				CapabilityLiquidContainer::new);

		CapabilityUtil.registerEmptyCapability(IBlowable.class);

		ContainerListenerCapability.register(CapabilityHeat.KEY.toString(), CapabilityList.HEATABLE);
		ContainerListenerCapability.register(CapabilityWork.KEY.toString(), CapabilityList.WORKABLE);
		ContainerListenerCapability.register(CapabilityLiquidContainer.KEY.toString(), CapabilityList.LIQUID_CONTAINER);
	}
}