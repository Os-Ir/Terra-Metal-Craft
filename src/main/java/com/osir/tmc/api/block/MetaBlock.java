package com.osir.tmc.api.block;

import java.util.Iterator;

import com.osir.tmc.CreativeTabList;
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

import gregtech.api.util.GTControlledRegistry;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MetaBlock extends BlockContainer implements ICustomModel, IStateMapperModel {
	protected String modid;

	public MetaBlock(String modid, Material material) {
		super(material);
		this.modid = modid;
		this.setRegistryName(modid, "meta_block." + MaterialNameMap.MAP.get(material));
		this.setCreativeTab(CreativeTabList.tabEquipment);
		this.setSoundType(SoundType.METAL);
		BlockHandler.BLOCK_REGISTRY.register(this);
		ItemHandler.ITEM_REGISTRY.register(new MetaBlockItem(this));
	}

	public String getModid() {
		return this.modid;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		MetaTileEntity te = (MetaTileEntity) world.getTileEntity(pos);
		MetaValueTileEntity meta = MetaTileEntityRegistry.getMetaTileEntity(stack);
		if (meta != null) {
			te.init(meta.create(), this);
		} else {
			Main.getLogger().warn("The MetaValueTileEntity of this block hasn't registered");
		}
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
		GTControlledRegistry<String, MetaValueTileEntity> registry = MetaTileEntityRegistry.getRegistry(this);
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
			GTControlledRegistry<String, MetaValueTileEntity> registry = MetaTileEntityRegistry.getRegistry(this);
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
		return new ExtendedBlockState(this, new IProperty[] {}, new IUnlistedProperty[] {});
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