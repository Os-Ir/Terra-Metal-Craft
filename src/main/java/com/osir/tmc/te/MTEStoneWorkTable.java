package com.osir.tmc.te;

import com.osir.tmc.Main;
import com.osir.tmc.api.te.MetaValueTileEntity;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MTEStoneWorkTable extends MetaValueTileEntity {
	public MTEStoneWorkTable() {
		super(Main.MODID, "stone_work_table", Material.ROCK);
	}

	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		return true;
	}

	@Override
	public MetaValueTileEntity create() {
		return new MTEStoneWorkTable();
	}
}