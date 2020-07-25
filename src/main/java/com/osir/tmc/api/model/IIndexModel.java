package com.osir.tmc.api.model;

import codechicken.lib.render.pipeline.IVertexSource;

public interface IIndexModel {
	int getStartVertex();

	int getEndVertex();

	IVertexSource getModel();
}