package com.dmissoh.biologic.models;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

public class Event {

	private long end;
	private long begin;
	private Color color;
	private String family;
	private Rectangle layout;
	private boolean isPunctual;

	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	public long getBegin() {
		return begin;
	}

	public void setBegin(long begin) {
		this.begin = begin;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}

	public long duration() {
		return (getEnd() - getBegin());
	}

	public final Rectangle getLayout() {
		return layout;
	}

	public final void setLayout(Rectangle layout) {
		this.layout = layout;
	}

	public final Color getColor() {
		return color;
	}

	public final void setColor(Color color) {
		this.color = color;
	}

	public final boolean isPunctual() {
		return isPunctual;
	}

	public final void setPunctual(boolean isPunctual) {
		this.isPunctual = isPunctual;
	}
}
