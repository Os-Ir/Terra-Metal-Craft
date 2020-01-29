package com.osir.tmc.handler;

import com.osir.tmc.api.TMCLog;
import com.osir.tmc.api.recipe.ModRegistry;

import gregtech.api.unification.material.Materials;

public class OreHandler {
	public static void register() {
		ModRegistry.REGISTRY_ORE_MATERIAL.register(0, "malachite", Materials.Malachite);
		ModRegistry.REGISTRY_ORE_MATERIAL.register(1, "chalcopyrite", Materials.Chalcopyrite);
		ModRegistry.REGISTRY_ORE_MATERIAL.register(2, "cassiterite", Materials.Cassiterite);
		ModRegistry.REGISTRY_ORE_MATERIAL.register(3, "galena", Materials.Galena);
	}
}