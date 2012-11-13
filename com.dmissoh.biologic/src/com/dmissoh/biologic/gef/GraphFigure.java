package com.dmissoh.biologic.gef;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.XYLayout;

import com.dmissoh.biologic.models.Family;
import com.dmissoh.biologic.models.Sequence;

public class GraphFigure extends Figure {
	private XYLayout layout;

	public GraphFigure(Sequence sequence) {
		this.layout = new XYLayout();
		setLayoutManager(this.layout);

		long startTime = sequence.getStartTime();
		long endTime = sequence.getEndTime();
		long during = endTime - startTime;

		XRulerBar xruler = new XRulerBar(this, 10, during);

		List<String> lbls = new ArrayList<String>();
		List<Family> families = sequence.getFamilies();
		for(Family family : families){
			lbls.add(family.getName());
		}

		YRulerBar yruler = new YRulerBar(this, lbls.toArray(new String[lbls.size()]));

		add(xruler);
		add(yruler);
	}
}
