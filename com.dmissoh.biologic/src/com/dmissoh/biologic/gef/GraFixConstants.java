/*
 * Created on Mar 26, 2005
 */
package com.dmissoh.biologic.gef;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.graphics.Color;

public class GraFixConstants {

	public static double xSpan = 400;
	public static double ySpan = 250;
	public static int xOffset = 80;
	public static int yOffset = 20;

	public static Color xAxisColor = ColorConstants.black;
	public static Color yAxisColor = ColorConstants.black;

	public static void setXSpan(double span) {
		xSpan = span;
	}

	public static void setYSpan(double span) {
		ySpan = span;
	}
}
