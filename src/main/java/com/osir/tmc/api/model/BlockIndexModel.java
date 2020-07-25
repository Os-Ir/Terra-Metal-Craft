package com.osir.tmc.api.model;

import codechicken.lib.render.pipeline.IVertexSource;

public class BlockIndexModel implements IIndexModel {
	protected IVertexSource model;
	protected int start, end;

	public BlockIndexModel(IVertexSource model, int start, int end) {
		this.model = model;
		this.start = start;
		this.end = end;
	}

	@Override
	public int getStartVertex() {
		return this.start;
	}

	@Override
	public int getEndVertex() {
		return this.end;
	}

	@Override
	public IVertexSource getModel() {
		return this.model;
	}
}