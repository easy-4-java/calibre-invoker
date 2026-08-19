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

/**
 * <p>
 * Read/Write metadata from/to e-book files.
 * </p>
 * <p>
 * Supported formats for reading metadata: azw, azw1, azw3, azw4, cbr, cbz, chm,
 * docx, epub, fb2, fbz, html, htmlz, imp, lit, lrf, lrx, mobi, odt, oebzip,
 * opf, pdb, pdf, pml, pmlz, pobi, prc, rar, rb, rtf, snb, tpz, txt, txtz, updb,
 * zip
 * </p>
 * <p>
 * Supported formats for writing metadata: azw, azw1, azw3, azw4, docx, epub,
 * fb2, fbz, htmlz, lrf, mobi, pdb, pdf, prc, rtf, tpz, txtz
 * </p>
 * <p>
 * Different file types support different kinds of metadata. If you try to set
 * some metadata on a file type that does not support it, the metadata will be
 * silently ignored.
 * </p>
 * https://manual.calibre-ebook.com/generated/en/ebook-meta.html
 * 
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
public class DefaultEbookMetaInvocationRequest extends AbstractInvocationRequest implements InvocationRequest  {

	/**
	 * <p>String to be used when sorting by author. If unspecified, and the author(s) are specified, it will be auto-generated from the author(s).</p>
	 * Set the value of the {@code author-sort} {@code true} if the argument {@code --author-sort} was
	 * specified, otherwise {@code false}
	 */
	private boolean authorSort;

	/**
	 * <p>Read metadata from the specified OPF file and use it to set metadata in the e-book. 
	 * Metadata specified on the command line will override metadata read from the OPF file</p>
	 * Set the value of the {@code from-opf} {@code true} if the argument
	 * {@code --from-opf} was specified, otherwise {@code false}
	 */
	private boolean fromOpf;

	/**
	 * <p>Get the cover from the e-book and save it at as the specified file. </p>
	 * Set the value of the {@code get-cover} {@code true} if the argument
	 * {@code --get-cover} was specified, otherwise {@code false}
	 */
	private boolean getCover;
	/**
	 * <p>The version of the title to be used for sorting. If unspecified, and the title is specified, it will be auto-generated from the title.</p>
	 *  Set the value of the {@code title-sort} {@code true} if the argument
	 * {@code --title-sort} was specified, otherwise {@code false}
	 */
	private boolean titleSort;

	/**
	 * Returns whether the author-sort string should be auto-generated from the author(s).
	 *
	 * @return {@code true} if author-sort mode is enabled, {@code false} otherwise.
	 */
	public boolean isAuthorSort() {
		return authorSort;
	}

	/**
	 * Sets whether the author-sort string should be auto-generated from the author(s).
	 *
	 * @param authorSort {@code true} to enable author-sort mode, {@code false} otherwise.
	 */
	public void setAuthorSort(boolean authorSort) {
		this.authorSort = authorSort;
	}

	/**
	 * Returns whether metadata should be read from an OPF file.
	 *
	 * @return {@code true} if OPF mode is enabled, {@code false} otherwise.
	 */
	public boolean isFromOpf() {
		return fromOpf;
	}

	/**
	 * Sets whether metadata should be read from an OPF file.
	 *
	 * @param fromOpf {@code true} to read from OPF, {@code false} otherwise.
	 */
	public void setFromOpf(boolean fromOpf) {
		this.fromOpf = fromOpf;
	}

	/**
	 * Returns whether the cover should be extracted from the e-book.
	 *
	 * @return {@code true} if cover extraction is enabled, {@code false} otherwise.
	 */
	public boolean isGetCover() {
		return getCover;
	}

	/**
	 * Sets whether the cover should be extracted from the e-book.
	 *
	 * @param getCover {@code true} to extract the cover, {@code false} otherwise.
	 */
	public void setGetCover(boolean getCover) {
		this.getCover = getCover;
	}

	/**
	 * Returns whether the title-sort string should be auto-generated from the title.
	 *
	 * @return {@code true} if title-sort mode is enabled, {@code false} otherwise.
	 */
	public boolean isTitleSort() {
		return titleSort;
	}

	/**
	 * Sets whether the title-sort string should be auto-generated from the title.
	 *
	 * @param titleSort {@code true} to enable title-sort mode, {@code false} otherwise.
	 */
	public void setTitleSort(boolean titleSort) {
		this.titleSort = titleSort;
	}

}
