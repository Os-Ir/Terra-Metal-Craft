package com.osir.tmc.handler;

import java.util.ArrayList;
import java.util.Arrays;

import com.osir.tmc.Main;
import com.osir.tmc.api.capability.CapabilityHeat;
import com.osir.tmc.api.capability.CapabilityList;
import com.osir.tmc.api.capability.IHeatable;
import com.osir.tmc.api.heat.MaterialStack;
import com.osir.tmc.api.recipe.ModRecipeMap;
import com.osir.tmc.api.recipe.ScalableRecipe;
import com.osir.tmc.api.util.ItemIndex;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
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
		ItemIndex idx = new ItemIndex(stack, ModRecipeMap.VALI_STACK);
		if (ModRecipeMap.REGISTRY_MATERIAL.containsKey(idx)) {
			MaterialStack mat = ModRecipeMap.REGISTRY_MATERIAL.getObject(idx);
			int maxTemp = mat.getMaterial().getMeltTemp();
			ScalableRecipe recipe = (ScalableRecipe) ModRecipeMap.MAP_HEAT.findRecipe(0, Arrays.asList(stack),
					new ArrayList(), 0);
			if (recipe != null) {
				maxTemp = Math.min((int) recipe.getValue("temp"), maxTemp);
			}
			e.addCapability(CapabilityHeat.KEY, new CapabilityHeat(mat.getMaterial(), mat.getAmount(), maxTemp));
		}
	}

	@SubscribeEvent
	public static void onTempUpdate(LivingUpdateEvent e) {
		if (e.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) e.getEntityLiving();
			InventoryPlayer inv = player.inventory;
			for (int i = 0; i < inv.getSizeInventory(); i++) {
				ItemStack stack = inv.getStackInSlot(i);
				if (!stack.hasCapability(CapabilityList.HEATABLE, null)) {
					continue;
				}
				IHeatable cap = stack.getCapability(CapabilityList.HEATABLE, null);
				float delta = Math.min((20 - cap.getTemp()) * 0.02F, -1);
				cap.increaseEnergy(delta);
			}
		}
	}
}