package com.osir.tmc.api.render;

import codechicken.lib.render.pipeline.IVertexSource;

public interface IIndexModel {
	int getStartVertex();

	int getEndVertex();

	IVertexSource getModel();
}