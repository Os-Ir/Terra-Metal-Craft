package com.osir.tmc.api.render;

import java.util.Collections;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.osir.tmc.Main;
import com.osir.tmc.api.model.IIndexModel;
import com.osir.tmc.api.te.MetaTileEntity;
import com.osir.tmc.api.te.MetaTileEntityRegistry;
import com.osir.tmc.api.te.MetaValueTileEntity;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.block.BlockRenderingRegistry;
import codechicken.lib.render.block.ICCBlockRenderer;
import codechicken.lib.render.item.IItemRenderer;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.texture.TextureUtils;
import codechicken.lib.util.TransformUtils;
import gregtech.api.util.ModCompatibility;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.model.IModelState;

public class MetaTileEntityRenderer implements ICCBlockRenderer, IItemRenderer {
	public static final MetaTileEntityRenderer INSTANCE = new MetaTileEntityRenderer();
	public static final EnumBlockRenderType RENDER_TYPE = BlockRenderingRegistry.createRenderType("META");
	public static final ModelResourceLocation LOCATION = new ModelResourceLocation(Main.MODID + ":meta_block",
			"normal");

	private MetaTileEntityRenderer() {

	}

	public void preInit() {
		BlockRenderingRegistry.registerRenderer(RENDER_TYPE, INSTANCE);
	}

	@Override
	public boolean renderBlock(IBlockAccess world, BlockPos pos, IBlockState state, BufferBuilder buffer) {
		TileEntity te = world.getTileEntity(pos);
		if (!(te instanceof MetaTileEntity)) {
			return false;
		}
		MetaValueTileEntity meta = ((MetaTileEntity) te).getMetaValue();
		if (meta == null) {
			return false;
		}
		BlockRenderLayer layer = MinecraftForgeClient.getRenderLayer();
		if (meta.canRender(layer)) {
			CCRenderState render = CCRenderState.instance();
			render.reset();
			render.bind(buffer);
			render.lightMatrix.locate(world, pos);
			List<IRenderPipeline> list = meta.getRenderPipeline();
			IVertexOperation[] ops = new IVertexOperation[] { render.lightMatrix };
			for (EnumFacing facing : EnumFacing.VALUES) {
				for (IRenderPipeline pipeline : list) {
					IIndexModel model = pipeline.getModel(facing);
					render.setPipeline(model.getModel(), model.getStartVertex(), model.getEndVertex(),
							pipeline.getPipeline(ops, facing));
					render.render();
				}
			}
			meta.onRender(render);
		}
		return true;
	}

	@Override
	public void renderItem(ItemStack stack, TransformType transformType) {
		MetaValueTileEntity meta = MetaTileEntityRegistry.getMetaTileEntity(ModCompatibility.getRealItemStack(stack));
		if (meta == null) {
			return;
		}
		GlStateManager.enableBlend();
		CCRenderState render = CCRenderState.instance();
		render.reset();
		render.startDrawing(GL11.GL_QUADS, DefaultVertexFormats.ITEM);
		List<IRenderPipeline> list = meta.getRenderPipeline();
		IVertexOperation[] ops = new IVertexOperation[0];
		for (EnumFacing facing : EnumFacing.VALUES) {
			for (IRenderPipeline pipeline : list) {
				IIndexModel model = pipeline.getModel(facing);
				render.setPipeline(model.getModel(), model.getStartVertex(), model.getEndVertex(),
						pipeline.getPipeline(ops, facing));
				render.render();
			}
		}
		meta.onRender(render);
		render.draw();
		GlStateManager.disableBlend();
	}

	@Override
	public void handleRenderBlockDamage(IBlockAccess world, BlockPos pos, IBlockState state, TextureAtlasSprite sprite,
			BufferBuilder buffer) {

	}

	@Override
	public void renderBrightness(IBlockState state, float brightness) {

	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		return Collections.emptyList();
	}

	@Override
	public boolean isAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean isGui3d() {
		return true;
	}

	@Override
	public boolean isBuiltInRenderer() {
		return true;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return TextureUtils.getMissingSprite();
	}

	@Override
	public ItemOverrideList getOverrides() {
		return ItemOverrideList.NONE;
	}

	@Override
	public IModelState getTransforms() {
		return TransformUtils.DEFAULT_BLOCK;
	}

	@Override
	public void registerTextures(TextureMap map) {

	}
}