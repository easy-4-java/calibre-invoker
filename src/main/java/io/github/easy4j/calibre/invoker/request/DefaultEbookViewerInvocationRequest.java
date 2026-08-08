/**
 * Copyright (c) 2018, Loong Wan (https://github.com/loong10k).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package io.github.easy4j.calibre.invoker.request;

/**
 * View an e-book.
 * https://manual.calibre-ebook.com/generated/en/ebook-viewer.html
 * @author [@Loong Wan](https://github.com/loong10k)
 */
public class DefaultEbookViewerInvocationRequest extends AbstractInvocationRequest implements InvocationRequest  {

	/**
	 * Continue reading at the previously opened book. 
	 * Set the value of the {@code continue} {@code true} if the argument
	 * {@code --continue} was specified, otherwise {@code false}
	 */
	private boolean continueReading;
 
	/**
	 * Print javascript alert and console messages to the console. 
	 * Set the value of the {@code debug-javascript} {@code true} if the argument
	 * {@code --debug-javascript} was specified, otherwise {@code false}
	 */
	private boolean debugJavascript;
	
	/**
	 * If specified, viewer window will try to open full screen when started.
	 * Set the value of the {@code full-screen} {@code true} if the argument
	 * {@code --full-screen} was specified, otherwise {@code false}
	 */
	private boolean fullscreen;
	/**
	 * If specified, viewer window will try to come to the front when started.
	 * Set the value of the {@code raise-window} {@code true} if the argument
	 * {@code --raise-window} was specified, otherwise {@code false}
	 */
	private boolean raiseWindow;

	/**
	 * Returns whether the viewer should continue reading at the previously opened book.
	 *
	 * @return {@code true} if continue-reading is enabled, {@code false} otherwise.
	 */
	public boolean isContinueReading() {
		return continueReading;
	}

	/**
	 * Sets whether the viewer should continue reading at the previously opened book.
	 *
	 * @param continueReading {@code true} to continue reading, {@code false} otherwise.
	 */
	public void setContinueReading(boolean continueReading) {
		this.continueReading = continueReading;
	}

	/**
	 * Returns whether JavaScript alert and console messages should be printed to the console.
	 *
	 * @return {@code true} if JavaScript debugging is enabled, {@code false} otherwise.
	 */
	public boolean isDebugJavascript() {
		return debugJavascript;
	}

	/**
	 * Sets whether JavaScript alert and console messages should be printed to the console.
	 *
	 * @param debugJavascript {@code true} to enable JavaScript debugging, {@code false} otherwise.
	 */
	public void setDebugJavascript(boolean debugJavascript) {
		this.debugJavascript = debugJavascript;
	}

	/**
	 * Returns whether the viewer should attempt to open in full screen mode.
	 *
	 * @return {@code true} if full screen is enabled, {@code false} otherwise.
	 */
	public boolean isFullscreen() {
		return fullscreen;
	}

	/**
	 * Sets whether the viewer should attempt to open in full screen mode.
	 *
	 * @param fullscreen {@code true} to enable full screen, {@code false} otherwise.
	 */
	public void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
	}

	/**
	 * Returns whether the viewer window should try to come to the front when started.
	 *
	 * @return {@code true} if raise-window is enabled, {@code false} otherwise.
	 */
	public boolean isRaiseWindow() {
		return raiseWindow;
	}

	/**
	 * Sets whether the viewer window should try to come to the front when started.
	 *
	 * @param raiseWindow {@code true} to raise the window, {@code false} otherwise.
	 */
	public void setRaiseWindow(boolean raiseWindow) {
		this.raiseWindow = raiseWindow;
	}
	
}
