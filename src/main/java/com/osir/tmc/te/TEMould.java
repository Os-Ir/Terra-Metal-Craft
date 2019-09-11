package com.osir.tmc.te;

import net.minecraft.util.ITickable;
import net.minecraftforge.items.ItemStackHandler;

public class TEMould extends TEInventory implements ITickable {
	public TEMould() {
		this.inventory = new ItemStackHandler(1);
	}

	@Override
	public void update() {

	}
}