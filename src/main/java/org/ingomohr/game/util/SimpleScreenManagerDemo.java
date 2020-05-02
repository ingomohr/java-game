package org.ingomohr.game.util;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JFrame;

public class SimpleScreenManagerDemo extends JFrame {

	private static final long DEMO_TIME = 5_000;

	public static void main(String[] args) {

		boolean fetchedParameters = false;
		int width = 0;
		int height = 0;
		int depth = 0;

		if (args.length == 3) {
			try {
				width = Integer.parseInt(args[0]);
				height = Integer.parseInt(args[1]);
				depth = Integer.parseInt(args[2]);
				fetchedParameters = true;
			} catch (NumberFormatException ex) {
			}
		}

		if (fetchedParameters) {
			new SimpleScreenManagerDemo().run(width, height, depth);
		} else {
			printUsage();
			System.out.println("\nUsing default settings: 800x600x32");
			new SimpleScreenManagerDemo().run(800, 600, 32);
		}

	}

	private static void printUsage() {
		System.out.println("Usage: SimpleScreenManagerDemo width height depth");
		System.out.println("  width - integer: width of the resolution");
		System.out.println("  height - integer: height of the resolution");
		System.out.println("  depth - integer: bit-depth of the resolution");
	}

	private void run(int width, int height, int depth) {

		DisplayMode mode = new DisplayMode(width, height, depth, DisplayMode.REFRESH_RATE_UNKNOWN);

		setBackground(Color.BLUE);
		setForeground(Color.WHITE);
		setFont(new Font("Dialog", Font.PLAIN, 24));

		SimpleScreenManager screen = new SimpleScreenManager();
		try {
			screen.setFullScreenMode(mode, this);
			try {
				Thread.sleep(DEMO_TIME);
			} catch (InterruptedException e) {
			}
		} catch (Throwable ex) {
			ex.printStackTrace();
		} finally {
			try {
				screen.disposeFullScreenWindow();
			} catch (Throwable e) {
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		g.drawString("Hello World", 20, 50);
	}

}
