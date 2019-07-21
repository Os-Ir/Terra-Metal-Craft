package com.osir.tmc.handler;

import com.osir.tmc.Main;
import com.osir.tmc.te.TEOriginalForge;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TEHandler {
	public static void register() {
		GameRegistry.registerTileEntity(TEOriginalForge.class, new ResourceLocation(Main.MODID, "original_forge"));
	}
}