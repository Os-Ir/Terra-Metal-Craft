package com.osir.tmc.block;

import java.util.List;

import com.osir.tmc.ModCreativeTab;
import com.osir.tmc.api.model.ICustomModel;
import com.osir.tmc.Main;
import com.osir.tmc.handler.BlockHandler;
import com.osir.tmc.handler.ItemHandler;
import com.osir.tmc.te.TEBasin;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBasin extends BlockContainer implements ICustomModel {
	public static final PropertyBool WATER = PropertyBool.create("water");
	private static final AxisAlignedBB BASIN_AABB = new AxisAlignedBB(0, 0, 0, 1, 0.375, 1);

	public BlockBasin() {
		super(Material.WOOD);
		this.setUnlocalizedName("basin");
		this.setRegistryName(Main.MODID, "basin");
		this.setHardness(1);
		this.setSoundType(SoundType.WOOD);
		this.setCreativeTab(ModCreativeTab.tabEquipment);
		this.setDefaultState(this.blockState.getBaseState().withProperty(WATER, false));
		BlockHandler.BLOCK_REGISTRY.register(this);
		ItemHandler.ITEM_REGISTRY.register(new ItemBlock(this).setRegistryName(Main.MODID, "basin"));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(I18n.format("tile.equipment.basin_1"));
		tooltip.add(I18n.format("tile.equipment.basin_2"));
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		TEBasin te = (TEBasin) world.getTileEntity(pos);
		ItemStack held = player.getHeldItem(hand);
		if (held.getItem() == Items.WATER_BUCKET) {
			if (te.fillByBucket() && !player.isCreative()) {
				player.setHeldItem(hand, new ItemStack(Items.BUCKET));
			}
		} else if (held.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
			IFluidHandlerItem fluid = held.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
			te.fill(fluid);
		} else {
			ItemStack result = te.applyRecipe(held);
			if (!result.isEmpty()) {
				player.inventory.addItemStackToInventory(result);
				if (!result.isEmpty()) {
					player.dropItem(result, false, false);
				}
			}
		}
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		world.setBlockState(pos, state.withProperty(WATER, false));
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		boolean water = meta == 1;
		return this.getDefaultState().withProperty(WATER, water);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		boolean water = state.getValue(WATER);
		return water ? 1 : 0;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, WATER);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BASIN_AABB;
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
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TEBasin();
	}

	@Override
	public ModelResourceLocation getBlockModel(ModelRegistryEvent e) {
		return new ModelResourceLocation(Main.MODID + ":basin", "inventory");
	}

	@Override
	public int getMetaData(ModelRegistryEvent e) {
		return 0;
	}
}