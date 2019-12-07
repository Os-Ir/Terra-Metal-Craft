package com.osir.tmc.handler.recipe;

import java.util.ArrayList;
import java.util.List;

import com.osir.tmc.api.recipe.AnvilRecipeType;
import com.osir.tmc.api.recipe.RecipeValueFormat;
import com.osir.tmc.api.recipe.ScalableRecipe;
import com.osir.tmc.api.recipe.ScalableRecipeBuilder;

import gregtech.api.recipes.RecipeMap;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class AnvilRecipeHandler {
	public static final RecipeMap<ScalableRecipeBuilder> REGISTRY = new RecipeMap<ScalableRecipeBuilder>("anvil", 1, 2,
			1, 4, 0, 0, 0, 0, new ScalableRecipeBuilder());

	public static final RecipeValueFormat FORMAT_ANVIL = new RecipeValueFormat("type", AnvilRecipeType.class, false);

	private static final ScalableRecipeBuilder BUILDER = REGISTRY.recipeBuilder();

	public static void register() {
		BUILDER.EUt(1);
		BUILDER.duration(1);
		BUILDER.addFormat(FORMAT_ANVIL);

		BUILDER.setValue("type", AnvilRecipeType.WELD);
		BUILDER.input(OrePrefix.ingot, Materials.Iron, 2);
		BUILDER.outputs(OreDictUnifier.get(OrePrefix.valueOf("ingotDouble"), Materials.Iron, 1));
		BUILDER.buildAndRegister();

		List input = new ArrayList();
		input.add(new ItemStack(Items.IRON_INGOT, 2));
		ScalableRecipe recipe = (ScalableRecipe) REGISTRY.findRecipe(1, input, new ArrayList(), 0);
		System.out.println(recipe.getValueList().size());
	}
}