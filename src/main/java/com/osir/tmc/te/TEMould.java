package com.osir.tmc.te;

import api.osir.tmc.heat.HeatTool;
import api.osir.tmc.inter.IBag;
import api.osir.tmc.item.ItemMelted;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;
import net.minecraftforge.items.ItemStackHandler;

public class TEMould extends TEBase implements ITickable, IBag {
	public TEMould() {
		this.inventory = new ItemStackHandler(1);
	}

	public boolean isSolidified() {
		ItemStack stack = this.inventory.getStackInSlot(0);
		if (stack == null || stack.isEmpty()) {
			return false;
		}
		if (stack.getItem() instanceof ItemMelted && HeatTool.isMelt(stack)) {
			return true;
		}
		return false;
	}

	@Override
	public void update() {

	}

	@Override
	public void setItems(ItemStackHandler items) {
		this.inventory = items;
	}

	@Override
	public ItemStackHandler getItems() {
		return this.inventory;
	}

	@Override
	public boolean isEmpty() {
		ItemStack stack = this.inventory.getStackInSlot(0);
		if (stack == null || stack.isEmpty()) {
			return true;
		}
		return false;
	}
}