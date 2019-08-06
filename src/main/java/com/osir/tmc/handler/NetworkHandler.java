package com.osir.tmc.handler;

import com.osir.tmc.Main;
import com.osir.tmc.network.MessageHeatableItem;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {
	public static SimpleNetworkWrapper instance = NetworkRegistry.INSTANCE.newSimpleChannel(Main.MODID);

	public static void register() {
		instance.registerMessage(new MessageHeatableItem.Handler(), MessageHeatableItem.class, 0, Side.CLIENT);
	}
}