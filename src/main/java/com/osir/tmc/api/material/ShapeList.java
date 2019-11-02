package com.osir.tmc.api.material;

import com.osir.tmc.Main;

import net.minecraft.util.ResourceLocation;

public class ShapeList {
	public static final MaterialShape BLOCK = new MaterialShape(new ResourceLocation(Main.MODID, "block"), 1296);
	public static final MaterialShape INGOT = new MaterialShape(new ResourceLocation(Main.MODID, "ingot"), 144);
	public static final MaterialShape NUGGET = new MaterialShape(new ResourceLocation(Main.MODID, "nugget"), 16);

	public static void register() {
		BLOCK.register("block");
		INGOT.register("ingot");
		NUGGET.register("nugget");
	}
}