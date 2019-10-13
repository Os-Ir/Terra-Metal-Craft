package com.osir.tmc.handler;

import com.osir.tmc.Main;
import com.osir.tmc.network.MessageAnvilButton;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {
	public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(Main.MODID);

	public static void register() {
		NETWORK.registerMessage(new MessageAnvilButton.Handler(), MessageAnvilButton.class, 0, Side.SERVER);
	}
}