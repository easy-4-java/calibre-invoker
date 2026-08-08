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

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for {@link DefaultEbookPolishInvocationRequest}.
 */
public class DefaultEbookPolishInvocationRequestTest {

    @Test
    public void shouldImplementInvocationRequest() {
        DefaultEbookPolishInvocationRequest request = new DefaultEbookPolishInvocationRequest();
        assertTrue(request instanceof InvocationRequest);
    }

    @Test
    public void shouldDefaultCompressImagesToFalse() {
        DefaultEbookPolishInvocationRequest request = new DefaultEbookPolishInvocationRequest();
        assertFalse(request.isCompressImages());
    }

    @Test
    public void shouldSetAndGetCompressImages() {
        DefaultEbookPolishInvocationRequest request = new DefaultEbookPolishInvocationRequest();
        request.setCompressImages(true);
        assertTrue(request.isCompressImages());
    }

    @Test
    public void shouldDefaultEmbedFontsToFalse() {
        DefaultEbookPolishInvocationRequest request = new DefaultEbookPolishInvocationRequest();
        assertFalse(request.isEmbedFonts());
    }

    @Test
    public void shouldSetAndGetEmbedFonts() {
        DefaultEbookPolishInvocationRequest request = new DefaultEbookPolishInvocationRequest();
        request.setEmbedFonts(true);
        assertTrue(request.isEmbedFonts());
    }

    @Test
    public void shouldDefaultRemoveJacketToFalse() {
        DefaultEbookPolishInvocationRequest request = new DefaultEbookPolishInvocationRequest();
        assertFalse(request.isRemoveJacket());
    }

    @Test
    public void shouldSetAndGetRemoveJacket() {
        DefaultEbookPolishInvocationRequest request = new DefaultEbookPolishInvocationRequest();
        request.setRemoveJacket(true);
        assertTrue(request.isRemoveJacket());
    }

    @Test
    public void shouldDefaultRemoveUnusedCssToFalse() {
        DefaultEbookPolishInvocationRequest request = new DefaultEbookPolishInvocationRequest();
        assertFalse(request.isRemoveUnusedCss());
    }

    @Test
    public void shouldSetAndGetRemoveUnusedCss() {
        DefaultEbookPolishInvocationRequest request = new DefaultEbookPolishInvocationRequest();
        request.setRemoveUnusedCss(true);
        assertTrue(request.isRemoveUnusedCss());
    }

    @Test
    public void shouldDefaultSmartenPunctuationToFalse() {
        DefaultEbookPolishInvocationRequest request = new DefaultEbookPolishInvocationRequest();
        assertFalse(request.isSmartenPunctuation());
    }

    @Test
    public void shouldSetAndGetSmartenPunctuation() {
        DefaultEbookPolishInvocationRequest request = new DefaultEbookPolishInvocationRequest();
        request.setSmartenPunctuation(true);
        assertTrue(request.isSmartenPunctuation());
    }

    @Test
    public void shouldDefaultSubsetFontsToFalse() {
        DefaultEbookPolishInvocationRequest request = new DefaultEbookPolishInvocationRequest();
        assertFalse(request.isSubsetFonts());
    }

    @Test
    public void shouldSetAndGetSubsetFonts() {
        DefaultEbookPolishInvocationRequest request = new DefaultEbookPolishInvocationRequest();
        request.setSubsetFonts(true);
        assertTrue(request.isSubsetFonts());
    }

    @Test
    public void shouldDefaultUpgradeBookToFalse() {
        DefaultEbookPolishInvocationRequest request = new DefaultEbookPolishInvocationRequest();
        assertFalse(request.isUpgradeBook());
    }

    @Test
    public void shouldSetAndGetUpgradeBook() {
        DefaultEbookPolishInvocationRequest request = new DefaultEbookPolishInvocationRequest();
        request.setUpgradeBook(true);
        assertTrue(request.isUpgradeBook());
    }
}
