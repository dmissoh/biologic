package com.dmissoh.biologic.gef;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Polyline;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;

public class YRulerBar extends Polyline {

	double xSpan = GraFixConstants.xSpan;
	double ySpan = GraFixConstants.ySpan;
	int maxElements = 0;
	int numSegments = 0;

	int xOffset = GraFixConstants.xOffset;
	String labelData[] = null;

	public YRulerBar(IFigure contents, String[] series) {
		this.setLineWidth(1);
		this.setLineStyle(SWT.LINE_SOLID);
		this.setForegroundColor(GraFixConstants.yAxisColor);

		maxElements = series.length;
		numSegments = series.length;
		// divide span of xAxis
		Point start = new Point(xOffset, ySpan);
		Point end = new Point(xOffset, 0);

		PointList points = new PointList();
		points.addPoint(start);
		points.addPoint(end);
		this.setPoints(points);

		Label labels[] = new Label[numSegments];
		for (int i = 0; i < numSegments; i++) {
			labels[i] = new Label(series[i]);
			labels[i].setForegroundColor(ColorConstants.black);
			contents.add(labels[i]);
			int width = 60;
			int height = 15;
			int xPos = xOffset - width;
			int yPos = (int) ySpan - (i + 1) * GraFixConstants.yOffset;
			contents.setConstraint(labels[i], new Rectangle(xPos, yPos, width,
					height));
		}
	}
}
