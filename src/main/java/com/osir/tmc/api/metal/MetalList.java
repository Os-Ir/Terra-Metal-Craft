package com.osir.tmc.api.metal;

import com.osir.tmc.api.heat.MaterialList;
import com.osir.tmc.handler.ItemHandler;

import net.minecraft.init.Items;

public class MetalList {
	public static final Metal IRON = new Metal("iron", Items.IRON_INGOT, MaterialList.IRON);
	public static final Metal GOLD = new Metal("iron", Items.GOLD_INGOT, MaterialList.GOLD);
}