package com.osir.tmc.block;

import java.util.List;
import java.util.Random;

import com.github.zi_jing.cuckoolib.util.math.MathUtil;
import com.osir.tmc.ModCreativeTab;
import com.osir.tmc.Main;
import com.osir.tmc.api.gui.SimpleUIHolder;
import com.osir.tmc.api.gui.factory.CapabilitySyncedUIFactory;
import com.osir.tmc.api.model.ICustomModel;
import com.osir.tmc.handler.BlockHandler;
import com.osir.tmc.handler.ItemHandler;
import com.osir.tmc.te.TEOriginalForge;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockOriginalForge extends BlockContainer implements ICustomModel {
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool BURN = PropertyBool.create("burn");

	public BlockOriginalForge() {
		super(Material.ROCK);
		this.setUnlocalizedName("original_forge");
		this.setRegistryName(Main.MODID, "original_forge");
		this.setHardness(2);
		this.setSoundType(SoundType.STONE);
		this.setHarvestLevel("pickaxe", 0);
		this.setCreativeTab(ModCreativeTab.tabEquipment);
		this.setDefaultState(
				this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(BURN, false));
		BlockHandler.BLOCK_REGISTRY.register(this);
		ItemHandler.ITEM_REGISTRY.register(new ItemBlock(this).setRegistryName(Main.MODID, "original_forge"));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(TextFormatting.BLUE + I18n.format("tile.equipment.rated_power") + " " + TextFormatting.GOLD
				+ MathUtil.format(TEOriginalForge.POWER * 20) + TextFormatting.GREEN + "W");
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
		CapabilitySyncedUIFactory.INSTANCE.openSyncedUI((SimpleUIHolder) world.getTileEntity(pos),
				(EntityPlayerMP) player);
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
		boolean burn = (meta & 4 >> 2) == 1;
		return this.getDefaultState().withProperty(FACING, facing).withProperty(BURN, burn);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		EnumFacing facing = state.getValue(FACING);
		int burn = state.getValue(BURN) ? 4 : 0;
		return facing.getHorizontalIndex() | burn;
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

	@Override
	public ModelResourceLocation getBlockModel(ModelRegistryEvent e) {
		return new ModelResourceLocation(Main.MODID + ":original_forge", "inventory");
	}

	@Override
	public int getMetaData(ModelRegistryEvent e) {
		return 0;
	}
}