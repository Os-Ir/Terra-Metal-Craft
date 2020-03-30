package com.osir.tmc.api.capability;

import java.util.Queue;

import com.osir.tmc.api.recipe.AnvilWorkType;

public interface IWorkable {
	int getWorkProgress();

	void setWorkProgress(int progress);

	void addWorkProgress(int add);

	boolean isWorked();

	Queue<AnvilWorkType> getLastSteps();

	void putStep(AnvilWorkType type);
}