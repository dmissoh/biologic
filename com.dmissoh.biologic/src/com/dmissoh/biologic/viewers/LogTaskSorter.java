/**
 * (c) Copyright Mirasol Op'nWorks Inc. 2002, 2003. 
 * http://www.opnworks.com
 * Created on Apr 2, 2003 by lgauthier@opnworks.com
 * 
 */

package com.dmissoh.biologic.viewers;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import com.dmissoh.biologic.models.Entry;

/**
 * Sorter for the TableViewerExample that displays items of type
 * <code>ExampleTask</code>. The sorter supports three sort criteria:
 * <p>
 * <code>DESCRIPTION</code>: Task description (String)
 * </p>
 * <p>
 * <code>OWNER</code>: Task Owner (String)
 * </p>
 * <p>
 * <code>PERCENT_COMPLETE</code>: Task percent completed (int).
 * </p>
 */
public class LogTaskSorter extends ViewerSorter {

	/**
	 * Constructor argument values that indicate to sort items by description,
	 * owner or percent complete.
	 */
	public final static int DESCRIPTION = 1;
	public final static int OWNER = 2;
	public final static int PERCENT_COMPLETE = 3;

	// Criteria that the instance uses
	private int criteria;

	/**
	 * Creates a resource sorter that will use the given sort criteria.
	 * 
	 * @param criteria
	 *            the sort criterion to use: one of <code>NAME</code> or
	 *            <code>TYPE</code>
	 */
	public LogTaskSorter(int criteria) {
		super();
		this.criteria = criteria;
	}

	/*
	 * (non-Javadoc) Method declared on ViewerSorter.
	 */
	public int compare(Viewer viewer, Object o1, Object o2) {

		Entry task1 = (Entry) o1;
		Entry task2 = (Entry) o2;

		switch (criteria) {
		case DESCRIPTION:
			return compareDescriptions(task1, task2);
		case OWNER:
			return compareOwners(task1, task2);
		case PERCENT_COMPLETE:
			return comparePercentComplete(task1, task2);
		default:
			return 0;
		}
	}

	/**
	 * Returns a number reflecting the collation order of the given tasks based
	 * on the percent completed.
	 * 
	 * @param task1
	 * @param task2
	 * @return a negative number if the first element is less than the second
	 *         element; the value <code>0</code> if the first element is equal
	 *         to the second element; and a positive number if the first element
	 *         is greater than the second element
	 */
	private int comparePercentComplete(Entry task1, Entry task2) {
		return collator.compare(task1.getType(), task2
				.getType());
	}

	/**
	 * Returns a number reflecting the collation order of the given tasks based
	 * on the description.
	 * 
	 * @param task1
	 *            the first task element to be ordered
	 * @param resource2
	 *            the second task element to be ordered
	 * @return a negative number if the first element is less than the second
	 *         element; the value <code>0</code> if the first element is equal
	 *         to the second element; and a positive number if the first element
	 *         is greater than the second element
	 */
	protected int compareDescriptions(Entry task1, Entry task2) {
		long diff = task1.getTimeStamp() - task2.getTimeStamp();
		return ((int) diff) * (-1);
	}

	/**
	 * Returns a number reflecting the collation order of the given tasks based
	 * on their owner.
	 * 
	 * @param resource1
	 *            the first resource element to be ordered
	 * @param resource2
	 *            the second resource element to be ordered
	 * @return a negative number if the first element is less than the second
	 *         element; the value <code>0</code> if the first element is equal
	 *         to the second element; and a positive number if the first element
	 *         is greater than the second element
	 */
	protected int compareOwners(Entry task1, Entry task2) {
		return collator.compare(task1.getName(), task2.getName());
	}

	/**
	 * Returns the sort criteria of this this sorter.
	 * 
	 * @return the sort criterion
	 */
	public int getCriteria() {
		return criteria;
	}
}
