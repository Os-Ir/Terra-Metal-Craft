package com.osir.tmc.block;

import com.osir.tmc.te.TEBase;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class TEBlock extends BlockContainer {
	protected TEBlock(Material material) {
		super(material);
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TEBase) {
			((TEBase) te).onBlockBreak();
		}
		super.breakBlock(world, pos, state);
	}
}