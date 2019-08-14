package com.osir.tmc.api.item;

import com.osir.tmc.api.metal.Metal;
import com.osir.tmc.api.metal.MetalRegistry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemIngot extends Item {
	private static final int UNIT = 144;
	private static final boolean SMELTABLE = true;
	private String metal;

	public ItemIngot(String regName, String unlName, String metal) {
		this.setRegistryName(regName);
		this.setUnlocalizedName(unlName);
		this.metal = metal;
	}

	public Metal getMetal(ItemStack stack) {
		if (metal == null) {
			return MetalRegistry.getMetal(this);
		}
		return MetalRegistry.getMetal(metal);
	}

	@Override
	public int getItemStackLimit(ItemStack stack) {
		if (stack.hasTagCompound()) {
			NBTTagCompound nbt = stack.getTagCompound();
			// if (HeatTool.hasEnergy(stack)) {
			// return 1;
			// }
		}
		return super.getItemStackLimit(stack);
	}
}