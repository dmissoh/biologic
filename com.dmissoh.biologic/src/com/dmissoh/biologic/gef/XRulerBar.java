package com.dmissoh.biologic.gef;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Polyline;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;

public class XRulerBar extends Polyline {

	double xSpan = GraFixConstants.xSpan;
	double ySpan = GraFixConstants.ySpan;
	double xOffset = GraFixConstants.xOffset;
	int numSegments = 0;

	String labelData[] = null;

	public XRulerBar(IFigure contents, int segments, long during) {
		this.setLineWidth(1);
		this.setLineStyle(SWT.LINE_SOLID);
		this.setForegroundColor(GraFixConstants.xAxisColor);

		numSegments = segments;
		int sectionWidth = (int) xSpan / numSegments;
		Point start = new Point(xOffset, ySpan);
		Point end = new Point(xSpan + xOffset, ySpan);

		int labelPositions[] = new int[numSegments];
		labelPositions[0] = (int)xOffset;
		for (int i = 0; i < numSegments; i++) {
			labelPositions[i] = ((i) * sectionWidth) + (int)xOffset;
		}

		PointList points = new PointList();
		points.addPoint(start);
		points.addPoint(end);
		this.setPoints(points);

		Label labels[] = new Label[numSegments];
		for (int i = 0; i < numSegments; i++) {
			String lbl = ((i) * (((during)/numSegments))/1000) + "";
			labels[i] = new Label(lbl);
			labels[i].setForegroundColor(ColorConstants.black);
			contents.add(labels[i]);
			int xPos = labelPositions[i];
			int yPos = (int) ySpan;
			int width = 30;
			int height = 10;
			contents.setConstraint(labels[i], new Rectangle(xPos, yPos, width,
					height));
		}
	}

}
