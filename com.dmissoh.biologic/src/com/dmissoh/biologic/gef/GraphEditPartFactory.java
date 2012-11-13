package com.dmissoh.biologic.gef;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import com.dmissoh.biologic.models.Event;
import com.dmissoh.biologic.models.Sequence;

public class GraphEditPartFactory implements EditPartFactory {

	public EditPart createEditPart(EditPart context, Object model) {
		AbstractGraphicalEditPart part = null;
		if(model instanceof Sequence){
			part = new GraphPart();
		}else if(model instanceof Event){
			part = new EventPart();
		}
		part.setModel(model);
		return part;
	}

}
