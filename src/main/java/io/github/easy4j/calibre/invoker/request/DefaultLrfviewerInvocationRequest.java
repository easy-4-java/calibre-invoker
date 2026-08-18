/**
 * Copyright (c) 2018, hiwepy (https://github.com/easy-4-java).
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

import java.io.File;

/**
 * https://manual.calibre-ebook.com/generated/en/lrfviewer.html
 * @author ： <a href="https://github.com/easy-4-java">hiwepy</a>
 */
public class DefaultLrfviewerInvocationRequest extends AbstractInvocationRequest implements LrfviewerInvocationRequest {

	/**
	 * Disable hyphenation. Should significantly speed up rendering.
	 */
	private boolean disableHyphenation;

	/**
	 * Profile the LRF renderer
	 */
	private boolean profile;
	
	/**
	 * LRS file. file.lrs
	 */
	private File lrsFile;
	
	/**
	 * Turn on visual aids to debugging the rendering engine
	 */
	private boolean visualDebug;

	/**
	 * By default the background is off white as I find this easier on the eyes. Use
	 * this option to make the background pure white.
	 */
	private boolean whiteBackground;

	@Override
	public boolean isDisableHyphenation() {
		return disableHyphenation;
	}

	@Override
	public boolean isProfile() {
		return profile;
	}

	@Override
	public File getLrsFile() {
		return lrsFile;
	}

	@Override
	public boolean isVisualDebug() {
		return visualDebug;
	}

	@Override
	public boolean isWhiteBackground() {
		return whiteBackground;
	}

	@Override
	public InvocationRequest setDisableHyphenation(boolean disableHyphenation) {
		this.disableHyphenation = disableHyphenation;
		return this;
	}

	@Override
	public InvocationRequest setProfile(boolean profile) {
		this.profile = profile;
		return this;
	}

	@Override
	public InvocationRequest setLrsFile(File lrsFile) {
		this.lrsFile = lrsFile;
		return this;
	}
	
	@Override
	public InvocationRequest setVisualDebug(boolean visualDebug) {
		this.visualDebug = visualDebug;
		return this;
	}

	@Override
	public InvocationRequest setWhiteBackground(boolean whiteBackground) {
		this.whiteBackground = whiteBackground;
		return this;
	}

}
