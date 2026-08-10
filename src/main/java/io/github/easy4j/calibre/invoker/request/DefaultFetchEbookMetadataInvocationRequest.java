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
 * Fetch book metadata from online sources. You must specify at least one of
 * title, authors or ISBN.
 * https://manual.calibre-ebook.com/generated/en/fetch-ebook-metadata.html
 * 
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
public class DefaultFetchEbookMetadataInvocationRequest extends AbstractInvocationRequest
		implements FetchEbookMetadataInvocationRequest {

	/**
	 * Specify the name of a metadata download plugin to use. By default, all
	 * metadata plugins will be used. Can be specified multiple times for multiple
	 * plugins. All plugin names: Google, Google Images, Amazon.com, Edelweiss, Open
	 * Library, Overdrive, Douban Books, OZON.ru, Big Book Search.
	 */
	private String allowedPlugin;

	/**
	 * Book author(s)
	 */
	private boolean authors;
	/**
	 * Specify a filename. The cover, if available, will be saved to it. Without
	 * this option, no cover will be downloaded.
	 */
	private File coverFile;
	/**
	 * Book ISBN
	 */
	private boolean isbn;
	/**
	 * Output the metadata in OPF format instead of human readable text.
	 */
	private boolean opf;
	/**
	 * Timeout in seconds. Default is 30s
	 */
	private long timeout = 30;
	/**
	 * Book title
	 */
	private boolean title;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAllowedPlugin() {
		return allowedPlugin;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InvocationRequest setAllowedPlugin(String allowedPlugin) {
		this.allowedPlugin = allowedPlugin;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAuthors() {
		return authors;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InvocationRequest setAuthors(boolean authors) {
		this.authors = authors;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File getCoverFile() {
		return coverFile;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InvocationRequest setCoverFile(File coverFile) {
		this.coverFile = coverFile;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isIsbn() {
		return isbn;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InvocationRequest setIsbn(boolean isbn) {
		this.isbn = isbn;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isOpf() {
		return opf;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InvocationRequest setOpf(boolean opf) {
		this.opf = opf;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getTimeout() {
		return timeout;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InvocationRequest setTimeout(long timeout) {
		this.timeout = timeout;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTitle() {
		return title;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InvocationRequest setTitle(boolean title) {
		this.title = title;
		return this;
	}

}
