package com.dmissoh.biologic.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.dmissoh.biologic.utils.TimeUtils;

public class StartSequenceDialog extends Dialog {

	private String title;
	private boolean isLive = true;
	private String description;
	private Text descriptionText;
	private Button modeButton;

	public StartSequenceDialog(Shell parentShell, String title) {
		super(parentShell);
		this.title = title;
		setShellStyle(getShellStyle() | SWT.RESIZE);
	}

	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		if (title != null) {
			shell.setText(title);
		}
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = new GridLayout(1, false);
		container.setLayout(gridLayout);

		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;

		Label descriptionLabel = new Label(container, SWT.NONE);
		descriptionLabel.setText("Give a short description for this sequence");

		descriptionText = new Text(container, SWT.BORDER);
		long now = System.currentTimeMillis();
		String date = TimeUtils.formatToDateTime(now);
		String initialInput = "SEQ " + date;
		descriptionText.setText(initialInput);
		descriptionText.setLayoutData(gridData);

		modeButton = new Button(container, SWT.CHECK);
		modeButton.setText("Live mode");
		modeButton.setSelection(true);

		return container;
	}

	@Override
	protected void okPressed() {
		setLive(this.modeButton.getSelection());
		setDescription(this.descriptionText.getText());
		super.okPressed();
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
