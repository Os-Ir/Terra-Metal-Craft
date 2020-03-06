package com.osir.tmc.handler;

import com.osir.tmc.Main;
import com.osir.tmc.api.util.registry.UnorderedRegistry;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = Main.MODID)
public class ItemHandler {
	public static final UnorderedRegistry<Item> ITEM_REGISTRY = new UnorderedRegistry<Item>();

	@SubscribeEvent
	public static void register(Register<Item> e) {
		e.getRegistry().registerAll(ITEM_REGISTRY.list().toArray(new Item[0]));
	}
}