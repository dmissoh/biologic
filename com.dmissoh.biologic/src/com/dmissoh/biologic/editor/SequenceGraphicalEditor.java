package com.dmissoh.biologic.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.ui.IEditorInput;

import com.dmissoh.biologic.gef.GraphEditPartFactory;

public class SequenceGraphicalEditor extends GraphicalEditor {
	


	public SequenceGraphicalEditor() {
		setEditDomain(new DefaultEditDomain(this));
	}

	
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#configureGraphicalViewer()
	 */
	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setEditPartFactory(new GraphEditPartFactory());
	}



	@Override
	protected void initializeGraphicalViewer() {
		GraphicalViewer viewer = getGraphicalViewer();
		IEditorInput input = getEditorInput();
		if (input instanceof SequenceEditorInput) {
			SequenceEditorInput sInput = (SequenceEditorInput) input;
			viewer.setContents(sInput.getSequence());
		}
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		
	}
}