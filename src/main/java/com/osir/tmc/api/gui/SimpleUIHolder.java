package com.osir.tmc.api.gui;

import gregtech.api.gui.IUIHolder;
import gregtech.api.gui.ModularUI;
import net.minecraft.entity.player.EntityPlayer;

public interface SimpleUIHolder extends IUIHolder {
	ModularUI createUI(EntityPlayer player);
}