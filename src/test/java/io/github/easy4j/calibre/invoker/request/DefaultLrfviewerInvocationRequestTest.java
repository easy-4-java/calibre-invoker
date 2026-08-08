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

import java.io.File;

import org.junit.Test;

/**
 * Tests for {@link DefaultLrfviewerInvocationRequest}.
 */
public class DefaultLrfviewerInvocationRequestTest {

    @Test
    public void shouldImplementLrfviewerInvocationRequest() {
        DefaultLrfviewerInvocationRequest request = new DefaultLrfviewerInvocationRequest();
        assertTrue(request instanceof LrfviewerInvocationRequest);
    }

    @Test
    public void shouldDefaultDisableHyphenationToFalse() {
        DefaultLrfviewerInvocationRequest request = new DefaultLrfviewerInvocationRequest();
        assertFalse(request.isDisableHyphenation());
    }

    @Test
    public void shouldSetAndGetDisableHyphenation() {
        DefaultLrfviewerInvocationRequest request = new DefaultLrfviewerInvocationRequest();
        InvocationRequest result = request.setDisableHyphenation(true);
        assertTrue(request.isDisableHyphenation());
        assertSame(request, result);
    }

    @Test
    public void shouldDefaultProfileToFalse() {
        DefaultLrfviewerInvocationRequest request = new DefaultLrfviewerInvocationRequest();
        assertFalse(request.isProfile());
    }

    @Test
    public void shouldSetAndGetProfile() {
        DefaultLrfviewerInvocationRequest request = new DefaultLrfviewerInvocationRequest();
        InvocationRequest result = request.setProfile(true);
        assertTrue(request.isProfile());
        assertSame(request, result);
    }

    @Test
    public void shouldDefaultLrsFileToNull() {
        DefaultLrfviewerInvocationRequest request = new DefaultLrfviewerInvocationRequest();
        assertNull(request.getLrsFile());
    }

    @Test
    public void shouldSetAndGetLrsFile() {
        DefaultLrfviewerInvocationRequest request = new DefaultLrfviewerInvocationRequest();
        File lrsFile = new File("/tmp/book.lrs");
        InvocationRequest result = request.setLrsFile(lrsFile);
        assertEquals(lrsFile, request.getLrsFile());
        assertSame(request, result);
    }

    @Test
    public void shouldDefaultVisualDebugToFalse() {
        DefaultLrfviewerInvocationRequest request = new DefaultLrfviewerInvocationRequest();
        assertFalse(request.isVisualDebug());
    }

    @Test
    public void shouldSetAndGetVisualDebug() {
        DefaultLrfviewerInvocationRequest request = new DefaultLrfviewerInvocationRequest();
        InvocationRequest result = request.setVisualDebug(true);
        assertTrue(request.isVisualDebug());
        assertSame(request, result);
    }

    @Test
    public void shouldDefaultWhiteBackgroundToFalse() {
        DefaultLrfviewerInvocationRequest request = new DefaultLrfviewerInvocationRequest();
        assertFalse(request.isWhiteBackground());
    }

    @Test
    public void shouldSetAndGetWhiteBackground() {
        DefaultLrfviewerInvocationRequest request = new DefaultLrfviewerInvocationRequest();
        InvocationRequest result = request.setWhiteBackground(true);
        assertTrue(request.isWhiteBackground());
        assertSame(request, result);
    }
}
