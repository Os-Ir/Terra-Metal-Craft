package com.osir.tmc.te;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;
import net.minecraftforge.items.ItemStackHandler;

public class TEAnvil extends TEInventory implements ITickable {
	protected int level;

	public TEAnvil(int level) {
		this.level = level;
		this.inventory = new ItemStackHandler(12);
	}

	@Override
	public void update() {
		ItemStack stack = this.inventory.getStackInSlot(1);
		if (stack != null && !stack.isEmpty()) {
			this.inventory.setStackInSlot(10, stack.copy());
		} else {
			this.inventory.setStackInSlot(10, ItemStack.EMPTY);
		}
		stack = this.inventory.getStackInSlot(2);
		if (stack != null && !stack.isEmpty()) {
			this.inventory.setStackInSlot(11, stack.copy());
		} else {
			this.inventory.setStackInSlot(11, ItemStack.EMPTY);
		}
	}
}