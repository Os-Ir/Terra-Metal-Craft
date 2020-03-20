package com.osir.tmc.api.capability;

public interface IWorkable {
	int getWorkProgress();

	void setWorkProgress(int progress);

	void addWorkProgress(int add);

	boolean isWorked();
}