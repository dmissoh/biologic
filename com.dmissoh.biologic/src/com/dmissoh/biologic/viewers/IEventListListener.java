package com.dmissoh.biologic.viewers;

import com.dmissoh.biologic.models.Entry;
/*
 * (c) Copyright Mirasol Op'nWorks Inc. 2002, 2003. 
 * http://www.opnworks.com
 * Created on Jun 11, 2003 by lgauthier@opnworks.com
 *
 */

public interface IEventListListener {
	
	/**
	 * Update the view to reflect the fact that a task was added 
	 * to the task list
	 * 
	 * @param task
	 */
	public void addTask(Entry task);
	
	/**
	 * Update the view to reflect the fact that a task was removed 
	 * from the task list
	 * 
	 * @param task
	 */
	public void removeTask(Entry task);
	
	/**
	 * Update the view to reflect the fact that one of the tasks
	 * was modified 
	 * 
	 * @param task
	 */
	public void updateTask(Entry task);
}
