package com.osir.tmc.api.recipe;

import com.osir.tmc.api.heat.MaterialStack;

import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.builders.SimpleRecipeBuilder;

public class RecipeMapList {
	public static final RecipeValueFormat FORMAT_ANVIL = new RecipeValueFormat("type",
			(obj) -> obj instanceof AnvilRecipeType, AnvilRecipeType.WELD);
	public static final RecipeValueFormat FORMAT_ANVIL_WORK = new RecipeValueFormat("info",
			(obj) -> obj instanceof Integer, AnvilRecipeHelper.buildWorkInfo(75));
	public static final RecipeValueFormat FORMAT_TEMP = new RecipeValueFormat("temp", (obj) -> obj instanceof Integer,
			20);
	public static final RecipeValueFormat FORMAT_MATERIAL = new RecipeValueFormat("material",
			(obj) -> obj instanceof MaterialStack, MaterialStack.EMPTY);

	public static final RecipeMap<ScalableRecipeBuilder> MAP_ANVIL = new RecipeMap<ScalableRecipeBuilder>("anvil", 1, 2,
			1, 4, 0, 0, 0, 0, new ScalableRecipeBuilder().addFormat(FORMAT_ANVIL).EUt(1).duration(1));
	public static final RecipeMap<ScalableRecipeBuilder> MAP_ANVIL_WORK = new RecipeMap<ScalableRecipeBuilder>(
			"anvil_work", 1, 2, 1, 1, 0, 0, 0, 0,
			new ScalableRecipeBuilder().addFormat(FORMAT_ANVIL_WORK).EUt(1).duration(1));
	public static final RecipeMap<ScalableRecipeBuilder> MAP_HEAT = new RecipeMap<ScalableRecipeBuilder>("heat", 1, 1,
			0, 1, 0, 0, 0, 0,
			new ScalableRecipeBuilder().addFormat(FORMAT_TEMP).addFormat(FORMAT_MATERIAL).EUt(1).duration(1));
	public static final RecipeMap<ScalableRecipeBuilder> MAP_MATERIAL = new RecipeMap<ScalableRecipeBuilder>("material",
			1, 1, 0, 0, 0, 0, 0, 0, new ScalableRecipeBuilder().addFormat(FORMAT_MATERIAL).EUt(1).duration(1));
	public static final RecipeMap<SimpleRecipeBuilder> MAP_CLEAN = new RecipeMap<SimpleRecipeBuilder>("clean", 1, 1, 1,
			1, 0, 0, 0, 0, new SimpleRecipeBuilder().EUt(1).duration(1));
}