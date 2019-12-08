package com.osir.tmc.api.recipe;

import com.osir.tmc.api.heat.HeatMaterialList;
import com.osir.tmc.api.heat.MaterialStack;

import gregtech.api.recipes.RecipeMap;

public class TMCRecipeMap {
	public static final RecipeValueFormat FORMAT_ANVIL = new RecipeValueFormat("type", AnvilRecipeType.class, false,
			AnvilRecipeType.WELD);
	public static final RecipeValueFormat FORMAT_TEMP = new RecipeValueFormat("temp", int.class, false, 20);
	public static final RecipeValueFormat FORMAT_MATERIAL = new RecipeValueFormat("material", MaterialStack.class,
			false, MaterialStack.EMPTY);

	public static final RecipeMap<ScalableRecipeBuilder> MAP_ANVIL = new RecipeMap<ScalableRecipeBuilder>("anvil", 1, 2,
			1, 4, 0, 0, 0, 0, new ScalableRecipeBuilder());
	public static final RecipeMap<ScalableRecipeBuilder> MAP_HEAT = new RecipeMap<ScalableRecipeBuilder>("anvil", 1, 1,
			0, 1, 0, 0, 0, 0, new ScalableRecipeBuilder());

	public static final ScalableRecipeBuilder BUILDER_ANVIL = MAP_ANVIL.recipeBuilder();
	public static final ScalableRecipeBuilder BUILDER_HEAT = MAP_HEAT.recipeBuilder();
}