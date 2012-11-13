package com.dmissoh.biologic.gef;

import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;

import com.dmissoh.biologic.utils.TimeUtils;

public class PunctualEventFigure extends Ellipse {

	public PunctualEventFigure(String name, long begin) {
		setOpaque(true);
		setToolTip(new Label(" " + name + "\n Start: "
				+ TimeUtils.formatToTime(begin) + " "));
	}

	@Override
	public void paint(Graphics graphics) {
		graphics.setAlpha(200);
		super.paint(graphics);
	}

	public void setLayout(Rectangle rec) {
		getParent().setConstraint(this, rec);
	}
}
