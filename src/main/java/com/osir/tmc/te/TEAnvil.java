package com.osir.tmc.te;

import net.minecraft.util.ITickable;
import net.minecraftforge.items.ItemStackHandler;

public class TEAnvil extends TEInventory implements ITickable {
	protected int level;

	public TEAnvil(int level) {
		this.level = level;
		this.inventory = new ItemStackHandler(10);
	}

	@Override
	public void update() {

	}
}