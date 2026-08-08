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
 * https://manual.calibre-ebook.com/generated/en/lrs2lrf.html
 * 
 * @author [@Loong Wan](https://github.com/loong10k)
 */
public class DefaultLrs2lrfInvocationRequest extends AbstractInvocationRequest implements Lrs2lrfInvocationRequest {

	/**
	 * Convert LRS to LRS, useful for debugging.
	 */
	private boolean lrs;
	/**
	 * LRS file. file.lrs
	 */
	private File lrsFile;
	/**
	 * Path to output file
	 */
	private File outputDirectory;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLrs() {
		return lrs;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File getLrsFile() {
		return lrsFile;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File getOutputDirectory() {
		return outputDirectory;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InvocationRequest setLrs(boolean lrs) {
		this.lrs = lrs;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InvocationRequest setOutputDirectory(File outputDirectory) {
		this.outputDirectory = outputDirectory;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InvocationRequest setLrsFile(File lrs) {
		this.lrsFile = lrs;
		return this;
	}

}
