/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.backends.lwjgl3;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;

/** @author Nathan Sweet */
public class Lwjgl3ApplicationConfiguration {
	/** If true, OpenAL will not be used. This means {@link Application#getAudio()} returns null and the gdx-openal.jar and OpenAL
	 * natives are not needed. */
	static public boolean disableAudio;

	/** whether to attempt use OpenGL ES 3.0. **/
	public boolean useGL30 = false;

	/** number of bits per color channel **/
	public int r = 8, g = 8, b = 8, a = 8;
	/** number of bits for depth and stencil buffer **/
	public int depth = 16, stencil = 0;
	/** number of samples for MSAA **/
	public int samples = 0;
	/** width & height of application window **/
	public int width = 640, height = 480;
	/** x & y of application window, -1 for center **/
	public int x = -1, y = -1;
	/** fullscreen **/
	public boolean fullscreen = false;
	/** used to emulate screen densities **/
	public int overrideDensity = -1;
	/** whether to enable vsync, can be changed at runtime via {@link Graphics#setVSync(boolean)} **/
	public boolean vSyncEnabled = false;
	/** title of application **/
	public String title;
	/** whether the window is resizable **/
	public boolean resizable = true;
	/** the maximum number of sources that can be played simultaneously */
	public int audioDeviceSimultaneousSources = 16;
	/** the audio device buffer size in samples **/
	public int audioDeviceBufferSize = 512;
	/** the audio device buffer count **/
	public int audioDeviceBufferCount = 9;
	public Color initialBackgroundColor = Color.BLACK;
	/** Target framerate when the window is in the foreground. The CPU sleeps as needed. Use 0 to never sleep. Default: 60 **/
	public int foregroundFPS = 60;
	/** Target framerate when the window is not in the foreground. The CPU sleeps as needed. Use 0 to never sleep, -1 to not render.
	 * Default: 60 **/
	public int backgroundFPS = 60;
	/** Target framerate when the window is hidden or minimized. The CPU sleeps as needed. Use 0 to never sleep, -1 to not render.
	 * Default: 60 **/
	public int hiddenFPS = 60;
	/** Preferences file type on the desktop. Default is FileType.External */
	public Files.FileType preferencesFileType = FileType.External;
	/** Preferences directory on the desktop. Default is ".prefs/". */
	public String preferencesDirectory = ".prefs/";

	public boolean decorated = true;


	/** Sets the r, g, b and a bits per channel based on the given {@link DisplayMode} and sets the fullscreen flag to true.
	 * @param mode */
	public void setFromDisplayMode (DisplayMode mode) {
		this.width = mode.width;
		this.height = mode.height;
		if (mode.bitsPerPixel == 16) {
			this.r = 5;
			this.g = 6;
			this.b = 5;
			this.a = 0;
		}
		if (mode.bitsPerPixel == 24) {
			this.r = 8;
			this.g = 8;
			this.b = 8;
			this.a = 0;
		}
		if (mode.bitsPerPixel == 32) {
			this.r = 8;
			this.g = 8;
			this.b = 8;
			this.a = 8;
		}
		this.fullscreen = true;
	}

	protected static class Lwjgl3ApplicationConfigurationDisplayMode extends DisplayMode {
		protected Lwjgl3ApplicationConfigurationDisplayMode (int width, int height, int refreshRate, int bitsPerPixel) {
			super(width, height, refreshRate, bitsPerPixel);
		}
	}

	public static DisplayMode getDesktopDisplayMode () {
		GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = genv.getDefaultScreenDevice();
		java.awt.DisplayMode mode = device.getDisplayMode();
		return new Lwjgl3ApplicationConfigurationDisplayMode(mode.getWidth(), mode.getHeight(), mode.getRefreshRate(),
			mode.getBitDepth());
	}

	public static DisplayMode[] getDisplayModes () {
		GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = genv.getDefaultScreenDevice();
		java.awt.DisplayMode desktopMode = device.getDisplayMode();
		java.awt.DisplayMode[] displayModes = device.getDisplayModes();
		ArrayList<DisplayMode> modes = new ArrayList<DisplayMode>();
		int idx = 0;
		for (java.awt.DisplayMode mode : displayModes) {
			boolean duplicate = false;
			for (int i = 0; i < modes.size(); i++) {
				if (modes.get(i).width == mode.getWidth() && modes.get(i).height == mode.getHeight()
					&& modes.get(i).bitsPerPixel == mode.getBitDepth()) {
					duplicate = true;
					break;
				}
			}
			if (duplicate) continue;
			if (mode.getBitDepth() != desktopMode.getBitDepth()) continue;
			modes.add(new Lwjgl3ApplicationConfigurationDisplayMode(mode.getWidth(), mode.getHeight(), mode.getRefreshRate(), mode
				.getBitDepth()));
		}

		return modes.toArray(new DisplayMode[modes.size()]);
	}
}
