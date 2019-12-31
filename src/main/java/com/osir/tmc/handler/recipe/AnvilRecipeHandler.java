package com.osir.tmc.handler.recipe;

import java.util.ArrayList;
import java.util.List;

import com.osir.tmc.api.recipe.AnvilRecipeType;
import com.osir.tmc.api.recipe.ModRecipeMap;
import com.osir.tmc.api.recipe.ScalableRecipe;
import com.osir.tmc.api.recipe.ScalableRecipeBuilder;

import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class AnvilRecipeHandler {
	public static void register() {
		ModRecipeMap.MAP_ANVIL.recipeBuilder().setValue("type", AnvilRecipeType.WELD)
				.input(OrePrefix.ingot, Materials.Iron, 2)
				.outputs(OreDictUnifier.get(OrePrefix.valueOf("ingotDouble"), Materials.Iron, 1)).buildAndRegister();
	}
}