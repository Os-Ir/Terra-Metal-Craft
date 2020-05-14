package com.osir.tmc.api.item;

import gregtech.api.items.metaitem.stats.IItemDurabilityManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class DurabilityManager implements IItemDurabilityManager {
	public static final IItemDurabilityManager INSTANCE = new DurabilityManager();

	@Override
	public boolean showsDurabilityBar(ItemStack stack) {
		if (!stack.hasTagCompound()) {
			return false;
		}
		NBTTagCompound nbtDamage = stack.getTagCompound().getCompoundTag("tmc.damage");
		if (nbtDamage == null) {
			return false;
		}
		return nbtDamage.getInteger("damage") != 0;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		if (!stack.hasTagCompound()) {
			return 0;
		}
		NBTTagCompound nbtDamage = stack.getTagCompound().getCompoundTag("tmc.damage");
		if (nbtDamage == null) {
			return 0;
		}
		int maxDamage = nbtDamage.getInteger("maxDamage");
		if (maxDamage == 0) {
			return 0;
		}
		return ((double) (nbtDamage.getInteger("damage"))) / maxDamage;
	}

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {
		double durability = this.getDurabilityForDisplay(stack);
		if (durability < 0.7) {
			return 0x00ff00;
		}
		if (durability < 0.9) {
			return 0xffff00;
		}
		return 0xff0000;
	}
}