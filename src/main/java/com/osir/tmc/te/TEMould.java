package com.osir.tmc.te;

import api.osir.tmc.inter.IBag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;
import net.minecraftforge.items.ItemStackHandler;

public class TEMould extends TEInventory implements ITickable, IBag {
	public TEMould() {
		this.inventory = new ItemStackHandler(1);
	}

	public boolean isSolidified() {
		ItemStack stack = this.inventory.getStackInSlot(0);
		if (stack == null || stack.isEmpty()) {
			return false;
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