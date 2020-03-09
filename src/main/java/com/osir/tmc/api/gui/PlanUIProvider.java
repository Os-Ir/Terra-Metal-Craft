package com.osir.tmc.api.gui;

import gregtech.api.gui.IUIHolder;

public interface PlanUIProvider extends IUIHolder {
	PlanUIHolder createHolder();
}