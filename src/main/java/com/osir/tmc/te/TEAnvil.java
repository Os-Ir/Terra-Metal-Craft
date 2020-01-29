package com.osir.tmc.te;

import com.osir.tmc.api.gui.SimpleUIHolder;

import gregtech.api.gui.ModularUI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ITickable;
import net.minecraftforge.items.ItemStackHandler;

public class TEAnvil extends TEInventory implements ITickable, SimpleUIHolder {
	protected int level;

	public TEAnvil() {
		this(0);
	}

	public TEAnvil(int level) {
		this.level = level;
		this.inventory = new ItemStackHandler(10);
	}

	@Override
	public void update() {

	}

	@Override
	public boolean isValid() {
		return !super.isInvalid();
	}

	@Override
	public boolean isRemote() {
		return this.world.isRemote;
	}

	@Override
	public void markAsDirty() {
		this.markDirty();
	}

	@Override
	public ModularUI createUI(EntityPlayer player) {
		return null;
	}
}