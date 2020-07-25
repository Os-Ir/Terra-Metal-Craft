package com.osir.tmc.world;

import java.util.List;
import java.util.Random;

import com.github.zi_jing.cuckoolib.util.registry.WeightedRegistry;
import com.osir.tmc.handler.recipe.OrePrefixRecipeHandler;

import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.type.Material;
import gregtech.api.unification.ore.OrePrefix;
import net.minecraft.item.ItemStack;

public class OreDepositInfo {
	public static final WeightedRegistry<String, OreDepositInfo> REGISTRY = new WeightedRegistry<String, OreDepositInfo>(
			100);

	protected WeightedRegistry<String, Material> ore;

	public OreDepositInfo(Object... obj) {
		this.ore = new WeightedRegistry<String, Material>(1000);
		int size = obj.length;
		if (size % 2 != 0) {
			throw new IllegalArgumentException("Material and weight length mismatch");
		}
		size /= 2;
		for (int i = 0; i < size; i++) {
			if (!(obj[i * 2] instanceof Material && obj[i * 2 + 1] instanceof Integer)) {
				throw new IllegalArgumentException("Wrong object type");
			}
			this.ore.register(i, obj[i * 2].toString(), (Material) obj[i * 2], (int) obj[i * 2 + 1]);
		}
	}

	public OreDepositInfo(List<Material> material, List<Integer> weight) {
		this.ore = new WeightedRegistry<String, Material>(1000);
		if (material.size() != weight.size()) {
			throw new IllegalStateException("Material and weight length mismatch");
		}
		int size = material.size();
		for (int i = 0; i < size; i++) {
			this.ore.register(i, material.toString(), material.get(i), weight.get(i));
		}
	}

	public WeightedRegistry<String, Material> getOreRegistry() {
		return this.ore;
	}

	public int getWeightSum() {
		return this.ore.getWeightSum();
	}

	public Material generate(Random rand) {
		return this.ore.choose();
	}

	public ItemStack generateItemStack(Random rand) {
		Material material = this.generate(rand);
		if (OrePrefixRecipeHandler.PREDICATE_ORE.test(material)) {
			return OreDictUnifier.get(OrePrefix.valueOf("oreCobble"), material);
		}
		return ItemStack.EMPTY;
	}
}