package api.osir.tmc.heat;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

public class HeatRegistry {
	private static List<HeatRecipe> recipeList = new ArrayList<HeatRecipe>();

	public static void addRecipe(HeatRecipe recipe) {
		recipeList.add(recipe);
	}

	public static HeatRecipe findRecipe(ItemStack stack) {
		int i;
		HeatRecipe recipe;
		for (i = 0; i < recipeList.size(); i++) {
			recipe = recipeList.get(i);
			if (recipe.match(stack)) {
				return recipe;
			}
		}
		return null;
	}
}