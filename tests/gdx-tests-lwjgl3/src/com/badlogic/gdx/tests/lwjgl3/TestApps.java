package com.badlogic.gdx.tests.lwjgl3;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowController;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.tests.AnimationTest;
import com.badlogic.gdx.tests.UITest;
import com.badlogic.gdx.tests.g3d.LightsTest;

/**
 * Simple test example. using multiWindow
 * 
 * @author Natan Guilherme
 */
public class TestApps implements ApplicationListener, InputProcessor
{
	static int nextID = 1;

	public static Texture texture; // single static texture to share context

	Batch batch;

	float r = MathUtils.random();
	float g = MathUtils.random();
	float b = MathUtils.random();

	FPSLogger logger = new FPSLogger();

	int id;

	Lwjgl3WindowController controller;

	public TestApps()
	{

	}

	public TestApps(Lwjgl3WindowController controller)
	{
		this.controller = controller;
	}

	public void create()
	{
		System.out.println(Gdx.app.getApplicationListener().toString() + " Create");// checking if globals are equal

		Gdx.input.setInputProcessor(this);

		batch = new SpriteBatch();

		if (texture == null)
		{
			texture = new Texture(Gdx.files.internal("data/stone2.png"));
		}
	}

	public void resize(int width, int height)
	{
		System.out.println(Gdx.app.getApplicationListener().toString() + " Resize: " + width + "," + height);
	}

	public void render()
	{
		Gdx.gl.glClearColor(r, g, b, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		if (texture != null)
		{
			batch.draw(texture, 100, 100);
		}

		batch.end();
	}

	public void pause()
	{
		System.out.println("Paused");
	}

	public void resume()
	{
		System.out.println("Resumed");
	}

	public void dispose()
	{
		System.out.println("Dispose");
	}

	public boolean keyDown(int keycode)
	{
		System.out.println(Gdx.app.getApplicationListener().toString() + " Keycode: " + keycode);// checking if globals are equal
		if (keycode == Keys.C && controller != null)
		{

			Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
			config.width = 640;
			config.height = 480;
			TestApps testApp = new TestApps(controller);
			testApp.id = nextID;
			nextID++;
			config.title = "ID: " + testApp.id;
			Lwjgl3Application app = new Lwjgl3Application(testApp, config, true);
			controller.addWindow("" + testApp.id, app);
		}
		else if (keycode == Keys.D)
		{
			Gdx.app.exit();
		}
		return false;
	}

	public boolean keyUp(int keycode)
	{
		return false;
	}

	public boolean keyTyped(char character)
	{

		System.out.println(Gdx.app.getApplicationListener().toString() + " KeyTyped: " + character);// checking if globals are equal

		return false;
	}

	@Override
	public String toString()
	{
		return "ID: " + id;
	}

	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		System.out.println(Gdx.app.getApplicationListener().toString() + "  touch down: " + screenX + ";" + screenY);

		return false;
	}

	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		System.out.println(Gdx.app.getApplicationListener().toString() + " TouchUp: " + screenX + "," + screenY);// checking if globals are equal
		return false;
	}

	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		System.out.println(Gdx.app.getApplicationListener().toString() + " touchDragged: " + screenX + "," + screenY);// checking if globals are equal
		return false;
	}

	public boolean mouseMoved(int screenX, int screenY)
	{
//		System.out.println(Gdx.app.getApplicationListener().toString() +  " MouseMoved: " + screenX + "," + screenY);// checking if globals are equal
		return false;
	}

	public boolean scrolled(int amount)
	{
		System.out.println(Gdx.app.getApplicationListener().toString() + " Scrolled: " + amount);// checking if globals are equal
		return false;
	}

	public static void singleWindow()
	{
		ApplicationListener listener = null;
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.width = 640;
		config.height = 480;
		config.title = "ID 0";

		listener = new UITest();
		config.title = "UITest";
		new Lwjgl3Application(listener, config, false);
	}

	/**
	 * Window Controller is experimental, sometimes creating multiple windows in
	 * single call too fast will crash.
	 */
	public static void multiWindow()
	{
		Lwjgl3WindowController controller = new Lwjgl3WindowController(true);
		Lwjgl3Application app = null;
		ApplicationListener listener = null;
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.width = 640;
		config.height = 480;
		config.title = "ID 0";
		config.vSyncEnabled = false;

		/// On TestApp window
		//KEY: "C" Create a new Window
		//KEY: "D" Delete the current window
		//*******
		listener = new TestApps(controller); // example of sharing context
		config.title = "TestApp";
		app = new Lwjgl3Application(listener, config, true);
		controller.addWindow("0", app);

		listener = new UITest();
		config.title = "UITest";
		app = new Lwjgl3Application(listener, config, true);
		controller.addWindow("1", app);

		listener = new AnimationTest();
		config.title = "AnimationTest";
		app = new Lwjgl3Application(listener, config, true);
		controller.addWindow("2", app);

		listener = new LightsTest();
		config.title = "LightsTest";
		app = new Lwjgl3Application(listener, config, true);
		controller.addWindow("3", app);

		nextID += 3;
		controller.start();
	}

	public static void main(String[] args)
	{
		boolean multiWindow = true;
		if (multiWindow)
			multiWindow();
		else
			singleWindow();
	}
}
