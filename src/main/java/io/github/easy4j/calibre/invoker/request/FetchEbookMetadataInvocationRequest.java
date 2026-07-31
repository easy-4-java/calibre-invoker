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
package io.github.easy4j.calibre.invoker.request;

import java.io.File;

public interface FetchEbookMetadataInvocationRequest extends InvocationRequest {

	File getCoverFile();

	public String getAllowedPlugin();

	boolean isAuthors();

	boolean isIsbn();

	boolean isOpf();

	boolean isTitle();

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
