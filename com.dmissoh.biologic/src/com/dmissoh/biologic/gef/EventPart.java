package com.dmissoh.biologic.gef;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import com.dmissoh.biologic.models.Event;

public class EventPart extends AbstractGraphicalEditPart {

	@Override
	protected IFigure createFigure() {
		Event model = (Event) getModel();
		IFigure figure = null;
		if (model.isPunctual()) {
			figure = new PunctualEventFigure(model.getFamily(), model.getBegin());
		} else {
			figure = new EventFigure(model.getFamily(), model.getBegin(), model.getEnd());
		}
		return figure;
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
		Event model = (Event) getModel();
		if (model.isPunctual()) {
			PunctualEventFigure figure = (PunctualEventFigure) getFigure();
			figure.setLayout(model.getLayout());
			figure.setBackgroundColor(model.getColor());
		} else {
			EventFigure figure = (EventFigure) getFigure();
			figure.setLayout(model.getLayout());
			figure.setBackgroundColor(model.getColor());
		}

	}

}
