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
 * Tests for {@link DefaultLrf2lrsInvocationRequest}.
 */
public class DefaultLrf2lrsInvocationRequestTest {

    @Test
    public void shouldImplementLrf2lrsInvocationRequest() {
        DefaultLrf2lrsInvocationRequest request = new DefaultLrf2lrsInvocationRequest();
        assertTrue(request instanceof Lrf2lrsInvocationRequest);
    }

    @Test
    public void shouldDefaultDontOutputResourcesToFalse() {
        DefaultLrf2lrsInvocationRequest request = new DefaultLrf2lrsInvocationRequest();
        assertFalse(request.isDontOutputResources());
    }

    @Test
    public void shouldSetAndGetDontOutputResources() {
        DefaultLrf2lrsInvocationRequest request = new DefaultLrf2lrsInvocationRequest();
        InvocationRequest result = request.setDontOutputResources(true);
        assertTrue(request.isDontOutputResources());
        assertSame(request, result);
    }

    @Test
    public void shouldDefaultLrfFileToNull() {
        DefaultLrf2lrsInvocationRequest request = new DefaultLrf2lrsInvocationRequest();
        assertNull(request.getLrfFile());
    }

    @Test
    public void shouldSetAndGetLrfFile() {
        DefaultLrf2lrsInvocationRequest request = new DefaultLrf2lrsInvocationRequest();
        File lrfFile = new File("/tmp/book.lrf");
        InvocationRequest result = request.setLrfFile(lrfFile);
        assertEquals(lrfFile, request.getLrfFile());
        assertSame(request, result);
    }

    @Test
    public void shouldDefaultOutputDirectoryToNull() {
        DefaultLrf2lrsInvocationRequest request = new DefaultLrf2lrsInvocationRequest();
        assertNull(request.getOutputDirectory());
    }

    @Test
    public void shouldSetAndGetOutputDirectory() {
        DefaultLrf2lrsInvocationRequest request = new DefaultLrf2lrsInvocationRequest();
        File outputDir = new File("/tmp/output");
        InvocationRequest result = request.setOutputDirectory(outputDir);
        assertEquals(outputDir, request.getOutputDirectory());
        assertSame(request, result);
    }
}
