package com.osir.tmc.block;

import java.util.List;

import com.osir.tmc.CreativeTabList;
import com.osir.tmc.Main;
import com.osir.tmc.api.gui.SimpleUIHolder;
import com.osir.tmc.api.gui.factory.CapabilitySyncedUIFactory;
import com.osir.tmc.api.render.ICustomModel;
import com.osir.tmc.api.render.IStateMapperModel;
import com.osir.tmc.handler.BlockHandler;
import com.osir.tmc.handler.ItemHandler;
import com.osir.tmc.te.TEAnvil;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockAnvil extends BlockContainer implements ICustomModel, IStateMapperModel {
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
		BlockHandler.BLOCK_REGISTRY.register(this);
		ItemHandler.ITEM_REGISTRY
				.register(new ItemBlock(this).setRegistryName(Main.MODID, "anvil." + material.getAnvilMaterial()));
	}

	public AnvilMaterialList getAnvilMaterial() {
		return this.material;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(I18n.format("tile.equipment.anvil"));
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
		}
		return ANVIL_AABB_B;
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
	public ModelResourceLocation getBlockModel(ModelRegistryEvent e) {
		return new ModelResourceLocation(Main.MODID + ":anvil", "inventory");
	}

	@Override
	public int getMetaData(ModelRegistryEvent e) {
		return 0;
	}

	@Override
	public IStateMapper getStateMapper(ModelRegistryEvent e) {
		return new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				return new ModelResourceLocation(Main.MODID + ":anvil", this.getPropertyString(state.getProperties()));
			}
		};
	}
}