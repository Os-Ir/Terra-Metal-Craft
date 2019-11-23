package com.osir.tmc.handler.recipe;

import java.util.ArrayList;
import java.util.Arrays;

import com.osir.tmc.api.recipe.AnvilRecipe;
import com.osir.tmc.api.recipe.AnvilRecipeBuilder;
import com.osir.tmc.api.recipe.AnvilRecipeType;

import gregtech.api.recipes.Recipe;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class AnvilRecipeHandler {
	private static final AnvilRecipeBuilder BUILDER = AnvilRecipe.REGISTRY.recipeBuilder();

	public static void register() {
		BUILDER.EUt(1);
		BUILDER.duration(1);

		BUILDER.type(AnvilRecipeType.WELD);
		BUILDER.input(OrePrefix.ingot, Materials.Iron, 2);
		BUILDER.outputs(OreDictUnifier.get(OrePrefix.plate, Materials.Iron, 1));
		BUILDER.buildAndRegister();
	}
}