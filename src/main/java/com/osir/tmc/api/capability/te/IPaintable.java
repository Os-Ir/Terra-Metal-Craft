package com.osir.tmc.api.capability.te;

import net.minecraft.util.EnumFacing;

public interface IPaintable {
	void paint(EnumFacing side, int color);

	int getPaintedColor(EnumFacing side);
}