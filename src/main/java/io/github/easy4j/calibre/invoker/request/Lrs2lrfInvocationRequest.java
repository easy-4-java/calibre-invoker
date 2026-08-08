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
 * Specifies the parameters used to control a Calibre {@code lrs2lrf} invocation. Compiles
 * an LRS file into an LRF file with optional output directory configuration.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 * @see InvocationRequest
 * @see io.github.easy4j.calibre.invoker.command.Lrs2lrfCommandLineBuilder
 */
public interface Lrs2lrfInvocationRequest extends InvocationRequest {

	/**
	 * Returns whether LRS-to-LRS conversion mode is enabled (useful for debugging).
	 *
	 * @return {@code true} if LRS mode is enabled, {@code false} otherwise.
	 */
	public boolean isLrs();

	/**
	 * Returns the LRS input file to compile.
	 *
	 * @return The LRS file path.
	 */
	public File getLrsFile();

	/**
	 * Returns the output directory for the compiled LRF file.
	 *
	 * @return The output directory, or {@code null} for the default location.
	 */
	public File getOutputDirectory();

	/**
	 * Set the value of the {@code lrs} {@code true} if the
	 * argument {@code --lrs} was specified, otherwise
	 * {@code false}
	 */
	InvocationRequest setLrs(boolean lrs);
	
	/**
	 * Set the value of the {@code output} {@code true} if the argument
	 * {@code --output} was specified, otherwise {@code false}
	 */
	InvocationRequest setOutputDirectory(File output);

	/**
	 * LRS file path. file.lrs
	 */
	InvocationRequest setLrsFile(File lrsFile);
	
}
