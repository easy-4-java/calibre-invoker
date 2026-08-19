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
 * Specifies the parameters used to control a Calibre {@code fetch-ebook-metadata} invocation.
 * Fetches book metadata from online sources using at least one of title, authors, or ISBN.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see InvocationRequest
 * @see io.github.easy4j.calibre.invoker.command.FetchEbookMetadataCommandLineBuilder
 */
public interface FetchEbookMetadataInvocationRequest extends InvocationRequest {

	/**
	 * Returns the cover file path where the downloaded cover will be saved.
	 *
	 * @return The cover file, or {@code null} if not set.
	 */
	File getCoverFile();

	/**
	 * Returns the name of the metadata download plugin to use.
	 *
	 * @return The allowed plugin name, or {@code null} to use all plugins.
	 */
	public String getAllowedPlugin();

	/**
	 * Returns whether the authors parameter is specified.
	 *
	 * @return {@code true} if authors mode is enabled, {@code false} otherwise.
	 */
	boolean isAuthors();

	/**
	 * Returns whether the ISBN parameter is specified.
	 *
	 * @return {@code true} if ISBN mode is enabled, {@code false} otherwise.
	 */
	boolean isIsbn();

	/**
	 * Returns whether output should be in OPF format.
	 *
	 * @return {@code true} if OPF output is enabled, {@code false} otherwise.
	 */
	boolean isOpf();

	/**
	 * Returns whether the title parameter is specified.
	 *
	 * @return {@code true} if title mode is enabled, {@code false} otherwise.
	 */
	boolean isTitle();

	/**
	 * Returns the timeout in seconds for the metadata fetch operation.
	 *
	 * @return The timeout in seconds, default is 30.
	 */
	long getTimeout();

	/**
	 * Set the value of the {@code allowed-plugin} {@code true} if the argument
	 * {@code --allowed-plugin} was specified, otherwise {@code false}
	 */
	InvocationRequest setAllowedPlugin(String allowedPlugin);

	/**
	 * Set the value of the {@code --authors, -a} {@code true} if the argument
	 * {@code --authors, -a} was specified, otherwise {@code false}
	 */
	InvocationRequest setAuthors(boolean authors);

	/**
	 * Set the value of the {@code  --isbn, -i} {@code true} if the argument
	 * {@code  --isbn, -i} was specified, otherwise {@code false}
	 */
	InvocationRequest setIsbn(boolean isbn);

	/**
	 * Specify a filename. The cover, if available, will be saved to it. Without this option, no cover will be downloaded.
	 */
	InvocationRequest setCoverFile(File coverFile);

	/**
	 * Set the value of the {@code --opf, -o} {@code true} if the argument
	 * {@code --opf, -o} was specified, otherwise {@code false}
	 */
	InvocationRequest setOpf(boolean opf);

	/**
	 * Set the value of the {@code --timeout, -d} {@code true} if the argument
	 * {@code  --timeout, -d} was specified, otherwise {@code false}
	 */
	InvocationRequest setTimeout(long timeout);

	/**
	 * Set the value of the {@code  --title, -t} {@code true} if the argument
	 * {@code --title, -t} was specified, otherwise {@code false}
	 */
	InvocationRequest setTitle(boolean title);

}
