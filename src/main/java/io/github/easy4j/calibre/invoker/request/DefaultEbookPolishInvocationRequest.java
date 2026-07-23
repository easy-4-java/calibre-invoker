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

/**
 * <p>Polishing books is all about putting the shine of perfection onto your carefully crafted e-books.</p>
 * <p>Polishing tries to minimize the changes to the internal code of your e-book. Unlike conversion, 
 * it does not flatten CSS, rename files, change font sizes, adjust margins, 
 * etc. Every action performs only the minimum set of changes needed for the desired effect.</p>
 * <p>You should use this tool as the last step in your e-book creation process.</p>
 * <p>Note that polishing only works on files in the AZW3 or EPUB formats.</p>
 * https://manual.calibre-ebook.com/generated/en/ebook-polish.html
 * @author ： <a href="https://github.com/hiwepy">hiwepy</a>
 */
public class DefaultEbookPolishInvocationRequest  extends AbstractInvocationRequest implements InvocationRequest {

	/**
	 * Losslessly compress images in the book, to reduce the filesize, without affecting image quality.
	 * Set the value of the {@code compress-images} {@code true} if the argument
	 * {@code --compress-images} was specified, otherwise {@code false}
	 */
	private boolean compressImages;
 
	/**
	 * <p>Embed all fonts that are referenced in the document and are not already embedded. 
	 * This will scan your computer for the fonts, and if they are found, they will be embedded into the document.
	 * Please ensure that you have the proper license for embedding the fonts used in this document. </p>
	 * Set the value of the {@code embed-fonts} {@code true} if the argument
	 * {@code --embed-fonts} was specified, otherwise {@code false}
	 */
	private boolean embedFonts;
	
	/**
	 * Remove a previous inserted book jacket page.
	 * Set the value of the {@code remove-jacket} {@code true} if the argument
	 * {@code --remove-jacket} was specified, otherwise {@code false}
	 */
	private boolean removeJacket;
	/**
	 * <p>Remove all unused CSS rules from stylesheets and style tags. 
	 * Some books created from production templates can have a large number of extra CSS rules that dont
	 * match any actual content. These extra rules can slow down readers that need to parse them all.</p>
	 * Set the value of the {@code remove-unused-css} {@code true} if the argument
	 * {@code --remove-unused-css} was specified, otherwise {@code false}
	 */
	private boolean removeUnusedCss;

	/**
	 * <p>Convert plain text dashes, ellipsis, quotes, multiple hyphens, 
	 * etc. into their typographically correct equivalents. 
	 * Note that the algorithm can sometimes generate incorrect results,  especially when single quotes at the start of contractions are involved.</p>
	 * Set the value of the {@code smarten-punctuation} {@code true} if the argument
	 * {@code --smarten-punctuation} was specified, otherwise {@code false}
	 */
	private boolean smartenPunctuation;
	
	/**
	 * <p>Subsetting fonts means reducing an embedded font to contain only the characters used from that font in the book. This greatly reduces the size of the font files (halving the font file sizes is common). For example, if the book uses a specific font for headers, then subsetting will reduce that font to contain only the characters present in the actual headers in the book. Or if the book embeds the bold and italic versions of a font, but bold and italic text is relatively rare, or absent altogether, then the bold and italic fonts can either be reduced to only a few characters or completely removed. The only downside to subsetting fonts is that if, at a later date you decide to add more text to your books, the newly added text might not be covered by the subset font.</p>
	 * Set the value of the {@code subset-fonts} {@code true} if the argument
	 * {@code --subset-fonts} was specified, otherwise {@code false}
	 */
	private boolean subsetFonts;
	
	/**
	 * Upgrade the internal structures of the book, if possible. For instance, upgrades EPUB 2 books to EPUB 3 books.
	 * Set the value of the {@code upgrade-book} {@code true} if the argument
	 * {@code --upgrade-book} was specified, otherwise {@code false}
	 */
	private boolean upgradeBook;

	public boolean isCompressImages() {
		return compressImages;
	}

	public void setCompressImages(boolean compressImages) {
		this.compressImages = compressImages;
	}

	public boolean isEmbedFonts() {
		return embedFonts;
	}

	public void setEmbedFonts(boolean embedFonts) {
		this.embedFonts = embedFonts;
	}

	public boolean isRemoveJacket() {
		return removeJacket;
	}

	public void setRemoveJacket(boolean removeJacket) {
		this.removeJacket = removeJacket;
	}

	public boolean isRemoveUnusedCss() {
		return removeUnusedCss;
	}

	public void setRemoveUnusedCss(boolean removeUnusedCss) {
		this.removeUnusedCss = removeUnusedCss;
	}

	public boolean isSmartenPunctuation() {
		return smartenPunctuation;
	}

	public void setSmartenPunctuation(boolean smartenPunctuation) {
		this.smartenPunctuation = smartenPunctuation;
	}

	public boolean isSubsetFonts() {
		return subsetFonts;
	}

	public void setSubsetFonts(boolean subsetFonts) {
		this.subsetFonts = subsetFonts;
	}

	public boolean isUpgradeBook() {
		return upgradeBook;
	}

	public void setUpgradeBook(boolean upgradeBook) {
		this.upgradeBook = upgradeBook;
	}
	
}
