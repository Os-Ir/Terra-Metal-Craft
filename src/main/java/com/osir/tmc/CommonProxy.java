package com.osir.tmc;

import com.osir.tmc.handler.CapabilityHandler;
import com.osir.tmc.handler.GuiHandler;
import com.osir.tmc.handler.HeatableItemHandler;
import com.osir.tmc.handler.NetworkHandler;
import com.osir.tmc.handler.TEHandler;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
	public void preInit(FMLPreInitializationEvent e) {
		CreativeTabList.register();
		TEHandler.register();
		CapabilityHandler.register();
		NetworkHandler.register();
		HeatableItemHandler.setup();
		HeatableItemHandler.setupMetal();
	}

	public void init(FMLInitializationEvent e) {
		new GuiHandler();
	}

	public void postInit(FMLPostInitializationEvent e) {

	}
}