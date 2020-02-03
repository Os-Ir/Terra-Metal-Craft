package com.osir.tmc.handler.recipe;

import com.osir.tmc.api.recipe.ModRecipeMap;
import com.osir.tmc.handler.OreHandler;

import gregtech.api.recipes.ModHandler;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.type.DustMaterial;
import gregtech.api.unification.material.type.Material;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.stack.UnificationEntry;

public class OrePrefixRecipeHandler {
	public static void register() {
		OrePrefix.valueOf("oreCobble").addProcessingHandler(DustMaterial.class,
				OrePrefixRecipeHandler::processOreCobble);
		OrePrefix.dustImpure.addProcessingHandler(DustMaterial.class, OrePrefixRecipeHandler::processDustImpure);
	}

	public static void processOreCobble(OrePrefix prefix, Material material) {
		if (OreHandler.PREDICATE_ORE.test(material)) {
			ModHandler.addShapelessRecipe("ore_cobble_to_dust_" + material,
					OreDictUnifier.get(OrePrefix.dustImpure, material), 'h',
					new UnificationEntry(OrePrefix.valueOf("oreCobble"), material));
		}
	}

	public static void processDustImpure(OrePrefix prefix, Material material) {
		ModRecipeMap.MAP_CLEAN.recipeBuilder().input(prefix, material)
				.outputs(OreDictUnifier.get(OrePrefix.dust, material)).buildAndRegister();
	}
}