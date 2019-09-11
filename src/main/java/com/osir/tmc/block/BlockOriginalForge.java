package com.osir.tmc.block;

import com.osir.tmc.CreativeTabList;
import com.osir.tmc.Main;
import com.osir.tmc.te.TEOriginalForge;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockOriginalForge extends TEBlock {
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool BURN = PropertyBool.create("burn");

	public BlockOriginalForge() {
		super(Material.ROCK);
		this.setUnlocalizedName("originalForge");
		this.setRegistryName("original_forge");
		this.setHardness(2.0F);
		this.setSoundType(SoundType.STONE);
		this.setHarvestLevel("pickaxe", 1);
		this.setCreativeTab(CreativeTabList.tabEquipment);
		this.setDefaultState(
				this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(BURN, false));
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return true;
		}
		player.openGui(Main.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		world.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()));
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing facing = EnumFacing.getHorizontal(meta & 3);
		boolean burn = (meta & 4) != 0;
		return this.getDefaultState().withProperty(FACING, facing).withProperty(BURN, burn);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		EnumFacing facing = state.getValue(FACING);
		boolean burn = state.getValue(BURN);
		return facing.getHorizontalIndex() | (burn ? 1 : 0);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, BURN);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TEOriginalForge();
	}
}