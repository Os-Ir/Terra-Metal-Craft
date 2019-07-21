package api.osir.tmc.heat;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class HeatRecipe {
	private int unit;
	private float specificHeat, meltTemp;
	private ItemStack input, output;

	public HeatRecipe(int unit, ItemStack input, HeatMaterial material) {
		this.unit = unit;
		this.input = input;
		this.specificHeat = material.getSpecificHeat();
		this.meltTemp = material.getMeltTemp();
	}

	public HeatRecipe(int unit, ItemStack input, ItemStack output, HeatMaterial material) {
		this(unit, input, material);
		this.output = output;
	}

	public boolean match(ItemStack stack) {
		if (stack == null) {
			return false;
		}
		Item item = stack.getItem();
		if (item != input.getItem()) {
			return false;
		}
		if (item.getHasSubtypes() && stack.getItemDamage() != input.getItemDamage()) {
			return false;
		}
		return true;
	}

	public int getUnit() {
		return this.unit;
	}

	public float getSpecificHeat() {
		return this.specificHeat;
	}

	public float getMeltTemp() {
		return this.meltTemp;
	}

	public ItemStack getInput() {
		return this.input;
	}

	public ItemStack getOutput() {
		return this.output;
	}
}