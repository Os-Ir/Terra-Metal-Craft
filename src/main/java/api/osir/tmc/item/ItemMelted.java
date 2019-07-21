package api.osir.tmc.item;

import api.osir.tmc.heat.HeatMaterial;
import api.osir.tmc.inter.IHeatable;
import api.osir.tmc.metal.Metal;
import api.osir.tmc.metal.MetalRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemMelted extends Item implements IHeatable {
	private static final boolean SMELTABLE = true;
	private int unit;
	private String metal;

	public ItemMelted(String name, String metal) {
		this.setRegistryName(name);
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
		return this.unit;
	}

	@Override
	public boolean isSmeltable(ItemStack is) {
		return SMELTABLE;
	}
}