package com.dmissoh.biologic.gef;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Rectangle;

import com.dmissoh.biologic.utils.TimeUtils;

public class EventFigure extends Figure {
	private ToolbarLayout layout;

	public EventFigure(String name, long end, long start) {

		this.layout = new ToolbarLayout();
		setLayoutManager(this.layout);
		Label nameLabel = new Label();
		nameLabel.setText(name);
		setBackgroundColor(ColorConstants.lightGray);
		add(nameLabel);

		LineBorder border = new LineBorder(ColorConstants.gray);
		setBorder(border);
		setOpaque(true);

		setToolTip(new Label(" " + name + " " + "\n Start: "
				+ TimeUtils.formatToTime(start) + "\n End  : "
				+ TimeUtils.formatToTime(end) + " "));
	}

	@Override
	public void paint(Graphics graphics) {
		graphics.setAlpha(220);
		super.paint(graphics);
	}

	public void setLayout(Rectangle rec) {
		getParent().setConstraint(this, rec);
	}
}
