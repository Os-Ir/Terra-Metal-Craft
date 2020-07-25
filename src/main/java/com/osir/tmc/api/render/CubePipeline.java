package com.osir.tmc.api.render;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
	protected Map<EnumFacing, List<IVertexOperation>> operation;

	public CubePipeline(BlockPos pos, AxisAlignedBB bounds) {
		this(pos, new Cuboid6(bounds));
	}

	public CubePipeline(BlockPos pos, Cuboid6 bounds) {
		this.pos = pos;
		this.bounds = bounds;
		this.texture = new HashMap<EnumFacing, TextureAtlasSprite>();
		this.operation = new HashMap<EnumFacing, List<IVertexOperation>>();
	}

	public CubePipeline addTexture(EnumFacing facing, TextureAtlasSprite texture) {
		this.texture.put(facing, texture);
		return this;
	}

	public CubePipeline addVertexOperation(IVertexOperation... operation) {
		this.addVertexOperation(EnumFacing.UP, operation);
		this.addVertexOperation(EnumFacing.DOWN, operation);
		this.addVertexOperation(EnumFacing.NORTH, operation);
		this.addVertexOperation(EnumFacing.SOUTH, operation);
		this.addVertexOperation(EnumFacing.WEST, operation);
		this.addVertexOperation(EnumFacing.EAST, operation);
		return this;
	}

	public CubePipeline addVertexOperation(EnumFacing facing, IVertexOperation... operation) {
		if (!this.operation.containsKey(facing)) {
			this.operation.put(facing, Arrays.asList(operation));
		} else {
			this.operation.get(facing).addAll(Arrays.asList(operation));
		}
		return this;
	}

	@Override
	public IVertexOperation[] getPipeline(IVertexOperation[] pipeline, EnumFacing facing) {
		Matrix4 translation = new Matrix4();
		if (this.pos != null) {
			translation.translate(this.pos.getX(), this.pos.getY(), this.pos.getZ());
		}
		IVertexOperation[] arrayPipeline = ArrayUtils.addAll(pipeline, new TransformationList(translation),
				new IconTransformation(this.texture.getOrDefault(facing, TextureUtils.getMissingSprite())));
		if (this.operation.containsKey(facing)) {
			IVertexOperation[] arrayOperation = this.operation.get(facing).toArray(new IVertexOperation[0]);
			return ArrayUtils.addAll(arrayPipeline, arrayOperation);
		}
		return arrayPipeline;
	}

	@Override
	public IIndexModel getModel(EnumFacing facing) {
		BlockFace model = BLOCK_FACE.get();
		model.loadCuboidFace(this.bounds, facing.getIndex());
		return new BlockIndexModel(model, 0, model.verts.length);
	}
}