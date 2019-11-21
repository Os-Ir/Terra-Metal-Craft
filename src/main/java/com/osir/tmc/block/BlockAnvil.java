package com.osir.tmc.block;

import com.osir.tmc.CreativeTabList;
import com.osir.tmc.Main;
import com.osir.tmc.api.inter.IBlockModel;
import com.osir.tmc.te.TEAnvil;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockAnvil extends BlockContainer implements IBlockModel {
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	private static final AxisAlignedBB ANVIL_AABB_A = new AxisAlignedBB(0, 0, 0.125, 1, 0.6875, 0.875);
	private static final AxisAlignedBB ANVIL_AABB_B = new AxisAlignedBB(0.125, 0, 0, 0.875, 0.6875, 1);

	private AnvilMaterialList material;

	public BlockAnvil(AnvilMaterialList material) {
		super(material.getMaterial());
		this.material = material;
		this.setUnlocalizedName("anvil." + material.getAnvilMaterial());
		this.setRegistryName(Main.MODID, "anvil." + material.getAnvilMaterial());
		this.setHardness(material.getLevel() + 3);
		this.setHarvestLevel("pickaxe", (material.getLevel() < 2) ? 1 : 2);
		this.setCreativeTab(CreativeTabList.tabEquipment);
		this.setSoundType(SoundType.ANVIL);
	}

	public AnvilMaterialList getAnvilMaterial() {
		return this.material;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return true;
		}
		player.openGui(Main.instance, 2, world, pos.getX(), pos.getY(), pos.getZ());
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
		return this.getDefaultState().withProperty(FACING, facing);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		EnumFacing facing = state.getValue(FACING);
		return facing.getHorizontalIndex();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		EnumFacing facing = state.getValue(FACING);
		if (facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH) {
			return ANVIL_AABB_A;
		} else {
			return ANVIL_AABB_B;
		}
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
		return new TEAnvil(this.material.getLevel());
	}

	@Override
	public void registerModel() {
		ModelLoader.setCustomStateMapper(this, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				return new ModelResourceLocation(Main.MODID + ":anvil", getPropertyString(state.getProperties()));
			}
		});
	}
}