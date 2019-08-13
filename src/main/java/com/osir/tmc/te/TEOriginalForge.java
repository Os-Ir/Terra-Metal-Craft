package com.osir.tmc.te;

import com.osir.tmc.api.osir.tmc.heat.HeatRecipe;
import com.osir.tmc.api.osir.tmc.heat.HeatRegistry;
import com.osir.tmc.api.osir.tmc.inter.IHeatable;
import com.osir.tmc.handler.CapabilityHandler;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class TEOriginalForge extends TEHeatBlock {
	public static final float RATE = 1;

	public TEOriginalForge() {
		this.inventory = new ItemStackHandler(9) {
			@Override
			public int getSlotLimit(int slot) {
				if (slot < 6) {
					return 1;
				}
				return 64;
			}
		};
	}

	@Override
	public void update() {
		if (this.world.isRemote) {
			return;
		}
		int i;
		if (!this.inventory.getStackInSlot(0).isEmpty() && this.inventory.getStackInSlot(1).isEmpty()) {
			this.inventory.setStackInSlot(1, this.inventory.getStackInSlot(0));
			this.inventory.setStackInSlot(0, ItemStack.EMPTY);
		}
		if (!this.inventory.getStackInSlot(1).isEmpty() && this.inventory.getStackInSlot(2).isEmpty()) {
			this.inventory.setStackInSlot(2, this.inventory.getStackInSlot(1));
			this.inventory.setStackInSlot(1, ItemStack.EMPTY);
		}
		if (this.burnTime <= 0 && !this.inventory.getStackInSlot(2).isEmpty()) {
			this.inventory.setStackInSlot(2, ItemStack.EMPTY);
			this.burnTime = 1600;
		}
		if (this.burnTime > 0) {
			this.energy += 400;
			this.burnTime--;
		}
		this.energy -= Math.max((this.temp - 20) * 0.05F, 1);
		this.energy = Math.min(Math.max(this.energy, 0), 809600);
		this.temp = (int) (this.energy / 920) + 20;
		for (i = 3; i < 6; i++) {
			ItemStack stack = this.inventory.getStackInSlot(i);
			IHeatable cap = stack.getCapability(CapabilityHandler.heatable, null);
			if (cap == null) {
				continue;
			}
			float delta = (this.temp - cap.getTemp()) * RATE;
			if (delta > 0) {
				delta = Math.max(delta, 1);
			} else {
				delta = Math.min(delta, -1);
			}
			cap.setIncreaseEnergy(delta);
			this.energy -= delta;
			if (cap.getUnit() == 0) {
				HeatRecipe recipe = HeatRegistry.findRecipe(stack);
				if (recipe != null) {
					ItemStack output = recipe.getOutput();
					if (output != null && !output.isEmpty()) {
						this.inventory.setStackInSlot(i, output.copy());
					} else {
						this.inventory.setStackInSlot(i, ItemStack.EMPTY);
					}
				}
			}
		}
		if (this.world != null) {
			this.world.markChunkDirty(this.pos, this);
		}
	}

	// private void heatItem(int idx) {
	// ItemStack stack = inventory.getStackInSlot(idx);
	// if (stack == null || stack.isEmpty() || stack.getCount() != 1) {
	// return;
	// }
	// HeatRecipe recipe = HeatRegistry.findIndex(stack);
	// if (recipe == null) {
	// return;
	// }
	// float delta = HeatTool.update(stack, temp, 2);
	// if (delta != 0) {
	// this.energy -= delta;
	// inventory.setStackInSlot(idx, HeatTool.setEnergy(stack, delta));
	// }
	// }
	//
	// private void recipeUpdate(int idx) {
	// ItemStack stack = inventory.getStackInSlot(idx);
	// if (!HeatTool.isMelt(stack) || HeatTool.getUnmeltedUnit(stack) > 0) {
	// return;
	// }
	// float temp = HeatTool.getTemp(stack);
	// ItemStack out = HeatRegistry.findIndex(stack).getOutput();
	// if (out == null) {
	// stack = ItemStack.EMPTY;
	// } else {
	// stack = out.copy();
	// if (HeatRegistry.findIndex(stack) != null) {
	// stack = HeatTool.setEnergy(stack, temp);
	// }
	// }
	// inventory.setStackInSlot(idx, stack);
	// }
	//
	// @Override
	// public void update() {
	// if (world.isRemote) {
	// return;
	// }
	// int i;
	// if (!inventory.getStackInSlot(0).isEmpty() &&
	// inventory.getStackInSlot(1).isEmpty()) {
	// inventory.setStackInSlot(1, inventory.getStackInSlot(0));
	// inventory.setStackInSlot(0, ItemStack.EMPTY);
	// }
	// if (!inventory.getStackInSlot(1).isEmpty() &&
	// inventory.getStackInSlot(2).isEmpty()) {
	// inventory.setStackInSlot(2, inventory.getStackInSlot(1));
	// inventory.setStackInSlot(1, ItemStack.EMPTY);
	// }
	// if (burnTime <= 0 && !inventory.getStackInSlot(2).isEmpty()) {
	// inventory.setStackInSlot(2, ItemStack.EMPTY);
	// burnTime = 1600;
	// }
	// for (i = 3; i < 6; i++) {
	// heatItem(i);
	// recipeUpdate(i);
	// }
	// float delta;
	// ItemStack stack;
	// for (i = 6; i < 9; i++) {
	// stack = inventory.getStackInSlot(i);
	// if (HeatTool.hasEnergy(stack)) {
	// delta = HeatTool.update(stack, 20, 0.05F);
	// inventory.setStackInSlot(i, HeatTool.setEnergy(stack, delta));
	// }
	// }
	// if (burnTime > 0) {
	// delta = 2 * (900 - temp);
	// } else {
	// delta = 0.2F * (20 - temp);
	// }
	// this.burnTime--;
	// this.energy += delta;
	// if (this.energy < 0) {
	// this.energy = 0;
	// }
	// if (this.energy > 900000) {
	// this.energy = 900000;
	// }
	// this.temp = 20 + this.energy / 1000;
	// this.markDirty();
	// }
}