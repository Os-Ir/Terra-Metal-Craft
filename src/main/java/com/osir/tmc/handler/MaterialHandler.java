package com.osir.tmc.handler;

import com.osir.tmc.api.material.MaterialList;
import com.osir.tmc.api.material.ShapeList;

public class MaterialHandler {
	public static void register() {
		ShapeList.register();
		MaterialList.register();
	}
}