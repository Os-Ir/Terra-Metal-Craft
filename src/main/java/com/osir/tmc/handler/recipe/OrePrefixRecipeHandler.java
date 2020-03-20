package com.osir.tmc.handler.recipe;

import java.util.function.Predicate;

import com.osir.tmc.api.heat.HeatMaterial;
import com.osir.tmc.api.heat.MaterialStack;
import com.osir.tmc.api.recipe.AnvilRecipeHelper;
import com.osir.tmc.api.recipe.AnvilRecipeType;
import com.osir.tmc.api.recipe.ModRecipeMap;
import com.osir.tmc.api.recipe.ModRegistry;

import gregtech.api.GTValues;
import gregtech.api.recipes.ModHandler;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.type.DustMaterial;
import gregtech.api.unification.material.type.Material;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.stack.UnificationEntry;

public class OrePrefixRecipeHandler {
	public static final Predicate<Material> PREDICATE_ORE = (Material material) -> {
		return ModRegistry.REGISTRY_ORE_MATERIAL.getNameForObject(material) != null;
	};

	public static void register() {
		OrePrefix.valueOf("oreCobble").addProcessingHandler(DustMaterial.class,
				OrePrefixRecipeHandler::processOreCobble);
		OrePrefix.dustImpure.addProcessingHandler(DustMaterial.class, OrePrefixRecipeHandler::processCleanDust);
		OrePrefix.dustPure.addProcessingHandler(DustMaterial.class, OrePrefixRecipeHandler::processCleanDust);
		for (OrePrefix prefix : ModRegistry.REGISTRY_HEATABLE_PREFIX) {
			prefix.addProcessingHandler(DustMaterial.class, OrePrefixRecipeHandler::processHeat);
		}
		OrePrefix.ingot.addProcessingHandler(DustMaterial.class, OrePrefixRecipeHandler::processWeld);
		OrePrefix.plate.addProcessingHandler(DustMaterial.class, OrePrefixRecipeHandler::processWeld);
		OrePrefix.stick.addProcessingHandler(DustMaterial.class, OrePrefixRecipeHandler::processWeld);
		OrePrefix.stick.addProcessingHandler(DustMaterial.class, OrePrefixRecipeHandler::processTwine);
		OrePrefix.valueOf("ingotDouble").addProcessingHandler(DustMaterial.class, OrePrefixRecipeHandler::processWork);
	}

	public static void processWork(OrePrefix prefix, Material material) {
		OrePrefix outputPrefix = null;
		int progress = AnvilRecipeHelper.progressHash(prefix);
		if (prefix == OrePrefix.valueOf("ingotDouble")) {
			outputPrefix = OrePrefix.plate;
		}
		if (ModRegistry.REGISTRY_HEATABLE_MATERIAL.containsKey(material)
				&& !OreDictUnifier.get(prefix, material).isEmpty()
				&& !OreDictUnifier.get(outputPrefix, material).isEmpty()) {
			ModRecipeMap.MAP_ANVIL_WORK.recipeBuilder().setValue("info", AnvilRecipeHelper.buildWorkInfo(progress))
					.input(prefix, material).outputs(OreDictUnifier.get(outputPrefix, material)).buildAndRegister();
		}
	}

	public static void processWeld(OrePrefix prefix, Material material) {
		OrePrefix outputPrefix = null;
		switch (prefix) {
		case ingot:
			outputPrefix = OrePrefix.valueOf("ingotDouble");
			break;
		case plate:
			outputPrefix = OrePrefix.valueOf("plateDouble");
			break;
		case stick:
			outputPrefix = OrePrefix.stickLong;
			break;
		default:
			break;
		}
		if (ModRegistry.REGISTRY_HEATABLE_MATERIAL.containsKey(material)
				&& !OreDictUnifier.get(prefix, material).isEmpty()
				&& !OreDictUnifier.get(outputPrefix, material).isEmpty()) {
			ModRecipeMap.MAP_ANVIL.recipeBuilder().setValue("type", AnvilRecipeType.WELD).input(prefix, material, 2)
					.outputs(OreDictUnifier.get(outputPrefix, material)).buildAndRegister();
		}
	}

	public static void processTwine(OrePrefix prefix, Material material) {
		OrePrefix outputPrefix = null;
		switch (prefix) {
		case stick:
			outputPrefix = OrePrefix.ring;
			break;
		default:
			break;
		}
		if (ModRegistry.REGISTRY_HEATABLE_MATERIAL.containsKey(material)
				&& !OreDictUnifier.get(prefix, material).isEmpty()
				&& !OreDictUnifier.get(outputPrefix, material).isEmpty()) {
			ModRecipeMap.MAP_ANVIL.recipeBuilder().setValue("type", AnvilRecipeType.TWINE).input(prefix, material)
					.outputs(OreDictUnifier.get(outputPrefix, material)).buildAndRegister();
		}
	}

	public static void processHeat(OrePrefix prefix, Material material) {
		if (ModRegistry.REGISTRY_HEATABLE_MATERIAL.containsKey(material)
				&& !OreDictUnifier.get(prefix, material).isEmpty()) {
			HeatMaterial heat = ModRegistry.REGISTRY_HEATABLE_MATERIAL.getObject(material);
			ModRecipeMap.MAP_HEAT.recipeBuilder().setValue("temp", heat.getMeltTemp())
					.setValue("material",
							new MaterialStack(heat, (int) (((float) prefix.materialAmount) / GTValues.M * 144)))
					.input(prefix, material).buildAndRegister();
			ModRecipeMap.MAP_MATERIAL.recipeBuilder()
					.setValue("material",
							new MaterialStack(heat, (int) (((float) prefix.materialAmount) / GTValues.M * 144)))
					.input(prefix, material).buildAndRegister();
		}
	}

	public static void processOreCobble(OrePrefix prefix, Material material) {
		if (PREDICATE_ORE.test(material)) {
			ModHandler.addShapelessRecipe("ore_cobble_to_dust_" + material,
					OreDictUnifier.get(OrePrefix.dustImpure, material), 'h',
					new UnificationEntry(OrePrefix.valueOf("oreCobble"), material));
		}
	}

	public static void processCleanDust(OrePrefix prefix, Material material) {
		ModRecipeMap.MAP_CLEAN.recipeBuilder().input(prefix, material)
				.outputs(OreDictUnifier.get(OrePrefix.dust, material)).buildAndRegister();
	}
}