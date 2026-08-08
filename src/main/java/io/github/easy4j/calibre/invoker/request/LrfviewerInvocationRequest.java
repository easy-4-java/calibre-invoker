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

import java.io.File;

/**
 * Specifies the parameters used to control a Calibre {@code lrfviewer} invocation. Views
 * LRF e-books with configurable rendering options including hyphenation, profiling,
 * visual debugging, and background color.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 * @see InvocationRequest
 * @see io.github.easy4j.calibre.invoker.command.LrfviewerCommandLineBuilder
 */
public interface LrfviewerInvocationRequest extends InvocationRequest {
	
	/**
	 * Returns whether hyphenation should be disabled for faster rendering.
	 *
	 * @return {@code true} if hyphenation is disabled, {@code false} otherwise.
	 */
	public boolean isDisableHyphenation();

	/**
	 * Returns whether the LRF renderer should be profiled.
	 *
	 * @return {@code true} if profiling is enabled, {@code false} otherwise.
	 */
	public boolean isProfile();

	/**
	 * Returns the LRS file to view.
	 *
	 * @return The LRS file path.
	 */
	public File getLrsFile();

	/**
	 * Returns whether visual debugging aids for the rendering engine are enabled.
	 *
	 * @return {@code true} if visual debug is enabled, {@code false} otherwise.
	 */
	public boolean isVisualDebug();

	/**
	 * Returns whether the background should be pure white instead of off-white.
	 *
	 * @return {@code true} if white background is enabled, {@code false} otherwise.
	 */
	public boolean isWhiteBackground();
	
	/**
	 * Set the value of the {@code disable-hyphenation} {@code true} if the
	 * argument {@code --disable-hyphenation} was specified, otherwise {@code false}
	 */
	InvocationRequest setDisableHyphenation(boolean disableHyphenation);
	
	/**
	 * Set the value of the {@code profile} {@code true} if the
	 * argument {@code --profile} was specified, otherwise {@code false}
	 */
	InvocationRequest setProfile(boolean profile);
	
	/**
	 * Set the value of the {@code visual-debug} {@code true} if the
	 * argument {@code --visual-debug} was specified, otherwise {@code false}
	 */
	InvocationRequest setVisualDebug(boolean visualDebug);

	/**
	 * Set the value of the {@code white-background} {@code true} if the
	 * argument {@code --white-background} was specified, otherwise {@code false}
	 */
	InvocationRequest setWhiteBackground(boolean whiteBackground);
	 
	/**
	 * LRS file path. file.lrs
	 */
	InvocationRequest setLrsFile(File lrsFile);
	 
}
