package com.osir.tmc.api.heat;

import gregtech.api.unification.material.Materials;

public class HeatMaterialList {
	public static final HeatMaterial EMPTY = new HeatMaterial(Materials._NULL, 1F, 20);

	public static final HeatMaterial IRON = new HeatMaterial(Materials.Iron, 0.46F, 1535);
	public static final HeatMaterial COPPER = new HeatMaterial(Materials.Copper, 0.39F, 1083);
	public static final HeatMaterial SILVER = new HeatMaterial(Materials.Silver, 0.24F, 962);
	public static final HeatMaterial TIN = new HeatMaterial(Materials.Tin, 0.24F, 232);
	public static final HeatMaterial ANTIMONY = new HeatMaterial(Materials.Antimony, 0.21F, 631);
	public static final HeatMaterial GOLD = new HeatMaterial(Materials.Gold, 0.13F, 1064);
	public static final HeatMaterial LEAD = new HeatMaterial(Materials.Lead, 0.13F, 328);

	public static final HeatMaterial WROUGHT_IRON = new HeatMaterial(Materials.WroughtIron, 0.47F, 1530);
	public static final HeatMaterial PIG_IRON = new HeatMaterial(Materials.PigIron, 0.53F, 1480);
	public static final HeatMaterial STEEL = new HeatMaterial(Materials.Steel, 0.49F, 1520);

	public static final HeatMaterial GLASS = new HeatMaterial(Materials.Glass, 0.96F, 1650);
}