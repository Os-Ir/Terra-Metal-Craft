package com.osir.tmc.te;

import com.osir.tmc.handler.HeatableItemHandler;

import api.osir.tmc.heat.HeatRecipe;
import api.osir.tmc.heat.HeatRegistry;
import api.osir.tmc.heat.HeatTool;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class TEOriginalForge extends TEHeatBlock {
	public TEOriginalForge() {
		inventory = new ItemStackHandler(9) {
			@Override
			public int getSlotLimit(int slot) {
				if (slot < 6) {
					return 1;
				}
				return 64;
			}
		};
	}

	private void heatItem(int idx) {
		ItemStack stack = inventory.getStackInSlot(idx);
		if (stack == null || stack.isEmpty() || stack.getCount() != 1) {
			return;
		}
		HeatRecipe recipe = HeatRegistry.findIndex(stack);
		if (recipe == null) {
			return;
		}
		float delta = HeatTool.update(stack, temp, 2);
		if (delta != 0) {
			this.energy -= delta;
			inventory.setStackInSlot(idx, HeatTool.setEnergy(stack, delta));
		}
	}

	private void recipeUpdate(int idx) {
		ItemStack stack = inventory.getStackInSlot(idx);
		if (!HeatTool.isMelt(stack) || HeatTool.getUnmeltedUnit(stack) > 0) {
			return;
		}
		float temp = HeatTool.getTemp(stack);
		ItemStack out = HeatRegistry.findIndex(stack).getOutput();
		if (out == null) {
			stack = ItemStack.EMPTY;
		} else {
			stack = out.copy();
			if (HeatRegistry.findIndex(stack) != null) {
				stack = HeatTool.setEnergy(stack, temp);
			}
		}
		inventory.setStackInSlot(idx, stack);
	}

	@Override
	public void update() {
		if (world.isRemote) {
			return;
		}
		int i;
		if (!inventory.getStackInSlot(0).isEmpty() && inventory.getStackInSlot(1).isEmpty()) {
			inventory.setStackInSlot(1, inventory.getStackInSlot(0));
			inventory.setStackInSlot(0, ItemStack.EMPTY);
		}
		if (!inventory.getStackInSlot(1).isEmpty() && inventory.getStackInSlot(2).isEmpty()) {
			inventory.setStackInSlot(2, inventory.getStackInSlot(1));
			inventory.setStackInSlot(1, ItemStack.EMPTY);
		}
		if (burnTime <= 0 && !inventory.getStackInSlot(2).isEmpty()) {
			inventory.setStackInSlot(2, ItemStack.EMPTY);
			burnTime = 1600;
		}
		for (i = 3; i < 6; i++) {
			heatItem(i);
			recipeUpdate(i);
		}
		float delta;
		ItemStack stack;
		for (i = 6; i < 9; i++) {
			stack = inventory.getStackInSlot(i);
			if (HeatTool.hasEnergy(stack)) {
				delta = HeatTool.update(stack, 20, 0.05F);
				inventory.setStackInSlot(i, HeatTool.setEnergy(stack, delta));
			}
		}
		if (burnTime > 0) {
			delta = 2 * (900 - temp);
		} else {
			delta = 0.2F * (20 - temp);
		}
		this.burnTime--;
		this.energy += delta;
		if (this.energy < 0) {
			this.energy = 0;
		}
		if (this.energy > 900000) {
			this.energy = 900000;
		}
		this.temp = 20 + this.energy / 1000;
		this.markDirty();
	}
}