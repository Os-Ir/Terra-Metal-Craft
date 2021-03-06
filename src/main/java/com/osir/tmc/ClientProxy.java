package com.osir.tmc;

import com.osir.tmc.api.render.MetaTileEntityRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientProxy extends CommonProxy {
	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		MetaTileEntityRenderer.INSTANCE.preInit();
	}

	@Override
	public void init(FMLInitializationEvent e) {
		super.init(e);
	}

	@Override
	public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);
	}

	@Override
	public IThreadListener getThreadListener(MessageContext context) {
		if (context.side.isClient()) {
			return Minecraft.getMinecraft();
		}
		return context.getServerHandler().player.getServer();
	}

	@Override
	public EntityPlayer getPlayer(MessageContext ctx) {
		if (ctx.side.isClient()) {
			return Minecraft.getMinecraft().player;
		}
		return ctx.getServerHandler().player;
	}
}