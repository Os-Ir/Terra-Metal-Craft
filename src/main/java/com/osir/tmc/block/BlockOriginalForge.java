package com.osir.tmc.block;

import java.util.Random;

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
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockOriginalForge extends TEBlock {
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool BURN = PropertyBool.create("burn");

	public BlockOriginalForge() {
		super(Material.ROCK);
		this.setUnlocalizedName("original_forge");
		this.setRegistryName("original_forge");
		this.setHardness(2);
		this.setSoundType(SoundType.STONE);
		this.setHarvestLevel("pickaxe", 1);
		this.setCreativeTab(CreativeTabList.tabEquipment);
		this.setDefaultState(
				this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(BURN, false));
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		if (state.getValue(BURN)) {
			return 15;
		}
		return 0;
	}

	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if (!state.getValue(BURN)) {
			return;
		}
		EnumFacing facing = state.getValue(FACING);
		double tx = pos.getX() + 0.5;
		double ty = pos.getY() + (rand.nextDouble() * 5 + 1) / 16;
		double tz = pos.getZ() + 0.5;
		double wdt = rand.nextDouble() * 0.625 - 0.3125;
		double dlt = 0.52;
		if (rand.nextDouble() < 0.1) {
			world.playSound(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE,
					SoundCategory.BLOCKS, 1, 1, false);
		}
		switch (facing) {
		case WEST:
			world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, tx - dlt, ty, tz + wdt, 0, 0, 0);
			world.spawnParticle(EnumParticleTypes.FLAME, tx - dlt, ty, tz + wdt, 0, 0, 0);
			break;
		case EAST:
			world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, tx + dlt, ty, tz + wdt, 0, 0, 0);
			world.spawnParticle(EnumParticleTypes.FLAME, tx + dlt, ty, tz + wdt, 0, 0, 0);
			break;
		case NORTH:
			world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, tx + wdt, ty, tz - dlt, 0, 0, 0);
			world.spawnParticle(EnumParticleTypes.FLAME, tx + wdt, ty, tz - dlt, 0, 0, 0);
			break;
		case SOUTH:
			world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, tx + wdt, ty, tz + dlt, 0, 0, 0);
			world.spawnParticle(EnumParticleTypes.FLAME, tx + wdt, ty, tz + dlt, 0, 0, 0);
			break;
		default:
			break;
		}
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
		return facing.getHorizontalIndex() | (burn ? 1 : 0) << 2;
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