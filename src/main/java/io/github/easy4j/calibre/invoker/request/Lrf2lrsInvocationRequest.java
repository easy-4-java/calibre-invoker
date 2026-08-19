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
 * Specifies the parameters used to control a Calibre {@code lrf2lrs} invocation. Converts
 * an LRF file into an LRS (XML UTF-8 encoded) file.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see InvocationRequest
 * @see io.github.easy4j.calibre.invoker.command.Lrf2lrsCommandLineBuilder
 */
public interface Lrf2lrsInvocationRequest extends InvocationRequest {

	/**
	 * Returns whether embedded image and font files should not be saved to disk.
	 *
	 * @return {@code true} if resource output is disabled, {@code false} otherwise.
	 */
	public boolean isDontOutputResources();

	/**
	 * Returns the LRF input file to convert.
	 *
	 * @return The LRF file path.
	 */
	public File getLrfFile();

	/**
	 * Returns the output directory for the converted LRS file.
	 *
	 * @return The output directory, or {@code null} for the default location.
	 */
	public File getOutputDirectory();
	
	/**
	 * Set the value of the {@code dont-output-resources} {@code true} if the
	 * argument {@code --dont-output-resources} was specified, otherwise {@code false}
	 */
	InvocationRequest setDontOutputResources(boolean dontOutputResources);
	
	/**
	 * Set the value of the {@code output} {@code true} if the argument
	 * {@code --output} was specified, otherwise {@code false}
	 */
	InvocationRequest setOutputDirectory(File output);

	/**
	 * LRF file path. file.lrf
	 */
	InvocationRequest setLrfFile(File lrfFile);
	
}
