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

import static com.badlogic.gdx.utils.SharedLibraryLoader.*;

import com.badlogic.gdx.utils.GdxNativesLoader;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.SharedLibraryLoader;

import java.io.File;
import java.lang.reflect.Method;

public final class Lwjgl3NativesLoader {
	static public boolean load = true;

	static {
		System.setProperty("org.lwjgl.input.Mouse.allowNegativeMouseCoords", "true");		

		// Don't extract natives if using JWS.
		try {
			Method method = Class.forName("javax.jnlp.ServiceManager").getDeclaredMethod("lookup", new Class[] {String.class});
			method.invoke(null, "javax.jnlp.PersistenceService");
			load = false;
		} catch (Throwable ex) {
			load = true;
		}
	}

	/** Extracts the LWJGL native libraries from the classpath and sets the "org.lwjgl.librarypath" system property. */
	static public void load () {
		GdxNativesLoader.load();
		if (GdxNativesLoader.disableNativesLoading) return;
		if (!load) return;

		SharedLibraryLoader loader = new SharedLibraryLoader();
		File nativesDir = null;
		try {
			if (isWindows) {
				File myFile = loader.extractFile(is64Bit ? "windows/x64/lwjgl.dll" : "windows/x86/lwjgl32.dll", null);
				nativesDir = myFile.getParentFile();
				loader.extractFile(is64Bit ? "windows/x64/glfw.dll" : "windows/x86/glfw32.dll", nativesDir.getName());
				if (!Lwjgl3ApplicationConfiguration.disableAudio)
					loader.extractFile(is64Bit ? "windows/x64/OpenAL.dll" : "windows/x86/OpenAL32.dll", nativesDir.getName());
			} else if (isMac) {
				nativesDir = loader.extractFile("macosx/x64/liblwjgl.dylib", null).getParentFile();
				loader.extractFile("windows/x64/libglfw.dylib", nativesDir.getName());
				if (!Lwjgl3ApplicationConfiguration.disableAudio)
					loader.extractFile("macosx/x64/libopenal.dylib", nativesDir.getName());
				
			} else if (isLinux) {
				nativesDir = loader.extractFile(is64Bit ? "linux/x64/liblwjgl.so" : "linux/x86/liblwjgl32.so", null).getParentFile();
				loader.extractFile(is64Bit ? "windows/x64/libglfw.so" : "windows/x86/libglfw32.so", nativesDir.getName());
				if (!Lwjgl3ApplicationConfiguration.disableAudio)
					loader.extractFile(is64Bit ? "linux/x64/libopenal.so" : "linux/x86/libopenal32.so", nativesDir.getName());
			}
		} catch (Throwable ex) {
			throw new GdxRuntimeException("Unable to extract LWJGL3 natives.", ex);
		}
		System.setProperty("org.lwjgl.librarypath", nativesDir.getAbsolutePath());
		load = false;
	}
}
