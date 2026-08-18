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
 * https://manual.calibre-ebook.com/generated/en/lrf2lrs.html
 * 
 * @author ： <a href="https://github.com/easy-4-java">hiwepy</a>
 */
public class DefaultLrf2lrsInvocationRequest extends AbstractInvocationRequest implements Lrf2lrsInvocationRequest {

	/**
	 * Indicates whether Do not save embedded image and font files to disk.
	 */
	private boolean dontOutputResources;
	/**
	 * LRF file. file.lrf
	 */
	private File lrfFile;
	/**
	 * Path to output file
	 */
	private File outputDirectory;

	public boolean isDontOutputResources() {
		return dontOutputResources;
	}

	public File getLrfFile() {
		return lrfFile;
	}

	public File getOutputDirectory() {
		return outputDirectory;
	}

	@Override
	public InvocationRequest setLrfFile(File lrfFile) {
		this.lrfFile = lrfFile;
		return this;
	}

	@Override
	public InvocationRequest setDontOutputResources(boolean dontOutputResources) {
		this.dontOutputResources = dontOutputResources;
		return this;
	}

	@Override
	public InvocationRequest setOutputDirectory(File outputDirectory) {
		this.outputDirectory = outputDirectory;
		return this;
	}
}
