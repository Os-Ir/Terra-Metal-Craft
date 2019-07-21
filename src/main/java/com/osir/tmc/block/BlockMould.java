package com.osir.tmc.block;

import com.osir.tmc.CreativeTabList;
import com.osir.tmc.handler.ItemHandler;

import api.osir.tmc.inter.IBag;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.items.ItemStackHandler;

public class BlockMould extends Block {
	private ItemStackHandler item = new ItemStackHandler(1);

	public BlockMould() {
		super(Material.ROCK);
		this.setUnlocalizedName("mould");
		this.setRegistryName("mould");
		this.setHardness(0.5F);
		this.setSoundType(SoundType.STONE);
		this.setHarvestLevel("pickaxe", 0);
		this.setCreativeTab(CreativeTabList.tabEquipment);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return true;
		}
		return true;
	}
}