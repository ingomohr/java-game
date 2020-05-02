package org.ingomohr.game.util;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import java.util.Objects;
import java.util.stream.Stream;

import javax.swing.JFrame;

/**
 * Manages initializing full screen graphics modes.
 */
public class SimpleScreenManager {

	private final GraphicsDevice device;

	public SimpleScreenManager() {
		final GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		device = env.getDefaultScreenDevice();

		printAvailableDisplayModes();

	}

	private void printAvailableDisplayModes() {
		System.out.println("Available modes");
		final DisplayMode[] displayModes = device.getDisplayModes();
		Stream.of(displayModes).forEach(mode -> {
			System.out.println(modeToStr(mode));
		});

	}

	private String modeToStr(DisplayMode mode) {
		final int height = mode.getHeight();
		final int width = mode.getWidth();
		final int bitDepth = mode.getBitDepth();
		final int refreshRate = mode.getRefreshRate();

		return width + "x" + height + "x" + bitDepth + " at " + refreshRate + "Hz";
	}

	/**
	 * Enters full screen mode for the give window.
	 * 
	 * @param mode   the mode to set.
	 * @param window the window to set to full screen. Cannot be <code>null</code>.
	 */
	public void setFullScreenMode(DisplayMode mode, JFrame window) {
		Objects.requireNonNull(window);

		window.setUndecorated(true);
		window.setResizable(false);

		device.setFullScreenWindow(window);

		if (mode != null && device.isDisplayChangeSupported()) {
			device.setDisplayMode(mode);
		}

	}

	/**
	 * Disposes the current full screen window - if there is one.
	 */
	public void disposeFullScreenWindow() {
		final Window window = device.getFullScreenWindow();
		if (window != null) {
			window.dispose();
		}
		device.setFullScreenWindow(null);
	}

}
