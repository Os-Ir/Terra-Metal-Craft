package com.osir.tmc.handler;

import com.osir.tmc.Main;
import com.osir.tmc.api.capability.CapabilityHeat;
import com.osir.tmc.api.capability.CapabilityList;
import com.osir.tmc.api.capability.IHeatable;
import com.osir.tmc.api.recipe.ModRecipeMap;
import com.osir.tmc.api.recipe.ScalableRecipe;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = Main.MODID)
public class EventHandler {
	@SubscribeEvent
	public static void onAttachCapabilitiesItem(AttachCapabilitiesEvent<ItemStack> e) {
		ItemStack stack = e.getObject();
		if (stack.hasCapability(CapabilityList.HEATABLE, null)) {
			return;
		}
		ScalableRecipe recipe = (ScalableRecipe) ModRecipeMap.MAP_HEAT.findRecipe(1, NonNullList.withSize(1, stack),
				NonNullList.create(), 0);
		if (recipe != null) {
			 e.addCapability(CapabilityHeat.KEY, new CapabilityHeat());
		}
		// HeatRecipe recipe = HeatRegistry.findRecipe(stack);
		// if (recipe != null) {
		// e.addCapability(CapabilityHeat.KEY, new CapabilityHeat(recipe.getMaterial(),
		// recipe.getUnit()));
		// }
		// if (stack.getItem() == ItemHandler.ITEM_MOULD) {
		// e.addCapability(CapabilityLiquidContainer.KEY, new
		// CapabilityLiquidContainer(1, 144, 230));
		// e.addCapability(CapabilityInventory.KEY, new CapabilityInventory(1));
		// }
	}

	@SubscribeEvent
	public static void onTempUpdate(LivingUpdateEvent e) {
		if (e.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) e.getEntityLiving();
			InventoryPlayer inv = player.inventory;
			int i;
			for (i = 0; i < inv.getSizeInventory(); i++) {
				ItemStack stack = inv.getStackInSlot(i);
				if (!stack.hasCapability(CapabilityList.HEATABLE, null)) {
					continue;
				}
				IHeatable cap = stack.getCapability(CapabilityList.HEATABLE, null);
				float delta = Math.min((20 - cap.getTemp()) * 0.02F, -1);
				cap.setIncreaseEnergy(delta);
			}
		}
	}
}