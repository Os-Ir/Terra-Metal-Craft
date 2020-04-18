package com.osir.tmc.api.render;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import codechicken.lib.render.BlockRenderer.BlockFace;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.texture.TextureUtils;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Matrix4;
import codechicken.lib.vec.TransformationList;
import codechicken.lib.vec.uv.IconTransformation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class CubePipeline implements IRenderPipeline {
	protected BlockPos pos;
	protected Cuboid6 bounds;
	protected Map<EnumFacing, TextureAtlasSprite> texture;

	public CubePipeline(BlockPos pos, AxisAlignedBB bounds) {
		this(pos, new Cuboid6(bounds));
	}

	public CubePipeline(BlockPos pos, Cuboid6 bounds) {
		this.pos = pos;
		this.bounds = bounds;
		this.texture = new HashMap<EnumFacing, TextureAtlasSprite>();
	}

	public CubePipeline addTexture(EnumFacing facing, TextureAtlasSprite texture) {
		this.texture.put(facing, texture);
		return this;
	}

	@Override
	public IVertexOperation[] getPipeline(IVertexOperation[] pipeline, EnumFacing facing) {
		Matrix4 translation = new Matrix4();
		if (this.pos != null) {
			translation.translate(this.pos.getX(), this.pos.getY(), this.pos.getZ());
		}
		return ArrayUtils.addAll(pipeline, new TransformationList(translation),
				new IconTransformation(this.texture.getOrDefault(facing, TextureUtils.getMissingSprite())));
	}

	@Override
	public IIndexModel getModel(EnumFacing facing) {
		BlockFace model = BLOCK_FACE.get();
		model.loadCuboidFace(this.bounds, facing.getIndex());
		return new BlockIndexModel(model, 0, model.verts.length);
	}
}