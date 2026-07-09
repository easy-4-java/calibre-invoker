/**
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
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
package io.github.hiwepy.calibre.invoker.request;

/**
 * View an e-book.
 * https://manual.calibre-ebook.com/generated/en/ebook-viewer.html
 * @author ： <a href="https://github.com/hiwepy">hiwepy</a>
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

	public boolean isContinueReading() {
		return continueReading;
	}

	public void setContinueReading(boolean continueReading) {
		this.continueReading = continueReading;
	}

	public boolean isDebugJavascript() {
		return debugJavascript;
	}

	public void setDebugJavascript(boolean debugJavascript) {
		this.debugJavascript = debugJavascript;
	}

	public boolean isFullscreen() {
		return fullscreen;
	}

	public void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
	}

	public boolean isRaiseWindow() {
		return raiseWindow;
	}

	public void setRaiseWindow(boolean raiseWindow) {
		this.raiseWindow = raiseWindow;
	}
	
}
