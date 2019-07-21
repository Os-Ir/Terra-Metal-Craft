package api.osir.tmc.inter;

import api.osir.tmc.heat.HeatMaterial;
import net.minecraft.item.ItemStack;

public interface IHeatable {
	HeatMaterial getMaterial(ItemStack stack);

	int getUnit(ItemStack stack);

	boolean isSmeltable(ItemStack is);
}