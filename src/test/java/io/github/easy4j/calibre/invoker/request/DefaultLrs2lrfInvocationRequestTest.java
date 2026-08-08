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
 * Tests for {@link DefaultLrs2lrfInvocationRequest}.
 */
public class DefaultLrs2lrfInvocationRequestTest {

    @Test
    public void shouldImplementLrs2lrfInvocationRequest() {
        DefaultLrs2lrfInvocationRequest request = new DefaultLrs2lrfInvocationRequest();
        assertTrue(request instanceof Lrs2lrfInvocationRequest);
    }

    @Test
    public void shouldDefaultLrsToFalse() {
        DefaultLrs2lrfInvocationRequest request = new DefaultLrs2lrfInvocationRequest();
        assertFalse(request.isLrs());
    }

    @Test
    public void shouldSetAndGetLrs() {
        DefaultLrs2lrfInvocationRequest request = new DefaultLrs2lrfInvocationRequest();
        InvocationRequest result = request.setLrs(true);
        assertTrue(request.isLrs());
        assertSame(request, result);
    }

    @Test
    public void shouldDefaultLrsFileToNull() {
        DefaultLrs2lrfInvocationRequest request = new DefaultLrs2lrfInvocationRequest();
        assertNull(request.getLrsFile());
    }

    @Test
    public void shouldSetAndGetLrsFile() {
        DefaultLrs2lrfInvocationRequest request = new DefaultLrs2lrfInvocationRequest();
        File lrsFile = new File("/tmp/book.lrs");
        InvocationRequest result = request.setLrsFile(lrsFile);
        assertEquals(lrsFile, request.getLrsFile());
        assertSame(request, result);
    }

    @Test
    public void shouldDefaultOutputDirectoryToNull() {
        DefaultLrs2lrfInvocationRequest request = new DefaultLrs2lrfInvocationRequest();
        assertNull(request.getOutputDirectory());
    }

    @Test
    public void shouldSetAndGetOutputDirectory() {
        DefaultLrs2lrfInvocationRequest request = new DefaultLrs2lrfInvocationRequest();
        File outputDir = new File("/tmp/output");
        InvocationRequest result = request.setOutputDirectory(outputDir);
        assertEquals(outputDir, request.getOutputDirectory());
        assertSame(request, result);
    }
}
