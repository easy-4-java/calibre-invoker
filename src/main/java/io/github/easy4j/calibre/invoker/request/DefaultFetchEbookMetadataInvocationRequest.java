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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

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
	private final List<String> allowedPlugins = new ArrayList<>();

	/**
	 * Book author(s)
	 */
	private String authors;
	private boolean legacyAuthorsRequested;
	/**
	 * Specify a filename. The cover, if available, will be saved to it. Without
	 * this option, no cover will be downloaded.
	 */
	private File coverFile;
	/**
	 * Book ISBN
	 */
	private String isbn;
	private boolean legacyIsbnRequested;
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
	private String title;
	private boolean legacyTitleRequested;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAllowedPlugin() {
		return allowedPlugins.isEmpty() ? null : allowedPlugins.get(0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InvocationRequest setAllowedPlugin(String allowedPlugin) {
		allowedPlugins.clear();
		addAllowedPlugin(allowedPlugin);
		return this;
	}

	@Override
	public List<String> getAllowedPlugins() {
		return Collections.unmodifiableList(new ArrayList<>(allowedPlugins));
	}

	@Override
	public InvocationRequest setAllowedPlugins(List<String> allowedPlugins) {
		this.allowedPlugins.clear();
		if (Objects.nonNull(allowedPlugins)) {
			for (String allowedPlugin : allowedPlugins) {
				addAllowedPlugin(allowedPlugin);
			}
		}
		return this;
	}

	@Override
	public InvocationRequest addAllowedPlugin(String allowedPlugin) {
		if (StringUtils.isNotBlank(allowedPlugin)) {
			allowedPlugins.add(allowedPlugin);
		}
		return this;
	}

	@Override
	public String getAuthors() {
		return authors;
	}

	@Override
	public InvocationRequest setAuthors(String authors) {
		this.authors = authors;
		this.legacyAuthorsRequested = false;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Deprecated
	@Override
	public boolean isAuthors() {
		return legacyAuthorsRequested || StringUtils.isNotBlank(authors);
	}

	/**
	 * {@inheritDoc}
	 */
	@Deprecated
	@Override
	public InvocationRequest setAuthors(boolean authors) {
		this.legacyAuthorsRequested = authors;
		if (!authors) {
			this.authors = null;
		}
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
	public String getIsbn() {
		return isbn;
	}

	@Override
	public InvocationRequest setIsbn(String isbn) {
		this.isbn = isbn;
		this.legacyIsbnRequested = false;
		return this;
	}

	@Deprecated
	@Override
	public boolean isIsbn() {
		return legacyIsbnRequested || StringUtils.isNotBlank(isbn);
	}

	/**
	 * {@inheritDoc}
	 */
	@Deprecated
	@Override
	public InvocationRequest setIsbn(boolean isbn) {
		this.legacyIsbnRequested = isbn;
		if (!isbn) {
			this.isbn = null;
		}
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
	public String getTitle() {
		return title;
	}

	@Override
	public InvocationRequest setTitle(String title) {
		this.title = title;
		this.legacyTitleRequested = false;
		return this;
	}

	@Deprecated
	@Override
	public boolean isTitle() {
		return legacyTitleRequested || StringUtils.isNotBlank(title);
	}

	/**
	 * {@inheritDoc}
	 */
	@Deprecated
	@Override
	public InvocationRequest setTitle(boolean title) {
		this.legacyTitleRequested = title;
		if (!title) {
			this.title = null;
		}
		return this;
	}

}
