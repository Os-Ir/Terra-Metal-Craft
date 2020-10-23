package com.osir.tmc.api;

import com.github.zi_jing.cuckoolib.recipe.RecipeMap;
import com.github.zi_jing.cuckoolib.recipe.RecipeValueFormat;
import com.osir.tmc.api.recipe.AnvilRecipeHelper;

public class Registries {
	public static final RecipeValueFormat<Integer> FORMAT_ANVIL_WORK = new RecipeValueFormat<Integer>("anvil_work",
			(obj) -> true, AnvilRecipeHelper.buildWorkInfo(75));

	public static final RecipeMap MAP_ANVIL_WORK = new RecipeMap("anvil", 1, 2, 1, 4, 0, 0, 0, 0);
}