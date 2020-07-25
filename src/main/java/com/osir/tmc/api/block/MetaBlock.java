package com.osir.tmc.api.block;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.tuple.Pair;

import com.osir.tmc.ModCreativeTab;
import com.osir.tmc.Main;
import com.osir.tmc.api.item.MetaBlockItem;
import com.osir.tmc.api.render.ICustomModel;
import com.osir.tmc.api.render.IStateMapperModel;
import com.osir.tmc.api.render.MetaTileEntityRenderer;
import com.osir.tmc.api.te.MetaTileEntity;
import com.osir.tmc.api.te.MetaTileEntityRegistry;
import com.osir.tmc.api.te.MetaValueTileEntity;
import com.osir.tmc.handler.BlockHandler;
import com.osir.tmc.handler.ItemHandler;

import codechicken.lib.block.property.unlisted.UnlistedIntegerProperty;
import codechicken.lib.block.property.unlisted.UnlistedStringProperty;
import gregtech.api.util.GTControlledRegistry;
import gregtech.api.util.ParticleHandlerUtil;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MetaBlock extends BlockContainer implements ICustomModel, IStateMapperModel {
	private static final UnlistedStringProperty PROPERTY_HARVEST_TOOL = new UnlistedStringProperty("harvest_tool");
	public static final UnlistedIntegerProperty PROPERTY_HARVEST_LEVEL = new UnlistedIntegerProperty("harvest_level");

	protected String modid;

	protected ThreadLocal<MetaValueTileEntity> tileEntityCache = new ThreadLocal<MetaValueTileEntity>();

	public MetaBlock(String modid, Material material) {
		super(material);
		this.modid = modid;
		this.setRegistryName(modid, "meta_block." + MaterialNameMap.getMaterialName(material));
		this.setCreativeTab(ModCreativeTab.tabEquipment);
		this.setSoundType(SoundType.METAL);
		this.setHardness(4);
		this.setHarvestLevel("wrench", 1);
		this.setDefaultState(this.getDefaultState());
		BlockHandler.BLOCK_REGISTRY.register(this);
		ItemHandler.ITEM_REGISTRY.register(new MetaBlockItem(this));
	}

	public String getModid() {
		return this.modid;
	}

	@Override
	public int getHarvestLevel(IBlockState state) {
		Integer value = ((IExtendedBlockState) state).getValue(PROPERTY_HARVEST_LEVEL);
		return value == null ? -1 : value;
	}

	@Override
	public String getHarvestTool(IBlockState state) {
		return ((IExtendedBlockState) state).getValue(PROPERTY_HARVEST_TOOL);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
		MetaValueTileEntity meta = this.getMetaTileEntity(stack);
		if (meta != null) {
			meta.addInformation(stack, player, tooltip, advanced);
		}
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te,
			ItemStack stack) {
		this.tileEntityCache.set(te == null ? null : ((MetaTileEntity) te).getMetaValue());
		super.harvestBlock(world, player, pos, state, te, stack);
		this.tileEntityCache.set(null);
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state,
			int fortune) {
		MetaValueTileEntity meta = this.tileEntityCache.get();
		if (meta != null) {
			meta.getDrops(drops, world, pos, state, fortune);
		} else {
			super.getDrops(drops, world, pos, state, fortune);
		}
	}

	public MetaValueTileEntity getMetaTileEntity(IBlockAccess world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof MetaTileEntity) {
			return ((MetaTileEntity) te).getMetaValue();
		}
		return null;
	}

	public MetaValueTileEntity getMetaTileEntity(ItemStack stack) {
		return MetaTileEntityRegistry.getMetaTileEntity(stack);
	}

	public int getId(IBlockAccess world, BlockPos pos) {
		return MetaTileEntityRegistry.getId(this.getMetaTileEntity(world, pos));
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		return this.getActualState(state, world, pos);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		MetaValueTileEntity metaValue = this.getMetaTileEntity(world, pos);
		if (metaValue == null) {
			return state;
		}
		return ((IExtendedBlockState) state).withProperty(PROPERTY_HARVEST_LEVEL, metaValue.getHarvestLevel())
				.withProperty(PROPERTY_HARVEST_TOOL, metaValue.getHarvestTool());
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		MetaTileEntity te = (MetaTileEntity) world.getTileEntity(pos);
		MetaValueTileEntity meta = MetaTileEntityRegistry.getMetaTileEntity(stack);
		if (meta != null) {
			te.init(meta.create());
		} else {
			Main.getLogger().warn("The MetaValueTileEntity of this block hasn't registered");
		}
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {

	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		return this.getMetaTileEntity(world, pos).onBlockActivated(world, pos, state, playerIn, hand, facing, hitX,
				hitY, hitZ);
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
		GTControlledRegistry<String, MetaValueTileEntity> registry = MetaTileEntityRegistry.getMTERegistry(this);
		Iterator<MetaValueTileEntity> ite = registry.iterator();
		while (ite.hasNext()) {
			MetaValueTileEntity meta = ite.next();
			if (meta.supportCreativeTab(tab)) {
				items.add(new ItemStack(this, 1, registry.getIDForObject(meta)));
			}
		}
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos,
			EntityPlayer player) {
		MetaValueTileEntity meta = ((MetaTileEntity) world.getTileEntity(pos)).getMetaValue();
		if (meta != null) {
			GTControlledRegistry<String, MetaValueTileEntity> registry = MetaTileEntityRegistry.getMTERegistry(this);
			return new ItemStack(this, 1, registry.getIDForObject(meta));
		}
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return true;
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
	@SideOnly(Side.CLIENT)
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return MetaTileEntityRenderer.RENDER_TYPE;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new ExtendedBlockState(this, new IProperty[] {},
				new IUnlistedProperty[] { PROPERTY_HARVEST_TOOL, PROPERTY_HARVEST_LEVEL });
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addHitEffects(IBlockState state, World world, RayTraceResult target, ParticleManager manager) {
		MetaValueTileEntity meta = this.getMetaTileEntity(world, target.getBlockPos());
		if (meta != null) {
			Pair<TextureAtlasSprite, Integer> atlasSprite = meta.getParticleTexture(world, target.getBlockPos());
			ParticleHandlerUtil.addHitEffects(state, world, target, atlasSprite.getLeft(), atlasSprite.getRight(),
					manager);
			return true;
		}
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new MetaTileEntity();
	}

	@Override
	public ModelResourceLocation getBlockModel(ModelRegistryEvent e) {
		return MetaTileEntityRenderer.LOCATION;
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
				return new ModelResourceLocation(Main.MODID + ":meta_block",
						this.getPropertyString(state.getProperties()));
			}
		};
	}
}