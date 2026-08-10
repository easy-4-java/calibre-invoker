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
 * Specifies the parameters used to control a Calibre {@code web2disk} invocation. The web2disk
 * tool downloads web pages to disk with configurable options such as base directory, delay,
 * encoding, filtering, and recursion depth.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see InvocationRequest
 * @see io.github.easy4j.calibre.invoker.command.Web2diskCommandLineBuilder
 */
public interface Web2diskInvocationRequest extends InvocationRequest {

	/**
	 * Returns whether CSS stylesheets should not be downloaded.
	 *
	 * @return {@code true} if stylesheet download is disabled, {@code false} otherwise.
	 */
	public boolean isDontDownloadStylesheets();

	/**
	 * Returns the base directory into which the URL content is saved.
	 *
	 * @return The base directory, or {@code null} if using the default.
	 */
	public File getBaseDirectory();

	/**
	 * Returns the minimum interval in seconds between consecutive fetches.
	 *
	 * @return The delay in seconds, default is 0.
	 */
	public int getDelay();

	/**
	 * Returns the character encoding for the websites being downloaded.
	 *
	 * @return The encoding string, or {@code null} to auto-detect.
	 */
	public String getEncoding();

	/**
	 * Returns the regular expression used to filter (ignore) links.
	 *
	 * @return The filter regexp, or {@code null} if not set.
	 */
	public String getFilterRegexp();

	/**
	 * Returns the regular expression used to match (follow) links.
	 *
	 * @return The match regexp, or {@code null} if not set.
	 */
	public String getMatchRegexp();

	/**
	 * Returns the maximum number of files to download from anchor tags.
	 *
	 * @return The maximum number of files.
	 */
	public long getMaxFiles();

	/**
	 * Returns the maximum number of levels to recurse (depth of links to follow).
	 *
	 * @return The maximum recursion depth, default is 1.
	 */
	public int getMaxRecursions();

	/**
	 * Returns the timeout in seconds to wait for a response from the server.
	 *
	 * @return The timeout in seconds, default is 10.
	 */
	public long getTimeout();

	/**
	 * Returns the URL to download.
	 *
	 * @return The URL string, e.g. "https://example.com".
	 */
	public String getURL();

	/**
	 * Set the value of the {@code base-dir} {@code true} if the argument
	 * {@code --base-dir} was specified, otherwise {@code false}
	 */
	InvocationRequest setBaseDirectory(File baseDir);

	/**
	 * Set the value of the {@code delay} {@code true} if the argument
	 * {@code --delay} was specified, otherwise {@code false}
	 */
	InvocationRequest setDelay(int delay);

	/**
	 * Set the value of the {@code dont-download-stylesheets} {@code true} if the
	 * argument {@code --dont-download-stylesheets} was specified, otherwise
	 * {@code false}
	 */
	InvocationRequest setDontDownloadStylesheets(boolean dontDownloadStylesheets);

	/**
	 * Set the value of the {@code encoding} {@code true} if the argument
	 * {@code  --encoding} was specified, otherwise {@code false}
	 */
	InvocationRequest setEncoding(String encoding);

	/**
	 * Set the value of the {@code filter-regexp} {@code true} if the argument
	 * {@code --filter-regexp} was specified, otherwise {@code false}
	 */
	InvocationRequest setFilterRegexp(String filterRegexp);

	/**
	 * Set the value of the {@code match-regexp} {@code true} if the argument
	 * {@code --match-regexp} was specified, otherwise {@code false}
	 */
	InvocationRequest setMatchRegexp(String matchRegexp);

	/**
	 * Set the value of the {@code max-files} {@code true} if the argument
	 * {@code --max-files} was specified, otherwise {@code false}
	 */
	InvocationRequest setMaxFiles(long maxFiles);

	/**
	 * Set the value of the {@code max-recursions} {@code true} if the argument
	 * {@code --max-recursions} was specified, otherwise {@code false}
	 */
	InvocationRequest setMaxRecursions(int maxRecursions);

	/**
	 * Set the value of the {@code timeout} {@code true} if the argument
	 * {@code --timeout} was specified, otherwise {@code false}
	 */
	InvocationRequest setTimeout(long timeout);
	/**
	 * Where URL is for example https://google.com
	 */
	InvocationRequest setURL(String url);

}
