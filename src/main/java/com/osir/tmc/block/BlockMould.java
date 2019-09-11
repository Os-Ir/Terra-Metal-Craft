package com.osir.tmc.block;

import java.util.List;

import com.osir.tmc.CreativeTabList;
import com.osir.tmc.api.inter.ILiquidContainer;
import com.osir.tmc.capability.CapabilityLiquidContainer;
import com.osir.tmc.handler.CapabilityHandler;
import com.osir.tmc.handler.ItemHandler;
import com.osir.tmc.te.TELiquidContainer;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public class BlockMould extends TEBlock {
	public static final AxisAlignedBB MOULD_AABB = new AxisAlignedBB(0.125, 0, 0.125, 0.875, 0.25, 0.875);

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
	public final void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state,
			int fortune) {
		TELiquidContainer te = (TELiquidContainer) world.getTileEntity(pos);
		IItemHandlerModifiable handler = (IItemHandlerModifiable) te
				.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		ILiquidContainer liquid = te.getCapability(CapabilityHandler.LIQUID_CONTAINER, null);
		ItemStack stack = new ItemStack(ItemHandler.ITEM_MOULD);
		IItemHandlerModifiable handlerStack = (IItemHandlerModifiable) stack
				.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		ILiquidContainer liquidStack = stack.getCapability(CapabilityHandler.LIQUID_CONTAINER, null);
		IStorage storage = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.getStorage();
		storage.readNBT(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, handlerStack, null,
				storage.writeNBT(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, handler, null));
		storage = CapabilityHandler.LIQUID_CONTAINER.getStorage();
		storage.readNBT(CapabilityHandler.LIQUID_CONTAINER, liquidStack, null,
				storage.writeNBT(CapabilityHandler.LIQUID_CONTAINER, liquid, null));
		drops.add(stack);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		IItemHandlerModifiable handler;
		ILiquidContainer liquid;
		if (stack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
			handler = (IItemHandlerModifiable) stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		} else {
			handler = new ItemStackHandler(1);
		}
		if (stack.hasCapability(CapabilityHandler.LIQUID_CONTAINER, null)) {
			liquid = stack.getCapability(CapabilityHandler.LIQUID_CONTAINER, null);
		} else {
			liquid = new CapabilityLiquidContainer.Implementation(1, 144, 230);
		}
		world.setTileEntity(pos, new TELiquidContainer(liquid, (ItemStackHandler) handler));
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player,
			boolean willHarvest) {
		if (willHarvest) {
			return true;
		}
		return super.removedByPlayer(state, world, pos, player, willHarvest);
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te,
			ItemStack stack) {
		super.harvestBlock(world, player, pos, state, te, stack);
		world.setBlockToAir(pos);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return true;
		}
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TELiquidContainer) {
			if (te.hasCapability(CapabilityHandler.LIQUID_CONTAINER, null)) {
				IItemHandlerModifiable handler = (IItemHandlerModifiable) te
						.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
				ItemStack stack = handler.getStackInSlot(0);
				if (stack != null && !stack.isEmpty()) {
					System.out.println("saved: " + stack);
				} else {
					System.out.println("empty");
				}
				ILiquidContainer liquid = te.getCapability(CapabilityHandler.LIQUID_CONTAINER, null);
			}
		}
		return true;
	}

	@Override
	public void addInformation(ItemStack stack, World world, List tooltip, ITooltipFlag flag) {
		tooltip.add(I18n.format("item.mould.description"));
		if (stack.hasCapability(CapabilityHandler.LIQUID_CONTAINER, null)) {
			ILiquidContainer liquid = stack.getCapability(CapabilityHandler.LIQUID_CONTAINER, null);

		}
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return MOULD_AABB;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TELiquidContainer();
	}
}