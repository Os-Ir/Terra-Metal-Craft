package com.osir.tmc.te;

import net.minecraft.tileentity.TileEntity;

public abstract class TEBase extends TileEntity {
	public abstract void onBlockBreak();
}