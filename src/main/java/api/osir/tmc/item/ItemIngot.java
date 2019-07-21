package api.osir.tmc.item;

import java.util.HashMap;
import java.util.Map;

import api.osir.tmc.heat.HeatMaterial;
import api.osir.tmc.heat.HeatTool;
import api.osir.tmc.inter.IHeatable;
import api.osir.tmc.metal.Metal;
import api.osir.tmc.metal.MetalRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemIngot extends Item implements IHeatable {
	private static final int UNIT = 144;
	private static final boolean SMELTABLE = true;
	private String metal;

	public ItemIngot(String regName, String unlName, String metal) {
		this.setRegistryName(regName);
		this.setUnlocalizedName(unlName);
		this.metal = metal;
	}

	public Metal getMetal(ItemStack stack) {
		if (metal == null) {
			return MetalRegistry.getMetal(this);
		}
		return MetalRegistry.getMetal(metal);
	}

	@Override
	public HeatMaterial getMaterial(ItemStack stack) {
		return this.getMetal(stack).getMaterial();
	}

	@Override
	public int getUnit(ItemStack stack) {
		return UNIT;
	}

	@Override
	public boolean isSmeltable(ItemStack stack) {
		return SMELTABLE;
	}

	@Override
	public int getItemStackLimit(ItemStack stack) {
		if (stack.hasTagCompound()) {
			NBTTagCompound nbt = stack.getTagCompound();
			if (HeatTool.hasEnergy(stack)) {
				return 1;
			}
		}
		return super.getItemStackLimit(stack);
	}
}