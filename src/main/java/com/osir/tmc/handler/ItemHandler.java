package com.osir.tmc.handler;

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
import com.github.zi_jing.cuckoolib.util.registry.UnorderedRegistry;
import com.osir.tmc.Main;
import com.osir.tmc.ModCreativeTab;
import com.osir.tmc.item.ModMetaItem;
import com.osir.tmc.tool.ToolChisel;
import com.osir.tmc.tool.ToolHammer;
import com.osir.tmc.tool.ToolKnife;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = Main.MODID)
public class ItemHandler {
	public static final UnorderedRegistry<Item> ITEM_REGISTRY = new UnorderedRegistry<Item>();

	public static final MetaItem META_ITEM = (MetaItem) new NormalMetaItem(Main.MODID, "meta_item")
			.setCreativeTab(ModCreativeTab.tabItem);
	public static final MaterialMetaItem MATERIAL_ITEM = (MaterialMetaItem) new MaterialMetaItem(Main.MODID,
			"material_item", SolidShapes.DUST, SolidShapes.PLATE, SolidShapes.KNIFE_HEAD, SolidShapes.HAMMER_HEAD,
			SolidShapes.CHISEL_HEAD).setCreativeTab(ModCreativeTab.tabMaterial);
	public static final ToolMetaItem TOOL_ITEM = (ToolMetaItem) new ToolMetaItem(Main.MODID, "tool_item")
			.setCreativeTab(ModCreativeTab.tabItem);

	public static MetaValueItem coin, grindedFlint, chippedFlint, toolEngraving;
	public static ToolMetaValueItem toolKnife, toolChisel, toolHammer;

	@SubscribeEvent
	public static void register(Register<Item> e) {
		e.getRegistry().registerAll(ITEM_REGISTRY.list().toArray(new Item[0]));
	}

	public static void registerMetaItem() {
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
		toolChisel = TOOL_ITEM.addItem(1, "chisel").setToolInfo(ToolChisel.INSTANCE);
		toolHammer = TOOL_ITEM.addItem(2, "hammer").setToolInfo(ToolHammer.INSTANCE);
	}
}