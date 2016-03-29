package com.zk.administrator.services;

public interface TaskCompleted {
	void onTaskCompletedSuccessfully(Object obj);

	void onTaskFailed();
}
