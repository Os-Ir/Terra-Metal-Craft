package com.osir.tmc.api.osir.tmc.metal;

import com.osir.tmc.api.osir.tmc.heat.MaterialList;
import com.osir.tmc.handler.ItemHandler;

import net.minecraft.init.Items;

public class MetalList {
	public static final Metal IRON = new Metal("iron", Items.IRON_INGOT, ItemHandler.IRON_MELTED, MaterialList.IRON);
	public static final Metal GOLD = new Metal("iron", Items.GOLD_INGOT, ItemHandler.GOLD_MELTED, MaterialList.GOLD);
}