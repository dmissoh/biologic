package com.dmissoh.biologic.viewers;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.dmissoh.biologic.Activator;
import com.dmissoh.biologic.models.Entry;
import com.dmissoh.biologic.models.Entry.TYPE;
import com.dmissoh.biologic.utils.TimeUtils;

/**
 * Label provider for the TableViewerExample
 *
 * @see org.eclipse.jface.viewers.LabelProvider
 */
public class LogLabelProvider extends LabelProvider implements
		ITableLabelProvider {

	public static final String START_IMAGE = "start";
	public static final String END_IMAGE = "end";
	public static final String PUNCTUAL_IMAGE = "punctual";

	private static ImageRegistry imageRegistry = new ImageRegistry();

	private boolean isLive;

	public LogLabelProvider(boolean isLive) {
		this.isLive = isLive;
	}

	/**
	 * Note: An image registry owns all of the image objects registered with it,
	 * and automatically disposes of them the SWT Display is disposed.
	 */
	static {
		imageRegistry.put(START_IMAGE, Activator
				.getImageDescriptor("icons/log_start.gif"));
		imageRegistry.put(END_IMAGE, Activator
				.getImageDescriptor("icons/log_end.gif"));
		imageRegistry.put(PUNCTUAL_IMAGE, Activator
				.getImageDescriptor("icons/log_punctual.gif"));
	}

	private Image getImage(TYPE type) {
		String key = "";
		if (type == TYPE.START) {
			key = START_IMAGE;
		} else if (type == TYPE.END) {
			key = END_IMAGE;
		} else {
			key = PUNCTUAL_IMAGE;
		}
		return imageRegistry.get(key);
	}

	public String getColumnText(Object element, int columnIndex) {
		String result = "";
		Entry entry = (Entry) element;
		switch (columnIndex) {
		case 0:
			break;
		case 1:
			if (isLive()) {
				result = TimeUtils.formatToTime(entry.getTimeStamp());
			} else {
				result = String.valueOf(entry.getTimeStamp() / 1000);
			}
			break;
		case 2:
			result = entry.getName();
			break;
		case 3:
			result = entry.getType() + "";
			break;
		default:
			break;
		}
		return result;
	}

	public Image getColumnImage(Object element, int columnIndex) {
		return (columnIndex == 0) ? getImage(((Entry) element).getType())
				: null;
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

}
