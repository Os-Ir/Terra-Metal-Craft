package com.osir.tmc.handler;

import com.osir.tmc.Main;
import com.osir.tmc.te.TEAnvil;
import com.osir.tmc.te.TEBasin;
import com.osir.tmc.te.TEMould;
import com.osir.tmc.te.TEOriginalForge;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TEHandler {
	public static void register() {
		GameRegistry.registerTileEntity(TEOriginalForge.class, new ResourceLocation(Main.MODID, "original_forge"));
		GameRegistry.registerTileEntity(TEBasin.class, new ResourceLocation(Main.MODID, "basin"));
		GameRegistry.registerTileEntity(TEAnvil.class, new ResourceLocation(Main.MODID, "anvil"));
		GameRegistry.registerTileEntity(TEMould.class, new ResourceLocation(Main.MODID, "mould"));
	}
}