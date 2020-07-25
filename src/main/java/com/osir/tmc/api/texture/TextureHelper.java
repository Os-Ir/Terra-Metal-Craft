package com.osir.tmc.api.texture;

import com.osir.tmc.Main;

import gregtech.api.gui.resources.TextureArea;
import net.minecraft.util.ResourceLocation;

public class TextureHelper {
	public static TextureArea fullImage(String modid, String location) {
		return new TextureArea(new ResourceLocation(modid, location), 0, 0, 1, 1);
	}

	public static TextureArea fullImage(String location) {
		return fullImage(Main.MODID, location);
	}
}