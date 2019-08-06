package api.osir.tmc.heat;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;

public class HeatTool {
	// public static float update(ItemStack stack, float envi, float rate) {
	// float temp = getTemp(stack);
	// float energy = getEnergy(stack);
	// float delta = rate * (envi - temp);
	// if (energy + delta < 0) {
	// delta = -energy;
	// }
	// return delta;
	// }
	//
	// public static boolean hasEnergy(ItemStack stack) {
	// if (stack == null || stack.isEmpty()) {
	// return false;
	// }
	// if (stack.hasTagCompound()) {
	// if (stack.getTagCompound().hasKey("energy")) {
	// return true;
	// }
	// }
	// return false;
	// }
	//
	// public static ItemStack setEnergy(ItemStack stack, float energy) {
	// energy += getEnergy(stack);
	// int unit = getCompleteUnit(stack) / 144;
	// float specificHeat = getSpecificHeat(stack), melt = getMeltHeat(stack) - 20;
	// float max = specificHeat * melt * unit;
	// NBTTagCompound nbt = stack.getTagCompound();
	// if (nbt == null) {
	// nbt = new NBTTagCompound();
	// }
	// if (energy <= 0) {
	// nbt.removeTag("energy");
	// } else if (energy > max) {
	// float surplus;
	// if (nbt.hasKey("surplus")) {
	// surplus = nbt.getFloat("surplus");
	// } else {
	// surplus = 0;
	// }
	// surplus += energy - max;
	// nbt.setFloat("surplus", surplus);
	// nbt.setFloat("energy", max);
	// } else {
	// nbt.setFloat("energy", energy);
	// }
	// stack.setTagCompound(nbt);
	// return stack;
	// }
	//
	// public static float getTemp(ItemStack stack) {
	// if (hasEnergy(stack)) {
	// return 20 + getEnergy(stack) / getSpecificHeat(stack) /
	// (getCompleteUnit(stack) / 144);
	// }
	// return 20;
	// }
	//
	// public static float getEnergy(ItemStack stack) {
	// if (hasEnergy(stack)) {
	// return stack.getTagCompound().getFloat("energy");
	// }
	// return 0;
	// }
	//
	// public static boolean isWorkable(ItemStack stack) {
	// if (isMelt(stack)) {
	// return false;
	// }
	// return getTemp(stack) > getMeltHeat(stack) * 0.6;
	// }
	//
	// public static boolean isWeldable(ItemStack stack) {
	// if (isMelt(stack)) {
	// return false;
	// }
	// return getTemp(stack) > getMeltHeat(stack) * 0.8;
	// }
	//
	// public static boolean isDanger(ItemStack stack) {
	// if (isMelt(stack)) {
	// return false;
	// }
	// return getTemp(stack) > getMeltHeat(stack) * 0.9;
	// }
	//
	// public static boolean isMelt(ItemStack stack) {
	// return getCompleteUnit(stack) > getUnmeltedUnit(stack);
	// }
	//
	// public static int getMeltedUnit(ItemStack stack) {
	// if (stack == null || stack.isEmpty() || !stack.hasTagCompound()) {
	// return 0;
	// }
	// NBTTagCompound nbt = stack.getTagCompound();
	// if (nbt.hasKey("surplus")) {
	// return Math.min(
	// (int) (nbt.getFloat("surplus") / (getSpecificHeat(stack) *
	// (getMeltHeat(stack) - 20) / 10) * 144),
	// getCompleteUnit(stack));
	// }
	// return 0;
	// }
	//
	// public static int getUnmeltedUnit(ItemStack stack) {
	// return getCompleteUnit(stack) - getMeltedUnit(stack);
	// }
	//
	// public static int getCompleteUnit(ItemStack stack) {
	// if (stack == null || stack.isEmpty()) {
	// return 0;
	// }
	// HeatRecipe recipe = HeatRegistry.findIndex(stack);
	// if (recipe == null) {
	// return 0;
	// }
	// return recipe.getUnit();
	// }
	//
	// public static float getMeltHeat(ItemStack stack) {
	// if (stack == null || stack.isEmpty()) {
	// return 20;
	// }
	// HeatRecipe recipe = HeatRegistry.findIndex(stack);
	// if (recipe == null) {
	// return 20;
	// }
	// return recipe.getMeltTemp();
	// }
	//
	// public static float getSpecificHeat(ItemStack stack) {
	// if (stack == null || stack.isEmpty()) {
	// return 1;
	// }
	// HeatRecipe recipe = HeatRegistry.findIndex(stack);
	// if (recipe == null) {
	// return 1;
	// }
	// return recipe.getSpecificHeat();
	// }
}