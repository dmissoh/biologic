package com.dmissoh.biologic.editor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.dmissoh.biologic.internal.PluginService;
import com.dmissoh.biologic.models.Sequence;

/**
 * The Class SequenceEditorInput.
 */
public class SequenceEditorInput implements IEditorInput {

	/** The sequence. */
	private Sequence sequence;

	/** The is read only. */
	private boolean isReadOnly;

	/**
	 * The Enum Purpose.
	 */
	enum Purpose {

		/** The TABULAR. */
		TABULAR,
		/** The GRAPHICAL. */
		GRAPHICAL
	};

	/** The purpose. */
	private Purpose purpose = Purpose.TABULAR;

	/**
	 * Instantiates a new sequence editor input.
	 *
	 * @param isLive
	 *            the is live
	 */
	private SequenceEditorInput(boolean isLive) {
		sequence = new Sequence(isLive);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof SequenceEditorInput)) {
			return false;
		}
		SequenceEditorInput input = (SequenceEditorInput) obj;
		return (input.getSequence().getStartTime() == getSequence()
				.getStartTime())
				&& (input.getPurpose() == getPurpose());
	}

	/**
	 * Instantiates a new sequence editor input.
	 *
	 * @param isReadOnly
	 *            the is read only
	 * @param isLive
	 *            the is live
	 */
	public SequenceEditorInput(boolean isReadOnly, boolean isLive) {
		this(isLive);
		this.isReadOnly = isReadOnly;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.IEditorInput#exists()
	 */
	public boolean exists() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.IEditorInput#getImageDescriptor()
	 */
	public ImageDescriptor getImageDescriptor() {
		if(getSequence().isLive()){
			return PluginService.getInstance().getImageDescriptor("icons/sequence_small.png");
		}else{
			return PluginService.getInstance().getImageDescriptor("icons/sequence_pause_small.gif");
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.IEditorInput#getName()
	 */
	public String getName() {
		return getTitle();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.IEditorInput#getPersistable()
	 */
	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.IEditorInput#getToolTipText()
	 */
	public String getToolTipText() {
		return getTitle();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Gets the sequence.
	 *
	 * @return the sequence
	 */
	public final Sequence getSequence() {
		return sequence;
	}

	/**
	 * Sets the sequence.
	 *
	 * @param sequence
	 *            the new sequence
	 */
	public final void setSequence(Sequence sequence) {
		this.sequence = sequence;
	}

	/**
	 * Checks if is read only.
	 *
	 * @return true, if is read only
	 */
	public final boolean isReadOnly() {
		return isReadOnly;
	}

	/**
	 * Sets the read only.
	 *
	 * @param isReadOnly
	 *            the new read only
	 */
	public final void setReadOnly(boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
	}

	/**
	 * Gets the purpose.
	 *
	 * @return the purpose
	 */
	public final Purpose getPurpose() {
		return purpose;
	}

	/**
	 * Sets the purpose.
	 *
	 * @param purpose
	 *            the new purpose
	 */
	public final void setPurpose(Purpose purpose) {
		this.purpose = purpose;
	}

	private String getTitle() {
		String title = "Sequence"
				+ ((getSequence().isLive()) ? " (live)" : " (recorded)");
		return title;
	}

}
