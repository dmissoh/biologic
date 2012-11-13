package com.dmissoh.biologic.gef;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.graphics.Color;

import com.dmissoh.biologic.models.Entry;
import com.dmissoh.biologic.models.Event;
import com.dmissoh.biologic.models.Family;
import com.dmissoh.biologic.models.Sequence;
import com.dmissoh.biologic.models.Entry.TYPE;

public class GraphPart extends AbstractGraphicalEditPart {

	private float scale;

	private int ypos = (int) GraFixConstants.ySpan;

	@Override
	protected IFigure createFigure() {
		if (getModel() instanceof Sequence) {
			Sequence sequence = (Sequence) getModel();
			IFigure figure = new GraphFigure(sequence);
			return figure;
		}
		return null;
	}

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.gef.editparts.AbstractGraphicalEditPart#registerVisuals()
	 */
	@Override
	protected void registerVisuals() {
		// TODO Auto-generated method stub
		super.registerVisuals();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	@Override
	protected List<Event> getModelChildren() {
		List<Event> events = new ArrayList<Event>();
		if (getModel() instanceof Sequence) {
			Sequence sequence = (Sequence) getModel();
			long startTime = sequence.getStartTime();
			long endTime = sequence.getEndTime();
			calculateScale(startTime, endTime);

			List<Family> families = sequence.getFamilies();
			for (Family family : families) {

				incrementYPos();
				Color color = getColor();

				List<Entry> entries = family.getLogEntries();
				// Make a copy
				Stack<Entry> copy = new Stack<Entry>();
				for (Entry entry : entries) {
					copy.push(entry);
				}

				while (!copy.empty()) {
					Entry startEntry = copy.pop();
					if (startEntry.getType() != TYPE.PUNCTUAL) {
						if (startEntry.getType() == TYPE.END) {
							long t1 = startEntry.getTimeStamp();
							if (!copy.empty()) {
								Entry endEntry = copy.pop();
								if (endEntry.getType() != TYPE.PUNCTUAL) {
									if (endEntry.getType() == TYPE.START) {

										long t2 = endEntry.getTimeStamp();
										addEvent(events, endEntry.getName(),
												t1, t2, startTime, color);
									}
								} else {
									addPunctualEvent(events, endEntry
											.getName(),
											endEntry.getTimeStamp(), startTime,
											color);
								}
							}
						}
					} else {
						addPunctualEvent(events, startEntry.getName(),
								startEntry.getTimeStamp(), startTime, color);
					}
				}
			}
		}
		return events;
	}

	private void addEvent(List<Event> events, String name, long t1, long t2,
			long startTime, Color color) {
		Event event = new Event();
		event.setFamily(name);
		event.setBegin(t1);
		event.setEnd(t2);

		int xPos = (int) getScaledXValue((t2 - startTime))
				+ GraFixConstants.xOffset;
		int width = (int) getScaledXValue(((t1 - startTime) - (t2 - startTime)));

		event.setLayout(new Rectangle(xPos, ypos, width, 15));
		event.setColor(color);
		events.add(event);
	}

	private void addPunctualEvent(List<Event> events, String name, long t,
			long startTime, Color color) {
		Event event = new Event();
		event.setFamily(name);
		event.setBegin(t);
		event.setPunctual(true);

		int xPos = (int) getScaledXValue((t - startTime))
				+ GraFixConstants.xOffset;

		event.setLayout(new Rectangle(xPos, ypos, 8, 8));
		event.setColor(color);
		events.add(event);
	}

	private void incrementYPos() {
		ypos = ypos - 20;
	}

	private Color getColor() {
		Color color = new Color(null, (new Double(Math.random() * 128))
				.intValue() + 128,
				(new Double(Math.random() * 128)).intValue() + 128,
				(new Double(Math.random() * 128)).intValue() + 128);
		return color;
	}

	private void calculateScale(long startTime, long endTime) {
		scale = ((float) GraFixConstants.xSpan / (endTime - startTime));
	}

	private float getScaledXValue(float value) {
		return (value * getScale());
	}

	private final float getScale() {
		return scale;
	}

}
