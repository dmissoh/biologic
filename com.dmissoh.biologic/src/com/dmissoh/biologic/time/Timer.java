package com.dmissoh.biologic.time;

import java.text.DateFormat;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class Timer implements Runnable {

	boolean keepRunning = true;

	private Shell shell;
	private Label timeLabel;
	private Label timeElapsedLabel;
	private boolean isLive;

	private Stopwatch stopwatch;

	DateFormat format = DateFormat.getTimeInstance(java.text.DateFormat.MEDIUM);

	public Timer(Label timeLabel, Label timeElapsedLabel, Shell shell,
			boolean isLive) {
		this.shell = shell;
		this.isLive = isLive;
		this.timeLabel = timeLabel;
		this.stopwatch = new Stopwatch();
		this.timeElapsedLabel = timeElapsedLabel;
	}

	public void run() {
		stopwatch.start();
		Display display = shell.getDisplay();
		while (!shell.isDisposed() && keepRunning) {
			try {
				display.asyncExec(new Runnable() {
					public void run() {
						if(isLive()){
							String time = format.format(stopwatch.getStartTime()
									+ stopwatch.elapsed());
							timeLabel.setText(time);
						}
						timeElapsedLabel.setText(((stopwatch.elapsed()) / 1000)
								+ " s");
						if (stopwatch.isPaused()) {
							timeElapsedLabel.setText(timeElapsedLabel.getText()
									+ " (paused)");
						}
					}
				});
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// Ask the thread to stop running
	public void stop() {
		stopwatch.stop();
		keepRunning = false;
	}

	public void pause() {
		stopwatch.pause();
	}

	public void resume() {
		stopwatch.resume();
	}

	public Stopwatch getStopwatch() {
		return stopwatch;
	}

	public void setStopwatch(Stopwatch stopwatch) {
		this.stopwatch = stopwatch;
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}
}