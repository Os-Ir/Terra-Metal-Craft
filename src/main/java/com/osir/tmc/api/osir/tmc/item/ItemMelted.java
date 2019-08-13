package com.osir.tmc.api.osir.tmc.item;

import com.osir.tmc.api.osir.tmc.metal.Metal;
import com.osir.tmc.api.osir.tmc.metal.MetalRegistry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemMelted extends Item {
	private static final boolean SMELTABLE = true;
	private int unit;
	private String metal;

	public ItemMelted(String name, String metal) {
		this.setRegistryName(name);
		this.metal = metal;
	}

	public Metal getMetal(ItemStack stack) {
		if (metal == null) {
			return MetalRegistry.getMetal(this);
		}
		return MetalRegistry.getMetal(metal);
	}
}