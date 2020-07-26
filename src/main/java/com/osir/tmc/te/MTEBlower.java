package com.osir.tmc.te;

import java.util.Arrays;
import java.util.List;

import com.osir.tmc.Main;
import com.osir.tmc.api.render.CubePipeline;
import com.osir.tmc.api.render.IRenderPipeline;
import com.osir.tmc.api.te.MetaValueTileEntity;

import codechicken.lib.vec.Cuboid6;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MTEBlower extends MetaValueTileEntity {
	public MTEBlower() {
		super(Main.MODID, "blower", Material.WOOD);
	}

	@Override
	public MetaValueTileEntity create() {
		return new MTEBlower();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public List<IRenderPipeline> getRenderPipeline(boolean renderItem) {
		BlockPos pos = renderItem ? BlockPos.ORIGIN : this.getPos();
		return Arrays.asList(new CubePipeline(pos, new Cuboid6(0.125, 0.25, 0.25, 0.875, 0.75, 0.75)),
				new CubePipeline(pos, new Cuboid6(0, 0, 0, 0.125, 1, 1)),
				new CubePipeline(pos, new Cuboid6(0.875, 0, 0, 1, 1, 1)));
	}
}