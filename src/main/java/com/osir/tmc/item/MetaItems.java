package com.osir.tmc.item;

import com.github.zi_jing.cuckoolib.material.SolidShapes;
import com.github.zi_jing.cuckoolib.metaitem.MaterialMetaItem;
import com.github.zi_jing.cuckoolib.metaitem.MetaItem;
import com.github.zi_jing.cuckoolib.metaitem.MetaValueItem;
import com.github.zi_jing.cuckoolib.metaitem.NormalMetaItem;
import com.github.zi_jing.cuckoolib.metaitem.module.IItemTooltipProvider;
import com.github.zi_jing.cuckoolib.metaitem.tool.ToolMetaItem;
import com.github.zi_jing.cuckoolib.metaitem.tool.ToolMetaValueItem;
import com.github.zi_jing.cuckoolib.util.ArraysUtil;
import com.github.zi_jing.cuckoolib.util.NBTAdapter;
import com.osir.tmc.Main;
import com.osir.tmc.ModCreativeTab;
import com.osir.tmc.tool.ToolKnife;

import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;

public class MetaItems {
	public static final MetaItem META_ITEM = (MetaItem) new NormalMetaItem(Main.MODID, "meta_item")
			.setCreativeTab(ModCreativeTab.tabItem);

	public static final MaterialMetaItem MATERIAL_ITEM = (MaterialMetaItem) new MaterialMetaItem(Main.MODID,
			"material_item", SolidShapes.DUST).setCreativeTab(ModCreativeTab.tabItem);

	public static final ToolMetaItem TOOL_ITEM = (ToolMetaItem) new ToolMetaItem(Main.MODID, "tool_item")
			.setCreativeTab(ModCreativeTab.tabItem);

	public static MetaValueItem coin;
	public static MetaValueItem grindedFlint;
	public static MetaValueItem chippedFlint;
	public static MetaValueItem toolEngraving;

	public static ToolMetaValueItem toolKnife;

	public static void register() {
		new ModMetaItem();
		ArraysUtil.registerAll(META_ITEM, MATERIAL_ITEM, TOOL_ITEM);
		coin = META_ITEM.addItem(0, "coin.tmc").addModule((IItemTooltipProvider) (stack, world, tooltip, flag) -> {
			tooltip.add(TextFormatting.GOLD + I18n.format("metaitem.tmc.coin.tmc.info"));
			tooltip.add(TextFormatting.GREEN + I18n.format("metaitem.tmc.coin.tmc.date") + " " + TextFormatting.BLUE
					+ "2019/7/21");
		});
		grindedFlint = META_ITEM.addItem(1, "grinded_flint");
		chippedFlint = META_ITEM.addItem(2, "chipped_flint");
		toolEngraving = META_ITEM.addItem(10, "tool_engraving")
				.addModule((IItemTooltipProvider) (stack, world, tooltip, flag) -> {
					NBTTagCompound nbtStack = NBTAdapter.getItemStackCompound(stack);
					if (nbtStack.hasKey("engraving_type")) {
						tooltip.add(TextFormatting.GREEN + I18n.format("metaitem.tmc.tool_engraving.type"));
					} else {
						tooltip.add(TextFormatting.YELLOW + I18n.format("metaitem.tmc.tool_engraving.empty"));
					}
				});

		toolKnife = TOOL_ITEM.addItem(0, "knife").setToolInfo(ToolKnife.INSTANCE);
	}
}