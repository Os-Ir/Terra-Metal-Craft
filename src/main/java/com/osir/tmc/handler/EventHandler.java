package com.osir.tmc.handler;

import java.util.List;

import com.osir.tmc.Main;
import com.osir.tmc.api.heat.HeatRecipe;
import com.osir.tmc.api.heat.HeatRegistry;
import com.osir.tmc.api.inter.IHeatable;
import com.osir.tmc.api.inter.ILiquidContainer;
import com.osir.tmc.capability.CapabilityHeat;
import com.osir.tmc.capability.CapabilityLiquidContainer;
import com.osir.tmc.capability.ItemStackInventory;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber(modid = Main.MODID)
public class EventHandler {
	// private static int tipIdx;

	@SubscribeEvent
	public static void onAttachCapabilitiesItem(AttachCapabilitiesEvent<ItemStack> e) {
		ItemStack stack = e.getObject();
		if (stack.hasCapability(CapabilityHandler.HEATABLE, null)) {
			return;
		}
		HeatRecipe recipe = HeatRegistry.findRecipe(stack);
		if (recipe != null) {
			e.addCapability(CapabilityHeat.KEY,
					new CapabilityHeat.Implementation(recipe.getMaterial(), recipe.getUnit()));
		}
		if (stack.getItem() == ItemHandler.ITEM_MOULD) {
			e.addCapability(CapabilityLiquidContainer.KEY, new CapabilityLiquidContainer.Implementation(1, 144, 230));
			e.addCapability(ItemStackInventory.KEY, new ItemStackInventory(1));
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onHeatableItemTooltip(ItemTooltipEvent e) {
		ItemStack stack = e.getItemStack();
		if (!stack.hasCapability(CapabilityHandler.HEATABLE, null)) {
			return;
		}
		IHeatable cap = stack.getCapability(CapabilityHandler.HEATABLE, null);
		List tooltip = e.getToolTip();
		long time = 0;
		if (e.getEntityPlayer() != null && e.getEntityPlayer().getEntityWorld() != null) {
			time = e.getEntityPlayer().getEntityWorld().getTotalWorldTime();
			// if (time % 200 == 0) {
			// tipIdx++;
			// }
		}
		// tooltip.add(tipIdx + "");
		tooltip.add(TextFormatting.AQUA + I18n.format("item.heatable.tip.title"));
		tooltip.add(TextFormatting.AQUA + "--" + I18n.format("item.heatable.tip.heating"));
		HeatRecipe recipe = HeatRegistry.findRecipe(stack);
		ItemStack output = recipe.getOutput();
		if (output == null || output.isEmpty()) {
			tooltip.add(TextFormatting.AQUA + "--" + I18n.format("item.heatable.tip.melting"));
		} else {
			tooltip.add(TextFormatting.AQUA + "--" + I18n.format("item.heatable.tip.making") + " "
					+ TextFormatting.YELLOW + I18n.format(output.getItem().getUnlocalizedName() + ".name"));
		}
		tooltip.add(TextFormatting.BLUE + I18n.format("item.heatable.material.meltPoint") + " " + TextFormatting.GOLD
				+ cap.getMeltTemp() + TextFormatting.GREEN + I18n.format("item.unit.temperature"));
		tooltip.add(TextFormatting.BLUE + I18n.format("item.heatable.material.specificHeat") + " " + TextFormatting.GOLD
				+ cap.getSpecificHeat() + TextFormatting.GREEN + I18n.format("item.unit.specificHeat"));
		tooltip.add(TextFormatting.BLUE + I18n.format("item.heatable.state.volume") + " " + TextFormatting.GOLD
				+ cap.getUnit() + TextFormatting.GREEN + I18n.format("item.unit.volume"));
		int temp = cap.getTemp();
		tooltip.add(TextFormatting.BLUE + I18n.format("item.heatable.state.temperature") + " " + TextFormatting.GOLD
				+ temp + TextFormatting.GREEN + I18n.format("item.unit.temperature"));
		if (cap.getUnit() != cap.getCompleteUnit()) {
			tooltip.add(I18n.format("item.heatable.state.melted"));
		} else {
			float t = (float) (temp - 20) / (cap.getMeltTemp() - 20);
			String info = "";
			if (t >= 0.9) {
				info += (time % 10 < 5 ? TextFormatting.RED : TextFormatting.WHITE)
						+ I18n.format("item.heatable.state.danger") + TextFormatting.WHITE + " | ";
			}
			if (output != null && !output.isEmpty()) {
				if (t >= 0.8) {
					info += TextFormatting.WHITE + I18n.format("item.heatable.state.weldable") + " | ";
				}
				if (t >= 0.6) {
					info += TextFormatting.WHITE + I18n.format("item.heatable.state.workable");
				}
			}
			if (!info.equals("")) {
				tooltip.add(info);
			}
		}
		if (cap.getTemp() != 20) {
			tooltip.add(cap.getColor());
		}
	}

	@SubscribeEvent
	public static void onLiquidContainerTooltip(ItemTooltipEvent e) {
		ItemStack stack = e.getItemStack();
		if (!stack.hasCapability(CapabilityHandler.LIQUID_CONTAINER, null)) {
			return;
		}
		ILiquidContainer cap = stack.getCapability(CapabilityHandler.LIQUID_CONTAINER, null);
		List tooltip = e.getToolTip();
		tooltip.add(TextFormatting.BLUE + I18n.format("item.liquidContainer.state.capacity") + " " + TextFormatting.GOLD
				+ cap.getCapacity() + TextFormatting.GREEN + I18n.format("item.unit.volume"));
	}

	@SubscribeEvent
	public static void onTempUpdate(LivingUpdateEvent e) {
		if (e.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) e.getEntityLiving();
			InventoryPlayer inv = player.inventory;
			int i;
			for (i = 0; i < inv.getSizeInventory(); i++) {
				ItemStack stack = inv.getStackInSlot(i);
				if (!stack.hasCapability(CapabilityHandler.HEATABLE, null)) {
					continue;
				}
				IHeatable cap = stack.getCapability(CapabilityHandler.HEATABLE, null);
				float delta = Math.min((20 - cap.getTemp()) * 5, -200);
				cap.setIncreaseEnergy(delta);
			}
		}
	}
}