package com.dmissoh.biologic.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class EventDialog extends Dialog {

	private Label eventNameLabel;
	private Label eventStartKeyLabel;
	private Label eventEndKeyLabel;
	private Label eventPunctualLabel;

	private Text eventNameText;
	private Text eventStartKeyText;
	private Text eventEndKeyText;
	private Button eventPunctualButton;

	private String title;

	private String eventName;
	private String startEventKey;
	private String endEventKey;
	private boolean isPunctual;

	public EventDialog(Shell parentShell, String title) {
		super(parentShell);
		this.title = title;
		setShellStyle(getShellStyle() | SWT.RESIZE);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets
	 * .Shell)
	 */
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		if (title != null) {
			shell.setText(title);
		}
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(2, false));

		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;

		eventNameLabel = new Label(container, SWT.NONE);
		eventNameLabel.setLayoutData(new GridData(
				GridData.HORIZONTAL_ALIGN_BEGINNING));
		eventNameLabel.setText("Event Name:");
		eventNameText = new Text(container, SWT.BORDER);
		eventNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		eventNameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateEnablement();
			}
		});

		eventStartKeyLabel = new Label(container, SWT.NONE);
		eventStartKeyLabel.setLayoutData(new GridData(
				GridData.HORIZONTAL_ALIGN_BEGINNING));
		eventStartKeyLabel.setText("Start Key:");
		eventStartKeyText = new Text(container, SWT.BORDER);
		eventStartKeyText.setTextLimit(1);
		eventStartKeyText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		eventStartKeyText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateEnablement();
			}
		});

		eventEndKeyLabel = new Label(container, SWT.NONE);
		eventEndKeyLabel.setLayoutData(new GridData(
				GridData.HORIZONTAL_ALIGN_BEGINNING));
		eventEndKeyLabel.setText("End Key:");
		eventEndKeyText = new Text(container, SWT.BORDER);
		eventEndKeyText.setTextLimit(1);
		eventEndKeyText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		eventEndKeyText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateEnablement();
			}
		});

		eventPunctualLabel = new Label(container, SWT.NONE);
		eventPunctualLabel.setText("Punctual:");
		eventPunctualButton = new Button(container, SWT.CHECK);
		eventPunctualButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateEnablement();
			}
		});
		initialize();
		return container;
	}

	private void initialize() {
		eventNameText.setText("Sample");
		eventStartKeyText.setText("a");
		eventEndKeyText.setText("s");
	}

	private void updateEnablement() {
		eventEndKeyText.setEnabled(!eventPunctualButton.getSelection());
		boolean isFine = false;
		isFine = (eventNameText.getText().length() > 0)
				&& (((eventStartKeyText.getText().length() == 1) && (eventEndKeyText
						.getText().length() == 1)) || (eventPunctualButton
						.getSelection() && eventStartKeyText.getText().length() == 1));
		if (getButton(Dialog.OK) != null) {
			getButton(Dialog.OK).setEnabled(isFine);
		}
	}

	@Override
	protected void okPressed() {
		setEventName(eventNameText.getText());
		setStartEventKey(eventStartKeyText.getText());
		setEndEventKey(eventEndKeyText.getText());
		setPunctual(eventPunctualButton.getSelection());
		super.okPressed();
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getStartEventKey() {
		return startEventKey;
	}

	public void setStartEventKey(String startEventKey) {
		this.startEventKey = startEventKey;
	}

	public String getEndEventKey() {
		return endEventKey;
	}

	public void setEndEventKey(String endEventKey) {
		this.endEventKey = endEventKey;
	}

	public boolean isPunctual() {
		return isPunctual;
	}

	public void setPunctual(boolean isPunctual) {
		this.isPunctual = isPunctual;
	}

}
