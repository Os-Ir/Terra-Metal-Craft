package com.osir.tmc.handler;

import com.osir.tmc.Main;
import com.osir.tmc.container.ContainerAnvil;
import com.osir.tmc.gui.GuiAnvil;
import com.osir.tmc.te.TEAnvil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class GuiHandler implements IGuiHandler {
	public GuiHandler() {
		NetworkRegistry.INSTANCE.registerGuiHandler(Main.instance, this);
	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
		switch (id) {
		case 2:
			return new ContainerAnvil((TEAnvil) te, player);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
		switch (id) {
		case 2:
			return new GuiAnvil(new ContainerAnvil((TEAnvil) te, player));
		}
		return null;
	}
}