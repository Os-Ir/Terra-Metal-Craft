package com.osir.tmc.item;

import com.github.zi_jing.cuckoolib.metaitem.MetaItem;
import com.github.zi_jing.cuckoolib.metaitem.MetaValueItem;
import com.github.zi_jing.cuckoolib.metaitem.module.IItemTooltipProvider;
import com.github.zi_jing.cuckoolib.util.NBTAdapter;
import com.osir.tmc.Main;
import com.osir.tmc.ModCreativeTab;
import com.osir.tmc.api.item.DurabilityProvider;

import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;

public class MetaItems {
	public static final MetaItem META_ITEM = (MetaItem) new MetaItem(Main.MODID, "meta_item")
			.setCreativeTab(ModCreativeTab.tabItem);

	public static MetaValueItem coin;
	public static MetaValueItem grindedFlint;
	public static MetaValueItem chippedFlint;
	public static MetaValueItem sharpFlint;

	public static MetaValueItem toolEngraving;

	public static void register() {
		new ModMetaItem();
		coin = META_ITEM.addItem(0, "coin.tmc").addModule((IItemTooltipProvider) (stack, world, tooltip, flag) -> {
			tooltip.add(TextFormatting.GOLD + I18n.format("metaitem.tmc.coin.tmc.info"));
			tooltip.add(TextFormatting.GREEN + I18n.format("metaitem.tmc.coin.tmc.date") + " " + TextFormatting.BLUE
					+ "2019/7/21");
		});
		grindedFlint = META_ITEM.addItem(1, "grinded_flint").addModule(DurabilityProvider.INSTANCE);
		chippedFlint = META_ITEM.addItem(2, "chipped_flint").addModule(DurabilityProvider.INSTANCE);
		toolEngraving = META_ITEM.addItem(10, "tool_engraving")
				.addModule((IItemTooltipProvider) (stack, world, tooltip, flag) -> {
					NBTTagCompound nbtStack = NBTAdapter.getItemStackCompound(stack);
					if (nbtStack.hasKey("engraving_type")) {
						tooltip.add(TextFormatting.GREEN + I18n.format("metaitem.tmc.tool_engraving.type"));
					} else {
						tooltip.add(TextFormatting.YELLOW + I18n.format("metaitem.tmc.tool_engraving.empty"));
					}
				});
	}
}