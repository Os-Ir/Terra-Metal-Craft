package com.osir.tmc.handler;

import com.osir.tmc.api.heat.HeatMaterialList;
import com.osir.tmc.world.OreDepositInfo;

import gregtech.api.unification.material.Materials;

public class OreHandler {
	public static void register() {
		HeatMaterialList.REGISTRY_ORE_MATERIAL.register(0, "malachite", Materials.Malachite);
		HeatMaterialList.REGISTRY_ORE_MATERIAL.register(1, "chalcopyrite", Materials.Chalcopyrite);
		HeatMaterialList.REGISTRY_ORE_MATERIAL.register(2, "cassiterite", Materials.Cassiterite);
		HeatMaterialList.REGISTRY_ORE_MATERIAL.register(3, "galena", Materials.Galena);

		OreDepositInfo.REGISTRY.register(0, "copper",
				new OreDepositInfo(Materials.Malachite, 40, Materials.Chalcopyrite, 60), 100);
		OreDepositInfo.REGISTRY.register(1, "tin", new OreDepositInfo(Materials.Cassiterite, 100), 100);
		OreDepositInfo.REGISTRY.register(2, "lead", new OreDepositInfo(Materials.Galena, 100), 100);
	}
}