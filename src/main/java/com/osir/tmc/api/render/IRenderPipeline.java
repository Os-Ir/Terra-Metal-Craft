package com.osir.tmc.api.render;

import codechicken.lib.render.BlockRenderer.BlockFace;
import codechicken.lib.render.pipeline.IVertexOperation;
import net.minecraft.util.EnumFacing;

public interface IRenderPipeline {
	public static final ThreadLocal<BlockFace> BLOCK_FACE = ThreadLocal.withInitial(BlockFace::new);

	IVertexOperation[] getPipeline(IVertexOperation[] pipeline, EnumFacing facing);

	IIndexModel getModel(EnumFacing facing);
}