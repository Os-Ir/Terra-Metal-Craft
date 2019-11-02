package com.osir.tmc.api.material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.osir.tmc.Main;
import com.osir.tmc.api.heat.HeatMaterial;

public class MaterialList {
	public static final List<String> SHAPE_EMPTY = new ArrayList();
	public static final List<String> SHAPE_METAL = Arrays.asList("block", "ingot", "nugget");

	public static final HeatMaterial IRON = new HeatMaterial(SHAPE_METAL, 0.46F, 1535);
	public static final HeatMaterial COPPER = new HeatMaterial(SHAPE_METAL, 0.39F, 1083);
	public static final HeatMaterial SILVER = new HeatMaterial(SHAPE_METAL, 0.24F, 962);
	public static final HeatMaterial TIN = new HeatMaterial(SHAPE_METAL, 0.24F, 232);
	public static final HeatMaterial ANTIMONY = new HeatMaterial(SHAPE_METAL, 0.21F, 631);
	public static final HeatMaterial GOLD = new HeatMaterial(SHAPE_METAL, 0.13F, 1064);
	public static final HeatMaterial LEAD = new HeatMaterial(SHAPE_METAL, 0.13F, 328);

	public static final HeatMaterial WROUGHT_IRON = new HeatMaterial(SHAPE_METAL, 0.47F, 1530);
	public static final HeatMaterial PIG_IRON = new HeatMaterial(SHAPE_METAL, 0.53F, 1480);
	public static final HeatMaterial STEEL = new HeatMaterial(SHAPE_METAL, 0.49F, 1520);

	public static final HeatMaterial SAND = new HeatMaterial(SHAPE_EMPTY, 0.96F, 820);

	public static void register() {
		MaterialRegistry.addMaterial(Main.MODID, "iron", IRON);
		MaterialRegistry.addMaterial(Main.MODID, "copper", COPPER);
		MaterialRegistry.addMaterial(Main.MODID, "silver", SILVER);
		MaterialRegistry.addMaterial(Main.MODID, "tin", TIN);
		MaterialRegistry.addMaterial(Main.MODID, "antimony", ANTIMONY);
		MaterialRegistry.addMaterial(Main.MODID, "gold", GOLD);
		MaterialRegistry.addMaterial(Main.MODID, "lead", LEAD);
		MaterialRegistry.addMaterial(Main.MODID, "wrought_iron", WROUGHT_IRON);
		MaterialRegistry.addMaterial(Main.MODID, "pig_iron", PIG_IRON);
		MaterialRegistry.addMaterial(Main.MODID, "steel", STEEL);
		MaterialRegistry.addMaterial(Main.MODID, "sand", SAND);
	}
}